package net.wolfygames7237.Crusadersoffiction.Item;

import net.minecraft.world.item.PickaxeItem;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItem {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CrusadersOfFiction.MOD_ID);

    public static final RegistryObject<Item> BLUESHARD = ITEMS.register( "blueshardh",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> WYSTERIUM = ITEMS.register( "wysterium",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> BLUEARMORSHARD = ITEMS.register( "bluearmorshard",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> BLUEARMORCASING = ITEMS.register( "bluearmorcasing",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> COMPUTERCHIP = ITEMS.register( "computerchip",
            () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPERWIRE = ITEMS.register( "copperwire",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> COPPERNUGGET = ITEMS.register( "coppernugget",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> WYSTERIUMNUGGET = ITEMS.register( "wysteriumnugget",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> SOULESSANCE = ITEMS.register( "soulessance",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAINBOWESSANCE = ITEMS.register( "rainbowessance",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> VOIDESSANCE = ITEMS.register( "voidessance",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAWWYSTERIUM = ITEMS.register( "rawwysterium",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> OBI_ROD = ITEMS.register( "obi_rod",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> WYSTERIUM_SWORD = ITEMS.register( "wysterium_sword",
            () ->new PickaxeItem(ModToolTiers.WYSTERIUM, 10, 30, new Item.Properties()));
    public static final RegistryObject<Item> WYSTERIUM_PICKAXE = ITEMS.register( "wysterium_pickaxe",
            () ->new PickaxeItem(ModToolTiers.WYSTERIUM, 5, 20, new Item.Properties()));
    public static final RegistryObject<Item> WYSTERIUM_AXE = ITEMS.register( "wysterium_axe",
            () ->new PickaxeItem(ModToolTiers.WYSTERIUM, 12, 20, new Item.Properties()));
    public static final RegistryObject<Item> WYSTERIUM_SHOVEL = ITEMS.register( "wysterium_shovel",
            () ->new PickaxeItem(ModToolTiers.WYSTERIUM, 2, 20, new Item.Properties()));
    public static final RegistryObject<Item> WYSTERIUM_HOE = ITEMS.register( "wysterium_hoe",
            () ->new PickaxeItem(ModToolTiers.WYSTERIUM, 0, 20, new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
