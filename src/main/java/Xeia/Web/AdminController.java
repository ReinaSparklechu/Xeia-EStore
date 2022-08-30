package Xeia.Web;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    //todo: admin should be able to add items, view customer inventory, edit customer inventory.
    @GetMapping
    public String admin() {
        return "admin";
    }
}
