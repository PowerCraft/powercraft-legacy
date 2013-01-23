package codechicken.nei;

public abstract class Button2ActiveState extends Button
{    
    @Override
    public Image getRenderIcon()
    {
    	if((state & 0x8) != 0)
    		return icon2;
    	
    	return icon;
    }

    public Image icon2;
}
