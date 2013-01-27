package powercraft.itemstorage;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.management.PC_Item;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_VecI;

public class PCis_ItemCompressor extends PC_Item {
	
	public static final int NORMAL = 0, ENDERACCESS = 1, HIGHT = 2, BIG = 3;
	public static final String id2Name[] = {"normal", "enderaccess", "hight", "big"};
	public static final int id2Texture[] = {0, 2, 3, 1};
	
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
	
	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this, 1, NORMAL));
		arrayList.add(new ItemStack(this, 1, ENDERACCESS));
		arrayList.add(new ItemStack(this, 1, HIGHT));
		arrayList.add(new ItemStack(this, 1, BIG));
		return arrayList;
	}
	
	@Override
	public String getItemNameIS(ItemStack itemStack) {
		return super.getItemName() + "." + id2Name[itemStack.getItemDamage()];
	}
	
	@Override
	public int getIconFromDamage(int par1) {
		return id2Texture[par1];
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName()+"."+id2Name[NORMAL], "compressor", null));
			names.add(new PC_Struct3<String, String, String[]>(getItemName()+"."+id2Name[ENDERACCESS], "ender compressor", null));
			names.add(new PC_Struct3<String, String, String[]>(getItemName()+"."+id2Name[HIGHT], "high stack compressor", null));
			names.add(new PC_Struct3<String, String, String[]>(getItemName()+"."+id2Name[BIG], "big compressor", null));
			return names;
		}
		return null;
	}

	public static PCis_CompressorInventory getInventoryFor(EntityPlayer player){
		ItemStack compressor = player.inventory.getCurrentItem();
		switch(compressor.getItemDamage()){
		case NORMAL:
			return new PCis_NormalCompressorInventory(player);
		case ENDERACCESS:
			return new PCis_EnderCompressorInventory(player);
		case HIGHT:
			return new PCis_HightCompressorInventory(player);
		case BIG:
			return new PCis_NormalCompressorInventory(player, new PC_VecI(9, 6));
		default:
			return null;
		}
	}
	
}
