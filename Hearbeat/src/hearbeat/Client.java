package hearbeat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// Creo la clase cliente del Heartbeat
public class Client {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("arguments: <id del cliente>");
			System.exit(1);
		}
		String id_cliente = args[0];
		try {
			//Creo el datagram socket
			DatagramSocket datagramSocket = new DatagramSocket();
			InetAddress addr1 = InetAddress.getByName("localhost");
			DatagramPacket datagrama1 = new DatagramPacket(
					id_cliente.getBytes(), id_cliente.getBytes().length, addr1,
					5555);
			//Si llega el mensaje
			while (true) {
				datagramSocket.send(datagrama1);
				Thread.sleep(10 * 1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}