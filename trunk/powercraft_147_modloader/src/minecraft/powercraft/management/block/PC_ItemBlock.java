package powercraft.management.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.item.PC_IItemInfo;
import powercraft.management.tileentity.PC_TileEntity;

public abstract class PC_ItemBlock extends ItemBlock implements PC_IItemInfo, PC_IMSG
{
    private PC_IModule module;

    protected PC_ItemBlock(int id)
    {
        super(id);
    }

    @Override
    public PC_IModule getModule()
    {
        return module;
    }

    public void setModule(PC_IModule module)
    {
    	this.module = module;
    }
    
    @Override
	public boolean showInCraftingTool() {
		return true;
	}
    
    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
    }

    @Override
    public void getSubItems(int index, CreativeTabs creativeTab, List list)
    {
        list.addAll(getItemStacks(new ArrayList<ItemStack>()));
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        int var11 = par3World.getBlockId(par4, par5, par6);

        if (var11 == Block.snow.blockID)
        {
            par7 = 1;
        }
        else if (var11 != Block.vine.blockID && var11 != Block.tallGrass.blockID && var11 != Block.deadBush.blockID )
        {
            if (par7 == 0)
            {
                --par5;
            }

            if (par7 == 1)
            {
                ++par5;
            }

            if (par7 == 2)
            {
                --par6;
            }

            if (par7 == 3)
            {
                ++par6;
            }

            if (par7 == 4)
            {
                --par4;
            }

            if (par7 == 5)
            {
                ++par4;
            }
        }

        if (par1ItemStack.stackSize == 0)
        {
            return false;
        }
        else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
        {
            return false;
        }
        else if (par5 == 255 && Block.blocksList[getBlockID()].blockMaterial.isSolid())
        {
            return false;
        }
        else if (par3World.canPlaceEntityOnSide(getBlockID(), par4, par5, par6, false, par7, par2EntityPlayer))
        {
            Block var12 = Block.blocksList[getBlockID()];
            int var13 = this.getMetadata(par1ItemStack.getItemDamage());
            int var14 = Block.blocksList[getBlockID()].onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, var13);

            if (placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, var14))
            {
                par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), var12.stepSound.getPlaceSound(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);
                --par1ItemStack.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        if (!world.setBlockAndMetadataWithNotify(x, y, z, getBlockID(), metadata))
        {
            return false;
        }

        if (world.getBlockId(x, y, z) == getBlockID())
        {
            Block block =  Block.blocksList[getBlockID()];
            block.onBlockPlacedBy(world, x, y, z, player);
            PC_TileEntity te = (PC_TileEntity)GameInfo.getTE(world, x, y, z);

            if (te == null)
            {
            	if(block instanceof PC_Block){
            		te = (PC_TileEntity)ValueWriting.setTE(world, x, y, z, ((PC_Block)block).createNewTileEntity(world, metadata));
            	}else if(block instanceof BlockContainer){
            		te = (PC_TileEntity)ValueWriting.setTE(world, x, y, z, ((BlockContainer)block).createNewTileEntity(world));
            	}
            }

            if (te != null)
            {
                te.create(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
            }
        }

        return true;
    }

	public void doCrafting(ItemStack itemStack, InventoryCrafting inventoryCrafting) {
	}

	public String getTextureFile() {
		Block b = Block.blocksList[getBlockID()];
		if(b instanceof PC_Block){
			return ((PC_Block) b).getTextureFile();
		}
		return "/terrain.png";
	}
    
}
