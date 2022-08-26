package Xeia.Web;

import Xeia.Customer.Customer;
import Xeia.Services.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String home(){
        return "home";
    }

    @PostMapping("/logout")
    public String logout(Model model, @ModelAttribute("customer") Customer customer){
        customerService.updateCustomer(customer);
        customer = new Customer();
        customer.setUsername("Guest");
        return "redirect:/";


    }
    @GetMapping("cart")
    public String viewCart(Model model, @ModelAttribute("customer") Customer customer){
        model.addAttribute("customer", customer);
        return "viewCart";
    }



}
