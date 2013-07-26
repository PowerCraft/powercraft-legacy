package powercraft.api.energy;

import net.minecraft.client.model.ModelRenderer;
import powercraft.apiOld.PC_Model;

class PC_ModelEnergyBeam extends PC_Model{

	private ModelRenderer energyBeam[] = new ModelRenderer[7];
	
	public PC_ModelEnergyBeam(){
		energyBeam[0] = new ModelRenderer(this).setTextureSize(16, 16);
		energyBeam[0].addBox(-1, -1, -1, 2, 2, 2);
		energyBeam[1] = new ModelRenderer(this).setTextureSize(16, 16);
		energyBeam[1].addBox(-1, -32, -1, 2, 31, 2);
		energyBeam[2] = new ModelRenderer(this).setTextureSize(16, 16);
		energyBeam[2].addBox(-1, 1, -1, 2, 31, 2);
		energyBeam[3] = new ModelRenderer(this).setTextureSize(16, 16);
		energyBeam[3].addBox(-1, -1, -32, 2, 2, 31);
		energyBeam[4] = new ModelRenderer(this).setTextureSize(16, 16);
		energyBeam[4].addBox(-1, -1, 1, 2, 2, 31);
		energyBeam[5] = new ModelRenderer(this).setTextureSize(16, 16);
		energyBeam[5].addBox(-32, -1, -1, 31, 2, 2);
		energyBeam[6] = new ModelRenderer(this).setTextureSize(16, 16);
		energyBeam[6].addBox(1, -1, -1, 31, 2, 2);
	}
	
	@Override
	public void setEnabled(int index, boolean enabled){
		energyBeam[index+1].showModel = enabled;
	}
	
	@Override
	public void renderAll() {
		for(int i=0; i<energyBeam.length; i++){
			energyBeam[i].render(SCALE/4.0f);
		}
	}

}
