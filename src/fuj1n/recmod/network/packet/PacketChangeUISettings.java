package fuj1n.recmod.network.packet;

import fuj1n.recmod.RecMod;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class PacketChangeUISettings extends AbstractPacket {

	private boolean isSelf, isOverride;

	public PacketChangeUISettings(boolean isSelf, boolean isOverride) {
		this.isSelf = isSelf;
		this.isOverride = isOverride;
	}

	public PacketChangeUISettings() {}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeBoolean(isSelf);
		buffer.writeBoolean(isOverride);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		isSelf = buffer.readBoolean();
		isOverride = buffer.readBoolean();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		if (isSelf) {
			RecMod.instance.showSelf = !RecMod.instance.showSelf;
			RecMod.instance.showSelfDef = isOverride ? !RecMod.instance.showSelfDef : RecMod.instance.showSelfDef;

			RecMod.instance.onUIStateChanged();
		} else {
			RecMod.instance.showUI = !RecMod.instance.showUI;

			RecMod.instance.onUIStateChanged();
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player) {}

}
