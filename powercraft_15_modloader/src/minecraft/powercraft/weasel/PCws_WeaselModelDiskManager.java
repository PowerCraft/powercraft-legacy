package powercraft.weasel;

import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;

public class PCws_WeaselModelDiskManager extends ModelBase {

	private ModelRenderer model[];
	
	public PCws_WeaselModelDiskManager(){
		textureWidth = 128;
		textureHeight = 128;
		
		model = new ModelRenderer[2];

		// box
		model[0] = new ModelRenderer(this, 0, 99);
		model[0].addBox(-8F, -13F, -8F, 16, 13, 16, 0.0F);

		// top
		model[1] = new ModelRenderer(this, 37, 53);
		model[1].addBox(-6F, -14F, -6F, 12, 1, 12, 0.0F);
	}
	
	public void renderDevice() {
		if(model!=null){
			for(int i=0; i<model.length; i++){
				model[i].render(0.0625F);
			}
		}
	}
	
}
