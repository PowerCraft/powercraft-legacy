package codechicken.core.vec;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Rotation implements ITransformation
{
    public static Rotation[] sideRotations = new Rotation[]{
        new Rotation( 0, 1, 0, 0),
        new Rotation( Math.PI, 1, 0, 0),
        new Rotation( Math.PI/2, 1, 0, 0),
        new Rotation(-Math.PI/2, 1, 0, 0),
        new Rotation(-Math.PI/2, 0, 0, 1),
        new Rotation( Math.PI/2, 0, 0, 1)};
    public static Rotation[] sideRotationsR = new Rotation[6];
    public static Quat[] sideQuats = new Quat[6];
    public static Quat[] sideQuatsR = new Quat[6];
    
    public static Vector3[] axes = new Vector3[]{
        new Vector3( 0,-1, 0),
        new Vector3( 0, 1, 0),
        new Vector3( 0, 0,-1),
        new Vector3( 0, 0, 1),
        new Vector3(-1, 0, 0),
        new Vector3( 1, 0, 0)};
    
    static
    {
        for(int i = 0; i < 6; i++)
        {
            Rotation r = sideRotations[i];
            sideRotationsR[i] = new Rotation(-r.angle, r.axis);
            sideQuats[i] = r.toQuat();
            sideQuatsR[i] = sideRotationsR[i].toQuat();
        }
    }
    
    public double angle;
    public Vector3 axis;
    public Vector3 point;
    
    private Vector3 negate;
    private Quat quat;
    
    public Rotation(double angle, Vector3 axis, Vector3 point)
    {
        this.angle = angle;
        this.axis = axis;
        this.point = point;
    }
    
    public Rotation(double angle, Vector3 axis)
    {
        this(angle, axis, null);
    }
    
    public Rotation(double angle, double x, double y, double z)
    {
        this(angle, new Vector3(x, y, z));
    }

    @Override
    public void transform(Vector3 vec)
    {
        if(quat == null)
            quat = Quat.aroundAxis(axis, angle);
        
        if(point == null)
            vec.rotate(quat);
        else
            vec.subtract(point).rotate(quat).add(point);
    }

    @Override
    public void apply(Matrix4 mat)
    {
        if(point == null)
            mat.rotate(angle, axis);
        else
        {
            if(negate == null)
                negate = point.copy().negate();
            
            mat.translate(point);
            mat.rotate(angle, axis);
            mat.translate(negate);
        }
    }
    
    public Quat toQuat()
    {
        if(quat == null)
            quat = Quat.aroundAxis(axis, angle);
        return quat;
    }

    @SideOnly(Side.CLIENT)
    public void glRotate()
    {
        if(point == null)
            GL11.glRotatef((float)angle*57.2958F, (float)axis.x, (float)axis.y, (float)axis.z);
        else
        {
            GL11.glTranslated(point.x, point.y, point.z);
            GL11.glRotatef((float)angle*57.2958F, (float)axis.x, (float)axis.y, (float)axis.z);
            GL11.glTranslated(-point.x, -point.y, -point.z);
        }
    }
}
