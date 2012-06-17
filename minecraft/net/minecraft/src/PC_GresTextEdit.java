package net.minecraft.src;

import org.lwjgl.input.Keyboard;

/**
 * Text editor
 * 
 * @author XOR19
 * @copy (c) 2012
 *
 */
public class PC_GresTextEdit extends PC_GresWidget {

	/**
	 * Input type for Text Edit field.
	 * 
	 * @author MightyPork
	 * @copy (c) 2012
	 *
	 */
	public enum PC_GresInputType{
		/** accept all characters */
		TEXT,
		/** accept signed number */
		INT,
		/** accept unsigned number */
		UNSIGNED_INT,
		/** accept signed number with dot */
		SIGNED_FLOAT,
		/** accept unsigned number with dot */
		UNSIGNED_FLOAT;
	}

	private int maxChars;
	private int mouseSelectStart = 0;
	private int mouseSelectEnd = 0;
	private boolean mousePressed = false;
	private PC_GresInputType type = PC_GresInputType.TEXT;

	/**
	 * Text Edit
	 * @param label text
	 * @param chars max number of characters
	 */
	public PC_GresTextEdit(String label, int chars) {
		super((chars + 1) * 10, PC_Utils.mc().fontRenderer.FONT_HEIGHT + 12, label);
		maxChars = chars;
		canAddWidget = false;
		color[textColorEnabled] = 0xffffffff;
		color[textColorShadowEnabled] = 0xff383838;
		color[textColorDisabled] = 0xffffffff;
		color[textColorShadowDisabled] = 0xff383838;
	}

	/**
	 * Text Edit
	 * @param initText text
	 * @param chars max no. of characters
	 * @param type input type allowed.
	 */
	public PC_GresTextEdit(String initText, int chars, PC_GresInputType type) {
		super((chars + 1) * 10, PC_Utils.mc().fontRenderer.FONT_HEIGHT + 12, initText);
		maxChars = chars;
		canAddWidget = false;
		color[textColorEnabled] = 0xffffffff;
		color[textColorShadowEnabled] = 0xff383838;
		color[textColorDisabled] = 0xffffffff;
		color[textColorShadowDisabled] = 0xff383838;
		this.type = type;
	}

	@Override
	public PC_CoordI calcSize() {
		return size.copy();
	}

	@Override
	public void calcChildPositions() {}

	@Override
	protected void render(PC_CoordI offsetPos) {
		
		if(mouseSelectEnd > text.length()) mouseSelectEnd = text.length();
		if(mouseSelectStart > text.length()) mouseSelectStart = text.length();

		drawHorizontalLine(offsetPos.x + pos.x, offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y, 0xffA0A0A0);
		drawHorizontalLine(offsetPos.x + pos.x, offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y + size.y - 1, 0xffA0A0A0);

		drawVerticalLine(offsetPos.x + pos.x, offsetPos.y + pos.y, offsetPos.y + pos.y + size.y - 1, 0xffA0A0A0);
		drawVerticalLine(offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y, offsetPos.y + pos.y + size.y - 1, 0xffA0A0A0);

		drawRect(offsetPos.x + pos.x + 1, offsetPos.y + pos.y + 1, offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y + size.y - 1, 0xff000000);

		if (text.length() > maxChars) text = text.substring(0, maxChars);

		if (mouseSelectStart != mouseSelectEnd && hasFocus) {
			int s = mouseSelectStart, e = mouseSelectEnd;
			if (s > e) {
				e = mouseSelectStart;
				s = mouseSelectEnd;
			}

			drawRect(offsetPos.x + pos.x + getStringWidth(text.substring(0, s)) + 6, offsetPos.y + pos.y + 4,
					offsetPos.x + pos.x + getStringWidth(text.substring(0, e)) + 6, offsetPos.y + pos.y + size.y - 5, 0xff3399FF);

		}

		drawString(text, offsetPos.x + pos.x + 6, offsetPos.y + pos.y + (size.y - 8) / 2);

		if (mouseSelectEnd == text.length()) {
			if (hasFocus && (cursorCounter / 6) % 2 == 0) drawString("_", offsetPos.x + pos.x + getStringWidth(text) + 6, offsetPos.y + pos.y
					+ (size.y - 8) / 2);
		} else if (hasFocus && (cursorCounter / 6) % 2 == 0) drawVerticalLine(
				offsetPos.x + pos.x + getStringWidth(text.substring(0, mouseSelectEnd)) + 5, offsetPos.y + pos.y + 3, offsetPos.y + pos.y + size.y - 5,
				color[enabled ? textColorEnabled : textColorDisabled]);

	}

