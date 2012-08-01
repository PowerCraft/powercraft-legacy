package net.minecraft.src;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import weasel.Calc;
import weasel.IWeaselHardware;
import weasel.WeaselEngine;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselNull;
import weasel.obj.WeaselObject;



/**
 * Class for bitmap manipulation
 * 
 * @author MightyPork, Darkhog, XOR19
 *
 */
public class PC_BitmapUtils {
	private static final int INV = -1;

	/**
	 * @param bitmap
	 * @return width of the bitmap
	 */
	public static int width(int[][] bitmap) {
		return bitmap.length;
	}

	/**
	 * @param bitmap
	 * @return height of the bitmap
	 */
	public static int height(int[][] bitmap) {
		return bitmap[0].length;
	}

	/**
	 * set bitmap pixel
	 * @param bitmap bitmap
	 * @param x x
	 * @param y y
	 * @param color color rgb or -1
	 */
	public static void setPixel(int[][] bitmap, int x, int y, int color) {
		if (x < 0 || x >= width(bitmap) || y < 0 || y >= height(bitmap)) return;
		bitmap[x][y] = color;
	}

	/**
	 * Get bitmap pixel
	 * @param bitmap bitmap
	 * @param x x
	 * @param y y
	 * @return color rgb or -1
	 */
	public static int getPixel(int[][] bitmap, int x, int y) {
		if (x < 0 || x >= width(bitmap) || y < 0 || y >= height(bitmap)) return INV;
		return bitmap[x][y];
	}


	/**
	 * Draw ellipse with center and width, height
	 * @param bitmap bitmap
	 * @param x1 center x
	 * @param y1 center y
	 * @param w width
	 * @param h height
	 * @param color color rgb or -1
	 */
	public static void ellipse(int[][] bitmap, int x1, int y1, int w, int h, int color) {
		if(w<0) {
			x1 += w;
			w = -w;
		}
		if(h<0) {
			y1 += h;
			h = -h;
		}
		
		float xc = x1 + (w / 2) - 1;
		float yc = y1 + (h / 2) - 1;
		float rx = w / 2;
		float ry = h / 2;
		float rxSq = rx * rx;
		float rySq = ry * ry;
		float x = 0, y = ry, p;
		float px = 0, py = 2 * rxSq * y;
		drawEllipse(bitmap, xc, yc, x, y, color);
		//Region 1
		p = rySq - (rxSq * ry) + (0.25f * rxSq);
		while (px < py) {
			x++;
			px = px + 2 * rySq;
			if (p < 0)
				p = p + rySq + px;
			else {
				y--;
				py = py - 2 * rxSq;
				p = p + rySq + px - py;
			}
			drawEllipse(bitmap, xc, yc, x, y, color);

		}
		//Region 2
		p = rySq * (x + 0.5f) * (x + 0.5f) + rxSq * (y - 1) * (y - 1) - rxSq * rySq;
		while (y > 0) {
			y--;
			py = py - 2 * rxSq;
			if (p > 0)
				p = p + rxSq - py;
			else {
				x++;
				px = px + 2 * rySq;
				p = p + rxSq - py + px;
			}
			drawEllipse(bitmap, xc, yc, x, y, color);

		}
	}

	private static void drawEllipse(int[][] bitmap, float xc, float yc, float x, float y, int color) {
		setPixel(bitmap, Math.round(xc + x), Math.round(yc + y), color);
		setPixel(bitmap, Math.round(xc - x), Math.round(yc + y), color);
		setPixel(bitmap, Math.round(xc + x), Math.round(yc - y), color);
		setPixel(bitmap, Math.round(xc - x), Math.round(yc - y), color);
	}
	
