package powercraft.hologram;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemStack;
import powercraft.management.PC_ItemArmor;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.ModuleInfo;

public class PChg_ItemArmorHologramGlasses extends PC_ItemArmor {

	public PChg_ItemArmorHologramGlasses(int id) {
		super(id, EnumArmorMaterial.IRON, 0, HEAD);
		setIconIndex(1);
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName(), "Hologram Glasses", null));
            return names;
		case PC_Utils.MSG_LOAD_FROM_CONFIG:
			setTextureFile(ModuleInfo.getTerrainFile(getModule()));
			break;
		}
		return null;
	}

	@Override
	public String getArmorTextureFile(ItemStack itemstack) {
		PC_Renderer.glEnable(0xbe2);//GL_BLEND
		return ModuleInfo.getTextureDirectory(getModule())+"glasses.png";
	}

}
