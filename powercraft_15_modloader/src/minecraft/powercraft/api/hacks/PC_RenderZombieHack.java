package powercraft.api.hacks;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderZombie;

import org.lwjgl.opengl.GL11;

public class PC_RenderZombieHack extends RenderZombie {
	
	
	/**
	 * Queries whether should render the specified pass or not.
	 */
	@Override
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		ItemStack var4 = par1EntityLiving.getCurrentArmor(3 - par2);
		
		if (var4 != null) {
			Item var5 = var4.getItem();
			
			if (var5 instanceof ItemArmor) {
				ItemArmor var6 = (ItemArmor) var5;
				this.loadTexture(PC_Hooks.getArmorTexture(var6, var4, par1EntityLiving, par2, 1, null));
				ModelBiped var7 = par2 == 2 ? this.field_82425_h : this.field_82423_g;
				var7.bipedHead.showModel = par2 == 0;
				var7.bipedHeadwear.showModel = par2 == 0;
				var7.bipedBody.showModel = par2 == 1 || par2 == 2;
				var7.bipedRightArm.showModel = par2 == 1;
				var7.bipedLeftArm.showModel = par2 == 1;
				var7.bipedRightLeg.showModel = par2 == 2 || par2 == 3;
				var7.bipedLeftLeg.showModel = par2 == 2 || par2 == 3;
				var7 = PC_Hooks.getArmorModel(var6, var4, par1EntityLiving, par2, var7);
				this.setRenderPassModel(var7);
				
				if (var7 != null) {
					var7.onGround = this.mainModel.onGround;
				}
				
				if (var7 != null) {
					var7.isRiding = this.mainModel.isRiding;
				}
				
				if (var7 != null) {
					var7.isChild = this.mainModel.isChild;
				}
				
				float var8 = 1.0F;
				
				if (var6.getArmorMaterial() == EnumArmorMaterial.CLOTH) {
					int var9 = var6.getColor(var4);
					float var10 = (float) (var9 >> 16 & 255) / 255.0F;
					float var11 = (float) (var9 >> 8 & 255) / 255.0F;
					float var12 = (float) (var9 & 255) / 255.0F;
					GL11.glColor3f(var8 * var10, var8 * var11, var8 * var12);
					
					if (var4.isItemEnchanted()) {
						return 31;
					}
					
					return 16;
				}
				
				GL11.glColor3f(var8, var8, var8);
				
				if (var4.isItemEnchanted()) {
					return 15;
				}
				
				return 1;
			}
		}
		
		return -1;
	}
	
	@Override
	protected void func_82408_c(EntityLiving par1EntityLiving, int par2, float par3) {
		ItemStack var4 = par1EntityLiving.getCurrentArmor(3 - par2);
		
		if (var4 != null) {
			Item var5 = var4.getItem();
			
			if (var5 instanceof ItemArmor) {
				ItemArmor var6 = (ItemArmor) var5;
				this.loadTexture(PC_Hooks.getArmorTexture(var6, var4, par1EntityLiving, par2, 2, null));
				float var7 = 1.0F;
				GL11.glColor3f(var7, var7, var7);
			}
		}
	}
	
}
