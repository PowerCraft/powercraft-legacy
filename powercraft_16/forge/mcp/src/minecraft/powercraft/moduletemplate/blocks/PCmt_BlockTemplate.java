/**
 * 
 */
package powercraft.moduletemplate.blocks;

import net.minecraft.block.material.Material;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;

/**
 * @author Aaron
 *
 */
@PC_BlockInfo(name = "BlockPattern", blockid = "blockpattern", defaultid = 3009)
public class PCmt_BlockTemplate extends PC_Block {

	/**
	 * @param id
	 */
	public PCmt_BlockTemplate(int id) {
		super(id, Material.rock);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void loadIcons() {
		// TODO Auto-generated method stub

	}
	
	
	@Override
	public void registerRecipes() {
		// TODO Auto-generated method stub

	}

}
