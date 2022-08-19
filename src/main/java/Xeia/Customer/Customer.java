package Xeia.Customer;

import Xeia.Items.Item;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


import java.util.Map;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Customer {
    @NonNull
    private String username;
    @NonNull
    private String password;
    private Map<Item, Integer> inventory;
    private Map<Item, Integer> shoppingCart;
    private long userId;

    public Customer(String userName, long userId) {
        this.username = userName;
        this.userId = userId;
    }
}
