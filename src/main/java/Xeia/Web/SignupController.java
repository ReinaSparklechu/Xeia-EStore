package Xeia.Web;

import Xeia.Customer.Customer;
import Xeia.Data.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/signup")
public class SignupController {
    @Autowired
    CustomerRepository customerRepo;
    @GetMapping
    public String signup(Model model, @ModelAttribute("Customer")Customer customer) throws NoSuchAlgorithmException {
        Customer tests = new Customer("CustomerName", "RandomCustomer");
        customerRepo.signUpCustomer(tests);
        model.addAttribute("customer" , customer);
        return "signup";
    }
    @PostMapping
    public String completeSignup(Model model, @ModelAttribute("Customer") Customer customer, Errors errors) {
        if(errors.hasErrors()) {
            return "/signup";
        } else {
            try{
                customerRepo.signUpCustomer(customer);
                return "redirect:/";
            } catch (Exception e) {
                System.out.println(e);
                return" signup";
            }

        }


    }
}
