package net.wolfygames7237.Crusadersoffiction.datagen;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.data.event.GatherDataEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class StructureJsonGenerator implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final PackOutput packOutput;
    private final String modid;

    public StructureJsonGenerator(PackOutput packOutput, String modid) {
        this.packOutput = packOutput;
        this.modid = modid;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return CompletableFuture.runAsync(() -> {
            try {
                // Input: src/main/resources/data/<modid>/structures
                Path projectRoot = Path.of("").toAbsolutePath().getParent();
                Path inputDir = projectRoot.resolve(
                        "src/main/resources/data/" + modid + "/structures"
                );
                if (!Files.exists(inputDir)) {
                    System.out.println("[StructureJsonGenerator] Input folder not found: " + inputDir.toAbsolutePath());
                    return;
                }

                // Output: src/generated/resources/data/<modid>/structures
                Path outputDir = packOutput.getOutputFolder()
                        .resolve("assets/" + modid + "/structures");
                Files.createDirectories(outputDir);

                Files.list(inputDir)
                        .filter(path -> path.toString().endsWith(".nbt"))
                        .filter(path -> !path.getFileName().toString().contains("_exclude"))
                        .forEach(nbtPath -> {
                            try (InputStream in = Files.newInputStream(nbtPath)) {
                                CompoundTag nbt = NbtIo.readCompressed(in);

// Unwrap structure root
                                CompoundTag rot = nbt;
                                if (rot.size() == 1) {
                                    String key = rot.getAllKeys().iterator().next();
                                    rot = rot.getCompound(key);
                                }

                                ListTag blocks = rot.getList("blocks", 10);
                                ListTag palette = rot.getList("palette", 10);;

                                JsonArray jsonBlocks = new JsonArray();

                                for (int i = 0; i < blocks.size(); i++) {
                                    CompoundTag blockTag = blocks.getCompound(i);

                                    ListTag posList = blockTag.getList("pos", Tag.TAG_INT);
                                    if (posList.size() != 3) continue;

                                    int x = posList.getInt(0);
                                    int y = posList.getInt(1);
                                    int z = posList.getInt(2);

                                    int stateIndex = blockTag.getInt("state");
                                    if (stateIndex < 0 || stateIndex >= palette.size()) continue;

                                    CompoundTag paletteEntry = palette.getCompound(stateIndex);
                                    String blockName = paletteEntry.getString("Name");

                                    JsonObject jb = new JsonObject();
                                    jb.addProperty("x", x);
                                    jb.addProperty("y", y);
                                    jb.addProperty("z", z);
                                    jb.addProperty("block", blockName);

                                    jsonBlocks.add(jb);
                                }

                                JsonObject root = new JsonObject();
                                root.add("blocks", jsonBlocks);

                                String name = nbtPath.getFileName().toString().replace(".nbt", "");
                                Path jsonPath = outputDir.resolve(name + ".json");

                                String json = GSON.toJson(root);

                                cache.writeIfNeeded(
                                        jsonPath,
                                        json.getBytes(StandardCharsets.UTF_8),
                                        Hashing.sha1().hashString(json, StandardCharsets.UTF_8)
                                );

                                System.out.println("[StructureJsonGenerator] Generated JSON: " + jsonPath);

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });

            } catch (Exception e) {
                throw new RuntimeException("[StructureJsonGenerator] Failed", e);
            }
        });
    }

    @Override
    public String getName() {
        return "Structure JSON Generator";
    }
}
