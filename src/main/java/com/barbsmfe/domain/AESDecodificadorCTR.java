package com.barbsmfe.domain;

import java.security.*;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESDecodificadorCTR {

	private static final int TAMANHO_IV = 16;

	public IvParameterSpec extrairIvDeMensagemCodificada(byte[] mensagemCodificada) {

		byte[] iv = new byte[TAMANHO_IV];
		System.arraycopy(mensagemCodificada, 0, iv, 0, iv.length);

		return new IvParameterSpec(iv);
	}

	public byte[] extrairMensagemCodificadaSemIv(byte[] mensagemCodificada) {

		int tamanhoMensagemCodificadaSemIv = mensagemCodificada.length - TAMANHO_IV;
		byte[] mensagemCodificadaSemIv = new byte[tamanhoMensagemCodificadaSemIv];
		System.arraycopy(mensagemCodificada, TAMANHO_IV, mensagemCodificadaSemIv, 0, tamanhoMensagemCodificadaSemIv);

		return mensagemCodificadaSemIv;
	}

	public static SecretKeySpec pegarChaveSecreta(String chave) throws Exception {

		byte[] chaveBytes = chave.getBytes();
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(chaveBytes, 0, chave.length());

		byte[] mdbytes = md.digest();

		return new SecretKeySpec(Arrays.copyOfRange(mdbytes, 0, 16), "AES");
	}

	public String decodificarMensagem(byte[] mensagemCodificada, String chave) throws Exception {

		IvParameterSpec ivExtraido = extrairIvDeMensagemCodificada(mensagemCodificada);
		SecretKeySpec chaveSecreta = pegarChaveSecreta(chave);
		byte[] mensagemCodificadaSemIv = extrairMensagemCodificadaSemIv(mensagemCodificada);
		Cipher decodificador = Cipher.getInstance("AES/CTR/NoPadding");

		decodificador.init(Cipher.DECRYPT_MODE, chaveSecreta, ivExtraido);
		byte[] mensagemDecodificada = decodificador.doFinal(mensagemCodificadaSemIv);

		return new String(mensagemDecodificada);
	}

}
