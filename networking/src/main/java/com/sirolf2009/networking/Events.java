package com.sirolf2009.networking;


public class Events {
	
	public class EventClientJoining {
		
		private IClient client;
		
		public EventClientJoining(IClient client) {
			setClient(client);
		}

		public IClient getClient() {
			return client;
		}

		public void setClient(IClient client) {
			this.client = client;
		}
		
	}
	
	public class EventPacketSend {
		
		private Packet packet;
		
		public EventPacketSend(Packet packet) {
			setPacket(packet);
		}

		public Packet getPacket() {
			return packet;
		}

		public void setPacket(Packet packet) {
			this.packet = packet;
		}
		
	}
	
	public class EventPacketReceived {
		
		private Packet packet;
		
		public EventPacketReceived(Packet packet) {
			setPacket(packet);
		}

		public Packet getPacket() {
			return packet;
		}

		public void setPacket(Packet packet) {
			this.packet = packet;
		}
		
	}
	
	public class EventUnknownPacketReceived {
		
		private int packetID;
		
		public EventUnknownPacketReceived(int packetID) {
			this.packetID = packetID;
		}

		public int getPacketID() {
			return packetID;
		}
		
	}
	
	public class EventInvalidPacketIDReceived {
		
		private String packetID;
		private Exception e;
		
		public EventInvalidPacketIDReceived(String packetID, Exception e) {
			this.packetID = packetID;
			this.e = e;
		}

		public String getPacketID() {
			return packetID;
		}

		public Exception getException() {
			return e;
		}
		
	}
	
	public class EventConnectionLost {
		
		private ICommunicator communicator;
		private Exception exception;
		
		public EventConnectionLost(ICommunicator communicator, Exception e) {
			setCommunicator(communicator);
			setException(e);
		}

		public ICommunicator getCommunicator() {
			return communicator;
		}

		public void setCommunicator(ICommunicator communicator) {
			this.communicator = communicator;
		}

		public Exception getException() {
			return exception;
		}

		public void setException(Exception exception) {
			this.exception = exception;
		}
		
	}
	
	public class EventReceiverInterrupted {
		
		private ICommunicator communicator;
		private InterruptedException exception;
		
		public EventReceiverInterrupted(ICommunicator communicator, InterruptedException e) {
			setCommunicator(communicator);
			setException(e);
		}

		public ICommunicator getCommunicator() {
			return communicator;
		}

		public void setCommunicator(ICommunicator communicator) {
			this.communicator = communicator;
		}

		public InterruptedException getException() {
			return exception;
		}

		public void setException(InterruptedException exception) {
			this.exception = exception;
		}
		
	}
	
	public class EventListeningForConnections {
	}
	
	public class EventConnectorShutdown {
	}

}
