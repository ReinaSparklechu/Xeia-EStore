package Xeia.Data;

import Xeia.Customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    @Override
    public void signUpCustomer(Customer newCustomer) {

    }
    private Customer mapRowToCustomer(ResultSet rs, int rowNum)throws SQLException {
        return new Customer(rs.getString("userName"), rs.getLong("userId"));
    }
}
