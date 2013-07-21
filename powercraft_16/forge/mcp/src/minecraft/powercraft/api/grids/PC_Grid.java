package powercraft.api.grids;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import cpw.mods.fml.common.TickType;
import powercraft.api.PC_ITickHandler;
import powercraft.api.PC_TickHandler;

public abstract class PC_Grid<G extends PC_Grid<G, T>, T extends PC_IGridProvider<G, T>> {

	protected List<T> ioNodes = new ArrayList<T>();
	protected List<T> edges = new ArrayList<T>();
	
	public PC_Grid(){}
	
	public void addIO(T ioNode){
		removeInternal(ioNode);
		ioNodes.add(ioNode);
	}
	
	public void add(T edge){
		removeInternal(edge);
		edges.add(edge);
	}
	
	protected void removeInternal(T edge) {
		edges.remove(edge);
		ioNodes.remove(edge);
	}
	
	public boolean hasNodes(){
		return !(edges.isEmpty() && ioNodes.isEmpty());
	}
	
	public void remove(T edge) {
		removeInternal(edge);
	}
	
	public void mixGrids(G otherGrid){
		if(this == otherGrid)
			return;
		for(T ioNode:otherGrid.ioNodes){
			ioNodes.add(ioNode);
			ioNode.setGrid((G) this);
		}
		for(T edge:otherGrid.edges){
			edges.add(edge);
			edge.setGrid((G) this);
		}
		otherGrid.ioNodes.clear();
		otherGrid.edges.clear();
	}

	public abstract void onUpdateTick(T node);
	
}
