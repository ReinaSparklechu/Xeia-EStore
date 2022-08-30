package Xeia.Customer;

import Xeia.Items.Item;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor

public class Customer implements UserDetails {

    private String username;

    private String password;

    private Map<Item, Integer> inventory = new HashMap<>();
    private Map<Item, Integer> shoppingCart = new TreeMap<>(Comparator.comparing(Item::getName));
    private  Map<String, Integer> cartBuffer = new HashMap<>();
    private long userId;

    private String lastVisited;
    private int funds;

    public Customer(String userName, long userId, int funds) {
        this.username = userName;
        this.userId = userId;
        this.funds = funds;
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
        Map<Item,Integer> cart = getShoppingCart();
        Item[] cartItems = new Item[cart.keySet().size()];
        cart.keySet().toArray(cartItems);
        Map<Item, Integer> sorted = new TreeMap<>(Comparator.comparing(Item::getName));
        for (Item i: cartItems) {
            sorted.put(i,cart.get(i));

        }
        this.shoppingCart = sorted;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("User"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
