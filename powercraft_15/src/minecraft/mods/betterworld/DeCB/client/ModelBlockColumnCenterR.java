
package mods.betterworld.DeCB.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;



public class ModelBlockColumnCenterR extends ModelBase
{
  //fields
    ModelRenderer columncenter;
    ModelRenderer columncenterf;
    ModelRenderer columncenterb;
    ModelRenderer columncenterl;
    ModelRenderer columncenterr;
  
  public ModelBlockColumnCenterR()
  {
    textureWidth = 16;
    textureHeight = 16;
    
      columncenter = new ModelRenderer(this, 0, 0);
      columncenter.addBox(0F, 0F, 0F, 6, 16, 6);
      columncenter.setRotationPoint(-3F, 8F, -3F);
      columncenter.setTextureSize(16,16);
      columncenter.mirror = true;
      setRotation(columncenter, 0F, 0F, 0F);
      columncenterf = new ModelRenderer(this, 7, 5);
      columncenterf.addBox(0F, 0F, 0F, 4, 16, 1);
      columncenterf.setRotationPoint(-2F, 8F, -4F);
      columncenterf.setTextureSize(16, 16);
      columncenterf.mirror = true;
      setRotation(columncenterf, 0F, 0F, 0F);
      columncenterb = new ModelRenderer(this, 7, 5);
      columncenterb.addBox(0F, 0F, 0F, 4, 16, 1);
      columncenterb.setRotationPoint(-2F, 8F, 3F);
      columncenterb.setTextureSize(16, 16);
      columncenterb.mirror = true;
      setRotation(columncenterb, 0F, 0F, 0F);
      columncenterl = new ModelRenderer(this, 7, 2);
      columncenterl.addBox(0F, 0F, 0F, 1, 16, 4);
      columncenterl.setRotationPoint(3F, 8F, -2F);
      columncenterl.setTextureSize(16, 16);
      columncenterl.mirror = true;
      setRotation(columncenterl, 0F, 0F, 0F);
      columncenterr = new ModelRenderer(this, 7, 2);
      columncenterr.addBox(0F, 0F, 0F, 1, 16, 4);
      columncenterr.setRotationPoint(-4F, 8F, -2F);
      columncenterr.setTextureSize(16, 16);
      columncenterr.mirror = true;
      setRotation(columncenterr, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    columncenter.render(f5);
    columncenterf.render(f5);
    columncenterb.render(f5);
    columncenterl.render(f5);
    columncenterr.render(f5);
  }
  
  public void renderModel (float f1)
  {
	    this.columncenter.render(f1);
	    this.columncenterf.render(f1);
	    this.columncenterb.render(f1);
	    this.columncenterl.render(f1);
	    this.columncenterr.render(f1);
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
