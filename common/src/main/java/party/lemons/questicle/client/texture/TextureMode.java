package party.lemons.questicle.client.texture;

import net.minecraft.util.StringRepresentable;

public enum TextureMode implements StringRepresentable
{
    DIRECT("direct"),
    BLOCK_ATLAS("block_atlas");

    private final String typeName;

    TextureMode(String typeName)
    {
        this.typeName = typeName;
    }

    @Override
    public String getSerializedName() {
        return typeName;
    }
}
