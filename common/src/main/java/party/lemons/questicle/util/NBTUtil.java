package party.lemons.questicle.util;

import net.minecraft.nbt.CompoundTag;

public class NBTUtil
{
    public static int increaseInt(String key, CompoundTag tag)
    {
        if(tag.contains(key))
        {
            int num = tag.getInt(key) + 1;
            tag.putInt(key, num);
            return num;
        }
        else {
            tag.putInt(key, 1);
            return 1;
        }
    }

    public static int setInt(String key, int count, CompoundTag tag)
    {
        tag.putInt(key, count);
        return count;
    }
}
