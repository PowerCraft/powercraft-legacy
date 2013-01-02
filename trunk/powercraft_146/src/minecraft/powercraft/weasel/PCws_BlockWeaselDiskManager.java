package powercraft.weasel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselString;

public class PCws_BlockWeaselDiskManager extends PC_Block implements PC_IPacketHandler {

	public PCws_BlockWeaselDiskManager(int id) {
		super(id, 6, Material.ground);
		setHardness(0.5F);
		setLightValue(0);
		setStepSound(Block.soundWoodFootstep);
		disableStats();
		setRequiresSelfNotify();
		setResistance(60.0F);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
		setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCws_TileEntityWeaselDiskManager();
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		setBlockBounds(0, 0, 0, 1, 1 - 2 * 0.0625F, 1);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		ItemStack ihold = player.getCurrentEquippedItem();

        if (ihold != null)
        {
            if (ihold.getItem() instanceof ItemBlock)
            {
                if (ihold.itemID == blockID)
                {
                    return false;
                }
            }
        }

        Gres.openGres("WeaselDiskManager", player, x, y, z);
        
        return true;
	}
	
	public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer){
        PC_Renderer.swapTerrain(block);
        float px = 0.0625F;
        
        ValueWriting.setBlockBounds(block, 0, 0, 0, 16 * px, 13 * px, 16 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new int[] { 230, 209, 210, 210, 210, 210 });

        ValueWriting.setBlockBounds(block, 0, 0, 0, 1, 1, 1);
        PC_Renderer.resetTerrain(true);
    }
	
	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			return "Digital Workbench";
		case PC_Utils.MSG_RENDER_INVENTORY_BLOCK:
			renderInventoryBlock((Block)obj[0], (Integer)obj[1], (Integer)obj[2], obj[3]);
			break;
		case PC_Utils.MSG_RENDER_WORLD_BLOCK:
			break;
		default:
			return null;
		}
		return true;
	}

	@Override
	public boolean handleIncomingPacket(EntityPlayer player, Object[] o) {
		ItemStack itemStack = (ItemStack)player.openContainer.getInventory().get(36);
		if(itemStack!=null){
			String msg = (String)o[0];
			if(msg.equals("setLabel")){
				PCws_ItemWeaselDisk.setLabel(itemStack, (String)o[1]);
			}else if(msg.equals("setColor")){
				PCws_ItemWeaselDisk.setColor(itemStack, (Integer)o[1]);
			}else if(msg.equals("setType")){
				PCws_ItemWeaselDisk.setType(itemStack, (Integer)o[1]);
			}else if(msg.equals("setText")){
				PCws_ItemWeaselDisk.setText(itemStack, (String)o[1]);
			}else if(msg.equals("setImageSize")){
				PC_VecI size = (PC_VecI)o[1];
				PCws_ItemWeaselDisk.setImageSize(itemStack, size);
				PCws_ItemWeaselDisk.setImageData(itemStack, new int[size.x][size.y]);
			}else if(msg.equals("setImageData")){
				Integer[] list = (Integer[])o[1];
				PC_VecI size = PCws_ItemWeaselDisk.getImageSize(itemStack);
				int num=0;
				int index=1;
				for(int i=0; i<size.x; i++){
					for(int j=0; j<size.y; j++){
						if(num>=list[0]){
							if(list.length<=index)
								break;
							PCws_ItemWeaselDisk.setImageColorAt(itemStack, new PC_VecI(i, j), list[index]);
							index++;
						}
						num++;
					}
				}
			}else if(msg.equals("setListText")){
				PCws_ItemWeaselDisk.setListText(itemStack, (String)o[1], (String)o[2]);
			}else if(msg.equals("removeMapVariable")){
				PCws_ItemWeaselDisk.removeMapVariable(itemStack, (String)o[1]);
			}else if(msg.equals("setMapVariable")){
				PCws_ItemWeaselDisk.setMapVariable(itemStack, (String)o[1], o[2] instanceof Integer?new WeaselInteger(o[2]):new WeaselString(o[2]));
			}
		}
		return false;
	}

}
