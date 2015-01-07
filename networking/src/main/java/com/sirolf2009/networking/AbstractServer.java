package com.sirolf2009.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.SimpleLog;

import com.sirolf2009.networking.Events.EventConnectionLost;

public abstract class AbstractServer implements IServer, Observer {

	private ServerSocket socket;
	private Connector connector;
	private List<IHost> hosts;
	public static Log log = new SimpleLog("Server");

	public AbstractServer(int port) throws IOException {
		socket = new ServerSocket(port);
		connector = new Connector(this, this);
		hosts = new ArrayList<IHost>();
		new Thread(connector).start();
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof Events.EventListeningForConnections) {
			log.info("Listening for connections");
		} else if(arg instanceof EventConnectionLost) {
			EventConnectionLost event = (EventConnectionLost) arg;
			IHost host = (IHost) event.getCommunicator();
			Socket socket = ((IHost)event.getCommunicator()).getSocket();
			log.info(socket.getInetAddress()+" disconnected");
			host.disconnect();
		}
	}

	public ServerSocket getSocket() {
		return socket;
	}

	@Override
	public boolean isRemote() {
		return true;
	}

	@Override
	public boolean isConnected() {
		return hosts.size() > 0;
	}

	@Override
	public void disconnect() {
	}

	@Override
	public void broadcast(Packet packet) {
		for(IHost host : hosts) {
			host.getSender().send(packet);
		}
	}

	@Override
	public void broadcast(Packet packet, IHost hostToSkip) {
		for(IHost host : hosts) {
			if(host == hostToSkip) {
				continue;
			}
			host.getSender().send(packet);
		}
	}

	@Override
	public void onClientConnect(IHost host) {
		log.info(host.getSocket().getInetAddress()+" connected");
		hosts.add(host);
	}

	@Override
	public IHost createHost(Socket client) {
		return new Host(client, this, this);
	}

	@Override
	public Connector getConnector() {
		return connector;
	}

}
