package party.lemons.questicle.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2i;

import java.util.List;
import java.util.Optional;

public class QCodecs
{
    public static final Codec<Vector2i> VEC2I = Codec.INT.listOf().comapFlatMap(
            (list) -> Util.fixedSize(list, 2).map((l) -> new Vector2i(l.get(0), l.get(1))),
            (vec2i) -> List.of(vec2i.x(), vec2i.y())
    );

    /**
     * Similar to ItemStack.CODEC, but "count" is now optional and no longer capitialized
     */
    public static final Codec<ItemStack> ITEM_STACK = RecordCodecBuilder.create(
            instance -> instance.group(
                            BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(ItemStack::getItem),
                            Codec.INT.optionalFieldOf("count", 1).forGetter(ItemStack::getCount),
                            CompoundTag.CODEC.optionalFieldOf("tag").forGetter(itemStack -> Optional.ofNullable(itemStack.getTag()))
                    )
                    .apply(instance, (item, count, tag)->{
                        ItemStack stack = new ItemStack(item, count);
                        tag.ifPresent(stack::setTag);
                        return stack;
                    })
    );


    public static final Codec<ItemStack> SIMPLE_ITEM_STACK = Codec.either(BuiltInRegistries.ITEM.byNameCodec(), ITEM_STACK)
            .xmap(e->e.map(ItemStack::new, i->i), Either::right
    );
}
