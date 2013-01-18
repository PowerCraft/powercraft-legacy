package powercraft.checkpoints;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_VecI;
import powercraft.transport.PCtr_BeltHelper;

public class PCcp_BlockCheckpoint extends PC_Block {
	
	public PCcp_BlockCheckpoint(int id) {
		super(id, 1, Material.air);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
        setHardness(1.0F);
        setResistance(8.0F);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(CreativeTabs.tabTools);
	}
    
    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        entityplayer.setSpawnChunk(new ChunkCoordinates(i, j, k), true);
        return true;
    }
    
	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		return null;
	}

}
