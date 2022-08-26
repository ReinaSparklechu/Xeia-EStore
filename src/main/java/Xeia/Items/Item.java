package Xeia.Items;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Data
@RequiredArgsConstructor
public class Item {

    private String name;
    private int price;

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public boolean equals(Object i) {
        if(i == this) {
            return true;
        }
        if(!(i instanceof Item)) {
            return false;
        }
        Item j = (Item) i;
        return j.getName().equalsIgnoreCase(this.name);
    }


}
