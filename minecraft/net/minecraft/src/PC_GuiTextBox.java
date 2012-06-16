package net.minecraft.src;

import org.lwjgl.input.Keyboard;

/**
 * Smart text edit box with mouse events and scrolling
 * 
 * @author MightyPork
 */
public class PC_GuiTextBox extends Gui {

	private final FontRenderer fontRenderer;
	private final int xPos;
	private final int yPos;
	private final int width;
	private final int height;
	private final int rows;
	private String text;
	private int cursorCounter;
	private int cursorPos;
	private int scrollRowStart = 0;
	/** GUI element focused */
	public boolean isFocused;
	/** GUI element enabled */
	public boolean isEnabled;
	private int TotalRows = 0;
	private int cursorRow = 0;
	private boolean infoValid = false;

	private int colorTextEnabled = 0xe0e0e0;
	private int colorTextDisabled = 0x707070;
	private int colorBackground = 0xff000000;
	private int colorBorder = 0xffa0a0a0;

	/**
	 * Set text colors 0xRRGGBB
	 * 
	 * @param en enabled clr
	 * @param dis disabled clr
	 */
	public void setTextColors(int en, int dis) {
		colorTextEnabled = en;
		colorTextDisabled = dis;
	}

	/**
	 * Set background colors 0xAARRGGBB.
	 * 
	 * @param ins inside box
	 * @param bdr border line
	 */
	public void setBoxColors(int ins, int bdr) {
		colorBackground = ins;
		colorBorder = bdr;
	}

	/**
	 * create text edit box
	 * 
	 * @param guiscreen parent gui screen
	 * @param fontrenderer font renderer
	 * @param x x coord left top
	 * @param y y coord left top
	 * @param w width
	 * @param h height
	 * @param r rows count
	 * @param s string to edit
	 */
	public PC_GuiTextBox(GuiScreen guiscreen, FontRenderer fontrenderer, int x, int y, int w, int h, int r, String s) {
		isFocused = false;
		isEnabled = true;
		fontRenderer = fontrenderer;
		xPos = x;
		yPos = y;
		width = w;
		height = r * 11 + 8 + h;
		rows = r;
		scrollRowStart = 0;
		setText(s);
		updateTextInfo();
	}

	/**
	 * Set edited text
	 * 
	 * @param s the text
	 */
	public void setText(String s) {
		text = s;
		cursorPos = 0; // s.length();
	}

	/**
	 * Get edited text
	 * 
	 * @return text
	 */
	public String getText() {
		return text;
	}

	/**
	 * update blinking counter
	 */
	public void updateCursorCounter() {
		cursorCounter++; // blinking
	}

