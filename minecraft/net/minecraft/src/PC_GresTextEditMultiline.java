package net.minecraft.src;


import java.util.ArrayList;

import org.lwjgl.input.Keyboard;


/**
 * Multiline text edit
 * 
 * @author XOR19
 */
public class PC_GresTextEditMultiline extends PC_GresWidget {

	private static final char br = '\n';

	/**
	 * Keyword colouring settings
	 * 
	 * @author XOR19
	 */
	public static class Keyword {
		/**
		 * Keyword coloring of plain word
		 * 
		 * @param word keyword string
		 * @param color color
		 */
		public Keyword(String word, int color) {
			this.word = word;
			this.color = color;
		}

		/**
		 * keyword coloring
		 * 
		 * @param word the word string or regexp pattern
		 * @param color color
		 * @param regexp is string a regexp pattern?
		 */
		public Keyword(String word, int color, boolean regexp) {
			this.word = word;
			this.color = color;
			this.isRegexp = regexp;
		}

		/**
		 * keyword coloring
		 * 
		 * @param word the word string or regexp pattern
		 * @param color color
		 * @param regexp is string a regexp pattern?
		 * @param nextWordColor color 
		 */
		public Keyword(String word, int color, boolean regexp, int nextWordKeywordColor) {
			this.word = word;
			this.color = color;
			this.isRegexp = regexp;
			this.nextWordKeywordColor = nextWordKeywordColor;
		}
		
		/**
		 * keyword coloring - sequence
		 * 
		 * @param start the string the sequence starts with
		 * @param end the end of the sequence
		 * @param color color
		 * @param regexp is string a regexp pattern?
		 */
		public Keyword(String start, String end, int color, boolean regexp) {
			this.word = start;
			this.end = end;
			this.color = color;
			this.isRegexp = regexp;
		}

		/** the keyword */
		public String word;
		/** the end */
		public String end;
		/** rgb color */
		public int color;
		
		public int nextWordKeywordColor;
		/** flag that this word uses a regular expresison for matching. */
		public boolean isRegexp;
	}

	public static class StringAdd{
		public StringAdd(String addString, boolean jumpToEnd){
			this.addString = addString;
			this.jumpToEnd = jumpToEnd;
		}
		
		public String addString;
		public boolean jumpToEnd;
	}
	
	public static interface AutoAdd{
		
		public StringAdd charAdd(PC_GresTextEditMultiline te, char c, String textBevore, String textBehind);
		
	}
	
	private PC_CoordI lastMousePosition = new PC_CoordI(0, 0);
	private PC_CoordI mouseSelectStart = new PC_CoordI(0, 0);
	private PC_CoordI mouseSelectEnd = new PC_CoordI(0, 0);
	private int mousePressed = 0;
	private PC_CoordI scroll = new PC_CoordI(0, 0);
	private ArrayList<Keyword> keyWords = null;
	private ArrayList<Keyword> oneFrameKeyWords = new ArrayList<Keyword>();
	private ArrayList<Keyword> newOneFrameKeyWords;
	private AutoAdd autoAdd = null;
	private int vScrollPos = 0, vScrollSize = 0, hScrollPos = 0, hScrollSize = 0;
	private Keyword keywordToFinish = null;
	private int nextWordKeywordColor = 0;
	
	/**
	 * Multi-row text edit
	 * 
	 * @param text initial text
	 * @param minWidth width
	 * @param minHeight height
	 */
	public PC_GresTextEditMultiline(String text, int minWidth, int minHeight) {
		super(minWidth > 20 ? minWidth : 20, minHeight > getFR().FONT_HEIGHT + 26 ? minHeight : getFR().FONT_HEIGHT + 26, text);
		canAddWidget = false;
		color[textColorEnabled] = 0xff000000 | PC_GresHighlightHelper.colorDefault;
		color[textColorShadowEnabled] = 0; //0xff383838;
		color[textColorClicked] = 0xff000000 | PC_GresHighlightHelper.colorDefault;
		color[textColorHover] = 0xff000000 | PC_GresHighlightHelper.colorDefault;
		color[textColorDisabled] = 0xff000000 | PC_GresHighlightHelper.colorDefault;
		color[textColorShadowDisabled] = 0; //0xff383838;
	}

