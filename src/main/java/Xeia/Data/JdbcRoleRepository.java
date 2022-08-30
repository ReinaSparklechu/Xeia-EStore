package Xeia.Data;

import Xeia.Customer.Customer;
import Xeia.Customer.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcRoleRepository implements RoleRepository{
    @Autowired
    JdbcTemplate jdbc;
    @Override
    public List<Role> getRoleByCustomerId(Customer c) {
        return jdbc.query("select r.role_name, r.role_id from Role r , Role_Customer rc, Customer c where c.userId = ? and c.userId = rc.CustomerId and r.Role_id = rc.Role_id", (rs, rowNum) -> new Role(rs.getString("Role_name"), rs.getLong("role_id")), c.getUserId());
    }
    public void giveCustomerRole(Customer c, List<Role> roles) {
        for(Role r: roles) {
            jdbc.update("insert into Role_Customer(role_id, customerId) values(?,?)" ,r.getId(), c.getUserId());
        }
    }

    @Override
    public Role getRoleByName(String name) {
        return jdbc.queryForObject("select role_name, role_id from Role where role_name = ? ", (rs, rowNum) -> new Role(rs.getString("role_name"), rs.getLong("role_id")), name);
    }
}
