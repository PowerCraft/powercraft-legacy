package powercraft.machines;

public class PCma_ClientProxy extends PCma_CommonProxy {

	@Override
	public Object[] registerGuis() {
		return new Object[]{
				"AutomaticWorkbench", PCma_GuiAutomaticWorkbench.class	
			};
	}

}