	/**
	 * Multi-row text edit
	 * 
	 * @param text initial text
	 * @param minWidth width
	 * @param minHeight height
	 * @param keyWords list of keywords
	 */
	public PC_GresTextEditMultiline(String text, int minWidth, int minHeight, ArrayList<Keyword> keyWords) {
		super(minWidth > 20 ? minWidth : 20, minHeight > getFR().FONT_HEIGHT + 26 ? minHeight : getFR().FONT_HEIGHT + 26, text);
		canAddWidget = false;
		color[textColorEnabled] = 0xff000000 | PC_GresHighlightHelper.colorDefault;
		color[textColorShadowEnabled] = 0; //0xff383838;
		color[textColorClicked] = 0xff000000 | PC_GresHighlightHelper.colorDefault;
		color[textColorHover] = 0xff000000 | PC_GresHighlightHelper.colorDefault;
		color[textColorDisabled] = 0xff000000 | PC_GresHighlightHelper.colorDefault;
		color[textColorShadowDisabled] = 0; //0xff383838;
		this.keyWords = keyWords;
	}

	/**
	 * Multi-row text edit
	 * 
	 * @param text initial text
	 * @param minWidth width
	 * @param minHeight height
	 * @param keyWords list of keywords
	 * @param autoAdd autoAdd function
	 */
	public PC_GresTextEditMultiline(String text, int minWidth, int minHeight, ArrayList<Keyword> keyWords, AutoAdd autoAdd) {
		super(minWidth > 20 ? minWidth : 20, minHeight > getFR().FONT_HEIGHT + 26 ? minHeight : getFR().FONT_HEIGHT + 26, text);
		canAddWidget = false;
		color[textColorEnabled] = 0xff000000 | PC_GresHighlightHelper.colorDefault;
		color[textColorShadowEnabled] = 0; //0xff383838;
		color[textColorClicked] = 0xff000000 | PC_GresHighlightHelper.colorDefault;
		color[textColorHover] = 0xff000000 | PC_GresHighlightHelper.colorDefault;
		color[textColorDisabled] = 0xff000000 | PC_GresHighlightHelper.colorDefault;
		color[textColorShadowDisabled] = 0; //0xff383838;
		this.keyWords = keyWords;
		this.autoAdd = autoAdd;
	}
	
	@Override
	public PC_CoordI getMinSize() {
		return calcSize();
	}

	@Override
	public PC_CoordI calcSize() {
		return size;
	}

	@Override
	public void calcChildPositions() {}

	private int getMaxLineLength() {
		int maxLength = 0, length = 0;
		for (int i = 0; i <= getLineNumbers(); i++) {
			length = getStringWidth(getLine(i));
			if (length > maxLength) {
				maxLength = length;
			}
		}
		return maxLength;
	}

	private void calcScrollPosition() {

		int sizeX = size.x - 12;
		int maxSizeX = getMaxLineLength();
		int sizeOutOfFrame = maxSizeX - sizeX + 14;
		if (sizeOutOfFrame < 0) {
			sizeOutOfFrame = 0;
		}
		float prozent = maxSizeX > 0 ? ((float) sizeOutOfFrame / maxSizeX) : 0;
		hScrollPos = (int) ((sizeOutOfFrame > 0 ? (float) scroll.x / sizeOutOfFrame : 0) * prozent * sizeX + 0.5);
		hScrollSize = (int) ((1 - prozent) * sizeX + 0.5);

		int lineNumbers = getLineNumbers() + 1;
		int linesNotToSee = lineNumbers - shownLines();
		if (linesNotToSee < 0) {
			linesNotToSee = 0;
		}

		prozent = lineNumbers > 0 ? ((float) linesNotToSee / lineNumbers) : 0;
		int sizeY = size.y - 12;
		vScrollPos = (int) ((linesNotToSee > 0 ? (float) scroll.y / linesNotToSee : 0) * prozent * sizeY + 0.5);
		vScrollSize = (int) ((1 - prozent) * sizeY + 0.5);
		updateScrollPosition();
	}

