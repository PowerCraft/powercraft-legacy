package net.minecraft.src;

public class EntityBodyHelper
{
    private EntityLiving field_75668_a;
    private int field_75666_b = 0;
    private float field_75667_c = 0.0F;

    public EntityBodyHelper(EntityLiving par1EntityLiving)
    {
        this.field_75668_a = par1EntityLiving;
    }

    public void func_75664_a()
    {
        double var1 = this.field_75668_a.posX - this.field_75668_a.prevPosX;
        double var3 = this.field_75668_a.posZ - this.field_75668_a.prevPosZ;

        if (var1 * var1 + var3 * var3 > 2.500000277905201E-7D)
        {
            this.field_75668_a.renderYawOffset = this.field_75668_a.rotationYaw;
            this.field_75668_a.rotationYawHead = this.func_75665_a(this.field_75668_a.renderYawOffset, this.field_75668_a.rotationYawHead, 75.0F);
            this.field_75667_c = this.field_75668_a.rotationYawHead;
            this.field_75666_b = 0;
        }
        else
        {
            float var5 = 75.0F;

            if (Math.abs(this.field_75668_a.rotationYawHead - this.field_75667_c) > 15.0F)
            {
                this.field_75666_b = 0;
                this.field_75667_c = this.field_75668_a.rotationYawHead;
            }
            else
            {
                ++this.field_75666_b;

                if (this.field_75666_b > 10)
                {
                    var5 = Math.max(1.0F - (float)(this.field_75666_b - 10) / 10.0F, 0.0F) * 75.0F;
                }
            }

            this.field_75668_a.renderYawOffset = this.func_75665_a(this.field_75668_a.rotationYawHead, this.field_75668_a.renderYawOffset, var5);
        }
    }

    private float func_75665_a(float par1, float par2, float par3)
    {
        float var4 = MathHelper.wrapAngleTo180_float(par1 - par2);

        if (var4 < -par3)
        {
            var4 = -par3;
        }

        if (var4 >= par3)
        {
            var4 = par3;
        }

        return par1 - var4;
    }
}