	/**
	 * Flood fill using recursion (safe) from point
	 * @param bitmap bitmap
	 * @param x start x
	 * @param y start y
	 * @param newColor fill color
	 */
	public static void floodFill(int[][] bitmap, int x, int y, int newColor) {
		
		PC_CoordI pos = new PC_CoordI(x, y);
		Stack<PC_CoordI> stack = new Stack<PC_CoordI>();
		int oldc = getPixel(bitmap, x, y);
		int newc = newColor;
		stack.push(pos);
		bitmap[pos.x][pos.y] = newc;
		long reccount = 0;
		while(!stack.empty()){
			pos = stack.pop();			
			PC_CoordI pos1;
			PC_CoordI offsets[] = {new PC_CoordI(-1,0),new PC_CoordI(1,0),new PC_CoordI(0,-1),new PC_CoordI(0,1)};
			
			for(PC_CoordI offset:offsets) {			
				pos1 = pos.offset(offset);			
				if (pos1.x >= 0 && pos1.x < width(bitmap) && pos1.y >= 0 && pos1.y < height(bitmap) && bitmap[pos1.x][pos1.y] == oldc
						&& bitmap[pos1.x][pos1.y] != newc) {
					bitmap[pos1.x][pos1.y] = newc;
					stack.push(pos1);
				}
			}
			reccount++;
			if(reccount > 1000000) break;
		}
	}

//	private static void floodfillbg(int[][] bitmap, int x, int y, int newColor, int oldColor) {
//		try {
//			if (x >= 0 && x < width(bitmap) && y >= 0 && y < height(bitmap) && getPixel(bitmap,x, y) == oldColor
//					&& getPixel(bitmap, x, y) != newColor) {
//				setPixel(bitmap, x, y, newColor); //set color before starting recursion
//
//				floodfillbg(bitmap, x + 1, y, newColor, oldColor);
//				floodfillbg(bitmap, x - 1, y, newColor, oldColor);
//				floodfillbg(bitmap, x, y + 1, newColor, oldColor);
//				floodfillbg(bitmap, x, y - 1, newColor, oldColor);
//			}
//		} catch (java.lang.StackOverflowError a) {}
//	}
	

	/**
	 * Draw image
	 * @param bitmap bitmap 
	 * @param image image bitmap
	 * @param x image left top x
	 * @param y image left top y
	 */
	public static void image(int[][] bitmap, int[][] image, int x, int y) {
		image(bitmap, image, x, y, -1, -1, -1, -1);
	}

	/**
	 * Draw image
	 * @param bitmap bitmap 
	 * @param image image bitmap
	 * @param x image left top x
	 * @param y image left top y
	 * @param xi clip left top x
	 * @param yi clip left top y
	 * @param w clip width
	 * @param h clip height
	 */
	public static void image(int[][] bitmap, int[][] image, int x, int y, int xi, int yi, int w, int h) {
		int x1 = x;
		int y1 = y;

		int startImageX = 0;
		int startImageY = 0;

		int sizeImgX, sizex, sizeImgY, sizey;
		sizeImgX = sizex = width(image);
		sizeImgY = sizey = height(image);

		if (xi != -1 && yi != -1 && w != -1 && h != -1) {
			startImageX = xi;
			startImageY = yi;
			sizex = w;
			sizey = h;
		}

		for (int j = Math.max(0, y1), jj = 0; j < Math.min(height(bitmap), y1 + sizey); j++, jj++) {
			for (int i = Math.max(0, x1), ii = 0; i < Math.min(width(bitmap), x1 + sizex); i++, ii++) {
				if (i >= 0 && i < width(bitmap) && j >= 0 && j < height(bitmap)) {
					if (startImageX + ii >= 0 && startImageY + jj >= 0) {
						if (startImageX + ii < sizeImgX && startImageY + jj < sizeImgY) {
							int color = getPixel(image, startImageX + ii, startImageY + jj);
							if (color != -1) bitmap[i][j] = color;
						}
					}
				}
			}
		}
	}
	
