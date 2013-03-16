package powercraft.weasel;

import net.minecraft.client.model.ModelRenderer;

public class PCws_WeaselModelSpeaker extends PCws_WeaselModelBase {

	public PCws_WeaselModelSpeaker(){
		
		model = new ModelRenderer[2];
		modelColorMark = new ModelRenderer[1];
		
		// the stone pad
		model[0] = new ModelRenderer(this, 64, 39);
		model[0].addBox(-8F, -3F, -8F, 16, 3, 16, 0.0F);

		// body
		model[1] = new ModelRenderer(this, 0, 40);
		model[1].addBox(-6F, -15F, -6F, 12, 12, 12, 0.0F);

		// the colour piece
		modelColorMark[0] = new ModelRenderer(this, 0, 31);
		modelColorMark[0].addBox(-2F, -15.5F, -2F, 4, 1, 4, 0.0F);
	}
	
}
