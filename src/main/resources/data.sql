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

insert into Store(Store_id, Store_name) values ( 'S00','apothecary');
insert into Store(Store_id, Store_name) values ( 'S01','smithy');
insert into Store(Store_id, Store_name) values ( 'S02','tailor');

insert into Item_Owner(Store_Id, item_name, quantity) values ('S00', 'Health Potion' , 10 );
insert into Item_Owner(Store_Id, item_name, quantity) values ('S00', 'Mana Potion' , 10 );
insert into Item_Owner(Store_Id, item_name, quantity) values ('S00', 'Antidote' , 10 );
insert into Item_Owner(Store_Id, item_name, quantity) values ('S00', 'Ration' , 10 );
insert into Item_Owner(Store_Id, item_name, quantity) values ('S02', 'Boots' , 5 );
insert into Item_Owner(Store_Id, item_name, quantity) values ('S02', 'Leather Pants' , 5 );
insert into Item_Owner(Store_Id, item_name, quantity) values ('S02', 'Leather Vest' , 5 );
insert into Item_Owner(Store_Id, item_name, quantity) values ('S02', 'Leather Gloves' , 5 );
insert into Item_Owner(Store_Id, item_name, quantity) values ('S02', 'Hunting Hat' , 5 );
insert into Item_Owner(Store_Id, item_name, quantity) values ('S01', 'Helmet' , 5 );
insert into Item_Owner(Store_Id, item_name, quantity) values ('S01', 'Chestplate' , 5 );
insert into Item_Owner(Store_Id, item_name, quantity) values ('S01', 'Cuisses' , 5 );
insert into Item_Owner(Store_Id, item_name, quantity) values ('S01', 'Gauntlet' , 5 );
insert into Item_Owner(Store_Id, item_name, quantity) values ('S01', 'Sword' , 5 );
insert into Item_Owner(Store_Id, item_name, quantity) values ('S01', 'Axe' , 5 );
insert into Item_Owner(Store_Id, item_name, quantity) values ('S01', 'Dagger' , 5 );

insert into Customer(username, passwordhash, funds, authority) values('username1234' , 'bdc87b9c894da5168059e00ebffb9077', 1000, 'USER');