	private void updateScrollPosition() {

		int sizeX = size.x - 12;
		int maxSizeX = getMaxLineLength();
		int sizeOutOfFrame = maxSizeX - sizeX + 14;
		if (sizeOutOfFrame < 0) {
			sizeOutOfFrame = 0;
		}
		float prozent = maxSizeX > 0 ? ((float) sizeOutOfFrame / (maxSizeX)) : 0;
		if (hScrollPos < 0) {
			hScrollPos = 0;
		}
		if (hScrollPos > sizeX - hScrollSize) {
			hScrollPos = sizeX - hScrollSize;
		}
		scroll.x = (int) (hScrollPos / prozent / sizeX * sizeOutOfFrame + 0.5);

		int sizeY = size.y - 12;
		int lineNumbers = getLineNumbers() + 1;
		int linesNotToSee = lineNumbers - shownLines();
		if (linesNotToSee < 0) {
			linesNotToSee = 0;
		}

		prozent = lineNumbers > 0 ? ((float) linesNotToSee / (lineNumbers)) : 0;
		if (vScrollPos < 0) {
			vScrollPos = 0;
		}
		if (vScrollPos > sizeY - vScrollSize) {
			vScrollPos = sizeY - vScrollSize;
		}
		scroll.y = (int) (vScrollPos / prozent / sizeY * linesNotToSee + 0.5);
	}

	private int getColorForKeyword(Keyword kw) {
		if (kw == null){
			nextWordKeywordColor = 0;
			return 0xff000000 | PC_GresHighlightHelper.colorDefault;
		}
		nextWordKeywordColor = kw.nextWordKeywordColor;
		return kw.color;
	}
	
	private int getColorForWord(String word) {
		Keyword kw = getKeywordForWord(word, true);
		if(nextWordKeywordColor!=0){
			Keyword k = new Keyword(word, nextWordKeywordColor);
			if(kw==null)
				kw = k;
			newOneFrameKeyWords.add(k);
		}
		return getColorForKeyword(kw);
		
	}

	private Keyword getKeywordForWord(String word, boolean giveKeyword) {
		if (keywordToFinish == null) {
			if (keyWords != null) {
				for (Keyword k : keyWords) {
					if (!k.isRegexp && k.word.equals(word)) {
						if (k.end != null) keywordToFinish = k;
						return k;
					}
					if (k.isRegexp && word.matches(k.word)) {
						if (k.end != null) keywordToFinish = k;
						return k;
					}
				}
				for (Keyword k : oneFrameKeyWords) {
					if (!k.isRegexp && k.word.equals(word))return k;
					if (k.isRegexp && word.matches(k.word))return k;
				}
			}
		} else {
			Keyword kw = keywordToFinish;
			if (!keywordToFinish.isRegexp && keywordToFinish.end.equals(word))
				keywordToFinish = null;
			else if (keywordToFinish.isRegexp && word.matches(keywordToFinish.end)) keywordToFinish = null;
			if (giveKeyword) return kw;
			if (keywordToFinish == null) return kw;
		}
		return null;
	}

	private boolean yCoordsInDrawRect(int cy) {
		return cy >= scroll.y && cy < scroll.y + shownLines();
	}

	private boolean coordsInDrawRect(PC_CoordI c) {
		if (!yCoordsInDrawRect(c.y)) {
			return false;
		}
		int cx = getStringWidth(getLine(c.y).substring(0, c.x));
		return cx >= scroll.x && cx < scroll.x + size.x - 26;
	}

	private void drawSelect(PC_CoordI offsetPos, int sx, int ex, int y) {
		if (!yCoordsInDrawRect(y)) {
			return;
		}
		String line = getLine(y);
		if (sx < 0) {
			sx = 0;
		}
		if (ex < 0) {
			ex = line.length();
		}
		int cy = y - scroll.y;
		int sxx = getStringWidth(line.substring(0, sx)) - scroll.x;
		int exx = getStringWidth(line.substring(0, ex)) - scroll.x;
		if (sxx < 0) {
			sxx = 0;
		} else if (sxx > size.x - 24) {
			return;
		}
		if (exx < 0) {
			return;
		} else if (exx > size.x - 24) {
			exx = size.x - 24;
		}

		drawRect(offsetPos.x + pos.x + sxx + 6, offsetPos.y + pos.y + 6 + cy * getFR().FONT_HEIGHT, offsetPos.x + pos.x + exx + 6, offsetPos.y + pos.y + 6 + (cy + 1) * getFR().FONT_HEIGHT, 0xff3399FF);
	}

