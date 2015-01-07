Networking API
==========

This is an API that allows you to connect multiple java applications to eachother. It follows an server-client structure.

# Example

## Server
```java
import java.io.IOException;
import java.util.Observable;

import com.sirolf2009.networking.AbstractServer;

public class Server extends AbstractServer {

	public Server() throws IOException {
		this(1200);
	}
	
	public Server(int port) throws IOException {
		super(port);
	}

  //Whenever something happens, this method gets called. Have a look at com.sirolf2009.networking for a list of possible events
	public void update(Observable o, Object arg) {
		super.update(o, arg);
		log.info(o+": "+arg);
	}
}
```

## Client

```java
import java.util.Observable;

import com.sirolf2009.networking.AbstractClient;

public class Client extends AbstractClient {
	
	public Client() {
		this("localhost", 1200);
	}

	public Client(String host, int port) {
		super(host, port);
	}

  //This method is called when the client connects to the server
	@Override
	public void onConnected() {
	}

  //This method is called when the client disconnects from the server
	@Override
	public void disconnect() {
	}
	
  //Whenever something happens, this method gets called. Have a look at com.sirolf2009.networking for a list of possible events
	@Override
	public void update(Observable o, Object arg) {
	}

}
```

The above code will set up a client and a server. Now, we'll create a package that will be send from the client to the server and back.

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class PacketHelloWorld extends Packet {
	
	private String name;
	
	//We need an empty constructor to re-create the package after it has been send over the internet
	public PacketHelloWorld() {}
	
	//We need this constructor to create the package and specify the information we want to send
	public PacketHelloWorld(String name) {
	  this.name = name;
	}

  //We print out our name over the internet
	@Override
	protected void write(PrintWriter out) {
		out.println(name);
	}

  //We receive the name that has been printed
	@Override
	public void receive(BufferedReader in) throws IOException {
		name = in.readLine();
	}

  //When the client receives the package, he prints a message
	@Override
	public void receivedOnClient(IClient client) {
	  System.out.println("The server sends his regards!");
	}
	
	//When the server receives the package, he prints a message and sends it back to the client
	@Override
	public void receivedOnServer(IHost host) {
	  System.out.println("Received \"Hello World!\" from: "+name);
	  host.getSender().send(packet);
	}
	
}
```

Packages are classes that contain information. They are send and retrieved over the internet. The example above sends a String called "name". When it arrives at the server the server is notified. Then the server sends it back to the client. When it arrives at the client, the client will see the String "The server sends his regards!".

Now we need to register this package. You can pretty much register it wherever and however you want, but I like to do it in a static clause in my main class. Like so

```java
import com.sirolf2009.networking.Packet;

public class Main {

  public Main() {
  }

  public static void main(String[] args) {
    new Main();
  }

}
static {
  Packet.registerPacket(1, PacketHelloWorld.class);
}
```

We register our package with an ID of 1. If we were to create another package, it would have to be registered with an ID of 2. NEVER register your packages with an ID of 0 as it is already in use by an handshake package.

Now we have a server, a client and something to send. Last thing left is to actually send it.

```java
import com.sirolf2009.networking.Packet;

public class Main {

  public Main() {
    //Create a new server instance
    Server server = new Server();
    //Create a new client instance
    Client client = new Client();
    
    //Send a new Hello World packet from the client to the server
    client.getSender().send(new PacketHelloWorld("sirolf2009");
  }

  public static void main(String[] args) {
    new Main();
  }

}
static {
  Packet.registerPacket(1, PacketHelloWorld.class);
}
```

If all goes well, you should see this

```
Received "Hello World!" from: sirolf2009
The server sends his regards!
```

That's it!
The next step now would be to add another client, write more packages etc...
Be sure to checkout my War Triumph project for some more example code and checkout the Event class to see what kind of events you can check. If you have any questions, feel to comment, Skype(sirolf2009) or email(masterflappie@gmail.com)
Good luck!
