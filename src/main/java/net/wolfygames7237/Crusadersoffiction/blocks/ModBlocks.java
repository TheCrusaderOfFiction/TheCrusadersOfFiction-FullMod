package net.wolfygames7237.Crusadersoffiction.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.Item.ModItem;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wolfygames7237.Crusadersoffiction.blocks.custom.*;
import net.wolfygames7237.Crusadersoffiction.blocks.custom.craftors.BlockCompressor;
import net.wolfygames7237.Crusadersoffiction.blocks.custom.craftors.Forge;
import net.wolfygames7237.Crusadersoffiction.blocks.custom.craftors.StructureBuilder;
import net.wolfygames7237.Crusadersoffiction.worldgen.tree.BorealTreeGrower;
import net.wolfygames7237.Crusadersoffiction.worldgen.tree.MahoganyTreeGrower;

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

    public static final RegistryObject<Block> MAHOGANY_LOG = registerBlock("mahogany_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> MAHOGANY_WOOD = registerBlock("mahogany_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<Block> STRIPPED_MAHOGANY_LOG = registerBlock("stripped_mahogany_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> STRIPPED_MAHOGANY_WOOD = registerBlock("stripped_mahogany_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> MAHOGANY_PLANKS = registerBlock("mahogany_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });
    public static final RegistryObject<Block> MAHOGANY_LEAVES = registerBlock("mahogany_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
            });
    public static final RegistryObject<Block> MAHOGANY_SAPLING = registerBlock("mahogany_sapling",
            () -> new ModMahoganySaplingBlock(new MahoganyTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> BOREAL_LOG = registerBlock("boreal_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> BOREAL_WOOD = registerBlock("boreal_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<Block> STRIPPED_BOREAL_LOG = registerBlock("stripped_boreal_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> STRIPPED_BOREAL_WOOD = registerBlock("stripped_boreal_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> BOREAL_PLANKS = registerBlock("boreal_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });
    public static final RegistryObject<Block> BOREAL_LEAVES = registerBlock("boreal_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
            });
    public static final RegistryObject<Block> BOREAL_SAPLING = registerBlock("boreal_sapling",
            () -> new ModBorealSaplingBlock(new BorealTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> JUNGLE_GRASS_BLOCK = registerBlock("jungle_grass_block",
            () -> new JungleGrassBlock(BlockBehaviour.Properties.copy(Blocks.MUD)));
    public static final RegistryObject<Block> GLOWING_MUSHROOM_BLOCK = registerBlock("glowing_mushroom_block",
            () -> new GlowingMushroomBlock(BlockBehaviour.Properties.copy(Blocks.MUD).lightLevel(state -> 5)));
    public static final RegistryObject<Block> GLOWING_MUSHROOM = registerBlock("glowing_mushroom",
            () -> new GlowingMushroom(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM).lightLevel(state -> 2)));
    public static final RegistryObject<Block> JUNGLE_GRASS = registerBlock("jungle_grass",
            () -> new JungleGrass(BlockBehaviour.Properties.copy(Blocks.GRASS)));

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