
create table if not exists Items (
    name varchar(32) primary key ,
    price int not null,
    isEquipment boolean not null ,
    isConsumable boolean not null,
    isEnchantable boolean,
    itemLvl int,
    enchantment varchar(32),
    quality int,
    effect varchar(32)
);
alter table Items add constraint Item_isEquipmentOrIsConsumable check (
    Items.isEquipment = true and Items.isEnchantable is not null and Items.itemLvl is not null and Items.enchantment is not null
    or Items.isConsumable = true and Items.quality is not null and Items.effect is not null
    );


create table if not exists Item_Store
(
    Store_Name varchar(32) not null,
    Item_Name varchar (32) not null,
    quantity int not null
);
alter table Item_Store add foreign key (Item_Name) references Items(name);

create table if not exists Customer(
    userId identity ,
    username varchar(32) not null,
    passwordHash varchar2(32) not null,
    funds int,
    authority varchar2(32) not null

);
create table if not exists Customer_Cart
(
    userId long not null,
    Item_name varchar(32) not null,
    Quantity int not null
);
alter table Customer_Cart add foreign key (userId) references Customer(userId);
alter table Customer_Cart add foreign key (Item_name) references Items(name);
create table if not exists Customer_Inventory (
    userId long not null,
    Item_name varchar(32) not null,
    Quantity int not null
);
alter table Customer_Inventory add foreign key (userId) references Customer(userId);
alter table Customer_Inventory add foreign key (Item_name) references Items(name);