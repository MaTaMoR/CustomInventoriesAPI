package us.swiftex.custominventories.utils.server;

import us.swiftex.custominventories.utils.PacketUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//Not mine : https://github.com/filoghost/HolographicDisplays/tree/master/HolographicDisplays/Plugin/com/gmail/filoghost/holographicdisplays/bridge/bungeecord/serverpinger
public class ServerPinger {

    public static PingResponse fetchData(final ServerAddress serverAddress, int timeout) throws IOException {
        try (Socket socket = new Socket(serverAddress.getAddress(), serverAddress.getPort())) {
            socket.setSoTimeout(timeout);

            try(DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream()); DataInputStream dataIn = new DataInputStream(socket.getInputStream())) {
                try(ByteArrayOutputStream byteOut = new ByteArrayOutputStream(); DataOutputStream handshake = new DataOutputStream(byteOut)) {

                    handshake.write(0);
                    PacketUtils.writeVarInt(handshake, 4);
                    PacketUtils.writeString(handshake, serverAddress.getAddress(), PacketUtils.UTF8);
                    handshake.writeShort(serverAddress.getPort());
                    PacketUtils.writeVarInt(handshake, 1);
                    byte[] bytes = byteOut.toByteArray();
                    PacketUtils.writeVarInt(dataOut, bytes.length);
                    dataOut.write(bytes);
                    bytes = new byte[]{0};
                    PacketUtils.writeVarInt(dataOut, bytes.length);
                    dataOut.write(bytes);
                    PacketUtils.readVarInt(dataIn);
                    PacketUtils.readVarInt(dataIn);
                    final byte[] responseData = new byte[PacketUtils.readVarInt(dataIn)];
                    dataIn.readFully(responseData);
                    final String jsonString = new String(responseData, PacketUtils.UTF8);

                    return new PingResponse(jsonString, serverAddress);
                }
            }
        }
    }
}