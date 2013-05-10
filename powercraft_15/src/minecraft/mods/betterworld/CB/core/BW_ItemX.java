package mods.betterworld.CB.core;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.ForceChunkEvent;

public class BW_ItemX extends ItemBlock {
	
	private static String[] subNames = {"bricks1", "bricks2", "bricks3", "bricks4", "bricks5", "bricks6", "bricks7", "bricks8",  
											  "bricks9", "bricks10", "bricks11", "bricks12", "bricks13", "bricks14", "bricks15", "bricks16", 
											  "bricks17", "bricks18", "bricks19", "bricks20", "bricks21", "bricks22", "bricks23", "bricks24",
											  "bricks25", "bricks26", "bricks27", "bricks28", "bricks29", "bricks30", "bricks31", "bricks32"	
											 };

	public BW_ItemX(int id) {
		super(id);
		this.setMaxDamage(0);
		setHasSubtypes(true);
		setUnlocalizedName("Block");
//		setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override
	public int getMetadata (int damageValue) {
		return damageValue;
	}
	
	public int getItemDamageVal(ItemStack itemstack)
	{
		return itemstack.getItemDamage();
	}
	
	String[] a= new String[32];
	@Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
		for (int i =0; i <32; i++)
		{
		a[i] = "Block." + subNames[itemStack.getItemDamage()];
		}
        return  "Block." + subNames[itemStack.getItemDamage()];
    }
	public String[] getSubName()
	{
		
		return a;
	}
/*	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs tab, List subItems) {
		for (int ix = 0; ix < 32; ix++) {
			subItems.add(new ItemStack(this, 1, ix));
		}
	}
*/
    @Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1) {
		return super.getIconFromDamage(par1);
	}
    @Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata) {
		// TODO Auto-generated method stub
		if(super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY,
				hitZ, metadata))
				{
			((BW_TileEntityBlockX)world.getBlockTileEntity(x, y, z)).setBlockDamageID(metadata);
			((BW_TileEntityBlockX)world.getBlockTileEntity(x, y, z)).setUserName(player.username);
			
			
				return true;
				}
		return false;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int var1, CreativeTabs var2, List var3)
    {
    	BW_BlockX block = (BW_BlockX) Block.blocksList[getBlockID()];
    	for(int i=0; i<block.getSubBlockCount(); i++){
	        var3.add(new ItemStack(var1, 1, i));

    	}
    }
}