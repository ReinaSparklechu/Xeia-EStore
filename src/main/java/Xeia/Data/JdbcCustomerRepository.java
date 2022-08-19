package Xeia.Data;

import Xeia.Customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

@Repository
public class JdbcCustomerRepository implements CustomerRepository {
    private JdbcTemplate jdbc;
    @Autowired
    public JdbcCustomerRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    @Override
    public Customer loginCustomer(String name, String passHash) {
        return null;
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
        System.out.println(kh.getKey().longValue());
        jdbc.update("update Customer set userId = " + kh.getKey().longValue() + " where username = \'" + newCustomer.getUsername() + "\' and userId = null");
        newCustomer.setUserId(kh.getKey().longValue());
        System.out.println("Signup complete for: " + newCustomer);
    }
    private Customer mapRowToCustomer(ResultSet rs, int rowNum)throws SQLException {
        return new Customer(rs.getString("userName"), rs.getLong("userId"));
    }
}
