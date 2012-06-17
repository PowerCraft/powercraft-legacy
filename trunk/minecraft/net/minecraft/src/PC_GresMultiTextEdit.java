package net.minecraft.src;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

public class PC_GresMultiTextEdit extends PC_GresWidget {

	private static final char br = '\n';
	
	public static class Keyword{
		public Keyword(String word, int color){
			this.word = word;
			this.color = color;
		}
		public String word;
		public int color;
	}
	
	private PC_CoordI mouseSelectStart = new PC_CoordI(0, 0);
	private PC_CoordI mouseSelectEnd = new PC_CoordI(0, 0);
	private boolean mousePressed = false;
	private PC_CoordI scroll = new PC_CoordI(0, 0);
	private ArrayList<Keyword> keyWords = null;
	
	public PC_GresMultiTextEdit(String text, int minWidth, int minHeight) {
		super(minWidth>20?minWidth:20, minHeight>PC_Utils.mc().fontRenderer.FONT_HEIGHT+12?minHeight:PC_Utils.mc().fontRenderer.FONT_HEIGHT+12, text);
		canAddWidget = false;
		color[textColorEnabled] = 0xffffffff;
		color[textColorShadowEnabled] = 0xff383838;
		color[textColorDisabled] = 0xffffffff;
		color[textColorShadowDisabled] = 0xff383838;
	}
	
