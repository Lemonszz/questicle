package party.lemons.questicle.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.FileUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.linkfs.LinkFileSystem;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import party.lemons.questicle.party.QuestPartyTypes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/***
 * Modified version of vanilla's FolderRepositorySource but handles missing metadata n shizz
 */
public class QRepositorySource implements RepositorySource
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Path folder;
    private final PackType packType;
    private final PackSource packSource;

    public QRepositorySource(Path path, PackType packType, PackSource packSource) {
        this.folder = path;
        this.packType = packType;
        this.packSource = packSource;
    }

    private static String nameFromPath(Path path) {
        return path.getFileName().toString();
    }

    @Override
    public void loadPacks(Consumer<Pack> consumer)
    {
        try
        {
            FileUtil.createDirectoriesSafe(this.folder);
            discoverPacks(this.folder, false,
                    (path, resourcesSupplier) ->
                    {
                        String string = nameFromPath(path);
                        Pack pack = Pack.readMetaAndCreate(
                                "file/" + string, Component.literal(string), false, resourcesSupplier, this.packType, Pack.Position.TOP, this.packSource
                        );
                        if (pack != null) {
                            consumer.accept(pack);
                        }
                    }
            );
        } catch (IOException var3) {
            LOGGER.warn("Failed to list packs in {}", this.folder, var3);
        }
    }

    public static void discoverPacks(Path path, boolean bl, BiConsumer<Path, Pack.ResourcesSupplier> biConsumer) throws IOException {
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);

        try {
            for(Path path2 : directoryStream) {
                Pack.ResourcesSupplier resourcesSupplier = detectPackResources(path2, bl);
                if (resourcesSupplier != null) {
                    biConsumer.accept(path2, resourcesSupplier);
                }
            }
        } catch (Throwable var8) {
            if (directoryStream != null) {
                try {
                    directoryStream.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }

            throw var8;
        }

        directoryStream.close();
    }

    @Nullable
    public static Pack.ResourcesSupplier detectPackResources(Path path, boolean bl) {
        BasicFileAttributes basicFileAttributes;
        try {
            basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (NoSuchFileException var5) {
            return null;
        } catch (IOException var6) {
            LOGGER.warn("Failed to read properties of '{}', ignoring", path, var6);
            return null;
        }

        if (basicFileAttributes.isDirectory())
        {
            if(!Files.isRegularFile(path.resolve("pack.mcmeta")))
            {
                createPackMeta(path);
            }
            return string -> new PathPackResources(string, path, bl);
        }
        else
        {
            if (basicFileAttributes.isRegularFile() && path.getFileName().toString().endsWith(".zip")) {
                FileSystem fileSystem = path.getFileSystem();
                if (fileSystem == FileSystems.getDefault() || fileSystem instanceof LinkFileSystem) {
                    File file = path.toFile();
                    return string -> new FilePackResources(string, file, bl);
                }
            }

            LOGGER.info("Found non-pack entry '{}', ignoring", path);
            return null;
        }
    }

    private static void createPackMeta(Path path)
    {
        Path metapath = path.resolve("pack.mcmeta");
        LOGGER.info("Writing Meta for {}", metapath);
        String json = """
                {
                  "pack": {
                    "description": "Questicle Default Metadata :)",
                    "pack_format": 15
                  }
                }
                """;

        File file = metapath.toFile();
        try(FileWriter writer = new FileWriter(file))
        {
           writer.append(json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
