package net.minecraft.src;

import java.util.List;

/**
 * Resizable GUI multi-line text label with fixed width and dynamic height
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PC_GresLabelMultiline extends PC_GresWidget {

	private int minRows = 1;

	/**
	 * Multiline label
	 * 
	 * @param text text
	 * @param width fixed widget width (text will wrap)
	 */
	public PC_GresLabelMultiline(String text, int width) {
		super(width, 10, text);
		canAddWidget = false;
		alignH = PC_GresAlign.LEFT;
	}

	/**
	 * @return the minimal no. rows
	 */
	public int getMinRows() {
		return minRows;
	}

	/**
	 * @param minRows set minimal no. of rows
	 * @return this
	 */
	public PC_GresLabelMultiline setMinRows(int minRows) {
		this.minRows = minRows;
		return this;
	}

	@Override
	public PC_CoordI calcSize() {
		getMinSize();
		if (size.y < minSize.y) {
			size.y = minSize.y;
		}
		if (size.x < minSize.x) {
			size.x = minSize.x;
		}
		return size.copy();
	}

	@Override
	protected void render(PC_CoordI offsetPos) {

		FontRenderer fontRenderer = getFontRenderer();

		int cnt = 0;

		String[] lines_nl = text.split("\n");

		for (String s : lines_nl) {
			s.trim();
			if (s.length() > 0) {
				@SuppressWarnings("unchecked")
				List<String> lines = fontRenderer.listFormattedStringToWidth(s, getMinSize().x);

				for (String ss : lines) {
					ss.trim();
					if (ss.length() > 0) {
						int wid = getStringWidth(ss);
						int xstart = offsetPos.x + pos.x;

						switch (alignH) {
							case LEFT:
								break;
							case CENTER:
								xstart = xstart + size.x / 2 - wid / 2;
								break;
							case RIGHT:
								xstart = xstart + size.x - wid;
						}

						drawString(ss, xstart, offsetPos.y + pos.y + (fontRenderer.FONT_HEIGHT + 1) * cnt);
						cnt++;
					}
				}
			}
		}

	}

	@Override
	public boolean mouseOver(PC_CoordI mpos) {
		return false;
	}

	@Override
	public boolean mouseClick(PC_CoordI mpos, int key) {
		return false;
	}

	@Override
	public boolean keyTyped(char c, int key) {
		return false;
	}

	@Override
	public void calcChildPositions() {

	}

	@Override
	public void mouseMove(PC_CoordI mpos) {

	}

	@Override
	public PC_CoordI getMinSize() {

		FontRenderer fontRenderer = getFontRenderer();

		int cnt = 0;

		String[] lines_nl = text.split("\n");

		for (String s : lines_nl) {
			s.trim();
			if (s.length() > 0) {
				@SuppressWarnings("unchecked")
				List<String> lines = fontRenderer.listFormattedStringToWidth(s, minSize.x);

				for (String ss : lines) {
					ss.trim();
					if (s.length() > 0) {
						cnt++;
					}
				}
			}
		}

		minSize.setTo(minSize.x, (fontRenderer.FONT_HEIGHT + 1) * Math.max(minRows, cnt));

		return minSize;
	}

	@Override
	public void mouseWheel(int i) {}

	@Override
	public void addedToWidget() {}
}
