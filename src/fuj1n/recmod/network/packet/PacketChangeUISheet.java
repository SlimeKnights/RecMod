package fuj1n.recmod.network.packet;

import fuj1n.recmod.RecMod;

import cpw.mods.fml.common.network.ByteBufUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class PacketChangeUISheet extends AbstractPacket {

	String newSheet;

	public PacketChangeUISheet(String newSheet) {
		this.newSheet = newSheet;
	}

	public PacketChangeUISheet() {}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		ByteBufUtils.writeUTF8String(buffer, newSheet);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		newSheet = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		RecMod.instance.sheetLocation = newSheet;
		RecMod.instance.onSheetChanged();
	}

	@Override
	public void handleServerSide(EntityPlayer player) {}
}
