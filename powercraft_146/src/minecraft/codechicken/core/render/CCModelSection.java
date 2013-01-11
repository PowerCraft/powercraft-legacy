package codechicken.core.render;

import codechicken.core.Quat;
import codechicken.core.Vector3;
import net.minecraft.client.renderer.Tessellator;

public class CCModelSection
{
	double textureWidth;
	double textureHeight;
	int tilesU;
	int tilesV;
	double offU;
	double offV;
	
	public Quat rotation;
	private Vector3 vec = new Vector3();
	
	public CCModelSection(int width, int height)
	{
		textureWidth = width;
		textureHeight = height;
	}
	
	public CCModelSection(int tileWidth, int tileHeight, int tilesX, int tilesY)
	{
		textureWidth = tileWidth*tilesX;
		textureHeight = tileHeight*tilesY;
		this.tilesU = tilesX;
		this.tilesV = tilesY;
	}
	
	public void setTile(int x, int y)
	{
		offU = textureWidth*x/tilesU;
		offV = textureHeight*y/tilesV;
	}

    public void addVertex(double x, double y, double z, double u, double v)
    {
        vec.set(x, y, z);
        if(rotation != null)
            rotation.rotate(vec);
        Tessellator.instance.addVertexWithUV(vec.x, vec.y, vec.z, (offU+u)/textureWidth, (offV+v)/textureHeight);
        
    }
    
    @Deprecated
	public void addVertex(Vertex5 vert)
	{
	    addVertex(vert.vec.x, vert.vec.y, vert.vec.z, vert.u, vert.v);
	}

    public CCModelSection transform(Quat quat)
    {
        rotation = quat;
        return this;
    }
}
