package powercraft.logic;

public class PClo_DelayerType
{
    public static final int TOTAL_DELAYER_COUNT = 2;

    @SuppressWarnings("javadoc")
    public static final int FIFO = 0, HOLD = 1;

    public static String[] names = new String[TOTAL_DELAYER_COUNT];

    static
    {
        names[FIFO] = "buffer";
        names[HOLD] = "slowRepeater";
    }
    
    public static String[] getTextures(){
    	String[] textures = new String[1+2*TOTAL_DELAYER_COUNT];
    	textures[0] = "sideplate";
    	for(int i=0; i<TOTAL_DELAYER_COUNT; i++){
    		textures[i+1] = names[i]+"_off";
    	}
    	for(int i=0; i<TOTAL_DELAYER_COUNT; i++){
    		textures[i+1+TOTAL_DELAYER_COUNT] = names[i]+"_on";
    	}
    	return textures;
    }
    
}
