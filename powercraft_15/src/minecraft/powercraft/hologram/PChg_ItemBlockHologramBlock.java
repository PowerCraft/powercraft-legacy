package powercraft.hologram;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.registry.PC_LangRegistry;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Utils;

public class PChg_ItemBlockHologramBlock extends PC_ItemBlock {

	public PChg_ItemBlockHologramBlock(int id) {
		super(id);
		setContainerItem(PChg_App.hologramBlockEmpty.getItemBlock());
	}
	
	@Override
	public boolean showInCraftingTool() {
		return false;
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
		if (!PC_Utils.setBID(world, x, y, z, getBlockID(), metadata))
        {
            return false;
        }

        if (world.getBlockId(x, y, z) == getBlockID())
        {
            Block block =  Block.blocksList[getBlockID()];
            block.onBlockPlacedBy(world, x, y, z, player, stack);
            NBTTagCompound nbtTag = stack.getTagCompound();
            if(nbtTag!=null){
            	ItemStack item = ItemStack.loadItemStackFromNBT(nbtTag.getCompoundTag("Item"));
            	if(item.getItem().getHasSubtypes()){
            		PC_Utils.setMD(world, x, y, z, item.getItemDamage());
            	}
            }
            TileEntity te = (TileEntity)PC_Utils.getTE(world, x, y, z);

            if (te == null)
            {
               PC_Utils.setTE(world, x, y, z, te = PC_Utils.createTileEntity(block, world, metadata));
            }
            
            if(te instanceof PC_TileEntity){
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
        return PC_LangRegistry.tr(getUnlocalizedName()+".desc.name", item.getDisplayName());
    }

	@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		names.add(new LangEntry(getUnlocalizedName(), "Hologramblock"));
		names.add(new LangEntry(getUnlocalizedName()+".desc", "Contains: %s"));
		return names;
	}

	
	
}
