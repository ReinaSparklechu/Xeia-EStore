package Xeia.Data;

import Xeia.Items.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.Map;

public interface ItemRepository extends CrudRepository<Item, String> {
    public Map<Item,Integer> loadInventory(String store);
    public void updateEntryForId(String ownerId, Item i, int quantity);

    public int getEntryQuantityById(String ownerId, Item i);
}
