package net.minecraft.src;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PC_GresRadioButton extends PC_GresWidget {
	
	public static class PC_GresRadioGroup extends HashSet<PC_GresRadioButton>{
		
	}

	private static final int WIDTH = 11;
	private boolean checked = false;
	private PC_GresRadioGroup radioGroup;

	public PC_GresRadioButton(String title, PC_GresRadioGroup group) {
		super(title);
		canAddWidget = false;
		color[textColorEnabled] = 0x000000;
		color[textColorShadowEnabled] = 0xAAAAAA;
		color[textColorDisabled] = 0x707070;
		color[textColorShadowDisabled] = 0xAAAAAA;
		
		radioGroup = group;
		radioGroup.add(this);
	}

	public boolean isChecked() {
		return checked;
	}

	public PC_GresRadioButton check() {
		checked = true;
		
		for(PC_GresRadioButton btn : radioGroup){
			if(btn != this) btn.uncheck();
		}
		
		return this;
	}

	private void uncheck() {
		checked = false;		
	}

	@Override
	public PC_CoordI calcSize() {
		FontRenderer fontRenderer = getFontRenderer();
		size.setTo(fontRenderer.getStringWidth(label), fontRenderer.FONT_HEIGHT).add(WIDTH + 3, 0);

		if (size.y < WIDTH) size.y = WIDTH;
		return size.copy();
	}

	@Override
	public PC_CoordI getMinSize() {
		return calcSize();
	}

	@Override
	public void calcChildPositions() {

	}

	@Override
	protected void render(PC_CoordI offsetPos) {
		int c1 = 0xff555555, c2 = 0xffffffff, c3 = 0xff8b8b8b;

		drawHorizontalLine(offsetPos.x + pos.x + 3, offsetPos.x + pos.x + WIDTH - 4, offsetPos.y + pos.y, c1);
		drawHorizontalLine(offsetPos.x + pos.x + 3, offsetPos.x + pos.x + WIDTH - 4, offsetPos.y + pos.y + WIDTH - 1, c2);

		drawVerticalLine(offsetPos.x + pos.x, offsetPos.y + pos.y + 2, offsetPos.y + pos.y + WIDTH - 3, c1);
		drawVerticalLine(offsetPos.x + pos.x + WIDTH - 1, offsetPos.y + pos.y + 2, offsetPos.y + pos.y + WIDTH - 3, c2);

		drawPoint(new PC_CoordI(offsetPos.x + pos.x + 1, offsetPos.y + pos.y + 2), c1);
		drawPoint(new PC_CoordI(offsetPos.x + pos.x + 2, offsetPos.y + pos.y + 1), c1);

		drawPoint(new PC_CoordI(offsetPos.x + pos.x + 1, offsetPos.y + pos.y + WIDTH - 3), c3);
		drawPoint(new PC_CoordI(offsetPos.x + pos.x + 2, offsetPos.y + pos.y + WIDTH - 2), c3);

		drawPoint(new PC_CoordI(offsetPos.x + pos.x + WIDTH - 2, offsetPos.y + pos.y + 2), c3);
		drawPoint(new PC_CoordI(offsetPos.x + pos.x + WIDTH - 3, offsetPos.y + pos.y + 1), c3);

		drawPoint(new PC_CoordI(offsetPos.x + pos.x + WIDTH - 2, offsetPos.y + pos.y + WIDTH - 3), c2);
		drawPoint(new PC_CoordI(offsetPos.x + pos.x + WIDTH - 3, offsetPos.y + pos.y + WIDTH - 2), c2);

		if (checked) drawString("o", offsetPos.x + pos.x + 3, offsetPos.y + pos.y + 1);

		drawString(label, offsetPos.x + pos.x + WIDTH + 3, offsetPos.y + pos.y + 1);
	}

	@Override
	public boolean mouseOver(PC_CoordI mpos) {
		return true;
	}

	@Override
	public boolean mouseClick(PC_CoordI mpos, int key) {
		if (!enabled) return false;
		if (key != -1) {
			check();
			return true;
		}
		return false;
	}

	@Override
	public void mouseMove(PC_CoordI mpos) {

	}

	@Override
	public void keyTyped(char c, int key) {

	}

}
