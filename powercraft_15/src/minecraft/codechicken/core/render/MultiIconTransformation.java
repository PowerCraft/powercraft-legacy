package codechicken.core.render;

import net.minecraft.util.Icon;

/**
 * Warning, when using this class, make sure UV coords are always < i+1 if they are not to be on the next icon index. (1 to 0.9999)
 */
public class MultiIconTransformation implements IUVTransformation
{
    public Icon[] icons;
    
    public MultiIconTransformation(Icon[] icons)
    {
        this.icons = icons;
    }
    
    @Override
    public void transform(UV texcoord)
    {
        int i = (int)texcoord.u+(int)texcoord.v;
        Icon icon = icons[i%icons.length];
        texcoord.u = icon.getInterpolatedU(texcoord.u%1*16);
        texcoord.v = icon.getInterpolatedV(texcoord.v%1*16);
    }
}
