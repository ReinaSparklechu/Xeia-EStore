delete from Items;

insert into Items(name, price, isEquipment, isConsumable , isEnchantable, itemLvl, enchantment)
    values ( 'Boots', 5, true, false, true, '10', '+2Speed');
insert into Items(name, price, isEquipment, isConsumable , isEnchantable, itemLvl, enchantment)
values ( 'Leather Pants', 5, true, false, true, '10', '+2Res');
insert into Items(name, price, isEquipment, isConsumable , isEnchantable, itemLvl, enchantment)
values ( 'Leather Vest', 5, true, false, true, '10', '+2Speed');
insert into Items(name, price, isEquipment, isConsumable , isEnchantable, itemLvl, enchantment)
values ( 'Leather Gloves', 5, true, false, true, '10', '+2Agility');
insert into Items(name, price, isEquipment, isConsumable , isEnchantable, itemLvl, enchantment)
values ( 'Hunting Hat', 5, true, false, true, '10', 'null');
insert into Items(name, price, isEquipment, isConsumable , isEnchantable, itemLvl, enchantment)
values ( 'Greaves', 5, true, false, true, '10', '+2Def');
insert into Items(name, price, isEquipment, isConsumable , isEnchantable, itemLvl, enchantment)
values ( 'Helmet', 5, true, false, true, '10', '+2Strength');
insert into Items(name, price, isEquipment, isConsumable , isEnchantable, itemLvl, enchantment)
values ( 'Chestplate', 5, true, false, true, '10', '+2Strength');
insert into Items(name, price, isEquipment, isConsumable , isEnchantable, itemLvl, enchantment)
values ( 'Cuisses', 5, true, false, true, '10', '+2Speed');
insert into Items(name, price, isEquipment, isConsumable , isEnchantable, itemLvl, enchantment)
values ( 'Gauntlet', 5, true, false, true, '10', '+2Speed');
insert into Items(name, price, isEquipment, isConsumable , isEnchantable, itemLvl, enchantment)
values ( 'Sword', 5, true, false, true, '10', '+2Strength');
insert into Items(name, price, isEquipment, isConsumable , isEnchantable, itemLvl, enchantment)
values ( 'Axe', 5, true, false, true, '10', '+3Strength');
insert into Items(name, price, isEquipment, isConsumable , isEnchantable, itemLvl, enchantment)
values ( 'Dagger', 5, true, false, true, '10', '+2Agility');
insert into Items(name, price, isEquipment, isConsumable , quality , effect)
values ( 'Health Potion', 5, false, true, '4', 'Health Restore');
insert into Items(name, price, isEquipment, isConsumable , quality , effect)
values ( 'Mana Potion', 5, false, true, '4', 'Mana Restore');
insert into Items(name, price, isEquipment, isConsumable , quality , effect)
values ( 'Antidote', 10, false, true, '3', 'Heals PoisonStatus');
insert into Items(name, price, isEquipment, isConsumable , quality , effect)
values ( 'Ration', 5, false, true, '4', 'Stamina Restore');

insert into Item_Store(store_name, item_name, quantity) values ('apothecary', 'Health Potion' , 10 );
insert into Item_Store(store_name, item_name, quantity) values ('apothecary', 'Mana Potion' , 10 );
insert into Item_Store(store_name, item_name, quantity) values ('apothecary', 'Antidote' , 10 );
insert into Item_Store(store_name, item_name, quantity) values ('apothecary', 'Ration' , 10 );
insert into Item_Store(store_name, item_name, quantity) values ('tailor', 'Boots' , 5 );
insert into Item_Store(store_name, item_name, quantity) values ('tailor', 'Leather Pants' , 5 );
insert into Item_Store(store_name, item_name, quantity) values ('tailor', 'Leather Vest' , 5 );
insert into Item_Store(store_name, item_name, quantity) values ('tailor', 'Leather Gloves' , 5 );
insert into Item_Store(store_name, item_name, quantity) values ('tailor', 'Hunting Hat' , 5 );
insert into Item_Store(store_name, item_name, quantity) values ('smithy', 'Helmet' , 5 );
insert into Item_Store(store_name, item_name, quantity) values ('smithy', 'Chestplate' , 5 );
insert into Item_Store(store_name, item_name, quantity) values ('smithy', 'Cuisses' , 5 );
insert into Item_Store(store_name, item_name, quantity) values ('smithy', 'Gauntlet' , 5 );
insert into Item_Store(store_name, item_name, quantity) values ('smithy', 'Sword' , 5 );
insert into Item_Store(store_name, item_name, quantity) values ('smithy', 'Axe' , 5 );
insert into Item_Store(store_name, item_name, quantity) values ('smithy', 'Dagger' , 5 );
