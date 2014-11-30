package com.sirolf2009.networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Sender extends Observable implements Runnable {

	public PrintWriter out;
	private List<Packet> queue;
	private ICommunicator communicator;
	private Events events;
	private boolean connected;

	public Sender(ICommunicator communicator) {
		try {
			this.setCommunicator(communicator);
			out = new PrintWriter(((Socket)communicator.getSocket()).getOutputStream());
			queue = new ArrayList<Packet>();
			events = new Events();
			connected = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(connected) {
			try {
				sendToClient();
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void send(Packet packet) {
		queue.add(packet);
	}

	private synchronized void sendToClient() {
		for(Packet packet : queue) {
			if(Packet.packetsPackettoID.get(packet.getClass()) == null) {
				System.err.println("WARNING! YOU ARE SENDING A PACKET THAT HAS NO ID BOUND TO IT! PACKET: "+packet);
			}
			packet.send(out);
			out.flush();
			setChanged();
			notifyObservers(events.new EventPacketSend(packet));
		}
		queue.clear();
	}

	public ICommunicator getCommunicator() {
		return communicator;
	}

	public void setCommunicator(ICommunicator communicator) throws IOException {
		out = new PrintWriter(((Socket)communicator.getSocket()).getOutputStream());
		queue = new ArrayList<Packet>();
		this.communicator = communicator;
	}
	
	public void disconnect() {
		connected = false;
	}

}