	public PC_GresMultiTextEdit(String text, int minWidth, int minHeight, ArrayList<Keyword> keyWords) {
		super(minWidth>20?minWidth:20, minHeight>PC_Utils.mc().fontRenderer.FONT_HEIGHT+12?minHeight:PC_Utils.mc().fontRenderer.FONT_HEIGHT+12, text);
		canAddWidget = false;
		color[textColorEnabled] = 0xffffffff;
		color[textColorShadowEnabled] = 0xff383838;
		color[textColorDisabled] = 0xffffffff;
		color[textColorShadowDisabled] = 0xff383838;
		this.keyWords = keyWords;
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
	public void calcChildPositions() {
	}

	private int getColorForWord(String word){
		if(keyWords!=null)
			for(Keyword k:keyWords){
				if(k.word.equalsIgnoreCase(word))
					return k.color;
			}
		return 0xffffffff;
	}
	
	private boolean yCoordsInDrawRect(int cy){
		return cy >= scroll.y && cy < scroll.y + shownLines();
	}
	
	private void drawSelect(PC_CoordI offsetPos, int sx, int ex, int y){
		if(!yCoordsInDrawRect(y))
			return;
		String line = getLine(y);
		if(sx<0)
			sx = 0;
		if(ex<0)
			ex = line.length();
		int cy = y-scroll.y;
		int sxx = getStringWidth(line.substring(0, sx)) - scroll.x;
		int exx = getStringWidth(line.substring(0, ex)) - scroll.x;
		if(sxx<0)
			sxx = 0;
		else if(sxx>size.x-12)
			return;
		if(exx<0)
			return;
		else if(exx>size.x-12)
			exx=size.x-12;
		
		drawRect(offsetPos.x + pos.x + sxx + 6, offsetPos.y + pos.y + 6 + cy * PC_Utils.mc().fontRenderer.FONT_HEIGHT,
				offsetPos.x + pos.x + exx + 6, offsetPos.y + pos.y + 6 + (cy + 1) * PC_Utils.mc().fontRenderer.FONT_HEIGHT, 0xff3399FF);
	}
	
	private void drawWord(PC_CoordI offsetPos, int x, int y, String word){
		if(!yCoordsInDrawRect(y))
			return;
		int wordlength = getStringWidth(word);
		if(x>size.x+wordlength-12)
			return;
		int color = getColorForWord(word);
		int strposStart = 0;
		int strSize = 0;
		int charSize;
		int sx = x;
		int xp;
		for (strposStart = 0; strposStart < word.length() && sx<0; strposStart++) {
			charSize = getStringWidth("" + word.charAt(strposStart));
			sx += charSize;
		}
		xp = sx;
		for (strSize = 0; strSize + strposStart < word.length() && sx<=size.x-15; strSize++) {
			charSize = getStringWidth("" + word.charAt(strposStart + strSize));
			sx += charSize;
		}
		getFontRenderer().drawStringWithShadow(word.substring(strposStart,strposStart + strSize), offsetPos.x + pos.x + 6 + xp, offsetPos.y + pos.y + 6 + (y-scroll.y) * PC_Utils.mc().fontRenderer.FONT_HEIGHT, color);
	}
	
	private void drawStringLine(PC_CoordI offsetPos, int y){
		if(!yCoordsInDrawRect(y))
			return;
		String line = getLine(y);
		String word = "";
		char c;
		int sx=-scroll.x;
		int wordStart = sx;
		for(int i=0; i<line.length(); i++){
			c = line.charAt(i);
			if(Character.isLetterOrDigit(c)){
				if(word.equals(""))
					wordStart = sx;
				word += c;
			}else if(Character.isSpaceChar(c)){
				if(!word.equals(""))
					drawWord(offsetPos, wordStart, y, word);
				word = "";
			}else{
				if(!word.equals(""))
					drawWord(offsetPos, wordStart, y, word);
				drawWord(offsetPos, sx, y, ""+c);
				word = "";
			}
			sx += getStringWidth(""+c);
		}
		if(!word.equals(""))
			drawWord(offsetPos, wordStart, y, word);
	}
	
	@Override
	protected void render(PC_CoordI offsetPos) {

		drawHorizontalLine(offsetPos.x + pos.x, offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y, 0xffA0A0A0);
		drawHorizontalLine(offsetPos.x + pos.x, offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y + size.y - 1, 0xffA0A0A0);

		drawVerticalLine(offsetPos.x + pos.x, offsetPos.y + pos.y, offsetPos.y + pos.y + size.y - 1, 0xffA0A0A0);
		drawVerticalLine(offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y, offsetPos.y + pos.y + size.y - 1, 0xffA0A0A0);

		drawRect(offsetPos.x + pos.x + 1, offsetPos.y + pos.y + 1, offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y + size.y - 1, 0xff000000);

		if ((!(mouseSelectStart.x == mouseSelectEnd.x && mouseSelectStart.y == mouseSelectEnd.y)) && hasFocus) {
			int s = calcSelectCoordsToStringIndex(mouseSelectStart), e = calcSelectCoordsToStringIndex(mouseSelectEnd);
			PC_CoordI cs = mouseSelectStart, ce = mouseSelectEnd;
			if (s > e) {
				cs = mouseSelectEnd;
				ce = mouseSelectStart;
			}
			
			if(mouseSelectStart.y == mouseSelectEnd.y){
				if(yCoordsInDrawRect(mouseSelectStart.y))
					drawSelect(offsetPos, cs.x, ce.x, cs.y);
			}else{
				if(yCoordsInDrawRect(cs.y))
					drawSelect(offsetPos, cs.x, -1, cs.y);
				for(int i=cs.y+1; i<ce.y; i++){
					if(yCoordsInDrawRect(i))
						drawSelect(offsetPos, 0, -1, i);
				}
				if(yCoordsInDrawRect(ce.y))
					drawSelect(offsetPos, 0, ce.x, ce.y);
			}

		}
		
		for(int i=0; i<shownLines(); i++)
			drawStringLine(offsetPos, i+scroll.y);
		

		if (calcSelectCoordsToStringIndex(mouseSelectEnd) == text.length()) {
			if (hasFocus && (cursorCounter / 6) % 2 == 0) drawString("_", offsetPos.x + pos.x + getStringWidth(getLine(mouseSelectEnd.y)) + 6 - scroll.x, offsetPos.y + pos.y
					+ 6 + (mouseSelectEnd.y -scroll.y) * PC_Utils.mc().fontRenderer.FONT_HEIGHT);
		} else if (hasFocus && (cursorCounter / 6) % 2 == 0) drawVerticalLine(
				offsetPos.x + pos.x + getStringWidth(getLine(mouseSelectEnd.y).substring(0, mouseSelectEnd.x)) + 5 - scroll.x, offsetPos.y + pos.y + 6 + (mouseSelectEnd.y -scroll.y) * PC_Utils.mc().fontRenderer.FONT_HEIGHT, offsetPos.y + pos.y + 6 + (mouseSelectEnd.y -scroll.y+1) * PC_Utils.mc().fontRenderer.FONT_HEIGHT,
				color[enabled ? textColorEnabled : textColorDisabled]);
	}

	@Override
	public boolean mouseOver(PC_CoordI mousePos) {
		return true;
	}
	
	private String getLine(int line){
		String s="";
		int pos=0;
		int lastBr=1;
		while(line>0){
			if(pos>=text.length())
				return "";
			if(text.charAt(pos)==br)
				line--;
			pos++;
		}
		while(true){
			if(pos>=text.length())
				return s;
			if(text.charAt(pos)==br)
				return s;
			s += text.charAt(pos);
			pos++;
		}
	}
	
	private PC_CoordI getMousePositionInString(PC_CoordI pos) {
		int charSize;
		int row=scroll.y;
		PC_CoordI coord = null;
		pos = pos.copy();
		pos.x -= 6;
		pos.y -= 6;
		row += pos.y<0?-1:pos.y/PC_Utils.mc().fontRenderer.FONT_HEIGHT;
		pos.x += scroll.x;
		if(row<0)
			row = 0;
		String rowText = getLine(row);
		for (int i = 0; i < rowText.length(); i++) {
			charSize = getStringWidth("" + rowText.charAt(i));
			if (pos.x - charSize / 2 < 0){
				coord = new PC_CoordI(i, row);
				break;
			}
			pos.x -= charSize;
		}
		int i= calcSelectCoordsToStringIndex(coord==null?new PC_CoordI(rowText.length(), row):coord);
		if(i>text.length())
			i=text.length();
		return calcStringIndexToSelectCoords(i);
	}
	
	private PC_CoordI lineUp(){
		if(mouseSelectEnd.y<=0){
			return new PC_CoordI(0, 0);
		}
		int xP = getStringWidth(getLine(mouseSelectEnd.y).substring(0, mouseSelectEnd.x));
		String rowText = getLine(mouseSelectEnd.y-1);
		int charSize;
		PC_CoordI coord = null;
		for (int i = 0; i < rowText.length(); i++) {
			charSize = getStringWidth("" + rowText.charAt(i));
			if (xP - charSize / 2 < 0){
				coord = new PC_CoordI(i, mouseSelectEnd.y-1);
				break;
			}
			xP -= charSize;
		}
		return coord==null?new PC_CoordI(rowText.length(), mouseSelectEnd.y-1):coord;
	}
	
	private PC_CoordI lineDown(){
		int xP = getStringWidth(getLine(mouseSelectEnd.y).substring(0, mouseSelectEnd.x));
		String rowText = getLine(mouseSelectEnd.y+1);
		int charSize;
		PC_CoordI coord = null;
		for (int i = 0; i < rowText.length(); i++) {
			charSize = getStringWidth("" + rowText.charAt(i));
			if (xP - charSize / 2 < 0){
				coord = new PC_CoordI(i, mouseSelectEnd.y+1);
				break;
			}
			xP -= charSize;
		}
		int i= calcSelectCoordsToStringIndex(coord==null?new PC_CoordI(rowText.length(), mouseSelectEnd.y+1):coord);
		if(i>text.length())
			i=text.length();
		return calcStringIndexToSelectCoords(i);
	}
	
	@Override
	public boolean mouseClick(PC_CoordI mousePos, int key) {
		mousePressed = false;
		if (!enabled) return false;
		if (key != -1) {
			mouseSelectEnd.setTo(getMousePositionInString(mousePos));
			if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) mouseSelectStart.setTo(mouseSelectEnd);
			mousePressed = true;
			setScrollToCursor();
			return true;
		}
		return false;
	}

