package com.sirolf2009.networking.test;

import java.util.Observable;

import com.sirolf2009.networking.AbstractClient;

public class TestClient extends AbstractClient {

	public TestClient(int port) {
		super("localhost", port);
	}

	@Override
	public void onConnected() {
		System.out.println("Connected");
		TestNetworking.Connected = true;
	}

	@Override
	public void disconnect() {
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println(arg);
	}

}
