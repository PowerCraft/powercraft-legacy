package powercraft.light;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import powercraft.management.PC_Color;
import powercraft.management.PC_ITileEntityRenderer;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.SaveHandler;

public class PCli_TileEntityLight extends PC_TileEntity implements PC_ITileEntityRenderer
{
    private PC_Color color = new PC_Color();

    private boolean isStable;

    private boolean isHuge;

    private static PCli_ModelLight model = new PCli_ModelLight();
    
    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

        SaveHandler.loadFromNBT(nbttagcompound, "color", color);
        isStable = nbttagcompound.getBoolean("stable");
        isHuge = nbttagcompound.getBoolean("huge");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        
        SaveHandler.saveToNBT(nbttagcompound, "color", color);

        nbttagcompound.setBoolean("stable", isStable);
        nbttagcompound.setBoolean("huge", isHuge);
    }

    public void setColor(PC_Color c)
    {
        color.setTo(c);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public PC_Color getColor()
    {
        return color;
    }

    public void setStable(boolean stable)
    {
        PC_PacketHandler.setTileEntity(this, "isStable", stable);
        isStable = stable;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public boolean isStable()
    {
        return isStable;
    }

    public void setHuge(boolean huge)
    {
        PC_PacketHandler.setTileEntity(this, "isHuge", huge);
        isHuge = huge;
    }

    public boolean isHuge()
    {
        return isHuge;
    }

    public boolean isActive()
    {
        return GameInfo.getBID(worldObj, xCoord, yCoord, zCoord) == PCli_BlockLight.on.blockID;
    }

    @Override
    public void setData(Object[] o)
    {
        int p = 0;

        while (p < o.length)
        {
            String var = (String)o[p++];

            if (var.equals("color"))
            {
                color.setTo((PC_Color)o[p++]);
            }
            else if (var.equals("isStable"))
            {
                isStable = (Boolean)o[p++];
                PCli_BlockLight bLight = (PCli_BlockLight)getBlockType();

                if (isStable)
                {
                    bLight.onPoweredBlockChange(worldObj, xCoord, yCoord, zCoord, true);
                }
                else
                {
                    bLight.updateTick(worldObj, xCoord, yCoord, zCoord, new Random());
                }
            }
            else if (var.equals("isHuge"))
            {
                isHuge = (Boolean)o[p++];
            }
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public Object[] getData()
    {
        return new Object[]
                {
                    "color", color,
                    "isStable", isStable,
                    "isHuge", isHuge
                };
    }

	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {

		PC_Renderer.glPushMatrix();
		float f = 1.0F;

		PC_Renderer.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		
		PC_Renderer.bindTexture(ModuleInfo.getTextureDirectory(ModuleInfo.getModule("Light")) + "block_light.png");

		PC_Renderer.glPushMatrix();
		PC_Renderer.glScalef(f, -f, -f);

		PC_Color clr = getColor();
		if(clr!=null)
			PC_Renderer.glColor4f(clr.x, clr.y, clr.z, 1.0f);
		else
			PC_Renderer.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		int meta = GameInfo.getMD(worldObj, getCoord());
		switch (meta) {
			case 0:
				break;
			case 1:
				PC_Renderer.glRotatef(-90, 1, 0, 0);
				break;
			case 2:
				PC_Renderer.glRotatef(90, 1, 0, 0);
				break;
			case 3:
				PC_Renderer.glRotatef(-90, 0, 0, 1);
				break;
			case 4:
				PC_Renderer.glRotatef(90, 0, 0, 1);
				break;
			case 5:
				PC_Renderer.glRotatef(180, 1, 0, 0);
				break;
		}

		if (isHuge()) {
			model.renderHuge();
		} else {
			model.renderNormal();
		}

		PC_Renderer.glPopMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
}
