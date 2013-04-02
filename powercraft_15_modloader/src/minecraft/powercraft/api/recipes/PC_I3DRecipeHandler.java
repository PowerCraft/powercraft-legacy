package powercraft.api.recipes;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import powercraft.api.utils.PC_Struct2;
import powercraft.api.utils.PC_VecI;

public interface PC_I3DRecipeHandler {
	
	public boolean foundStructAt(EntityPlayer entityplayer, World world, PC_Struct2<PC_VecI, Integer> structStart);
	
	public boolean canBeCrafted();
	
}
