package powercraft.api.gres;


import java.util.List;

import powercraft.api.PC_RectI;
import powercraft.api.PC_Vec2I;


public class PC_GresWindow extends PC_GresContainer {

	private static final String textureName = "Window";

	private List<PC_GresWindowSideTab> sideTabs;
	
	public PC_GresWindow(String title) {

		frame.setTo(new PC_RectI(4, 4 + fontRenderer.FONT_HEIGHT + 2, 4, 4));
		setText(title);
	}


	@Override
	protected PC_Vec2I calculateMinSize() {

		return getTextureMinSize(textureName).max(fontRenderer.getStringWidth(text) + 8, 0);
	}


	@Override
	protected PC_Vec2I calculateMaxSize() {

		return new PC_Vec2I(-1, -1);
	}


	@Override
	protected PC_Vec2I calculatePrefSize() {

		return new PC_Vec2I(-1, -1);
	}


	@Override
	protected void paint(PC_RectI scissor, float timeStamp) {

		drawTexture(textureName, 0, 0, rect.width, rect.height);
		drawString(text, 4, 4, rect.width - 4, PC_GresAlign.H.CENTER, false);
	}

}