	@Override
	public boolean mouseOver(PC_CoordI mpos) {
		return true;
	}

	private int getMousePositionInString(int x) {
		int charSize;
		x -= 6;
		for (int i = 0; i < text.length(); i++) {
			charSize = getStringWidth("" + text.charAt(i));
			if (x - charSize / 2 < 0) return i;
			x -= charSize;
		}
		return text.length();
	}

	@Override
	public boolean mouseClick(PC_CoordI mpos, int key) {
		mousePressed = false;
		if (!enabled) return false;
		if (key != -1) {
			mouseSelectStart = getMousePositionInString(mpos.x);
			mouseSelectEnd = getMousePositionInString(mpos.x);
			mousePressed = true;
			return true;
		}
		return false;
	}

	private void addKey(char c) {
		int s = mouseSelectStart, e = mouseSelectEnd;
		if (s > e) {
			e = mouseSelectStart;
			s = mouseSelectEnd;
		}
		String s1 = text.substring(0, s);
		String s2 = text.substring(e);
		if ((s1 + c + s2).length() > maxChars) return;
		text = s1 + c + s2;
		mouseSelectEnd += 1;
		mouseSelectStart = mouseSelectEnd;
	}

	private void deleteSelected() {
		int s = mouseSelectStart, e = mouseSelectEnd;
		if (s > e) {
			e = mouseSelectStart;
			s = mouseSelectEnd;
		}
		String s1 = text.substring(0, s);
		String s2 = text.substring(e);
		text = s1 + s2;
		mouseSelectEnd = s;
		mouseSelectStart = s;
	}

	private void key_backspace() {
		if (mouseSelectStart != mouseSelectEnd) {
			deleteSelected();
			return;
		}
		if (mouseSelectEnd <= 0) return;
		String s1 = text.substring(0, mouseSelectEnd - 1);
		String s2 = text.substring(mouseSelectEnd);
		text = s1 + s2;
		mouseSelectEnd -= 1;
		mouseSelectStart = mouseSelectEnd;
	}

	private void key_delete() {
		if (mouseSelectStart != mouseSelectEnd) {
			deleteSelected();
			return;
		}
		if (mouseSelectEnd >= text.length()) return;
		String s1 = text.substring(0, mouseSelectEnd);
		String s2 = text.substring(mouseSelectEnd + 1);
		text = s1 + s2;
	}

	private String getSelect() {
		int s = mouseSelectStart, e = mouseSelectEnd;
		if (s > e) {
			e = mouseSelectStart;
			s = mouseSelectEnd;
		}
		return text.substring(s, e);
	}

	private void setSelected(String stri) {
		int s = mouseSelectStart, e = mouseSelectEnd;
		if (s > e) {
			e = mouseSelectStart;
			s = mouseSelectEnd;
		}
		String s1 = text.substring(0, s);
		String s2 = text.substring(e);
		String ss = "";
		switch (type) {
			case UNSIGNED_INT:
				for (int i = 0; i < stri.length(); i++)
					if (Character.isDigit(Character.valueOf(stri.charAt(i)))) ss += stri.charAt(i);
				break;
				
			case INT:
				if (text.length() > 0) if (text.charAt(0) == '-') if (mouseSelectStart == 0 && mouseSelectEnd == 0) break;
				for (int i = 0; i < stri.length(); i++) {
					if (i == 0) if (stri.charAt(0) == '-') if (s == 0) ss += stri.charAt(i);
					if (Character.isDigit(Character.valueOf(stri.charAt(i)))) ss += stri.charAt(i);
				}
				break;
				
			default:
				for (int i = 0; i < stri.length(); i++)
					if (ChatAllowedCharacters.isAllowedCharacter(stri.charAt(i))) ss += stri.charAt(i);
				break;
		}
		if ((s1 + ss + s2).length() > maxChars) return;
		text = s1 + ss + s2;
		mouseSelectEnd = s + ss.length();
		mouseSelectStart = s;
	}

