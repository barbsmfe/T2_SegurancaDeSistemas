package com.barbsmfe.domain;

import java.security.*;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AESDecodificadorCBC {

	private static final int TAMANHO_IV = 16;

	public byte[] toByteArray(String s) {
		return DatatypeConverter.parseHexBinary(s);
	}
	
	public String toHexString(byte[] array) {
		return DatatypeConverter.printHexBinary(array);
	}
	
	public IvParameterSpec extrairIvDeMensagemCodificada(byte[] mensagemCodificada) {

		byte[] iv = new byte[TAMANHO_IV];
		System.arraycopy(mensagemCodificada, 0, iv, 0, iv.length);

		return new IvParameterSpec(iv);
	}

	public byte[] extrairMensagemCodificadaSemIv(byte[] mensagemCodificada) {

		int tamanhoMensagemCodificadaSemIv = mensagemCodificada.length - TAMANHO_IV;
		byte[] mensagemCodificadaSemIv = new byte[tamanhoMensagemCodificadaSemIv];
		System.arraycopy(mensagemCodificada, TAMANHO_IV, mensagemCodificadaSemIv, 0, tamanhoMensagemCodificadaSemIv);

		System.out.println(toHexString(mensagemCodificadaSemIv));
		
		return mensagemCodificadaSemIv;
	}

	public static SecretKeySpec pegarChaveSecreta(String chave) throws Exception {
		
		byte[] chaveBytes = chave.getBytes();
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(chaveBytes, 0, chave.length());

		byte[] mdbytes = md.digest();

		return new SecretKeySpec(Arrays.copyOfRange(mdbytes, 0, 16), "AES");
	}
	
	public String decrypt(String encrypted, String chave) {
	    try {
	        IvParameterSpec iv = extrairIvDeMensagemCodificada(encrypted.getBytes());
	        SecretKeySpec skeySpec = pegarChaveSecreta(chave);
	 
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
	        byte[] original = cipher.doFinal(encrypted.getBytes());
	 
	        return new String(original);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	 
	    return null;
	}
	
	public String decodificarMensagem(String mensagemCodificada, String chave) throws Exception {
		
		byte[] mensagemCodificadaBytes = toByteArray(mensagemCodificada);

		IvParameterSpec ivExtraido = extrairIvDeMensagemCodificada(mensagemCodificadaBytes);
		SecretKeySpec chaveSecreta = pegarChaveSecreta(chave);
		byte[] mensagemCodificadaSemIv = extrairMensagemCodificadaSemIv(mensagemCodificadaBytes);
		Cipher decodificador = Cipher.getInstance("AES/CBC/PKCS5Padding");

		decodificador.init(Cipher.DECRYPT_MODE, chaveSecreta, ivExtraido);
		byte[] mensagemDecodificada = decodificador.doFinal(mensagemCodificadaSemIv);
		
		return toHexString(mensagemDecodificada);
	}

}
