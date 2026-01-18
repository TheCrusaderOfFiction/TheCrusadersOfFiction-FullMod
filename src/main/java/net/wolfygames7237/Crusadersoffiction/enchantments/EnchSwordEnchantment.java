package net.wolfygames7237.Crusadersoffiction.enchantments;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.wolfygames7237.Crusadersoffiction.entity.ModEntities;
import net.wolfygames7237.Crusadersoffiction.entity.custom.EnchantedSwordProjectileEntity;

public class EnchSwordEnchantment extends Enchantment {
    protected EnchSwordEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
    public int getMinCost(int pEnchantmentLevel) {
        return pEnchantmentLevel * 25;
    }
    public int getMaxCost(int pEnchantmentLevel) {
        return this.getMinCost(pEnchantmentLevel) + 50;
    }
    public boolean isTreasureOnly() {
        return true;
    }
    public boolean checkCompatibility(Enchantment pEnch) {
        return super.checkCompatibility(pEnch) && pEnch != ModEnchantments.LIGHTNING_STRIKER.get();
    }

    private void spawn(
            ServerLevel level,
            Player player,
            EntityType<? extends EnchantedSwordProjectileEntity> type,
            Vec3 pos,
            Vec3 look,
            float damage,
            float piercePercent
    ) {
        EnchantedSwordProjectileEntity proj = type.create(level);
        if (proj == null) return;

        proj.setOwner(player);
        proj.setDamage(damage, piercePercent);
        proj.setPos(pos.x, pos.y, pos.z);
        proj.shoot(look.x, look.y, look.z, 1.5F, 0.0F);

        level.addFreshEntity(proj);
    }
    private float getSwordDamage(Player player) {
        double base = player.getAttributeValue(Attributes.ATTACK_DAMAGE);

        // Remove empty hand damage (1.0)
        return (float) Math.max(0, base - 1.0);
    }

    public void fireProjectile(Player player, int pLevel) {
        if (player.level().isClientSide()) return;

        ServerLevel level = (ServerLevel) player.level();
        ItemStack sword = player.getMainHandItem();

        float swordDamage = getSwordDamage(player);
        Vec3 look = player.getLookAngle();
        Vec3 spawnPos = player.position().add(0, 1.5, 0);

        if (pLevel == 1) {
            spawn(level, player, ModEntities.ENCHANTED_SWORD_PROJECTILE.get(),
                    spawnPos, look,
                    swordDamage * 0.1F, 0.20F);
        }

        if (pLevel == 2) {
            spawn(level, player, ModEntities.ENCHANTED_SWORD_PROJECTILE.get(),
                    spawnPos, look,
                    swordDamage * 0.2F, 0.40F);
        }

        if (pLevel == 3) {
            spawn(level, player, ModEntities.ENCHANTED_SWORD_PROJECTILE.get(),
                    spawnPos, look,
                    swordDamage * 0.3F, 0.60F);
        }
    }
    @Override
    public int getMaxLevel() {
        return 3;
    }
}
