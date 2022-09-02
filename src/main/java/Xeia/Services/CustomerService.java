package Xeia.Services;

import Xeia.Customer.Customer;
import Xeia.Customer.Role;
import Xeia.Data.CustomerRepository;
import Xeia.Data.ItemRepository;
import Xeia.Data.RoleRepository;
import Xeia.Items.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CustomerService {

    CustomerRepository custRepo;
    ItemRepository itemRepo;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    public CustomerService(CustomerRepository custRepo, ItemRepository itemRepo) {
        this.custRepo = custRepo;
        this.itemRepo = itemRepo;
    }

    public void checkoutCart(Customer c) {
        Map<Item, Integer> cartBuffer = c.getShoppingCart();
        Map<Item, Integer> invBuffer = c.getInventory();
        int totalCost = 0;
        for (Map.Entry<Item, Integer> e: cartBuffer.entrySet()) {
            int storeQty = itemRepo.getEntryQuantityById(e.getKey().getOwner(), e.getKey());
            Logger.getGlobal().info("Current item store qty is " + storeQty);
            int cartQty = e.getValue();
            if(storeQty >= cartQty) {
                Logger.getLogger("global").info("Selling " + cartBuffer + " of " + e.getKey().getName());
                invBuffer.put(e.getKey(), invBuffer.getOrDefault(e.getKey(),0) + e.getValue());
                totalCost += e.getKey().getPrice() *e.getValue();
                Logger.getGlobal().info("Updating itemRepo values");
                itemRepo.updateEntryForId(e.getKey().getOwner(), e.getKey(), storeQty-cartQty);
                Logger.getGlobal().info("New entry quantity for: " + e.getKey().getName() + " is now " + itemRepo.getEntryQuantityById(e.getKey().getOwner(),e.getKey()));
            } else {
                Logger.getLogger("global").warning("Customer had more copies of items in cart than allowed sale not made for item: " + e.getKey());
            }
        }
        c.setFunds(c.getFunds() - totalCost);
        cartBuffer.clear();
        c.setInventory(invBuffer);
        c.setShoppingCart(cartBuffer);



        //insert into database table Customer Inventory
        //update store_item


    }

    public void updateCustomer(Customer c) {
        System.out.println("Updating customer: " + c);
        custRepo.updateCart(c);
        custRepo.updateInventory(c);
        custRepo.updateFund(c);

    }
    public void registerCustomer(Customer c) throws NoSuchAlgorithmException {
        custRepo.signUpCustomer(c);
        Role userRole = roleRepository.getRoleByName("USER");
        roleRepository.giveCustomerRole(c, Arrays.asList(userRole));

    }
    public void loginCustomer(Customer c) {
        //todo: update so that all details go through serivce
        c.setInventory(custRepo.getCustomerInventoryById(c.getUserId()));
        c.setShoppingCart(custRepo.getCustomerShoppingCartById(c.getUserId()));

    }
}