	/**
	 * rectangle
	 * @param bitmap bitmap
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param color
	 */
	public static void rect(int[][]bitmap, int x1, int y1, int x2, int y2, int color) {
		rect(bitmap, x1, y1, x2, y2, color, color, false);
	}
	
	/**
	 * frame rectangle
	 * @param bitmap bitmap
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param colorFrame
	 */
	public static void frame(int[][]bitmap, int x1, int y1, int x2, int y2, int colorFrame) {
		rect(bitmap, x1, y1, x2, y2, colorFrame, colorFrame, true);
	}

	/**
	 * rectangle with different frame color
	 * @param bitmap
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param colorFill
	 * @param colorFrame
	 */
	public static void rect(int[][]bitmap, int x1, int y1, int x2, int y2, int colorFill, int colorFrame) {
		rect(bitmap, x1, y1, x2, y2, colorFill, colorFrame, false);
	}
	
	private static void rect(int[][]bitmap, int x1, int y1, int x2, int y2, int colorFill, int colorFrame, boolean frameOnly) {

		if (x2 < x1) {
			int tmp = x1;
			x1 = x2;
			x2 = tmp;
		}
		if (y2 < y1) {
			int tmp = y1;
			y1 = y2;
			y2 = tmp;
		}
		
		if (!frameOnly) {
			for (int j = Math.max(0, y1); j <= Math.min(height(bitmap), y2); j++) {
				for (int i = Math.max(0, x1); i <= Math.min(width(bitmap), x2); i++) {
					if (i >= 0 && i < width(bitmap) && j >= 0 && j < height(bitmap)) {
						bitmap[i][j] = colorFill;
					}
				}
			}
		}

		// frame
		for (int i = x1; i <= x2; i++) {
			if (i >= 0 && i < width(bitmap) && y1 >= 0 && y1 < height(bitmap)) bitmap[i][y1] = colorFrame;
			if (i >= 0 && i < width(bitmap) && y2 >= 0 && y2 < height(bitmap)) bitmap[i][y2] = colorFrame;
		}

		for (int j = y1; j <= y2; j++) {
			if (x1 >= 0 && x1 < width(bitmap) && j >= 0 && j < height(bitmap)) bitmap[x1][j] = colorFrame;
			if (x2 >= 0 && x2 < width(bitmap) && j >= 0 && j < height(bitmap)) bitmap[x2][j] = colorFrame;
		}
	}
	
	/**
	 * a line
	 * @param bitmap bitmap
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param color color or -1
	 */
	public static void line(int[][] bitmap, int x1, int y1, int x2, int y2, int color) {
		
		if(x1 == x2 && y1 == y2) {
			if (x1 >= 0 && x1 < width(bitmap) && y1 >= 0 && y1 < height(bitmap)) bitmap[x1][y1] = color;
			return;
		}
		
		
		int tmp;

		if (x2 < x1) {
			tmp = x1;
			x1 = x2;
			x2 = tmp;
			tmp = y1;
			y1 = y2;
			y2 = tmp;
		}

		float f;
		int x, y, xd = x2 - x1, yd = y2 - y1;

		if (xd > Math.abs(yd)) {
			f = (float) yd / xd;
			for (int i = 0; i <= xd; i++) {
				x = x1 + i;
				y = (int) (y1 + f * i);
				if (x >= 0 && x < width(bitmap) && y >= 0 && y < height(bitmap)) bitmap[x][y] = color;
			}
		} else if (yd < 0) {
			f = (float) xd / yd;
			for (int j = 0; j >= yd; j--) {
				x = (int) (x1 + f * j);
				y = y1 + j;
				if (x >= 0 && x < width(bitmap) && y >= 0 && y < height(bitmap)) bitmap[x][y] = color;
			}
		} else {
			f = (float) xd / yd;
			for (int j = 0; j <= yd; j++) {
				x = (int) (x1 + f * j);
				y = y1 + j;
				if (x >= 0 && x < width(bitmap) && y >= 0 && y < height(bitmap)) bitmap[x][y] = color;
			}
		}

	}
	
