package powercraft.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.Direction;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntitySign;
import net.minecraft.src.World;

public class PC_ActivatorListener implements PC_IActivatorListener
{
    private static List<PC_IActivatorListener> listeners = new ArrayList<PC_IActivatorListener>();

    static
    {
        registerListener(new PC_ActivatorListener());
    }

    private PC_ActivatorListener() {}

    @Override
    public boolean onActivatorUsedOnBlock(ItemStack stack, EntityPlayer player, World world, PC_CoordI pos)
    {
        if (pos.getId(world) == Block.mobSpawner.blockID)
        {
            if (world.isRemote)
            {
                PC_Utils.openGres("SpawnerEditor", player, pos.x, pos.y, pos.z);
            }

            stack.damageItem(1, player);
            return true;
        }

        if (pos.getId(world) == Block.signPost.blockID || pos.getId(world) == Block.signWall.blockID)
        {
            TileEntitySign tileentitysign = (TileEntitySign) world.getBlockTileEntity(pos.x, pos.y, pos.z);

            if (tileentitysign != null)
            {
                player.displayGUIEditSign(tileentitysign);
                stack.damageItem(1, player);
            }

            return true;
        }
        
        return false;
    }

    public static void registerListener(PC_IActivatorListener listener)
    {
        listeners.add(listener);
    }

    public static List<PC_IActivatorListener> getListeners()
    {
        return new ArrayList<PC_IActivatorListener>(listeners);
    }
}
