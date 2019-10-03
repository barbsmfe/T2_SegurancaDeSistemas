package com.barbsmfe.domain;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AESDecodificador {

	private String tipoDeInstancia;
	private static final int TAMANHO_IV = 16;
	private static final String INSTANCIA_CBC = "AES/CBC/PKCS5Padding";
	private static final String INSTANCIA_CTR = "AES/CTR/NoPadding";
	
	public AESDecodificador(String instancia) {
		if(instancia.equals("CBC")) {
			tipoDeInstancia = INSTANCIA_CBC;
		} else if(instancia.equals("CTR")) {
			tipoDeInstancia = INSTANCIA_CTR;
		}
	}

	public static byte[] toByteArray(String s) {
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
		
		return mensagemCodificadaSemIv;
	}

	public static SecretKeySpec pegarChaveSecreta(String chave) throws Exception {
		return new SecretKeySpec(toByteArray(chave), "AES");
	}	
	
	public String decodificarMensagem(String mensagemCodificada, String chave) throws Exception {
		
		byte[] mensagemCodificadaBytes = toByteArray(mensagemCodificada);

		IvParameterSpec ivExtraido = extrairIvDeMensagemCodificada(mensagemCodificadaBytes);
		SecretKeySpec chaveSecreta = pegarChaveSecreta(chave);
		byte[] mensagemCodificadaSemIv = extrairMensagemCodificadaSemIv(mensagemCodificadaBytes);
		Cipher decodificador = Cipher.getInstance(tipoDeInstancia);

		decodificador.init(Cipher.DECRYPT_MODE, chaveSecreta, ivExtraido);
		byte[] mensagemDecodificada = decodificador.doFinal(mensagemCodificadaSemIv); 
		
		return new String(mensagemDecodificada);
	}

}
