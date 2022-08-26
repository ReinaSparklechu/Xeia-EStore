package Xeia.Services;

import Xeia.Customer.Customer;
import Xeia.Data.CustomerRepository;
import Xeia.Data.ItemRepository;
import Xeia.Items.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    CustomerRepository custRepo;
    ItemRepository itemRepo;
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
        }
        System.out.println("Inv buffer: " +invBuffer);
        System.out.println("Cart buffer: " + cartBuffer);
        cartBuffer.clear();
        c.setInventory(invBuffer);
        c.setShoppingCart(cartBuffer);



        //insert into database table Customer Inventory
        //update store_item


    }

    public void updateCustomer(Customer c) {
        custRepo.updateCart(c);
        custRepo.updateInventory(c);
        custRepo.updateFund(c);

    }
}
