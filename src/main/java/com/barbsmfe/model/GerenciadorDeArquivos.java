package com.barbsmfe.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GerenciadorDeArquivos {

	public String[] leitorDeArquivo(String nomeArquivo) {
		String linha = "";
		String[] instanciaSenhaETexto = new String[4];
		try {
			FileReader fileReader = new FileReader(nomeArquivo);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((linha = bufferedReader.readLine()) != null) {
				if(linha.contains("CBC") || linha.contains("CTR")) {
					instanciaSenhaETexto[0] = linha;	
				} else if (linha.contains("CHAVE")) {
					instanciaSenhaETexto[1] = linha.split(":")[1];
				} else if (linha.contains("CIPHERTEXT") || linha.contains("PLAINTEXT")) {
					instanciaSenhaETexto[2] = linha.split(":")[1];
					instanciaSenhaETexto[3] = linha.split(":")[0];
				}
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Não foi possível encontrar o arquivo:  '" + nomeArquivo + "'");
		} catch (IOException ex) {
			System.out.println("Erro ao ler o arquivo: '" + nomeArquivo + "'");
		}
		return instanciaSenhaETexto;
	}

}
