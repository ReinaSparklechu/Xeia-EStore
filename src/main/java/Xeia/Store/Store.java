package Xeia.Store;

import Xeia.Items.Item;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@RequiredArgsConstructor
public class Store {
    String storeName;
    Map<Item, Integer> inventory;
}
