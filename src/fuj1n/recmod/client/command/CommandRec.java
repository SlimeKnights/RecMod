package fuj1n.recmod.client.command;

import cpw.mods.fml.common.network.PacketDispatcher;
import fuj1n.recmod.RecMod;
import java.io.*;
import java.util.*;
import net.minecraft.command.*;
import net.minecraft.network.packet.Packet250CustomPayload;

public class CommandRec extends CommandBase {

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "rec";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "<r/s> (toggle recording or streaming)";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if (astring.length == 1 && (astring[0].equals("r") || astring[0].equals("s"))) {
			String sender = icommandsender.getCommandSenderName();
			int type = astring[0].equals("r") ? 0 : 1;
			boolean flag = astring[0].equals("r") ? !RecMod.instance.isPlayerRecording(sender) : !RecMod.instance.isPlayerStreaming(sender);
			RecMod.instance.updatePlayerInformation(sender, type, flag);
			spreadData(sender, type, flag);

		} else {
			throw new WrongUsageException(getCommandUsage(icommandsender), new Object[0]);
		}
	}

	// public void sendUpdatePacket(String playerName, int type, boolean flag) {
	// ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
	// DataOutputStream outputStream = new DataOutputStream(bos);
	// try {
	// outputStream.writeUTF(playerName);
	// outputStream.writeInt(type);
	// outputStream.writeBoolean(flag);
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	//
	// Packet250CustomPayload packet = new Packet250CustomPayload();
	// packet.channel = "recModData";
	// packet.data = bos.toByteArray();
	// packet.length = bos.size();
	// if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
	// PacketDispatcher.sendPacketToServer(packet);
	// }
	// }

	public void spreadData(String playerName, int typeData, boolean flag) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeUTF(playerName);
			outputStream.writeInt(typeData);
			outputStream.writeBoolean(flag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "recModData";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		PacketDispatcher.sendPacketToAllPlayers(packet);
	}

	@Override
	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		List l = new ArrayList();
		if (par2ArrayOfStr.length == 1) {
			if (par2ArrayOfStr[0].equals("r")) {
				l.add("s");
			} else {
				l.add("r");
			}
		}

		return l;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		return true;
	}

}
