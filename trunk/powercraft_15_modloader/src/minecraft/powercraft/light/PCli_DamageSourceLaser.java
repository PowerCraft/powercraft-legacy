package powercraft.light;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityLiving;
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
        return PC_LangRegistry.tr("pc.damage.laser", par1EntityLiving.getTranslatedEntityName());
    }
	
}
