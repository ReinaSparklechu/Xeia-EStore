package Xeia.Customer;

import lombok.Data;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegistrationForm {
    @NotBlank(message = "Please provide a username")
    @Size(min = 9)
    @Pattern(regexp = "^[0-9a-zA-Z]{9,32}", message = "Username must be at least 9 characters long, max of 32 characters")
    private String username;

    @NotBlank(message = "Please provide a password")
    @Size(min = 9)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[0-9a-zA-Z]{9,32}", message = "Password must contain one lowercase letter, uppercase letter and a number")
    private String password;


    private boolean nameAlreadyExists = false;


    public Customer toCustomer() {
        var cus = new Customer();
        cus.setUsername(username);
        cus.setPassword(password);
        return cus;
    }
}
