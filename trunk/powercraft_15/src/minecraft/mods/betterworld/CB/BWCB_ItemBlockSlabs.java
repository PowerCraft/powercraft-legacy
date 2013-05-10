package mods.betterworld.CB;

import java.util.List;

import com.google.common.util.concurrent.SettableFuture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.betterworld.CB.core.BWCB_BlockList;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.ForceChunkEvent;

public class BWCB_ItemBlockSlabs extends ItemBlock {

	public BWCB_ItemBlockSlabs(int id) {
		super(id);
		this.setMaxDamage(0);
		setHasSubtypes(true);
		setUnlocalizedName("BlockSlabs");

	}

	/*
	 * @Override public int getMetadata (int damageValue) { return damageValue;
	 * }
	 */
	public int getItemDamageVal(ItemStack itemstack) {
		return itemstack.getItemDamage();
	}

	String[] a = new String[BWCB_BlockList.blockStoneNormalName.size()];

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return "BlockSlabs."
				+ BWCB_BlockList.blockStoneNormalName.get(itemStack
						.getItemDamage());

	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1) {
		return super.getIconFromDamage(par1);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10){
		int i1 = par3World.getBlockId(par4, par5, par6);
		if(i1!=getBlockID()){
			return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
		}
		BWCB_BlockSlabs slabs = (BWCB_BlockSlabs) Block.blocksList[i1];
		if(slabs.isDoubleSlab){
			super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
		}
		else if (par1ItemStack.stackSize == 0)
        {
            return false;
        }
        else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
        {
            return false;
        }
        else
        {
        	BWCB_BlockSlabs doubleSlab = (BWCB_BlockSlabs) Block.blocksList[slabs.otherID];
            int j1 = par3World.getBlockMetadata(par4, par5, par6);
            int k1 = j1 & 7;
            boolean flag = (j1 & 8) != 0;
            BWCB_TileEntityBlockSlabs te = ((BWCB_TileEntityBlockSlabs)par3World.getBlockTileEntity(par4, par5, par6));
            int dam = te.getBlockDamageID();
            
            if ((par7 == 1 && !flag || par7 == 0 && flag) && dam == par1ItemStack.getItemDamage())
            {
            	if (par3World.checkNoEntityCollision(doubleSlab.getCollisionBoundingBoxFromPool(par3World, par4, par5, par6)) && par3World.setBlock(par4, par5, par6, doubleSlab.blockID, k1, 3))
            	{
            		te.validate();
            		par3World.setBlockTileEntity(par4, par5, par6, te);
                    par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), doubleSlab.stepSound.getPlaceSound(), (doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, doubleSlab.stepSound.getPitch() * 0.8F);
                    --par1ItemStack.stackSize;
                }

                return true;
            }else{
            	return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
            }
        }
		return false;
	}
    
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata) {

		if (super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY,
				hitZ, metadata)) {
			((BWCB_TileEntityBlockSlabs) world.getBlockTileEntity(x, y, z))
					.setBlockDamageID(stack.getItemDamage());
			((BWCB_TileEntityBlockSlabs) world.getBlockTileEntity(x, y, z))
					.setUserName(player.username);
			((BWCB_TileEntityBlockSlabs) world.getBlockTileEntity(x, y, z))
					.setLightValue(BWCB_BlockList.blockStoneNormalLight
							.get(stack.getItemDamage()));
			((BWCB_TileEntityBlockSlabs) world.getBlockTileEntity(x, y, z))
					.setBlockLocked(false);
			return true;
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int var1, CreativeTabs var2, List var3) {
		BWCB_BlockSlabs block = (BWCB_BlockSlabs) Block.blocksList[getBlockID()];
		for (int i = 0; i < block.getSubBlockCount(); i++) {
			var3.add(new ItemStack(var1, 1, i));

		}
	}
	
}
