package net.wolfygames7237.Crusadersoffiction.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.Item.ModItem;

public class ModItemModelProvider extends ItemModelProvider {
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
}
