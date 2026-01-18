package net.wolfygames7237.Crusadersoffiction.enchantments.handler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.enchantments.EnchSwordEnchantment;
import net.wolfygames7237.Crusadersoffiction.enchantments.ModEnchantments;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(
        modid = CrusadersOfFiction.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class EnchantedSwordSwingHandler {

    private static final Map<UUID, Boolean> SWING_STATE = new HashMap<>();
    private static final Map<UUID, Long> COOLDOWNS = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        if (player.level().isClientSide()) return;

        ItemStack held = player.getMainHandItem();

        int enchantLevel = EnchantmentHelper.getItemEnchantmentLevel(
                ModEnchantments.ENCH_SWORD_PROJECTILE.get(),
                held
        );

        if (enchantLevel <= 0) {
            SWING_STATE.put(player.getUUID(), false);
            return;
        }

        boolean currentlySwinging = player.swinging;
        boolean wasSwinging = SWING_STATE.getOrDefault(player.getUUID(), false);

        if (currentlySwinging && !wasSwinging) {

            long gameTime = player.level().getGameTime();
            long nextAllowed = COOLDOWNS.getOrDefault(player.getUUID(), 0L);

            if (gameTime >= nextAllowed) {
                EnchSwordEnchantment enchant =
                        (EnchSwordEnchantment) ModEnchantments.ENCH_SWORD_PROJECTILE.get();

                enchant.fireProjectile(player, enchantLevel);

                COOLDOWNS.put(
                        player.getUUID(),
                        gameTime + getCooldownForLevel(enchantLevel)
                );
            }
        }

        SWING_STATE.put(player.getUUID(), currentlySwinging);
    }

    private static int getCooldownForLevel(int level) {
        if (level == 1) return 0;
        if (level == 2) return 40;
        if (level == 3) return 20;
        return 60;
    }
}
