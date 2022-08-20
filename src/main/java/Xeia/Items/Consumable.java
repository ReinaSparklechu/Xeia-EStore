package Xeia.Items;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
}
