package net.minecraft.src;


/**
 * Model for PClo_TileEntityRadioRenderer.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCde_ModelDeco extends ModelBase {

	private ModelRenderer ironFrame[];
	private ModelRenderer ironLedge[];
	private ModelRenderer ironLedgeStairs[];

	/**
	 * Radio block model.
	 */
	public PCde_ModelDeco() {

		textureWidth = 128;
		textureHeight = 64;

		ironFrame = new ModelRenderer[8];
		ironLedge = new ModelRenderer[5];
		ironLedgeStairs = new ModelRenderer[4];



		// frame
		// top bottom
		ironFrame[0] = new ModelRenderer(this, 0, 0);
		ironFrame[0].addBox(-8F, -8F, -8F, 16, 3, 16, 0.0F);
		ironFrame[0].addBox(-8F, 5F, -8F, 16, 3, 16, 0.0F);

		ironFrame[1] = new ModelRenderer(this, 0, 0);
		ironFrame[1].addBox(-8F, -8F, -8F, 16, 3, 16, 0.0F);
		ironFrame[1].addBox(-8F, 5F, -8F, 16, 3, 16, 0.0F);
		ironFrame[1].rotateAngleX = (float) (Math.PI / 2);

		ironFrame[2] = new ModelRenderer(this, 0, 0);
		ironFrame[2].addBox(-8F, -8F, -8F, 16, 3, 16, 0.0F);
		ironFrame[2].addBox(-8F, 5F, -8F, 16, 3, 16, 0.0F);
		ironFrame[2].rotateAngleZ = (float) (Math.PI / 2);

		// fillings
		ironFrame[3] = new ModelRenderer(this, 64, 39);
		ironFrame[3].addBox(-5F, -9F, -5F, 10, 4, 10, 0.0F);

		ironFrame[4] = new ModelRenderer(this, 64, 39);
		ironFrame[4].addBox(-5F, -9F, -5F, 10, 4, 10, 0.0F);
		ironFrame[4].rotateAngleZ = (float) (Math.PI / 2);

		ironFrame[5] = new ModelRenderer(this, 64, 39);
		ironFrame[5].addBox(-5F, -9F, -5F, 10, 4, 10, 0.0F);
		ironFrame[5].rotateAngleX = (float) (Math.PI / 2);

		ironFrame[6] = new ModelRenderer(this, 64, 39);
		ironFrame[6].addBox(-5F, -9F, -5F, 10, 4, 10, 0.0F);
		ironFrame[6].rotateAngleZ = -(float) (Math.PI / 2);

		ironFrame[7] = new ModelRenderer(this, 64, 39);
		ironFrame[7].addBox(-5F, -9F, -5F, 10, 4, 10, 0.0F);
		ironFrame[7].rotateAngleX = -(float) (Math.PI / 2);


		ironLedge[0] = new ModelRenderer(this, 64, 0);
		ironLedge[0].addBox(-8F, 7F, -8F, 16, 1, 16, 0.0F);

		ironLedge[1] = new ModelRenderer(this, 0, 37);
		ironLedge[1].addBox(-8F, -4F, -8F, 1, 11, 16, 0.0F);

		ironLedge[2] = new ModelRenderer(this, 0, 37);
		ironLedge[2].addBox(-8.0002F, -4.00008F, -8.0002F, 1, 11, 16, 0.0F);
		ironLedge[2].rotateAngleY = (float) (Math.PI * 0.5F);

		ironLedge[3] = new ModelRenderer(this, 0, 37);
		ironLedge[3].addBox(-8.0004F, -4.00005F, -8.0004F, 1, 11, 16, 0.0F);
		ironLedge[3].rotateAngleY = (float) (Math.PI * 1F);

		ironLedge[4] = new ModelRenderer(this, 0, 37);
		ironLedge[4].addBox(-8.0006F, -4.0001F, -8.0006F, 1, 11, 16, 0.0F);
		ironLedge[4].rotateAngleY = (float) (Math.PI * 1.5F);



		ironLedgeStairs[0] = new ModelRenderer(this, 64, 17);
		ironLedgeStairs[0].addBox(-8F, -9F, -8F, 8, 1, 16, 0.0F);
		ironLedgeStairs[0].addBox(0F, -1F, -8F, 8, 1, 16, 0.0F);

		ironLedgeStairs[1] = new ModelRenderer(this, 35, 37);
		ironLedgeStairs[1].addBox(-8F, -20F, -8F, 1, 11, 8, 0.0F);
		ironLedgeStairs[1].addBox(-8F, -12F, 0F, 1, 11, 8, 0.0F);
		ironLedgeStairs[1].rotateAngleY = (float) (Math.PI * 0.5F);

		ironLedgeStairs[2] = new ModelRenderer(this, 35, 37);
		ironLedgeStairs[2].addBox(7F, -12F, 0F, 1, 11, 8, 0.0F);
		ironLedgeStairs[2].addBox(7F, -20F, -8F, 1, 11, 8, 0.0F);
		ironLedgeStairs[2].rotateAngleY = (float) (Math.PI * 0.5F);

		ironLedgeStairs[3] = new ModelRenderer(this, 64, 34);
		ironLedgeStairs[3].addBox(-11.5F, -1F, -2F, 23, 1, 4, 0.0F);
		ironLedgeStairs[3].setRotationPoint(0, 0, 0);
		ironLedgeStairs[3].rotateAngleZ = (float) (Math.PI * 0.25F);
	}

	/**
	 * Set iron frame fillings
	 * 
	 * @param side 0 = top, 1,2,3,4 = sides
	 * @param show visible
	 */
	public void setFrameParts(int side, boolean show) {
		ironFrame[side + 3].showModel = show;
	}

	/**
	 * Set which fences are shown.
	 * 
	 * @param a 1st
	 * @param b 2nd
	 * @param c 3rd
	 * @param d 4th
	 * @param floor floor piece
	 */
	public void setLedgeFences(boolean a, boolean b, boolean c, boolean d, boolean floor) {
		ironLedge[0].showModel = floor;
		ironLedge[1].showModel = b;
		ironLedge[2].showModel = d;
		ironLedge[3].showModel = a;
		ironLedge[4].showModel = c;
	}

	/**
	 * Set which fences are shown for stairs.
	 * 
	 * @param a 1st
	 * @param b 2nd
	 */
	public void setStairsFences(boolean a, boolean b) {
		ironLedgeStairs[1].showModel = a;
		ironLedgeStairs[2].showModel = b;
	}

	/**
	 * Do render.
	 * 
	 * @param type device type. Equals to type in tile entity. NonSolid block
	 *            adds 100 to it.
	 */
	public void render(int type) {
		// parts[1].render(0.0625F);

		if (type == 0) {
			for (ModelRenderer part : ironFrame) {
				if (part == null) {
					break;
				}

				part.render(0.0625F); // length of one size and position unit
			}
		} else if (type == 100) {
			for (ModelRenderer part : ironLedge) {
				if (part == null) {
					break;
				}

				part.render(0.0625F); // length of one size and position unit
			}
		} else if (type == 101) {
			for (ModelRenderer part : ironLedgeStairs) {
				if (part == null) {
					break;
				}

				part.render(0.0625F); // length of one size and position unit
			}
		}
	}
}
