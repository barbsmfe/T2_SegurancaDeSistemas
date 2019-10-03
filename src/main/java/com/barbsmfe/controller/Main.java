package com.barbsmfe.controller;

import com.barbsmfe.domain.*;
import com.barbsmfe.model.*;

public class Main {

	public static void main(String[] args) throws Exception {

		GerenciadorDeArquivos gerenciadorDeArquivos = new GerenciadorDeArquivos();
		
		String[] instanciaSenhaETexto = gerenciadorDeArquivos.leitorDeArquivo("tarefa6.txt");
	
		AESDecodificador decodificador = new AESDecodificador(instanciaSenhaETexto[0]);
		AESCodificador codificador = new AESCodificador(instanciaSenhaETexto[0]);
				
		String chave = instanciaSenhaETexto[1];
		String mensagem = instanciaSenhaETexto[2];
		
		if(instanciaSenhaETexto[3].equals("CIPHERTEXT")) {
			System.out.println("Decriptada: " + decodificador.decodificarMensagem(mensagem, chave));
		} else if(instanciaSenhaETexto[3].equals("PLAINTEXT")) {
			String mensagemEncriptada = codificador.encriptarMensagem(mensagem, chave);
			String mensagemDecriptada = decodificador.decodificarMensagem(mensagemEncriptada, chave);
			System.out.println("Encriptada: " + mensagemEncriptada + "\nDecriptada: " + mensagemDecriptada);
		}
	}
	
}
