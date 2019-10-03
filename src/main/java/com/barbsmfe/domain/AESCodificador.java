package com.barbsmfe.domain;

import java.security.*;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AESCodificador {

	private String tipoDeInstancia;
	private static final int TAMANHO_IV = 16;
	private static final String INSTANCIA_CBC = "AES/CBC/PKCS5Padding";
	private static final String INSTANCIA_CTR = "AES/CTR/NoPadding";
	
	public AESCodificador(String instancia) {
		if(instancia.equals("CBC")) {
			tipoDeInstancia = INSTANCIA_CBC;
		} else if(instancia.equals("CTR")) {
			tipoDeInstancia = INSTANCIA_CTR;
		}
	}

	public IvParameterSpec gerarIv() {

		byte[] iv = new byte[TAMANHO_IV];
		SecureRandom random = new SecureRandom();
		random.nextBytes(iv);

		return new IvParameterSpec(iv);
	}
	
	public static byte[] toByteArray(String s) {
		return DatatypeConverter.parseHexBinary(s);
	}
	
	public String toHexString(byte[] array) {
		return DatatypeConverter.printHexBinary(array);
	}
	
	public static SecretKeySpec pegarChaveSecreta(String chave) throws Exception {
		return new SecretKeySpec(toByteArray(chave), "AES");
	}
	
	public String encriptarMensagem(String mensagem, String chave) throws Exception {
		
		byte[] mensagemBytes = toByteArray(mensagem);
		IvParameterSpec ivGerado = gerarIv();
		SecretKeySpec chaveSecreta = pegarChaveSecreta(chave);
		Cipher codificador = Cipher.getInstance(tipoDeInstancia);
		
		codificador.init(Cipher.ENCRYPT_MODE, chaveSecreta, ivGerado);
		byte[] mensagemCodificada = codificador.doFinal(mensagemBytes);
		
		return combinarIVComParteEncriptada(mensagemCodificada, ivGerado);		
	}

	private String combinarIVComParteEncriptada(byte[] mensagemCodificada, IvParameterSpec ivGerado) {		
		return toHexString(ivGerado.getIV()) + toHexString(mensagemCodificada);
	}
}
