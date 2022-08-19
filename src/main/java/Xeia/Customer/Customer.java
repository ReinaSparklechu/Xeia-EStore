package Xeia.Customer;

import Xeia.Items.Item;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Map;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Customer {
    @NonNull
    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z_]{9,32}", message = "Username must be at least 9 characters long, max of 32 characters")
    private String username;
    @NonNull
    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z_]{9,32}", message ="password must be at least 9 characters long, max of 32 characters" )
    private String password;
    private Map<Item, Integer> inventory;
    private Map<Item, Integer> shoppingCart;
    private long userId;

    public Customer(String userName, long userId) {
        this.username = userName;
        this.userId = userId;
    }
}
