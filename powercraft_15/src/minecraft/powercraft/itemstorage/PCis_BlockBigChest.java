package powercraft.itemstorage;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.recipes.PC_I3DRecipeHandler;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Struct2;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

@PC_BlockInfo(name="Big Chest", tileEntity=PCis_TileEntityBigChest.class)
public class PCis_BlockBigChest extends PC_Block implements PC_I3DRecipeHandler {

	public PCis_BlockBigChest(int id) {
		super(id, Material.glass);
	}
	
	@Override
	public boolean showInCraftingTool() {
		return false;
	}
	
	@Override
	public TileEntity newTileEntity(World world) {
		return new PCis_TileEntityBigChest();
	}
	
	public int quantityDropped(Random par1Random){
        return 0;
    }
	
	public boolean isOpaqueCube(){
        return false;
    }
	
	public boolean renderAsNormalBlock(){
        return false;
    }
	
	@Override
	public int idPicked(World par1World, int par2, int par3, int par4){
		return Block.glass.blockID;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		PCis_TileEntityBigChest te = PC_Utils.getTE(world, x, y, z);
		PC_VecI pos = new PC_VecI(x, y, z);
		IInventory inv = te.getInventory();
		
		if(inv!=null && !world.isRemote){
			if(!world.isRemote){
				if(inv instanceof PCis_BigChestInventory){
					((PCis_BigChestInventory)inv).destroy = true;
				}
				PC_InventoryUtils.dropInventoryContents(inv, world, pos);
			}else{
				for(int i=0; i<inv.getSizeInventory(); i++){
					inv.setInventorySlotContents(i, null);
				}
			}
		}
		te.breakStruct();
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public boolean foundStructAt(EntityPlayer entityplayer, World world, PC_Struct2<PC_VecI, Integer> structStart) {
		PC_VecI pos;
		PCis_TileEntityBigChest te;
		
		pos = structStart.a;
		PC_Utils.setBID(world, pos, blockID, 0);
		te = PC_Utils.getTE(world, pos);
		te.setPos(PCis_TileEntityBigChest.BOTTOMBACKLEFT);
		
		pos = structStart.a.offset(0, 3, 0);
		PC_Utils.setBID(world, pos, blockID, 0);
		te = PC_Utils.getTE(world, pos);
		te.setPos(PCis_TileEntityBigChest.TOPBACKLEFT);
		
		pos = structStart.a.offset(3, 0, 0);
		PC_Utils.setBID(world, pos, blockID, 0);
		te = PC_Utils.getTE(world, pos);
		te.setPos(PCis_TileEntityBigChest.BOTTOMBACKRIGHT);
		
		pos = structStart.a.offset(3, 3, 0);
		PC_Utils.setBID(world, pos, blockID, 0);
		te = PC_Utils.getTE(world, pos);
		te.setPos(PCis_TileEntityBigChest.TOPBACKRIGHT);
		
		pos = structStart.a.offset(0, 0, 3);
		PC_Utils.setBID(world, pos, blockID, 0);
		te = PC_Utils.getTE(world, pos);
		te.setPos(PCis_TileEntityBigChest.BOTTOMFRONTLEFT);
		
		pos = structStart.a.offset(0, 3, 3);
		PC_Utils.setBID(world, pos, blockID, 0);
		te = PC_Utils.getTE(world, pos);
		te.setPos(PCis_TileEntityBigChest.TOPFRONTLEFT);
		
		pos = structStart.a.offset(3, 0, 3);
		PC_Utils.setBID(world, pos, blockID, 0);
		te = PC_Utils.getTE(world, pos);
		te.setPos(PCis_TileEntityBigChest.BOTTOMFRONTRIGHT);
		
		pos = structStart.a.offset(3, 3, 3);
		PC_Utils.setBID(world, pos, blockID, 0);
		te = PC_Utils.getTE(world, pos);
		te.setPos(PCis_TileEntityBigChest.TOPFRONTRIGHT);
		
		return true;
	}

	@Override
	public boolean canBeCrafted() {
		return blockID!=-1;
	}
	
	@Override
	public Icon getIcon(PC_Direction dir, int metadata) {
		return Block.glass.getIcon(dir.getMCDir(), metadata);
	}
	
}
