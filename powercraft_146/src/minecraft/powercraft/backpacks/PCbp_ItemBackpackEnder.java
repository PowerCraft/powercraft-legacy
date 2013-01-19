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

public class PCbp_ItemBackpackEnder extends PC_Item {

	public PCbp_ItemBackpackEnder(int id) {
		super(id);
	}

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (!world.isRemote){
            Gres.openGres("BackpackEnder", entityplayer);
        }
        return itemstack;
    }

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName(), "Ender Backpack", Arrays.asList("The backpack which connects to the Enderchest").toArray()));
            return names;
		}
		return null;
	}

}