	private void drawSpecialChar(PC_CoordI offsetPos, int x, int y, String word, boolean highlited) {
		if (!highlited) {
			drawStringStringAt(offsetPos, x, y, word, 0xff000000 | PC_GresHighlightHelper.colorDefault);
			return;
		}
		Keyword kw = null;
		String w = "";
		for (int j = 0; j < word.length(); j += w.length()) {
			w = "";
			kw = null;
			for (int i = word.length(); i > j; i--) {
				w = word.substring(j, i);
				kw = getKeywordForWord(w, false);
				if (kw != null) break;
			}
			if (w.equals("")) {
				w = "" + word.charAt(j);
			}
			if (kw == null) kw = keywordToFinish;
			drawStringStringAt(offsetPos, x, y, w, getColorForKeyword(kw));
			x += getStringWidth(w);
		}
	}

	private void drawWord(PC_CoordI offsetPos, int x, int y, String word, boolean highlited) {
		drawStringStringAt(offsetPos, x, y, word, highlited ? getColorForWord(word) : 0xff000000 | PC_GresHighlightHelper.colorDefault);
	}

	private void drawStringStringAt(PC_CoordI offsetPos, int x, int y, String word, int color) {
		if (!yCoordsInDrawRect(y)) {
			return;
		}
		int wordlength = getStringWidth(word);
		if (x > size.x + wordlength - 26) {
			return;
		}
		int strposStart = 0;
		int strSize = 0;
		int charSize;
		int sx = x;
		int xp;
		for (strposStart = 0; strposStart < word.length() && sx < 0; strposStart++) {
			charSize = getStringWidth("" + word.charAt(strposStart));
			sx += charSize;
		}
		xp = sx;
		for (strSize = 0; strSize + strposStart < word.length() && sx <= size.x - 25; strSize++) {
			charSize = getStringWidth("" + word.charAt(strposStart + strSize));
			sx += charSize;
		}
		drawStringColor(word.substring(strposStart, strposStart + strSize), offsetPos.x + pos.x + 6 + xp, offsetPos.y + pos.y + 6 + (y - scroll.y) * getFR().FONT_HEIGHT, color);
	}

	private void drawStringLine(PC_CoordI offsetPos, int y) {
		/*if (!yCoordsInDrawRect(y)) {
			return;
		}*/
		String line = getLine(y);
		String word = "";
		char c;
		int sx = -scroll.x;
		int wordStart = sx;
		boolean isWord = false;
		for (int i = 0; i < line.length(); i++) {
			c = line.charAt(i);
			if (Character.isLetterOrDigit(c) || c == '_' || (!word.equals("") && ("._".contains(c + "")))) {
				if (!isWord && !word.equals("")) {
					drawSpecialChar(offsetPos, wordStart, y, word, true);
					word = "";
				}
				if (word.equals("")) {
					wordStart = sx;
				}
				isWord = true;
				word += c;
			} else if (Character.isSpaceChar(c)) {
				if (!word.equals("")) {
					if (isWord)
						drawWord(offsetPos, wordStart, y, word, true);
					else
						drawSpecialChar(offsetPos, wordStart, y, word, true);
				}
				word = "";
			} else {
				if (isWord && !word.equals("")) {
					drawWord(offsetPos, wordStart, y, word, true);
					word = "";
				}
				isWord = false;
				if (word.equals("")) {
					wordStart = sx;
				}
				word += c;
			}
			sx += getStringWidth("" + c);
		}
		if (!word.equals("")) {
			if (isWord)
				drawWord(offsetPos, wordStart, y, word, true);
			else
				drawSpecialChar(offsetPos, wordStart, y, word, true);
		}
	}

