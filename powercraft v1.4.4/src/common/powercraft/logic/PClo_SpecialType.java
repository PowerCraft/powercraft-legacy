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

    public static int[] index = new int[TOTAL_SPECIAL_COUNT];

    static
    {
        index[DAY] = 86;
        index[NIGHT] = 87;
        index[RAIN] = 88;
        index[CHEST_EMPTY] = 89;
        index[CHEST_FULL] = 90;
        index[SPECIAL] = 91;
    }
}
