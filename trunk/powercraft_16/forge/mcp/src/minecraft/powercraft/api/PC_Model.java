package powercraft.api;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * 
 * the base model for PowerCraft TileEntity Models
 * 
 * @author XOR
 *
 */
public abstract class PC_Model extends ModelBase {

	/**
	 * the render scale, default 0.0625F
	 */
	protected static final float SCALE = 0.0625F;
	
	/**
	 * sets a bodypart enabled/disabled
	 * @param index the body part index
	 * @param enabled the state
	 */
	public abstract void setEnabled(int index, boolean enabled);
	
	/**
	 * render all enabled bodyparts
	 */
	public abstract void renderAll();
	
	/**
	 * renders the model
	 * @param modelRenderer the renderer
	 */
	protected void render(ModelRenderer modelRenderer){
		modelRenderer.render(SCALE);
	}
	
}
