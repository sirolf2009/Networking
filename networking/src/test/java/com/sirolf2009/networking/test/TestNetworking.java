package com.sirolf2009.networking.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sirolf2009.networking.AbstractClient;
import com.sirolf2009.networking.AbstractServer;
import com.sirolf2009.networking.IClient;
import com.sirolf2009.networking.IHost;
import com.sirolf2009.networking.Packet;

public class TestNetworking {
	
	private TestServer server;
	private TestClient client;
	private String message;
	
	public static boolean SendAndReceived;
	public static boolean Connected;
	public static String ReceivedOnServer;
	public static String ReceivedOnClient;

	@Before
	public void setUp() throws Exception {
		Packet.registerPacket(1, TestPacket.class);
		server = new TestServer(25565);
		client = new TestClient(25565);
		message = "Hello World";
		while(!Connected) { Thread.sleep(1);}
		System.out.println("Connected in setup");
		client.getSender().send(new TestPacket(message));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws InterruptedException {
		System.out.println("Waiting for packet");
		while(!SendAndReceived) { Thread.sleep(1);}
		System.out.println("Packet received");
		assertEquals(message, ReceivedOnServer);
		assertEquals(message, ReceivedOnClient);
	}
	
	static {
		Packet.registerPacket(1, TestPacket.class);
	}

}
