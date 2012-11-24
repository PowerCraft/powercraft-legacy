package net.minecraftforge.event.entity.player;

import java.util.ArrayList;

import net.minecraft.src.*;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

@Cancelable
public class PlayerDropsEvent extends LivingDropsEvent
{
    public final EntityPlayer entityPlayer;

    public PlayerDropsEvent(EntityPlayer entity, DamageSource source, ArrayList<EntityItem> drops, boolean recentlyHit)
    {
        super(entity, source, drops,
                (source.getEntity() instanceof EntityPlayer) ?
                EnchantmentHelper.getLootingModifier(((EntityPlayer)source.getEntity())) : 0,
                recentlyHit, 0);
        this.entityPlayer = entity;
    }
}