	/**
	 * Call this method from you GuiScreen to process the keys into textbox.
	 * 
	 * @param c character
	 * @param i key index
	 */
	public void textboxKeyTyped(char c, int i) {
		if (!isEnabled || !isFocused) { return; }
		if (c == '\t') {
			text = text.substring(0, cursorPos) + "   " + text.substring(cursorPos, text.length());
			cursorPos += 3;
		}
		if (c == '\026') // ctrl v
		{
			String s;
			s = GuiScreen.getClipboardString();

			if (s == null) { return; }

			if (rows > 2 || countRowsAddString(s) <= rows) {
				text = text.substring(0, cursorPos) + s + text.substring(cursorPos, text.length());
				cursorPos += s.length();
			}
		}

		if (i == Keyboard.KEY_HOME && text.length() > 0) {
			cursorPos = 0;
		}

		if ((i == Keyboard.KEY_RETURN || c == '\n') && rows > 2) {
			text = text.substring(0, cursorPos) + "\n" + text.substring(cursorPos, text.length());
			cursorPos++;

		} else if (ChatAllowedCharacters.allowedCharacters.indexOf(c) >= 0) {
			if (rows > 2 || countRowsAddChar(c) <= rows) {
				text = text.substring(0, cursorPos) + c + text.substring(cursorPos, text.length());
				cursorPos++;
			}
		}

		if (i == Keyboard.KEY_END && text.length() > 0) {
			cursorPos = text.length();
		}

		if (i == Keyboard.KEY_LEFT && text.length() > 0 && cursorPos > 0) // backspace
		{
			cursorPos--;
		}

		if (i == Keyboard.KEY_UP && text.length() > 0) {
			if (getCursorRow() < 1) { return; }

			cursorPos = getRowStartIndex(getCursorRow() - 1);

			if (getCursorRow() <= scrollRowStart) {
				scroll(-1);
			}
		}

		if (i == Keyboard.KEY_DOWN && text.length() > 0) {
			if (getCursorRow() >= countRows() - 1) { return; }

			cursorPos = getRowStartIndex(getCursorRow() + 1);

			if (getCursorRow() >= scrollRowStart + rows - 1) {
				scroll(1);
			}
		}

		if (i == Keyboard.KEY_RIGHT && text.length() > 0 && cursorPos < text.length()) // backspace
		{
			cursorPos++;
		}

		if (i == Keyboard.KEY_BACK && text.length() > 0 && cursorPos > 0) // backspace
		{
			String newtext = "";

			if (cursorPos == 1) {
				newtext = "";
			} else {
				newtext = text.substring(0, cursorPos - 1);
			}

			if (cursorPos < text.length()) {
				newtext += text.substring(cursorPos, text.length());
			}

			text = newtext;
			cursorPos--;
		}

		if (i == Keyboard.KEY_DELETE && text.length() > 0 && cursorPos < text.length()) // backspace
		{
			String newtext = "";
			if (text.length() > 1) {
				newtext = text.substring(0, cursorPos);
			}
			newtext += text.substring(cursorPos + 1, text.length());
			text = newtext;
		}

		if (cursorPos > text.length()) {
			cursorPos = text.length();
		}
		if (cursorPos < 0) {
			cursorPos = 0;
		}

		updateTextInfo();

		while (getCursorRow() < scrollRowStart) {
			scroll(-1);
		}
		while (getCursorRow() >= scrollRowStart + rows) {
			scroll(1);
		}

		updateTextInfo();
	}

	/**
	 * Perform mouse click event
	 * 
	 * @param i x coord
	 * @param j y coord
	 * @param k ? unused
	 */
	public void mouseClicked(int i, int j, int k) {
		boolean flag = checkClicked(i, j);
		setFocused(flag);
		cursorPos = getPosAtCoords(i, j);
		cursorPos = MathHelper.clamp_int(cursorPos, 0, text.length());
		updateTextInfo();
	}

	/**
	 * Test whether the mouse clicked inside.
	 * 
	 * @param x
	 * @param y
	 * @return clicked
	 */
	public boolean checkClicked(int x, int y) {
		return isEnabled && x >= xPos && x < xPos + width && y >= yPos && y < yPos + height;
	}

	/**
	 * Set whether this box is focused
	 * 
	 * @param flag
	 */
	public void setFocused(boolean flag) {
		if (flag && !isFocused) {
			cursorCounter = 0;
		}
		isFocused = flag;
	}

