package net.wolfygames7237.Crusadersoffiction.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import net.wolfygames7237.Crusadersoffiction.Item.structure.StoneCompressedPlacerItem;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;
import net.wolfygames7237.Crusadersoffiction.blocks.entity.BlockCompressorBlockEntity;
import net.wolfygames7237.Crusadersoffiction.blocks.entity.StructureBuilderBlockEntity;
import org.jetbrains.annotations.NotNull;

public class BlockCompressorMenu extends AbstractContainerMenu {

    public final BlockCompressorBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public BlockCompressorMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, (BlockCompressorBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public BlockCompressorMenu(int id, Inventory inv, BlockCompressorBlockEntity entity, ContainerData data) {
        super(ModMenuTypes.BLOCK_COMPRESSOR_MENU.get(), id);

        this.blockEntity = entity;
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            // INPUT
            addSlot(new SlotItemHandler(handler, 0, 45, 33) {
                @Override
                public int getMaxStackSize(@NotNull ItemStack stack) {
                    // Allow up to 4 beds in input slot
                    if (stack.getItem() instanceof BedItem) {
                        return 4;
                    }
                    return super.getMaxStackSize(stack);
                }
            });

            // OUTPUT
            addSlot(new SlotItemHandler(handler, 1, 139, 33) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
        });

        addDataSlots(data);
    }

    // Vanilla inventory constants
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 2;

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Vanilla → TE (player inventory to block entity)
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackToCustom(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX,
                    TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false))
                return ItemStack.EMPTY;
        }
        // TE → Vanilla (block entity to player inventory)
        else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX,
                    VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false))
                return ItemStack.EMPTY;
        } else {
            return ItemStack.EMPTY;
        }

        if (sourceStack.isEmpty()) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }
    private boolean moveItemStackToCustom(ItemStack stack, int startIndex, int endIndex, boolean reverse) {
        boolean success = false;
        int i = reverse ? endIndex - 1 : startIndex;

        while (stack.getCount() > 0 && (reverse ? i >= startIndex : i < endIndex)) {
            Slot slot = this.slots.get(i);
            ItemStack slotStack = slot.getItem();

            // Only stack if same item & tags
            if (!slotStack.isEmpty() && ItemStack.isSameItemSameTags(stack, slotStack)) {
                int maxStack = slot.getMaxStackSize(stack); // <-- respect custom slot max
                int space = maxStack - slotStack.getCount();
                if (space > 0) {
                    int toMove = Math.min(stack.getCount(), space);
                    slotStack.grow(toMove);
                    stack.shrink(toMove);
                    slot.setChanged();
                    success = true;
                }
            }

            i += reverse ? -1 : 1;
        }

        // Try to place in empty slot if anything left
        i = reverse ? endIndex - 1 : startIndex;
        while (stack.getCount() > 0 && (reverse ? i >= startIndex : i < endIndex)) {
            Slot slot = this.slots.get(i);
            if (slot.getItem().isEmpty() && slot.mayPlace(stack)) {
                int maxStack = slot.getMaxStackSize(stack);
                ItemStack toInsert = stack.copy();
                toInsert.setCount(Math.min(stack.getCount(), maxStack));
                slot.set(toInsert);
                stack.shrink(toInsert.getCount());
                slot.setChanged();
                success = true;
            }

            i += reverse ? -1 : 1;
        }

        return success;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.BLOCK_COMPRESSOR.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }
    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);  // Max Progress
        int progressArrowSize = 26; // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
}
