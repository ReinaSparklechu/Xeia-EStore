package Xeia.Items;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Consumable extends Item{
    private int quality;
    String effect;

    public Consumable(String name, int price, int quality, String effect) {
        super(name,price);
        this.quality = quality;
        this.effect = effect;
    }
}
