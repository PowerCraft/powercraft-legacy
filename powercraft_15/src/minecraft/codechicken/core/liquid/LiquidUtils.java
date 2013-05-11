package codechicken.core.liquid;

import codechicken.core.CommonUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;

public class LiquidUtils
{
    public static int B = LiquidContainerRegistry.BUCKET_VOLUME;
    public static LiquidStack water = new LiquidStack(Block.waterStill.blockID, 1000);
    public static LiquidStack lava = new LiquidStack(Block.lavaStill.blockID, 1000);

    public static boolean fillTankWithContainer(ITankContainer tank, EntityPlayer player)
    {
        ItemStack stack = player.getCurrentEquippedItem();
        LiquidStack liquid = LiquidContainerRegistry.getLiquidForFilledItem(stack);

        if(liquid == null)
            return false;

        if(tank.fill(ForgeDirection.UNKNOWN, liquid, false) != liquid.amount && !player.capabilities.isCreativeMode)
            return false;
        
        tank.fill(ForgeDirection.UNKNOWN, liquid, true);
        
        if(!player.capabilities.isCreativeMode)
            player.inventory.setInventorySlotContents(player.inventory.currentItem, CommonUtils.consumeItem(stack));
        
        player.inventoryContainer.detectAndSendChanges();
        return true;
    }

    public static boolean emptyTankIntoContainer(ITankContainer tank, EntityPlayer player, LiquidStack tankLiquid)
    {
        ItemStack stack = player.getCurrentEquippedItem();

        if(!LiquidContainerRegistry.isEmptyContainer(stack))
            return false;
        
        ItemStack filled = LiquidContainerRegistry.fillLiquidContainer(tankLiquid, stack);
        LiquidStack liquid = LiquidContainerRegistry.getLiquidForFilledItem(filled);

        if(liquid == null || filled == null)
            return false;
        
        tank.drain(ForgeDirection.UNKNOWN, liquid.amount, true);

        if(!player.capabilities.isCreativeMode)
        {
            if(stack.stackSize == 1)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, filled);
            else if(player.inventory.addItemStackToInventory(filled))
                stack.stackSize--;
            else
                return false;
        }
        
        player.inventoryContainer.detectAndSendChanges();        
        return true;
    }

    public static LiquidStack copy(LiquidStack liquid, int quantity)
    {
        liquid = liquid.copy();
        liquid.amount = quantity;
        return liquid;
    }
}
