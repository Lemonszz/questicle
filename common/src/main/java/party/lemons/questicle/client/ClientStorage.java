package party.lemons.questicle.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import party.lemons.questicle.client.gui.QuestScreen;
import party.lemons.questicle.network.C2sSendClaimRequest;
import party.lemons.questicle.party.QuestParty;
import party.lemons.questicle.quest.QuestList;
import party.lemons.questicle.quest.quest.Quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientStorage
{
    //TODO: better client storage.
    //- Clear on leave server
    //- Rerequesting
    public static Map<ResourceLocation, Quest> clientQuests = new HashMap<>();
    private static Map<ResourceLocation, QuestList> clientQuestLists = new HashMap<>();
    private static List<QuestList> sortedQuestLists = new ArrayList<>();
    public static QuestParty clientParty;

    public static void sendClaimRequest(Quest displayQuest)
    {
        new C2sSendClaimRequest(displayQuest).sendToServer();
    }

    public static void sendClaimAll()   //TODO: needs button for this
    {
        new C2sSendClaimRequest().sendToServer();
    }

    public static void setClientQuestLists(Map<ResourceLocation, QuestList> lists)
    {
        clientQuestLists = lists;
        sortedQuestLists = new ArrayList<>(lists.values());
        sortedQuestLists.sort(QuestList::compareTo);

        if(Minecraft.getInstance().screen instanceof QuestScreen questScreen)
        {
            questScreen.init(Minecraft.getInstance(), Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight());
        }
        else
        {
            QuestScreen.resetPersistentData();
        }

    }

    public static Map<ResourceLocation, QuestList> getQuestLists() {
        return clientQuestLists;
    }

    public static List<QuestList> getSortedQuestLists()
    {
        return sortedQuestLists;
    }
}
