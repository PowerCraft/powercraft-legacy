package powercraft.weasel;

import net.minecraft.client.model.ModelRenderer;

public class PCws_WeaselModelDiskDrive extends PCws_WeaselModelBase {

	public PCws_WeaselModelDiskDrive(){
		model = new ModelRenderer[1];
		modelColorMark = new ModelRenderer[1];
		
		// box
		model[0] = new ModelRenderer(this, 64, 99);
		model[0].addBox(-8F, -13F, -8F, 16, 13, 16, 0.0F);

		// colored
		modelColorMark[0] = new ModelRenderer(this, 0, 31);
		modelColorMark[0].addBox(-2F, -14F, -2F, 4, 1, 4, 0.0F);
	}
	
}
