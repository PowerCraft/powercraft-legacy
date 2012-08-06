package net.minecraft.src;

public class ModelVillager extends ModelBase
{
    public ModelRenderer field_78191_a;
    public ModelRenderer field_78189_b;
    public ModelRenderer field_78190_c;
    public ModelRenderer field_78187_d;
    public ModelRenderer field_78188_e;

    public ModelVillager(float par1)
    {
        this(par1, 0.0F);
    }

    public ModelVillager(float par1, float par2)
    {
        byte var3 = 64;
        byte var4 = 64;
        this.field_78191_a = (new ModelRenderer(this)).setTextureSize(var3, var4);
        this.field_78191_a.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        this.field_78191_a.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, par1);
        this.field_78191_a.setTextureOffset(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2, 4, 2, par1);
        this.field_78189_b = (new ModelRenderer(this)).setTextureSize(var3, var4);
        this.field_78189_b.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        this.field_78189_b.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, par1);
        this.field_78189_b.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, par1 + 0.5F);
        this.field_78190_c = (new ModelRenderer(this)).setTextureSize(var3, var4);
        this.field_78190_c.setRotationPoint(0.0F, 0.0F + par2 + 2.0F, 0.0F);
        this.field_78190_c.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, par1);
        this.field_78190_c.setTextureOffset(44, 22).addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, par1);
        this.field_78190_c.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, par1);
        this.field_78187_d = (new ModelRenderer(this, 0, 22)).setTextureSize(var3, var4);
        this.field_78187_d.setRotationPoint(-2.0F, 12.0F + par2, 0.0F);
        this.field_78187_d.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1);
        this.field_78188_e = (new ModelRenderer(this, 0, 22)).setTextureSize(var3, var4);
        this.field_78188_e.mirror = true;
        this.field_78188_e.setRotationPoint(2.0F, 12.0F + par2, 0.0F);
        this.field_78188_e.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        this.setRotationAngles(par2, par3, par4, par5, par6, par7);
        this.field_78191_a.render(par7);
        this.field_78189_b.render(par7);
        this.field_78187_d.render(par7);
        this.field_78188_e.render(par7);
        this.field_78190_c.render(par7);
    }

    /**
     * Sets the models various rotation angles.
     */
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6)
    {
        this.field_78191_a.rotateAngleY = par4 / (180F / (float)Math.PI);
        this.field_78191_a.rotateAngleX = par5 / (180F / (float)Math.PI);
        this.field_78190_c.rotationPointY = 3.0F;
        this.field_78190_c.rotationPointZ = -1.0F;
        this.field_78190_c.rotateAngleX = -0.75F;
        this.field_78187_d.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2 * 0.5F;
        this.field_78188_e.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2 * 0.5F;
        this.field_78187_d.rotateAngleY = 0.0F;
        this.field_78188_e.rotateAngleY = 0.0F;
    }
}
