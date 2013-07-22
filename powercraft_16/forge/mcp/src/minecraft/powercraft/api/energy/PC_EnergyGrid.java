package powercraft.api.energy;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import powercraft.api.grids.PC_Grid;


public class PC_EnergyGrid extends PC_Grid<PC_EnergyGrid, PC_ConduitEnergyTileEntity> {

	private List<PC_ConduitEnergyTileEntity> nodes = new ArrayList<PC_ConduitEnergyTileEntity>();


	public void addNode(PC_ConduitEnergyTileEntity node) {

		removeInternal(node);
		nodes.add(node);
	}


	@Override
	protected void removeInternal(PC_ConduitEnergyTileEntity edge) {

		nodes.remove(edge);
		super.removeInternal(edge);
	}


	@Override
	public boolean hasNodes() {

		return super.hasNodes() || !nodes.isEmpty();
	}


	@Override
	public void mixGrids(PC_EnergyGrid otherGrid) {

		for (PC_ConduitEnergyTileEntity node : otherGrid.nodes) {
			nodes.add(node);
			node.setGrid(this);
		}
		otherGrid.nodes.clear();
		super.mixGrids(otherGrid);
	}


	private PC_ConduitEnergyTileEntity getFirstNode() {

		if (!ioNodes.isEmpty()) return ioNodes.get(0);
		return null;
	}


	@Override
	public void onUpdateTick(PC_ConduitEnergyTileEntity node) {

		if (node != getFirstNode()) return;
		List<PC_IEnergyConsumer> consumers = new ArrayList<PC_IEnergyConsumer>();
		List<PC_IEnergyProvider> providers = new ArrayList<PC_IEnergyProvider>();
		List<PC_IEnergyPuffer> puffers = new ArrayList<PC_IEnergyPuffer>();
		for (PC_ConduitEnergyTileEntity ioNode : ioNodes) {
			ioNode.addEnergyInterfaces(consumers, providers, puffers);
		}
		calc(consumers, providers, puffers);
	}


	private static <T> List<T> removeDoubels(List<T> list, List<?> anyOther) {

		List<T> newList = new ArrayList<T>();
		for (T t : list) {
			if (!newList.contains(t) && (anyOther == null || !anyOther.contains(t))) {
				newList.add(t);
			}
		}
		return newList;
	}


	public static void calc(List<PC_IEnergyConsumer> consumers, List<PC_IEnergyProvider> providers, List<PC_IEnergyPuffer> puffers) {

		consumers = removeDoubels(consumers, puffers);
		providers = removeDoubels(providers, puffers);
		puffers = removeDoubels(puffers, null);
		PC_PufferData pufferDatas[] = new PC_PufferData[puffers.size()];
		float maxProviderPower = 0;
		float maxPufferPower = 0;
		float requestedPower = 0;
		float pufferRequestedPower = 0;
		float pufferLevel = 0;
		for (PC_IEnergyProvider provider : providers) {
			maxProviderPower += provider.getEnergyForUsage();
		}
		int i = 0;
		for (PC_IEnergyPuffer puffer : puffers) {
			pufferDatas[i++] = new PC_PufferData(puffer);
			maxPufferPower += puffer.getEnergyForUsage();
			pufferLevel += puffer.getEnergyLevel();
			pufferRequestedPower += puffer.getEnergyRequest();
		}
		Arrays.sort(pufferDatas);
		if (!puffers.isEmpty()) pufferLevel /= puffers.size();
		for (PC_IEnergyConsumer consumer : consumers) {
			requestedPower += consumer.getEnergyRequest();
		}
		if (requestedPower <= maxProviderPower + maxPufferPower) {
			for (PC_IEnergyConsumer consumer : consumers) {
				consumer.consumeEnergy(consumer.getEnergyRequest());
			}
			float powerThere = maxProviderPower - requestedPower;
			if (powerThere > 0) {
				for (i = 0; i < pufferDatas.length; i++) {
					if (pufferDatas[i].pufferRequestedPower - pufferDatas[i].gottenPower < powerThere) {
						powerThere -= pufferDatas[i].pufferRequestedPower - pufferDatas[i].gottenPower;
						pufferDatas[i].gottenPower += pufferDatas[i].pufferRequestedPower;
					} else {
						pufferDatas[i].gottenPower += powerThere;
						powerThere = 0;
						break;
					}
				}
			}
			float proz;
			if (powerThere <= 0) {
				proz = 1;
			} else {
				if (maxProviderPower > 0) {
					proz = (requestedPower + powerThere) / maxProviderPower;
				} else {
					proz = -1;
				}
			}
			if (proz != -1) {
				for (PC_IEnergyProvider provider : providers) {
					provider.getEnergy(provider.getEnergyForUsage() * proz);
				}
			}
			if (powerThere < 0) {
				powerThere = -powerThere;
				for (i = pufferDatas.length - 1; i >= 0; i--) {
					if (pufferDatas[i].maxPufferPower - pufferDatas[i].removedPower < powerThere) {
						powerThere -= pufferDatas[i].maxPufferPower - pufferDatas[i].removedPower;
						pufferDatas[i].removedPower += pufferDatas[i].maxPufferPower;
					} else {
						pufferDatas[i].removedPower += powerThere;
						powerThere = 0;
						break;
					}
				}
			}
			for (i = 0; i < pufferDatas.length; i++) {
				pufferDatas[i].clamp(pufferLevel);
			}
			i = 0;
			int j = pufferDatas.length - 1;
			while (i < j) {
				while (pufferDatas[i].pufferRequestedPower <= pufferDatas[i].gottenPower && i < j) {
					i++;
				}
				while (pufferDatas[j].maxPufferPower <= pufferDatas[j].removedPower && i < j) {
					j--;
				}
				if (i >= j) break;
				if (pufferDatas[i].pufferRequestedPower - pufferDatas[i].gottenPower > pufferDatas[j].maxPufferPower - pufferDatas[j].removedPower) {
					pufferDatas[i].gottenPower += pufferDatas[j].maxPufferPower - pufferDatas[j].removedPower;
					pufferDatas[j].removedPower = pufferDatas[j].maxPufferPower;
				} else {
					pufferDatas[j].removedPower += pufferDatas[i].pufferRequestedPower - pufferDatas[i].gottenPower;
					pufferDatas[i].gottenPower = pufferDatas[i].pufferRequestedPower;
				}
			}
			for (i = 0; i < pufferDatas.length; i++) {
				if (pufferDatas[i].gottenPower > 0) pufferDatas[i].puffer.consumeEnergy(pufferDatas[i].gottenPower);
				if (pufferDatas[i].removedPower > 0) pufferDatas[i].puffer.getEnergy(pufferDatas[i].removedPower);
			}
		} else {
			for (PC_IEnergyProvider provider : providers) {
				provider.getEnergy(provider.getEnergyForUsage());
			}
			for (PC_IEnergyPuffer puffer : puffers) {
				puffer.getEnergy(puffer.getEnergyForUsage());
			}
			if (requestedPower > 0) {
				float proz = (maxProviderPower + maxPufferPower) / requestedPower;
				for (PC_IEnergyConsumer consumer : consumers) {
					consumer.consumeEnergy(consumer.getEnergyRequest() * proz);
				}
			}
		}
	}

}
