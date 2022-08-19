package Xeia.Items;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Equipment extends Item{
    private int itemLvl;
    private boolean isEnchantable;
    private String enchantment;


    public Equipment(String name, int price, int itemLvl, boolean isEnchantable, String enchantment) {
        super.setName(name);
        super.setPrice(price);
        this.itemLvl = itemLvl;
        this.enchantment = enchantment;
        this.isEnchantable = isEnchantable;
    }
    public String toString() {
        return "\nEquipment (Name=" + this.getName()
                + ", Price=" + this.getPrice()
                + ", itemLvl=" + this.itemLvl
                + ", isEnchantable=" + this.isEnchantable
                + ", Enchantment=" + this.enchantment;
    }
}
