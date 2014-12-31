package fuj1n.recmod.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.relauncher.Side;
import fuj1n.recmod.RecMod;
import fuj1n.recmod.network.packet.PacketRemovePlayer;

public class PlayerTracker
{

	@SubscribeEvent
	public void onPlayerLogin (PlayerLoggedInEvent event)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
		{
			RecMod.instance.updatePlayerInformation(event.player.getCommandSenderName(), 0, false);
			RecMod.instance.updatePlayerInformation(event.player.getCommandSenderName(), 1, false);
			RecMod.instance.sendDataToPlayer(event.player);
		}
	}

	@SubscribeEvent
	public void onPlayerLogout (PlayerLoggedOutEvent event)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
		{
			removePlayerName(event.player.getCommandSenderName());
		}
	}

	public void removePlayerName (String name)
	{
		RecMod.instance.removeUnneededData(name);

		PacketRemovePlayer pckt = new PacketRemovePlayer(name);
		RecMod.packetPipeline.sendToAll(pckt);
	}
}
