package net.wolfygames7237.Crusadersoffiction.blocks;

import net.minecraft.world.level.block.SoundType;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.Item.ModItem;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wolfygames7237.Crusadersoffiction.blocks.custom.BlockCompressor;
import net.wolfygames7237.Crusadersoffiction.blocks.custom.Forge;
import net.wolfygames7237.Crusadersoffiction.blocks.custom.StructureBuilder;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CrusadersOfFiction.MOD_ID);

    public static final RegistryObject<Block> WYSTERIUM_ORE = registerBlock("wysterium_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK)
                    .strength(50).explosionResistance(1500).requiresCorrectToolForDrops(), UniformInt.of(0,20)));

    public static final RegistryObject<Block> WYSTERIUM_BLOCK = registerBlock("wysterium_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK)
                    .strength(50).explosionResistance(1500).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> CHARGED_COAL_BLOCK = registerBlock("charged_coal_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK).sound(SoundType.CANDLE)));

    public static final RegistryObject<Block> FORGE = registerBlock("forge",
            () -> new Forge(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> STRUCTURE_BUILDER = registerBlock("structure_builder",
            () -> new StructureBuilder(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> BLOCK_COMPRESSOR = registerBlock("block_compressor",
            () -> new BlockCompressor(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItem.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}