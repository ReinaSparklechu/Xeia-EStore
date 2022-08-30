package Xeia.Web;

import Xeia.Customer.Customer;
import Xeia.Data.CustomerRepository;
import Xeia.Data.JdbcCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/login")
@SessionAttributes("customer")
public class loginController {

    @Autowired
    CustomerRepository customerRepo;
    private String convertToHex(final byte[] messageDigest) {
        BigInteger bigint = new BigInteger(1, messageDigest);
        String hexText = bigint.toString(16);
        while (hexText.length() < 32) {
            hexText = "0".concat(hexText);
        }
        return hexText;
    }

    @ModelAttribute("customer")
    public Customer customer() {
        return new Customer();
    }

    @GetMapping
    public String login(Model model, @ModelAttribute("customer")Customer customer)
    {
        model.addAttribute("customer", customer);
        return "login";
    }

    //todo bind error messages and return it to user
    @PostMapping
    public String authenticate(Model model, Customer customer, BindingResult result, HttpServletResponse response) throws NoSuchAlgorithmException, IOException {
        String pass = customer.getPassword();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] hashed = md5.digest(pass.getBytes(StandardCharsets.UTF_8));
        String hashedString = convertToHex(hashed);
        Customer auth = customerRepo.loginCustomer(customer.getUsername(), hashedString);
        if(auth ==null) {
            result.addError(new ObjectError("AccountNotFound", "Either Username or password was wrong"));
            model.addAttribute("customer", customer);
            return"login";
        } else {
            model.addAttribute("customer", auth);
        }
        return "redirect:/store";
    }
}
