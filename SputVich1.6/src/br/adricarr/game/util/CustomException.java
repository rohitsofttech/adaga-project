package br.adricarr.game.util;

public class CustomException extends Exception {

	/**
	 *  ID da Classe
	 */
	private static final long serialVersionUID = 2822724644499841485L;
	
	protected String gMensagem;
	
	public CustomException(final String pMensagem){
		super(pMensagem);
		 this.gMensagem = pMensagem;
	}
	

	public String getMensagem() {
		return gMensagem;
	}

	public void setMensagem(String gMensagem) {
		this.gMensagem = gMensagem;
	}

}
