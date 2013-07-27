package powercraft.checkpoint;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.registry.PC_LangRegistry;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

@PC_BlockInfo(name = "Checkpoint", tileEntity=PCcp_TileEntityCheckpoint.class)
public class PCcp_BlockCheckpoint extends PC_Block implements PC_IItemInfo {
	
	public PCcp_BlockCheckpoint(int id) {
		super(id, Material.air, "checkpoint_bottom", "checkpoint_top", "checkpoint_side");
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F/16.0F, 1.0F);
        setHardness(1.0F);
        setResistance(8.0F);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
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
				if(!player.getBedLocation().equals(cc)){
					player.setSpawnChunk(cc, true);
					if(world.isRemote){
			        	PC_Utils.chatMsg(PC_LangRegistry.tr("pc.checkpoint.setSpawn", new PC_VecI(x, y, z).toString()));
			        }
				}
			}
		}
	}

	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9){
		if(entityplayer.isSneaking()){
			if(!world.isRemote && PC_Utils.isPlayerOPOrOwner(entityplayer)){
				PC_GresRegistry.openGres("Checkpoint", entityplayer, PC_Utils.<PC_TileEntity>getTE(world, i, j, k));
			}
			return true;
		}
		if(world.isRemote){
			PC_Utils.chatMsg(PC_LangRegistry.tr("pc.checkpoint.setSpawn", new PC_VecI(i, j, k).toString()));
        }
		entityplayer.setSpawnChunk(new ChunkCoordinates(i, j, k), true);
        return true;
    }

	@Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }
    
    @Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}

}
