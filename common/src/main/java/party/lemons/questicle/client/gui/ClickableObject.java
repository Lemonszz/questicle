package party.lemons.questicle.client.gui;

public interface ClickableObject<T>
{
    default boolean isClickable(T ctx)
    {
        return true;
    }
    void onClick(T ctx);
}
