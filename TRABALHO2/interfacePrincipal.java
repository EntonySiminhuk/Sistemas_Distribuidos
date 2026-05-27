package Trabalho2;

import javax.swing.*;
import java.awt.*;

public class interfacePrincipal extends JFrame{
    private JButton btnIniciarServidor;
    private JButton btnPararServidor;
    private JButton btnCriarCliente;
    private JTextField campoNome;
    private JTextArea logs;
    private Thread threadServidor;
    private servidor_TCP servidor;

    public interfacePrincipal() {

        setTitle("Controle Chat TCP");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // PAINEL SUPERIOR
        JPanel topo = new JPanel();

        btnIniciarServidor = new JButton("Iniciar Servidor");
        btnPararServidor =  new JButton("Parar Servidor");

        topo.add(btnIniciarServidor);
        topo.add(btnPararServidor);

        add(topo, BorderLayout.NORTH);

        // CENTRO
        logs = new JTextArea();
        logs.setEditable(false);

        JScrollPane scroll = new JScrollPane(logs);

        add(scroll, BorderLayout.CENTER);

        // INFERIOR
        JPanel inferior = new JPanel();
        campoNome = new JTextField(15);
        btnCriarCliente = new JButton("Abrir Cliente");

        inferior.add(new JLabel("Nome:"));
        inferior.add(campoNome);
        inferior.add(btnCriarCliente);

        add(inferior, BorderLayout.SOUTH);

        // EVENTOS
        btnIniciarServidor.addActionListener(e -> {
            if(servidor != null) {
                logs.append("Servidor já está rodando.\n");
                return;
            }
            iniciarServidor();
        });

        btnCriarCliente.addActionListener(e -> {
            criarCliente();
        });

        btnPararServidor.addActionListener(e -> {
            if(servidor != null){
                servidor.pararServidor();
                servidor = null;
                threadServidor = null;
                logs.append("Servidor Finalizado. \n");
            }

        });

        setVisible(true);
    }

    // INICIAR SERVIDOR
    private void iniciarServidor() {

        threadServidor = new Thread(() -> {
            try {
                servidor = new servidor_TCP(1234);
                logs.append("Servidor iniciado.\n");
                servidor.run();

            } catch (Exception e) {
                logs.append("Erro servidor.\n");
            }
        });

        threadServidor.start();
    }

    // ABRIR CLIENTE
    private void criarCliente() {
        String nome = campoNome.getText();

        if(nome.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Digite um nome.");
            return;
        }

        new telaInterface(nome);
        logs.append("Cliente criado: " + nome + "\n");
    }

    public static void main(String[] args) {
        new interfacePrincipal();
    }
}
