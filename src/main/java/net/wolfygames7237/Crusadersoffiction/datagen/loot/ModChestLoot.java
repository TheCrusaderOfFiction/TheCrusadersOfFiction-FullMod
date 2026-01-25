package net.wolfygames7237.Crusadersoffiction.datagen.loot;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.util.ModTags;

import java.util.List;
import java.util.function.BiConsumer;

public class ModChestLoot implements LootTableSubProvider {

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(new ResourceLocation(CrusadersOfFiction.MOD_ID, "chests/test"),
                LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.GOLD_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                                .add(LootItem.lootTableItem(Items.DIAMOND).when(LootItemRandomChanceCondition.randomChance(0.25f)))
                                .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(1))
                                .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(5)
                                )));
        consumer.accept(new ResourceLocation(CrusadersOfFiction.MOD_ID, "chests/shrine"),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2,5))
                        .add(LootItem.lootTableItem(Blocks.COBBLESTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
                        )
                        .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1,5))
                        .add(LootItem.lootTableItem(Items.RAW_IRON).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
                        .add(LootItem.lootTableItem(Items.RAW_IRON).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        )
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.IRON_SWORD).when(LootItemRandomChanceCondition.randomChance(0.1f)))
                        .add(LootItem.lootTableItem(Blocks.AIR).when(LootItemRandomChanceCondition.randomChance(0.9f)))
                        )
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Blocks.COBBLESTONE).setWeight(2))
                        .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK)
                                  .when(LootItemRandomChanceCondition.randomChance(0.0f))
                                  .apply(SetNbtFunction.setTag(createSharpnessBook())))
                        .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK)
                                .setWeight(1)
                                  .apply(SetNbtFunction.setTag(createEnchantedSwordBook(
                                  new ResourceLocation("crusadersoffiction", "enchanted_sword"), 1)))
                        )
                ));

    }
    private static CompoundTag createSharpnessBook() {
        CompoundTag tag = new CompoundTag();

        ListTag enchants = new ListTag();
        CompoundTag sharpness = new CompoundTag();
        sharpness.putString("id", "minecraft:sharpness");
        sharpness.putShort("lvl", (short) 1);

        enchants.add(sharpness);
        tag.put("StoredEnchantments", enchants);

        return tag;
    }
    private static CompoundTag createEnchantedSwordBook(ResourceLocation enchantId, int level) {
        CompoundTag root = new CompoundTag();

        ListTag enchants = new ListTag();
        CompoundTag enchant = new CompoundTag();

        enchant.putString("id", enchantId.toString());
        enchant.putShort("lvl", (short) level);

        enchants.add(enchant);
        root.put("StoredEnchantments", enchants);

        return root;
    }
}
