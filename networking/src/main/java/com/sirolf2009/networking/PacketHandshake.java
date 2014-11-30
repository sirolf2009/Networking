package com.sirolf2009.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class PacketHandshake extends Packet {
	
	private int index;
	
	public PacketHandshake() {}
	
	public PacketHandshake(int index) {
		this.index = index;
	}

	@Override
	protected void write(PrintWriter out) {
		out.println(index);
	}

	@Override
	public void receive(BufferedReader in) throws IOException {
		index = Integer.parseInt(in.readLine());
	}

	@Override
	public void receivedOnClient(IClient client) {
		client.getSender().send(this);
	}
	
	@Override
	public void receivedOnServer(IHost host) {
		host.getServer().onClientConnect(host);
	}
	
}
