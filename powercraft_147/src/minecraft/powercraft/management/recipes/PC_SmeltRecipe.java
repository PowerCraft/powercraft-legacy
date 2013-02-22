package powercraft.management.recipes;

import powercraft.management.PC_ItemStack;

public class PC_SmeltRecipe implements PC_IRecipe {

	private PC_ItemStack result;
	private PC_ItemStack input;
	private float experience;
	
	public PC_SmeltRecipe(PC_ItemStack result, PC_ItemStack input){
		this(result, input, 0.0f);
	}
	
	public PC_SmeltRecipe(PC_ItemStack result, PC_ItemStack input, float experience){
		this.result = result;
		this.input = input;
		this.experience = experience;
	}
	
	public PC_ItemStack getResult(){
		return result.copy();
	}
	
	public PC_ItemStack getInput(){
		return input.copy();
	}
	
	public float getExperience(){
		return experience;
	}
	
}
