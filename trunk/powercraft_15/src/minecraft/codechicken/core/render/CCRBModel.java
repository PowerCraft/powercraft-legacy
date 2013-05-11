package codechicken.core.render;

import codechicken.core.vec.Rotation;
import codechicken.core.vec.Vector3;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class CCRBModel extends CCModel
{
    public static class LC
    {
        public LC(int s, float e, float f, float g, float h)
        {
            side = s;
            fa = e;
            fb = f;
            fc = g;
            fd = h;
        }
        
        public int side;
        public float fa;
        public float fb;
        public float fc;
        public float fd;
        
        public static LC compute(Vector3 vec, Vector3 normal)
        {
            int side = findSide(normal);
            if(side < 0)
                throw new IllegalArgumentException("Non axial vertex normal "+normal);
            return compute(vec, side);
        }
        
        public static LC compute(Vector3 vec, int side)
        {
            boolean offset = false;
            switch(side)
            {
                case 0: offset = vec.y <= 0; break;
                case 1: offset = vec.y >= 1; break;
                case 2: offset = vec.z <= 0; break;
                case 3: offset = vec.z >= 1; break;
                case 4: offset = vec.x <= 0; break;
                case 5: offset = vec.x >= 1; break;
            }
            if(!offset)
                side+=6;
            Vector3 v1 = Rotation.axes[((side&0xE)+3)%6];
            Vector3 v2 = Rotation.axes[((side&0xE)+5)%6];
            float d1 = (float) vec.scalarProject(v1);
            float d2 = 1-d1;
            float d3 = (float) vec.scalarProject(v2);
            float d4 = 1-d3;
            return new LC(side, d2*d4, d2*d3, d1*d4, d1*d3);
        }
    }
    
    public static class LightMatrix
    {
        public int computed = 0;
        public float[][] ao = new float[12][4];
        public int[][] brightness = new int[12][4];
        
        private float[] aSamples = new float[27];
        private int[] bSamples = new int[27];
        
        /**
         * The 9 positions in the sample array for each side, sides >= 6 are centered on sample 13 (the block itself)
         */
        public static final int[][] ssamplem = new int[][]{
            { 0, 1, 2, 3, 4, 5, 6, 7, 8},
            {18,19,20,21,22,23,24,25,26},
            { 0, 9,18, 1,10,19, 2,11,20},
            { 6,15,24, 7,16,25, 8,17,26},
            { 0, 3, 6, 9,12,15,18,21,24},
            { 2, 5, 8,11,14,17,20,23,26},
            { 9,10,11,12,13,14,15,16,17},
            { 9,10,11,12,13,14,15,16,17},
            { 3,12,21, 4,13,22, 5,14,23},
            { 3,12,21, 4,13,22, 5,14,23},
            { 1, 4, 7,10,13,16,19,22,25},
            { 1, 4, 7,10,13,16,19,22,25}};
        public static final int[][] qsamplem = new int[][]{//the positions in the side sample array for each corner
            {0,1,3,4},
            {5,1,2,4},
            {6,7,3,4},
            {5,7,8,4}};
        public static final float[] sideao = new float[]{
            0.5F, 1F, 0.8F, 0.8F, 0.6F, 0.6F, 
            0.5F, 1F, 0.8F, 0.8F, 0.6F, 0.6F};
        
        /*static
        {
            int[][] os = new int[][]{
                    {0,-1,0},
                    {0, 1,0},
                    {0,0,-1},
                    {0,0, 1},
                    {-1,0,0},
                    { 1,0,0}};
            
            for(int s = 0; s < 12; s++)
            {
                int[] d0 = s < 6 ? new int[]{os[s][0]+1, os[s][1]+1, os[s][2]+1} : new int[]{1, 1, 1};
                int[] d1 = os[((s&0xE)+3)%6];
                int[] d2 = os[((s&0xE)+5)%6];
                for(int a = -1; a <= 1; a++)
                    for(int b = -1; b <= 1; b++)
                        ssamplem[s][(a+1)*3+b+1] = (d0[1]+d1[1]*a+d2[1]*b)*9+(d0[2]+d1[2]*a+d2[2]*b)*3+(d0[0]+d1[0]*a+d2[0]*b);
            }
            System.out.println(Arrays.deepToString(ssamplem));
        }*/
        
        public void computeAt(IBlockAccess a, int x, int y, int z)
        {
            computed = 0;
            //inc x, inc z, inc y
            sample( 0, aSamples, bSamples, a, x-1, y-1, z-1);
            sample( 1, aSamples, bSamples, a, x  , y-1, z-1);
            sample( 2, aSamples, bSamples, a, x+1, y-1, z-1);
            sample( 3, aSamples, bSamples, a, x-1, y-1, z  );
            sample( 4, aSamples, bSamples, a, x  , y-1, z  );
            sample( 5, aSamples, bSamples, a, x+1, y-1, z  );
            sample( 6, aSamples, bSamples, a, x-1, y-1, z+1);
            sample( 7, aSamples, bSamples, a, x  , y-1, z+1);
            sample( 8, aSamples, bSamples, a, x+1, y-1, z+1);
            sample( 9, aSamples, bSamples, a, x-1, y  , z-1);
            sample(10, aSamples, bSamples, a, x  , y  , z-1);
            sample(11, aSamples, bSamples, a, x+1, y  , z-1);
            sample(12, aSamples, bSamples, a, x-1, y  , z  );
            sample(13, aSamples, bSamples, a, x  , y  , z  );
            sample(14, aSamples, bSamples, a, x+1, y  , z  );
            sample(15, aSamples, bSamples, a, x-1, y  , z+1);
            sample(16, aSamples, bSamples, a, x  , y  , z+1);
            sample(17, aSamples, bSamples, a, x+1, y  , z+1);
            sample(18, aSamples, bSamples, a, x-1, y+1, z-1);
            sample(19, aSamples, bSamples, a, x  , y+1, z-1);
            sample(20, aSamples, bSamples, a, x+1, y+1, z-1);
            sample(21, aSamples, bSamples, a, x-1, y+1, z  );
            sample(22, aSamples, bSamples, a, x  , y+1, z  );
            sample(23, aSamples, bSamples, a, x+1, y+1, z  );
            sample(24, aSamples, bSamples, a, x-1, y+1, z+1);
            sample(25, aSamples, bSamples, a, x  , y+1, z+1);
            sample(26, aSamples, bSamples, a, x+1, y+1, z+1);
        }
        
        public int[] brightness(int side)
        {
            sideSample(side);
            return brightness[side];
        }
        
        public float[] ao(int side)
        {
            sideSample(side);
            return ao[side];
        }
        
        public void sideSample(int side)
        {
            if((computed&1<<side) == 0)
            {
                int[] ssample = ssamplem[side];
                for(int q = 0; q < 4; q++)
                {
                    int[] qsample = qsamplem[q];
                    if(Minecraft.isAmbientOcclusionEnabled())
                        interp(side, q, ssample[qsample[0]], ssample[qsample[1]], ssample[qsample[2]], ssample[qsample[3]]);
                    else
                        interp(side, q, ssample[4], ssample[4], ssample[4], ssample[4]);
                }
                computed|=1<<side;
            }
        }

        private void sample(int i, float[] aSamples, int[] bSamples, IBlockAccess a, int x, int y, int z)
        {
            int bid = a.getBlockId(x, y, z);
            Block b = Block.blocksList[bid];
            if(b == null)
            {
                bSamples[i] = a.getLightBrightnessForSkyBlocks(x, y, z, 0);
                aSamples[i] = 1;
            }
            else
            {
                bSamples[i] = a.getLightBrightnessForSkyBlocks(x, y, z, b.getLightValue(a, x, y, z));
                aSamples[i] = b.getAmbientOcclusionLightValue(a, x, y, z);
            }
        }
        
        private void interp(int s, int q, int a, int b, int c, int d)
        {
            ao[s][q] = interpAO(aSamples[a], aSamples[b], aSamples[c], aSamples[d])*sideao[s];
            brightness[s][q] = interpBrightness(bSamples[a], bSamples[b], bSamples[c], bSamples[d]);
        }
        
        public static float interpAO(float a, float b, float c, float d)
        {
            return (a+b+c+d)/4F;
        }
        
        public static int interpBrightness(int a, int b, int c, int d)
        {
            if(a == 0)
                a = d;
            if(b == 0)
                b = d;
            if(c == 0)
                c = d;
            return (a+b+c+d)>>2 & 0xFF00FF;
        }
        

        public void setColour(Tessellator tess, LC lc, int c)
        {
            
            float[] a = ao(lc.side);
            float f = (a[0]*lc.fa + a[1]*lc.fb + a[2]*lc.fc + a[3]*lc.fd);
            tess.setColorRGBA((int)((c>>>24)*f), (int)((c>>16&0xFF)*f), (int)((c>>8&0xFF)*f), (c&0xFF));
        }

        public void setBrightness(Tessellator tess, LC lc)
        {
            int[] b = brightness(lc.side);
            tess.setBrightness((int)(b[0]*lc.fa + b[1]*lc.fb + b[2]*lc.fc+b[3]*lc.fd) & 0xFF00FF);
        }
    }
    
    public LC[] lightCoefficents;
    private LightMatrix lightMatrix;
    
    /**
     * Lighting sides and coefficients are computed for each vertex of the model. All faces must be axis planar and all verts are assumed to be in the range (0,0,0) to (1,1,1)
     */
    protected CCRBModel(CCModel m)
    {
        super(m.vertexMode);
        verts = new Vertex5[m.verts.length];
        copy(m, 0, this, 0, m.verts.length);
        if(normals == null)
            computeNormals();
        if(colours == null)
            setColour(-1);
        computeLighting();
    }

    private void computeLighting()
    {
        lightCoefficents = new LC[verts.length];
        for(int k = 0; k < verts.length; k++)
            lightCoefficents[k] = LC.compute(verts[k].vec, normals[k]);
    }
    
    public CCRBModel setLightMatrix(LightMatrix m)
    {
        lightMatrix = m;
        return this;
    }
    
    @Override
    public void applyColour(Tessellator tess, int i)
    {
        if(lightMatrix == null)
             super.applyColour(tess, i);
        else
            lightMatrix.setColour(tess, lightCoefficents[i], colours[i]);
    }
    
    @Override
    public void applyVertexModifiers(Tessellator tess, int i)
    {
        if(lightMatrix != null)
            lightMatrix.setBrightness(tess, lightCoefficents[i]);
    }
}
