package party.lemons.questicle.quest.icon.impl;

import com.mojang.serialization.Codec;
import party.lemons.questicle.quest.icon.QuestIconType;
import party.lemons.questicle.quest.icon.QuestIconTypes;
import party.lemons.questicle.quest.icon.QuestIcon;

public class BlankQuestIcon implements QuestIcon {
    public static final Codec<BlankQuestIcon> CODEC = Codec.unit(() -> BlankQuestIcon.INSTANCE);
    public static final BlankQuestIcon INSTANCE = new BlankQuestIcon();
    @Override
    public QuestIconType<?> type() {
        return QuestIconTypes.BLANK.get();
    }
}
