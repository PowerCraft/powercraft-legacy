package powercraft.api.gres;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.Slot;

import org.lwjgl.opengl.GL11;

import powercraft.apiOld.PC_RectI;
import powercraft.apiOld.PC_Vec2I;


@SuppressWarnings("unused")
public abstract class PC_GresContainer extends PC_GresComponent {

	protected final List<PC_GresComponent> childs = new ArrayList<PC_GresComponent>();

	private PC_IGresLayout layout;

	private boolean updatingLayout;

	private boolean updatingLayoutAgain;

	protected final PC_RectI frame = new PC_RectI(0, 0, 0, 0);


	public PC_GresContainer() {

	}


	public PC_RectI getFrame() {

		return new PC_RectI(frame);
	}


	public PC_RectI getChildRect() {

		return new PC_RectI(rect.x + frame.x, rect.y + frame.y, rect.width - frame.x - frame.width, rect.height - frame.y - frame.height);
	}


	public void setLayout(PC_IGresLayout layout) {

		this.layout = layout;
		if (layout != null) layout.updateLayout(this);
	}


	public PC_IGresLayout getLayout() {

		return layout;
	}


	public void updateLayout() {

		if (layout != null) {
			if (!updatingLayout) {
				updatingLayout = true;
				do {
					updatingLayoutAgain = false;
					layout.updateLayout(this);
				} while (updatingLayoutAgain);
				updatingLayout = false;
			} else {
				updatingLayoutAgain = true;
			}
		}
	}


	public void add(PC_GresComponent component) {

		if (!childs.contains(component)) {
			childs.add(component);
			component.setParent(this);
			notifyChange();
		}
	}


	public void remove(PC_GresComponent component) {

		childs.remove(component);
		component.setParent(null);
		notifyChange();
	}


	public void removeAll() {

		while (!childs.isEmpty())
			childs.remove(0).setParent(null);
	}


	public boolean isChild(PC_GresComponent component) {

		return childs.contains(component);
	}


	public void notifyChildChange(PC_GresComponent component) {

		notifyChange();
	}


	@Override
	protected void notifyChange() {

		super.notifyChange();
		updateLayout();
	}


	@Override
	public void setMinSize(PC_Vec2I minSize) {

		if (minSize == null && layout != null) {
			this.minSize.setTo(layout.getMinimumLayoutSize(this).add(frame.x + frame.width, frame.y + frame.height));
			maxSizeSet = false;
		} else {
			if (minSize == null) {
				this.minSize.setTo(calculateMinSize().add(frame.x + frame.width, frame.y + frame.height));
				minSizeSet = false;
			} else {
				this.minSize.setTo(minSize.add(frame.x + frame.width, frame.y + frame.height));
				minSizeSet = true;
			}
		}
	}


	@Override
	public void setMaxSize(PC_Vec2I maxSize) {

		if (maxSize == null) {
			this.maxSize.setTo(calculateMaxSize().add(frame.x + frame.width, frame.y + frame.height));
			maxSizeSet = false;
		} else {
			this.maxSize.setTo(maxSize.add(frame.x + frame.width, frame.y + frame.height));
			maxSizeSet = true;
		}
	}


	@Override
	public void setPrefSize(PC_Vec2I prefSize) {

		if (prefSize == null && layout != null) {
			this.prefSize.setTo(layout.getPreferredLayoutSize(this).add(frame.x + frame.width, frame.y + frame.height));
			prefSizeSet = false;
		} else {
			if (prefSize == null) {
				this.prefSize.setTo(calculatePrefSize().add(frame.x + frame.width, frame.y + frame.height));
				prefSizeSet = false;
			} else {
				this.prefSize.setTo(prefSize.add(frame.x + frame.width, frame.y + frame.height));
				prefSizeSet = true;
			}
		}
	}


	@Override
	public void setVisible(boolean visible) {

		super.setVisible(visible);
		for (PC_GresComponent child : childs) {
			child.setParentVisible(visible);
		}
	}


	@Override
	protected void setParentVisible(boolean visible) {

		super.setParentVisible(enabled);
		for (PC_GresComponent child : childs) {
			child.setParentVisible(visible);
		}
	}


	@Override
	public void setEnabled(boolean enabled) {

		super.setEnabled(enabled);
		for (PC_GresComponent child : childs) {
			child.setParentEnabled(enabled);
		}
	}


	@Override
	protected void setParentEnabled(boolean enabled) {

		super.setParentEnabled(enabled);
		for (PC_GresComponent child : childs) {
			child.setParentEnabled(visible);
		}
	}


	@Override
	protected void doPaint(PC_Vec2I offset, PC_RectI scissorOld, double scale, int displayHeight, float timeStamp) {

		if (visible) {
			PC_RectI rect = new PC_RectI(this.rect);
			rect.x += offset.x;
			rect.y += offset.y;
			PC_RectI scissor = setDrawRect(scissorOld, rect, scale, displayHeight);
			GL11.glPushMatrix();
			GL11.glTranslatef(this.rect.x, this.rect.y, 0);
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			paint(scissor, timeStamp);
			rect.x += frame.x;
			rect.y += frame.y;
			GL11.glTranslatef(frame.x, frame.y, 0);
			offset = rect.getLocation();
			for (PC_GresComponent child : childs) {
				child.doPaint(offset, scissor, scale, displayHeight, timeStamp);
			}
			GL11.glPopMatrix();
		}
	}


	@Override
	protected PC_GresComponent getComponentAtPosition(PC_Vec2I position) {

		if (visible) {
			position = position.sub(frame.getLocation());
			for (PC_GresComponent child : childs) {
				PC_RectI rect = child.getRect();
				if (rect.contains(position)) {
					PC_GresComponent component = child.getComponentAtPosition(position.sub(rect.getLocation()));
					if (component != null) return component;
				}
			}
			return this;
		}
		return null;
	}


	@Override
	protected void onTick() {

		for (PC_GresComponent child : childs) {
			child.onTick();
		}
	}


	@Override
	protected Slot getSlotAtPosition(PC_Vec2I position) {

		if (visible) {
			position = position.sub(frame.getLocation());
			for (PC_GresComponent child : childs) {
				PC_RectI rect = child.getRect();
				if (rect.contains(position)) {
					Slot slot = child.getSlotAtPosition(position.sub(rect.getLocation()));
					if (slot != null) return slot;
				}
			}
		}
		return null;
	}


	@Override
	protected void tryActionOnKeyTyped(char key, int keyCode) {

		if (visible) {
			for (PC_GresComponent child : childs) {
				child.tryActionOnKeyTyped(key, keyCode);
			}
		}
	}

}
