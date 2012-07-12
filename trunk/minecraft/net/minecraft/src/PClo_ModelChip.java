package net.minecraft.src;


/**
 * Model for PClo_TileEntityGateRenderer.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_ModelChip extends ModelBase {

	private ModelRenderer parts[];

	/**
	 * Radio block model.
	 */
	public PClo_ModelChip() {

		textureWidth = 64;
		textureHeight = 34;

		parts = new ModelRenderer[2];

		// body
		parts[0] = new ModelRenderer(this, 0, 0);
		parts[0].addBox(-4F, -1F, -5F, 8, 2, 10, 0.0F);

		// legs
		parts[1] = new ModelRenderer(this, 0, 12);
		parts[1].addBox(-5F, 0F, -5F, 10, 3, 10, 0.0F);

	}

	/**
	 * Do render.
	 */
	public void render() {
		for (ModelRenderer part : parts) {
			if (part == null) {
				break;
			}

			part.render(0.0625F); // length of one size and position unit

		}
	}
}
