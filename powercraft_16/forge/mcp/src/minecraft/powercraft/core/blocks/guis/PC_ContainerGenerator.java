package powercraft.core.blocks.guis;


import net.minecraft.entity.player.EntityPlayer;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.core.blocks.tileentities.PC_TileEntityGenerator;


public class PC_ContainerGenerator extends PC_GresBaseWithInventory {

	protected final PC_TileEntityGenerator generator;


	public PC_ContainerGenerator(PC_TileEntityGenerator generator, EntityPlayer player) {

		super(player, generator);
		this.generator = generator;
	}

}
