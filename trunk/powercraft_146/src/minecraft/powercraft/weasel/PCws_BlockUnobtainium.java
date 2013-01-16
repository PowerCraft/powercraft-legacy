package powercraft.weasel;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_MathHelper;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;

public class PCws_BlockUnobtainium extends PC_Block {

	public PCws_BlockUnobtainium(int id) {
		super(id, 2, Material.rock);
		setHardness(0.7F);
		setResistance(10.0F);
		setStepSound(Block.soundStoneFootstep);
		setCreativeTab(CreativeTabs.tabBlock);
	}
	
	/*@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCws_TileEntityUnobtainium();
	}*/

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			return "Unobtainium";
		case PC_Utils.MSG_LOAD_WORLD:{
			PC_Utils.ValueWriting.removeSmeltingRecipes(new ItemStack(this));
			PC_Utils.ValueWriting.addSmeltingRecipes(new ItemStack(this), new ItemStack(PCws_App.ingotUnobtaninium), 1.0F);
			break;
		}case PC_Utils.MSG_SPAWNS_IN_CHUNK:{
			Random rand = (Random)obj[0];
			return rand.nextDouble()>0.7?1:0;
		}case PC_Utils.MSG_BLOCKS_ON_SPAWN_POINT:
			return 40;
		case PC_Utils.MSG_SPAWN_POINT:
			return new PC_VecI(((Random)obj[0]).nextInt(16),
					((Random)obj[0]).nextInt(PC_MathHelper.clamp_int(30 - 5, 1, 255)) + 5,
					((Random)obj[0]).nextInt(16));
		case PC_Utils.MSG_SPAWN_POINT_METADATA:
			return 0;
		}
		return null;
	}

}
