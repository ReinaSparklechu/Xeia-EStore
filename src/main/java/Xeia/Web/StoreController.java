package Xeia.Web;

import Xeia.Customer.Customer;
import Xeia.Data.ItemRepository;
import Xeia.Items.Equipment;
import Xeia.Items.Item;
import Xeia.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/store")
@SessionAttributes("customer")
public class StoreController {
    // TODO: 20/8/2022 sort store page listings so that items should be displayed by alphabetical order 

    ItemRepository itemRepository;

    CustomerService customerService;

    @Autowired
    StoreController(ItemRepository itemRepository, CustomerService customerService){
        this.itemRepository = itemRepository;
        this.customerService = customerService;
        Equipment equipment = new Equipment();
    }

    @GetMapping
    public String store(@ModelAttribute("customer") Customer customer) {
        return "store";
    }
    @GetMapping("/smithy")
    public String smithy(Model model, @ModelAttribute("customer")Customer customer){
        Map<Item, Integer> inventory = itemRepository.loadInventory("smithy");
        customer.setLastVisited("smithy");
        model.addAttribute("inventory", inventory);
        model.addAttribute("storeName", "Smithy");
        return "storePage";
    }

    @GetMapping("/apothecary")
    public String apothecary(Model model,  @ModelAttribute("customer")Customer customer) {
        Map<Item, Integer> inventory = itemRepository.loadInventory("apothecary");
        customer.setLastVisited("apothecary");
        model.addAttribute("inventory", inventory);
        model.addAttribute("storeName", "Apothecary");
        return "storePage";
    }
    @GetMapping("/tailor")
    public String tailor(Model model,  @ModelAttribute("customer")Customer customer) {
        Map<Item, Integer> inventory = itemRepository.loadInventory("tailor");
        customer.setLastVisited("tailor");
        model.addAttribute("inventory", inventory);
        model.addAttribute("storeName", "Tailor");
        return "storePage";

    }
    @PostMapping("/checkout")
    public String checkout(Model model, @ModelAttribute("customer")Customer customer){
        //put items from cart into inventory
        Map<Item, Integer> cartBuffer = customer.getShoppingCart();
        Map<Item, Integer> invBuffer = customer.getInventory();
        AtomicInteger totalCost = new AtomicInteger();
        //iterate through cart
        cartBuffer.forEach((cartItem, cartQuantity) -> {
            //add to cost
            totalCost.set(cartItem.getPrice() * cartQuantity.intValue() + totalCost.get());
        });
        //todo dont allow for checkout if customer not enough funds
        customerService.checkoutCart(customer);
        //deduct funds and update cust object
        int cost = totalCost.get();
        customer.setFunds(customer.getFunds() - cost);

        //update db
        customerService.updateCustomer(customer);

        return "redirect:/";
    }
    @PostMapping
    public String addtoCart(Model model, @ModelAttribute("customer")Customer customer) {
        Map<Item, Integer> inventory = itemRepository.loadInventory(customer.getLastVisited());
        customer.consolidateCart(inventory);
        model.addAttribute("customer", customer);
        return "redirect:/store";
    }

}
