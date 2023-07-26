package party.lemons.questicle.client.widget;

import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class QButton extends AbstractButton {

    private final AbstractButtonPress onPressed;

    public QButton(int x, int y, int width, int height, AbstractButtonPress onPressed)
    {
        super(x, y, width, height, Component.empty());

        this.onPressed = onPressed;
    }

    @Override
    public void onPress() {
        onPressed.onButtonPress(this);
    }


    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
