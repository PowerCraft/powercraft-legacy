package powercraft.api.grid;


public interface PC_IGridProvider<G extends PC_Grid<G, T>, T extends PC_IGridProvider<G, T>> {

	public G getGrid();


	public void setGrid(G grid);

}
