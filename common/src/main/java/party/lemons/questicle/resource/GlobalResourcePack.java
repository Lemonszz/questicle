package party.lemons.questicle.resource;

import com.mojang.logging.LogUtils;
import dev.architectury.hooks.PackRepositoryHooks;
import dev.architectury.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import org.slf4j.Logger;

import java.nio.file.Path;

public class GlobalResourcePack
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Path GLOBAL_ASSETS_FOLDER = Platform.getGameFolder().resolve("questicle/assets");


    public static void init()
    {
        if(!GLOBAL_ASSETS_FOLDER.toFile().exists()) {
            GLOBAL_ASSETS_FOLDER.toFile().mkdirs();

            LOGGER.info("Creating Assets Folder");
        }

        PackRepositoryHooks.addSource(Minecraft.getInstance().getResourcePackRepository(), new QRepositorySource(GLOBAL_ASSETS_FOLDER, PackType.CLIENT_RESOURCES, PackSource.BUILT_IN));
    }
}
