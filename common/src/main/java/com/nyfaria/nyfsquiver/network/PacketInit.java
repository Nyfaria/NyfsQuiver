package com.nyfaria.nyfsquiver.network;

import com.nyfaria.nyfsquiver.network.c2s.NextSlotPacket;
import com.nyfaria.nyfsquiver.network.c2s.OpenEquippedQuiverPacket;
import commonnetwork.api.Network;

public class PacketInit {
    public static void loadClass() {
        Network.registerPacket(OpenEquippedQuiverPacket.LOCATION, OpenEquippedQuiverPacket.class, OpenEquippedQuiverPacket::encode, OpenEquippedQuiverPacket::decode, OpenEquippedQuiverPacket::handle);
        Network.registerPacket(NextSlotPacket.LOCATION, NextSlotPacket.class, NextSlotPacket::encode, NextSlotPacket::decode, NextSlotPacket::handle);
    }

}
