package Xeia.Data;

import Xeia.Customer.Customer;

public interface CustomerRepository {

    public Customer loginCustomer(String name, String passHash);
    public void signUpCustomer(Customer newCustomer);
}
