package powercraft.itemstorage;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_Item;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.Gres;

public class PCis_ItemCompressor extends PC_Item {
	
	public static final int NORMAL = 0, ENDERACCESS = 1;
	public static final String id2Name[] = {"normal", "enderaccess"};
	
	public PCis_ItemCompressor(int id) {
		super(id);
		setMaxDamage(0);
        setMaxStackSize(1);
        setHasSubtypes(true);
	}

	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (!world.isRemote){
        	Gres.openGres("Compressor", entityplayer);
        }
        return itemstack;
    }

	public int getFunctionForItemStack(ItemStack is){
		if(is.itemID == Block.enderChest.blockID){
			return ENDERACCESS;
		}
		return NORMAL;
	}
	
	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this, 1, NORMAL));
		arrayList.add(new ItemStack(this, 1, ENDERACCESS));
		return arrayList;
	}
	
	@Override
	public String getItemNameIS(ItemStack itemStack) {
		return super.getItemName() + "." + id2Name[itemStack.getItemDamage()];
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName()+"."+id2Name[NORMAL], "Compressor", null));
			names.add(new PC_Struct3<String, String, String[]>(getItemName()+"."+id2Name[ENDERACCESS], "Ender Compressor", null));
			return names;
		}
		return null;
	}

}
