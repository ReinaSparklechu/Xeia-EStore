package Xeia.Customer;

import Xeia.Items.Item;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@NoArgsConstructor

public class Customer {
    // TODO: 20/8/2022 insert funds and implement into db 
    @NonNull
    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z_]{9,32}", message = "Username must be at least 9 characters long, max of 32 characters")
    private String username;
    @NonNull
    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z_]{9,32}", message ="password must be at least 9 characters long, max of 32 characters" )
    private String password;
    private Map<Item, Integer> inventory = new HashMap<>();
    private Map<Item, Integer> shoppingCart = new HashMap<>();
    private  Map<String, Integer> cartBuffer = new HashMap<>();
    private long userId;

    private String lastVisited;

    public Customer(String userName, long userId) {
        this.username = userName;
        this.userId = userId;
    }

    public void consolidateCart(Map<Item,Integer> inventory) {
        List<Item> itemList = new ArrayList<>();
        Item[] keyset = inventory.keySet().toArray(new Item[0]);
        System.out.println("Buffer contains" + cartBuffer);
        for(String s: cartBuffer.keySet()) {
            if(cartBuffer.get(s) != null) {
                for (Item i : keyset) {
                    if (i.getName().equals(s)) {

                        System.out.println("Adding to cart item:" + i);
                        addToCart(i, cartBuffer.get(s));
                    }
                }
            }
        }
        cartBuffer.clear();
    }
    //method to add item into cart, if item exists, increment by 1 else, enter new entry.
    public void addToCart(Item i, int j) {
        List<Item> keylist = shoppingCart.keySet().stream().toList();
        if(keylist.contains(i)) {
            shoppingCart.forEach((item, integer) -> {
                if(item.getName().equals(i.getName())){
                    shoppingCart.replace(item, integer +j);
                }
            });
            System.out.println("replaced");
        } else {
            shoppingCart.put(i,j);
        }
    }
}
