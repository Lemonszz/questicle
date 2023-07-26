package party.lemons.questicle.party;

import com.mojang.serialization.Codec;

public record QuestPartyType<T extends QuestParty>(Codec<T> codec) {
}
