package com.sirolf2009.networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;

public class Sender extends Observable implements Runnable {

	public PrintWriter out;
	private Queue<Packet> queue;
	private ICommunicator communicator;
	private Events events;
	private boolean connected;

	public Sender(ICommunicator communicator) {
		try {
			setCommunicator(communicator);
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
		while(queue.size() != 0) {
			Packet packet = queue.poll();
			if(Packet.packetsPackettoID.get(packet.getClass()) == null) {
				System.err.println("WARNING! YOU ARE SENDING A PACKET THAT HAS NO ID BOUND TO IT! PACKET: "+packet);
			}
			packet.send(out);
			out.flush();
			setChanged();
			notifyObservers(events.new EventPacketSend(packet, communicator));
		}
	}

	public ICommunicator getCommunicator() {
		return communicator;
	}

	public void setCommunicator(ICommunicator communicator) throws IOException {
		out = new PrintWriter(((Socket)communicator.getSocket()).getOutputStream());
		queue = new LinkedList<Packet>();
		this.communicator = communicator;
	}
	
	public void disconnect() {
		connected = false;
	}

}
