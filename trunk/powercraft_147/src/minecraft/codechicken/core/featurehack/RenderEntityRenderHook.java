package codechicken.core.featurehack;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.Render;

public class RenderEntityRenderHook extends Render
{
	@Override
	public void doRender(Entity entity, double x, double y, double z, float f, float frame)
	{
		EntityRenderHook hook = (EntityRenderHook)entity;
		GL11.glTranslated(x-hook.posX, y-hook.posY, z-hook.posZ);
		((EntityRenderHook)entity).callback.render(frame);
		GL11.glTranslated(hook.posX-x, hook.posY-y, hook.posZ-z);
	}
}
