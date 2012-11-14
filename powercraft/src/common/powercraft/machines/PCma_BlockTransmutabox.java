package powercraft.machines;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import powercraft.core.PC_Block;
import powercraft.core.PC_IPowerReceiver;

public class PCma_BlockTransmutabox extends PC_Block implements PC_IPowerReceiver {

	public PCma_BlockTransmutabox(int id) {
		super(id, 22, Material.rock);
		setHardness(1.5F);
		setResistance(50.0F);
		setStepSound(Block.soundMetalFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public void receivePower(World world, int x, int y, int z, float power) {
		
	}

	@Override
	public String getDefaultName() {
		return "Transmutabox";
	}

}
