package powercraft.logic;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import powercraft.api.PC_MathHelper;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.registry.PC_LangRegistry;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;

public class PClo_ItemBlockSpecial extends PC_ItemBlock
{
    public PClo_ItemBlockSpecial(int id)
    {
        super(id);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        for (int i = 0; i < PClo_SpecialType.TOTAL_SPECIAL_COUNT; i++)
        {
            arrayList.add(new ItemStack(this, 1, i));
        }

        return arrayList;
    }

    @Override
    public int getIconFromDamage(int i)
    {
        return PClo_App.special.getBlockTextureFromSideAndMetadata(1, 0);
    }

    @Override
    public String getItemNameIS(ItemStack itemstack)
    {
        return getItemName() + ".special" + itemstack.getItemDamage();
    }

    @Override
    public boolean isFull3D()
    {
        return false;
    }

    @Override
    public boolean shouldRotateAroundWhenRendering()
    {
        return false;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean b)
    {
        list.add(getDescriptionForSpecial(itemStack.getItemDamage()));
    }

    public static String getDescriptionForSpecial(int dmg)
    {
        return PC_LangRegistry.tr("pc.special." + PClo_SpecialType.names[PC_MathHelper.clamp_int(dmg, 0, PClo_SpecialType.TOTAL_SPECIAL_COUNT - 1)] + ".desc");
    }

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
	        for (int i = 0; i < PClo_SpecialType.TOTAL_SPECIAL_COUNT - 1; i++)
	        {
	            names.add(new LangEntry(getItemName() + ".special"+i, "sensor "+PClo_SpecialType.names[i]));       
	        };

	        int i = PClo_SpecialType.TOTAL_SPECIAL_COUNT - 1;

	        names.add(new LangEntry(getItemName() + ".special"+i, PClo_SpecialType.names[i]+" controller"));

            return names;
		}
		return null;
	}
}
