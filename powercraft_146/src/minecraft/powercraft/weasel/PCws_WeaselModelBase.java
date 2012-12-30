package powercraft.weasel;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class PCws_WeaselModelBase extends ModelBase {

	protected ModelRenderer model[];
	protected ModelRenderer modelColorMark[];
	
	public PCws_WeaselModelBase(){
		textureWidth = 128;
		textureHeight = 128;
	}
	
	public void renderDevice(PCws_TileEntityWeasel te) {
		if(model!=null){
			for(int i=0; i<model.length; i++){
				model[i].render(0.0625F);
			}
		}
	}
	
	public void renderColorMark(PCws_TileEntityWeasel te) {
		if(modelColorMark!=null){
			for(int i=0; i<modelColorMark.length; i++){
				modelColorMark[i].render(0.0625F);
			}
		}
	}

	public void renderText(PCws_TileEntityWeasel te, FontRenderer fontrenderer) {
		
	}
	
}
