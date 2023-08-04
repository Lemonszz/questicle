package party.lemons.questicle.client;

import com.mojang.blaze3d.pipeline.RenderCall;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import party.lemons.questicle.Questicle;
import party.lemons.questicle.QuesticleClient;
import party.lemons.questicle.client.shader.QShaders;
import party.lemons.questicle.client.texture.TextureData;

import java.util.List;
import java.util.Optional;

public class DrawUtils
{
    public static float MOB_DOWNSCALE_SIZE = 4;
    public static int MOB_DOWNSCALE_AMOUNT = 3;

    private static void executeRenderCall(RenderCall renderCall)
    {
        if (RenderSystem.isOnRenderThread())
        {
            renderCall.execute();
        }
        else
        {
            RenderSystem.recordRenderCall(renderCall);
        }
    }

    public static int fontLineHeight()
    {
        return Minecraft.getInstance().font.lineHeight;
    }

    public static void drawLine(GuiGraphics graphics, int startX, int startY, int endX, int endY, int width)
    {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        float len = Mth.sqrt(Mth.square(endX - startX) + Mth.square(endY - startY));
        float size = (float)width / 2;

        RenderSystem.setShader(()->QShaders.repeatingRect);
        RenderSystem.setShaderTexture(0, Questicle.id("textures/gui/connection.png"));

        graphics.pose().pushPose();
        graphics.pose().translate(startX, startY, 0);

        graphics.pose().mulPose(Axis.ZP.rotation((float)Mth.atan2(endY - startY, endX - startX)));

        Matrix4f posMatrix = graphics.pose().last().pose();

        executeRenderCall(()->{
            ShaderInstance shader = RenderSystem.getShader();
            if(shader != null)
            {
                shader.getUniform("qRectPos").set((float) size, (float)size);
                shader.getUniform("qTileSize").set((float) width, (float) width);
                shader.getUniform("qTileUv").set(0, 0, 1.0F, 1.0F);
                shader.getUniform("qPosMatrix").set(posMatrix);
            }
        });

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        buffer.vertex(posMatrix, 0, -size, 0).uv(0, 0).endVertex();
        buffer.vertex(posMatrix, 0, size, 0).uv(1, 0).endVertex();
        buffer.vertex(posMatrix, len, size, 0).uv(1, 1).endVertex();
        buffer.vertex(posMatrix, len, -size, 0).uv(1, 0).endVertex();


        tesselator.end();
        graphics.pose().popPose();
    }

    public static void blitRepeating(GuiGraphics graphics, ResourceLocation resourceLocation, int drawX, int drawY, int width, int height, int uvX, int uvY, int tileX, int tileY)
    {
        blitRepeating(graphics, resourceLocation, drawX, drawY, width, height, tileX, tileY, 0, 0, 1, 1);
    }

    public static void blitRepeating(GuiGraphics graphics, ResourceLocation textureLocation, float drawX, float drawY, float drawWidth, float drawHeight, float tileSizeX, float tileSizeY, float u0, float v0, float u1, float v1) {
        RenderSystem.setShader(() -> QShaders.repeatingRect);
        RenderSystem.setShaderTexture(0, textureLocation);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        Matrix4f posMatrix = graphics.pose().last().pose();

        executeRenderCall(() -> {
            ShaderInstance shader = RenderSystem.getShader();
            if (shader != null) {
                shader.getUniform("qRectPos").set(drawX, drawY);
                shader.getUniform("qTileSize").set(tileSizeX, tileSizeY);
                shader.getUniform("qTileUv").set(u0, v0, u1, v1);
                shader.getUniform("qPosMatrix").set(posMatrix);
            }
        });

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        RenderSystem.enableBlend();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        buffer.vertex(posMatrix, drawX, drawY, 0).endVertex();
        buffer.vertex(posMatrix, drawX, drawY + drawHeight, 0).endVertex();
        buffer.vertex(posMatrix, drawX + drawWidth, drawY + drawHeight, 0).endVertex();
        buffer.vertex(posMatrix, drawX + drawWidth, drawY, 0).endVertex();
        BufferUploader.drawWithShader(buffer.end());
        RenderSystem.disableBlend();
    }

    public static void drawEntityIcon(GuiGraphics graphics, List<EntityType<?>> entityList, Optional<CompoundTag> tag, int drawX, int drawY, float iconScale)
    {
        int index = Mth.abs((QuesticleClient.tick / 30) % entityList.size());
        EntityType<?> type = entityList.get(index);
        Entity entity = type.create(Minecraft.getInstance().level);
        tag.ifPresent(compoundTag -> entity.load(compoundTag));

        float width = entity.getBbWidth();
        float height = entity.getBbHeight();
        float scale = (float)Math.pow(24, Math.max(0.5, 1.4 - height));
        if(width >= MOB_DOWNSCALE_SIZE || height >= MOB_DOWNSCALE_SIZE)
            scale /= MOB_DOWNSCALE_AMOUNT;

        if(entity instanceof LivingEntity livingEntity)
        {
            livingEntity.yBodyRot = 155.0F;
            livingEntity.setYRot(155F);
            livingEntity.yHeadRot = livingEntity.getYRot();
        }

        DrawUtils.drawEntity(graphics, entity, drawX, drawY, scale * iconScale);
    }

    public static void drawEntity(GuiGraphics graphics, Entity entity, int drawX, int drawY, float scale)
    {
        graphics.pose().pushPose();
        graphics.pose().translate(drawX, drawY, 50.0);
        graphics.pose().mulPoseMatrix(new Matrix4f().scaling(scale, -scale, -scale));
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityRenderers = Minecraft.getInstance().getEntityRenderDispatcher();
        RenderSystem.runAsFancy(
                () -> entityRenderers.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, graphics.pose(), graphics.bufferSource(), 15728880)
        );

        graphics.flush();
        entityRenderers.setRenderShadow(true);
        graphics.pose().popPose();
        Lighting.setupFor3DItems();
    }

    public static ItemStack drawItemStackList(GuiGraphics graphics, List<ItemStack> stacks, int drawX, int drawY)
    {
        if(stacks.isEmpty())
            return ItemStack.EMPTY;

        int index = Mth.abs((QuesticleClient.tick / 30) % stacks.size());
        ItemStack stack = stacks.get(index);
        graphics.renderFakeItem(stack, drawX, drawY);

        return stack;
    }
    public static ItemStack drawItemStackListWithDecoration(GuiGraphics graphics, List<ItemStack> stacks, int drawX, int drawY)
    {
        ItemStack stack = drawItemStackList(graphics, stacks, drawX, drawY);
        graphics.renderItemDecorations(Minecraft.getInstance().font, stack, drawX, drawY);

        return stack;
    }

    public static void drawTexture(GuiGraphics graphics, TextureData tex, int drawX, int drawY, int width, int height)
    {
        graphics.blit(drawX, drawY, 0, width, height, tex.sprite());
    }
}
