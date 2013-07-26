package powercraft.apiOld;

import java.util.EnumSet;

import net.minecraft.client.settings.KeyBinding;
import powercraft.api.registry.PC_RegistryClient;

import com.google.common.collect.ObjectArrays;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class PC_KeyHandler extends KeyHandler {

	public PC_KeyHandler() {
		super(new KeyBinding[0], new boolean[0]);
	}

	@Override
	public String getLabel() {
		return "PowerCraft-KeyHandler";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb,
			boolean tickEnd, boolean isRepeat) {
		PC_RegistryClient.keyEvent(kb.keyDescription, true);
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		PC_RegistryClient.keyEvent(kb.keyDescription, false);
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT, TickType.RENDER);
	}

	public void addKey(String name, int key) {
		keyBindings = ObjectArrays.concat(keyBindings,
				new KeyBinding(name, key));
		repeatings = new boolean[keyBindings.length];
		keyDown = new boolean[keyBindings.length];
	}

}