	@Override
	protected void render(PC_CoordI offsetPos) {

		drawHorizontalLine(offsetPos.x + pos.x, offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y, 0xffA0A0A0);
		drawHorizontalLine(offsetPos.x + pos.x, offsetPos.x + pos.x + size.x - 12, offsetPos.y + pos.y + size.y - 1, 0xffA0A0A0);

		drawVerticalLine(offsetPos.x + pos.x, offsetPos.y + pos.y, offsetPos.y + pos.y + size.y - 1, 0xffA0A0A0);
		drawVerticalLine(offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y, offsetPos.y + pos.y + size.y - 12, 0xffA0A0A0);

		drawRect(offsetPos.x + pos.x + 1, offsetPos.y + pos.y + 1, offsetPos.x + pos.x + size.x - 12, offsetPos.y + pos.y + size.y - 12, 0xff000000 | PC_GresHighlightHelper.colorBackground);

		int scrollbarBg = 0x909090;

		drawRect(offsetPos.x + pos.x + 1, offsetPos.y + pos.y + size.y - 11, offsetPos.x + pos.x + size.x - 12, offsetPos.y + pos.y + size.y - 1, scrollbarBg);

		drawRect(offsetPos.x + pos.x + size.x - 11, offsetPos.y + pos.y + 1, offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y + size.y - 12, scrollbarBg);

		drawHorizontalLine(offsetPos.x + pos.x, offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y + size.y - 12, 0xffA0A0A0);

		drawVerticalLine(offsetPos.x + pos.x + size.x - 12, offsetPos.y + pos.y, offsetPos.y + pos.y + size.y - 1, 0xffA0A0A0);

		if (mousePressed == 0 || mousePressed == 1) {
			calcScrollPosition();
		}

		renderTextureSliced(offsetPos.offset(hScrollPos + 1, size.y - 11), mod_PCcore.getImgDir() + "gres/scrollbar_handle.png", new PC_CoordI(hScrollSize - 1, 10), new PC_CoordI(0, 0), new PC_CoordI(256, 256));

		renderTextureSliced(offsetPos.offset(size.x - 11, 1 + vScrollPos), mod_PCcore.getImgDir() + "gres/scrollbar_handle.png", new PC_CoordI(10, vScrollSize - 1), new PC_CoordI(0, 0), new PC_CoordI(256, 256));

		if ((!(mouseSelectStart.x == mouseSelectEnd.x && mouseSelectStart.y == mouseSelectEnd.y)) && hasFocus) {
			int s = calcSelectCoordsToStringIndex(mouseSelectStart), e = calcSelectCoordsToStringIndex(mouseSelectEnd);
			PC_CoordI cs = mouseSelectStart, ce = mouseSelectEnd;
			if (s > e) {
				cs = mouseSelectEnd;
				ce = mouseSelectStart;
			}

			if (mouseSelectStart.y == mouseSelectEnd.y) {
				if (yCoordsInDrawRect(mouseSelectStart.y)) {
					drawSelect(offsetPos, cs.x, ce.x, cs.y);
				}
			} else {
				if (yCoordsInDrawRect(cs.y)) {
					drawSelect(offsetPos, cs.x, -1, cs.y);
				}
				for (int i = cs.y + 1; i < ce.y; i++) {
					if (yCoordsInDrawRect(i)) {
						drawSelect(offsetPos, 0, -1, i);
					}
				}
				if (yCoordsInDrawRect(ce.y)) {
					drawSelect(offsetPos, 0, ce.x, ce.y);
				}
			}

		}

		keywordToFinish = null;
		newOneFrameKeyWords = new ArrayList<Keyword>();
		for (int i = 0; i <= getLineNumbers(); i++) {
			drawStringLine(offsetPos, i);
			if (keywordToFinish != null) {
				if (!keywordToFinish.isRegexp && keywordToFinish.end.equals("\n"))
					keywordToFinish = null;
				else if (keywordToFinish.isRegexp && "\n".matches(keywordToFinish.end)) keywordToFinish = null;
			}
		}
		oneFrameKeyWords = newOneFrameKeyWords;
		
		if (hasFocus && (cursorCounter / 6) % 2 == 0) {
			if (coordsInDrawRect(new PC_CoordI(mouseSelectEnd.x > 0 ? mouseSelectEnd.x - 1 : 0, mouseSelectEnd.y))) {
				if (calcSelectCoordsToStringIndex(mouseSelectEnd) == text.length()) {
					drawString("_", offsetPos.x + pos.x + getStringWidth(getLine(mouseSelectEnd.y)) + 6 - scroll.x, offsetPos.y + pos.y + 6 + (mouseSelectEnd.y - scroll.y) * getFR().FONT_HEIGHT);
				} else {
					drawVerticalLine(offsetPos.x + pos.x + getStringWidth(getLine(mouseSelectEnd.y).substring(0, mouseSelectEnd.x)) + 5 - scroll.x, offsetPos.y + pos.y + 6 + (mouseSelectEnd.y - scroll.y) * getFR().FONT_HEIGHT,
							offsetPos.y + pos.y + 6 + (mouseSelectEnd.y - scroll.y + 1) * getFR().FONT_HEIGHT, color[enabled ? textColorEnabled : textColorDisabled]);
				}
			}
		}
	}

	@Override
	public boolean mouseOver(PC_CoordI mousePos) {
		return true;
	}

