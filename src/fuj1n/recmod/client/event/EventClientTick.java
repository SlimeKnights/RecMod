package fuj1n.recmod.client.event;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import fuj1n.recmod.RecMod;
import fuj1n.recmod.network.packet.PacketUpdatePlayerStatus;
import net.minecraft.client.Minecraft;

public class EventClientTick
{

	boolean recDown = false;
	boolean strDown = false;

	Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onTick (ClientTickEvent event)
	{
		if (event.type == Type.CLIENT && event.phase == Phase.END && mc.theWorld == null && mc.thePlayer == null && RecMod.instance.mapsDirty)
		{

			if (RecMod.instance.keepState)
			{
				String player = mc.getSession().getUsername();
				RecMod.instance.recState = RecMod.instance.isPlayerRecording(player);
				RecMod.instance.strState = RecMod.instance.isPlayerStreaming(player);
			}

			RecMod.instance.clearMaps();

			RecMod.instance.mapsDirty = false;
		}

		if (event.type == Type.CLIENT && event.phase == Phase.START && mc.theWorld != null && mc.thePlayer != null && RecMod.instance.showMode == 3)
		{
			String player = mc.thePlayer.getCommandSenderName();
			RecMod.instance.showSelf = RecMod.instance.isPlayerRecording(player) || RecMod.instance.isPlayerStreaming(player);
		}

		if (!RecMod.instance.enableKeys)
		{
			return;
		}
		if (event.type == Type.CLIENT && event.phase == Phase.START && mc.theWorld != null && mc.currentScreen == null)
		{
			keyTick();
		}
	}

	public void keyTick ()
	{
		if (Keyboard.isKeyDown(RecMod.instance.keyRec))
		{
			if (!recDown)
			{
				recDown = true;
			}
		}
		else
		{
			if (recDown)
			{
				recDown = false;
				RecMod.instance.updatePlayerInformation(mc.thePlayer.getCommandSenderName(), 0, !RecMod.instance.isPlayerRecording(mc.thePlayer.getCommandSenderName()));
				RecMod.packetPipeline.sendToServer(new PacketUpdatePlayerStatus(mc.thePlayer.getCommandSenderName(), 0, RecMod.instance.isPlayerRecording(mc.thePlayer.getCommandSenderName())));
			}
		}

		if (Keyboard.isKeyDown(RecMod.instance.keyStr))
		{
			if (!strDown)
			{
				strDown = true;
			}
		}
		else
		{
			if (strDown)
			{
				strDown = false;
				RecMod.instance.updatePlayerInformation(mc.thePlayer.getCommandSenderName(), 1, !RecMod.instance.isPlayerStreaming(mc.thePlayer.getCommandSenderName()));
				RecMod.packetPipeline.sendToServer(new PacketUpdatePlayerStatus(mc.thePlayer.getCommandSenderName(), 1, RecMod.instance.isPlayerStreaming(mc.thePlayer.getCommandSenderName())));
			}
		}
	}
}
