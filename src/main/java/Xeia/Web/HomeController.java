package Xeia.Web;

import Xeia.Customer.Customer;
import Xeia.Services.CustomerService;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/")
@SessionAttributes("customer")
public class HomeController {

    CustomerService customerService;
    @ModelAttribute("customer")
    public Customer customer() {
        Customer guest = new Customer();
        guest.setUsername("guest");
        return guest;
    }

    @GetMapping
    public String home(Model model, Customer customer) {

        return "home";
    }

    @PostMapping("/logout")
    public String logout(Model model, @ModelAttribute("customer") Customer customer, SessionStatus session){
        customerService.updateCustomer(customer);
        customer = new Customer();
        customer.setUsername("Guest");
        model.addAttribute("customer", customer);
        session.setComplete();
        return "redirect:/";


    }
    @GetMapping("cart")
    public String viewCart(Model model, @ModelAttribute("customer") Customer customer){
        model.addAttribute("customer", customer);
        return "viewCart";
    }



}
