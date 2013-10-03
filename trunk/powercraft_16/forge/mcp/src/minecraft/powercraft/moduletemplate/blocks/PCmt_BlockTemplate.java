/**
 * 
 */
package powercraft.moduletemplate.blocks;

import net.minecraft.block.material.Material;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.core.blocks.tileentities.PC_TileEntityRoaster;

/**
 * @author Aaron
 *
 */
@PC_BlockInfo(name = "BlockPattern", blockid = "blockpattern", defaultid = 3009, tileEntity = PC_TileEntityRoaster.class)
public class PCmt_BlockTemplate extends PC_Block {

	/**
	 * @param id
	 * @param material
	 */
	protected PCmt_BlockTemplate(int id) {
		super(id, Material.rock);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see powercraft.api.blocks.PC_IBlock#loadIcons()
	 */
	@Override
	public void loadIcons() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see powercraft.api.blocks.PC_IBlock#registerRecipes()
	 */
	@Override
	public void registerRecipes() {
		// TODO Auto-generated method stub

	}

}
