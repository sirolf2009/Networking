package com.sirolf2009.networking.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.sirolf2009.networking.IClient;
import com.sirolf2009.networking.IHost;
import com.sirolf2009.networking.Packet;

public class TestPacket extends Packet {
	
	private String message;
	
	public TestPacket() {}
	
	public TestPacket(String msg) {
		message = msg;
	}
	
	protected void write(PrintWriter out) {
		out.println(message);
	}
	
	public void receive(BufferedReader in) throws IOException {
		message = in.readLine();
	}
	
	public void receivedOnClient(IClient client) {
		TestNetworking.ReceivedOnClient = message;
		TestNetworking.SendAndReceived = true;
	}
	public void receivedOnServer(IHost host) {
		TestNetworking.ReceivedOnServer = message;
		host.getSender().send(this);
	}
	
}
