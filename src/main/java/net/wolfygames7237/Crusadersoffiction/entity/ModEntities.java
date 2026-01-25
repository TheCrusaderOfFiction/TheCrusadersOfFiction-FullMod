package net.wolfygames7237.Crusadersoffiction.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.entity.custom.EnchantedSwordProjectileEntity;
import net.wolfygames7237.Crusadersoffiction.entity.custom.EnchantedSwordProjectileEntity2;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CrusadersOfFiction.MOD_ID);


    public static final RegistryObject<EntityType<EnchantedSwordProjectileEntity>> ENCHANTED_SWORD_PROJECTILE =
            ENTITY_TYPES.register("enchanted_sword_projectile",
                    () -> EntityType.Builder.<EnchantedSwordProjectileEntity>of(EnchantedSwordProjectileEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("enchanted_sword_projectile"));
    public static final RegistryObject<EntityType<EnchantedSwordProjectileEntity2>> ENCHANTED_SWORD_PROJECTILE2 =
            ENTITY_TYPES.register("enchanted_sword_projectile2",
                    () -> EntityType.Builder.<EnchantedSwordProjectileEntity2>of(EnchantedSwordProjectileEntity2::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("enchanted_sword_projectile2"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
