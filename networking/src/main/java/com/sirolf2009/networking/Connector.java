package com.sirolf2009.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

public class Connector extends Observable implements Runnable {

	private ICommunicator communicator;
	private boolean isAlive;
	static final Logger log = Logger.getLogger("com.sirolf2009.networking");
	private Events events;
	private Observer observer;
	
	private List<IHost> hosts;

	public Connector(ICommunicator communicator, Observer observer) {
		this.communicator = communicator;
		isAlive = true;
		events = new Events();
		addObserver(observer);
		this.observer = observer;
	}

	public synchronized void run() {
		setChanged();
		if(isAlive) {
			if(communicator.isRemote()) {
				hosts = new ArrayList<IHost>();
				notifyObservers(events.new EventListeningForConnections());
				while(true) {
					try {
						setChanged();
						IHost host = ((IServer)communicator).createHost(((ServerSocket)communicator.getSocket()).accept());
						//IHost host = new IH(((ServerSocket)communicator.getSocket()).accept(), (IServer) communicator, observer);
						getHosts().add(host);
						host.getSender().send(new PacketHandshake(getHosts().indexOf(host)));
						notifyObservers(events.new EventClientJoining(host));
					} catch (IOException e) {
						e.printStackTrace();
						log.severe(e.getMessage());
						break;
					}
				}
			} else {
				IClient client = (IClient) communicator;
				client.setSender(new Sender(communicator));
				client.setReceiver(new Receiver(communicator));
				client.getSender().addObserver(observer);
				client.getReceiver().addObserver(observer);
				new Thread(client.getSender()).start();
				new Thread(client.getReceiver()).start();
				client.onConnected();
			}
		}
	}

	public void disconnect() {
		notifyObservers(events.new EventConnectorShutdown());
		isAlive = false;
	}

	public List<IHost> getHosts() {
		return hosts;
	}

	public void setHosts(List<IHost> hosts) {
		this.hosts = hosts;
	}

}
