package net.wolfygames7237.Crusadersoffiction.Item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.util.ModTags;

import java.util.List;

public class ModToolTiers {


    public static final Tier MOD_WOOD = TierSortingRegistry.registerTier(
            new ForgeTier(1, 59, 2.0F, 0.0F, 15,
                    ModTags.Blocks.NEEDS_WOOD_TOOL, () -> Ingredient.of(ItemTags.PLANKS)),
            new ResourceLocation("crusadersoffiction", "wood"),
            List.of(), List.of());
    public static final Tier MOD_STONE = TierSortingRegistry.registerTier(
            new ForgeTier(2, 250, 6.0F, 2.0F, 14,
                    BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS)),
            new ResourceLocation("crusadersoffiction", "stone"),
            List.of(Tiers.WOOD), List.of());
    public static final Tier COPPER = TierSortingRegistry.registerTier(
            new ForgeTier(3, 59, 2.0F, 0.0F, 15,
                    ModTags.Blocks.NEEDS_COPPER_TOOL, () -> Ingredient.of(ItemTags.PLANKS)),
            new ResourceLocation("crusadersoffiction", "copper"),
            List.of(Tiers.STONE), List.of());
    public static final Tier MOD_IRON = TierSortingRegistry.registerTier(
            new ForgeTier(4, 250, 6.0F, 2.0F, 14,
                    BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(Items.IRON_INGOT)),
            new ResourceLocation("crusadersoffiction", "iron"),
            List.of(ModToolTiers.COPPER), List.of());
    public static final Tier MOD_DIAMOND = TierSortingRegistry.registerTier(
            new ForgeTier(5, 1561, 8.0F, 3.0F, 10,
                    BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(Items.DIAMOND)),
            new ResourceLocation("crusadersoffiction", "diamond"),
            List.of(Tiers.IRON), List.of());
    public static final Tier WYSTERIUM = TierSortingRegistry.registerTier(
            new ForgeTier(6,20000,5f,5f,1,
                    ModTags.Blocks.NEEDS_WYSTERIUM_TOOL, () -> Ingredient.of(ModItem.WYSTERIUM.get())),
            new ResourceLocation(CrusadersOfFiction.MOD_ID, "wysterium"), List.of(Tiers.NETHERITE), List.of());

}
