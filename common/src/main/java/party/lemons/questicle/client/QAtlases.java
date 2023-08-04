package party.lemons.questicle.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import party.lemons.questicle.Questicle;
import party.lemons.questicle.client.texture.TextureData;

import java.util.HashMap;
import java.util.Map;

public class QAtlases extends SimplePreparableReloadListener<Integer> {
    public static final ResourceLocation FRAME_ATLAS = Questicle.id("quest_frames");
    public static final ResourceLocation ELEMENTS_ATLAS = Questicle.id("quest_elements");

    public static QAtlasHolder FRAMES;
    public static QAtlasHolder ELEMENTS;

    public static final Map<ResourceLocation, QAtlasHolder> ATLASES = new HashMap<>();

    public static void init()
    {
        //creating atlases these here stops it exploding
        FRAMES = registerAtlas(FRAME_ATLAS);
        ELEMENTS = registerAtlas(ELEMENTS_ATLAS);

        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new QAtlases());
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, FRAMES);
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, ELEMENTS);
    }

    private static QAtlasHolder registerAtlas(ResourceLocation atlas)
    {
        QAtlasHolder atlasHolder = new QAtlasHolder(Minecraft.getInstance().getTextureManager(), atlas);
        ATLASES.put(atlas, atlasHolder);
        return atlasHolder;
    }

    public static TextureAtlasSprite getAtlasSprite(ResourceLocation location, ResourceLocation sprite)
    {
        if(ATLASES.containsKey(location))
            return ATLASES.get(location).getSprite(sprite);

        return Minecraft.getInstance().getModelManager().getAtlas(convertAtlasLocation(location)).getSprite(sprite);
    }

    public static TextureAtlasSprite getAtlasSprite(TextureData sprite)
    {
        if(ATLASES.containsKey(sprite.atlas()))
            return ATLASES.get(sprite.atlas()).getSprite(sprite);

        return Minecraft.getInstance().getModelManager().getAtlas(convertAtlasLocation(sprite.atlas())).getSprite(sprite.texture());
    }

    @Override
    protected Integer prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        return 1;
    }

    @Override
    protected void apply(Integer object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        FRAMES.clear();
        ELEMENTS.clear();
    }

    public static ResourceLocation convertAtlasLocation(ResourceLocation atlas)
    {
        return new ResourceLocation(atlas.getNamespace(), "textures/atlas/" + atlas.getPath() + ".png");
    }

    public static class QAtlasHolder extends TextureAtlasHolder {

        private final Map<TextureData, TextureAtlasSprite> spriteData = new HashMap<>();
        public QAtlasHolder(TextureManager textureManager, ResourceLocation atlas) {
            super(textureManager, QAtlases.convertAtlasLocation(atlas), atlas);
        }

        public TextureAtlasSprite getSprite(TextureData textureData)
        {
            return spriteData.computeIfAbsent(textureData, (i)->this.getSprite(i.texture()));
        }

        public TextureAtlasSprite getSprite(ResourceLocation location)
        {
            return super.getSprite(location);
        }

        public void clear()
        {
            spriteData.clear();
        }

    }
}