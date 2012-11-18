package powercraft.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.src.NBTTagCompound;

public class PC_Color implements PC_INBT, Serializable
{
    private static Map<String, String> magicColors = new HashMap<String, String>();
    static
    {
        magicColors.put("[black]", "§0");
        magicColors.put("[navy]", "§1");
        magicColors.put("[blue]", "§1");
        magicColors.put("[dblue]", "§1");
        magicColors.put("[darkblue]", "§1");
        magicColors.put("[green]", "§2");
        magicColors.put("[dgreen]", "§2");
        magicColors.put("[darkgreen]", "§2");
        magicColors.put("[dcyan]", "§3");
        magicColors.put("[darkcyan]", "§3");
        magicColors.put("[daqua]", "§3");
        magicColors.put("[darkaqua]", "§3");
        magicColors.put("[darkred]", "§4");
        magicColors.put("[red]", "§4");
        magicColors.put("[dred]", "§4");
        magicColors.put("[purple]", "§5");
        magicColors.put("[orange]", "§6");
        magicColors.put("[grey]", "§7");
        magicColors.put("[gray]", "§7");
        magicColors.put("[dgrey]", "§8");
        magicColors.put("[darkgrey]", "§8");
        magicColors.put("[dgray]", "§8");
        magicColors.put("[darkgray]", "§8");
        magicColors.put("[indigo]", "§9");
        magicColors.put("[lblue]", "§9");
        magicColors.put("[lightblue]", "§9");
        magicColors.put("[lime]", "§a");
        magicColors.put("[limegreen]", "§a");
        magicColors.put("[aqua]", "§b");
        magicColors.put("[cyan]", "§b");
        magicColors.put("[lred]", "§c");
        magicColors.put("[lightred]", "§c");
        magicColors.put("[pink]", "§d");
        magicColors.put("[yellow]", "§e");
        magicColors.put("[white]", "§f");
        magicColors.put("[random]", "§k");
        magicColors.put("[bold]", "§l");
        magicColors.put("[b]", "§l");
        magicColors.put("[s]", "§m");
        magicColors.put("[strike]", "§m");
        magicColors.put("[u]", "§n");
        magicColors.put("[underline]", "§n");
        magicColors.put("[italics]", "§o");
        magicColors.put("[i]", "§o");
        magicColors.put("[reset]", "§r");
        magicColors.put("[r]", "§r");
        magicColors.put("[#0]", "§0");
        magicColors.put("[#1]", "§1");
        magicColors.put("[#2]", "§2");
        magicColors.put("[#3]", "§3");
        magicColors.put("[#4]", "§4");
        magicColors.put("[#5]", "§5");
        magicColors.put("[#6]", "§6");
        magicColors.put("[#7]", "§7");
        magicColors.put("[#8]", "§8");
        magicColors.put("[#9]", "§9");
        magicColors.put("[#a]", "§a");
        magicColors.put("[#b]", "§b");
        magicColors.put("[#c]", "§c");
        magicColors.put("[#d]", "§d");
        magicColors.put("[#e]", "§e");
        magicColors.put("[#f]", "§f");
    }

    private static HashMap<String, Integer> namedColors = new HashMap<String, Integer>();

    static
    {
        namedColors.put("white", 0xffffff);
        namedColors.put("silver", 0xc0c0c0);
        namedColors.put("gray", 0x808080);
        namedColors.put("black", 0x000000);
        namedColors.put("red", 0xff0000);
        namedColors.put("maroon", 0x800000);
        namedColors.put("yellow", 0xffff00);
        namedColors.put("olive", 0x808000);
        namedColors.put("lime", 0x00ff00);
        namedColors.put("green", 0x008000);
        namedColors.put("aqua", 0x00ffff);
        namedColors.put("teal", 0x008080);
        namedColors.put("blue", 0x0000ff);
        namedColors.put("navy", 0x000080);
        namedColors.put("fuchsia", 0xff00ff);
        namedColors.put("purple", 0x800080);
        namedColors.put("brick", 0xB22222);
        namedColors.put("darkred", 0x8B0000);
        namedColors.put("salmon", 0xFA8072);
        namedColors.put("pink", 0xff1493);
        namedColors.put("orange", 0xff4500);
        namedColors.put("gold", 0xffd700);
        namedColors.put("magenta", 0xff00ff);
        namedColors.put("violet", 0x9400d3);
        namedColors.put("indigo", 0x483D8B);
        namedColors.put("limegreen", 0x32cd32);
        namedColors.put("darkgreen", 0x006400);
        namedColors.put("cyan", 0x00ffff);
        namedColors.put("steel", 0x4682b4);
        namedColors.put("darkblue", 0x00008b);
        namedColors.put("brown", 0x8b4513);
        namedColors.put("lightgray", 0xd3d3d3);
        namedColors.put("darkgray", 0xa9a9a9);
    }

    public static String convertMagicColors(String input)
    {
        input = input.replaceAll("\\[/.*?\\]", "§r");
        input = input.replaceAll("</.*?>", "§r");

        for (Entry<String, String> entry : magicColors.entrySet())
        {
            input = input.replace(entry.getKey(), entry.getValue());
            input = input.replace(entry.getKey().replace('[', '<').replace(']', '>'), entry.getValue());
        }

        return input;
    }

