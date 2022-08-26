package Xeia.Data;

import Xeia.Customer.Customer;

import java.security.NoSuchAlgorithmException;

public interface CustomerRepository {

    public Customer loginCustomer(String name, String passHash);
    public void signUpCustomer(Customer newCustomer) throws NoSuchAlgorithmException;

    void updateCart(Customer c);
    void updateInventory(Customer c);

    void updateFund(Customer c);

    Customer findCustomer(String username);
}
