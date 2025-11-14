package net.wolfygames7237.Crusadersoffiction.Item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.util.ModTags;

import java.util.List;

public class ModToolTiers {
    public static final Tier WYSTERIUM = TierSortingRegistry.registerTier(
            new ForgeTier(5,20000,5f,5f,1,
                    ModTags.Blocks.NEEDS_WYSTERIUM_TOOL, () -> Ingredient.of(ModItem.WYSTERIUM.get())),
            new ResourceLocation(CrusadersOfFiction.MOD_ID, "wysterium"), List.of(Tiers.NETHERITE), List.of());
}
