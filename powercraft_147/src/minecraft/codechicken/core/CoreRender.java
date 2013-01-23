package codechicken.core;

import net.minecraft.client.renderer.Tessellator;

public class CoreRender
{
	public static void addVecWithUV(Vector3 vec, double u, double v)
	{
		Tessellator.instance.addVertexWithUV(vec.x, vec.y, vec.z, u, v);
	}
	
	public static void addVec(Vector3 vec)
	{
		Tessellator.instance.addVertex(vec.x, vec.y, vec.z);
	}
}
