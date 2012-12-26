package powercraft.net;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.management.PC_ItemBlock;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;

public class PCnt_ItemBlockSensor extends PC_ItemBlock {

	public PCnt_ItemBlockSensor(int id) {
		super(id);
		setHasSubtypes(true);
	}
	
	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return (new StringBuilder()).append(super.getItemName()).append(".")
				.append(itemstack.getItemDamage() == 0 ? "item" : itemstack.getItemDamage() == 1 ? "living" : "player").toString();
	}
	
	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this, 1, 0));
		arrayList.add(new ItemStack(this, 1, 1));
		arrayList.add(new ItemStack(this, 1, 2));
		return arrayList;
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".item", "Item Proximity Detector", null));
			names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".living", "Mob Proximity Detector", null));
			names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".player", "Player Proximity Detector", null));
            return names;
		}
		return null;
	}

}
