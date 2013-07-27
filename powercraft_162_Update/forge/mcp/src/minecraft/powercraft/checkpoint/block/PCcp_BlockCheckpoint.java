package powercraft.checkpoint.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import powercraft.api.PC_InventoryUtils;
import powercraft.api.PC_Utils;
import powercraft.api.block.PC_Block;
import powercraft.api.block.PC_BlockInfo;
import powercraft.api.registry.PC_TextureRegistry;
import powercraft.apiOld.registry.PC_LangRegistry;
import powercraft.apiOld.utils.PC_VecI;

@PC_BlockInfo(name="Checkpoint", blockid="checkpoint", defaultid=3100, tileEntity=PCcp_TileEntityCheckpoint.class)
public class PCcp_BlockCheckpoint extends PC_Block {

	public PCcp_BlockCheckpoint(int id) {
		super(id, Material.air);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F/16.0F, 1.0F);
        setHardness(1.0F);
        setResistance(8.0F);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public void loadIcons() {
		blockIcon = PC_TextureRegistry.registerIcon("Icon");
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z,
			Entity entity) {
		if(entity instanceof EntityPlayer){
			PCcp_TileEntityCheckpoint te = PC_Utils.getTE(world, x, y, z);
			EntityPlayer player = (EntityPlayer) entity;
			if(!world.isRemote){
				if(player.ticksExisted<=2 && PC_InventoryUtils.getInventoryFullSlots(player.inventory)==0 && player.experienceTotal==0&&!player.onGround){
					for(int i=0; i<te.getSizeInventory(); i++){
						ItemStack is = te.getStackInSlot(i);
						if(is!=null){
							player.inventory.setInventorySlotContents(i, is.copy());
						}
					}
				}
			}
			if(te.isCollideTriggerd()){
				ChunkCoordinates cc = new ChunkCoordinates(x, y, z);
				if(!player.getBedLocation(player.dimension).equals(cc)){
					player.setSpawnChunk(cc, true);
					if(world.isRemote){
			        	PC_Utils.chatMsg(PC_LangRegistry.tr("pc.checkpoint.setSpawn", new PC_VecI(x, y, z).toString()));
			        }
				}
			}
		}
	}

	@Override
	public void registerRecipes() {
		
	}

}
