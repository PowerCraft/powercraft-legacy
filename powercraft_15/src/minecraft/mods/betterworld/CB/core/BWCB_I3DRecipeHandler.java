package mods.betterworld.CB.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;


public interface BWCB_I3DRecipeHandler {

	public boolean foundStructAt(EntityPlayer entityplayer, World world, BWCB_Struct2<BWCB_VecI, Integer> structStart);
	public boolean canBeCrafted();
	
}
