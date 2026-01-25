package net.wolfygames7237.Crusadersoffiction.Item.structure.renderer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = "crusadersoffiction", value = Dist.CLIENT)
public class StructureJsonLoader {

    private static final Gson GSON = new Gson();

    private static final Map<ResourceLocation, List<BlockInfo>> CACHE = new HashMap<>();

    private static class JsonBlock {
        int x, y, z;
        String block;
    }

    public record BlockInfo(BlockPos pos, Block block) {}

    public static List<BlockInfo> loadBlockInfos(ResourceLocation jsonResource) {
        if (CACHE.containsKey(jsonResource)) {
            return CACHE.get(jsonResource);
        }

        List<BlockInfo> infos = new ArrayList<>();

        try {
            ResourceManager manager = Minecraft.getInstance().getResourceManager();
            var resource = manager.getResource(jsonResource).orElseThrow();

            try (Reader reader = new InputStreamReader(resource.open())) {
                JsonObject root = GSON.fromJson(reader, JsonObject.class);
                JsonArray blocks = root.getAsJsonArray("blocks");
                if (blocks == null) return List.of();

                for (JsonElement el : blocks) {
                    JsonObject obj = el.getAsJsonObject();

                    int x = obj.get("x").getAsInt();
                    int y = obj.get("y").getAsInt();
                    int z = obj.get("z").getAsInt();
                    String blockId = obj.get("block").getAsString();

                    Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(blockId));
                    infos.add(new BlockInfo(new BlockPos(x, y, z), block));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CACHE.put(jsonResource, infos);
        return infos;
    }


}
