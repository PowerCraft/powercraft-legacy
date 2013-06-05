package powercraft.machines;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.annotation.PC_BlockFlag;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.entity.PC_FakePlayer;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.registry.PC_ItemRegistry;
import powercraft.api.registry.PC_KeyRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.utils.PC_Struct2;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

@PC_BlockFlag(flags={PC_MSGRegistry.HARVEST_STOP, PC_MSGRegistry.NO_HARVEST})
@PC_BlockInfo(name="Replacer", tileEntity=PCma_TileEntityReplacer.class) //this is an Annotation; it can contain Data; here it references to the Blocks TileEntity
public class PCma_BlockReplacer extends PC_Block implements PC_IItemInfo
{
    private static final int TXTOP = 1, TXSIDE = 0; //These are the sides and their IDs in the texture file

    public PCma_BlockReplacer(int id)
    {
        super(id, Material.ground, "replacer_side", "replacer_top", "replacer_side");
        setHardness(0.7F);
        setResistance(10.0F);
        setStepSound(Block.soundStoneFootstep);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        PCma_TileEntityReplacer tileentity = (PCma_TileEntityReplacer) world.getBlockTileEntity(i, j, k);

        if (tileentity != null)
        {
        	tileentity.setAidEnabled(!tileentity.isAidEnabled());
        }
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        ItemStack ihold = entityplayer.getCurrentEquippedItem(); //This is the Item which the player has currently equipped

        PCma_TileEntityReplacer tileentity = PC_Utils.<PCma_TileEntityReplacer>getTE(world, i, j, k); //This brings us the TileEntity of the current Block
        
        if (ihold != null)
        {
           if (ihold.getItem().itemID == PC_ItemRegistry.getPCItemIDByName("PCco_ItemActivator"))
            {
                int l = MathHelper.floor_double(((entityplayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;

                if (PC_KeyRegistry.isPlacingReversed(entityplayer))
                {
                	l = (((l+4)%4)+2)%4;
                }

                if (entityplayer.isSneaking())
                {
                    l = PC_KeyRegistry.isPlacingReversed(entityplayer) ? 5 : 4;
                }

                if (tileentity != null)
                {
                	PC_VecI coordOffset = tileentity.getCoordOffset();
                    switch (l)
                    {
                        case 0:
                            coordOffset.z++;
                            break;

                        case 2:
                            coordOffset.z--;
                            break;

                        case 3:
                            coordOffset.x++;
                            break;

                        case 1:
                            coordOffset.x--;
                            break;

                        case 4:
                            coordOffset.y++;
                            break;

                        case 5:
                            coordOffset.y--;
                            break;
                    }

                    coordOffset.x = MathHelper.clamp_int(coordOffset.x, -16, 16);
                    coordOffset.y = MathHelper.clamp_int(coordOffset.y, -16, 16);
                    coordOffset.z = MathHelper.clamp_int(coordOffset.z, -16, 16);
                    tileentity.setCoordOffset(coordOffset);
                }

                return true;
            }
        }

        if (world.isRemote) //this says whether we're on server or not if isRemote==true then we're on client
        {
            return true; //on the server there can't be a gui that's why we interrupt it before the gui get's opened
        }

        PC_GresRegistry.openGres("Replacer", entityplayer, tileentity); // This opens the Gui
        return true; // If we return true, then this click is "counted" else it's like we've ignored it
    }

    @Override
    public int tickRate(World world)
    {
        return 1;
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        world.scheduleBlockUpdate(i, j, k, blockID, tickRate(world));
    }

    private boolean replacer_canHarvestBlockAt(World world, PC_VecI pos)
    {
        int id = PC_Utils.getMD(world, pos);

        if (id == 0 || Block.blocksList[id] == null)
        {
            return true;
        }

        /** TODO if (!PC_MSGRegistry.hasFlag(world, pos, PC_Utils.NO_HARVEST))
        {
            return false;
        }*/

        if (id == 7 && pos.y == 0)
        {
            return false;
        }

        return true;
    }

    private boolean replacer_canPlaceBlockAt(World world, ItemStack itemstack, PC_VecI pos)
    {
        if (itemstack == null)
        {
            return true;
        }

        Item item = itemstack.getItem();

        if (item.itemID == Block.lockedChest.blockID)
        {
            return PC_Utils.getTE(world, pos) == null;
        }

        if (item instanceof ItemBlock)
        {
            Block block = Block.blocksList[item.itemID];

            if (block == null)
            {
                return false;
            }

            /** TODO if (PC_MSGRegistry.hasFlag(itemstack, PC_Utils.NO_BUILD))
            {
                return false;
            }*/

            if (block.hasTileEntity())
            {
                return false;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean replacer_placeBlockAt(World world, int meta, ItemStack itemstack, PC_VecI pos)
    {
        if (itemstack == null)
        {
        	PC_Utils.setBID(world, pos, 0, 0);
            return true;
        }

        if (itemstack.itemID == Block.lockedChest.blockID)
        {
        	PC_Utils.setBID(world, pos, 0, 0);
            world.removeBlockTileEntity(pos.x, pos.y, pos.z);

            if (!Item.itemsList[Block.lockedChest.blockID].onItemUse(itemstack, new PC_FakePlayer(world), world, pos.x, pos.y + 1, pos.z, 0, 0.0f, 0.0f, 0.0f))
            {
                return false;
            }

            itemstack.stackSize--;

            if (meta != -1)
            {
            	PC_Utils.setMD(world, pos, meta);
            }

            return true;
        }

        if (!replacer_canPlaceBlockAt(world, itemstack, pos))
        {
            return false;
        }

        ItemBlock iblock = (ItemBlock) itemstack.getItem();

        if (iblock.itemID == Block.waterStill.blockID)
        {
            iblock = (ItemBlock) Item.itemsList[Block.waterMoving.blockID];
        }

        if (iblock.itemID == Block.lavaStill.blockID)
        {
            iblock = (ItemBlock) Item.itemsList[Block.lavaMoving.blockID];
        }
        
        if (PC_Utils.setBID(world, pos, iblock.getBlockID(), iblock.getMetadata(itemstack.getItemDamage())))
        {
            if (PC_Utils.getBID(world, pos) == iblock.getBlockID())
            {
                world.notifyBlockChange(pos.x, pos.y, pos.z, iblock.getBlockID());
            }

            if (meta != -1 && !iblock.getHasSubtypes())
            {
            	PC_Utils.setMD(world, pos, meta);
            }

            itemstack.stackSize--;
        }

        return true;
    }

    private PC_Struct2<ItemStack, Integer> replacer_harvestBlockAt(World world, PC_VecI pos)
    {
        ItemStack loot = null;
        int meta = PC_Utils.getMD(world, pos);

        if (!replacer_canHarvestBlockAt(world, pos))
        {
            return null;
        }

        if (PC_Utils.getTE(world, pos) != null)
        {
        	/** TODO return new PC_Struct2<ItemStack, Integer>(PC_Utils.extractAndRemoveChest(world, pos), meta);*/
        }

        Block block = Block.blocksList[PC_Utils.getBID(world, pos)];

        if (block == null)
        {
            return null;
        }

        /** TODO  if (PC_Block.canSilkHarvest(block))
        {
            loot = PC_Block.createStackedBlock(block, PC_Utils.getMD(world, pos));
        }
        else
        */{
            int dropId = block.blockID;
            int dropMeta = block.damageDropped(PC_Utils.getMD(world, pos));
            int dropQuant = block.quantityDropped(world.rand);

            if (dropId <= 0)
            {
                dropId = PC_Utils.getBID(world, pos);
            }

            if (dropQuant <= 0)
            {
                dropQuant = 1;
            }

            loot = new ItemStack(dropId, dropQuant, dropMeta);
        }

        return new PC_Struct2<ItemStack, Integer>(loot, meta);
    }

    private void swapBlocks(PCma_TileEntityReplacer te)
    {
        PC_VecI pos = te.getCoord().offset(te.getCoordOffset());

        if (pos.equals(te.getCoord()))
        {
            return;
        }

        if (!replacer_canHarvestBlockAt(te.getWorldObj(), pos))
        {
            return;
        }

        if (!replacer_canPlaceBlockAt(te.getWorldObj(), te.getStackInSlot(0), pos))
        {
            return;
        }

        PC_Struct2<ItemStack, Integer> harvested = replacer_harvestBlockAt(te.getWorldObj(), pos);

        if (!replacer_placeBlockAt(te.getWorldObj(), te.extraMeta, te.getStackInSlot(0), pos))
        {
            if (harvested != null)
            {
                replacer_placeBlockAt(te.getWorldObj(), harvested.b, harvested.a, pos);
            }

            return;
        }

        if (harvested == null)
        {
            te.setInventorySlotContents(0, null);
            te.extraMeta = -1;
        }
        else
        {
        	te.setInventorySlotContents(0, harvested.a);
            te.extraMeta = harvested.b;
        }
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        PCma_TileEntityReplacer ter = (PCma_TileEntityReplacer) world.getBlockTileEntity(i, j, k);

        if (ter != null && !world.isRemote)
        {
            boolean powered = isIndirectlyPowered(world, i, j, k);

            if (powered != ter.isState())
            {
                swapBlocks(ter);
                ter.setState(powered);
            }
        }
    }

    private boolean isIndirectlyPowered(World world, int i, int j, int k)
    {
    	/** TODO if (PC_Utils.isPoweredDirectly(world, i, j, k))
        {
            return true;
        }*/

        if (world.isBlockIndirectlyGettingPowered(i, j, k))
        {
            return true;
        }

        /** TODO if (PC_Utils.isPoweredDirectly(world, i, j-1, k))
        {
            return true;
        }*/

        if (world.isBlockIndirectlyGettingPowered(i, j - 1, k))
        {
            return true;
        }

        return false;
    }

    @Override
    public int getMobilityFlag()
    {
        return 0;
    }

    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
    }
    
}
