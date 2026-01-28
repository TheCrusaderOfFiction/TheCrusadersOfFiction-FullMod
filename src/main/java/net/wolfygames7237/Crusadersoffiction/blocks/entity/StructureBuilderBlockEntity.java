package net.wolfygames7237.Crusadersoffiction.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.wolfygames7237.Crusadersoffiction.Item.ModItem;
import net.wolfygames7237.Crusadersoffiction.Item.structure.StoneCompressedPlacerItem;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;
import net.wolfygames7237.Crusadersoffiction.recipe.ForgeRecipe;
import net.wolfygames7237.Crusadersoffiction.recipe.ModRecipeTypes;
import net.wolfygames7237.Crusadersoffiction.recipe.ModRecipes;
import net.wolfygames7237.Crusadersoffiction.recipe.StructureBuilderRecipe;
import net.wolfygames7237.Crusadersoffiction.screen.StructureBuilderMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StructureBuilderBlockEntity extends BlockEntity implements MenuProvider {

    public static final int GRID_WIDTH = 5;
    public static final int GRID_HEIGHT = 4;
    public static final int INPUT_COUNT = GRID_WIDTH * GRID_HEIGHT; // 20
    private static final int OUTPUT_SLOT = 20;

    protected final ContainerData data;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    // --- Item handler with output-slot interception ---
    private final ItemStackHandler itemHandler = new ItemStackHandler(INPUT_COUNT + 1) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return slot >= 0 && slot < INPUT_COUNT; // only inputs allowed
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            // Only intercept output slot
            if (slot == OUTPUT_SLOT) {
                StructureBuilderRecipe recipe = getCurrentRecipe().orElse(null);
                if (recipe != null && !simulate) {
                    // Consume ingredients when player takes item
                    int[] requiredAmounts = recipe.getRequiredAmounts();
                    for (int i = 0; i < INPUT_COUNT; i++) {
                        int amt = i < requiredAmounts.length ? requiredAmounts[i] : 0;
                        if (amt > 0) extractItem(i, amt, false);
                    }
                }
            }
            return super.extractItem(slot, amount, simulate);
        }
        @Override
        public int getSlotLimit(int slot) {
            ItemStack stack = getStackInSlot(slot);

            // Beds stack to 4 inside this block only
            if (!stack.isEmpty() && stack.getItem() instanceof StoneCompressedPlacerItem) {
                return 32;
            }
            if (!stack.isEmpty() && stack.getItem() == Items.WATER_BUCKET) {
                return 2;
            }

            return super.getSlotLimit(slot);
        }
    };

    public StructureBuilderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STRUCTURE_BUILDER_BE.get(), pos, state);

        this.data = new ContainerData() {
            @Override
            public int get(int index) { return 0; }
            @Override
            public void set(int index, int value) {}
            @Override
            public int getCount() { return 2; } // e.g., progress + max progress
        };
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Structure Builder");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new StructureBuilderMenu(id, playerInventory, this, data);
    }

    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();
        return super.getCapability(cap, side);
    }

    /** Drop all items on break */
    public void drops() {
        Containers.dropContents(level, worldPosition, new SimpleContainer(itemHandler.getSlots()) {{
            for (int i = 0; i < itemHandler.getSlots(); i++) setItem(i, itemHandler.getStackInSlot(i));
        }});
    }

    /** Attempt to craft once (ghost crafting) */
    public void craftOnce() {
        if (level == null || level.isClientSide) return;

        Optional<StructureBuilderRecipe> recipeOpt = getCurrentRecipe();
        ItemStack currentOutput = itemHandler.getStackInSlot(OUTPUT_SLOT);

        if (recipeOpt.isEmpty()) {
            if (!currentOutput.isEmpty()) {
                itemHandler.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY); // clear ghost output
            }
            return;
        }

        StructureBuilderRecipe recipe = recipeOpt.get();

        SimpleContainer craftingInv = new SimpleContainer(INPUT_COUNT);
        for (int i = 0; i < INPUT_COUNT; i++)
            craftingInv.setItem(i, itemHandler.getStackInSlot(i));

        ItemStack result = recipe.assemble(craftingInv, level.registryAccess());
        if (result.isEmpty() || !canInsertIntoOutput(result)) {
            if (!currentOutput.isEmpty()) itemHandler.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);
            return;
        }

        // Only update output if it changed
        if (!ItemStack.isSameItemSameTags(currentOutput, result) || currentOutput.getCount() != result.getCount()) {
            itemHandler.setStackInSlot(OUTPUT_SLOT, result.copy());
            setChanged();
        }
    }

    private boolean canInsertIntoOutput(ItemStack result) {
        ItemStack output = itemHandler.getStackInSlot(OUTPUT_SLOT);
        if (output.isEmpty()) return true;
        return ItemStack.isSameItemSameTags(output, result)
                && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    /** Returns the current recipe if grid matches */
    private Optional<StructureBuilderRecipe> getCurrentRecipe() {
        if (level == null || level.isClientSide) return Optional.empty();

        SimpleContainer inv = new SimpleContainer(INPUT_COUNT);
        for (int i = 0; i < INPUT_COUNT; i++)
            inv.setItem(i, itemHandler.getStackInSlot(i));

        for (StructureBuilderRecipe recipe : level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.STRUCTURE.get())) {
            if (recipe.matches(inv, level)) return Optional.of(recipe);
        }
        return Optional.empty();
    }

    /** Ticker to update ghost crafting every tick */
    private int[] lastInputCounts = new int[INPUT_COUNT]; // tracks counts of input items

    /** Ticker to update ghost crafting when inputs change */
    public static <T extends BlockEntity> BlockEntityTicker<T> ticker(Level level, BlockPos pos, BlockState state, T be) {
        if (!(be instanceof StructureBuilderBlockEntity builder)) return null;
        if (level.isClientSide) return null; // server only

        boolean inputsChanged = false;

        // Check if any input slot changed
        for (int i = 0; i < INPUT_COUNT; i++) {
            int count = builder.itemHandler.getStackInSlot(i).getCount();
            if (count != builder.lastInputCounts[i]) {
                inputsChanged = true;
                builder.lastInputCounts[i] = count;
            }
        }

        if (inputsChanged) {
            // Only update ghost output if inputs changed
            ItemStack output = builder.itemHandler.getStackInSlot(OUTPUT_SLOT);
            Optional<StructureBuilderRecipe> recipeOpt = builder.getCurrentRecipe();

            if (recipeOpt.isPresent() && output.isEmpty()) {
                StructureBuilderRecipe recipe = recipeOpt.get();
                SimpleContainer craftingInv = new SimpleContainer(INPUT_COUNT);
                for (int i = 0; i < INPUT_COUNT; i++) {
                    craftingInv.setItem(i, builder.itemHandler.getStackInSlot(i));
                }

                ItemStack result = recipe.assemble(craftingInv, level.registryAccess());
                if (!result.isEmpty()) {
                    builder.itemHandler.setStackInSlot(OUTPUT_SLOT, result.copy());
                    builder.setChanged();
                }
            } else if (recipeOpt.isEmpty() && !output.isEmpty()) {
                // Clear ghost output if grid no longer matches
                builder.itemHandler.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);
                builder.setChanged();
            }
        }

        return null;
    }
}