	private String getLine(int line) {
		String s = "";
		int pos = 0;
		while (line > 0) {
			if (pos >= text.length()) {
				return "";
			}
			if (text.charAt(pos) == br) {
				line--;
			}
			pos++;
		}
		while (true) {
			if (pos >= text.length()) {
				return s;
			}
			if (text.charAt(pos) == br) {
				return s;
			}
			s += text.charAt(pos);
			pos++;
		}
	}

	private PC_CoordI getMousePositionInString(PC_CoordI pos) {
		int charSize;
		int row = scroll.y;
		PC_CoordI coord = null;
		pos = pos.copy();
		pos.x -= 6;
		pos.y -= 6;
		row += pos.y < 0 ? -1 : pos.y / getFR().FONT_HEIGHT;
		pos.x += scroll.x;
		if (row < 0) {
			row = 0;
		}
		String rowText = getLine(row);
		for (int i = 0; i < rowText.length(); i++) {
			charSize = getStringWidth("" + rowText.charAt(i));
			if (pos.x - charSize / 2 < 0) {
				coord = new PC_CoordI(i, row);
				break;
			}
			pos.x -= charSize;
		}
		int i = calcSelectCoordsToStringIndex(coord == null ? new PC_CoordI(rowText.length(), row) : coord);
		if (i > text.length()) {
			i = text.length();
		}
		return calcStringIndexToSelectCoords(i);
	}

	private PC_CoordI lineUp() {
		if (mouseSelectEnd.y <= 0) {
			return new PC_CoordI(0, 0);
		}
		int xP = getStringWidth(getLine(mouseSelectEnd.y).substring(0, mouseSelectEnd.x));
		String rowText = getLine(mouseSelectEnd.y - 1);
		int charSize;
		PC_CoordI coord = null;
		for (int i = 0; i < rowText.length(); i++) {
			charSize = getStringWidth("" + rowText.charAt(i));
			if (xP - charSize / 2 < 0) {
				coord = new PC_CoordI(i, mouseSelectEnd.y - 1);
				break;
			}
			xP -= charSize;
		}
		return coord == null ? new PC_CoordI(rowText.length(), mouseSelectEnd.y - 1) : coord;
	}

	private PC_CoordI lineDown() {
		int xP = getStringWidth(getLine(mouseSelectEnd.y).substring(0, mouseSelectEnd.x));
		String rowText = getLine(mouseSelectEnd.y + 1);
		int charSize;
		PC_CoordI coord = null;
		for (int i = 0; i < rowText.length(); i++) {
			charSize = getStringWidth("" + rowText.charAt(i));
			if (xP - charSize / 2 < 0) {
				coord = new PC_CoordI(i, mouseSelectEnd.y + 1);
				break;
			}
			xP -= charSize;
		}
		int i = calcSelectCoordsToStringIndex(coord == null ? new PC_CoordI(rowText.length(), mouseSelectEnd.y + 1) : coord);
		if (i > text.length()) {
			i = text.length();
		}
		return calcStringIndexToSelectCoords(i);
	}

