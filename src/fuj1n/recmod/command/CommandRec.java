package fuj1n.recmod.command;

import java.util.*;

import fuj1n.recmod.RecMod;
import fuj1n.recmod.network.packet.*;
import ibxm.Player;
import net.minecraft.command.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
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
		icommandsender.addChatMessage(new ChatComponentText("\u00A7c<gui> (displays an easy to use GUI)"));
		icommandsender.addChatMessage(new ChatComponentText("\u00A7c<r/s> (toggle recording or streaming"));
		icommandsender.addChatMessage(new ChatComponentText("\u00A7c<ui> <self> [p] (toggle self ui - p toggles disabled by default)"));
		icommandsender.addChatMessage(new ChatComponentText("\u00A7c<sheet> <sheetname> (replace the texture sheet used)"));
		return "Wut?";
	}

	public void onWrongUsage(ICommandSender icommandsender) {
		getCommandUsage(icommandsender);
	}
	
	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if(icommandsender instanceof MinecraftServer || icommandsender instanceof RConConsoleSource){
			throw new CommandException("Only players are allowed to perform this command.", new Object[0]);
		}
		
		if (astring.length == 1 && (astring[0].equals("r") || astring[0].equals("s"))) {
			String sender = icommandsender.getCommandSenderName();
			int type = astring[0].equals("r") ? 0 : 1;
			boolean flag = astring[0].equals("r") ? !RecMod.instance.isPlayerRecording(sender) : !RecMod.instance.isPlayerStreaming(sender);
			RecMod.instance.updatePlayerInformation(sender, type, flag);
			spreadData(sender, type, flag);
		} else if((astring.length == 2 || (astring.length == 3 && astring[2].equals("p"))) && astring[0].equals("ui") && (astring[1].equals("self") || astring[1].equals("sidebar")) && icommandsender instanceof EntityPlayer){
			boolean isSelf = astring[1].equals("self");
			boolean isOverride = astring.length == 3 && astring[2].equals("p");
			sendUIUpdatePacket((EntityPlayer)icommandsender, isSelf, isOverride);
		} else if(astring.length == 2 && astring[0].equals("sheet")){
			sendSheetChangePacket((EntityPlayer)icommandsender, astring[1]);
		} else if(astring.length == 1 && astring[0].equals("gui")){
			
		} else {
			onWrongUsage(icommandsender);
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
		PacketUpdatePlayerStatus pckt = new PacketUpdatePlayerStatus(playerName, typeData, flag);
		RecMod.packetPipeline.sendToAll(pckt);
	}
	
	public void sendUIUpdatePacket(EntityPlayer p, boolean isSelf, boolean isOverride){
		if(p instanceof EntityPlayerMP){
			PacketChangeUISettings pckt = new PacketChangeUISettings(isSelf, isOverride);
			RecMod.packetPipeline.sendTo(pckt, (EntityPlayerMP)p);
		}
	}

	public void sendSheetChangePacket(EntityPlayer p, String newSheet){
		if(p instanceof EntityPlayerMP){
			PacketChangeUISheet pckt = new PacketChangeUISheet(newSheet);
			RecMod.packetPipeline.sendTo(pckt, (EntityPlayerMP)p);
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
		}else if(par2ArrayOfStr.length == 2 && par2ArrayOfStr[0].equals("ui")){
			l.add("self");
		}else if(par2ArrayOfStr.length == 2 && par2ArrayOfStr[0].equals("sheet")){
			l.add("recmod:textures/sheets/indicators.png");
			l.add("recmod:textures/sheets/indicatorsx2.png");
		}else if(par2ArrayOfStr.length == 3 && par2ArrayOfStr[1].equals("self")){
			l.add("p");
		}

		return l;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		return true;
	}

}
