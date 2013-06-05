package codechicken.core.inventory;

import codechicken.core.vec.BlockCoord;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class InventorySensor
{
    private static class InventoryHash
    {
        public InventoryHash(int loc)
        {
            this.side = loc;
        }
        
        public InventoryCopy inv;
        public int side;
    }
    
    public static int[] adjacent = new int[]{-1, 0, 1, 2, 3, 4, 5};
    private InventoryHash[] invs;
    private World world;
    private BlockCoord pos;
    private boolean changed;
    
    public InventorySensor(World world, int x, int y, int z, int[] sides)
    {
        this.world = world;
        pos = new BlockCoord(x, y, z);
        invs = new InventoryHash[sides.length];
        for(int i = 0; i < invs.length; i++)
            invs[i] = new InventoryHash(sides[i]);
    }
    
    public boolean changed()
    {
        if(changed)
            return true;
        
        if(checkChange())
            changed = true;
        
        return changed;
    }
    
    private boolean checkChange()
    {
        for(int i = 0; i < invs.length; i++)
        {
            InventoryHash hash = invs[i];
            IInventory inv = getInventory(world, pos.x, pos.y, pos.z, hash.side);
            
            if(hash.inv == null ? inv != null : !InventoryUtils.inventoriesEqual(inv, hash.inv.inv))
                return true;
            
            if(inv == null)
                continue;
            
            InventoryCopy copyInv = hash.inv;
            for(int j = 0; j < copyInv.items.length; j++)
            {
                if(!hash.inv.accessible[j])
                    continue;
                
                if(!InventoryUtils.areStacksIdentical(copyInv.getStackInSlot(j), inv.getStackInSlot(j)))
                    return true;
            }
        }
        
        return false;
    }
    
    public void reset()
    {
        for(int i = 0; i < invs.length; i++)
        {
            InventoryHash hash = invs[i];
            IInventory inv = getInventory(world, pos.x, pos.y, pos.z, hash.side);
            if(hash.inv == null ? inv != null : inv != hash.inv.inv)
            {
                if(inv == null)
                {
                    hash.inv = null;
                }
                else
                {
                    hash.inv = new InventoryCopy(inv);
                    if(hash.side >= 0)
                        hash.inv.open(new InventoryRange(inv, ForgeDirection.getOrientation(hash.side).getOpposite()));
                    else
                        hash.inv.open(new InventoryRange(inv, 0, inv.getSizeInventory()));
                }
            }
            
            if(hash.inv == null)
                continue;

            InventoryCopy copyInv = hash.inv;
            for(int j = 0; j < copyInv.items.length; j++)
            {
                ItemStack stack = inv.getStackInSlot(j);
                if(stack != null)
                    stack = stack.copy();
                copyInv.setInventorySlotContents(j, stack);
            }
        }
        changed = false;
    }
    
    private static IInventory getInventory(World world, int x, int y, int z, int side)
    {
        if(side >= 0)
        {
            ForgeDirection fside = ForgeDirection.getOrientation(side);
            x+=fside.offsetX;
            y+=fside.offsetY;
            z+=fside.offsetZ;
        }
        
        return InventoryUtils.getInventory(world, x, y, z);
    }
}
