package Xeia.Web;

import Xeia.Customer.Customer;
import Xeia.Data.CustomerRepository;
import Xeia.Data.JdbcCustomerRepository;
import Xeia.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;
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
    @Autowired
    CustomerService customerService;
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
        model.addAttribute("formError", false);
        return "login";
    }

    @GetMapping("process")
    public String process(Model model, HttpServletResponse response, HttpServletRequest request) {
        System.out.println("In process");
        Customer customer = customerRepo.findCustomer((String) request.getSession().getAttribute("custName"));
        customerService.loginCustomer(customer);
        System.out.println(customer);
        model.addAttribute("customer", customer);
        return "redirect:/store";
    }
    @GetMapping("failure")
    public String failure(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException  {
        System.out.println("Has Error");
        model.addAttribute("formError" , true);
        return"login";
    }


   //todo bind error messages and return it to user
    @PostMapping
    public String authenticate(Model model, @AuthenticationPrincipal Customer customer, BindingResult result, HttpServletResponse response) throws NoSuchAlgorithmException{
        System.out.println("Authenticating getting pass");
        String pass = customer.getPassword();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] hashed = md5.digest(pass.getBytes(StandardCharsets.UTF_8));
        String hashedString = convertToHex(hashed);
        System.out.println("Authenticating");
        Customer auth = customerRepo.loginCustomer(customer.getUsername(), hashedString);
        System.out.println("authenticated");
        if(auth ==null) {
            System.out.println("Has Error");
            result.addError(new ObjectError("AccountNotFound", "Either Username or password was wrong"));
            model.addAttribute("customer", customer);
            return"login";
        } else {
            System.out.println("Auth no error");
            model.addAttribute("customer", auth);
        }
        return "redirect:/store";
    }
}
