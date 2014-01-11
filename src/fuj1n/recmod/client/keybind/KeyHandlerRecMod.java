package fuj1n.recmod.client.keybind;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.client.registry.*;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import net.minecraft.client.settings.KeyBinding;

public class KeyHandlerRecMod extends KeyHandler {

	Minecraft mc = Minecraft.getMinecraft();
	boolean b = false;
	
	public KeyHandlerRecMod(KeyBinding[] keyBindings, boolean[] repeatings) {
		super(keyBindings, repeatings);
	}

	@Override
	public String getLabel() {
		return "fuj1n.RecMod.KeyBindingTickHandler";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		if(b){
			if (kb == keyBindings[0]) {
				mc.thePlayer.sendChatMessage("/rec r");
			} else if (kb == keyBindings[1]) {
				mc.thePlayer.sendChatMessage("/rec s");
			}
		}
		b = !b;
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	public static void registerSelf() {
		KeyBinding recBinding = new KeyBinding("ToggleRecording", 44);
		KeyBinding streamBinding = new KeyBinding("ToggleStreaming", 45);
		KeyBindingRegistry.registerKeyBinding(new KeyHandlerRecMod(new KeyBinding[] { recBinding, streamBinding }, new boolean[] { false, false }));
	}

}
