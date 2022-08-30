package Xeia.Web;

import Xeia.Customer.Customer;
import Xeia.Customer.RegistrationForm;
import Xeia.Data.CustomerRepository;
import Xeia.Security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/signup")
public class SignupController {
    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    PasswordEncoder passwordEncoder;
    @ModelAttribute("registrationForm")
    public RegistrationForm registrationForm(){
        return new RegistrationForm();
    }
    @GetMapping
    public String signup(Model model, @ModelAttribute("registrationForm")RegistrationForm registrationForm){
        model.addAttribute("registrationForm" , registrationForm);
        return "signup";
    }

    @PostMapping
    public String completeSignup(Model model, @Valid @ModelAttribute("registrationForm") RegistrationForm registrationForm, Errors errors, HttpServletResponse response) throws NoSuchAlgorithmException {
        registrationForm.setNameAlreadyExists(false);
        if(errors.hasErrors()) {
            System.out.println("has errors");
            model.addAttribute("registrationForm", registrationForm);
            return "signup";
        }
        try{
            // since customer repo only throws exception when there is no username found, we catch the exception and use it as the case to add a new customer to the repository
            var existing = customerRepo.findCustomer(registrationForm.getUsername());
            registrationForm.setNameAlreadyExists(true);
            model.addAttribute("registrationForm", registrationForm);
            System.out.println("returning form");
            return "signup";
        }catch(EmptyResultDataAccessException f) {
            System.out.println("caught: " + f);
            var newcust = registrationForm.toCustomer();
            newcust.setFunds(1000);
            customerRepo.signUpCustomer(newcust);
            System.out.println("New customer signed up!");
            return"redirect:/login";
        }


    }
}
