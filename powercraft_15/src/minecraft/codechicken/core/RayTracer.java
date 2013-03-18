package codechicken.core;

import java.util.List;

import codechicken.core.RayTracer.IndexedCuboid6;
import codechicken.core.alg.MathHelper;
import codechicken.core.vec.BlockCoord;
import codechicken.core.vec.Cuboid6;
import codechicken.core.vec.Vector3;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class RayTracer
{    
    public static class IndexedCuboid6 extends Cuboid6
    {
        public int index;
        
        public IndexedCuboid6(int index, Cuboid6 cuboid)
        {
            super(cuboid);
            this.index = index;
        }
    }

    private Vector3 vec = new Vector3();
    private Vector3 vec2 = new Vector3();

    private Vector3 s_vec = new Vector3();
    private double s_dist;
    private int s_side;
    private IndexedCuboid6 c_cuboid;
    
    private void traceSide(int side, Vector3 start, Vector3 end, Cuboid6 cuboid)
    {
        vec.set(start);
        Vector3 hit = null;
        switch(side)
        {
            case 0:
                hit = vec.XZintercept(end, cuboid.min.y);
                break;
            case 1:
                hit = vec.XZintercept(end, cuboid.max.y);
                break;
            case 2:
                hit = vec.XYintercept(end, cuboid.min.z);
                break;
            case 3:
                hit = vec.XYintercept(end, cuboid.max.z);
                break;
            case 4:
                hit = vec.YZintercept(end, cuboid.min.x);
                break;
            case 5:
                hit = vec.YZintercept(end, cuboid.max.x);
                break;
        }
        if(hit == null)
            return;
        
        switch(side)
        {
            case 0:
            case 1:
                if(!MathHelper.between(cuboid.min.x, hit.x, cuboid.max.x) || !MathHelper.between(cuboid.min.z, hit.z, cuboid.max.z)) return;
                break;
            case 2:
            case 3:
                if(!MathHelper.between(cuboid.min.x, hit.x, cuboid.max.x) || !MathHelper.between(cuboid.min.y, hit.y, cuboid.max.y)) return;
                break;
            case 4:
            case 5:
                if(!MathHelper.between(cuboid.min.y, hit.y, cuboid.max.y) || !MathHelper.between(cuboid.min.z, hit.z, cuboid.max.z)) return;
                break;
        }
        
        double dist = vec2.set(hit).subtract(start).magSquared();
        if(dist < s_dist)
        {
            s_side = side;
            s_dist = dist;
            s_vec.set(vec);
        }
    }
    
    public MovingObjectPosition rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid)
    {
        s_dist = Double.MAX_VALUE;
        s_side = -1;
        
        for(int i = 0; i < 6; i++)
            traceSide(i, start, end, cuboid);
        
        if(s_side < 0)
            return null;
        
        MovingObjectPosition mop = new MovingObjectPosition(0, 0, 0, s_side, s_vec.toVec3D());
        mop.typeOfHit = null;
        return mop;
    }

    public MovingObjectPosition rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid, BlockCoord pos)
    {
        MovingObjectPosition mop = rayTraceCuboid(start, end, cuboid);
        if(mop != null)
        {
            mop.typeOfHit = EnumMovingObjectType.TILE;
            mop.blockX = pos.x;
            mop.blockY = pos.y;
            mop.blockZ = pos.z;
        }
        return mop;
    }

    public MovingObjectPosition rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid, Entity e)
    {
        MovingObjectPosition mop = rayTraceCuboid(start, end, cuboid);
        if(mop != null)
        {
            mop.typeOfHit = EnumMovingObjectType.ENTITY;
            mop.entityHit = e;
        }
        return mop;
    }
    
    public MovingObjectPosition rayTraceCuboids(Vector3 start, Vector3 end, List<IndexedCuboid6> cuboids)
    {
        double c_dist = Double.MAX_VALUE;
        MovingObjectPosition c_hit = null;
        
        for(IndexedCuboid6 cuboid : cuboids)
        {
            MovingObjectPosition mop = rayTraceCuboid(start, end, cuboid);
            if(mop != null && s_dist < c_dist)
            {
                mop.subHit = cuboid.index;
                c_dist = s_dist;
                c_hit = mop;
                c_cuboid = cuboid;
            }
        }
        
        return c_hit;
    }

    public MovingObjectPosition rayTraceCuboids(Vector3 start, Vector3 end, List<IndexedCuboid6> cuboids, BlockCoord pos, Block block)
    {
        MovingObjectPosition mop = rayTraceCuboids(start, end, cuboids);
        if(mop != null)
        {
            mop.typeOfHit = EnumMovingObjectType.TILE;
            mop.blockX = pos.x;
            mop.blockY = pos.y;
            mop.blockZ = pos.z;
            if(block != null)
                c_cuboid.add(new Vector3(-pos.x, -pos.y, -pos.z)).setBlockBounds(block);
        }
        return mop;
    }
}
