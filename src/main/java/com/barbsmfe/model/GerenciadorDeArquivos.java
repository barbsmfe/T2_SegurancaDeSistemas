package com.barbsmfe.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GerenciadorDeArquivos {

	public String[] leitorDeArquivo(String nomeArquivo) {
		String linha = "";
		String[] senhaETexto = { "", "" };
		try {
			FileReader fileReader = new FileReader(nomeArquivo);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((linha = bufferedReader.readLine()) != null) {
				if (linha.contains("CBC Key:") || linha.contains("CTR key:")) {
					senhaETexto[0] = linha.split(": ")[1];
				} else if (linha.contains("CTR Plaintext:") || linha.contains("CBC Plaintext:")) {
					senhaETexto[1] = linha.split(": ")[1];
				}
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Não foi possível encontrar o arquivo:  '" + nomeArquivo + "'");
		} catch (IOException ex) {
			System.out.println("Erro ao ler o arquivo: '" + nomeArquivo + "'");
		}
		return senhaETexto;
	}

}
