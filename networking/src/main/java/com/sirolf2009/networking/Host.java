package com.sirolf2009.networking;

import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class Host extends Observable implements IHost {
	
	public Socket socket;
	public Sender sender;
	public Receiver receiver;
	public boolean isConnected;
	private IServer server;
	public String name;

	public Host(Socket socket, IServer server, Observer observer) {
		this.socket = socket;
		sender = new Sender(this);
		receiver = new Receiver(this);
		this.server = server;
		addObserver(observer);
		sender.addObserver(observer);
		receiver.addObserver(observer);
		new Thread(sender, socket.toString() + " sender").start();
		new Thread(receiver, socket.toString() + " receiver").start();
	}

	@Override
	public Connector getConnector() {
		return server.getConnector();
	}

	@Override
	public Sender getSender() {
		return sender;
	}

	@Override
	public Receiver getReceiver() {
		return receiver;
	}

	@Override
	public Socket getSocket() {
		return socket;
	}

	@Override
	public boolean isRemote() {
		return true;
	}

	@Override
	public boolean isConnected() {
		return isConnected;
	}

	@Override
	public void disconnect() {
		isConnected = false;
		receiver.disconnect();
		sender.disconnect();
	}

	@Override
	public void setSender(Sender sender) {
		this.sender = sender;
	}

	@Override
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	@Override
	public void onConnected() {
	}

	@Override
	public IServer getServer() {
		return server;
	}

}