	@Override
	public boolean mouseClick(PC_CoordI mousePos, int key) {
		mousePressed = 0;
		if (!enabled) {
			return false;
		}
		lastMousePosition.setTo(mousePos);
		if (key != -1) {
			if (mousePos.x < size.x - 12 && mousePos.y < size.y - 12) {
				mouseSelectEnd.setTo(getMousePositionInString(mousePos));
				if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) {
					mouseSelectStart.setTo(mouseSelectEnd);
				}
				mousePressed = 1;
				setScrollToCursor();
				return true;
			} else if (mousePos.y < size.y - 12) {
				if (mousePos.y - 1 < vScrollPos) {
					scroll.y--;
					return true;
				}
				if (mousePos.y - 1 >= vScrollPos + vScrollSize) {
					scroll.y++;
					return true;
				}
				mousePressed = 2;
				return true;
			} else if (mousePos.x < size.x - 12) {
				if (mousePos.x - 1 < hScrollPos) {
					scroll.x -= 5;
					return true;
				}
				if (mousePos.x - 1 >= hScrollPos + hScrollSize) {
					scroll.x += 5;
					return true;
				}
				mousePressed = 3;
				return true;
			}
		}
		return false;
	}

	@Override
	public void mouseMove(PC_CoordI mousePos) {
		switch (mousePressed) {
			case 1:
				mouseSelectEnd.setTo(getMousePositionInString(mousePos));
				setScrollToCursor();
				break;
			case 2:
				vScrollPos += mousePos.y - lastMousePosition.y;
				updateScrollPosition();
				break;
			case 3:
				hScrollPos += mousePos.x - lastMousePosition.x;
				updateScrollPosition();
				break;
		}
		lastMousePosition.setTo(mousePos);
	}

	private int calcSelectCoordsToStringIndex(PC_CoordI c) {
		int pos = 0;
		int line = c.y;
		while (line > 0) {
			if (pos >= text.length()) {
				return pos;
			}
			if (text.charAt(pos) == br) {
				line--;
			}
			pos++;
		}
		int p = c.x;
		while (p > 0) {
			if (pos >= text.length()) {
				return pos;
			}
			if (text.charAt(pos) == br) {
				return pos;
			}
			pos++;
			p--;
		}
		return pos;
	}

	private PC_CoordI calcStringIndexToSelectCoords(int index) {
		int xx = 0, yy = 0;
		for (int i = 0; i < index; i++) {
			if (i >= text.length()) {
				break;
			}
			if (text.charAt(i) == br) {
				yy++;
				xx = -1;
			}
			xx++;
		}
		return new PC_CoordI(xx, yy);
	}

	/**
	 * Replace selection with given key
	 * 
	 * @param c key
	 */
	public void addKey(char c) {
		int s = calcSelectCoordsToStringIndex(mouseSelectStart), e = calcSelectCoordsToStringIndex(mouseSelectEnd);
		if (s > e) {
			int t = e;
			e = s;
			s = t;
		}

		int oldSize = e - s;

		String s1 = text.substring(0, s);
		String s2 = text.substring(e);
		StringAdd a = autoAdd.charAdd(this, c, s1, s2);
		if(a==null){
			text = s1 + c + s2;
			e++;
		}else{
			text = s1 + c + a.addString + s2;
			e += a.jumpToEnd?a.addString.length()+1:1;
		}
		e -= oldSize;
		mouseSelectEnd.setTo(calcStringIndexToSelectCoords(e));
		mouseSelectStart.setTo(mouseSelectEnd);
	}
	
	private void deleteSelected() {
		int s = calcSelectCoordsToStringIndex(mouseSelectStart), e = calcSelectCoordsToStringIndex(mouseSelectEnd);
		if (s > e) {
			int t = e;
			e = s;
			s = t;
		}
		String s1 = text.substring(0, s);
		String s2 = text.substring(e);
		text = s1 + s2;
		mouseSelectEnd.setTo(calcStringIndexToSelectCoords(s));
		mouseSelectStart.setTo(mouseSelectEnd);
	}

	private void key_backspace() {
		if (!(mouseSelectStart.x == mouseSelectEnd.x && mouseSelectStart.y == mouseSelectEnd.y)) {
			deleteSelected();
			return;
		}
		int e = calcSelectCoordsToStringIndex(mouseSelectEnd);
		if (e <= 0) {
			return;
		}
		String s1 = text.substring(0, e - 1);
		String s2 = text.substring(e);
		text = s1 + s2;
		mouseSelectEnd.setTo(calcStringIndexToSelectCoords(e - 1));
		mouseSelectStart.setTo(mouseSelectEnd);
	}

	private void key_delete() {
		if (!(mouseSelectStart.x == mouseSelectEnd.x && mouseSelectStart.y == mouseSelectEnd.y)) {
			deleteSelected();
			return;
		}
		int p = calcSelectCoordsToStringIndex(mouseSelectEnd);
		if (p >= text.length()) {
			return;
		}
		String s1 = text.substring(0, p);
		String s2 = text.substring(p + 1);
		text = s1 + s2;
	}

	private String getSelect() {
		int s = calcSelectCoordsToStringIndex(mouseSelectStart), e = calcSelectCoordsToStringIndex(mouseSelectEnd);
		if (s > e) {
			int t = e;
			e = s;
			s = t;
		}
		return text.substring(s, e);
	}

	private void setSelected(String stri) {
		int s = calcSelectCoordsToStringIndex(mouseSelectStart), e = calcSelectCoordsToStringIndex(mouseSelectEnd);
		if (s > e) {
			int t = e;
			e = s;
			s = t;
			mouseSelectStart.setTo(mouseSelectEnd);
		}
		String s1 = text.substring(0, s);
		String s2 = text.substring(e);
		text = s1 + stri + s2;

		mouseSelectEnd.setTo(calcStringIndexToSelectCoords(s + stri.length()));
		//EXPERIMENTAL
		mouseSelectStart.setTo(mouseSelectEnd);
	}

	private int shownLines() {
		return (size.y - 26) / getFR().FONT_HEIGHT;
	}

	private void setScrollToCursor() {
		int cy = mouseSelectEnd.y - scroll.y;
		if (cy < 0) {
			scroll.y = mouseSelectEnd.y;
		} else if (cy >= shownLines()) {
			scroll.y = mouseSelectEnd.y - shownLines() + 1;
		}
		String line = getLine(mouseSelectEnd.y);
		int cxs = getStringWidth(line.substring(0, mouseSelectEnd.x));
		int cxb = mouseSelectEnd.x > 0 ? getStringWidth(line.substring(mouseSelectEnd.x - 1, mouseSelectEnd.x)) : 0;
		int cx = cxs - scroll.x;
		if (cx <= cxb) {
			scroll.x = cxs - cxb;
		} else if (cx >= size.x - 25) {
			scroll.x = cxs - size.x + 27;
		}
	}
	
	@Override
	public boolean keyTyped(char c, int key) {
		int p;
		if (!enabled || !hasFocus || (mousePressed != 0 && mousePressed != 1)) {
			return false;
		}
		switch (c) {
			case 3:
				GuiScreen.setClipboardString(getSelect());
				setScrollToCursor();
				return true;

			case 22:
				setSelected(GuiScreen.getClipboardString());
				setScrollToCursor();
				return true;

			case 24:
				GuiScreen.setClipboardString(getSelect());
				deleteSelected();
				setScrollToCursor();
				return true;
		}
		switch (key) {
			case Keyboard.KEY_RETURN:
				addKey(br);
				setScrollToCursor();
				return true;
			case Keyboard.KEY_BACK:
				key_backspace();
				setScrollToCursor();
				return true;
			case Keyboard.KEY_DELETE:
				key_delete();
				setScrollToCursor();
				return true;
			case Keyboard.KEY_LEFT:
				p = calcSelectCoordsToStringIndex(mouseSelectEnd);
				if (p > 0) {
					mouseSelectEnd.setTo(calcStringIndexToSelectCoords(p - 1));
				}
				if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) {
					mouseSelectStart.setTo(mouseSelectEnd);
				}
				setScrollToCursor();
				return true;
			case Keyboard.KEY_RIGHT:
				p = calcSelectCoordsToStringIndex(mouseSelectEnd);
				if (p < text.length()) {
					mouseSelectEnd.setTo(calcStringIndexToSelectCoords(p + 1));
				}
				if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) {
					mouseSelectStart.setTo(mouseSelectEnd);
				}
				setScrollToCursor();
				return true;
			case Keyboard.KEY_UP:
				mouseSelectEnd.setTo(lineUp());
				if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) {
					mouseSelectStart.setTo(mouseSelectEnd);
				}
				setScrollToCursor();
				return true;
			case Keyboard.KEY_DOWN:
				mouseSelectEnd.setTo(lineDown());
				if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) {
					mouseSelectStart.setTo(mouseSelectEnd);
				}
				setScrollToCursor();
				return true;
			default:
				if (ChatAllowedCharacters.isAllowedCharacter(c)) {
					addKey(c);
					setScrollToCursor();
					return true;
				}
				return false;
		}
	}

	private int getLineNumbers() {
		int line = 0;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == br) {
				line++;
			}
		}
		return line;
	}

	@Override
	public void mouseWheel(int i) {
		scroll.y -= i;
		if (scroll.y < 0) {
			scroll.y = 0;
		}
		int maxY = getLineNumbers() - shownLines() + 1;
		if (maxY < 0) {
			maxY = 0;
		}
		if (scroll.y > maxY) {
			scroll.y = maxY;
		}
	}

	@Override
	public void addedToWidget() {}
	
	/**
	 * static version of getFontRenderer
	 * @return font renderer
	 */
	public static FontRenderer getFR() {
		return mod_PCcore.fontRendererDefault;
	}
	
	@Override
	public FontRenderer getFontRenderer() {
		return getFR();
	}
}
