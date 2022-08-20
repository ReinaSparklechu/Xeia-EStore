package Xeia.Items;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Item {
    private String name;
    private int price;

    @Override
    public boolean equals(Object i) {
        System.out.println("base comparitor used");
        if(i == this) {
            return true;
        }
        if(!(i instanceof Item||i instanceof Equipment||i instanceof Consumable)) {
            return false;
        }
        Item j = (Item) i;
        return (j.getName().equalsIgnoreCase(this.name));
    }


}
