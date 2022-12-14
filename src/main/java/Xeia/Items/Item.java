package Xeia.Items;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Data
@RequiredArgsConstructor
public class Item {

    private String name;
    private int price;
    private String owner;

    public Item(String name, int price, String owner) {
        this.name= name;
        this.price = price;
        this.owner = owner;
    }

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
