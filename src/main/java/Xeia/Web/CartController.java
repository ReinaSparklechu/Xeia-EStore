package Xeia.Web;

import Xeia.Customer.Customer;
import Xeia.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
@SessionAttributes("customer")
public class CartController {
    @Autowired
    CustomerService customerService;

    @GetMapping
    public String viewCart(Model model, @ModelAttribute("customer") Customer customer){
        model.addAttribute("customer", customer);
        return "viewCart";
    }
    @PostMapping("/update")
    public String updateCart(Model model, @ModelAttribute("customer") Customer customer) {
        customer.updateCart();
        System.out.println(customer);
        model.addAttribute("customer" , customer);
        customerService.updateCustomer(customer);
        return "redirect:/store";
    }
}
