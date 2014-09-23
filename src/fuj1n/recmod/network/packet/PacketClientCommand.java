package fuj1n.recmod.network.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import fuj1n.recmod.RecMod;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class PacketClientCommand extends AbstractPacket {

	public String[] params;

	public PacketClientCommand() {
	}

	public PacketClientCommand(String[] params) {
		this.params = params;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(params.length);

		for (int i = 0; i < params.length; i++) {
			ByteBufUtils.writeUTF8String(buffer, params[i]);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		int length = buffer.readInt();

		if (length > 0) {
			params = new String[length];

			for (int i = 0; i < params.length; i++) {
				params[i] = ByteBufUtils.readUTF8String(buffer).trim();
			}
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		if(params == null || params.length == 0){
			player.openGui(RecMod.instance, 0, player.worldObj, 0, 0, 0);
		} else if (params[0].equals("bobber")) {
			boolean isOverride = params.length == 2 && params[1].equals("p");

				RecMod.instance.showSelf = !RecMod.instance.showSelf;
				RecMod.instance.showSelfDef = isOverride ? !RecMod.instance.showSelfDef : RecMod.instance.showSelfDef;

				RecMod.instance.onUIStateChanged();
		} else if (params[0].equals("sheet")) {
			RecMod.instance.sheetLocation = params[1];
			RecMod.instance.onSheetChanged();
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
	}

}
