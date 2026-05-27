package Trabalho2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class servidor_TCP {
	private int porta;
	private ServerSocket conexao;
	private static final int Num_threads = 8;
	private boolean rodando = true;
	
	public static ConcurrentHashMap<String, ThreadCliente> //ConcurrentHashMap permite que varias threads realizem acoes ao mesmo tempo
		clientes = new ConcurrentHashMap<>();
	
	public servidor_TCP(int porta) {
		this.porta = porta;
	}

	public void run() throws IOException{
		try{
			conexao = new ServerSocket(porta);//reserva a porta de escuta

			System.out.println("[Servidor] Aguardando conexões na porta " + conexao.getLocalPort() + "...");

			while(rodando) {
				try{
					Socket socket = conexao.accept();//espera ate o cliente se comunicar

					Thread comunicar = new Thread(new ThreadCliente(socket));//quando o cliente conecta, uma nova thread é criada para se comunicar com o cliente
					comunicar.start();
				} catch(IOException e){
					//Só ira mostrar erro se o servidor não estiver finalizado
					if(rodando){
						System.out.println("Erro ao aceitar conexão.");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Erro ao iniciar servidor.");
		}
		System.out.println("Servidor Finalizado.");
	}

	//METODO PARA PARAR O SERVIDOR DA INTERFACEPRINCIPAL
	public void pararServidor() {
		rodando = false;
		try {
			// FECHA TODOS CLIENTES
			for(ThreadCliente cliente : clientes.values()) {cliente.desconectar();
			}
			clientes.clear();

			// FECHA SERVIDOR
			conexao.close();

		} catch(IOException e) {

			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {

		servidor_TCP servidor = new servidor_TCP(1234);

		try {
			servidor.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}