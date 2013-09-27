package powercraft.api.energy;


public class PC_PufferData implements Comparable<PC_PufferData> {

	public PC_IEnergyPuffer puffer;
	public float maxPufferPower;
	public float pufferLevel;
	public float pufferRequestedPower;
	public float removedPower;
	public float gottenPower;


	public PC_PufferData(PC_IEnergyPuffer puffer) {

		this.puffer = puffer;
		maxPufferPower = puffer.getEnergyForUsage(null);
		pufferLevel = puffer.getEnergyLevel(null);
		pufferRequestedPower = puffer.getEnergyRequest(null);
	}


	public void clamp(float level) {

		if (pufferLevel == level) {
			maxPufferPower = 0;
			pufferRequestedPower = 0;
		} else if (pufferLevel - maxPufferPower < level && pufferLevel > level) {
			maxPufferPower = pufferLevel - level;
		} else if (pufferLevel + pufferRequestedPower > level && pufferLevel < level) {
			pufferRequestedPower = level - pufferLevel;
		}
	}


	@Override
	public int compareTo(PC_PufferData o) {

		return pufferLevel > o.pufferLevel ? 1 : pufferLevel < o.pufferLevel ? -1 : 0;
	}

}
