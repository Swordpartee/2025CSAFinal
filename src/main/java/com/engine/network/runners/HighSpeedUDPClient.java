// package com.engine.network.runners;

// import java.io.IOException;
// import java.net.*;

// public class HighSpeedUDPClient {

//     public static void main(String[] args) throws IOException, InterruptedException {
//         String serverIp = "68.179.181.230"; // Change to your server's IP
//         int serverPort = 8888;
//         int payloadSize = 128; // bytes per packet
//         int totalPackets = 1_000_000_000; // total number of packets to send
//         byte[] payload = new byte[payloadSize];
//         DatagramSocket socket = new DatagramSocket();
//         InetAddress address = InetAddress.getByName(serverIp);
//         DatagramPacket packet = new DatagramPacket(payload, payload.length, address, serverPort);

//         for (int i = 0; i < totalPackets; i++) {
//             socket.send(packet);
//         }
//         // System.out.printf("Sending %d packets to %s:%d (%d bytes each)%n", totalPackets, serverIp, serverPort, payloadSize);

//         // long startTime = System.nanoTime();
//         // long nextSendTime = startTime;

//         // for (int i = 0; i < totalPackets; i++) {
//         //     socket.send(packet);

//         //     if (rateLimit > 0) {
//         //         // Throttle sending rate
//         //         nextSendTime += 1_000_000_000L / rateLimit;
//         //         long now;
//         //         while ((now = System.nanoTime()) < nextSendTime) {
//         //             // Busy-wait or sleep for tight timing
//         //             Thread.onSpinWait();
//         //         }
//         //     }

//         //     if (i > 0 && i % 100_000 == 0) {
//         //         double elapsed = (System.nanoTime() - startTime) / 1_000_000_000.0;
//         //         System.out.printf("Sent %d packets in %.2f sec (%.2f pps)%n", i, elapsed, i / elapsed);
//         //     }
//         // }

//         // long elapsedNs = System.nanoTime() - startTime;
//         // double elapsedSec = elapsedNs / 1_000_000_000.0;
//         // System.out.printf("Done. Sent %d packets in %.2f sec (%.2f packets/sec)%n",
//         //         totalPackets, elapsedSec, totalPackets / elapsedSec);

//         socket.close();
//     }
// }

