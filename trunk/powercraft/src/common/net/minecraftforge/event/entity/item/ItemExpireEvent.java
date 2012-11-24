package net.minecraftforge.event.entity.item;

import net.minecraft.src.EntityItem;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.entity.EntityEvent;

@Cancelable
public class ItemExpireEvent extends ItemEvent
{
    public int extraLife;

    public ItemExpireEvent(EntityItem entityItem, int extraLife)
    {
        super(entityItem);
        this.extraLife = extraLife;
    }
}