	/**
	 * Draw the box into GUI screen
	 */
	public void drawTextBox() {
		drawRect(xPos - 1, yPos - 1, xPos + width + 1, yPos + height + 1, colorBorder);
		drawRect(xPos, yPos, xPos + width, yPos + height, colorBackground);

		if (rows > 2) {
			int sbHeight = height - 14; // 4,4 borders, 8 scb height
			int r = countRows();
			if (r < 1) {
				r = 1;
			}
			int sbY = yPos + 4 + (int) (((float) getCursorRow() / (float) r) * sbHeight);
			drawRect(xPos + width - 2, sbY, xPos + width, sbY + 8, 0xBB9999FF);
		}

		boolean cursorVisible = isEnabled && isFocused && (cursorCounter / 6) % 2 == 0;

		int lastEnd = 0;
		int rowc = 0;

		boolean alreadyShowedCursor = false;
		int showedRows = 0;
		int widthCounter = 0;
		for (int cnt = 0; cnt <= text.length(); cnt++) {

			boolean enter = false;
			if (text.length() > 0 && cnt < text.length()) {
				enter = text.substring(cnt, cnt + 1).equals("\n");
			}

			if (cnt < text.length()) {
				widthCounter += fontRenderer.getStringWidth(text.substring(cnt, cnt + 1).replace("\n", ""));
			}

			if (widthCounter >= width - 12 || cnt >= text.length() || enter) {
				if (rowc >= scrollRowStart) {
					fontRenderer.renderString(text.substring(lastEnd, cnt).replace("\n", ""), xPos + 4,
							(rows == 1 ? (yPos + (height - 8) / 2) : (yPos + 4 + (rowc - scrollRowStart) * 11)),
							isEnabled ? colorTextEnabled : colorTextDisabled, false);

					if (!alreadyShowedCursor && cursorPos >= lastEnd && cursorPos <= cnt) {
						alreadyShowedCursor = true;
						if (isEnabled && isFocused && cursorVisible) {
							fontRenderer.renderString("_",
									xPos + 4 + fontRenderer.getStringWidth(text.substring(lastEnd, cursorPos).replace("\n", "")),
									(rows == 1 ? (yPos + (height - 8) / 2) : (yPos + 4 + (rowc - scrollRowStart) * 11)) + 1,
									colorTextEnabled, false);

						}
					}
					showedRows++;

				}

				lastEnd = cnt;
				rowc++;
				widthCounter = 0;

			}

			if (showedRows >= rows) {
				break;
			}
		}

	}

	/**
	 * Update counters. Counters are cached to make it faster.
	 */
	private void updateTextInfo() {
		infoValid = false;
		cursorRow = getCursorRow();
		getRowStartIndex(getCursorRow());
		TotalRows = countRows();
		infoValid = true;
	}

	private int getCursorRow() {
		if (infoValid) { return cursorRow; }

		int lastEnd = 0;
		int rowc = 0;

		int widthCounter = 0;
		for (int cnt = 0; cnt <= text.length(); cnt++) {

			boolean enter = false;
			if (text.length() > 0 && cnt < text.length()) {
				enter = text.substring(cnt, cnt + 1).equals("\n");
			}

			if (cnt < text.length()) {
				widthCounter += fontRenderer.getStringWidth(text.substring(cnt, cnt + 1).replace("\n", ""));
			}

			if (widthCounter >= width - 12 || cnt >= text.length() || enter) {

				if (cursorPos >= lastEnd && cursorPos <= cnt) {
					cursorRow = rowc;
					return rowc;
				}

				lastEnd = cnt;
				rowc++;
				widthCounter = 0;
			}
		}

		return rowc;
	}

	/**
	 * Returns index of the first character in given row
	 * 
	 * @param row row
	 * @return the index
	 */
	private int getRowStartIndex(int row) {
		int lastEnd = 0;
		int rowc = 0;

		if (row < 0) {
			row = 0;
		}

		if (row == 0) { return 0; }

		int widthCounter = 0;
		for (int cnt = 0; cnt <= text.length(); cnt++) {

			boolean enter = false;
			if (text.length() > 0 && cnt < text.length()) {
				enter = text.substring(cnt, cnt + 1).equals("\n");
			}

			if (cnt < text.length()) {
				widthCounter += fontRenderer.getStringWidth(text.substring(cnt, cnt + 1).replace("\n", ""));
			}

			if (widthCounter >= width - 12 || cnt >= text.length() || enter) {

				if (row == rowc) { return lastEnd + 1; }

				lastEnd = cnt;
				rowc++;
				widthCounter = 0;
			}
		}

		return rowc;
	}

	/**
	 * Count rows after adding one character
	 * 
	 * @param ch character
	 * @return number of rows
	 */
	private int countRowsAddChar(char ch) {
		return countRowsAddString(Character.toString(ch));
	}

