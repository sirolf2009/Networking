package com.sirolf2009.networking;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observer;

public abstract class AbstractClient implements IClient, Observer {

	private Socket socket;
	private Sender sender;
	private Receiver receiver;
	private Connector connector;
	
	public AbstractClient(String host, int port) {
		try {
			socket = new Socket(host, port);
			connector = new Connector(this, this);
			new Thread(connector).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void sendPacket(Packet packet) {
		getSender().send(packet);
	}

	@Override
	public boolean isRemote() {
		return false;
	}

	@Override
	public boolean isConnected() {
		return socket.isConnected();
	}
	
	@Override
	public Sender getSender() {
		return sender;
	}

	@Override
	public void setSender(Sender sender) {
		this.sender = sender;
	}

	@Override
	public Receiver getReceiver() {
		return receiver;
	}

	@Override
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	@Override
	public Socket getSocket() {
		return socket;
	}

}
