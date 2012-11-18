package net.minecraftforge.event.entity.item;

import net.minecraft.src.EntityItem;
import net.minecraftforge.event.entity.EntityEvent;

public class ItemEvent extends EntityEvent
{
    public final EntityItem entityItem;

    public ItemEvent(EntityItem itemEntity)
    {
        super(itemEntity);
        this.entityItem = itemEntity;
    }
}