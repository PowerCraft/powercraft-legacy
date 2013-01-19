package powercraft.backpacks;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.management.PC_Item;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.Gres;

public class PCbp_ItemBackpack extends PC_Item {

	public static String idToName[] = {"normal", "ender"};
	
	public PCbp_ItemBackpack(int id) {
		super(id);
		setMaxDamage(0);
        setMaxStackSize(1);
        setHasSubtypes(true);
	}
	
    @Override
	public String getItemNameIS(ItemStack itemstack) {
		return getItemName()+"."+idToName[itemstack.getItemDamage()];
	}

	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (!world.isRemote){
        	Gres.openGres("Backpack", entityplayer);
        }
        return itemstack;
    }
	
	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this, 1, 0));
		arrayList.add(new ItemStack(this, 1, 1));
		return arrayList;
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName()+"."+idToName[0], "Backpack", null));
			names.add(new PC_Struct3<String, String, String[]>(getItemName()+"."+idToName[1], "Ender Backpack", new String[]{"The backpack which connects to the Enderchest"}));
			return names;
		}
		return null;
	}

	public static boolean isEnderBackpack(ItemStack backpack) {
		return backpack.getItemDamage()==1;
	}

}
