package powercraft.logic;

public class PClo_GateType
{
    public static final int TOTAL_GATE_COUNT = 7;

    @SuppressWarnings("javadoc")
    public static final int NOT = 0, AND = 1, NAND = 2, OR = 3, NOR = 4, XOR = 5, XNOR = 6;

    public static final int ROT_R_L = 0, ROT_L_D = 1, ROT_L_D_R = 2, ROT_R_D = 3;

    public static String[] names = new String[TOTAL_GATE_COUNT];

    static
    {
        names[NOT] = "not";
        names[AND] = "and";
        names[NAND] = "nand";
        names[OR] = "or";
        names[NOR] = "nor";
        names[XOR] = "xor";
        names[XNOR] = "xnor";
    }

    public static String[] getTextures(){
    	String[] textures = new String[1+2*TOTAL_GATE_COUNT];
    	textures[0] = "sideplate";
    	for(int i=0; i<TOTAL_GATE_COUNT; i++){
    		textures[i+1] = names[i]+"_off";
    	}
    	for(int i=0; i<TOTAL_GATE_COUNT; i++){
    		textures[i+1+TOTAL_GATE_COUNT] = names[i]+"_on";
    	}
    	return textures;
    }

    public static int rotateCornerSides(int gateType, int rotation)
    {
        if (gateType == NOT)
        {
            return ROT_L_D_R;
        }

        rotation++;

        if (rotation > 3)
        {
            rotation -= 4;
        }

        return rotation;
    }

    public static boolean getGateOutput(int gateType, int rotation, boolean i1, boolean i2, boolean i3)
    {
        switch (gateType)
        {
            case NOT:
                return !i2;

            case AND:
                switch (rotation)
                {
                    case ROT_R_L:
                        return i1 && i3;

                    case ROT_L_D:
                        return i1 && i2;

                    case ROT_L_D_R:
                        return i1 && i2 && i3;

                    case ROT_R_D:
                        return i2 && i3;
                }

                break;

            case NAND:
                switch (rotation)
                {
                    case ROT_R_L:
                        return !(i1 && i3);

                    case ROT_L_D:
                        return !(i1 && i2);

                    case ROT_L_D_R:
                        return !(i1 && i2 && i3);

                    case ROT_R_D:
                        return !(i2 && i3);
                }

                break;

            case OR:
                switch (rotation)
                {
                    case ROT_R_L:
                        return i1 || i3;

                    case ROT_L_D:
                        return i1 || i2;

                    case ROT_L_D_R:
                        return i1 || i2 || i3;

                    case ROT_R_D:
                        return i2 || i3;
                }

                break;

            case NOR:
                switch (rotation)
                {
                    case ROT_R_L:
                        return !(i1 || i3);

                    case ROT_L_D:
                        return !(i1 || i2);

                    case ROT_L_D_R:
                        return !(i1 || i2 || i3);

                    case ROT_R_D:
                        return !(i2 || i3);
                }

                break;

            case XOR:
                switch (rotation)
                {
                    case ROT_R_L:
                        return i1 != i3;

                    case ROT_L_D:
                        return i1 != i2;

                    case ROT_L_D_R:
                        return (i1 != i2) || (i2 != i3);

                    case ROT_R_D:
                        return i2 != i3;
                }

                break;

            case XNOR:
                switch (rotation)
                {
                    case ROT_R_L:
                        return i1 == i3;

                    case ROT_L_D:
                        return i1 == i2;

                    case ROT_L_D_R:
                        return (i1 == i2) && (i2 == i3);

                    case ROT_R_D:
                        return i2 == i3;
                }

                break;
        }

        return false;
    }
}
