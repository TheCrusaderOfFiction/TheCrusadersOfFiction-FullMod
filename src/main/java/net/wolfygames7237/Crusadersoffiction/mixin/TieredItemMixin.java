package net.wolfygames7237.Crusadersoffiction.mixin;


import net.minecraft.world.item.*;
import net.wolfygames7237.Crusadersoffiction.Item.ModToolTiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TieredItem.class)
public abstract class TieredItemMixin {

    /**
     * SRG Name: m_43314_ (Standard Mojang/Parchment name: getTier)
     */
    @Inject(method = "getTier", at = @At("HEAD"), cancellable = true, remap = true)
    private void onGetTier(CallbackInfoReturnable<Tier> cir) {
        TieredItem item = (TieredItem) (Object) this;

        // Use the Item instance to check which tool it is
        if (item == Items.STONE_PICKAXE || item == Items.STONE_AXE || item == Items.STONE_SHOVEL || item == Items.STONE_HOE) {
            cir.setReturnValue(ModToolTiers.MOD_STONE);
        } else if (item == Items.IRON_PICKAXE || item == Items.IRON_AXE || item == Items.IRON_SHOVEL || item == Items.IRON_HOE) {
            cir.setReturnValue(ModToolTiers.MOD_IRON);
        } else if (item == Items.DIAMOND_PICKAXE || item == Items.DIAMOND_AXE || item == Items.DIAMOND_SHOVEL || item == Items.DIAMOND_HOE){
            cir.setReturnValue(ModToolTiers.MOD_DIAMOND);
        } else if (item == Items.WOODEN_PICKAXE || item == Items.WOODEN_AXE || item == Items.WOODEN_SHOVEL || item == Items.WOODEN_HOE) {
            cir.setReturnValue(ModToolTiers.MOD_WOOD);
        }
    }
}
