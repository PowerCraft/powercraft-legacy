package powercraft.light;

import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import powercraft.management.PC_FakePlayer;
import powercraft.management.PC_Utils.Lang;

public class PCli_DamageSourceLaser extends DamageSource {
	
	private static PCli_DamageSourceLaser instance;
	
	public static DamageSource getDamageSource() {
		if(instance==null)
			instance = new PCli_DamageSourceLaser();
		return instance;
	}
	
	private PCli_DamageSourceLaser(){
		super("laser");
	}
	
	@Override
	public String getDeathMessage(EntityPlayer par1EntityPlayer)
    {
        return Lang.tr("pc.damage.laser", par1EntityPlayer.username);
    }
	
}
