package powercraft.api.security;

import net.minecraft.entity.player.EntityPlayer;

public interface PC_IPermissionHandler {

	public boolean checkPermission(EntityPlayer player, PC_Permission permission, String password);
	
	public boolean hasPermission(EntityPlayer player, PC_Permission permission);
	
	public boolean tryPermission(EntityPlayer player, PC_Permission permission);
	
	public boolean tryPassword(EntityPlayer player, String password);
	
	public boolean needPassword(EntityPlayer player);
	
}
