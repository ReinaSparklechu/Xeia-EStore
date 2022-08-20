package Xeia.Web;

import Xeia.Customer.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/")
@SessionAttributes("customer")
public class HomeController {

    @ModelAttribute("customer")
    public Customer customer() {
        return new Customer();
    }
    @GetMapping
    public String home(){
        return "home";
    }



}
