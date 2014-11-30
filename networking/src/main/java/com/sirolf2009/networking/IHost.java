package com.sirolf2009.networking;

public interface IHost extends IClient {

	public Connector getConnector();
	public IServer getServer();
	
}
