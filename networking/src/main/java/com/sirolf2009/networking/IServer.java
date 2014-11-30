package com.sirolf2009.networking;

import java.net.ServerSocket;
import java.net.Socket;

public interface IServer extends ICommunicator {

	public void onClientConnect(IHost client);
	
	public Connector getConnector();
	
	public IHost createHost(Socket client);
	
	public ServerSocket getSocket();

	public void broadcast(Packet packet);
	public void broadcast(Packet packet, IHost host);
	
}
