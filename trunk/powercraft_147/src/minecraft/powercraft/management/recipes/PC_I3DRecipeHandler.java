package powercraft.management.recipes;

import powercraft.management.PC_Struct2;
import powercraft.management.PC_VecI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface PC_I3DRecipeHandler {

	boolean foundStructAt(EntityPlayer entityplayer, World world, PC_Struct2<PC_VecI, Integer> structStart);

}
