package Xeia.Data;

import Xeia.Customer.Customer;
import Xeia.Items.Consumable;
import Xeia.Items.Equipment;
import Xeia.Items.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class JdbcItemRepository implements ItemRepository {
    private JdbcTemplate jdbc;

    @Autowired
    public JdbcItemRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private Map.Entry<Item, Integer> mapRowToItem(ResultSet rs, int rowNum) throws SQLException {
        if(rs.getBoolean("isConsumable")) {
            return Map.entry(
                    new Consumable(rs.getString("name"), rs.getInt("price"), rs.getInt("quality"), rs.getString("effect"))
                    , rs.getInt("quantity"));
        } else if(rs.getBoolean("isEquipment")) {
            return Map.entry( new Equipment(rs.getString("name"), rs.getInt("price"), rs.getInt("itemLvl"), rs.getBoolean("isEnchantable"), rs.getString("enchantment")),
                    rs.getInt("quantity"));
        } else {
            return null;
        }
    }

    private Integer mapRowToInteger(ResultSet rs, int rowNum) throws SQLException {
        return Integer.valueOf(rs.getInt("quantity"));
    }

    public Map<Item,Integer> loadInventory(String store) {


        List<Map.Entry<Item, Integer>> inventory = new ArrayList<>();
        Map<Item,Integer> invMap = new TreeMap<>(Comparator.comparing(Item::getName));

        // query for store inventory
        String storeId = jdbc.queryForObject("select Store_id from Store where Store_name =  ?" , new RowMapper<String>(){
            public String mapRow(ResultSet rs, int rn) throws SQLException {
                return rs.getString("Store_id");
            }
        } , store);
        if(store.equalsIgnoreCase("apothecary")) {
            inventory = jdbc.query("select i.name, i.price, i.quality, i.effect, i.isConsumable, i.isEquipment ,s.quantity from Items i, Item_Owner s  where s.Store_Id = \'" + storeId +"\' and i.name = s.Item_Name" , this::mapRowToItem);
        } else {
            inventory = jdbc.query("select i.name, i.price, i.itemLvl, i.isEnchantable, i.enchantment , i.isConsumable, i.isEquipment, s.quantity from Items i , Item_Owner s where s.Store_Id = \'" + storeId +"\' and i.name = s.Item_Name" , this::mapRowToItem);
        }
        for(Map.Entry e : inventory) {
            var i = (Item) e.getKey();
            var q = (Integer) e.getValue();
            i.setOwner(storeId);
            invMap.put(i,q);

        }

        return invMap;
    }

    @Override
    public int getEntryQuantityById(String ownerId, Item item) {
        return jdbc.queryForObject("Select quantity from Item_Owner where store_Id = ? and item_Name = ? ", new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("Quantity");
            }
        }, ownerId, item.getName());

    }

    @Override
    public void updateEntryForId(String ownerId, Item i, int quantity) {
        jdbc.update("update Item_Owner set quantity = ? where item_name = ? and Store_Id = ? ", quantity, i.getName(), ownerId);
    }

}
