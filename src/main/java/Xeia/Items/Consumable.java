package Xeia.Items;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@RequiredArgsConstructor
@Data
@AllArgsConstructor

public class Consumable extends Item{
    private int quality;
    private String effect;

    public Consumable(String name, int price, int quality, String effect) {
        super.setName(name);
        super.setPrice(price);
        this.quality = quality;
        this.effect = effect;
    }
    public boolean equals(Consumable c) {
        System.out.println("c comparitor used");
        if(c.getName().equals(this.getName())) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        return "\nEquipment (Name=" + this.getName()
                + ", Price=" + this.getPrice()
                + ", Quality=" + this.quality
                + ", Effect=" + this.effect +')';
    }
}
