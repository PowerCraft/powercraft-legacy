package powercraft.logic;


public class PClo_FlipFlopType
{
    public static final int TOTAL_FLIPFLOP_COUNT = 4;

    @SuppressWarnings("javadoc")
    public static final int D = 0, RS = 1, T = 2, RANDOM = 3;

    public static String[] names = new String[TOTAL_FLIPFLOP_COUNT];

    static
    {
        names[D] = "D";
        names[RS] = "RS";
        names[T] = "T";
        names[RANDOM] = "random";
    }

	public static String[] getTextures(){
    	String[] textures = new String[1+2*TOTAL_FLIPFLOP_COUNT];
    	textures[0] = "sideplate";
    	for(int i=0; i<TOTAL_FLIPFLOP_COUNT; i++){
    		textures[i+1] = names[i]+"_off";
    	}
    	for(int i=0; i<TOTAL_FLIPFLOP_COUNT; i++){
    		textures[i+1+TOTAL_FLIPFLOP_COUNT] = names[i]+"_on";
    	}
    	return textures;
    }
}
