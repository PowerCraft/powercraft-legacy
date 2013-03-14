package powercraft.logic;

public class PClo_RepeaterType
{
    public static final int TOTAL_REPEATER_COUNT = 6;

    @SuppressWarnings("javadoc")
    public static final int CROSSING = 0, SPLITTER_I = 1, REPEATER_STRAIGHT = 2, REPEATER_CORNER = 3, REPEATER_STRAIGHT_I = 4, REPEATER_CORNER_I = 5;

    public static String[] names = new String[TOTAL_REPEATER_COUNT];

    static
    {
        names[CROSSING] = "crossing";
        names[SPLITTER_I] = "splitter";
        names[REPEATER_STRAIGHT] = "repeaterStraight";
        names[REPEATER_CORNER] = "repeaterCorner";
        names[REPEATER_STRAIGHT_I] = "repeaterStraightInstant";
        names[REPEATER_CORNER_I] = "repeaterCornerInstant";
    }

    public static String[] getTextures(){
    	String[] textures = new String[1+2*TOTAL_REPEATER_COUNT];
    	textures[0] = "sideplate";
    	for(int i=0; i<TOTAL_REPEATER_COUNT; i++){
    		textures[i+1] = names[i]+"_off";
    	}
    	for(int i=0; i<TOTAL_REPEATER_COUNT; i++){
    		textures[i+1+TOTAL_REPEATER_COUNT] = names[i]+"_on";
    	}
    	return textures;
    }

    public static boolean[] canBeOn = new boolean[TOTAL_REPEATER_COUNT];
    static
    {
        canBeOn[CROSSING] = false;
        canBeOn[SPLITTER_I] = false;
        canBeOn[REPEATER_STRAIGHT] = true;
        canBeOn[REPEATER_CORNER] = true;
        canBeOn[REPEATER_STRAIGHT_I] = false;
        canBeOn[REPEATER_CORNER_I] = false;
    }

    public static int change(int repeaterType, int type)
    {
        type++;

        switch (repeaterType)
        {
            case CROSSING:
            case SPLITTER_I:
                if (type > 3)
                {
                    type -= 4;
                }

                break;

            case REPEATER_CORNER:
            case REPEATER_CORNER_I:
                if (type > 1)
                {
                    type -= 2;
                }

                break;

            default:
                type = 0;
                break;
        }

        return type;
    }
}
