package powercraft.projector;

import powercraft.api.tileentity.PC_ITileEntityRenderer;
import powercraft.api.tileentity.PC_TileEntity;

public class PCpj_TileEntityProjector extends PC_TileEntity implements PC_ITileEntityRenderer {

	public int framebufferID;
	public int depthRenderBufferID;
	
	@Override
	public void invalidate() {
		super.invalidate();
		PCpj_App.sDestryFrameBuffer(this);
	}

	@Override
	public void validate() {
		super.validate();
		PCpj_App.sCreateFrameBuffer(this);
	}

	@Override
	public void renderTileEntityAt(double x, double y, double z, float timeStamp) {
		if(PCpj_EntityRenderer.canAdd){
			PCpj_EntityRenderer.toRender.add(this);
		}
	}

}
