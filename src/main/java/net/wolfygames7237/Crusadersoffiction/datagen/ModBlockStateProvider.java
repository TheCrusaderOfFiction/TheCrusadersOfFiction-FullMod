package net.wolfygames7237.Crusadersoffiction.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CrusadersOfFiction.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        blockWithItem(ModBlocks.WYSTERIUM_ORE);
        blockWithItem(ModBlocks.WYSTERIUM_BLOCK);
        blockWithItem(ModBlocks.CHARGED_COAL_BLOCK);

        horizontalBlock(ModBlocks.FORGE.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/forge")));
        horizontalBlock(ModBlocks.STRUCTURE_BUILDER.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/structure_builder")));
        horizontalBlock(ModBlocks.BLOCK_COMPRESSOR.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/block_compressor")));

        logBlock(((RotatedPillarBlock) ModBlocks.MAHOGANY_LOG.get()));
        axisBlock(((RotatedPillarBlock) ModBlocks.MAHOGANY_WOOD.get()), blockTexture(ModBlocks.MAHOGANY_LOG.get()), blockTexture(ModBlocks.MAHOGANY_LOG.get()));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_MAHOGANY_LOG.get(), new ResourceLocation(CrusadersOfFiction.MOD_ID, "block/stripped_mahogany_log"),
                new ResourceLocation(CrusadersOfFiction.MOD_ID, "block/stripped_mahogany_log_top"));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_MAHOGANY_WOOD.get(), new ResourceLocation(CrusadersOfFiction.MOD_ID, "block/stripped_mahogany_log"),
                new ResourceLocation(CrusadersOfFiction.MOD_ID, "block/stripped_mahogany_log"));

        logBlock(((RotatedPillarBlock) ModBlocks.BOREAL_LOG.get()));
        axisBlock(((RotatedPillarBlock) ModBlocks.BOREAL_WOOD.get()), blockTexture(ModBlocks.BOREAL_LOG.get()), blockTexture(ModBlocks.BOREAL_LOG.get()));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_BOREAL_LOG.get(), new ResourceLocation(CrusadersOfFiction.MOD_ID, "block/stripped_boreal_log"),
                new ResourceLocation(CrusadersOfFiction.MOD_ID, "block/stripped_boreal_log_top"));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_BOREAL_WOOD.get(), new ResourceLocation(CrusadersOfFiction.MOD_ID, "block/stripped_boreal_log"),
                new ResourceLocation(CrusadersOfFiction.MOD_ID, "block/stripped_boreal_log"));

        blockItem(ModBlocks.MAHOGANY_LOG);
        blockItem(ModBlocks.MAHOGANY_WOOD);
        blockItem(ModBlocks.STRIPPED_MAHOGANY_LOG);
        blockItem(ModBlocks.STRIPPED_MAHOGANY_WOOD);
        blockWithItem(ModBlocks.MAHOGANY_PLANKS);
        leavesBlock(ModBlocks.MAHOGANY_LEAVES);
        saplingBlock(ModBlocks.MAHOGANY_SAPLING);

        blockItem(ModBlocks.BOREAL_LOG);
        blockItem(ModBlocks.BOREAL_WOOD);
        blockItem(ModBlocks.STRIPPED_BOREAL_LOG);
        blockItem(ModBlocks.STRIPPED_BOREAL_WOOD);
        blockWithItem(ModBlocks.BOREAL_PLANKS);
        leavesBlock(ModBlocks.BOREAL_LEAVES);
        saplingBlock(ModBlocks.BOREAL_SAPLING);

        blockWithItem(ModBlocks.JUNGLE_GRASS_BLOCK);
        blockWithItem(ModBlocks.GLOWING_MUSHROOM_BLOCK);

        mushroomBlock(ModBlocks.GLOWING_MUSHROOM.get(), "glowing_mushroom");
        mushroomBlock(ModBlocks.JUNGLE_GRASS.get(), "jungle_grass");



    }

    private void mushroomBlock(Block block, String name) {
        ResourceLocation texture = modLoc("block/" + name);

        ModelFile model = models().cross(name, texture)
                .renderType("cutout");

        simpleBlock(block, model);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void leavesBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().cubeAll(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void saplingBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void blockItem(RegistryObject<Block> blockRegistryObject, String appendix) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile("crusadersoffiction:block/" + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath() + appendix));
    }

    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile("crusadersoffiction:block/" + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath()));
    }
}
