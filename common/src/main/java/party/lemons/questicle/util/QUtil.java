package party.lemons.questicle.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 * Every project has a random utils class
 */
public class QUtil
{
    public static String titleCase(final String words) {
        return Stream.of(words.trim().split("\\s"))
                .filter(word -> word.length() > 0)
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    public static List<StructureStart> getStructuresAt(ServerLevel level, BlockPos blockPos)
    {
        StructureManager structureManager = level.structureManager();

        List<StructureStart> structureList = new ArrayList<>();

        List<StructureStart> possibleStarts = structureManager.startsForStructure(new ChunkPos(blockPos), structure -> true);
        for(StructureStart structureStart : possibleStarts)
        {
            if (structureManager.structureHasPieceAt(blockPos, structureStart)) {
                structureList.add(structureStart);
            }
        }

        return structureList;
    }

    public static void giveOrDrop(ServerPlayer player, ItemStack stack)
    {
        if(!player.addItem(stack))
            player.drop(stack, false);
    }
}