	/**
	 * Scroll by given number of rows
	 * 
	 * @param i rows (+1, -1)
	 */
	private void scroll(int i) {
		scrollRowStart += i;
		if (scrollRowStart < 0) {
			scrollRowStart = 0;
		}

		int cntrows = countRows();
		int lastValidRow = cntrows - rows + 1;
		if (lastValidRow < 0) {
			lastValidRow = 0;
		}

		if (scrollRowStart > lastValidRow) {
			scrollRowStart = lastValidRow;
		}
	}

	/**
	 * Count rows after future adding string
	 * 
	 * @param stringAdded
	 * @return rows count
	 */
	private int countRowsAddString(String stringAdded) {

		int lastEnd = 0;
		int rowc = 0;

		String tmptext = text + stringAdded;

		int lastrow = 0;
		for (int cnt = 0; cnt <= tmptext.length(); cnt++) {
			lastrow = rowc;
			if (fontRenderer.getStringWidth(tmptext.substring(lastEnd, cnt).replace("\n", "")) >= width - 12 || cnt >= tmptext.length()
					|| (cnt < text.length() && text.substring(cnt, cnt + 1).equals("\n"))) {
				lastEnd = cnt;
				rowc++;

			}
		}

		if (lastrow < rowc) {
			rowc--;
		}

		return rowc + 1;
	}

	/**
	 * Count rows in the text edit box
	 * 
	 * @return rows count
	 */
	private int countRows() {
		if (infoValid) { return TotalRows; }

		int lastEnd = 0;
		int rowc = 0;

		if (text.length() == 0) {
			TotalRows = 0;
			return 0;
		}

		int lastrow = 0;
		for (int cnt = 0; cnt <= text.length(); cnt++) {
			lastrow = rowc;
			if (fontRenderer.getStringWidth(text.substring(lastEnd, cnt).replace("\n", "")) >= width - 12 || cnt >= text.length()
					|| (cnt < text.length() && text.substring(cnt, cnt + 1).equals("\n"))) {
				lastEnd = cnt;
				rowc++;

			}
		}

		if (lastrow < rowc) {
			rowc--;
		}

		TotalRows = rowc + 1;
		return rowc + 1;
	}

	/**
	 * Get index of the character under mouse cursor
	 * 
	 * @param x
	 * @param y
	 * @return index in text
	 */
	private int getPosAtCoords(int x, int y) {

		if (text.length() == 0) { return 0; }

		int lastEnd = 0;
		int rowc = 0;

		int rowStart = yPos + 4;
		int rowEnd = yPos + 4 + 11;
		int charStart = 0;
		int charEnd = 8;

		int showedRow = 0;
		boolean showingStarted = (scrollRowStart == 0);
		if (scrollRowStart == 0) {
			showedRow++;
		}
		for (int cnt = 0; cnt <= text.length(); cnt++) {

			if ((showingStarted && (y > rowStart && rowc >= scrollRowStart && y < rowEnd)) || rows == 0) {
				charStart = xPos + 4 + (cnt == 0 ? 0 : fontRenderer.getStringWidth(text.substring(lastEnd, cnt - 1).replace("\n", "")));
				charEnd = xPos + 4 + fontRenderer.getStringWidth(text.substring(lastEnd, cnt).replace("\n", ""));

				if (x >= charStart && x < charEnd) { return cnt - 1; }
			}

			if (fontRenderer.getStringWidth(text.substring(lastEnd, cnt).replace("\n", "")) >= width - 12 || cnt >= text.length()
					|| (cnt < text.length() && text.substring(cnt, cnt + 1).equals("\n"))) {

				// if row is visible and mouse pointer is within vertical range
				if (showingStarted && rowc >= scrollRowStart && y > rowStart && y < rowEnd) { return cnt; }

				lastEnd = cnt;
				rowc++;

				rowStart = yPos + 4 + 11 * (showedRow);
				rowEnd = yPos + 4 + 11 * (showedRow + 1);

				if (rowc >= scrollRowStart) {
					showedRow++;
					showingStarted = true;
				}

			}

			if (rowc > scrollRowStart + rows) {
				break;
			}
		}

		return text.length();
	}
}
