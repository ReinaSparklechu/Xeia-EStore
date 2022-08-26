package Xeia.Data;

import Xeia.Customer.Customer;
import Xeia.Items.Item;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.concurrent.ExecutionException;


@Repository
public class JdbcCustomerRepository implements CustomerRepository {
    private JdbcTemplate jdbc;
    @Autowired
    public JdbcCustomerRepository(JdbcTemplate jdbc) throws IOException, ExecutionException, InterruptedException {
        this.jdbc = jdbc;

    }

    //if customer is not authenticated it will return a null object
    @Override
    public Customer loginCustomer(String name, String passHash) {
        try{
            String auth = jdbc.queryForObject("Select passwordHash from Customer where username = \'" + name +"\'", String.class);
            if(auth.equals(passHash)) {
                System.out.println("authenticating");
                Customer authenticated = jdbc.queryForObject("select username, userid, funds from Customer where username = ?" , this::mapRowToCustomer, name);
                System.out.println("queried customer: " + authenticated);
                return authenticated;

            } else {
                return null;
            }
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Account was not found");
            return null;
        }

    }



    private String convertToHex(final byte[] messageDigest) {
        BigInteger bigint = new BigInteger(1, messageDigest);
        String hexText = bigint.toString(16);
        while (hexText.length() < 32) {
            hexText = "0".concat(hexText);
        }
        return hexText;
    }

    @Override
    public void signUpCustomer(Customer newCustomer) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(newCustomer.getPassword().getBytes(StandardCharsets.UTF_8));
        String passwordHash = convertToHex(md.digest());
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory("insert into Customer (username, passwordHash) values (?,?)", Types.VARCHAR,Types.VARCHAR);
        pscf.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        newCustomer.getUsername()
                        , passwordHash));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(psc,kh);
        jdbc.update("update Customer set userId = " + kh.getKey().longValue() + " where username = \'" + newCustomer.getUsername() + "\' and userId = null");
        newCustomer.setUserId(kh.getKey().longValue());
    }
    private Customer mapRowToCustomer(ResultSet rs, int rowNum)throws SQLException {
        return new Customer(rs.getString("userName"), rs.getLong("userId"), rs.getInt("funds"));
    }


    public void updateCart(Customer customer) {

        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory("insert into Customer_Cart(userId, Item_name, Quantity) values ( ?,?,? )", Types.BIGINT, Types.VARCHAR, Types.INTEGER);
        List<String> dbCart = jdbc.query("select item_name from Customer_Cart where userId = " +customer.getUserId(),this::mapInv);
        //process shopping cart and update db
        customer.getShoppingCart().forEach(((item, integer) -> {
            PreparedStatementCreator psc = pscf.newPreparedStatementCreator(Arrays.asList(customer.getUserId(),item.getName(),integer.intValue()));
            jdbc.update(psc);
            dbCart.remove(item);
        }));
        if(!dbCart.isEmpty()) {
            for (String itemName: dbCart) {
                jdbc.update("delete from Customer_Cart where Item_Name = \'" + itemName +"\'");
            }

        }

    }
    private String mapInv(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("Item_Name");
    }
    public void updateInventory(Customer customer){
        List<String> dbInv;
        PreparedStatementCreatorFactory pscfCheck = new PreparedStatementCreatorFactory("select Item_Name from Customer_Inventory where userId = ?", Types.BIGINT);
        PreparedStatementCreatorFactory pscfInsert = new PreparedStatementCreatorFactory("insert into Customer_Inventory(userId, Item_name, Quantity) values ( ?,?,? )", Types.BIGINT, Types.VARCHAR, Types.INTEGER);
        PreparedStatementCreatorFactory pscfUpdate = new PreparedStatementCreatorFactory("update Customer_Inventory set Quantity = ? where userId = " + customer.getUserId() + " and Item_name = ?", Types.INTEGER, Types.VARCHAR);
        //get list of current items in cust db inventory
        PreparedStatementCreator psc= pscfCheck.newPreparedStatementCreator(Arrays.asList(customer.getUserId()));
        dbInv = jdbc.query(psc,this::mapInv);

        //go through customer inventory and update database accordingly.
        customer.getInventory().forEach((item, integer) -> {
            if(dbInv.contains(item.getName())) {
                PreparedStatementCreator update = pscfUpdate.newPreparedStatementCreator(Arrays.asList(integer.intValue(),item.getName()));
                jdbc.update(update);
            } else {
                PreparedStatementCreator insert = pscfInsert.newPreparedStatementCreator(Arrays.asList(customer.getUserId(),item.getName(),integer));
                jdbc.update(insert);
            }
            dbInv.remove(item.getName());
        });
        if(!dbInv.isEmpty()) {
            //delete from db
            for (String itemName: dbInv) {
                jdbc.update("delete from Customer_Inventory where Item_Name = \'" + itemName +"\'");
            }

        }
    }

    @Override
    public void updateFund(Customer c) {
        jdbc.update("Update customer set funds = ? where userId = \'" + c.getUserId() + "\'", c.getFunds());

    }
}
