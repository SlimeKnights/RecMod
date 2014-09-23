package fuj1n.recmod.command;

import fuj1n.recmod.RecMod;
import fuj1n.recmod.network.packet.*;
import java.util.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.ChatComponentText;

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
		icommandsender.addChatMessage(new ChatComponentText("\u00A7cRec Usage: "));
		icommandsender.addChatMessage(new ChatComponentText("\u00A7c{no oprands} (displays an easy to use GUI)"));
		icommandsender.addChatMessage(new ChatComponentText("\u00A7c<r/s> (toggle recording or streaming)"));
		icommandsender.addChatMessage(new ChatComponentText("\u00A7c<bobber> [p] (toggle the bobber - p toggles never display)"));
		icommandsender.addChatMessage(new ChatComponentText("\u00A7c<sheet> <sheetname> (replace the texture sheet used)"));
		return "Wut?";
	}

	public void onWrongUsage(ICommandSender icommandsender) {
		getCommandUsage(icommandsender);
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if (!(icommandsender instanceof EntityPlayer)) {
			throw new CommandException("Only players are allowed to perform this command.", new Object[0]);
		}

		EntityPlayer player = (EntityPlayer) icommandsender;

		if (astring.length == 0) {
			sendClientProcess(player, astring);
		} else if (astring.length == 1 && (astring[0].equals("r") || astring[0].equals("s"))) {
			String sender = icommandsender.getCommandSenderName();
			int type = astring[0].equals("r") ? 0 : 1;
			boolean flag = astring[0].equals("r") ? !RecMod.instance.isPlayerRecording(sender) : !RecMod.instance.isPlayerStreaming(sender);
			RecMod.instance.updatePlayerInformation(sender, type, flag);
			spreadData(sender, type, flag);
		} else if ((astring.length == 1 || (astring.length == 2 && astring[1].equals("p"))) && astring[0].equals("bobber")) {
			sendClientProcess(player, astring);
		} else if (astring.length == 2 && astring[0].equals("sheet")) {
			sendClientProcess(player, astring);
		} else {
			onWrongUsage(icommandsender);
		}
	}

	public void spreadData(String playerName, int typeData, boolean flag) {
		PacketUpdatePlayerStatus pckt = new PacketUpdatePlayerStatus(playerName, typeData, flag);
		RecMod.packetPipeline.sendToAll(pckt);
	}

	public void sendClientProcess(EntityPlayer p, String[] params) {
		if (p instanceof EntityPlayerMP) {
			PacketClientCommand pckt = new PacketClientCommand(params);
			RecMod.packetPipeline.sendTo(pckt, (EntityPlayerMP) p);
		}
	}

	@Override
	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		List l = new ArrayList();
		if (par2ArrayOfStr.length == 1) {
			l.add("r");
			l.add("s");
			l.add("ui");
			l.add("sheet");
		} else if (par2ArrayOfStr.length == 2 && par2ArrayOfStr[0].equals("ui")) {
		} else if (par2ArrayOfStr.length == 2 && par2ArrayOfStr[0].equals("sheet")) {
			l.add("recmod:textures/sheets/indicators.png");
			l.add("recmod:textures/sheets/indicatorsx2.png");
		} else if (par2ArrayOfStr.length == 3 && par2ArrayOfStr[1].equals("self")) {
			l.add("p");
		}

		return l;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		return true;
	}

}
