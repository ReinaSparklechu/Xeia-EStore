package Xeia.Web;

import Xeia.Data.ItemRepository;
import Xeia.Data.JdbcItemRepository;
import Xeia.Items.Equipment;
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
        Equipment equipment = new Equipment();
    }
    @GetMapping
    public String store() {
        return "store";
    }
    @GetMapping("/smithy")
    public String smithy(Model model){
        Map<Item, Integer> inventory = itemRepository.loadInventory("smithy");
        model.addAttribute("inventory", inventory);
        model.addAttribute("storeName", "Smithy");
        return "storePage";
    }

    @GetMapping("/apothecary")
    public String apothecary(Model model) {
        Map<Item, Integer> inventory = itemRepository.loadInventory("apothecary");
        model.addAttribute("inventory", inventory);
        model.addAttribute("storeName", "Apothecary");
        return "storePage";
    }
    @GetMapping("/tailor")
    public String tailor(Model model) {
        Map<Item, Integer> inventory = itemRepository.loadInventory("tailor");
        model.addAttribute("inventory", inventory);
        model.addAttribute("storeName", "Tailor");
        return "storePage";

    }

}
