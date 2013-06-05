package codechicken.core.render;

import net.minecraft.util.Icon;

public class IconTransformation implements IUVTransformation
{
    public Icon icon;
    
    public IconTransformation(Icon icon)
    {
        this.icon = icon;
    }
    
    @Override
    public void transform(UV texcoord)
    {
        texcoord.u = icon.getInterpolatedU(texcoord.u%1*16);
        texcoord.v = icon.getInterpolatedV(texcoord.v%1*16);
    }
}
