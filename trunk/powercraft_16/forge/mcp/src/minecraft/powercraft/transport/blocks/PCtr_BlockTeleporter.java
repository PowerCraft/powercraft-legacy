package powercraft.transport.blocks;

import net.minecraft.block.material.Material;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;

@PC_BlockInfo(name="Teleporter", blockid="teleporter", defaultid=4023)
public class PCtr_BlockTeleporter extends PC_Block {

	public PCtr_BlockTeleporter(int id) {
		super(id, Material.ground);
	}

	@Override
	public void loadIcons() {
		
	}

	@Override
	public void registerRecipes() {

	}

}
