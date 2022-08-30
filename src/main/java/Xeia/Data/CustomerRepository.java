package Xeia.Data;

import Xeia.Customer.Customer;
import Xeia.Items.Item;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface CustomerRepository {

    public Customer loginCustomer(String name, String passHash);
    public long signUpCustomer(Customer newCustomer) throws NoSuchAlgorithmException;

    void updateCart(Customer c);
    void updateInventory(Customer c);

    void updateFund(Customer c);

    Customer findCustomer(String username);

    Map<Item, Integer> getCustomerInventoryById(long custId);
}
