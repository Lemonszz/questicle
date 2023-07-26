package party.lemons.questicle.resource;

import com.mojang.logging.LogUtils;
import dev.architectury.hooks.PackRepositoryHooks;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import org.slf4j.Logger;

import java.nio.file.Path;

public class GlobalDataPackHandler
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Path GLOBAL_DATA_FOLDER = Platform.getGameFolder().resolve("questicle/data");

    public static void init()
    {
        if(!GLOBAL_DATA_FOLDER.toFile().exists()) {
            GLOBAL_DATA_FOLDER.toFile().mkdirs();

            LOGGER.info("Creating Global Data Folder");
        }

    }

    public static void injectData(PackRepository packRepository)
    {
        PackRepositoryHooks.addSource(packRepository, new QRepositorySource(GLOBAL_DATA_FOLDER, PackType.SERVER_DATA, PackSource.BUILT_IN));
    }
}
