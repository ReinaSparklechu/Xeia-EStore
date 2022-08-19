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
        if(i == this) {
            return true;
        }
        if(!(i instanceof Item)) {
            return false;
        }
        Item j = (Item) i;
        return (j.name == this.name);
    }

}
