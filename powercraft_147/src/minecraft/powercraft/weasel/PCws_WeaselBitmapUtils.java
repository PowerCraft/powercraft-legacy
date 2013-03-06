package powercraft.weasel;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import powercraft.management.PC_Color;
import powercraft.management.PC_VecI;
import powercraft.management.renderer.PC_Renderer;
import powercraft.weasel.engine.Calc;
import powercraft.weasel.engine.WeaselEngine;
import powercraft.weasel.engine.WeaselFunctionManager;
import powercraft.weasel.exception.WeaselRuntimeException;
import powercraft.weasel.obj.WeaselDouble;
import powercraft.weasel.obj.WeaselObject;

/**
 * Class for bitmap manipulation
 * 
 * @author MightyPork, Darkhog, XOR19
 *
 */
public class PCws_WeaselBitmapUtils {
	private static final int INV = -1;

	/**
	 * @param bitmap
	 * @return width of the bitmap
	 */
	public static int width(WeaselBitmapProvider bitmap) {
		return bitmap.getBitmapSize().x;
	}

	/**
	 * @param bitmap
	 * @return height of the bitmap
	 */
	public static int height(WeaselBitmapProvider bitmap) {
		return bitmap.getBitmapSize().y;
	}

	/**
	 * set bitmap pixel
	 * @param bitmap bitmap
	 * @param x x
	 * @param y y
	 * @param color color rgb or -1
	 */
	public static void setPixel(WeaselBitmapProvider bitmap, int x, int y, int color) {
		if (x < 0 || x >= width(bitmap) || y < 0 || y >= height(bitmap)) return;
		bitmap.setBitmapPixel(x, y, color);
	}

