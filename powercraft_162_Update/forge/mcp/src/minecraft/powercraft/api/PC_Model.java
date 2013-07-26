package powercraft.api;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public abstract class PC_Model extends ModelBase {

	protected static final float SCALE = 0.0625F;
	
	public abstract void setEnabled(int index, boolean enabled);
	
	public abstract void renderAll();
	
	protected void render(ModelRenderer modelRenderer){
		modelRenderer.render(SCALE);
	}
	
}
