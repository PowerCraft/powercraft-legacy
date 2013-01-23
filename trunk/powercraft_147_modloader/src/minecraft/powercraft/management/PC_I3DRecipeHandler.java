package powercraft.management;

import net.minecraft.src.World;

public interface PC_I3DRecipeHandler {

	boolean foundStructAt(World world, PC_Struct2<PC_VecI, Integer> structStart);

}
