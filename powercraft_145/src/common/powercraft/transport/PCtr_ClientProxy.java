package powercraft.transport;


public class PCtr_ClientProxy extends PCtr_CommonProxy {

	@Override
	public Object[] registerGuis() {
		return new Object[]{
				"SeperationBelt", PCtr_GuiSeparationBelt.class,
				"EjectionBelt", PCtr_GuiEjectionBelt.class
		};
	}
	
}
