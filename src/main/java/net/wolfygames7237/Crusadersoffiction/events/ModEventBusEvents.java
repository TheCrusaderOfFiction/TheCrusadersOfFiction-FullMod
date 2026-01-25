package net.wolfygames7237.Crusadersoffiction.events;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.entity.client.EnchantedSwordProjectileModel;
import net.wolfygames7237.Crusadersoffiction.entity.layers.ModModelLayers;

@Mod.EventBusSubscriber(modid = CrusadersOfFiction.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.ENCHANTED_SWORD_PROJECTILE_LAYER, EnchantedSwordProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ENCHANTED_SWORD_PROJECTILE2_LAYER, EnchantedSwordProjectileModel::createBodyLayer);

    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {

    }

    @SubscribeEvent
    public static void registerSpawnPlacement(SpawnPlacementRegisterEvent event) {

    }
}