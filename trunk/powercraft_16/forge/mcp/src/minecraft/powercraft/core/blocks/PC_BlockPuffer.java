package powercraft.core.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import powercraft.api.PC_ClientRegistry;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;

@PC_BlockInfo(name="Puffer", blockid="puffer", defaultid=3002, tileEntity = PC_TileEntityPuffer.class)
public class PC_BlockPuffer extends PC_Block {

	public PC_BlockPuffer(int id) {
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
		// TODO Auto-generated method stub

	}

	
	
}
