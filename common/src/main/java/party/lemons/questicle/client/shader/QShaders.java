package party.lemons.questicle.client.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import net.minecraft.client.renderer.ShaderInstance;

import java.io.IOException;

public class QShaders
{
    public static ShaderInstance repeatingRect;

    public static void init()
    {
        ClientReloadShadersEvent.EVENT.register((provider, sink) -> {
            try {
                sink.registerShader(new ShaderInstance(provider, "q_repeating_rect", DefaultVertexFormat.POSITION), (sh)->{
                    QShaders.repeatingRect = sh;
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
