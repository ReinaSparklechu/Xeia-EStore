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
    private String password;
    private Map<Item, Integer> inventory;
    private Map<Item, Integer> shoppingCart;
    @NonNull
    private long userId;
}
