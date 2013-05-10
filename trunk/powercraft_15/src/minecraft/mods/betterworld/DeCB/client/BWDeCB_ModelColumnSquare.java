package mods.betterworld.DeCB.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class BWDeCB_ModelColumnSquare extends ModelBase{



    ModelRenderer bottom1;
    ModelRenderer bottom2;
    ModelRenderer col1;
    ModelRenderer col2;
    ModelRenderer bottomH3;
    ModelRenderer top1;
    ModelRenderer top2;
    ModelRenderer topH3;
    ModelRenderer colBottom1;
    ModelRenderer colBottom2;
    ModelRenderer colTopBottom1;
    ModelRenderer colTopBottom2;
    ModelRenderer colTop1;
    ModelRenderer colTop2;
    //Left
    ModelRenderer topCL1;
    ModelRenderer topCL2;
    ModelRenderer topHCL3;
    ModelRenderer centerCL1;
    ModelRenderer centerCL2;
    ModelRenderer centerCL3;
    //Right
    ModelRenderer topCR1;
    ModelRenderer topCR2;
    ModelRenderer topHCR3;
    ModelRenderer centerCR1;
    ModelRenderer centerCR2;
    ModelRenderer centerCR3;
    //Front
    ModelRenderer topCF1;
    ModelRenderer topCF2;
    ModelRenderer topHCF3;
    ModelRenderer centerCF1;
    ModelRenderer centerCF2;
    ModelRenderer centerCF3;
    //Back
    ModelRenderer topCB1;
    ModelRenderer topCB2;
    ModelRenderer topHCB3;
    ModelRenderer centerCB1;
    ModelRenderer centerCB2;
    ModelRenderer centerCB3;
    ModelRenderer topBase1;
    ModelRenderer topBase2;


  
  public BWDeCB_ModelColumnSquare()
  {
    textureWidth = 16;
    textureHeight = 16;
    
      bottom1 = new ModelRenderer(this, 2, 2);
      bottom1.addBox(-6F, 0F, -6F, 12, 1, 12);
      bottom1.setRotationPoint(0F, 23F, 0F);
      bottom1.setTextureSize(16, 16);
      bottom1.mirror = true;
      setRotation(bottom1, 0F, 0F, 0F);
      bottom2 = new ModelRenderer(this, 3, 3);
      bottom2.addBox(-5F, 0F, -5F, 10, 1, 10);
      bottom2.setRotationPoint(0F, 22F, 0F);
      bottom2.setTextureSize(16, 16);
      bottom2.mirror = true;
      setRotation(bottom2, 0F, 0F, 0F);
      col1 = new ModelRenderer(this, 3, -6);
      col1.addBox(-4F, 0F, -3F, 8, 16, 6);
      col1.setRotationPoint(0F, 8F, 0F);
      col1.setTextureSize(16, 16);
      col1.mirror = true;
      setRotation(col1, 0F, 0F, 0F);
      col2 = new ModelRenderer(this, 4, -6);
      col2.addBox(-3F, 0F, -4F, 6, 16, 8);
      col2.setRotationPoint(0F, 8F, 0F);
      col2.setTextureSize(16, 16);
      col2.mirror = true;
      setRotation(col2, 0F, 0F, 0F);
      bottomH3 = new ModelRenderer(this, 3, 12);
      bottomH3.addBox(-4F, 0F, -4F, 8, 4, 8);
      bottomH3.setRotationPoint(0F, 20F, 0F);
      bottomH3.setTextureSize(16, 16);
      bottomH3.mirror = true;
      setRotation(bottomH3, 0F, 0F, 0F);
      top1 = new ModelRenderer(this, 2, 2);
      top1.addBox(-6F, 0F, -6F, 12, 1, 12);
      top1.setRotationPoint(0F, 8F, 0F);
      top1.setTextureSize(16, 16);
      top1.mirror = true;
      setRotation(top1, 0F, 0F, 0F);
      top2 = new ModelRenderer(this, 3, 3);
      top2.addBox(-5F, 0F, -5F, 10, 1, 10);
      top2.setRotationPoint(0F, 9F, 0F);
      top2.setTextureSize(16, 16);
      top2.mirror = true;
      setRotation(top2, 0F, 0F, 0F);
      topH3 = new ModelRenderer(this, 3, 0);
      topH3.addBox(-4F, 0F, -4F, 8, 4, 8);
      topH3.setRotationPoint(0F, 8F, 0F);
      topH3.setTextureSize(16, 16);
      topH3.mirror = true;
      setRotation(topH3, 0F, 0F, 0F);
      colBottom1 = new ModelRenderer(this, 3, 2);
      colBottom1.addBox(-4F, 0F, -3F, 8, 12, 6);
      colBottom1.setRotationPoint(0F, 8F, 0F);
      colBottom1.setTextureSize(16, 16);
      colBottom1.mirror = true;
      setRotation(colBottom1, 0F, 0F, 0F);
      colBottom2 = new ModelRenderer(this, 4, 2);
      colBottom2.addBox(-3F, 0F, -4F, 6, 12, 8);
      colBottom2.setRotationPoint(0F, 8F, 0F);
      colBottom2.setTextureSize(16,16);
      colBottom2.mirror = true;
      setRotation(colBottom2, 0F, 0F, 0F);
      colTopBottom1 = new ModelRenderer(this, 3, 1);
      colTopBottom1.addBox(-4F, 0F, -3F, 8, 8, 6);
      colTopBottom1.setRotationPoint(0F, 12F, 0F);
      colTopBottom1.setTextureSize(16, 16);
      colTopBottom1.mirror = true;
      setRotation(colTopBottom1, 0F, 0F, 0F);
      colTopBottom2 = new ModelRenderer(this, 4, 1);
      colTopBottom2.addBox(-3F, 0F, -4F, 6, 8, 8);
      colTopBottom2.setRotationPoint(0F, 12F, 0F);
      colTopBottom2.setTextureSize(16, 16);
      colTopBottom2.mirror = true;
      setRotation(colTopBottom2, 0F, 0F, 0F);
      setRotation(colTopBottom2, 0F, 1.570796F, 0F);
      colTop1 = new ModelRenderer(this, 3, 4);
      colTop1.addBox(-4F, 0F, -3F, 8, 12, 6);
      colTop1.setRotationPoint(0F, 12F, 0F);
      colTop1.setTextureSize(16, 16);
      colTop1.mirror = true;
      setRotation(colTop1, 0F, 0F, 0F);
      colTop2 = new ModelRenderer(this, 4, 4);
      colTop2.addBox(-3F, 0F, -4F, 6, 12, 8);
      colTop2.setRotationPoint(0F, 12F, 0F);
      colTop2.setTextureSize(16, 16);
      colTop2.mirror = true;
      setRotation(colTop2, 0F, 0F, 0F);
      //Left
      topCL1 = new ModelRenderer(this, -3, 3);
      topCL1.addBox(-6F, 0F, -6F, 14, 1, 12);
      topCL1.setRotationPoint(0F, 8F, 0F);
      topCL1.setTextureSize(16, 16);
      topCL1.mirror = true;
      setRotation(topCL1, 0F, 0F, 0F);
      topCL2 = new ModelRenderer(this, 1, 5);
      topCL2.addBox(-5F, 0F, -5F, 13, 1, 10);
      topCL2.setRotationPoint(0F, 9F, 0F);
      topCL2.setTextureSize(16, 16);
      topCL2.mirror = true;
      setRotation(topCL2, 0F, 0F, 0F);
      topHCL3 = new ModelRenderer(this, 3, 8);
      topHCL3.addBox(-4F, 0F, -4F, 12, 2, 8);
      topHCL3.setRotationPoint(0F, 10F, 0F);
      topHCL3.setTextureSize(16, 16);
      topHCL3.mirror = true;
      setRotation(topHCL3, 0F, 0F, 0F);
      centerCL1 = new ModelRenderer(this, 5, 12);
      centerCL1.addBox(-4F, 0F, -3F, 12, 4, 6);
      centerCL1.setRotationPoint(0F, 12F, 0F);
      centerCL1.setTextureSize(16, 16);
      centerCL1.mirror = true;
      setRotation(centerCL1, 0F, 0F, 0F);
      centerCL2 = new ModelRenderer(this, 3, 10);
      centerCL2.addBox(-3F, 0F, -4F, 11, 3, 8);
      centerCL2.setRotationPoint(0F, 12F, 0F);
      centerCL2.setTextureSize(16, 16);
      centerCL2.mirror = true;
      setRotation(centerCL2, 0F, 0F, 0F);
      centerCL3 = new ModelRenderer(this, 3, 13);
      centerCL3.addBox(-3F, 0F, -4F, 6, 1, 8);
      centerCL3.setRotationPoint(0F, 15F, 0F);
      centerCL3.setTextureSize(16, 16);
      centerCL3.mirror = true;
      setRotation(centerCL3, 0F, 0F, 0F);
      //Right
      topCR1 = new ModelRenderer(this, -3, 3);
      topCR1.addBox(-6F, 0F, -6F, 14, 1, 12);
      topCR1.setRotationPoint(0F, 8F, 0F);
      topCR1.setTextureSize(16, 16);
      topCR1.mirror = true;
      setRotation(topCR1, 0F, 3.141593F, 0F);
      topCR2 = new ModelRenderer(this, 1, 5);
      topCR2.addBox(-5F, 0F, -5F, 13, 1, 10);
      topCR2.setRotationPoint(0F, 9F, 0F);
      topCR2.setTextureSize(16, 16);
      topCR2.mirror = true;
      setRotation(topCR2, 0F, 3.141593F, 0F);
      topHCR3 = new ModelRenderer(this, 3, 8);
      topHCR3.addBox(-4F, 0F, -4F, 12, 2, 8);
      topHCR3.setRotationPoint(0F, 10F, 0F);
      topHCR3.setTextureSize(16, 16);
      topHCR3.mirror = true;
      setRotation(topHCR3, 0F, 3.141593F, 0F);
      centerCR1 = new ModelRenderer(this, 5, 12);
      centerCR1.addBox(-4F, 0F, -3F, 12, 4, 6);
      centerCR1.setRotationPoint(0F, 12F, 0F);
      centerCR1.setTextureSize(16, 16);
      centerCR1.mirror = true;
      setRotation(centerCR1, 0F, 3.141593F, 0F);
      centerCR2 = new ModelRenderer(this, 3, 10);
      centerCR2.addBox(-3F, 0F, -4F, 11, 3, 8);
      centerCR2.setRotationPoint(0F, 12F, 0F);
      centerCR2.setTextureSize(16, 16);
      centerCR2.mirror = true;
      setRotation(centerCR2, 0F, 3.141593F, 0F);
      centerCR3 = new ModelRenderer(this, 3, 13);
      centerCR3.addBox(-3F, 0F, -4F, 6, 1, 8);
      centerCR3.setRotationPoint(0F, 15F, 0F);
      centerCR3.setTextureSize(16, 16);
      centerCR3.mirror = true;
      setRotation(centerCR3, 0F, 3.141593F, 0F);
      //Front
      topCF1 = new ModelRenderer(this, -3, 3);
      topCF1.addBox(-6F, 0F, -6F, 14, 1, 12);
      topCF1.setRotationPoint(0F, 8F, 0F);
      topCF1.setTextureSize(64, 32);
      topCF1.mirror = true;
      setRotation(topCF1, 0F, 1.570796F, 0F);
      topCF2 = new ModelRenderer(this, 1, 5);
      topCF2.addBox(-5F, 0F, -5F, 13, 1, 10);
      topCF2.setRotationPoint(0F, 9F, 0F);
      topCF2.setTextureSize(64, 32);
      topCF2.mirror = true;
      setRotation(topCF2, 0F, 1.570796F, 0F);
      topHCF3 = new ModelRenderer(this, 3, 8);
      topHCF3.addBox(-4F, 0F, -4F, 12, 2, 8);
      topHCF3.setRotationPoint(0F, 10F, 0F);
      topHCF3.setTextureSize(64, 32);
      topHCF3.mirror = true;
      setRotation(topHCF3, 0F, 1.570796F, 0F);
      centerCF1 = new ModelRenderer(this, 5, 12);
      centerCF1.addBox(-4F, 0F, -3F, 12, 4, 6);
      centerCF1.setRotationPoint(0F, 12F, 0F);
      centerCF1.setTextureSize(64, 64);
      centerCF1.mirror = true;
      setRotation(centerCF1, 0F, 1.570796F, 0F);
      centerCF2 = new ModelRenderer(this, 3, 10);
      centerCF2.addBox(-3F, 0F, -4F, 11, 3, 8);
      centerCF2.setRotationPoint(0F, 12F, 0F);
      centerCF2.setTextureSize(64, 64);
      centerCF2.mirror = true;
      setRotation(centerCF2, 0F, 1.570796F, 0F);
      centerCF3 = new ModelRenderer(this, 3, 13);
      centerCF3.addBox(-3F, 0F, -4F, 6, 1, 8);
      centerCF3.setRotationPoint(0F, 15F, 0F);
      centerCF3.setTextureSize(64, 64);
      centerCF3.mirror = true;
      setRotation(centerCF3, 0F, 1.570796F, 0F);
      //Back
      topCB1 = new ModelRenderer(this, -3, 3);
      topCB1.addBox(-6F, 0F, -6F, 14, 1, 12);
      topCB1.setRotationPoint(0F, 8F, 0F);
      topCB1.setTextureSize(16, 16);
      topCB1.mirror = true;
      setRotation(topCB1, 0F, 4.712389F, 0F);
      topCB2 = new ModelRenderer(this, 1, 5);
      topCB2.addBox(-5F, 0F, -5F, 13, 1, 10);
      topCB2.setRotationPoint(0F, 9F, 0F);
      topCB2.setTextureSize(16, 16);
      topCB2.mirror = true;
      setRotation(topCB2, 0F, 4.712389F, 0F);
      topHCB3 = new ModelRenderer(this, 3, 8);
      topHCB3.addBox(-4F, 0F, -4F, 12, 2, 8);
      topHCB3.setRotationPoint(0F, 10F, 0F);
      topHCB3.setTextureSize(16, 16);
      topHCB3.mirror = true;
      setRotation(topHCB3, 0F, 4.712389F, 0F);
      centerCB1 = new ModelRenderer(this, 5, 12);
      centerCB1.addBox(-4F, 0F, -3F, 12, 4, 6);
      centerCB1.setRotationPoint(0F, 12F, 0F);
      centerCB1.setTextureSize(16, 16);
      centerCB1.mirror = true;
      setRotation(centerCB1, 0F, 4.712389F, 0F);
      centerCB2 = new ModelRenderer(this, 3, 10);
      centerCB2.addBox(-3F, 0F, -4F, 11, 3, 8);
      centerCB2.setRotationPoint(0F, 12F, 0F);
      centerCB2.setTextureSize(16, 16);
      centerCB2.mirror = true;
      setRotation(centerCB2, 0F, 4.712389F, 0F);
      centerCB3 = new ModelRenderer(this, 3, 13);
      centerCB3.addBox(-3F, 0F, -4F, 6, 1, 8);
      centerCB3.setRotationPoint(0F, 15F, 0F);
      centerCB3.setTextureSize(16, 16);
      centerCB3.mirror = true;
      setRotation(centerCB3, 0F, 4.712389F, 0F);
      topBase1 = new ModelRenderer(this, 3, 14);
      topBase1.addBox(-3F, 0F, -4F, 6, 8, 8);
      topBase1.setRotationPoint(0F, 16F, 0F);
      topBase1.setTextureSize(16, 16);
      topBase1.mirror = true;
      setRotation(topBase1, 0F, 0F, 0F);
      topBase2 = new ModelRenderer(this, 4, 16);
      topBase2.addBox(-4F, 0F, -3F, 8, 8, 6);
      topBase2.setRotationPoint(0F, 16F, 0F);
      topBase2.setTextureSize(16, 16);
      topBase2.mirror = true;
      setRotation(topBase2, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    bottom1.render(f5);
    bottom2.render(f5);
    col1.render(f5);
    col2.render(f5);
    bottomH3.render(f5);
    top1.render(f5);
    top2.render(f5);
    topH3.render(f5);
    colBottom1.render(f5);
    colBottom2.render(f5);
    colTopBottom1.render(f5);
    colTopBottom2.render(f5);
    //Left
    topCL1.render(f5);
    topCL2.render(f5);
    topHCL3.render(f5);
    centerCL1.render(f5);
    centerCL2.render(f5);
    centerCL3.render(f5);
    //Right
    topCR1.render(f5);
    topCR2.render(f5);
    topHCR3.render(f5);
    centerCR1.render(f5);
    centerCR2.render(f5);
    centerCR3.render(f5);
    //Front
    topCF1.render(f5);
    topCF2.render(f5);
    topHCF3.render(f5);
    centerCF1.render(f5);
    centerCF2.render(f5);
    centerCF3.render(f5);
    topBase1.render(f5);
    topBase2.render(f5);
  }
  
  public void renderFull(float f5)
  {
	  	bottom1.render(f5);
	  	bottom2.render(f5);
	  	bottomH3.render(f5);
	  	top1.render(f5);
	  	top2.render(f5);
	  	topH3.render(f5);
	  	colTopBottom1.render(f5);
	  	colTopBottom2.render(f5);
  }
  public void renderTop(float f5)
  {
	    top1.render(f5);
	    top2.render(f5);
	    topH3.render(f5);
	    colTop1.render(f5);
	    colTop2.render(f5);
  }
  public void renderBottom(float f5)
  {
	    bottom1.render(f5);
	    bottom2.render(f5);
	    bottomH3.render(f5);
	    colBottom1.render(f5);
	    colBottom2.render(f5);
  }
  public void renderMiddle(float f5)
  {
	    col1.render(f5);
	    col2.render(f5);
  }
  public void renderLeft(float f5){
	    topCL1.render(f5);
	    topCL2.render(f5);
	    topHCL3.render(f5);
	    centerCL1.render(f5);
	    centerCL2.render(f5);
//	    centerCL3.render(f5);  
  }
  
  public void renderRight(float f5){
	    topCR1.render(f5);
	    topCR2.render(f5);
	    topHCR3.render(f5);
	    centerCR1.render(f5);
	    centerCR2.render(f5);
//	    centerCR3.render(f5); 
  }
  
  public void renderFront(float f5){
	    topCF1.render(f5);
	    topCF2.render(f5);
	    topHCF3.render(f5);
	    centerCF1.render(f5);
	    centerCF2.render(f5);
//	    centerCF3.render(f5);  
  }
  
  public void renderBack(float f5){
	    topCB1.render(f5);
	    topCB2.render(f5);
	    topHCB3.render(f5);
	    centerCB1.render(f5);
	    centerCB2.render(f5);
//	    centerCB3.render(f5); 
  }
  
public void renderTopBase(float f5)
{
    topBase1.render(f5);
    topBase2.render(f5);
    centerCL3.render(f5);
    centerCF3.render(f5);
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

