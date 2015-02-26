package hearbeat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;

//La clase server extiende de Thread
public class Server extends Thread {
	private HashMap<String, Integer> tabla_clientes = new HashMap<String, Integer>();
	private int tiempoespera = 30;
	private int tiempopaso = 2;

	//Sincronización con el cliente
	public synchronized void agregarCliente(String id_cliente) {
		System.out.println((new Date()) + " Heartbeat recibido de "
				+ id_cliente);
		tabla_clientes.put(id_cliente, 0);
	}

	//Se ejecuta el hilo
	public void run() {
		
		//Mientras esto se cumpla
		while (true) {
			try {
				//Se ejecutará cada 1000 ms
				sleep(tiempopaso * 1000);
				synchronized (this) {
					String[] ids = tabla_clientes.keySet().toArray(
							new String[0]);
					for (String id_cliente : ids) {
						int tiempo_cliente = tabla_clientes.get(id_cliente);
						tiempo_cliente = tiempo_cliente + tiempopaso;
						if (tiempo_cliente > tiempoespera) {
							//Cuando terminan las pulsaciones
							System.out.println((new Date()) + " El cliente "
									+ id_cliente + " ha muerto");
							tabla_clientes.remove(id_cliente);
						} else {
							tabla_clientes.put(id_cliente, tiempo_cliente);
						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			// Creación del socket datagrama
			InetSocketAddress addr = new InetSocketAddress("localhost", 5555);
			DatagramSocket datagramSocket = new DatagramSocket(addr);
			byte[] buffer = new byte[128];
			DatagramPacket datagrama1 = new DatagramPacket(buffer,
					buffer.length);
			Server serv = new Server();
			serv.start();
			//Mientras se cumpla recibirá el datagrama
			while (true) {
				datagramSocket.receive(datagrama1);
				serv.agregarCliente(new String(datagrama1.getData()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
