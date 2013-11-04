package powercraft.api.gres;

import java.lang.reflect.Array;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import powercraft.api.PC_RectI;
import powercraft.api.PC_Vec2I;
import powercraft.api.gres.events.PC_GresEvent;
import powercraft.api.gres.events.PC_GresMouseButtonEvent;
import powercraft.api.gres.events.PC_IGresEventListener;

@SideOnly(Side.CLIENT)
public class PC_GresDisplayObject extends PC_GresComponent {

	private Object display;
	
	public PC_GresDisplayObject(Object display){
		setDisplayObject(display);
	}
	
	public Object getDisplayObject(){
		if(display instanceof ObjectChange){
			return ((ObjectChange)display).display;
		}
		return display;
	}
	
	public Object getActiveDisplayObject(){
		if(display instanceof ObjectChange){
			return ((ObjectChange)display).getObject();
		}
		return display;
	}
	
	public int getActiveDisplayObjectIndex(){
		if(display instanceof ObjectChange){
			return ((ObjectChange)display).pos;
		}
		return 0;
	}
	
	public void setDisplayObject(Object toDisplay){
		if(display==toDisplay)
			return;
		if(toDisplay.getClass().isArray()){
			toDisplay = new ObjectChange(toDisplay);
		}
		if(toDisplay instanceof Item){
			toDisplay = new ItemStack((Item)toDisplay);
		}else if(toDisplay instanceof Block){
			toDisplay = new ItemStack((Block)toDisplay);
		}
		if(!(toDisplay instanceof Icon || toDisplay instanceof ItemStack || toDisplay instanceof PC_GresTexture || toDisplay instanceof ObjectChange))
			throw new IllegalArgumentException("Unknow display object");
		if(display instanceof ObjectChange)
			removeEventListener((ObjectChange)display);
		display = toDisplay;
		if(display instanceof ObjectChange)
			addEventListener((ObjectChange)display);
		notifyChange();
	}
	
	@Override
	protected PC_Vec2I calculateMinSize() {
		Object d = display;
		if(display instanceof ObjectChange){
			d = ((ObjectChange)display).getObject();
		}
		if(d instanceof Icon){
			int x = ((Icon)d).getIconWidth();
			int y = ((Icon)d).getIconHeight();
			return new PC_Vec2I(x, y);
		}else if(d instanceof ItemStack){
			return new PC_Vec2I(16, 16);
		}else if(d instanceof PC_GresTexture){
			return ((PC_GresTexture)d).getMinSize();
		}
		return new PC_Vec2I(-1, -1);
	}

	@Override
	protected PC_Vec2I calculateMaxSize() {
		return new PC_Vec2I(-1, -1);
	}

	@Override
	protected PC_Vec2I calculatePrefSize() {
		Object d = display;
		if(display instanceof ObjectChange){
			d = ((ObjectChange)display).getObject();
		}
		if(d instanceof Icon){
			int x = ((Icon)d).getIconWidth();
			int y = ((Icon)d).getIconHeight();
			return new PC_Vec2I(x, y);
		}else if(d instanceof ItemStack){
			return new PC_Vec2I(16, 16);
		}else if(d instanceof PC_GresTexture){
			return ((PC_GresTexture)d).getDefaultSize();
		}
		return new PC_Vec2I(-1, -1);
	}

	@Override
	protected void paint(PC_RectI scissor, double scale, int displayHeight, float timeStamp) {
		Object d = display;
		if(display instanceof ObjectChange){
			d = ((ObjectChange)display).getObject();
		}
		if(d instanceof Icon){
			PC_GresRenderer.drawIcon(0, 0, rect.width, rect.height, (Icon)d);
		}else if(d instanceof ItemStack){
			int x = rect.width/2-8;
			int y = rect.height/2-8;
			PC_GresRenderer.drawItemStack(x, y, (ItemStack)d, null);
		}else if(d instanceof PC_GresTexture){
			((PC_GresTexture)d).draw(0, 0, rect.width, rect.height, 0);
		}
	}
	
	private static class ObjectChange implements PC_IGresEventListener{

		private Object[] display;
		private int pos;
		
		private ObjectChange(Object[] display){
			this.display = new Object[display.length];
			for(int i=0; i<display.length; i++){
				Object o = display[i];
				if(o instanceof Item){
					o = new ItemStack((Item)o);
				}else if(o instanceof Block){
					o = new ItemStack((Block)o);
				}
				if(!(o instanceof Icon || o instanceof ItemStack || o instanceof PC_GresTexture || o instanceof ObjectChange))
					throw new IllegalArgumentException("Unknow display object");
				this.display[i] = o;
			}
		}
		
		private ObjectChange(Object display){
			this.display = new Object[Array.getLength(display)];
			for(int i=0; i<this.display.length; i++){
				Object o = Array.get(display, i);
				if(o instanceof Item){
					o = new ItemStack((Item)o);
				}else if(o instanceof Block){
					o = new ItemStack((Block)o);
				}
				if(!(o instanceof Icon || o instanceof ItemStack || o instanceof PC_GresTexture || o instanceof ObjectChange))
					throw new IllegalArgumentException("Unknow display object");
				this.display[i] = o;
			}
		}
		
		@Override
		public void onEvent(PC_GresEvent event) {
			if(event instanceof PC_GresMouseButtonEvent){
				if(((PC_GresMouseButtonEvent)event).getEvent()==PC_GresMouseButtonEvent.Event.CLICK){
					pos++;
					pos %= display.length;
				}
			}
		}

		private Object getObject(){
			return display[pos];
		}
		
	}
	
}
