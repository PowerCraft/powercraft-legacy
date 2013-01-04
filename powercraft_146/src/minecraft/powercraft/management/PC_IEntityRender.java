package powercraft.management;

import java.util.List;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

public interface PC_IEntityRender {
	
	public List<PC_Struct2<Class<? extends Entity>, Render>> registerEntityRender(List<PC_Struct2<Class<? extends Entity>, Render>> list);
	
}

