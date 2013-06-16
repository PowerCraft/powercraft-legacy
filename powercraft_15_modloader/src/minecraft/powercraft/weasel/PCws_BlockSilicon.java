package powercraft.weasel;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.annotation.PC_OreInfo;
import powercraft.api.block.PC_Block;

@PC_BlockInfo(name="Silicon")
@PC_OreInfo(oreName="Silicon", genOresInChunk=1, genOresDepositMaxCount=10, genOresMinY=10, genOresMaxY=30)
public class PCws_BlockSilicon extends PC_Block {
	
	public PCws_BlockSilicon(int id) {
		super(id, Material.rock, "siliconore");
		setHardness(0.7F);
		setResistance(10.0F);
		setStepSound(Block.soundStoneFootstep);
		setCreativeTab(CreativeTabs.tabBlock);
	}

}
