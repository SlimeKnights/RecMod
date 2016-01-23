package fuj1n.recmod.network.packet;

import fuj1n.recmod.RecMod;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketUpdatePlayerStatus extends AbstractPacket
{

    private String player;
    private int type;
    private boolean flag;

    public PacketUpdatePlayerStatus(String player, int type, boolean flag)
    {
        this.player = player;
        this.type = type;
        this.flag = flag;
    }

    public PacketUpdatePlayerStatus()
    {
    }

    @Override
    public void encodeInto (ChannelHandlerContext ctx, ByteBuf buffer)
    {
        ByteBufUtils.writeUTF8String(buffer, player);
        buffer.writeInt(type);
        buffer.writeBoolean(flag);
    }

    @Override
    public void decodeInto (ChannelHandlerContext ctx, ByteBuf buffer)
    {
        player = ByteBufUtils.readUTF8String(buffer);
        type = buffer.readInt();
        flag = buffer.readBoolean();
    }

    @Override
    public void handleClientSide (EntityPlayer player)
    {
        //TODO non-temporary solution to NPE
        if(player == null)
            return;


        if (!player.getName().equals(this.player))
        {
            RecMod.instance.updatePlayerInformation(this.player, type, flag);
        }
    }

    @Override
    public void handleServerSide (EntityPlayer player)
    {
        RecMod.instance.updatePlayerInformation(this.player, type, flag);

        // Send the packet to all the players
        RecMod.packetPipeline.sendToAll(this);
    }

}
