package Trabalho2;


import java.net.Socket;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Cliente_TCP {
	private int porta;
	private String host;
	
	public Cliente_TCP(String host, int porta) {
		this.host = host;
		this.porta = porta;
	}

	public static void main(String[] args) {

		try {
			Socket socket = new Socket("localhost", 1234);

			// STREAMS DE OBJETO
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream()); //saida dos objetos
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			Scanner teclado = new Scanner(System.in);
			boolean conectado = false; //variavel para verificar a conectividade do cliente
			String nome = "";

			while (!conectado) {
			    System.out.print("Digite seu nome: ");
			    nome = teclado.nextLine();

			    // Envia o nome para o servidor testar
			    output.writeObject(nome);
			    output.flush();

			    // Recebe a resposta do teste
			    Mensagem respostaServidor = (Mensagem) input.readObject();

			    if (respostaServidor.getConteudo().equals("OK")) {
			    	conectado = true; // Quebra o loop do login e segue em frente
			    } else {
			    	// Se cair aqui, é porque o conteúdo é "ERRO: Nome já em uso."
			    	System.out.println(respostaServidor.getConteudo() + " Por favor, escolha outro.");
			    	// O loop continua e pede o nome novamente na próxima volta!
			    }
			}

			// THREAD QUE RECEBE
			Thread receber = new Thread(new ThreadEscuta(socket, input));
			receber.start();

			System.out.println("Conectado a sala de bate papo!");

			// LOOP DE ENVIO
			while (true) {

				System.out.print("> ");
				String texto = teclado.nextLine();
				Mensagem mensagem; //sempre declarada vazia para cada mensagem ser diferente

				// PRIVADO
				if(texto.startsWith("/privado")) {

					String[] partes = texto.split(" ", 3);
					String destino = partes[1];
					String conteudo = partes[2];

					mensagem = new Mensagem(nome, destino, conteudo);

				} else {

					// BROADCAST
					mensagem = new Mensagem(nome,null, texto);
				}
				
				//prepara e envia a mensagem
				output.writeObject(mensagem);
				output.flush();
			}

		} catch (Exception e) {

			System.out.println("Erro ao conectar.");
		}
	}
}