package fuj1n.recmod.client.keybind;

import java.util.ArrayList;

import org.lwjgl.input.*;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.*;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.*;
import net.minecraftforge.common.MinecraftForge;

public class KeyHandlerRecMod {

	public KeyBinding[] keyBindings;
	public boolean[] keyDown;
	public boolean[] repeatings;
	public boolean isDummy;

	Minecraft mc = Minecraft.getMinecraft();
	boolean b = false;

	public KeyHandlerRecMod(KeyBinding[] keyBindings, boolean[] repeatings) {
		assert keyBindings.length == repeatings.length : "You need to pass two arrays of identical length";
		this.keyBindings = keyBindings;
		this.repeatings = repeatings;
		this.keyDown = new boolean[keyBindings.length];
		isDummy = false;
	}

	@SubscribeEvent
	public void onTick(TickEvent event) {
		System.out.println("Key Tick");
		if (event.side == Side.CLIENT) {
			if (event.phase == Phase.START)
				keyTick(event.type, false);
			else if (event.phase == Phase.END)
				keyTick(event.type, true);
		}

	}

	public void keyTick(Type type, boolean tickEnd) {
		for (int i = 0; i < keyBindings.length; i++) {
			KeyBinding keyBinding = keyBindings[i];
			int keyCode = keyBinding.getKeyCode();
			boolean state = (keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode));
			if (state != keyDown[i] || (state && repeatings[i])) {
				if (state) {
					keyDown(type, keyBinding, tickEnd, state != keyDown[i]);
				} else {
					keyUp(type, keyBinding, tickEnd);
				}
				if (tickEnd) {
					keyDown[i] = state;
				}
			}
		}
	}

	public void keyDown(Type types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {

	}

	public void keyUp(Type types, KeyBinding kb, boolean tickEnd) {
		if (mc.theWorld != null && mc.currentScreen == null && /*mc.gameSettings.keyBindPlayerList.pressed &&*/(!mc.isIntegratedServerRunning() || mc.thePlayer.sendQueue.playerInfoList.size() > 1))
			if (b) {
				if (kb == keyBindings[0]) {
					mc.thePlayer.sendChatMessage("/rec r");
				} else if (kb == keyBindings[1]) {
					mc.thePlayer.sendChatMessage("/rec s");
				}
			}
		b = !b;
	}

	public static void registerSelf() {
		System.out.println("Key system register");
		String keyCategory = "Recording Mod Bindings";
		KeyBinding recBinding = new KeyBinding("Toggle Recording", 44, keyCategory);
		KeyBinding streamBinding = new KeyBinding("Toggle Streaming", 45, keyCategory);
		KeyHandlerRecMod keyh = new KeyHandlerRecMod(new KeyBinding[] { recBinding, streamBinding }, new boolean[] { false, false });
		MinecraftForge.EVENT_BUS.register(keyh);
		keyh.aggregateKeys();
	}

	private void aggregateKeys() {
		GameSettings settings = Minecraft.getMinecraft().gameSettings;
		ArrayList<KeyBinding> harvestedBindings = Lists.newArrayList();
		for (KeyBinding kb : keyBindings) {
			harvestedBindings.add(kb);
		}

		KeyBinding[] modKeyBindings = harvestedBindings.toArray(new KeyBinding[harvestedBindings.size()]);
		KeyBinding[] allKeys = new KeyBinding[settings.keyBindings.length + modKeyBindings.length];
		System.arraycopy(settings.keyBindings, 0, allKeys, 0, settings.keyBindings.length);
		System.arraycopy(modKeyBindings, 0, allKeys, settings.keyBindings.length, modKeyBindings.length);
		settings.keyBindings = allKeys;
		settings.loadOptions();
	}
}
