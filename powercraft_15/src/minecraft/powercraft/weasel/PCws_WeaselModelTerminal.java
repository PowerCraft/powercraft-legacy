package powercraft.weasel;

import net.minecraft.client.model.ModelRenderer;

public class PCws_WeaselModelTerminal extends PCws_WeaselModelBase {

	public PCws_WeaselModelTerminal(){
		
		model = new ModelRenderer[3];
		modelColorMark = new ModelRenderer[1];
		
		model[0] = new ModelRenderer(this, 39, 67);
		model[0].addBox(-7F, -4F, -7F, 14, 4, 14, 0.0F);

		// keyb
		model[1] = new ModelRenderer(this, 62, 59);
		model[1].addBox(-5F, -5F, -5F, 10, 1, 4, 0.0F);

		//screen
		model[2] = new ModelRenderer(this, 39, 85);
		model[2].addBox(-6F, -12F, 0F, 12, 8, 6, 0.0F);

		modelColorMark[0] = new ModelRenderer(this, 13, 12);
		modelColorMark[0].addBox(-2F, -13F, 2F, 4, 1, 2, 0.0F);
	}
	
}
