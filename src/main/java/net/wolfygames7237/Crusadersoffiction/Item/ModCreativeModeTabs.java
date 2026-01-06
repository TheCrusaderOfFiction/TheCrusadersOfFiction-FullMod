package net.wolfygames7237.Crusadersoffiction.Item;

import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CrusadersOfFiction.MOD_ID);

    public static final RegistryObject<CreativeModeTab> Crusaders_Mod = CREATIVE_MODE_TABS.register("crusader_mod",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItem.WYSTERIUM.get()))
                    .title(Component.translatable("creativetab.crusader_mod"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItem.BLUESHARD.get());
                        output.accept(ModItem.BLUEARMORSHARD.get());
                        output.accept(ModItem.BLUEARMORCASING.get());
                        output.accept(ModItem.COMPUTERCHIP.get());
                        output.accept(ModItem.COPPERWIRE.get());
                        output.accept(ModItem.COPPERNUGGET.get());
                        output.accept(ModBlocks.WYSTERIUM_ORE.get());
                        output.accept(ModBlocks.WYSTERIUM_BLOCK.get());
                        output.accept(ModItem.WYSTERIUM.get());
                        output.accept(ModItem.WYSTERIUMNUGGET.get());
                        output.accept(ModItem.RAWWYSTERIUM.get());
                        output.accept(ModItem.WYSTERIUM_SWORD.get());
                        output.accept(ModItem.WYSTERIUM_PICKAXE.get());
                        output.accept(ModItem.WYSTERIUM_AXE.get());
                        output.accept(ModItem.WYSTERIUM_SHOVEL.get());
                        output.accept(ModItem.WYSTERIUM_HOE.get());
                        output.accept(ModItem.WYSTERIUM_HELMET.get());
                        output.accept(ModItem.WYSTERIUM_CHESTPLATE.get());
                        output.accept(ModItem.WYSTERIUM_LEGGINGS.get());
                        output.accept(ModItem.WYSTERIUM_BOOTS.get());
                        output.accept(ModItem.OBI_ROD.get());
                        output.accept(ModItem.SOULESSANCE.get());
                        output.accept(ModItem.RAINBOWESSANCE.get());
                        output.accept(ModItem.VOIDESSANCE.get());

                    })
                    .build());


    public static final RegistryObject<CreativeModeTab> Crusaders_progress = CREATIVE_MODE_TABS.register("crusaders_progress",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.FORGE.get()))
                    .title(Component.translatable("creativetab.crusader_progress"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(ModBlocks.FORGE.get());

                        output.accept(ModItem.COPPER_HAMMER.get());
                        output.accept(ModItem.IRON_HAMMER.get());

                        output.accept(ModItem.IRON_SWORD_BLADE.get());
                        output.accept(ModItem.IRON_AXE_HEAD.get());
                        output.accept(ModItem.IRON_PICKAXE_HEAD.get());
                        output.accept(ModItem.IRON_SHOVEL_BLADE.get());
                        output.accept(ModItem.IRON_HOE_BLADE.get());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
