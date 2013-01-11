package codechicken.core.render;

import net.minecraft.client.renderer.Tessellator;
import codechicken.core.render.Vertex5;

public class CCTextureSection extends CCModelSection
{
    public CCTextureSection(int width, int height)
    {
        super(width, height);
    }

    public void addVertex(Vertex5 vert)
    {
        Tessellator.instance.addVertexWithUV(vert.vec.x, vert.vec.y, vert.vec.z, (offU+vert.u)/textureWidth, (offV+vert.v)/textureHeight);
    }
}
