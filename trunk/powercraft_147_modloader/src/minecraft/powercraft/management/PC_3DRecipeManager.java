package powercraft.management;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.World;

public class PC_3DRecipeManager {

	private static List<PC_3DRecipe> recipes = new ArrayList<PC_3DRecipe>();
	
	public static void add3DRecipe(PC_3DRecipe recipe){
		recipes.add(recipe);
	}
	
	public static void add3DRecipe(PC_I3DRecipeHandler obj, Object...o){
		recipes.add(new PC_3DRecipe(obj, o));
	}
	
	public static boolean searchRecipeAndDo(World world, PC_VecI pos){
		for(PC_3DRecipe recipe:recipes){
			if(recipe.isStruct(world, pos)){
				return true;
			}
		}
		return false;
	}
	
}
