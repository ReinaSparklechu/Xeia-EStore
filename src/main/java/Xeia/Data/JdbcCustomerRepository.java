package Xeia.Data;

import Xeia.Customer.Customer;
import Xeia.Customer.Role;
import Xeia.Items.Consumable;
import Xeia.Items.Equipment;
import Xeia.Items.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

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
import java.util.logging.Logger;


@Repository
public class JdbcCustomerRepository implements CustomerRepository {
    private JdbcTemplate jdbc;
    @Autowired
    RoleRepository roleRepository;

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
                Customer authenticated = jdbc.queryForObject("select username, passwordHash,userid, funds from Customer where username = ?" , this::mapRowToCustomer, name);
                List<Role> roles = roleRepository.getRoleByCustomerId(authenticated);
                authenticated.setRoles(roles);
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

    @Override
    public Customer findCustomer(String username) {
        Customer authenticated = jdbc.queryForObject("select username, passwordHash, userid, funds from Customer where username = ?" , this::mapRowToCustomer, username);
        if(authenticated != null) {
            List<Role> roles = roleRepository.getRoleByCustomerId(authenticated);
            authenticated.setRoles(roles);
            System.out.println(authenticated);
            return authenticated;
        }
        throw new UsernameNotFoundException("User :" + username + " not found");
    }
    //todo: implement
    @Override
    public Map<Item, Integer> getCustomerInventoryById(long custId) {
        List<Map.Entry<Item, Integer>> inventory = new ArrayList<>();
        Map<Item,Integer> invMap = new TreeMap<>(Comparator.comparing(Item::getName));
        inventory = jdbc.query("select i.name, i.price, i.quality, i.effect, i.isConsumable, i.isEquipment,i.itemLvl, i.isEnchantable, i.enchantment,s.quantity from Items i, Item_Owner s where s.cust_id = ? and i.name = s.item_name",
                (rs, rowNum) -> {
                    if(rs.getBoolean("isConsumable")) {
                        return Map.entry(
                                new Consumable(rs.getString("name"), rs.getInt("price"), rs.getInt("quality"), rs.getString("effect"))
                                , rs.getInt("quantity"));
                    } else if(rs.getBoolean("isEquipment")) {
                        return Map.entry( new Equipment(rs.getString("name"), rs.getInt("price"), rs.getInt("itemLvl"), rs.getBoolean("isEnchantable"), rs.getString("enchantment")),
                                rs.getInt("quantity"));
                    } else {
                        return null;
                    }

        },custId);
        for(Map.Entry e : inventory) {
            var i = (Item) e.getKey();
            var q = (Integer) e.getValue();
            i.setOwner(String.valueOf(custId));
            invMap.put(i,q);

        }
        return invMap;
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
    public long signUpCustomer(Customer newCustomer) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(newCustomer.getPassword().getBytes(StandardCharsets.UTF_8));
        String passwordHash = convertToHex(md.digest());
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory("insert into Customer (username, passwordHash,  funds) values (?,?,?)", Types.VARCHAR,Types.VARCHAR, Types.INTEGER);
        pscf.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        newCustomer.getUsername()
                        , passwordHash,newCustomer.getFunds()));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(psc,kh);
        newCustomer.setUserId(kh.getKey().longValue());
        return newCustomer.getUserId();
    }
    private Customer mapRowToCustomer(ResultSet rs, int rowNum)throws SQLException {
        return new Customer(rs.getString("userName"), rs.getLong("userId"), rs.getInt("funds"), rs.getString("passwordHash"));
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
        PreparedStatementCreatorFactory pscfCheck = new PreparedStatementCreatorFactory("select Item_Name from Item_Owner where cust_id = ?", Types.BIGINT);
        PreparedStatementCreatorFactory pscfInsert = new PreparedStatementCreatorFactory("insert into Item_Owner(cust_id, Item_name, Quantity) values ( ?,?,? )", Types.BIGINT, Types.VARCHAR, Types.INTEGER);
        PreparedStatementCreatorFactory pscfUpdate = new PreparedStatementCreatorFactory("update Item_Owner set Quantity = ? where cust_id = " + customer.getUserId() + " and Item_name = ?", Types.INTEGER, Types.VARCHAR);
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
                jdbc.update("delete from Item_Owner where Item_Name = ? and Cust_id = ?", itemName,customer.getUserId());
            }

        }
    }

    @Override
    public void updateFund(Customer c) {
        jdbc.update("Update customer set funds = ? where userId = \'" + c.getUserId() + "\'", c.getFunds());

    }
}
