package powercraft.api.gres;

import powercraft.api.PC_RectI;
import powercraft.api.PC_Vec2I;

public class PC_GresRadioButton extends PC_GresComponent {
	private static PC_Vec2I minSize;
	private boolean state;
	
	@Override
	protected PC_Vec2I calculateMinSize() {
		if(minSize==null)minSize=getTextureMinSize("RadioButton");
		return minSize.add(fontRenderer.getStringWidth(text), 0);
	}

	@Override
	protected PC_Vec2I calculateMaxSize() {
		if(minSize==null)minSize=getTextureMinSize("RadioButton");
		return new PC_Vec2I(-1, minSize.y);
	}

	@Override
	protected PC_Vec2I calculatePrefSize() {
		if(minSize==null)minSize=getTextureMinSize("RadioButton");
		return minSize.add(fontRenderer.getStringWidth(text), 0);
	}

	@Override
	protected void paint(PC_RectI scissor, float timeStamp) {
		draw(textureName, 0, 0, rect.width, rect.height);
	}

}
