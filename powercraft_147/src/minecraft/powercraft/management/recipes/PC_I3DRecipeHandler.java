package powercraft.management.recipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_VecI;

public interface PC_I3DRecipeHandler {

	public boolean foundStructAt(EntityPlayer entityplayer, World world, PC_Struct2<PC_VecI, Integer> structStart);
	public boolean canBeCrafted();
	
}