	@Override
	public boolean keyTyped(char c, int key) {
		if (!enabled || !hasFocus) return false;
		switch (c) {
			case 3:
				GuiScreen.setClipboardString(getSelect());
				return true;

			case 22:
				setSelected(GuiScreen.getClipboardString());
				return true;

			case 24:
				GuiScreen.setClipboardString(getSelect());
				deleteSelected();
				return true;
		}
		switch (key) {
			case Keyboard.KEY_RETURN:
				return true;
			case Keyboard.KEY_BACK:
				key_backspace();
				return true;
			case Keyboard.KEY_DELETE:
				key_delete();
				return true;
			case Keyboard.KEY_LEFT:
				if (mouseSelectEnd > 0) {
					mouseSelectEnd -= 1;
					if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) mouseSelectStart = mouseSelectEnd;

				}
				return true;
			case Keyboard.KEY_RIGHT:
				if (mouseSelectEnd < text.length()) {
					mouseSelectEnd += 1;
					if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) mouseSelectStart = mouseSelectEnd;
				}
				return true;
			default:
				switch (type) {
					case UNSIGNED_INT:
						if (Character.isDigit(Character.valueOf(c))){
							addKey(c);
							return true;
						}else{
							return false;
						}
						
					case INT:
						//writing before minus
						if (text.length() > 0 && text.charAt(0) == '-' && mouseSelectStart == 0 && mouseSelectEnd == 0) return true;
						
						if (Character.isDigit(Character.valueOf(c))){
							addKey(c);
							return true;
						}else if ((mouseSelectStart == 0 || mouseSelectEnd == 0) && c == '-'){
							addKey(c);
							return true;
						}
						return false;
						
					case SIGNED_FLOAT:
						
						if (c == '.'){
							if (mouseSelectStart == 0 || mouseSelectEnd == 0) return true;
							if (text.length() > 0 && (mouseSelectStart == 1 || mouseSelectEnd == 1) && text.charAt(0) == '-') return true;
							if (text.length() > 0 && text.contains(".")) return true;
							addKey(c);	
							return true;
						}
						
						if (text.length() > 0 && text.charAt(0) == '-' && mouseSelectStart == 0 && mouseSelectEnd == 0){
							return true;
						}
						
						if (Character.isDigit(Character.valueOf(c))){
							addKey(c);
							return true;
						}else if ((mouseSelectStart == 0 || mouseSelectEnd == 0) && c == '-'){
							addKey(c);
							return true;
						}
						
						return false;
						
					case UNSIGNED_FLOAT:
						
						if (c == '.'){
							if (mouseSelectStart == 0 || mouseSelectEnd == 0) return true;
							if (text.length() > 0 && text.contains(".")) return true;
							addKey(c);	
							return true;
						}
						
						if (Character.isDigit(Character.valueOf(c))){
							addKey(c);
							return true;
						}
						
						return false;
						
					case TEXT:
					default:
						if (ChatAllowedCharacters.isAllowedCharacter(c)){
							addKey(c);
							return true;
						}
						return false;
				}
		}		
	}

	@Override
	public void mouseMove(PC_CoordI mpos) {
		if (mousePressed) mouseSelectEnd = getMousePositionInString(mpos.x);
	}

	@Override
	public PC_CoordI getMinSize() {
		return new PC_CoordI((maxChars + 1) * 10, getFontRenderer().FONT_HEIGHT + 12);
	}

}
