package net.minecraft.src;



/**
 * Fishing machine "screw" model
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_ModelFishingMachineScrew extends ModelBase {

	@SuppressWarnings("javadoc")
	public ModelRenderer screwStick, screwBody, screwPane1, screwPane2, screwPane3, screwPane4;

	/**
	 * Screw model
	 */
	public PCma_ModelFishingMachineScrew() {
		textureWidth = 105;
		textureHeight = 53;

		screwStick = new ModelRenderer(this, 42, 26);
		screwStick.addBox(-0.5F, -10, -0.5F, 1, 10, 1, 0.0F);
		screwStick.setRotationPoint(0.0F, 0.0F, 0.0F);

		screwBody = new ModelRenderer(this, 75, 0);
		screwBody.addBox(-2, -11, -2, 4, 6, 4, 0.0F);
		screwBody.setRotationPoint(0.0F, 0.0F, 0.0F);

		screwPane1 = new ModelRenderer(this, 75, 0);
		screwPane1.addBox(-2, -1, -8, 4, 1, 6, 0.0F);
		screwPane1.setRotationPoint(0.0F, -9.0F, 0.0F);
		screwPane1.rotateAngleZ = ((float) Math.PI) / 4F;

		screwPane2 = new ModelRenderer(this, 75, 0);
		screwPane2.addBox(-2, -1, -8, 4, 1, 6, 0.0F);
		screwPane2.setRotationPoint(0.0F, -9.0F, 0.0F);
		screwPane2.rotateAngleZ = -((float) Math.PI) / 4F;
		screwPane2.rotateAngleY = (float) Math.PI;

		screwPane3 = new ModelRenderer(this, 75, 0);
		screwPane3.addBox(-8, -1, -2, 6, 1, 4, 0.0F);
		screwPane3.setRotationPoint(0.0F, -9.0F, 0.0F);
		screwPane3.rotateAngleX = ((float) Math.PI) / 4F;

		screwPane4 = new ModelRenderer(this, 75, 0);
		screwPane4.addBox(-8, -1, -2, 6, 1, 4, 0.0F);
		screwPane4.setRotationPoint(0.0F, -9.0F, 0.0F);
		screwPane4.rotateAngleX = ((float) Math.PI) / 4F;
		screwPane4.rotateAngleY = (float) Math.PI;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		screwStick.render(f5);
		screwBody.render(f5);
		screwPane1.render(f5);
		screwPane2.render(f5);
		screwPane3.render(f5);
		screwPane4.render(f5);
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {}
}
