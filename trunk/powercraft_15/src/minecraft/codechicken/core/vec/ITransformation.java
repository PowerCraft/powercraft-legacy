package codechicken.core.vec;


public interface ITransformation
{   
    public void transform(Vector3 vec);
    
    public void apply(Matrix4 mat);
}
