import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Observable;

public class Comunicacion extends Observable implements Runnable{
	
	private MulticastSocket socket;
	private final int PORT = 5000;
	private final String GROUP_ADDRESS = "224.2.2.2";
	private InetAddress ia;
	private int identifier;
	private boolean identified;

	public Comunicacion() {
		try {
			socket = new MulticastSocket(PORT);
			ia = InetAddress.getByName(GROUP_ADDRESS);
			socket.joinGroup(ia);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		grupo();


		try {
			socket.setSoTimeout(2000); 
			while (!identified) {
				respuestas();
			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void grupo(){
		Seria mensaje = new Seria("Hi i'm a new member");
		byte[] bytes = serialize(mensaje);
		try {
			sendMessage(bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void respuestas() throws IOException {
		try {
			DatagramPacket receivedPacket = receiveMessage();
			Object receivedObject = deserialize(receivedPacket.getData());

			if (receivedObject instanceof Seria) {
				Seria message = (Seria) receivedObject;
				String messageContent = message.getContent();

				if (messageContent.contains("I am:")) { 
					String[] partes = messageContent.split(":");
					int ID = Integer.parseInt(partes[1]);
					if (ID >= identifier) {
						identifier = ID + 1;
					}
					if (identifier >= 2) {
						InetAddress host = InetAddress.getByName(GROUP_ADDRESS);
						socket.leaveGroup(host);
						socket.close();
					}
				}
			}

		} catch (SocketTimeoutException e) {
			identified = true;
			socket.setSoTimeout(0);
			System.out.println("My AutoID is: " + identifier);
		}

	}

	private void gracias() {
		Seria message = new Seria("Hi, I am:" + identifier);
		byte[] bytes = serialize(message);
		try {
			sendMessage(bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private byte[] serialize(Object data) {
		byte[] bytes = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(data);
			bytes = baos.toByteArray();
			oos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	private Object deserialize(byte[] bytes) {
		Object data = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			data = ois.readObject();
			ois.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return data;
	}

	public void sendMessage(byte[] data) throws IOException {
		DatagramPacket packet = new DatagramPacket(data, data.length, ia, PORT);
		socket.send(packet);
	}

	public DatagramPacket receiveMessage() throws IOException {
		byte[] buffer = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);
		System.out.println("Se recibe");
		return packet;
	}

	@Override
	public void run() {
		while (true) {
			if (socket != null) {
				try {
					DatagramPacket receivedPacked = receiveMessage();
					Object receivedObject = deserialize(receivedPacked.getData());
					if (receivedObject != null) {
						if (receivedObject instanceof Seria) {
							Seria message = (Seria) receivedObject;
							String messageContent = message.getContent();
							if (messageContent.contains("new member")) {
								gracias();
							}
						}
						System.out.println("NO LLEGO");
						setChanged();
						notifyObservers(receivedObject);
						clearChanged();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public int getIdentifier() {
		// TODO Auto-generated method stub
		return this.identifier;
	}

}