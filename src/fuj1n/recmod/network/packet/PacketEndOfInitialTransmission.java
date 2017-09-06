package fuj1n.recmod.network.packet;

import fuj1n.recmod.RecMod;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class PacketEndOfInitialTransmission extends AbstractPacket {

  public PacketEndOfInitialTransmission() {

  }

  @Override
  public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {

  }

  @Override
  public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {

  }

  @Override
  public void handleClientSide(EntityPlayer player) {
    //TODO non-temporary solution to NPE
    System.out.println(player);

    if (player == null)
      return;

    RecMod.packetPipeline.sendToServer(new PacketUpdatePlayerStatus(player.getName(), 0, RecMod.instance.keepState && RecMod.instance.recState));
    RecMod.packetPipeline.sendToServer(new PacketUpdatePlayerStatus(player.getName(), 1, RecMod.instance.keepState && RecMod.instance.strState));
  }

  @Override
  public void handleServerSide(EntityPlayer player) {
  }

}
