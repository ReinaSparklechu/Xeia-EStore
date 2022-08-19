package Xeia.Data;

import Xeia.Items.Item;

import java.util.Map;

public interface ItemRepository {
    public Map<Item,Integer> loadInventory(String store);
}
