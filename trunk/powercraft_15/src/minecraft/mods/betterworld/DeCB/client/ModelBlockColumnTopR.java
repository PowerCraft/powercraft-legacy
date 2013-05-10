
package mods.betterworld.DeCB.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;



public class ModelBlockColumnTopR extends ModelBase
{
  //fields
    ModelRenderer columncenter;
    ModelRenderer top2;
    ModelRenderer top1;
    ModelRenderer columncenterf;
    ModelRenderer columncenterb;
    ModelRenderer columncenterl;
    ModelRenderer columncenterr;
    ModelRenderer top2f;
    ModelRenderer top2b;
    ModelRenderer top2l;
    ModelRenderer top2r;
    ModelRenderer top1b;
    ModelRenderer top1f;
    ModelRenderer top1r;
    ModelRenderer top1l;
  
  public ModelBlockColumnTopR()
  {
    textureWidth = 16;
    textureHeight = 16;
    
      columncenter = new ModelRenderer(this, 0, 12);
      columncenter.addBox(0F, 0F, 0F, 6, 14, 6);
      columncenter.setRotationPoint(-3F, 10F, -3F);
      columncenter.setTextureSize(16, 16);
      columncenter.mirror = true;
      setRotation(columncenter, 0F, 0F, 0F);
      top2 = new ModelRenderer(this, 2, 1);
      top2.addBox(0F, 0F, 0F, 8, 8, 1);
      top2.setRotationPoint(-4F, 10F, -4F);
      top2.setTextureSize(16, 16);
      top2.mirror = true;
      setRotation(top2, 1.570796F, 0F, 0F);
      top1 = new ModelRenderer(this, 0, 0);
      top1.addBox(0F, 0F, 0F, 10, 10, 1);
      top1.setRotationPoint(-5F, 9F, -5F);
      top1.setTextureSize(16, 16);
      top1.mirror = true;
      setRotation(top1, 1.570796F, 0F, 0F);
      columncenterf = new ModelRenderer(this, 8, 17);
      columncenterf.addBox(0F, 0F, 0F, 4, 14, 1);
      columncenterf.setRotationPoint(-2F, 10F, -4F);
      columncenterf.setTextureSize(16,16);
      columncenterf.mirror = true;
      setRotation(columncenterf, 0F, 0F, 0F);
      columncenterb = new ModelRenderer(this, 8, 17);
      columncenterb.addBox(0F, 0F, 0F, 4, 14, 1);
      columncenterb.setRotationPoint(-2F, 10F, 3F);
      columncenterb.setTextureSize(16, 16);
      columncenterb.mirror = true;
      setRotation(columncenterb, 0F, 0F, 0F);
      columncenterl = new ModelRenderer(this, 8, 14);
      columncenterl.addBox(0F, 0F, 0F, 1, 14, 4);
      columncenterl.setRotationPoint(3F, 10F, -2F);
      columncenterl.setTextureSize(16, 16);
      columncenterl.mirror = true;
      setRotation(columncenterl, 0F, 0F, 0F);
      columncenterr = new ModelRenderer(this, 8, 14);
      columncenterr.addBox(0F, 0F, 0F, 1, 14, 4);
      columncenterr.setRotationPoint(-4F, 10F, -2F);
      columncenterr.setTextureSize(16, 16);
      columncenterr.mirror = true;
      setRotation(columncenterr, 0F, 0F, 0F);
      top2f = new ModelRenderer(this, 2, 0);
      top2f.addBox(0F, 0F, 0F, 8, 1, 1);
      top2f.setRotationPoint(-4F, 8F, -6F);
      top2f.setTextureSize(16, 16);
      top2f.mirror = true;
      setRotation(top2f, 0F, 0F, 0F);
      top2b = new ModelRenderer(this, 2, 0);
      top2b.addBox(0F, 0F, 0F, 8, 1, 1);
      top2b.setRotationPoint(-4F, 8F, 5F);
      top2b.setTextureSize(16, 16);
      top2b.mirror = true;
      setRotation(top2b, 0F, 0F, 0F);
      top2l = new ModelRenderer(this, 2, 0);
      top2l.addBox(0F, 0F, 0F, 8, 1, 1);
      top2l.setRotationPoint(-6F, 8F, 4F);
      top2l.setTextureSize(16, 16);
      top2l.mirror = true;
      setRotation(top2l, 0F, 1.570796F, 0F);
      top2r = new ModelRenderer(this, 2, 0);
      top2r.addBox(0F, 0F, 0F, 8, 1, 1);
      top2r.setRotationPoint(5F, 8F, 4F);
      top2r.setTextureSize(16, 16);
      top2r.mirror = true;
      setRotation(top2r, 0F, 1.570796F, 0F);
      top1b = new ModelRenderer(this, 4, 0);
      top1b.addBox(0F, 0F, 0F, 6, 1, 1);
      top1b.setRotationPoint(-3F, 9F, 4F);
      top1b.setTextureSize(16, 16);
      top1b.mirror = true;
      setRotation(top1b, 0F, 0F, 0F);
      top1f = new ModelRenderer(this, 4, 0);
      top1f.addBox(0F, 0F, 0F, 6, 1, 1);
      top1f.setRotationPoint(-3F, 9F, -5F);
      top1f.setTextureSize(16, 16);
      top1f.mirror = true;
      setRotation(top1f, 0F, 0F, 0F);
      top1r = new ModelRenderer(this, 4, 0);
      top1r.addBox(0F, 0F, 0F, 6, 1, 1);
      top1r.setRotationPoint(-5F, 9F, 3F);
      top1r.setTextureSize(16, 16);
      top1r.mirror = true;
      setRotation(top1r, 0F, 1.570796F, 0F);
      top1l = new ModelRenderer(this, 4, 0);
      top1l.addBox(0F, 0F, 0F, 6, 1, 1);
      top1l.setRotationPoint(4F, 9F, 3F);
      top1l.setTextureSize(16, 16);
      top1l.mirror = true;
      setRotation(top1l, 0F, 1.570796F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    columncenter.render(f5);
    top2.render(f5);
    top1.render(f5);
    columncenterf.render(f5);
    columncenterb.render(f5);
    columncenterl.render(f5);
    columncenterr.render(f5);
    top2f.render(f5);
    top2b.render(f5);
    top2l.render(f5);
    top2r.render(f5);
    top1b.render(f5);
    top1f.render(f5);
    top1r.render(f5);
    top1l.render(f5);
  }
  
  public void renderModel (float f1)
  {
	    this.columncenter.render(f1);
	    this.top2.render(f1);
	    this.top1.render(f1);
	    this.columncenterf.render(f1);
	    this.columncenterb.render(f1);
	    this.columncenterl.render(f1);
	    this.columncenterr.render(f1);
	    this.top2f.render(f1);
	    this.top2b.render(f1);
	    this.top2l.render(f1);
	    this.top2r.render(f1);
	    this.top1b.render(f1);
	    this.top1f.render(f1);
	    this.top1r.render(f1);
	    this.top1l.render(f1);
  }
  
  
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
  }

}
