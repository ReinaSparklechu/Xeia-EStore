package Xeia.Data;

import Xeia.Customer.Customer;
import Xeia.Items.Consumable;
import Xeia.Items.Equipment;
import Xeia.Items.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcItemRepository implements ItemRepository {
    private JdbcTemplate jdbc;

    @Autowired
    public JdbcItemRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private Item mapRowToItem(ResultSet rs, int rowNum) throws SQLException {
        if(rs.getBoolean("isConsumable")) {
            return new Consumable(rs.getString("name"), rs.getInt("price"), rs.getInt("quality"), rs.getString("effect"));
        } else if(rs.getBoolean("isEquipment")) {
            return new Equipment(rs.getString("name"), rs.getInt("price"), rs.getInt("itemLvl"), rs.getBoolean("isEnchantable"), rs.getString("enchantment"));
        } else {
            System.out.println("Item could not be formed properly, returning null");
            return null;
        }
    }

    private Integer mapRowToInteger(ResultSet rs, int rowNum) throws SQLException {
        return Integer.valueOf(rs.getInt("quantity"));
    }
    public Map<Item,Integer> loadInventory(String store) {

        List<Item> items;
        List <Integer> quantity;
        Map<Item, Integer> inventory = new HashMap<>(10);
        quantity = jdbc.query("select quantity from Item_Store where Store_Name = \'" + store + "\'", this::mapRowToInteger);
        if(store.equalsIgnoreCase("apothecary")) {
            items = jdbc.query("select i.name, i.price, i.quality, i.effect, i.isConsumable, i.isEquipment from Items i, Item_Store s  where s.Store_Name = \'" + store +"\' and i.name = s.Item_Name" , this::mapRowToItem);
        } else {
            items = jdbc.query("select i.name, i.price, i.itemLvl, i.isEnchantable, i.enchantment , i.isConsumable, i.isEquipment from Items i , Item_Store s where s.Store_Name = \'" + store +"\' and i.name = s.Item_Name" , this::mapRowToItem);
        }


        if(items.size() == quantity.size()) {
            int length = items.size();
            Iterator<Item> items1 = items.iterator();
            Iterator<Integer> integerIterator = quantity.iterator();
            for(int i = 0; i<length; i++) {
                inventory.put(items1.next(),integerIterator.next());
            }
        } else {
            throw new RuntimeException("Item quantity list mismatch: ");
        }
        return inventory;
    }
    //todo
    public void makeSale(Customer customer) {

    }

}
