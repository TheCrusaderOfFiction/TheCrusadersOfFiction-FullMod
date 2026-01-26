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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.wolfygames7237.Crusadersoffiction.recipe.ModRecipeTypes;
import net.wolfygames7237.Crusadersoffiction.recipe.BlockCompressorRecipe;
import net.wolfygames7237.Crusadersoffiction.screen.BlockCompressorMenu;
import net.wolfygames7237.Crusadersoffiction.screen.ForgeMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BlockCompressorBlockEntity extends BlockEntity implements MenuProvider {

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int MAX_PROGRESS = 100;

    private int progress = 0;

    /* ----------------- DATA ----------------- */

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> MAX_PROGRESS;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) progress = value;
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    /* ----------------- INVENTORY ----------------- */

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return slot == INPUT_SLOT;
        }

        @Override
        public int getSlotLimit(int slot) {
            ItemStack stack = getStackInSlot(slot);

            // Beds stack to 4 inside this block only
            if (!stack.isEmpty() && stack.getItem() instanceof BedItem) {
                return 4;
            }

            return super.getSlotLimit(slot);
        }
    };

    /* ----------------- CONSTRUCTOR ----------------- */

    public BlockCompressorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLOCK_COMPRESSOR_BE.get(), pos, state);
    }

    /* ----------------- TICK ----------------- */

    public static <T extends BlockEntity> BlockEntityTicker<T> ticker(
            Level level, BlockPos pos, BlockState state, T be
    ) {
        if (!level.isClientSide && be instanceof BlockCompressorBlockEntity compressor) {
            compressor.tickCrafting();
        }
        return null;
    }

    private void tickCrafting() {
        if (level == null || level.isClientSide) return;

        ItemStack input = itemHandler.getStackInSlot(INPUT_SLOT);

        if (input.isEmpty()) {
            progress = 0;
            return;
        }

        Optional<BlockCompressorRecipe> recipeOpt = getCurrentRecipe();
        if (recipeOpt.isEmpty()) {
            progress = 0;
            return;
        }

        BlockCompressorRecipe recipe = recipeOpt.get();

        ItemStack result = recipe.getResultItem(level.registryAccess());
        if (!canInsertIntoOutput(result)) {
            progress = 0;
            return;
        }

        progress++;

        if (progress >= MAX_PROGRESS) {
            int required = recipe.getRequiredAmounts()[0];

            itemHandler.extractItem(INPUT_SLOT, required, false);

            ItemStack outputSlot = itemHandler.getStackInSlot(OUTPUT_SLOT);
            if (outputSlot.isEmpty()) {
                itemHandler.setStackInSlot(OUTPUT_SLOT, result.copy());
            } else {
                outputSlot.grow(result.getCount());
            }

            progress = 0;
            setChanged();
        }
    }

    /* ----------------- HELPERS ----------------- */

    private boolean canInsertIntoOutput(ItemStack stack) {
        ItemStack output = itemHandler.getStackInSlot(OUTPUT_SLOT);

        if (output.isEmpty()) return true;

        if (!ItemStack.isSameItemSameTags(output, stack)) return false;

        int slotLimit = itemHandler.getSlotLimit(OUTPUT_SLOT);
        return output.getCount() + stack.getCount() <= slotLimit;
    }

    private Optional<BlockCompressorRecipe> getCurrentRecipe() {
        if (level == null || level.isClientSide) return Optional.empty();

        SimpleContainer container = new SimpleContainer(1);
        container.setItem(0, itemHandler.getStackInSlot(INPUT_SLOT));

        for (BlockCompressorRecipe recipe :
                level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.COMPRESSOR.get())) {

            if (recipe.matches(container, level)) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

    /* ----------------- SAVE / LOAD ----------------- */

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inv", itemHandler.serializeNBT());
        tag.putInt("progress", progress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inv"));
        progress = tag.getInt("progress");
    }

    /* ----------------- MENU ----------------- */

    @Override
    public Component getDisplayName() {
        return Component.literal("Block Compressor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new BlockCompressorMenu(id, inv, this, data);
    }

    /* ----------------- CAPS ----------------- */

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
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();
        return super.getCapability(cap, side);
    }

    /* ----------------- DROPS ----------------- */

    public void drops() {
        Containers.dropContents(level, worldPosition,
                new SimpleContainer(itemHandler.getStackInSlot(0), itemHandler.getStackInSlot(1)));
    }
}
