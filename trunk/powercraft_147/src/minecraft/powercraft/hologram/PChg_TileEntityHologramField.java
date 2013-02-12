package powercraft.hologram;

import powercraft.management.PC_ITileEntityRenderer;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_VecI;

public class PChg_TileEntityHologramField extends PC_TileEntity implements PC_ITileEntityRenderer{

	public static final String OFFSETS = "offsets";
	
	public int glList;
	public int tick;
	public boolean update=true;
	
	public PChg_TileEntityHologramField(){
		setData(OFFSETS, new PC_VecI());
	}
	
	public PC_VecI getOffset(){
		return (PC_VecI)getData(OFFSETS);
	}
	
	public void setOffset(PC_VecI coordOffset) {
		setData(OFFSETS, coordOffset.copy());
		tick=0;
	}
	
	@Override
	protected void dataChange(String key, Object value){
		if(key.equals(OFFSETS))
			tick=0;
	}
	
	@Override
	public void updateEntity() {
		if(tick%20==0){
			update=true;
		}
		tick++;
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {
		PChg_App.getInstance().renderHologramField(this, x, y, z);
	}
	
}