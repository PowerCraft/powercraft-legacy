package powercraft.api.security;

public enum PC_Permission {

	NEEDPASSWORD, ENTERGUI, CHANGEGUI, HANDLEUSER, INTERACT, ADVENTUREACCESS, BLOCKHARVEST;
	
	public long id(){
		return 1<<ordinal();
	}
	
}
