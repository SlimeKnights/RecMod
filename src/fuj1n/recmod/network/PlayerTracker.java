package fuj1n.recmod.network;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import fuj1n.recmod.RecMod;
import java.io.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PlayerTracker implements IPlayerTracker {

	@Override
	public void onPlayerLogin(EntityPlayer player) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			RecMod.instance.updatePlayerInformation(player.getCommandSenderName(), 0, false);
			RecMod.instance.updatePlayerInformation(player.getCommandSenderName(), 1, false);
			RecMod.instance.sendDataToPlayer(player);
		}
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			removePlayerName(player.getCommandSenderName());
		}
	}

	public void removePlayerName(String name) {
		RecMod.instance.removeUnneededData(name);

		ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeUTF(name);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "recModDataC";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		PacketDispatcher.sendPacketToAllPlayers(packet);
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
	}

}
