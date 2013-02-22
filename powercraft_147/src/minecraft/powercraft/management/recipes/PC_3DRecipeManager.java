package powercraft.management.recipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import powercraft.management.PC_VecI;
import powercraft.management.registry.PC_RecipeRegistry;

//AlphaI
@Deprecated
public class PC_3DRecipeManager {

	//AlphaI
	@Deprecated
	public static void add3DRecipe(PC_3DRecipe recipe){
		PC_RecipeRegistry.add3DRecipe(recipe);
	}
	
	//AlphaI
	@Deprecated
	public static void add3DRecipe(PC_I3DRecipeHandler obj, Object...o){
		PC_RecipeRegistry.add3DRecipe(obj, o);
	}
	
	//AlphaI
	@Deprecated
	public static boolean searchRecipeAndDo(EntityPlayer entityplayer, World world, PC_VecI pos){
		return PC_RecipeRegistry.searchRecipe3DAndDo(entityplayer, world, pos);
	}
	
}
