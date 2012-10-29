package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBat extends ModelBase
{
    private ModelRenderer field_82895_a;
    private ModelRenderer field_82893_b;
    private ModelRenderer field_82894_c;
    private ModelRenderer field_82891_d;
    private ModelRenderer field_82892_e;
    private ModelRenderer field_82890_f;

    public ModelBat()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.field_82895_a = new ModelRenderer(this, 0, 0);
        this.field_82895_a.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
        ModelRenderer var1 = new ModelRenderer(this, 24, 0);
        var1.addBox(-4.0F, -6.0F, -2.0F, 3, 4, 1);
        this.field_82895_a.addChild(var1);
        ModelRenderer var2 = new ModelRenderer(this, 24, 0);
        var2.mirror = true;
        var2.addBox(1.0F, -6.0F, -2.0F, 3, 4, 1);
        this.field_82895_a.addChild(var2);
        this.field_82893_b = new ModelRenderer(this, 0, 16);
        this.field_82893_b.addBox(-3.0F, 4.0F, -3.0F, 6, 12, 6);
        this.field_82893_b.setTextureOffset(0, 34).addBox(-5.0F, 16.0F, 0.0F, 10, 6, 1);
        this.field_82894_c = new ModelRenderer(this, 42, 0);
        this.field_82894_c.addBox(-12.0F, 1.0F, 1.5F, 10, 16, 1);
        this.field_82892_e = new ModelRenderer(this, 24, 16);
        this.field_82892_e.setRotationPoint(-12.0F, 1.0F, 1.5F);
        this.field_82892_e.addBox(-8.0F, 1.0F, 0.0F, 8, 12, 1);
        this.field_82891_d = new ModelRenderer(this, 42, 0);
        this.field_82891_d.mirror = true;
        this.field_82891_d.addBox(2.0F, 1.0F, 1.5F, 10, 16, 1);
        this.field_82890_f = new ModelRenderer(this, 24, 16);
        this.field_82890_f.mirror = true;
        this.field_82890_f.setRotationPoint(12.0F, 1.0F, 1.5F);
        this.field_82890_f.addBox(0.0F, 1.0F, 0.0F, 8, 12, 1);
        this.field_82893_b.addChild(this.field_82894_c);
        this.field_82893_b.addChild(this.field_82891_d);
        this.field_82894_c.addChild(this.field_82892_e);
        this.field_82891_d.addChild(this.field_82890_f);
    }

    public int func_82889_a()
    {
        return 36;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        EntityBat var8 = (EntityBat)par1Entity;

        if (var8.func_82235_h())
        {
            this.field_82895_a.rotateAngleX = par6 / (180F / (float)Math.PI);
            this.field_82895_a.rotateAngleY = (float)Math.PI - par5 / (180F / (float)Math.PI);
            this.field_82895_a.rotateAngleZ = (float)Math.PI;
            this.field_82895_a.setRotationPoint(0.0F, -2.0F, 0.0F);
            this.field_82894_c.setRotationPoint(-3.0F, 0.0F, 3.0F);
            this.field_82891_d.setRotationPoint(3.0F, 0.0F, 3.0F);
            this.field_82893_b.rotateAngleX = (float)Math.PI;
            this.field_82894_c.rotateAngleX = -0.15707964F;
            this.field_82894_c.rotateAngleY = -((float)Math.PI * 2F / 5F);
            this.field_82892_e.rotateAngleY = -1.7278761F;
            this.field_82891_d.rotateAngleX = this.field_82894_c.rotateAngleX;
            this.field_82891_d.rotateAngleY = -this.field_82894_c.rotateAngleY;
            this.field_82890_f.rotateAngleY = -this.field_82892_e.rotateAngleY;
        }
        else
        {
            this.field_82895_a.rotateAngleX = par6 / (180F / (float)Math.PI);
            this.field_82895_a.rotateAngleY = par5 / (180F / (float)Math.PI);
            this.field_82895_a.rotateAngleZ = 0.0F;
            this.field_82895_a.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.field_82894_c.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.field_82891_d.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.field_82893_b.rotateAngleX = ((float)Math.PI / 4F) + MathHelper.cos(par4 * 0.1F) * 0.15F;
            this.field_82893_b.rotateAngleY = 0.0F;
            this.field_82894_c.rotateAngleY = MathHelper.cos(par4 * 1.3F) * (float)Math.PI * 0.25F;
            this.field_82891_d.rotateAngleY = -this.field_82894_c.rotateAngleY;
            this.field_82892_e.rotateAngleY = this.field_82894_c.rotateAngleY * 0.5F;
            this.field_82890_f.rotateAngleY = -this.field_82894_c.rotateAngleY * 0.5F;
        }

        this.field_82895_a.render(par7);
        this.field_82893_b.render(par7);
    }
}
