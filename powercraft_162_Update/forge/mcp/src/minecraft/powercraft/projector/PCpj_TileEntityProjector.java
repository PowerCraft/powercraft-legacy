package powercraft.projector;

import java.nio.FloatBuffer;

import powercraft.api.tileentity.PC_ITileEntityRenderer;
import powercraft.api.tileentity.PC_TileEntity;

public class PCpj_TileEntityProjector extends PC_TileEntity {

	public static FloatBuffer projectionsMatrix;
	public static FloatBuffer modelviewMatrix;
	
	@Override
	public void invalidate() {
		super.invalidate();
		if(worldObj.isRemote)
			PCpj_ProjectorRenderer.toRender.remove(this);
	}

	@Override
	public void validate() {
		super.validate();
		if(worldObj.isRemote)
			PCpj_ProjectorRenderer.toRender.add(this);
	}

}
