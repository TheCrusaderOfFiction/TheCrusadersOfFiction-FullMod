package net.wolfygames7237.Crusadersoffiction.util;

import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {

        public static final TagKey<Block> NEEDS_WYSTERIUM_TOOL = tag("needs_wysterium_tool");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(CrusadersOfFiction.MOD_ID, name));
        }

        public static class Items {

            private static TagKey<Item> tag(String name) {
                return ItemTags.create(new ResourceLocation(CrusadersOfFiction.MOD_ID, name));
            }

        }
    }
}
