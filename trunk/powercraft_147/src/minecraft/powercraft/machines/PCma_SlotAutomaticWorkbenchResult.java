package powercraft.machines;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;

public class PCma_SlotAutomaticWorkbenchResult extends Slot
{
    private final PCma_TileEntityAutomaticWorkbench storageInv;
    private Container parent;

    public PCma_SlotAutomaticWorkbenchResult(PCma_TileEntityAutomaticWorkbench storage, IInventory result, Container parent, int i, int j, int k)
    {
        super(result, i, j, k);
        storageInv = storage;
        this.parent = parent;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer thePlayer, ItemStack itemstack)
    {
        itemstack.onCrafting(thePlayer.worldObj, thePlayer, itemstack.stackSize);

        if (itemstack.itemID == Block.workbench.blockID)
        {
            thePlayer.addStat(AchievementList.buildWorkBench, 1);
        }
        else if (itemstack.itemID == Item.pickaxeWood.itemID)
        {
            thePlayer.addStat(AchievementList.buildPickaxe, 1);
        }
        else if (itemstack.itemID == Block.stoneOvenIdle.blockID)
        {
            thePlayer.addStat(AchievementList.buildFurnace, 1);
        }
        else if (itemstack.itemID == Item.hoeWood.itemID)
        {
            thePlayer.addStat(AchievementList.buildHoe, 1);
        }
        else if (itemstack.itemID == Item.bread.itemID)
        {
            thePlayer.addStat(AchievementList.makeBread, 1);
        }
        else if (itemstack.itemID == Item.cake.itemID)
        {
            thePlayer.addStat(AchievementList.bakeCake, 1);
        }
        else if (itemstack.itemID == Item.pickaxeStone.itemID)
        {
            thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
        }
        else if (itemstack.itemID == Item.swordWood.itemID)
        {
            thePlayer.addStat(AchievementList.buildSword, 1);
        }
        else if (itemstack.itemID == Block.enchantmentTable.blockID)
        {
            thePlayer.addStat(AchievementList.enchantments, 1);
        }
        else if (itemstack.itemID == Block.bookShelf.blockID)
        {
            thePlayer.addStat(AchievementList.bookcase, 1);
        }

        storageInv.decrementRecipe();
        parent.onCraftMatrixChanged(storageInv);
    }
}
