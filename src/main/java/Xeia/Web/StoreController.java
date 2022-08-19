package Xeia.Web;

import Xeia.Data.ItemRepository;
import Xeia.Data.JdbcItemRepository;
import Xeia.Items.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/store")
public class StoreController {

    ItemRepository itemRepository;

    @Autowired
    StoreController(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @GetMapping("/smithy")
    public String smithy(Model model){
        Map<Item, Integer> inventory = itemRepository.loadInventory("smithy");
        System.out.println(inventory);
        model.addAttribute("inventory", inventory);
        return "store";
    }

}
