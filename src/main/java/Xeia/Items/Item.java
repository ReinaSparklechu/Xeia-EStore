package Xeia.Items;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class Item {
    private String name;
    private int price;

    public boolean equals(Item i) {
        if(i.getName() == this.name && i.getPrice() == this.price) {
            return true;
        }else{
            return false;
        }
    }

}