	private static void drawChar(int[][] bitmap, ByteBuffer image, int imageWidth, int x, int y, char _char, int color) {
		int xT = (_char % 16) * 8;
		int yT = (_char / 16) * 8;
		int width = PC_Utils.mc().fontRenderer.getCharWidth(_char);

		int xp, yp;

		for (int j = 0; j < PC_Utils.mc().fontRenderer.FONT_HEIGHT; j++) {
			for (int i = 0; i < width; i++) {
				xp = i + x;
				yp = j + y;
				if (xp >= 0 && xp < width(bitmap) && yp >= 0 && yp < height(bitmap)) {
					if (image.get(((i + xT) + (j + yT) * imageWidth) * 4) != 0) bitmap[xp][yp] = color;
				}
			}
		}
	}

	/**
	 * Draw line of text
	 * @param bitmap
	 * @param x left top x
	 * @param y left top y
	 * @param s text string
	 * @param color color
	 */
	public static void text(int[][] bitmap, int x, int y, String s, int color) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mod_PCcore.fontRendererDefault.fontTextureName);
		int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
		int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
		ByteBuffer image = BufferUtils.createByteBuffer(width * height * 4);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			drawChar(bitmap, image, width, x, y, c, color);
			x += PC_Utils.mc().fontRenderer.getCharWidth(c);
		}
	}
	
	/**
	 * Interface for someone who wants to use weaselBitmapAdapter
	 * 
	 * @author MightyPork
	 */
	public static interface WeaselBitmapProvider{
		
		/**
		 * Try to locate image data on local network and return them; if there is no such image, return null.
		 * @param name image name
		 * @return image bitmap
		 */
		public int[][] getImageForName(String name);
		
		/**
		 * @return drawing canvas
		 */
		public int[][] getScreen();
		
		/**
		 * Called after the screen was changed. Used to store screen to itemstack's NBT tag.
		 * @param newScreen
		 */
		public void screenChanged(int[][] newScreen);
		
		/**
		 * Set screen data, resize etc.
		 * @param newdata new screen data
		 */
		public void setScreen(int[][] newdata);
	}
	
	
	public static class WeaselBitmapAdapter implements IWeaselHardware{
		private WeaselBitmapProvider provider;
		private String objName = "";
		
		private int[][] lastScreen = null;
		
		/**
		 * set object name. Used to create functions and variables for weasel network.
		 * @param str
		 */
		public void setObjName(String str) {
			objName = str;
		}
		
		/**
		 * Bitmap adapter
		 * @param provider bitmap provider
		 * @param objName name of the object
		 */
		public WeaselBitmapAdapter(WeaselBitmapProvider provider, String objName) {
			this.provider = provider;
			this.objName = objName;
		}

		private WeaselObject setGetPixel(String functionName, Object... args) {
			if (args.length != 3 && args.length != 2)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + args.length + ", needs 2 or 3)");
			int x = Calc.toInteger(args[0]);
			int y = Calc.toInteger(args[1]);
			if (x < 0 || x >= width(provider.getScreen()) || y < 0 || y >= height(provider.getScreen())) {
				//out of screen
				return new WeaselInteger(-1);
			}
			WeaselObject c = new WeaselInteger(provider.getScreen()[x][y]);
			if (args.length == 3) {
				//set
				int color = PC_Color.getHexColorForName(args[2]);
				(lastScreen=provider.getScreen())[x][y] = color;
			}
			return c;
		}

		private void setPixel(String functionName, Object... args) {
			if (args.length != 3)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + args.length + ", needs 3)");
			int x = Calc.toInteger(args[0]);
			int y = Calc.toInteger(args[1]);
			int color = PC_Color.getHexColorForName(args[2]);
			
			PC_BitmapUtils.setPixel((lastScreen=provider.getScreen()), x, y, color);
		}

		private WeaselObject getPixel(String functionName, Object... args) {
			if (args.length != 2)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + args.length + ", needs 2)");
			int x = Calc.toInteger(args[0]);
			int y = Calc.toInteger(args[1]);
			if (x < 0 || x >= width(provider.getScreen()) || y < 0 || y >= height(provider.getScreen())) return new WeaselInteger(-1);
			return new WeaselInteger(provider.getScreen()[x][y]);
		}

		private void drawImage(String functionName, Object... objects) {

			// name, left, top
			// name, left, top, startx, starty, width, height
			if (objects.length != 3 && objects.length != 7)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + objects.length + ", needs 5 or 6)");

			String name = Calc.toString(objects[0]);

			int[][] imageData = provider.getImageForName(name);

			if (imageData == null) {
				throw new WeaselRuntimeException("Error: Couldn't find image disk " + name + ".");
			}

			int x1 = Calc.toInteger(objects[1]);
			int y1 = Calc.toInteger(objects[2]);
			
			if (objects.length == 3) {
				PC_BitmapUtils.image((lastScreen=provider.getScreen()), imageData, x1, y1);
				return;
			}

			int startImageX = 0;
			int startImageY = 0;

			int sizex, sizey;

			startImageX = Calc.toInteger(objects[3]);
			startImageY = Calc.toInteger(objects[4]);

			sizex = Calc.toInteger(objects[5]);
			sizey = Calc.toInteger(objects[6]);

			PC_BitmapUtils.image((lastScreen=provider.getScreen()), imageData, x1, y1,startImageX,startImageY,sizex,sizey);
			
		}

		private void drawRect(boolean frameOnly, String functionName, Object... objects) {
			if (objects.length != 5 && objects.length != 6)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + objects.length + ", needs 5 or 6)");

			if (frameOnly && objects.length != 5)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + objects.length + ", needs 5 or 6)");

			int x1 = Calc.toInteger(objects[0]);
			int y1 = Calc.toInteger(objects[1]);
			int x2 = Calc.toInteger(objects[2]);
			int y2 = Calc.toInteger(objects[3]);
			
			int color, framecolor;
			
			if(frameOnly) {
				framecolor = PC_Color.getHexColorForName(objects[4]);
				PC_BitmapUtils.frame((lastScreen=provider.getScreen()), x1, y1, x2, y2, framecolor);
				return;			
			}
			
			if (objects.length == 5) {
				color = PC_Color.getHexColorForName(objects[4]);
				PC_BitmapUtils.rect((lastScreen=provider.getScreen()), x1, y1, x2, y2, color);
				return;
			}
			
			if (objects.length == 6) {
				color = PC_Color.getHexColorForName(objects[4]);
				framecolor = PC_Color.getHexColorForName(objects[5]);
				PC_BitmapUtils.rect((lastScreen=provider.getScreen()), x1, y1, x2, y2, color, framecolor);
				return;
			}
		}

		private void drawLine(String functionName, WeaselObject[] args) {
			if (args.length != 5)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + args.length + ", needs 5)");
			int x1 = Calc.toInteger(args[0]);
			int y1 = Calc.toInteger(args[1]);
			int x2 = Calc.toInteger(args[2]);
			int y2 = Calc.toInteger(args[3]);
			int color = PC_Color.getHexColorForName(args[4]);
			
			PC_BitmapUtils.line((lastScreen=provider.getScreen()), x1, y1, x2, y2, color);
		}

		private void drawString(String functionName, WeaselObject[] args) {
			if (args.length != 4)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + args.length + ", needs 4)");
			int x = Calc.toInteger(args[0]);
			int y = Calc.toInteger(args[1]);
			String s = Calc.toString(args[2]);
			int color = PC_Color.getHexColorForName(args[3]);

			PC_BitmapUtils.text((lastScreen=provider.getScreen()), x, y, s, color);
		}
		
		private void drawFill(String functionName, WeaselObject[] args) {
			if (args.length != 3)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + args.length + ", needs 3)");
			int x = Calc.toInteger(args[0]);
			int y = Calc.toInteger(args[1]);
			int color = PC_Color.getHexColorForName(args[2]);

			PC_BitmapUtils.floodFill((lastScreen=provider.getScreen()),x,y,color);			
		}
		
		private void drawEllipse(String functionName, WeaselObject[] args) {
			if (args.length != 5)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + args.length + ", needs 5)");
			int x = Calc.toInteger(args[0]);
			int y = Calc.toInteger(args[1]);
			int w = Calc.toInteger(args[2]);
			int h = Calc.toInteger(args[3]);
			int color = PC_Color.getHexColorForName(args[4]);
			
			PC_BitmapUtils.ellipse((lastScreen=provider.getScreen()),x,y,w,h,color);			
		}
		
		private void drawCircle(String functionName, WeaselObject[] args) {
			if (args.length != 4)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + args.length + ", needs 4)");
			int x = Calc.toInteger(args[0]);
			int y = Calc.toInteger(args[1]);
			int r = Calc.toInteger(args[2]);
			int color = PC_Color.getHexColorForName(args[3]);
			
			PC_BitmapUtils.ellipse((lastScreen=provider.getScreen()),x,y,r,r,color);			
		}

		@Override
		public boolean doesProvideFunction(String functionName) {
			return getProvidedFunctionNames().contains(functionName);
		}

		@Override
		public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
			if (functionName.equals(getName())) {
				return setGetPixel(functionName, (Object[]) args);
			} else if (functionName.equals(getName() + ".dot") || functionName.equals(getName() + ".point") || functionName.equals(getName() + ".set")
					|| functionName.equals(getName() + ".draw.pixel")) {
				// name.dot(x,y,color);
				setPixel(functionName, (Object[]) args);
				provider.screenChanged(lastScreen);
				return null;
			} else if (functionName.equals(getName() + ".get")) {
				// name.get(x,y);
				return getPixel(functionName, (Object[]) args);
			} else if (functionName.equals(getName() + ".rect") || functionName.equals(getName() + ".draw.rect")) {
				// name.rect(x,y,x2,y2,color);
				// name.rect(x,y,x2,y2,color,frame);
				drawRect(false, functionName, (Object[]) args);
				provider.screenChanged(lastScreen);
				return null;
			} else if (functionName.equals(getName() + ".frame") || functionName.equals(getName() + ".draw.frame")) {
				// name.frame(x,y,x2,y2,frameColor);
				drawRect(true, functionName, (Object[]) args);
				provider.screenChanged(lastScreen);
				return null;
			} else if (functionName.equals(getName() + ".line") || functionName.equals(getName() + ".draw.line")) {
				// name.line(x,y,x2,y2,color);
				drawLine(functionName, args);
				provider.screenChanged(lastScreen);
				return null;
			} else if (functionName.equals(getName() + ".string") || functionName.equals(getName() + ".draw.string")) {
				// name.string(x,y,text,color);
				drawString(functionName, args);
				provider.screenChanged(lastScreen);
				return null;
			} else if (functionName.equals(getName() + ".fill") || functionName.equals(getName() + ".draw.fill")) {
				// name.string(x,y,text,color);
				drawFill(functionName, args);
				provider.screenChanged(lastScreen);
				return null;
			} else if (functionName.equals(getName() + ".ellipse") || functionName.equals(getName() + ".draw.ellipse")) {
				// name.string(x,y,text,color);
				drawEllipse(functionName, args);
				provider.screenChanged(lastScreen);
				return null;
			} else if (functionName.equals(getName() + ".circle") || functionName.equals(getName() + ".draw.circle")) {
				// name.string(x,y,text,color);
				drawCircle(functionName, args);
				provider.screenChanged(lastScreen);
				return null;
			} else if (functionName.equals(getName() + ".image") || functionName.equals(getName() + ".draw.image")) {
				// name.image(imgName, x,y);
				// name.image(imgName, x,y, imageX, imageY, width, height);
				drawImage(functionName, (Object[]) args);
				provider.screenChanged(lastScreen);
				return null;
			}  else if (functionName.equals(getName() + ".resize")) {
				try {
					int w = Calc.toInteger(args[0]);
					int h = Calc.toInteger(args[1]);
					if(w>260 || h > 130 || w<1 || h<1) {
						throw new WeaselRuntimeException("resize: "+w+"x"+h+" is not a valid image size (max 260x130).");						
					}
					int[][] newdata = new int[w][h];
					for (int x = 0; x < newdata.length; x++) {
						Arrays.fill(newdata[x], -1);
					}
					
					provider.setScreen(newdata);

					return null;
				}catch(Exception e) {
					throw new WeaselRuntimeException(e.getMessage());
				}
			}  else if (functionName.equals(getName() + ".clear")) {
				try {
					int[][] data = provider.getScreen();
					
					rect(data, 0, 0, width(data)-1, height(data)-1, -1);
					
					provider.setScreen(data);

					return null;
				}catch(Exception e) {
					throw new WeaselRuntimeException(e.getMessage());
				}
			}
			
			throw new IllegalAccessError();
		}

		@Override
		public WeaselObject getVariable(String name) {
			if (name.equals(getName() + ".W") || name.equals(getName() + ".w") || name.equalsIgnoreCase(getName() + ".width")) {
				return new WeaselInteger(width(provider.getScreen()));
			} else if (name.equals(getName() + ".H") || name.equals(getName() + ".h") || name.equalsIgnoreCase(getName() + ".height")) {
				return new WeaselInteger(height(provider.getScreen()));
			}
			if (name.equals(getName())) return new WeaselNull();
			return null;
		}

		@Override
		public void setVariable(String name, Object object) {
			if (name.equals(getName())) {
				int clr = PC_Color.getHexColorForName(object);
				PC_BitmapUtils.rect((lastScreen=provider.getScreen()), 0, 0, width(provider.getScreen()), height(provider.getScreen()), clr);
				provider.screenChanged(lastScreen);
			}
		}

		@Override
		public List<String> getProvidedFunctionNames() {
			ArrayList<String> list = new ArrayList<String>(1);
			list.add(getName());
			list.add(getName() + ".set");
			list.add(getName() + ".get");
			list.add(getName() + ".draw.pixel");
			list.add(getName() + ".draw.rect");
			list.add(getName() + ".draw.frame");
			list.add(getName() + ".draw.line");
			list.add(getName() + ".draw.string");
			list.add(getName() + ".draw.image");
			list.add(getName() + ".draw.fill");
			list.add(getName() + ".draw.ellipse");
			list.add(getName() + ".draw.circle");
			list.add(getName() + ".pixel");
			list.add(getName() + ".point");
			list.add(getName() + ".rect");
			list.add(getName() + ".frame");
			list.add(getName() + ".line");
			list.add(getName() + ".string");
			list.add(getName() + ".fill");
			list.add(getName() + ".image");
			list.add(getName() + ".ellipse");
			list.add(getName() + ".circle");
			list.add(getName() + ".resize");
			list.add(getName() + ".clear");
			return list;
		}

		@Override
		public List<String> getProvidedVariableNames() {
			List<String> list = new ArrayList<String>(1);
			list.add(getName() + ".WIDTH");
			list.add(getName() + ".HEIGHT");
			list.add(getName() + ".W");
			list.add(getName() + ".H");
			list.add(getName() + ".w");
			list.add(getName() + ".h");
			list.add(getName() + ".width");
			list.add(getName() + ".height");
			list.add(getName());
			return list;
		}
		
		private String getName() {
			return objName;
		}
		
	}

}
