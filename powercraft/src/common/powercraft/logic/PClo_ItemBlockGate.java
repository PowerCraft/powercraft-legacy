package powercraft.logic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import powercraft.core.PC_ItemBlock;
import powercraft.core.PC_TileEntity;
import powercraft.core.PC_Utils;

public class PClo_ItemBlockGate extends PC_ItemBlock {

	public PClo_ItemBlockGate(int id){
		super(id);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	@Override
	public String[] getDefaultNames() {
		List<String> s =  new ArrayList<String>();
		for(int i=0; i<PClo_GateType.TOTAL_GATE_COUNT; i++){
			s.add(getItemName()+".gate"+i);
			s.add("Gate "+PClo_GateType.names[i]);
		};
		s.add(getItemName());
		s.add("Gate");
		return s.toArray(new String[0]);
	}
	
	@Override
	public String getCraftingToolModule(){
		return mod_PowerCraftLogic.getInstance().getNameWithoutPowerCraft();
	}
	
	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		for(int i=0; i<PClo_GateType.TOTAL_GATE_COUNT; i++)
			arrayList.add(new ItemStack(this, 1, i));
		return arrayList;
	}
	
	@Override
	public int getIconFromDamage(int i) {
		return mod_PowerCraftLogic.gate.getBlockTextureFromSideAndMetadata(1, 0);
	}

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return getItemName() + ".gate" + itemstack.getItemDamage();
	}
	
	@Override
	public boolean isFull3D() {
		return false;
	}

	@Override
	public boolean shouldRotateAroundWhenRendering() {
		return false;
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean b) {
		list.add(getDescriptionForGate(itemStack.getItemDamage()));
	}

	/**
	 * Get description bubble for gate
	 * 
	 * @param dmg gate item damage value
	 * @return the description string
	 */
	public static String getDescriptionForGate(int dmg) {
		return PC_Utils.tr("pc.gate." + PClo_GateType.names[MathHelper.clamp_int(dmg, 0, PClo_GateType.TOTAL_GATE_COUNT - 1)] + ".desc");
	}
	
}
