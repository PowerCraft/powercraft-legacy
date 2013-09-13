package powercraft.api.gres;

import powercraft.api.PC_RectI;
import powercraft.api.PC_Vec2I;

public class PC_GresCheckBox extends PC_GresComponent {

	private static final String textureName[] = {"CheckBox", "CheckBoxChecked"};
	
	private boolean state;
	
	public PC_GresCheckBox(String title){
		setText(title);
	}
	
	@Override
	protected PC_Vec2I calculateMinSize() {
		return new PC_Vec2I(9+fontRenderer.getStringWidth(text)+(text!=null&&!text.isEmpty()?1:0), 9);
	}

	@Override
	protected PC_Vec2I calculateMaxSize() {
		return new PC_Vec2I(9+fontRenderer.getStringWidth(text)+(text!=null&&!text.isEmpty()?1:0), fontRenderer.FONT_HEIGHT);
	}

	@Override
	protected PC_Vec2I calculatePrefSize() {
		return calculateMaxSize();
	}
	
	@Override
	protected void paint(PC_RectI scissor, float timeStamp) {
		drawTexture(textureName[state?1:0], 0, 0, 9, 9);
		drawString(text, 10, 0, rect.width - 9, rect.height, PC_GresAlign.H.CENTER, PC_GresAlign.V.CENTER, false);
	}

	@Override
	protected void handleMouseButtonClick(PC_Vec2I mouse, int buttons, int eventButton) {
		state=!state;
	}
	
}
