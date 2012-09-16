package net.minecraft.src;

public class PCma_LaserDamageSource extends DamageSource {

	protected PCma_LaserDamageSource() {
		super("laser");
	}
	
	public String func_76360_b(EntityPlayer par1EntityPlayer)
    {
        return PC_Lang.tr("pc.laser.kill", par1EntityPlayer.username);
    }

}
