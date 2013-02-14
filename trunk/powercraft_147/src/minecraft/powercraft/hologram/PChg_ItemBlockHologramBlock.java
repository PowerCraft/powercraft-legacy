package powercraft.hologram;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import powercraft.logic.PClo_GateType;
import powercraft.management.PC_ItemBlock;
import powercraft.management.PC_MathHelper;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ValueWriting;

public class PChg_ItemBlockHologramBlock extends PC_ItemBlock {

	public PChg_ItemBlockHologramBlock(int id) {
		super(id);
		setContainerItem(PChg_App.hologramBlockEmpty.getItemBlock());
	}
	
	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		return true;
	}

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack par1ItemStack) {
		return false;
	}

	@Override
	public void doCrafting(ItemStack itemStack, InventoryCrafting inventoryCrafting) {
		ItemStack itemBlock = null;
		for(int i=0; i<inventoryCrafting.getSizeInventory(); i++){
			if(inventoryCrafting.getStackInSlot(i)!=null){
				if(inventoryCrafting.getStackInSlot(i).getItem() != getContainerItem()){
					itemBlock = inventoryCrafting.getStackInSlot(i);
					break;
				}
			}
		}
		NBTTagCompound nbtTag = itemStack.getTagCompound();
		if(nbtTag==null)
			nbtTag = new NBTTagCompound();
		NBTTagCompound nbtTag2 = new NBTTagCompound();
		itemBlock = itemBlock.copy();
		itemBlock.stackSize=1;
		itemBlock.writeToNBT(nbtTag2);
		nbtTag.setCompoundTag("Item", nbtTag2);
		itemStack.setTagCompound(nbtTag);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		if (!world.setBlockAndMetadataWithNotify(x, y, z, getBlockID(), metadata))
        {
            return false;
        }

        if (world.getBlockId(x, y, z) == getBlockID())
        {
            Block block =  Block.blocksList[getBlockID()];
            block.onBlockPlacedBy(world, x, y, z, player);
            NBTTagCompound nbtTag = stack.getTagCompound();
            if(nbtTag!=null){
            	ItemStack item = ItemStack.loadItemStackFromNBT(nbtTag.getCompoundTag("Item"));
            	if(item.getItem().getHasSubtypes()){
            		ValueWriting.setMD(world, x, y, z, item.getItemDamage());
            	}
            }
            TileEntity te = (TileEntity)GameInfo.getTE(world, x, y, z);

            if (te == null)
            {
                te = (TileEntity)ValueWriting.setTE(world, x, y, z, block.createTileEntity(world, metadata));
            }

            if (te instanceof PC_TileEntity)
            {
                ((PC_TileEntity)te).create(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
            }
        }

        return true;
	}

	@Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean b){
        list.add(getDescriptionForHologram(itemStack));
    }
	
	public String getDescriptionForHologram(ItemStack itemStack){
		NBTTagCompound nbtTag = itemStack.getTagCompound();
		if(nbtTag==null)
			return "";
		ItemStack item = ItemStack.loadItemStackFromNBT(nbtTag.getCompoundTag("Item"));
        return Lang.tr(getItemName()+".desc.name", item.getDisplayName());
    }
	
	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName(), "Hologramblock", null));
			names.add(new PC_Struct3<String, String, String[]>(getItemName()+".desc", "Contains: %s", null));
			return names;
		case PC_Utils.MSG_DONT_SHOW_IN_CRAFTING_TOOL:
			return true;
		}
		return null;
	}

}
