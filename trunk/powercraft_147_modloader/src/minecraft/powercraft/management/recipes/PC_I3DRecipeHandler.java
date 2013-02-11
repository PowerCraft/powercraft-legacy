package powercraft.management.recipes;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_VecI;

public interface PC_I3DRecipeHandler {

	boolean foundStructAt(EntityPlayer entityplayer, World world, PC_Struct2<PC_VecI, Integer> structStart);

}