	@Override
	public void mouseMove(PC_CoordI mousePos) {
		if (mousePressed){ 
			mouseSelectEnd.setTo(getMousePositionInString(mousePos));
			setScrollToCursor();
		}
	}

	private int calcSelectCoordsToStringIndex(PC_CoordI c){
		int pos=0;
		int line = c.y;
		while(line>0){
			if(pos>=text.length())
				return pos;
			if(text.charAt(pos)==br)
				line--;
			pos++;
		}
		int p = c.x;
		while(p>0){
			if(pos>=text.length())
				return pos;
			if(text.charAt(pos)==br)
				return pos;
			pos++;
			p--;
		}
		return pos;
	}
	
	private PC_CoordI calcStringIndexToSelectCoords(int index){
		int xx=0, yy=0;
		for(int i=0; i<index; i++){
			if(i>=text.length())
				break;
			if(text.charAt(i)==br){
				yy++;
				xx=-1;
			}
			xx++;
		}
		return new PC_CoordI(xx, yy);
	}
	
	private void addKey(char c) {
		int s = calcSelectCoordsToStringIndex(mouseSelectStart), e = calcSelectCoordsToStringIndex(mouseSelectEnd);
		if (s > e) {
			int t = e;
			e = s;
			s = t;
		}
		String s1 = text.substring(0, s);
		String s2 = text.substring(e);
		text = s1 + c + s2;
		e++;
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
		if(e<=0)
			return;
		String s1 = text.substring(0, e - 1);
		String s2 = text.substring(e);
		text = s1 + s2;
		mouseSelectEnd.setTo(calcStringIndexToSelectCoords(e-1));
		mouseSelectStart.setTo(mouseSelectEnd);
	}

