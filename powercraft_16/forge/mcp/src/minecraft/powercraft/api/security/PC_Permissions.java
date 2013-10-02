package powercraft.api.security;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MD5String;
import net.minecraft.world.EnumGameType;
import powercraft.api.PC_Logger;
import powercraft.api.PC_Utils;

public final class PC_Permissions implements PC_IPermissionHandler{

	private final static MD5String MD5 = new MD5String("");
	
	private final String owner;
	private final HashMap<String, Long> permissions = new HashMap<String, Long>();
	private long defaultPermissions;
	private String passwordMD5;
	
	public PC_Permissions(String owner){
		this.owner = owner;
		defaultPermissions = -1;
	}
	
	public PC_Permissions(NBTTagCompound compound){
		owner = compound.getString("owner");
		int count = compound.getInteger("count");
		for(int i=0; i<count; i++){
			permissions.put(compound.getString("key["+i+"]"), compound.getLong("value["+i+"]"));
		}
		defaultPermissions = compound.getLong("defaultPermissions");
		if(compound.hasKey("password"))
			passwordMD5 = compound.getString("password");
	}
	
	public void saveToNBT(NBTTagCompound compound){
		compound.setString("owner", owner);
		int i=0;
		for(Entry<String, Long> e:permissions.entrySet()){
			compound.setString("key["+i+"]", e.getKey());
			compound.setLong("value["+i+"]", e.getValue());
			i++;
		}
		compound.setLong("defaultPermissions", defaultPermissions);
		if(passwordMD5!=null)
			compound.setString("password", passwordMD5);
	}
	
	@Override
	public boolean checkPermission(EntityPlayer player, PC_Permission permission, String password){
		if(!hasPermission(player, permission))
			return false;
		if(hasPermission(player, PC_Permission.NEEDPASSWORD)){
			if(!passwordMD5.equals(MD5.getMD5String(password))){
				return false;
			}
		}
		return hasPermission(player, permission);
	}
	
	@Override
	public boolean hasPermission(EntityPlayer player, PC_Permission permission){
		if(player==null)
			return false;
		String username = player.username;
		if(username==null)
			return false;
		if(username.equals(owner) || PC_Utils.isOP(username)){
			return permission!=PC_Permission.NEEDPASSWORD;
		}
		Long userPermissions = permissions.get(username);
		if(userPermissions==null){
			userPermissions = defaultPermissions;
			if((userPermissions&PC_Permission.ADVENTUREACCESS.id())==0 && PC_Utils.getGameTypeFor(player)==EnumGameType.ADVENTURE){
				return false;
			}
		}
		return (userPermissions&permission.id())!=0;
	}
	
	@Override
	public boolean tryPermission(EntityPlayer player, PC_Permission permission){
		if(hasPermission(player, permission)){
			return true;
		}
		if(permission!=PC_Permission.NEEDPASSWORD)
			PC_Logger.warning("Player %s tried to %s without permission", player.username, permission);
		return false;
	}
	
	@Override
	public boolean needPassword(EntityPlayer player){
		return hasPermission(player, PC_Permission.NEEDPASSWORD);
	}
	
}
