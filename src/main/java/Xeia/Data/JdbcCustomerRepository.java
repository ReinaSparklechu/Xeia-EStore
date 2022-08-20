package Xeia.Data;

import Xeia.Customer.Customer;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Repository
public class JdbcCustomerRepository implements CustomerRepository {
    private JdbcTemplate jdbc;
    private Firestore db;
    @Autowired
    public JdbcCustomerRepository(JdbcTemplate jdbc) throws IOException, ExecutionException, InterruptedException {
        this.jdbc = jdbc;

    }

    //if customer is not authenticated it will return a null object
    @Override
    public Customer loginCustomer(String name, String passHash) {
        try{
            String auth = jdbc.queryForObject("Select passwordHash from Customer where username = \'" + name +"\'", String.class);
            System.out.println("queried");
            System.out.println("queried hash: " + auth + "\nGiven hash: " + passHash);
            if(auth.equals(passHash)) {
                System.out.println("authenticating");
                Customer authenticated = jdbc.queryForObject("select username, userid from Customer where username = ?" , this::mapRowToCustomer, name);
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
        System.out.println("Bytes for password: " + newCustomer.getPassword() +" is: " + passwordHash);
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
        System.out.println("Signup complete for: " + newCustomer);
    }
    private Customer mapRowToCustomer(ResultSet rs, int rowNum)throws SQLException {
        return new Customer(rs.getString("userName"), rs.getLong("userId"));
    }
}
