package powercraft.light;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityPlayer;
import powercraft.management.PC_Utils.Lang;

public class PCli_DamageSourceLaser extends DamageSource {

	private static PCli_DamageSourceLaser instance=null;
	
	public static PCli_DamageSourceLaser getDamageSource(){
		if(instance==null)
			instance = new PCli_DamageSourceLaser();
		return instance;
	}
	
	private PCli_DamageSourceLaser(){
		super("laser");
	}
	
	public String getDeathMessage(EntityPlayer par1EntityPlayer)
    {
        return Lang.tr("pc.damage.laser", par1EntityPlayer.username);
    }
	
}
