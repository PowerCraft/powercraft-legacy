package powercraft.light;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import powercraft.api.registry.PC_LangRegistry;

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
	public String getDeathMessage(EntityLiving par1EntityLiving){
        return PC_LangRegistry.tr("pc.damage.laser", par1EntityLiving.func_96090_ax());
    }
	
}
