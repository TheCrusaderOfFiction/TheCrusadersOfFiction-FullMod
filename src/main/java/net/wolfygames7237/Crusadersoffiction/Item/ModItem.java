package net.wolfygames7237.Crusadersoffiction.Item;

import net.minecraft.world.item.*;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wolfygames7237.Crusadersoffiction.Item.custom.ModArmorItem;
import net.wolfygames7237.Crusadersoffiction.Item.structure.*;

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

    public static final RegistryObject<Item> COPPER_HAMMER = ITEMS.register( "copper_hammer",
            () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_HAMMER = ITEMS.register( "iron_hammer",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> IRON_SWORD_BLADE = ITEMS.register( "iron_sword_blade",
            () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_HOE_BLADE = ITEMS.register( "iron_hoe_blade",
            () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_SHOVEL_BLADE = ITEMS.register( "iron_shovel_blade",
            () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_AXE_HEAD = ITEMS.register( "iron_axe_head",
            () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_PICKAXE_HEAD = ITEMS.register( "iron_pickaxe_head",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> DIAMOND_SWORD_BLADE = ITEMS.register( "diamond_sword_blade",
            () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_HOE_BLADE = ITEMS.register( "diamond_hoe_blade",
            () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_SHOVEL_BLADE = ITEMS.register( "diamond_shovel_blade",
            () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_AXE_HEAD = ITEMS.register( "diamond_axe_head",
            () ->new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_PICKAXE_HEAD = ITEMS.register( "diamond_pickaxe_head",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> ROCK = ITEMS.register( "rock",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> ROCK_HATCHET = ITEMS.register( "rock_hatchet",
            () ->new AxeItem(Tiers.WOOD,1,1, new Item.Properties()));
    public static final RegistryObject<Item> FIBER = ITEMS.register( "fiber",
            () ->new Item(new Item.Properties()));

    public static final RegistryObject<Item> WYSTERIUM_SWORD = ITEMS.register( "wysterium_sword",
            () ->new SwordItem(ModToolTiers.WYSTERIUM, 10, 30, new Item.Properties()));
    public static final RegistryObject<Item> WYSTERIUM_PICKAXE = ITEMS.register( "wysterium_pickaxe",
            () ->new PickaxeItem(ModToolTiers.WYSTERIUM, 5, 20, new Item.Properties()));
    public static final RegistryObject<Item> WYSTERIUM_AXE = ITEMS.register( "wysterium_axe",
            () ->new AxeItem(ModToolTiers.WYSTERIUM, 12, 20, new Item.Properties()));
    public static final RegistryObject<Item> WYSTERIUM_SHOVEL = ITEMS.register( "wysterium_shovel",
            () ->new ShovelItem(ModToolTiers.WYSTERIUM, 2, 20, new Item.Properties()));
    public static final RegistryObject<Item> WYSTERIUM_HOE = ITEMS.register( "wysterium_hoe",
            () ->new HoeItem(ModToolTiers.WYSTERIUM, 0, 20, new Item.Properties()));

    public static final RegistryObject<Item> COPPER_SWORD = ITEMS.register( "copper_sword",
            () ->new SwordItem(ModToolTiers.COPPER, 5, 2, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_PICKAXE = ITEMS.register( "copper_pickaxe",
            () ->new PickaxeItem(ModToolTiers.COPPER, 3, 1, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_AXE = ITEMS.register( "copper_axe",
            () ->new AxeItem(ModToolTiers.COPPER, 7, 1, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_SHOVEL = ITEMS.register( "copper_shovel",
            () ->new ShovelItem(ModToolTiers.COPPER, 3, 1, new Item.Properties()));
    public static final RegistryObject<Item> COPPER_HOE = ITEMS.register( "copper_hoe",
            () ->new HoeItem(ModToolTiers.COPPER, 0, 2, new Item.Properties()));

    public static final RegistryObject<Item> WYSTERIUM_HELMET = ITEMS.register("wysterium_helmet",
            () -> new ModArmorItem(ModArmorMaterials.WYSTERIUM, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> WYSTERIUM_CHESTPLATE = ITEMS.register("wysterium_chestplate",
            () -> new ArmorItem(ModArmorMaterials.WYSTERIUM, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> WYSTERIUM_LEGGINGS = ITEMS.register("wysterium_leggings",
            () -> new ArmorItem(ModArmorMaterials.WYSTERIUM, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> WYSTERIUM_BOOTS = ITEMS.register("wysterium_boots",
            () -> new ArmorItem(ModArmorMaterials.WYSTERIUM, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> COBBLESTONE_PLACER = ITEMS.register("cobblestone_placer",
                    () -> new StoneCompressedPlacerItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> MAGMA_BLOCK_PLACER = ITEMS.register("magma_block_placer",
            () -> new MagmaCompressedPlacerItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> DIRT_PLACER = ITEMS.register("dirt_placer",
            () -> new DirtCompressedPlacerItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> STONE_PLACER = ITEMS.register("stone_placer",
            () -> new NatstoneCompressedPlacerItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> OAK_LEAVES_PLACER = ITEMS.register("oak_leaves_placer",
            () -> new OakleavesCompressedPlacerItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> BED_PLACER = ITEMS.register("bed_placer",
            () -> new BedCompressedPlacerItem(new Item.Properties().stacksTo(4)));
    public static final RegistryObject<Item> MOB_FARM_STRUCTURE = ITEMS.register("mob_farm_structure",
            () -> new MobfarmCompressedPlacerCenteredItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> IRON_FARM_STRUCTURE = ITEMS.register("iron_farm_structure",
            () -> new IronfarmCompressedPlacerTopItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUGARCANE_FARM_STRUCTURE = ITEMS.register("sugarcane_farm_structure",
            () -> new SugarcanefarmCompressedPlacerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MAGMA_FARM_STRUCTURE = ITEMS.register("magma_farm_structure",
            () -> new MagmafarmCompressedPlacerCenteredItem(new Item.Properties().stacksTo(1)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
