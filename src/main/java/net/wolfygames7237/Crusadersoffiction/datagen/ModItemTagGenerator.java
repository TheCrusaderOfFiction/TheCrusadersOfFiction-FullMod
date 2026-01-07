package net.wolfygames7237.Crusadersoffiction.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.Item.ModItem;
import net.wolfygames7237.Crusadersoffiction.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                               CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, CrusadersOfFiction.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItem.WYSTERIUM_HELMET.get(),
                        ModItem.WYSTERIUM_CHESTPLATE.get(),
                        ModItem.WYSTERIUM_LEGGINGS.get(),
                        ModItem.WYSTERIUM_BOOTS.get());

        this.tag(ModTags.Blocks.Items.HAMMER)
                .add(ModItem.COPPER_HAMMER.get(),
                        ModItem.IRON_HAMMER.get());

        this.tag(ItemTags.AXES)
                .add(ModItem.ROCK_HATCHET.get());


    }
}
