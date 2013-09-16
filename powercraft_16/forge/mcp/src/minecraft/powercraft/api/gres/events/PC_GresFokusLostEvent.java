package powercraft.api.gres.events;

import powercraft.api.gres.PC_GresComponent;

public class PC_GresFokusLostEvent extends PC_GresConsumeableEvent {
	
	private final PC_GresComponent newFocusedComponent;
	
	public PC_GresFokusLostEvent(PC_GresComponent component, PC_GresComponent newFocusedComponent) {
		super(component);
		this.newFocusedComponent = newFocusedComponent;
	}
	
	public PC_GresComponent getNewFocusedComponent(){
		return newFocusedComponent;
	}
	
}
