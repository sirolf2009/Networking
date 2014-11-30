package com.sirolf2009.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

public class Receiver extends Observable implements Runnable {

	private BufferedReader in;
	private ICommunicator communicator;
	private Events events;
	private boolean connected;

	public Receiver(ICommunicator communicator) {
		try {
			this.communicator = communicator;
			in = new BufferedReader(new InputStreamReader(((Socket)communicator.getSocket()).getInputStream()));
			events = new Events();
			connected = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Receiver(IHost client) {
		try {
			communicator = client;
			in = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
			events = new Events();
			connected = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String packetIDString = null;
		while(connected) {
			setChanged();
			try {
				packetIDString = in.readLine();
				int packetID = Integer.parseInt(packetIDString);
				if(Packet.packetsIDtoPacket.get(packetID) == null) {
					notifyObservers(events.new EventUnknownPacketReceived(packetID));
				} else {
					Packet packet = (Packet) Packet.packetsIDtoPacket.get(packetID).newInstance();
					packet.receive(in);
					notifyObservers(events.new EventPacketReceived(packet));
					if(!communicator.isRemote()) {
						packet.receivedOnClient((IClient) communicator);
					} else {
						packet.receivedOnServer((IHost) communicator);
					}
				}
				Thread.sleep(20);
			} catch (SocketException e) {
				notifyObservers(events.new EventConnectionLost(communicator, e));
			} catch (InterruptedException e) {
				notifyObservers(events.new EventReceiverInterrupted(communicator, e));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch(NumberFormatException e) {
				if(packetIDString != null) {
					notifyObservers(events.new EventInvalidPacketIDReceived(packetIDString, e));
				} else {
					notifyObservers(events.new EventConnectionLost(communicator, e));
				}
			}
		}
	}
	
	public void disconnect() {
		connected = false;
	}

}
