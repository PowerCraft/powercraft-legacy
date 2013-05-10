package mods.PCTest;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class PCModelBlockWire extends ModelBase
{
  //fields
    ModelRenderer Center;
    ModelRenderer front;
    ModelRenderer back;
    ModelRenderer left;
    ModelRenderer right;
    ModelRenderer connect;
    ModelRenderer overFB;
    ModelRenderer overLR;
    ModelRenderer front1;
    ModelRenderer front2;
    ModelRenderer back1;
    ModelRenderer back2;
    ModelRenderer left1;
    ModelRenderer left2;
    ModelRenderer right1;
    ModelRenderer right2;
  
  public PCModelBlockWire()
  {
    textureWidth = 32;
    textureHeight = 32;
    
      Center = new ModelRenderer(this, 5, 5);
      Center.addBox(-1F, 7F, -1F, 2, 1, 2);
      Center.setRotationPoint(0F, 16F, 0F);
      Center.setTextureSize(32, 32);
      Center.mirror = true;
      setRotation(Center, 0F, -0.0349066F, 0F);
      front = new ModelRenderer(this, 0, 0);
      front.addBox(-1F, 7F, -7F, 2, 1, 6);
      front.setRotationPoint(0F, 16F, 0F);
      front.setTextureSize(32, 32);
      front.mirror = true;
      setRotation(front, 0F, 0F, 0F);
      back = new ModelRenderer(this, 0, 0);
      back.addBox(-1F, 7F, 1F, 2, 1, 6);
      back.setRotationPoint(0F, 16F, 0F);
      back.setTextureSize(32, 32);
      back.mirror = true;
      setRotation(back, 0F, 0F, 0F);
      left = new ModelRenderer(this, 0, 5);
      left.addBox(1F, 7F, -1F, 6, 1, 2);
      left.setRotationPoint(0F, 16F, 0F);
      left.setTextureSize(32, 32);
      left.mirror = true;
      setRotation(left, 0F, 0F, 0F);
      right = new ModelRenderer(this, 0, 5);
      right.addBox(-7F, 7F, -1F, 6, 1, 2);
      right.setRotationPoint(0F, 16F, 0F);
      right.setTextureSize(32, 32);
      right.mirror = true;
      setRotation(right, 0F, 0F, 0F);
      connect = new ModelRenderer(this, 19, 3);
      connect.addBox(-1.5F, 6F, -1.5F, 3, 2, 3);
      connect.setRotationPoint(0F, 16F, 0F);
      connect.setTextureSize(32, 32);
      connect.mirror = true;
      setRotation(connect, 0F, 0.7853982F, 0F);
      overFB = new ModelRenderer(this, 3, 3);
      overFB.addBox(-1F, 6F, -2F, 2, 1, 4);
      overFB.setRotationPoint(0F, 16F, 0F);
      overFB.setTextureSize(32, 32);
      overFB.mirror = true;
      setRotation(overFB, 0F, 0F, 0F);
      overLR = new ModelRenderer(this, 3, 5);
      overLR.addBox(-2F, 6F, -1F, 4, 1, 2);
      overLR.setRotationPoint(0F, 16F, 0F);
      overLR.setTextureSize(32, 32);
      overLR.mirror = true;
      setRotation(overLR, 0F, 0F, 0F);
      front1 = new ModelRenderer(this, 5, 6);
      front1.addBox(-1F, 7F, -8F, 2, 1, 1);
      front1.setRotationPoint(0F, 16F, 0F);
      front1.setTextureSize(32, 32);
      front1.mirror = true;
      setRotation(front1, 0F, 0F, 0F);
      front2 = new ModelRenderer(this, 4, 5);
      front2.addBox(-1F, 7F, -9F, 2, 1, 2);
      front2.setRotationPoint(0F, 16F, 0F);
      front2.setTextureSize(32, 32);
      front2.mirror = true;
      setRotation(front2, 0F, 0F, 0F);
      back1 = new ModelRenderer(this, 4, 6);
      back1.addBox(-1F, 7F, 7F, 2, 1, 1);
      back1.setRotationPoint(0F, 16F, 0F);
      back1.setTextureSize(32, 32);
      back1.mirror = true;
      setRotation(back1, 0F, 0F, 0F);
      back2 = new ModelRenderer(this, 4, 5);
      back2.addBox(-1F, 7F, 7F, 2, 1, 2);
      back2.setRotationPoint(0F, 16F, 0F);
      back2.setTextureSize(32, 32);
      back2.mirror = true;
      setRotation(back2, 0F, 0F, 0F);
      left1 = new ModelRenderer(this, 5, 5);
      left1.addBox(7F, 7F, -1F, 1, 1, 2);
      left1.setRotationPoint(0F, 16F, 0F);
      left1.setTextureSize(32, 32);
      left1.mirror = true;
      setRotation(left1, 0F, 0F, 0F);
      left2 = new ModelRenderer(this, 4, 5);
      left2.addBox(7F, 7F, -1F, 2, 1, 2);
      left2.setRotationPoint(0F, 16F, 0F);
      left2.setTextureSize(32, 32);
      left2.mirror = true;
      setRotation(left2, 0F, 0F, 0F);
      right1 = new ModelRenderer(this, 5, 5);
      right1.addBox(-8F, 7F, -1F, 1, 1, 2);
      right1.setRotationPoint(0F, 16F, 0F);
      right1.setTextureSize(32, 32);
      right1.mirror = true;
      setRotation(right1, 0F, 0F, 0F);
      right2 = new ModelRenderer(this, 0, 0);
      right2.addBox(-9F, 7F, -1F, 2, 1, 2);
      right2.setRotationPoint(0F, 16F, 0F);
      right2.setTextureSize(32, 32);
      right2.mirror = true;
      setRotation(right2, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    Center.render(f5);
    front.render(f5);
    back.render(f5);
    left.render(f5);
    right.render(f5);
    connect.render(f5);
    overFB.render(f5);
    overLR.render(f5);
    front1.render(f5);
    front2.render(f5);
    back1.render(f5);
    back2.render(f5);
    left1.render(f5);
    left2.render(f5);
    right1.render(f5);
    right2.render(f5);
  }
  
  public void renderCenter(float f5)
  {
	    Center.render(f5);
  }
  
  public void renderFront(float f5)
  {
	  front.render(f5);
  }
  
  public void renderBack(float f5)
  {
	  back.render(f5); 
  }
  
  public void renderLeft(float f5)
  {
	  left.render(f5);
  }
  
  public void renderRight(float f5)
  {
	  right.render(f5); 
  }
  
  public void renderFront1(float f5)
  {
	  front1.render(f5);
  }
  
  public void renderBack1(float f5)
  {
	  back1.render(f5); 
  }
  
  public void renderLeft1(float f5)
  {
	  left1.render(f5);
  }
  
  public void renderRight1(float f5)
  {
	  right1.render(f5); 
  }
  
  public void renderFront2(float f5)
  {
	  front2.render(f5);
  }
  
  public void renderBack2(float f5)
  {
	  back2.render(f5); 
  }
  
  public void renderLeft2(float f5)
  {
	  left2.render(f5);
  }
  
  public void renderRight2(float f5)
  {
	  right2.render(f5); 
  }
  public void renderConnect(float f5)
  {
	    connect.render(f5); 
  }
  public void renderOverFB(float f5)
  {
	    overFB.render(f5);
  }
  public void renderOverLR(float f5)
  {
	    overLR.render(f5);
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
