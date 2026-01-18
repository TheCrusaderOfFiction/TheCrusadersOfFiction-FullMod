package net.wolfygames7237.Crusadersoffiction.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.wolfygames7237.Crusadersoffiction.entity.ModEntities;

public class EnchantedSwordProjectileEntity extends Projectile {
    private static final EntityDataAccessor<Boolean> HIT =
            SynchedEntityData.defineId(EnchantedSwordProjectileEntity.class, EntityDataSerializers.BOOLEAN);
    private int counter = 0;


    public EnchantedSwordProjectileEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public EnchantedSwordProjectileEntity(Level level, Player player) {
        super(ModEntities.ENCHANTED_SWORD_PROJECTILE.get(), level);
        setOwner(player);
        BlockPos blockpos = player.blockPosition();
        double d0 = (double)blockpos.getX() + 0.5D;
        double d1 = (double)blockpos.getY() + 1.75D;
        double d2 = (double)blockpos.getZ() + 3D;
        this.moveTo(d0, d1, d2, this.getYRot(), this.getXRot());
    }

    @Override
    public void tick() {
        super.tick();
        if(this.entityData.get(HIT)) {
            if(this.tickCount >= counter) {
                this.discard();
            }
        }

        if (this.tickCount >= 300) {
            this.remove(RemovalReason.DISCARDED);
        }

        Vec3 vec3 = this.getDeltaMovement();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult))
            this.onHit(hitresult);

        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();

        double d5 = vec3.x;
        double d6 = vec3.y;
        double d7 = vec3.z;

        if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            this.discard();
        } else if (this.isInWaterOrBubble()) {
            this.discard();
        } else {
            this.setDeltaMovement(vec3.scale(0.99F));
            this.setPos(d0, d1, d2);
        }
    }
    private float normalDamage;
    private float pierceDamage;

    public void setDamage(float totalDamage, float piercePercent) {
        piercePercent = Mth.clamp(piercePercent, 0.0F, 1.0F);

        this.pierceDamage = totalDamage * piercePercent;
        this.normalDamage = totalDamage - this.pierceDamage;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        if (this.level().isClientSide()) return;

        Entity hitEntity = hitResult.getEntity();
        Entity owner = this.getOwner();

        if (hitEntity == owner) return;

        LivingEntity livingOwner =
                owner instanceof LivingEntity living ? living : null;

        boolean hurt = false;

        // Normal damage (affected by armor)
        if (normalDamage > 0.0F) {
            hurt |= hitEntity.hurt(
                    this.damageSources().mobProjectile(this, livingOwner),
                    normalDamage
            );
        }

        // Armor-piercing damage (MAGIC ignores armor)
        if (pierceDamage > 0.0F) {
            hurt |= hitEntity.hurt(
                    this.damageSources().magic(),
                    pierceDamage
            );
        }

        if (hurt) {
            // Optional effects (safe to re-enable)
        /*
        if (hitEntity instanceof LivingEntity livingHit) {
            livingHit.addEffect(
                new MobEffectInstance(MobEffects.POISON, 100, 1),
                owner
            );
        }
        */
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        if(this.level().isClientSide()) {
            return;
        }

        if(hitResult.getType() == HitResult.Type.ENTITY && hitResult instanceof EntityHitResult entityHitResult) {
            Entity hit = entityHitResult.getEntity();
            Entity owner = this.getOwner();
            if(owner != hit) {
                this.entityData.set(HIT, true);
                counter = this.tickCount + 5;
            }
        } else {
            this.entityData.set(HIT, true);
            counter = this.tickCount + 5;
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(HIT, false);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}