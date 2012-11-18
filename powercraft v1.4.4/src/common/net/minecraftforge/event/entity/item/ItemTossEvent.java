package net.minecraftforge.event.entity.item;

import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.entity.EntityEvent;

@Cancelable
public class ItemTossEvent extends ItemEvent
{
    public final EntityPlayer player;

    public ItemTossEvent(EntityItem entityItem, EntityPlayer player)
    {
        super(entityItem);
        this.player = player;
    }
}