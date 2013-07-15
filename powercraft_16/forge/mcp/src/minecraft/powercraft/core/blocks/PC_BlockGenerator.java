package powercraft.core.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import powercraft.api.PC_ClientRegistry;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.blocks.PC_TileEntity;

@PC_BlockInfo(name="Generator", blockid="generator", defaultid=3001, tileEntity = PC_TileEntityGenerator.class)
public class PC_BlockGenerator extends PC_Block {

	public PC_BlockGenerator(int id) {
		super(id, Material.ground);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void loadIcons() {
		blockIcon = PC_ClientRegistry.registerIcon("DefaultMaschineTexture");
	}

	@Override
	public void registerRecipes() {
		
	}

	
	
}