	private void key_delete() {
		if (!(mouseSelectStart.x == mouseSelectEnd.x && mouseSelectStart.y == mouseSelectEnd.y)) {
			deleteSelected();
			return;
		}
		int p = calcSelectCoordsToStringIndex(mouseSelectEnd);
		if (p >= text.length()) return;
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
	}

	private int shownLines(){
		return (size.y - 12)/PC_Utils.mc().fontRenderer.FONT_HEIGHT;
	}
	
	private void setScrollToCursor(){
		int cy = mouseSelectEnd.y - scroll.y;
		if(cy<0){
			scroll.y = mouseSelectEnd.y;
		}else if(cy>=shownLines()){
			scroll.y = mouseSelectEnd.y - shownLines() + 1;
		}
		String line = getLine(mouseSelectEnd.y);
		int cxs = getStringWidth(line.substring(0, mouseSelectEnd.x));
		int cxb = mouseSelectEnd.x>0?getStringWidth(line.substring(mouseSelectEnd.x-1, mouseSelectEnd.x)):0;
		int cx = cxs - scroll.x;
		if(cx<=cxb){
			scroll.x = cxs - cxb;
		}else if(cx>=size.x-15){
			scroll.x = cxs - size.x + 15;
		}
		
	}

	@Override
	public boolean keyTyped(char c, int key) {
		int p;
		if (!enabled || !hasFocus) return false;
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
				if (p > 0)
					mouseSelectEnd.setTo(calcStringIndexToSelectCoords(p - 1));
				if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) mouseSelectStart.setTo(mouseSelectEnd);
				setScrollToCursor();
				return true;
			case Keyboard.KEY_RIGHT:
				p = calcSelectCoordsToStringIndex(mouseSelectEnd);
				if (p < text.length())
					mouseSelectEnd.setTo(calcStringIndexToSelectCoords(p + 1));
				if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)))  mouseSelectStart.setTo(mouseSelectEnd);
				setScrollToCursor();
				return true;
			case Keyboard.KEY_UP:
				mouseSelectEnd.setTo(lineUp());
				if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) mouseSelectStart.setTo(mouseSelectEnd);
				setScrollToCursor();
				return true;
			case Keyboard.KEY_DOWN:
				mouseSelectEnd.setTo(lineDown());
				if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) mouseSelectStart.setTo(mouseSelectEnd);
				setScrollToCursor();
				return true;
			default:
				if (ChatAllowedCharacters.isAllowedCharacter(c)){
					addKey(c);
					setScrollToCursor();
					return true;
				}
				return false;
		}		
	}

	@Override
	public void mouseWheel(int i) {
	if(i>0){
		mouseSelectEnd.setTo(lineUp());
		if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) mouseSelectStart.setTo(mouseSelectEnd);
		setScrollToCursor();
	}
	if(i<0){
		mouseSelectEnd.setTo(lineDown());
		if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) mouseSelectStart.setTo(mouseSelectEnd);
		setScrollToCursor();
	}
	}
	
}