    public double r;

    public double g;

    public double b;

    private int powerCrystalMeta = -1;

    private boolean invisible = false;

    public PC_Color(double r, double g, double b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public PC_Color(int hex)
    {
        this.r = red(hex);
        this.g = green(hex);
        this.b = blue(hex);
    }

    public PC_Color(PC_Color source)
    {
        this.r = source.r;
        this.g = source.g;
        this.b = source.b;
    }

    public PC_Color()
    {
        r = g = b = 1D;
    }

    private static Random rand = new Random();

    public PC_Color randomize()
    {
        do
        {
            r = rand.nextFloat();
            g = rand.nextFloat();
            b = rand.nextFloat();
        }
        while (r + g + b < 0.8F);

        return this;
    }

    public PC_Color randomizePure()
    {
        r = rand.nextFloat();
        g = rand.nextFloat();
        b = rand.nextFloat();
        return this;
    }

    public static PC_Color randomColor()
    {
        return new PC_Color().randomize();
    }

    public static PC_Color pureRandomColor()
    {
        return new PC_Color().randomizePure();
    }

    public int getHex()
    {
        int r255 = (int) Math.round(r * 255) & 0xff;
        int g255 = (int) Math.round(g * 255) & 0xff;
        int b255 = (int) Math.round(b * 255) & 0xff;
        return r255 << 16 | g255 << 8 | b255;
    }

    public int getMeta()
    {
        return powerCrystalMeta;
    }

    public PC_Color setMeta(int meta)
    {
        powerCrystalMeta = meta;
        return this;
    }

    public PC_Color setVisible(boolean visibility)
    {
        invisible = !visibility;
        return this;
    }

    public boolean isVisible()
    {
        return !invisible;
    }

    public PC_Color mixWith(PC_Color color)
    {
        return new PC_Color((r + color.r) / 2D, (g + color.g) / 2D, (b + color.b) / 2D);
    }

    public PC_Color mixWith(double cr, double cg, double cb)
    {
        return new PC_Color((r + cr) / 2D, (g + cg) / 2D, (b + cb) / 2D);
    }

    public static PC_Color valueOf(int hex)
    {
        return new PC_Color(red(hex), green(hex), blue(hex));
    }

    public PC_Color copy()
    {
        return new PC_Color(r, g, b);
    }

    public PC_Color setTo(int hex)
    {
        r = red(hex);
        g = green(hex);
        b = blue(hex);
        return this;
    }

    public PC_Color setTo(PC_Color color)
    {
        r = color.r;
        g = color.g;
        b = color.b;
        return this;
    }

    public void setTo(double cr, double cg, double cb)
    {
        r = cr;
        g = cg;
        b = cb;
    }

    public static double red(int hex)
    {
        return (1D / 255D) * ((hex & 0xff0000) >> 16);
    }

    public static double green(int hex)
    {
        return (1D / 255D) * ((hex & 0x00ff00) >> 8);
    }

    public static double blue(int hex)
    {
        return (1D / 255D) * ((hex & 0x0000ff));
    }

    public static final int crystal_colors[] = { 0xff9900, 0xff1111, 0x39ff11, 0x5555ff, 0xCCCCFF, 0xff33ff, 0x33ffff, 0xffff00 };

    @SuppressWarnings("javadoc")
    public static enum dye
    {
        BLACK(0), RED(1), GREEN(2), BROWN(3), BLUE(4), PURPLE(5), CYAN(6), LIGHTGRAY(7), GRAY(8), PINK(9), LIME(10), YELLOW(11), LIGHTBLUE(12), MAGENTA(
                13), ORANGE(14), WHITE(15);

        public int meta;

        private dye(int m)
        {
            this.meta = m;
        }
    }

    @SuppressWarnings("javadoc")
    public static enum cloth
    {
        BLACK(15), RED(14), GREEN(13), BROWN(12), BLUE(11), PURPLE(10), CYAN(9), LIGHTGRAY(8), GRAY(7), PINK(6), LIME(5), YELLOW(4), LIGHTBLUE(3), MAGENTA(
                2), ORANGE(1), WHITE(0);

        public int meta;

        private cloth(int m)
        {
            this.meta = m;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setDouble("r", r);
        tag.setDouble("g", g);
        tag.setDouble("b", b);
        tag.setInteger("xtal_meta", powerCrystalMeta);
        tag.setBoolean("invisible", invisible);
        return tag;
    }

    @Override
    public PC_INBT readFromNBT(NBTTagCompound tag)
    {
        r = tag.getDouble("r");
        g = tag.getDouble("g");
        b = tag.getDouble("b");
        powerCrystalMeta = tag.getInteger("xtal_meta");
        invisible = tag.getBoolean("invisible");
        return this;
    }

    public static PC_Color fromHex(int hex)
    {
        return new PC_Color().setTo(hex);
    }

    public static Integer getHexColorForName(String text)
    {
        if (namedColors.containsKey(text.toLowerCase()))
        {
            return namedColors.get(text.toLowerCase());
        }

        return null;
    }
}
