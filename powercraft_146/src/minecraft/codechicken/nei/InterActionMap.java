package codechicken.nei;

public enum InterActionMap
{
	TIME(false),
	CREATIVE(false),
	RAIN(false),
	ITEM(false),
	HEAL(false),
	DELETE(true),
	MAGNET(true),
	ENCHANT(true);
	
	public boolean requiresSMPCounterpart;
	
	InterActionMap(boolean requiresSMPCounterpart)
	{
		this.requiresSMPCounterpart = requiresSMPCounterpart;
	}
	
	public static InterActionMap getAction(String name)
	{
		return valueOf(name.toUpperCase());
	}
	
	public String getName()
	{
		return name().toLowerCase();
	}
	
	public static final int protocol = 4;
}
