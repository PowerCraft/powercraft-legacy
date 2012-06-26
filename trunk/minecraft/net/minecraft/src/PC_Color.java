package net.minecraft.src;

/**
 * PowerCraft color handling and manipulation class.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_Color implements PC_INBT {

	/** Red color channel */
	public double r;
	/** Green color channel */
	public double g;
	/** Blue color channel */
	public double b;

	/** meta of power crystal <b>item</b> with this exact color */
	private int powerCrystalMeta = -1;

	/** invisibility flag */
	private boolean invisible = false;

	/**
	 * Make color of R,G and B channels.
	 * 
	 * @param r red channel
	 * @param g green channel
	 * @param b blue channel
	 */
	public PC_Color(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	/**
	 * Make color of HEX
	 * 
	 * @param hex hex color
	 */
	public PC_Color(int hex) {
		this.r = red(hex);
		this.g = green(hex);
		this.b = blue(hex);
	}

	/**
	 * Get color as copy of another color.
	 * 
	 * @param source orig color
	 */
	public PC_Color(PC_Color source) {
		this.r = source.r;
		this.g = source.g;
		this.b = source.b;
	}

	/**
	 * Create white visible color with default settings.
	 */
	public PC_Color() {
		r = g = b = 1D;
	}

	/**
	 * @return this color as hex 0xRRGGBB
	 */
	public int getHex() {
		int r255 = (int) Math.round(r * 255);
		int g255 = (int) Math.round(g * 255);
		int b255 = (int) Math.round(b * 255);

		return r255 << 16 | g255 << 8 | b255;
	}

	/**
	 * Get PowerCrystal's metadata
	 * 
	 * @return power crystal's metadata equivalent to this color.
	 */
	public int getMeta() {
		return powerCrystalMeta;
	}

	/**
	 * Set power crystal's metadata equivalent to this color.
	 * 
	 * @param meta the metadata
	 * @return self
	 */
	public PC_Color setMeta(int meta) {
		powerCrystalMeta = meta;
		return this;
	}

	/**
	 * Set visibility flag.<br>
	 * This flag can be used to toggle particle rendering in lasers, for example.
	 * 
	 * @param visibility state
	 * @return self
	 */
	public PC_Color setVisible(boolean visibility) {
		invisible = !visibility;
		return this;
	}

	/**
	 * Is the color visible?
	 * 
	 * @return visible
	 */
	public boolean isVisible() {
		return !invisible;
	}

	/**
	 * Mix this color with other color
	 * 
	 * @param color other color
	 * @return the result color
	 */
	public PC_Color mixWith(PC_Color color) {
		return new PC_Color((r + color.r) / 2D, (g + color.g) / 2D, (b + color.b) / 2D);
	}

	/**
	 * Mix this color with other color
	 * 
	 * @param cr red
	 * @param cg green
	 * @param cb blue
	 * @return the result color
	 */
	public PC_Color mixWith(double cr, double cg, double cb) {
		return new PC_Color((r + cr) / 2D, (g + cg) / 2D, (b + cb) / 2D);
	}

	/**
	 * Converts given hex to a color.
	 * 
	 * @param hex the hex code
	 * @return color object
	 */
	public static PC_Color valueOf(int hex) {
		return new PC_Color(red(hex), green(hex), blue(hex));
	}

	/**
	 * @return copy of this color
	 */
	public PC_Color copy() {
		return new PC_Color(r, g, b);
	}

	/**
	 * Set color from hex
	 * 
	 * @param hex hex color
	 */
	public void setTo(int hex) {
		r = red(hex);
		g = green(hex);
		b = blue(hex);
	}

	/**
	 * Set color channels
	 * 
	 * @param cr
	 * @param cg
	 * @param cb
	 */
	public void setTo(double cr, double cg, double cb) {
		r = cr;
		g = cg;
		b = cb;
	}

	/**
	 * Get red channel from hex color, as double.
	 * 
	 * @param hex color
	 * @return red channel
	 */
	public static double red(int hex) {
		return (1D / 255D) * ((hex & 0xff0000) >> 16);
	}

	/**
	 * Get green channel from hex color, as double.
	 * 
	 * @param hex color
	 * @return red channel
	 */
	public static double green(int hex) {
		return (1D / 255D) * ((hex & 0x00ff00) >> 8);
	}

	/**
	 * Get blue channel from hex color, as double.
	 * 
	 * @param hex color
	 * @return red channel
	 */
	public static double blue(int hex) {
		return (1D / 255D) * ((hex & 0x0000ff));
	}


	// ..........................................orange....red......green.....darkblue..lightblue..purple.....cyan......yellow
	/** PowerCrystal colors */
	public static final int crystal_colors[] = { 0xff9900, 0xff1111, 0x39ff11, 0x5555ff, 0xCCCCFF, 0xff33ff, 0x33ffff, 0xffff00 };

	/** Control Light colors */
	public static final int light_colors[] = { 0x333333, 0xff0000, 0x009900, 0x553300, 0x3333ff, 0x9900ff, 0x00ffff, 0x666666, 0x434343, 0xe881a8, 0x41ff34, 0xffff00, 0x6666ff, 0xff0099, 0xff9900, 0xf0f0f0 };

	/**
	 * Enum of dye colors.
	 * call dye.RED.meta for the int damage value.
	 * 
	 * @author MightyPork
	 * @copy (c) 2012
	 */
	@SuppressWarnings("javadoc")
	public static enum dye {
		BLACK(0), RED(1), GREEN(2), BROWN(3), BLUE(4), PURPLE(5), CYAN(6), LIGHTGRAY(7), GRAY(8), PINK(9), LIME(10), YELLOW(11), LIGHTBLUE(
				12), MAGENTA(13), ORANGE(14), WHITE(15);

		public int meta;

		private dye(int m) {
			this.meta = m;
		}
	}

	/**
	 * Enum of cloth colors.
	 * call cloth.RED.meta for the int meta value.
	 * 
	 * @author MightyPork
	 * @copy (c) 2012
	 */
	@SuppressWarnings("javadoc")
	public static enum cloth {
		BLACK(15), RED(14), GREEN(13), BROWN(12), BLUE(11), PURPLE(10), CYAN(9), LIGHTGRAY(8), GRAY(7), PINK(6), LIME(5), YELLOW(4), LIGHTBLUE(
				3), MAGENTA(2), ORANGE(1), WHITE(0);

		public int meta;

		private cloth(int m) {
			this.meta = m;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setDouble("r", r);
		tag.setDouble("g", g);
		tag.setDouble("b", b);
		tag.setInteger("xtal_meta", powerCrystalMeta);
		tag.setBoolean("invisible", invisible);
		return tag;

	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		r = tag.getDouble("r");
		g = tag.getDouble("g");
		b = tag.getDouble("b");
		powerCrystalMeta = tag.getInteger("xtal_meta");
		invisible = tag.getBoolean("invisible");
		return this;
	}

}
