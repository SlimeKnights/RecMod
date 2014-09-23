package fuj1n.recmod.client.keybind;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import fuj1n.recmod.RecMod;
import fuj1n.recmod.network.packet.PacketUpdatePlayerStatus;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class KeyHandlerRecMod {

	boolean recDown = false;
	boolean strDown = false;

	Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onTick(ClientTickEvent event) {
		if (!RecMod.instance.enableKeys) {
			return;
		}
		if (event.type == Type.CLIENT && event.phase == Phase.START && mc.theWorld != null && mc.currentScreen == null) {
			keyTick();
			keyTick();
		}
	}

	public void keyTick() {
		if (Keyboard.isKeyDown(RecMod.instance.keyRec)) {
			if (!recDown) {
				recDown = true;
			}
		} else {
			if (recDown) {
				recDown = false;
				RecMod.instance.updatePlayerInformation(mc.thePlayer.getCommandSenderName(), 0, !RecMod.instance.isPlayerRecording(mc.thePlayer.getCommandSenderName()));
				RecMod.packetPipeline.sendToServer(new PacketUpdatePlayerStatus(mc.thePlayer.getCommandSenderName(), 0, RecMod.instance.isPlayerRecording(mc.thePlayer.getCommandSenderName())));
			}
		}

		if (Keyboard.isKeyDown(RecMod.instance.keyStr)) {
			if (!strDown) {
				strDown = true;
			}
		} else {
			if (strDown) {
				strDown = false;
				RecMod.instance.updatePlayerInformation(mc.thePlayer.getCommandSenderName(), 1, !RecMod.instance.isPlayerStreaming(mc.thePlayer.getCommandSenderName()));
				RecMod.packetPipeline.sendToServer(new PacketUpdatePlayerStatus(mc.thePlayer.getCommandSenderName(), 1, RecMod.instance.isPlayerStreaming(mc.thePlayer.getCommandSenderName())));
			}
		}
	}
}
