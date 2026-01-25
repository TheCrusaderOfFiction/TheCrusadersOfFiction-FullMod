package net.wolfygames7237.Crusadersoffiction.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.Item.ModItem;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;


import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CrusadersOfFiction.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItem.WYSTERIUM);
        simpleItem(ModItem.RAWWYSTERIUM);
        simpleItem(ModItem.WYSTERIUMNUGGET);
        handheldItem(ModItem.WYSTERIUM_SWORD);
        handheldItem(ModItem.WYSTERIUM_PICKAXE);
        handheldItem(ModItem.WYSTERIUM_AXE);
        handheldItem(ModItem.WYSTERIUM_SHOVEL);
        handheldItem(ModItem.WYSTERIUM_HOE);

        simpleItem(ModItem.OBI_ROD);
        simpleItem(ModItem.BLUESHARD);
        simpleItem(ModItem.BLUEARMORSHARD);
        simpleItem(ModItem.BLUEARMORCASING);
        simpleItem(ModItem.COPPERNUGGET);
        simpleItem(ModItem.COPPERWIRE);
        simpleItem(ModItem.SOULESSANCE);
        simpleItem(ModItem.RAINBOWESSANCE);
        simpleItem(ModItem.VOIDESSANCE);
        simpleItem(ModItem.COMPUTERCHIP);

        trimmedArmorItem(ModItem.WYSTERIUM_HELMET);
        trimmedArmorItem(ModItem.WYSTERIUM_CHESTPLATE);
        trimmedArmorItem(ModItem.WYSTERIUM_LEGGINGS);
        trimmedArmorItem(ModItem.WYSTERIUM_BOOTS);

        handheldItem(ModItem.COPPER_SWORD);
        handheldItem(ModItem.COPPER_PICKAXE);
        handheldItem(ModItem.COPPER_AXE);
        handheldItem(ModItem.COPPER_SHOVEL);
        handheldItem(ModItem.COPPER_HOE);

        complexBlock(ModBlocks.FORGE.get());
        complexBlock(ModBlocks.STRUCTURE_BUILDER.get());
        complexBlock(ModBlocks.BLOCK_COMPRESSOR.get());
        simpleItem(ModItem.FIBER);

        handheldItem(ModItem.COPPER_HAMMER);
        handheldItem(ModItem.IRON_HAMMER);

        handheldItem(ModItem.IRON_SWORD_BLADE);
        handheldItem(ModItem.IRON_PICKAXE_HEAD);
        handheldItem(ModItem.IRON_AXE_HEAD);
        handheldItem(ModItem.IRON_SHOVEL_BLADE);
        handheldItem(ModItem.IRON_HOE_BLADE);

        handheldItem(ModItem.DIAMOND_SWORD_BLADE);
        handheldItem(ModItem.DIAMOND_PICKAXE_HEAD);
        handheldItem(ModItem.DIAMOND_AXE_HEAD);
        handheldItem(ModItem.DIAMOND_SHOVEL_BLADE);
        handheldItem(ModItem.DIAMOND_HOE_BLADE);

        handheldItem(ModItem.ROCK_HATCHET);
        handheldItem(ModItem.ROCK);

        blockItem(ModItem.MOB_FARM_STRUCTURE, Blocks.STONE);

        generatePlacerModels();
    }
    private void generatePlacerModels() {
        // Loop through all your ModItems
        ModItem.ITEMS.getEntries().stream()
                .filter(regObj -> regObj.getId().getPath().endsWith("_placer"))
                .forEach(regObj -> {
                    Item placer = regObj.get();

                    // Extract the prefix (remove "_PLACER")
                    String prefix = regObj.getId().getPath().replace("_placer", "");

                    // Attempt to find a corresponding block in ModBlocks or vanilla Blocks
                    Block block = getBlockForPrefix(prefix);

                    if (block == Blocks.AIR) {
                        System.out.println("[ItemModelProvider] Warning: Could not find block for placer " + regObj.getId());
                        return;
                    }

                    // Generate the item model
                    blockItem(regObj, block);
                    System.out.println("[ItemModelProvider] Generated placer model: " + regObj.getId() + " -> " + block);
                });
    }

    /** Lookup block by prefix in ModBlocks first, then vanilla Blocks */
    private Block getBlockForPrefix(String prefix) {
        // Try ModBlocks first
        try {
            for (Field field : ModBlocks.class.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase(prefix) && RegistryObject.class.isAssignableFrom(field.getType())) {
                    RegistryObject<Block> ro = (RegistryObject<Block>) field.get(null);
                    return ro.get();
                }
            }
        } catch (IllegalAccessException ignored) {}

        // Fallback to vanilla Blocks
        try {
            for (Field field : Blocks.class.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase(prefix) && Block.class.isAssignableFrom(field.getType())) {
                    return (Block) field.get(null);
                }
            }
        } catch (IllegalAccessException ignored) {}

        return Blocks.AIR;
    }

    private ItemModelBuilder complexBlock(Block block) {
        return withExistingParent(ForgeRegistries.BLOCKS.getKey(block).getPath(), new ResourceLocation(CrusadersOfFiction.MOD_ID,
                "block/" + ForgeRegistries.BLOCKS.getKey(block).getPath()));
    }

    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = CrusadersOfFiction.MOD_ID; // Change this to your mod id

        if(itemRegistryObject.get() instanceof ArmorItem armorItem) {
            trimMaterials.entrySet().forEach(entry -> {

                ResourceKey<TrimMaterial> trimMaterial = entry.getKey();
                float trimValue = entry.getValue();

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = "item/" + armorItem;
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = new ResourceLocation(MOD_ID, armorItemPath);
                ResourceLocation trimResLoc = new ResourceLocation(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = new ResourceLocation(MOD_ID, currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemRegistryObject.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                new ResourceLocation(MOD_ID,
                                        "item/" + itemRegistryObject.getId().getPath()));
            });
        }
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.tryParse("item/generated")).texture("layer0",
                ResourceLocation.tryBuild(CrusadersOfFiction.MOD_ID, "item/" + item.getId().getPath()));
    }
    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(CrusadersOfFiction.MOD_ID,"item/" + item.getId().getPath()));
    }
    private ItemModelBuilder blockItem(RegistryObject<Item> itemReg, Block block) {
        return withExistingParent(
                itemReg.getId().getPath(),
                new ResourceLocation("minecraft", "block/" + ForgeRegistries.BLOCKS.getKey(block).getPath())
        );
    }


}
