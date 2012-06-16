package net.minecraft.src;

import org.lwjgl.input.Keyboard;

public class PC_GresTextEdit extends PC_GresWidget {

	public static final int WORD = 0, NUMBER = 1, UNSIGNED_NUMBER = 2;

	private int maxChars;
	private int mouseSelectStart = 0;
	private int mouseSelectEnd = 0;
	private boolean mousePressed = false;
	private int type = WORD;

	public PC_GresTextEdit(String label, int chars) {
		super((chars + 1) * 10, PC_Utils.mc().fontRenderer.FONT_HEIGHT + 12, label);
		maxChars = chars;
		canAddWidget = false;
		color[textColorEnabled] = 0xffffffff;
		color[textColorShadowEnabled] = 0xff383838;
		color[textColorDisabled] = 0xffffffff;
		color[textColorShadowDisabled] = 0xff383838;
	}

	public PC_GresTextEdit(String label, int chars, int type) {
		super((chars + 1) * 10, PC_Utils.mc().fontRenderer.FONT_HEIGHT + 12, label);
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

		drawHorizontalLine(offsetPos.x + pos.x, offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y, 0xffA0A0A0);
		drawHorizontalLine(offsetPos.x + pos.x, offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y + size.y - 1, 0xffA0A0A0);

		drawVerticalLine(offsetPos.x + pos.x, offsetPos.y + pos.y, offsetPos.y + pos.y + size.y - 1, 0xffA0A0A0);
		drawVerticalLine(offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y, offsetPos.y + pos.y + size.y - 1, 0xffA0A0A0);

		drawRect(offsetPos.x + pos.x + 1, offsetPos.y + pos.y + 1, offsetPos.x + pos.x + size.x - 1, offsetPos.y + pos.y + size.y - 1, 0xff000000);

		if (label.length() > maxChars) label = label.substring(0, maxChars);

		if (mouseSelectStart != mouseSelectEnd && hasFocus) {
			int s = mouseSelectStart, e = mouseSelectEnd;
			if (s > e) {
				e = mouseSelectStart;
				s = mouseSelectEnd;
			}

			drawRect(offsetPos.x + pos.x + getStringLength(label.substring(0, s)) + 6, offsetPos.y + pos.y + 4,
					offsetPos.x + pos.x + getStringLength(label.substring(0, e)) + 6, offsetPos.y + pos.y + size.y - 5, 0xff3399FF);

		}

		drawString(label, offsetPos.x + pos.x + 6, offsetPos.y + pos.y + (size.y - 8) / 2);

		if (mouseSelectEnd == label.length()) {
			if (hasFocus && (cursorCounter / 6) % 2 == 0) drawString("_", offsetPos.x + pos.x + getStringLength(label) + 6, offsetPos.y + pos.y
					+ (size.y - 8) / 2);
		} else if (hasFocus && (cursorCounter / 6) % 2 == 0) drawVerticalLine(
				offsetPos.x + pos.x + getStringLength(label.substring(0, mouseSelectEnd)) + 5, offsetPos.y + pos.y + 3, offsetPos.y + pos.y + size.y - 5,
				color[enabled ? textColorEnabled : textColorDisabled]);

	}

	@Override
	public boolean mouseOver(PC_CoordI mpos) {
		return true;
	}

	private int getMousePositionInString(int x) {
		int charSize;
		x -= 6;
		for (int i = 0; i < label.length(); i++) {
			charSize = getStringLength("" + label.charAt(i));
			if (x - charSize / 2 < 0) return i;
			x -= charSize;
		}
		return label.length();
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
		String s1 = label.substring(0, s);
		String s2 = label.substring(e);
		if ((s1 + c + s2).length() > maxChars) return;
		label = s1 + c + s2;
		mouseSelectEnd += 1;
		mouseSelectStart = mouseSelectEnd;
	}

	private void deleteSelected() {
		int s = mouseSelectStart, e = mouseSelectEnd;
		if (s > e) {
			e = mouseSelectStart;
			s = mouseSelectEnd;
		}
		String s1 = label.substring(0, s);
		String s2 = label.substring(e);
		label = s1 + s2;
		mouseSelectEnd = s;
		mouseSelectStart = s;
	}

	private void key_backspace() {
		if (mouseSelectStart != mouseSelectEnd) {
			deleteSelected();
			return;
		}
		if (mouseSelectEnd <= 0) return;
		String s1 = label.substring(0, mouseSelectEnd - 1);
		String s2 = label.substring(mouseSelectEnd);
		label = s1 + s2;
		mouseSelectEnd -= 1;
		mouseSelectStart = mouseSelectEnd;
	}

	private void key_delete() {
		if (mouseSelectStart != mouseSelectEnd) {
			deleteSelected();
			return;
		}
		if (mouseSelectEnd >= label.length()) return;
		String s1 = label.substring(0, mouseSelectEnd);
		String s2 = label.substring(mouseSelectEnd + 1);
		label = s1 + s2;
	}

	private String getSelect() {
		int s = mouseSelectStart, e = mouseSelectEnd;
		if (s > e) {
			e = mouseSelectStart;
			s = mouseSelectEnd;
		}
		return label.substring(s, e);
	}

	private void setSelected(String stri) {
		int s = mouseSelectStart, e = mouseSelectEnd;
		if (s > e) {
			e = mouseSelectStart;
			s = mouseSelectEnd;
		}
		String s1 = label.substring(0, s);
		String s2 = label.substring(e);
		String ss = "";
		switch (type) {
			case UNSIGNED_NUMBER:
				for (int i = 0; i < stri.length(); i++)
					if (Character.isDigit(Character.valueOf(stri.charAt(i)))) ss += stri.charAt(i);
				break;
				
			case NUMBER:
				if (label.length() > 0) if (label.charAt(0) == '-') if (mouseSelectStart == 0 && mouseSelectEnd == 0) break;
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
		label = s1 + ss + s2;
		mouseSelectEnd = s + ss.length();
		mouseSelectStart = s;
	}

	@Override
	public void keyTyped(char c, int key) {
		if (!enabled || !hasFocus) return;
		switch (c) {
			case 3:
				GuiScreen.setClipboardString(getSelect());
				return;

			case 22:
				setSelected(GuiScreen.getClipboardString());
				return;

			case 24:
				GuiScreen.setClipboardString(getSelect());
				deleteSelected();
				return;
		}
		switch (key) {
			case Keyboard.KEY_BACK:
				key_backspace();
				break;
			case Keyboard.KEY_DELETE:
				key_delete();
				break;
			case Keyboard.KEY_LEFT:
				if (mouseSelectEnd > 0) {
					mouseSelectEnd -= 1;
					if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) mouseSelectStart = mouseSelectEnd;

				}
				break;
			case Keyboard.KEY_RIGHT:
				if (mouseSelectEnd < label.length()) {
					mouseSelectEnd += 1;
					if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) mouseSelectStart = mouseSelectEnd;
				}
				break;
			default:
				switch (type) {
					case UNSIGNED_NUMBER:
						if (Character.isDigit(Character.valueOf(c))) addKey(c);
						break;
					case NUMBER:
						if (label.length() > 0) if (label.charAt(0) == '-') if (mouseSelectStart == 0 && mouseSelectEnd == 0) break;
						if (Character.isDigit(Character.valueOf(c))) addKey(c);
						else if ((mouseSelectStart == 0 || mouseSelectEnd == 0) && key == Keyboard.KEY_MINUS) addKey(c);
						break;
					default:
						if (ChatAllowedCharacters.isAllowedCharacter(c)) addKey(c);
						break;
				}
				break;
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
