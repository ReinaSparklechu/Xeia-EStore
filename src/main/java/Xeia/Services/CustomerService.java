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
        List<Item> cartItems = cartBuffer.keySet().stream().toList();
        System.out.println("Inv buffer: " +invBuffer);
        System.out.println("Cart buffer: " + cartBuffer);
        System.out.println("starting processing");
        boolean test = invBuffer.containsKey(cartItems.get(0));
        for(Map.Entry<Item, Integer> pair: cartBuffer.entrySet()) {
            if(invBuffer.keySet().stream().toList().contains(pair.getKey())) {

                //use invbuffer for each workaround
                for(Map.Entry<Item, Integer> invPair: invBuffer.entrySet()) {
                    if(invPair.getKey().equals(pair.getKey())) {
                        invBuffer.replace(invPair.getKey(),invPair.getValue() + pair.getValue());
                    }
                }
            } else {
                invBuffer.put(pair.getKey(), pair.getValue());
            }
            String owner = pair.getKey().getOwner();

            //query the whole mapping from item owner
            int ownerQuantity = itemRepo.getEntryQuantityById(owner, pair.getKey());
            int cartQuantity = cartBuffer.get(pair.getKey());
            Logger.getLogger("Global").log(Level.FINE, "Owner q is " + ownerQuantity + " and cart Q is " + cartQuantity);
            //find the current quantity

            //subtract using pair.getvalue

            //update item owner
            itemRepo.updateEntryForId(pair.getKey().getOwner(), pair.getKey(), ownerQuantity - cartQuantity);
        }
        System.out.println("Inv buffer: " +invBuffer);
        System.out.println("Cart buffer: " + cartBuffer);
        // before clearing, get each item's entry in from the db using item.owner, update quantity of items
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
