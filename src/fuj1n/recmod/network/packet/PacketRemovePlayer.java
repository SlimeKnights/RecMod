package fuj1n.recmod.network.packet;

import fuj1n.recmod.RecMod;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketRemovePlayer extends AbstractPacket {

  private String player;

  public PacketRemovePlayer(String player) {
    this.player = player;
  }

  public PacketRemovePlayer() {
  }

  @Override
  public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
    ByteBufUtils.writeUTF8String(buffer, player);
  }

  @Override
  public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
    player = ByteBufUtils.readUTF8String(buffer);
  }

  @Override
  public void handleClientSide(EntityPlayer player) {
    RecMod.instance.removeUnneededData(this.player);
  }

  @Override
  public void handleServerSide(EntityPlayer player) {
  }

}
