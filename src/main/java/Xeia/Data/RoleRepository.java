package Xeia.Data;

import Xeia.Customer.Customer;
import Xeia.Customer.Role;

import java.util.List;

public interface RoleRepository {
    List<Role> getRoleByCustomerId(Customer c);

    //todo
    Role getRoleByName(String name);
    void giveCustomerRole(Customer c, List<Role> roles);
}
