/**
 * 
 */
package structurepattern.blocks;

import net.minecraft.block.material.Material;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.core.blocks.PC_TileEntityRoaster;

/**
 * @author Aaron
 *
 */
@PC_BlockInfo(name = "Roaster", blockid = "roaster", defaultid = 3003, tileEntity = PC_TileEntityRoaster.class)
public class PCsp_BlockPattern extends PC_Block {

	/**
	 * @param id
	 * @param material
	 */
	protected PCsp_BlockPattern(int id) {
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
