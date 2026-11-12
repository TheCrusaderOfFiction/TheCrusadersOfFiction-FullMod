package net.wolfygames7237.Crusadersoffiction.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** Adds the custom jump-breaking behavior to ordinary ice. */
@Mod.EventBusSubscriber(modid = CrusadersOfFiction.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class IceBehaviorHandler {
    private static final Set<UUID> PLAYERS_WHO_JUMPED = new HashSet<>();

    private IceBehaviorHandler() {}

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && !player.isSpectator()) {
            PLAYERS_WHO_JUMPED.add(player.getUUID());
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || !PLAYERS_WHO_JUMPED.remove(player.getUUID())) {
            return;
        }

        // The fall event runs on landing, while the player's feet are at the block top.
        BlockPos icePosition = BlockPos.containing(player.getX(), player.getY() - 0.01, player.getZ());
        if (player.serverLevel().getBlockState(icePosition).is(Blocks.ICE)) {
            player.serverLevel().destroyBlock(icePosition, true, player);
        }
    }
}
