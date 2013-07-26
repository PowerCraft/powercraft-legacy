package powercraft.api.gres;


import powercraft.apiOld.PC_Vec2I;


public interface PC_IGresLayout {

	public PC_Vec2I getPreferredLayoutSize(PC_GresContainer container);


	public PC_Vec2I getMinimumLayoutSize(PC_GresContainer container);


	public void updateLayout(PC_GresContainer container);

}
