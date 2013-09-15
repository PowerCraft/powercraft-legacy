package powercraft.api.gres;

import powercraft.api.PC_RectI;
import powercraft.api.PC_Vec2I;
import powercraft.api.gres.events.PC_GresEvent;
import powercraft.api.gres.events.PC_GresFokusLostEvent;
import powercraft.api.gres.events.PC_IGresEventListener;

public class PC_GresComboBox extends PC_GresComponent {

	private PC_GresFrame frame;
	
	@Override
	protected PC_Vec2I calculateMinSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected PC_Vec2I calculateMaxSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected PC_Vec2I calculatePrefSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void paint(PC_RectI scissor, double scale, int displayHeight, float timeStamp) {
		
	}

	@Override
	protected void handleMouseButtonDown(PC_Vec2I mouse, int buttons, int eventButton) {
		frame = new PC_GresFrame();
		ComboBoxEventListener cbel = new ComboBoxEventListener();
		
		frame.addEventListener(cbel);
		getGuiHandler().add(frame);
	}
	
	@Override
	protected void setParent(PC_GresContainer parent) {
		if(frame!=null){
			getGuiHandler().remove(frame);
			frame = null;
		}
		super.setParent(parent);
	}

	private class ComboBoxEventListener implements PC_IGresEventListener{

		@Override
		public void onEvent(PC_GresEvent event) {
			if(event.getComponent()==frame){
				if(event instanceof PC_GresFokusLostEvent){
					getGuiHandler().remove(frame);
					frame = null;
				}
			}
		}
		
	}
	
}