	/**
	 * Get bitmap pixel
	 * @param bitmap bitmap
	 * @param x x
	 * @param y y
	 * @return color rgb or -1
	 */
	public static int getPixel(WeaselBitmapProvider bitmap, int x, int y) {
		if (x < 0 || x >= width(bitmap) || y < 0 || y >= height(bitmap)) return INV;
		return bitmap.getBitmapPixel(x, y);
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
	public static void ellipse(WeaselBitmapProvider bitmap, int x1, int y1, int w, int h, int color) {
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

	private static void drawEllipse(WeaselBitmapProvider bitmap, float xc, float yc, float x, float y, int color) {
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
	public static void floodFill(WeaselBitmapProvider bitmap, int x, int y, int newColor) {
		
		PC_VecI pos = new PC_VecI(x, y);
		Stack<PC_VecI> stack = new Stack<PC_VecI>();
		int oldc = getPixel(bitmap, x, y);
		int newc = newColor;
		stack.push(pos);
		bitmap.setBitmapPixel(pos.x, pos.y, newc);
		long reccount = 0;
		while(!stack.empty()){
			pos = stack.pop();			
			PC_VecI pos1;
			PC_VecI offsets[] = {new PC_VecI(-1,0),new PC_VecI(1,0),new PC_VecI(0,-1),new PC_VecI(0,1)};
			
			for(PC_VecI offset:offsets) {			
				pos1 = pos.offset(offset);			
				if (pos1.x >= 0 && pos1.x < width(bitmap) && pos1.y >= 0 && pos1.y < height(bitmap) && 
						bitmap.getBitmapPixel(pos1.x, pos1.y) == oldc && bitmap.getBitmapPixel(pos1.x, pos1.y) != newc) {
					bitmap.setBitmapPixel(pos1.x, pos1.y, newc);
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
	public static void image(WeaselBitmapProvider bitmap, WeaselBitmapProvider image, int x, int y) {
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
	public static void image(WeaselBitmapProvider bitmap, WeaselBitmapProvider image, int x, int y, int xi, int yi, int w, int h) {
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
							if (color != -1) bitmap.setBitmapPixel(i, j, color);
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
	public static void rect(WeaselBitmapProvider bitmap, int x1, int y1, int x2, int y2, int color) {
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
	public static void frame(WeaselBitmapProvider bitmap, int x1, int y1, int x2, int y2, int colorFrame) {
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
	public static void rect(WeaselBitmapProvider bitmap, int x1, int y1, int x2, int y2, int colorFill, int colorFrame) {
		rect(bitmap, x1, y1, x2, y2, colorFill, colorFrame, false);
	}
	
	private static void rect(WeaselBitmapProvider bitmap, int x1, int y1, int x2, int y2, int colorFill, int colorFrame, boolean frameOnly) {

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
						bitmap.setBitmapPixel(i, j, colorFill);
					}
				}
			}
		}

		// frame
		for (int i = x1; i <= x2; i++) {
			if (i >= 0 && i < width(bitmap) && y1 >= 0 && y1 < height(bitmap)) 
				bitmap.setBitmapPixel(i, y1, colorFrame);
			if (i >= 0 && i < width(bitmap) && y2 >= 0 && y2 < height(bitmap)) 
				bitmap.setBitmapPixel(i, y2, colorFrame);
		}

		for (int j = y1; j <= y2; j++) {
			if (x1 >= 0 && x1 < width(bitmap) && j >= 0 && j < height(bitmap)) 
				bitmap.setBitmapPixel(x1, j, colorFrame);
			if (x2 >= 0 && x2 < width(bitmap) && j >= 0 && j < height(bitmap)) 
				bitmap.setBitmapPixel(x2, j, colorFrame);
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
	public static void line(WeaselBitmapProvider bitmap, int x1, int y1, int x2, int y2, int color) {
		
		if(x1 == x2 && y1 == y2) {
			if (x1 >= 0 && x1 < width(bitmap) && y1 >= 0 && y1 < height(bitmap)) 
				bitmap.setBitmapPixel(x1, y1, color);
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
				if (x >= 0 && x < width(bitmap) && y >= 0 && y < height(bitmap)) 
					bitmap.setBitmapPixel(x, y, color);
			}
		} else if (yd < 0) {
			f = (float) xd / yd;
			for (int j = 0; j >= yd; j--) {
				x = (int) (x1 + f * j);
				y = y1 + j;
				if (x >= 0 && x < width(bitmap) && y >= 0 && y < height(bitmap)) 
					bitmap.setBitmapPixel(x, y, color);
			}
		} else {
			f = (float) xd / yd;
			for (int j = 0; j <= yd; j++) {
				x = (int) (x1 + f * j);
				y = y1 + j;
				if (x >= 0 && x < width(bitmap) && y >= 0 && y < height(bitmap)) 
					bitmap.setBitmapPixel(x, y, color);
			}
		}

	}
	
	private static void drawChar(WeaselBitmapProvider bitmap, ByteBuffer image, int imageWidth, int x, int y, char _char, int color) {
		int xT = (_char % 16) * 8;
		int yT = (_char / 16) * 8;
		int width = PC_Renderer.getFontRenderer().getCharWidth(_char);

		int xp, yp;

		for (int j = 0; j < PC_Renderer.getFontRenderer().FONT_HEIGHT; j++) {
			for (int i = 0; i < width; i++) {
				xp = i + x;
				yp = j + y;
				if (xp >= 0 && xp < width(bitmap) && yp >= 0 && yp < height(bitmap)) {
					if (image.get(((i + xT) + (j + yT) * imageWidth) * 4) != 0) 
						bitmap.setBitmapPixel(xp, yp, color);
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
	public static void text(WeaselBitmapProvider bitmap, int x, int y, String s, int color) {
		/*GL11.glBindTexture(GL11.GL_TEXTURE_2D, mod_PCcore.fontRendererDefault.fontTextureName);
		int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
		int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
		ByteBuffer image = BufferUtils.createByteBuffer(width * height * 4);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			drawChar(bitmap, image, width, x, y, c, color);
			x += PC_Utils.mc().fontRenderer.getCharWidth(c);
		}*/
	}
	
	public static int getColor(Object obj){
		if(obj instanceof WeaselDouble || obj instanceof Double || obj instanceof Integer){
			return Calc.toInteger(obj);
		}else{
			return PC_Color.getHexColorForName(Calc.toString(obj));
		}
	}
	
	/**
	 * Interface for someone who wants to use weaselBitmapAdapter
	 * 
	 * @author MightyPork
	 */
	public static interface WeaselBitmapProvider{
		
		public PC_VecI getBitmapSize();
		public int getBitmapPixel(int x, int y);
		public void setBitmapPixel(int x, int y, int color);
		public void resize(int w, int h);
		public void notifyChanges();
		public WeaselBitmapProvider getImageForName(String name);
		
	}
	
	public static class WeaselBitmapAdapter extends WeaselFunctionManager{
		private WeaselBitmapProvider provider;
		
		/**
		 * Bitmap adapter
		 * @param provider bitmap provider
		 * @param objName name of the object
		 */
		public WeaselBitmapAdapter(WeaselBitmapProvider provider) {
			this.provider = provider;
		}

		private WeaselObject setGetPixel(String functionName, Object... args) {
			if (args.length != 3 && args.length != 2)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + args.length + ", needs 2 or 3)");
			int x = Calc.toInteger(args[0]);
			int y = Calc.toInteger(args[1]);
			if (x < 0 || x >= provider.getBitmapSize().x || y < 0 || y >= provider.getBitmapSize().y) {
				//out of screen
				return new WeaselDouble(-1);
			}
			WeaselObject c = new WeaselDouble(provider.getBitmapPixel(x, y));
			if (args.length == 3) {
				//set
				int color = getColor(args[2]);
				provider.setBitmapPixel(x, y, color);
			}
			return c;
		}

		private void setPixel(String functionName, Object... args) {
			if (args.length != 3)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + args.length + ", needs 3)");
			int x = Calc.toInteger(args[0]);
			int y = Calc.toInteger(args[1]);
			int color = getColor(args[2]);
			
			PCws_WeaselBitmapUtils.setPixel(provider, x, y, color);
			provider.notifyChanges();
		}

		private WeaselObject getPixel(String functionName, Object... args) {
			if (args.length != 2)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + args.length + ", needs 2)");
			int x = Calc.toInteger(args[0]);
			int y = Calc.toInteger(args[1]);
			if (x < 0 || x >= width(provider) || y < 0 || y >= height(provider)) return new WeaselDouble(-1);
			return new WeaselDouble(provider.getBitmapPixel(x, y));
		}

		private void drawImage(String functionName, Object... objects) {

			// name, left, top
			// name, left, top, startx, starty, width, height
			if (objects.length != 3 && objects.length != 7)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + objects.length + ", needs 5 or 6)");

			String name = Calc.toString(objects[0]);

			WeaselBitmapProvider imageData = provider.getImageForName(name);

			if (imageData == null) {
				throw new WeaselRuntimeException("Error: Couldn't find image disk " + name + ".");
			}

			int x1 = Calc.toInteger(objects[1]);
			int y1 = Calc.toInteger(objects[2]);
			
			if (objects.length == 3) {
				PCws_WeaselBitmapUtils.image(provider, imageData, x1, y1);
				return;
			}

			int startImageX = 0;
			int startImageY = 0;

			int sizex, sizey;

			startImageX = Calc.toInteger(objects[3]);
			startImageY = Calc.toInteger(objects[4]);

			sizex = Calc.toInteger(objects[5]);
			sizey = Calc.toInteger(objects[6]);

			PCws_WeaselBitmapUtils.image(provider, imageData, x1, y1,startImageX,startImageY,sizex,sizey);
			provider.notifyChanges();
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
				framecolor = PC_Color.getHexColorForName(Calc.toString(objects[4]));
				PCws_WeaselBitmapUtils.frame(provider, x1, y1, x2, y2, framecolor);
				provider.notify();
				return;			
			}
			
			if (objects.length == 5) {
				color = PC_Color.getHexColorForName(Calc.toString(objects[4]));
				PCws_WeaselBitmapUtils.rect(provider, x1, y1, x2, y2, color);
				provider.notify();
				return;
			}
			
			if (objects.length == 6) {
				color = PC_Color.getHexColorForName(Calc.toString(objects[4]));
				framecolor = PC_Color.getHexColorForName(Calc.toString(objects[5]));
				PCws_WeaselBitmapUtils.rect(provider, x1, y1, x2, y2, color, framecolor);
				provider.notify();
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
			int color = PC_Color.getHexColorForName(Calc.toString(args[4]));
			
			PCws_WeaselBitmapUtils.line(provider, x1, y1, x2, y2, color);
			provider.notify();
		}

		private void drawString(String functionName, WeaselObject[] args) {
			if (args.length != 4)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + args.length + ", needs 4)");
			int x = Calc.toInteger(args[0]);
			int y = Calc.toInteger(args[1]);
			String s = Calc.toString(args[2]);
			int color = PC_Color.getHexColorForName(Calc.toString(args[3]));

			PCws_WeaselBitmapUtils.text(provider, x, y, s, color);
			provider.notify();
		}
		
		private void drawFill(String functionName, WeaselObject[] args) {
			if (args.length != 3)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + args.length + ", needs 3)");
			int x = Calc.toInteger(args[0]);
			int y = Calc.toInteger(args[1]);
			int color = PC_Color.getHexColorForName(Calc.toString(args[2]));

			PCws_WeaselBitmapUtils.floodFill(provider,x,y,color);			
			provider.notify();
		}
		
		private void drawEllipse(String functionName, WeaselObject[] args) {
			if (args.length != 5)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + args.length + ", needs 5)");
			int x = Calc.toInteger(args[0]);
			int y = Calc.toInteger(args[1]);
			int w = Calc.toInteger(args[2]);
			int h = Calc.toInteger(args[3]);
			int color = PC_Color.getHexColorForName(Calc.toString(args[4]));
			
			PCws_WeaselBitmapUtils.ellipse(provider,x,y,w,h,color);		
			provider.notify();
		}
		
		private void drawCircle(String functionName, WeaselObject[] args) {
			if (args.length != 4)
				throw new WeaselRuntimeException("Wrong argument count for function " + functionName + " (" + args.length + ", needs 4)");
			int x = Calc.toInteger(args[0]);
			int y = Calc.toInteger(args[1]);
			int r = Calc.toInteger(args[2]);
			int color = PC_Color.getHexColorForName(Calc.toString(args[3]));
			
			PCws_WeaselBitmapUtils.ellipse(provider,x,y,r,r,color);		
			provider.notify();
		}

		@Override
		public boolean doesProvideFunction(String functionName) {
			return getProvidedFunctionNames().contains(functionName);
		}

		@Override
		public WeaselObject call(WeaselEngine engine, String functionName, boolean var, WeaselObject... args) {
			if (functionName.equals("dot") || functionName.equals("point") || functionName.equals("set")
					|| functionName.equals("draw.pixel")) {
				// name.dot(x,y,color);
				setPixel(functionName, (Object[]) args);
				
				return null;
			} else if (functionName.equals("get")) {
				// name.get(x,y);
				return getPixel(functionName, (Object[]) args);
			} else if (functionName.equals("rect") || functionName.equals("draw.rect")) {
				// name.rect(x,y,x2,y2,color);
				// name.rect(x,y,x2,y2,color,frame);
				drawRect(false, functionName, (Object[]) args);
				return null;
			} else if (functionName.equals("frame") || functionName.equals("draw.frame")) {
				// name.frame(x,y,x2,y2,frameColor);
				drawRect(true, functionName, (Object[]) args);
				return null;
			} else if (functionName.equals("line") || functionName.equals("draw.line")) {
				// name.line(x,y,x2,y2,color);
				drawLine(functionName, args);
				return null;
			} else if (functionName.equals("string") || functionName.equals("draw.string")) {
				// name.string(x,y,text,color);
				drawString(functionName, args);
				return null;
			} else if (functionName.equals("fill") || functionName.equals("draw.fill")) {
				// name.string(x,y,text,color);
				drawFill(functionName, args);
				return null;
			} else if (functionName.equals("ellipse") || functionName.equals("draw.ellipse")) {
				// name.string(x,y,text,color);
				drawEllipse(functionName, args);
				return null;
			} else if (functionName.equals("circle") || functionName.equals("draw.circle")) {
				// name.string(x,y,text,color);
				drawCircle(functionName, args);
				return null;
			} else if (functionName.equals("image") || functionName.equals("draw.image")) {
				// name.image(imgName, x,y);
				// name.image(imgName, x,y, imageX, imageY, width, height);
				drawImage(functionName, (Object[]) args);
				return null;
			}  else if (functionName.equals("resize")) {
				try {
					int w = Calc.toInteger(args[0]);
					int h = Calc.toInteger(args[1]);
					if(w>260 || h > 130 || w<1 || h<1) {
						throw new WeaselRuntimeException("resize: "+w+"x"+h+" is not a valid image size (max 260x130).");						
					}
					
					provider.resize(w, h);
					provider.notify();
					return null;
				}catch(Exception e) {
					throw new WeaselRuntimeException(e.getMessage());
				}
			}  else if (functionName.equals("clear")) {
				try {
					
					rect(provider, 0, 0, width(provider)-1, height(provider)-1, -1);
					provider.notify();
					return null;
				}catch(Exception e) {
					throw new WeaselRuntimeException(e.getMessage());
				}
			} else if (functionName.equals("W") || functionName.equals("w") || functionName.equalsIgnoreCase("width")) {
				return new WeaselDouble(width(provider));
			} else if (functionName.equals("H") || functionName.equals("h") || functionName.equalsIgnoreCase("height")) {
				return new WeaselDouble(height(provider));
			}
			
			throw new IllegalAccessError();
		}

		@Override
		public List<String> getProvidedFunctionNames() {
			ArrayList<String> list = new ArrayList<String>(1);
			list.add("set");
			list.add("get");
			list.add("draw.pixel");
			list.add("draw.rect");
			list.add("draw.frame");
			list.add("draw.line");
			list.add("draw.string");
			list.add("draw.image");
			list.add("draw.fill");
			list.add("draw.ellipse");
			list.add("draw.circle");
			list.add("pixel");
			list.add("point");
			list.add("rect");
			list.add("frame");
			list.add("line");
			list.add("string");
			list.add("fill");
			list.add("image");
			list.add("ellipse");
			list.add("circle");
			list.add("resize");
			list.add("clear");
			return list;
		}

		@Override
		public List<String> getProvidedVariableNames() {
			List<String> list = new ArrayList<String>(1);
			list.add("WIDTH");
			list.add("HEIGHT");
			list.add("W");
			list.add("H");
			list.add("w");
			list.add("h");
			list.add("width");
			list.add("height");
			return list;
		}
		
	}
}
