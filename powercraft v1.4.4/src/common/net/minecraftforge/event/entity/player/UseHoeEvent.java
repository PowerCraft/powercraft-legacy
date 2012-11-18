package net.minecraftforge.event.entity.player;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.Event;

@Cancelable
@Event.HasResult
public class UseHoeEvent extends PlayerEvent
{
    public final ItemStack current;
    public final World world;
    public final int x;
    public final int y;
    public final int z;

    private boolean handeled = false;

    public UseHoeEvent(EntityPlayer player, ItemStack current, World world, int x, int y, int z)
    {
        super(player);
        this.current = current;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
