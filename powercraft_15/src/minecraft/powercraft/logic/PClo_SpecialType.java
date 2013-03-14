package powercraft.logic;

public class PClo_SpecialType
{
    public static final int TOTAL_SPECIAL_COUNT = 6;

    @SuppressWarnings("javadoc")
    public static final int DAY = 0, NIGHT = 1, RAIN = 2, CHEST_EMPTY = 3, CHEST_FULL = 4, SPECIAL = 5;

    public static String[] names = new String[TOTAL_SPECIAL_COUNT];

    static
    {
        names[DAY] = "day";
        names[NIGHT] = "night";
        names[RAIN] = "rain";
        names[CHEST_EMPTY] = "chestEmpty";
        names[CHEST_FULL] = "chestFull";
        names[SPECIAL] = "special";
    }

    public static String[] getTextures(){
    	String[] textures = new String[1+2*TOTAL_SPECIAL_COUNT];
    	textures[0] = "sideplate";
    	for(int i=0; i<TOTAL_SPECIAL_COUNT; i++){
    		textures[i+1] = names[i]+"_off";
    	}
    	for(int i=0; i<TOTAL_SPECIAL_COUNT; i++){
    		textures[i+1+TOTAL_SPECIAL_COUNT] = names[i]+"_on";
    	}
    	return textures;
    }
}
