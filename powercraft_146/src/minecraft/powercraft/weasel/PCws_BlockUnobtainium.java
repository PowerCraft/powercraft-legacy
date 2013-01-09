package powercraft.weasel;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.IBlockAccess;
import powercraft.management.PC_Block;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;

public class PCws_BlockUnobtainium extends PC_Block {

	public PCws_BlockUnobtainium(int id) {
		super(id, 2, Material.rock);
		setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			return "Unobtainium";
		case PC_Utils.MSG_LOAD_WORLD:{
			PC_Utils.ValueWriting.removeSmeltingRecipes(new ItemStack(this));
			PC_Utils.ValueWriting.addSmeltingRecipes(new ItemStack(this), new ItemStack(PCws_App.ingotUnobtaninium), 1.0F);
		}
		}
		return null;
	}

}
