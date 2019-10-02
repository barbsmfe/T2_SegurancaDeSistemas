package com.barbsmfe.domain;

import java.security.*;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCodificadorCBC {

	private static final int TAMANHO_IV = 16;

	public IvParameterSpec gerarIv() {

		byte[] iv = new byte[TAMANHO_IV];
		SecureRandom random = new SecureRandom();
		random.nextBytes(iv);

		return new IvParameterSpec(iv);
	}

	public SecretKeySpec pegarChaveSecreta(String chave) throws Exception {
		byte[] chaveBytes = chave.getBytes();

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(chaveBytes, 0, chave.length());
		byte[] mdBytes = md.digest();

		return new SecretKeySpec(Arrays.copyOfRange(mdBytes, 0, 16), "AES");
	}

	public byte[] encriptarMensagem(String mensagem, String chave) throws Exception {
		
		byte[] mensagemBytes = mensagem.getBytes();
		IvParameterSpec ivGerado = gerarIv();
		SecretKeySpec chaveSecreta = pegarChaveSecreta(chave);
		Cipher codificador = Cipher.getInstance("AES/CBC/PKCS5Padding");
		
		codificador.init(Cipher.ENCRYPT_MODE, chaveSecreta, ivGerado);
		byte[] mensagemCodificada = codificador.doFinal(mensagemBytes);
		
		return combinarIVComParteEncriptada(mensagemCodificada, ivGerado);		
	}

	private byte[] combinarIVComParteEncriptada(byte[] mensagemCodificada, IvParameterSpec ivGerado) {
		
		byte[] uniaoIvEMensagemEncriptados = new byte[TAMANHO_IV + mensagemCodificada.length];
		System.arraycopy(ivGerado, 0, uniaoIvEMensagemEncriptados, 0, TAMANHO_IV);
		System.arraycopy(mensagemCodificada, 0, uniaoIvEMensagemEncriptados, TAMANHO_IV, mensagemCodificada.length);
		
		return uniaoIvEMensagemEncriptados;
	}
}
