package powercraft.checkpoint.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import powercraft.api.block.PC_Block;
import powercraft.api.block.PC_BlockInfo;
import powercraft.api.registry.PC_TextureRegistry;

@PC_BlockInfo(name="Tutorial", blockid="tutorial", defaultid=2000, tileEntity=PCcp_TileEntityCheckpoint.class)
public class PCcp_BlockCheckpoint extends PC_Block {

	public PCcp_BlockCheckpoint(int id) {
		super(id, Material.ground);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public void loadIcons() {
		blockIcon = PC_TextureRegistry.registerIcon("Icon");
	}

	@Override
	public void registerRecipes() {
		
	}

}
