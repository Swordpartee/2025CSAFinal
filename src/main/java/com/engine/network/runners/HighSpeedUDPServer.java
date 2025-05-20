package com.engine.network.runners;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class HighSpeedUDPServer {

    private static final int PORT = 4445;
    private static final int BUFFER_SIZE = 128;

    public static void main(String[] args) throws IOException {
        int packets = 0;

        Selector selector = Selector.open();

        DatagramChannel channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(PORT));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);

        ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

        System.out.println("Non-blocking UDP server listening on port " + PORT);

        while (true) {
            int readyChannels = selector.select(); // Blocks until I/O is ready
            if (readyChannels == 0) continue;

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();

                if (key.isReadable()) {
                    packets++;

                    DatagramChannel ch = (DatagramChannel) key.channel();
                    buffer.clear();
                    InetSocketAddress sender = (InetSocketAddress) ch.receive(buffer);
                    buffer.flip();

                    // Process packet
                    int size = buffer.remaining();
                    byte[] data = new byte[size];
                    buffer.get(data);

                    if (packets % 100_000 == 0) {
                        System.out.printf("Received " + packets + " packets from " + sender + " with " + size + " bytes\n");
                    }
                    // System.out.println("Received packet from " + sender + " with " + size + " bytes");
                }
            }
        }
    }
}
