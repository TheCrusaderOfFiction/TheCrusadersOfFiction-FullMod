package net.wolfygames7237.Crusadersoffiction.datagen.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import net.wolfygames7237.Crusadersoffiction.Item.ModItem;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
       this.dropSelf(ModBlocks.WYSTERIUM_BLOCK.get());
        this.dropSelf(ModBlocks.FORGE.get());
        this.dropSelf(ModBlocks.STRUCTURE_BUILDER.get());
        this.dropSelf(ModBlocks.BLOCK_COMPRESSOR.get());
        this.dropSelf(ModBlocks.CHARGED_COAL_BLOCK.get());


        this.add(ModBlocks.WYSTERIUM_ORE.get(),
                block -> createWysteriumOreDrops(ModBlocks.WYSTERIUM_ORE.get(), ModItem.RAWWYSTERIUM.get()));

        this.dropSelf(ModBlocks.MAHOGANY_LOG.get());
        this.dropSelf(ModBlocks.MAHOGANY_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_MAHOGANY_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_MAHOGANY_WOOD.get());
        this.dropSelf(ModBlocks.MAHOGANY_PLANKS.get());
        this.dropSelf(ModBlocks.MAHOGANY_SAPLING.get());

        this.dropSelf(ModBlocks.BOREAL_LOG.get());
        this.dropSelf(ModBlocks.BOREAL_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_BOREAL_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_BOREAL_WOOD.get());
        this.dropSelf(ModBlocks.BOREAL_PLANKS.get());
        this.dropSelf(ModBlocks.BOREAL_SAPLING.get());

        this.add(ModBlocks.GLOWING_MUSHROOM.get(),
                block -> createGlowingMushroomDrops(block));
        this.add(ModBlocks.JUNGLE_GRASS.get(),
                block -> createJungleGrassDrops(block));


        this.add(ModBlocks.MAHOGANY_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.MAHOGANY_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.add(ModBlocks.BOREAL_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.BOREAL_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));

        this.dropOther(ModBlocks.JUNGLE_GRASS_BLOCK.get(), Blocks.MUD);
        this.dropOther(ModBlocks.GLOWING_MUSHROOM_BLOCK.get(), Blocks.MUD);


    }

    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }
    protected LootTable.Builder createWysteriumOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 1.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    protected LootTable.Builder createGlowingMushroomDrops(Block block) {
        return LootTable.lootTable()

                // Normal mushroom drop
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(block)))

                // Seed drop chance
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .when(LootItemRandomChanceCondition.randomChance(0.025f)) // 25% chance
                        .add(LootItem.lootTableItem(ModItem.MUSHROOM_SEEDS.get())));
    }

    protected LootTable.Builder createJungleGrassDrops(Block block) {
        return LootTable.lootTable()
                // Seed drop chance
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .when(LootItemRandomChanceCondition.randomChance(0.025f)) // 25% chance
                        .add(LootItem.lootTableItem(ModItem.JUNGLE_SEEDS.get())));
    }
}
