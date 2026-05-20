package Trabalho2;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensagem implements Serializable {

    private String remetente;
    private String destinatario;
    private String conteudo;
    private LocalDateTime horario;

    // CONSTRUTOR
    public Mensagem(String remetente, String destinatario, String conteudo) {
    	this.remetente = remetente;
    	this.destinatario = destinatario;
    	this.conteudo = conteudo;
    	this.horario = LocalDateTime.now();
    }

    // METODOS
    public String getRemetente() {
    	return remetente;
    }

    public void setRemetente(String remetente) {
    	this.remetente = remetente;
    }

    public String getDestinatario() {
    	return destinatario;
    }

    public void setDestinatario(String destinatario) {
    	this.destinatario = destinatario;
    }

    public String getConteudo() {
    	return conteudo;
    }

    public void setConteudo(String conteudo) {
    	this.conteudo = conteudo;
    }

    public LocalDateTime getHorario() {
    	return horario;
    }

    public void setHorario(LocalDateTime horario) {
    	this.horario = horario;
    }
    
    // EXIBIÇÃO DA MENSAGEM
    @Override
    public String toString() {
    	DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    	String horaFormatada = this.horario.format(formatador);

    	if (destinatario == null) {
    		return "[" + horaFormatada + "] " + remetente + ": " + conteudo;
    	}

    	return "[" + horaFormatada + "] " + remetente + " -> " + destinatario + ": " + conteudo;
    }
}