package powercraft.tutorial;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import powercraft.api.PC_ClientRegistry;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;

@PC_BlockInfo(name="Tutorial", blockid="tutorial", defaultid=2000, tileEntity=PC_TileEntityTutorial.class)
public class PC_BlockTutorial extends PC_Block {

	public PC_BlockTutorial(int id) {
		super(id, Material.ground);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public void loadIcons() {
		blockIcon = PC_ClientRegistry.registerIcon("Icon");
	}

	@Override
	public void registerRecipes() {
		
	}

}
