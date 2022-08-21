package Xeia.Services;

import Xeia.Customer.Customer;
import Xeia.Data.CustomerRepository;
import Xeia.Data.ItemRepository;
import Xeia.Items.Item;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerService {

    CustomerRepository custRepo;
    ItemRepository itemRepo;
    public CustomerService() {
        System.out.println("created service bean");
    }
    //todo
    public void checkoutCart(Customer c) {
        Map<Item, Integer> buffer = new HashMap<>();
        c.getShoppingCart().forEach(((item, integer) -> {
            buffer.put(item,integer);
            //insert into database table Customer Inventory
            //update store_item
        }));

    }
}
