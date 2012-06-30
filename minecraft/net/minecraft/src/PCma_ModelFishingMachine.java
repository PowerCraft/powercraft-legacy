package net.minecraft.src;



/**
 * Fishing machine main model
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_ModelFishingMachine extends ModelBase {

	@SuppressWarnings("javadoc")
	public ModelRenderer chestSocket, engine, chimneyStick, chimneyTop;

	/**
	 * fisher model
	 */
	public PCma_ModelFishingMachine() {
		textureWidth = 105;
		textureHeight = 53;

		chestSocket = new ModelRenderer(this, 0, 0);
		chestSocket.addBox(-9, -22.9F, -9, 18, 7, 18, 0.0F);
		chestSocket.setRotationPoint(0.0F, 0.0F, 0.0F);

		engine = new ModelRenderer(this, 0, 26);
		engine.addBox(-7, -16, -7, 14, 14, 14, 0.0F);
		engine.setRotationPoint(0.0F, 0.0F, 0.0F);

		chimneyStick = new ModelRenderer(this, 56, 33);
		chimneyStick.addBox(-2, -32, -12, 4, 16, 4, 0.0F);
		chimneyStick.setRotationPoint(0.0F, 0.0F, 0.0F);

		chimneyTop = new ModelRenderer(this, 56, 25);
		chimneyTop.addBox(-3, -33, -13, 6, 2, 6, 0.0F);
		chimneyTop.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		chestSocket.render(f5);
		chimneyStick.render(f5);
		chimneyTop.render(f5);
		engine.render(f5);
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {}
}
