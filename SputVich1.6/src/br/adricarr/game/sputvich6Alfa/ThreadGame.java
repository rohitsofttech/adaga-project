package br.adricarr.game.sputvich6Alfa;

import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.badlogic.gdx.math.Vector2;

import android.util.Log;

public class ThreadGame extends Thread {
	
	/*
	 * Thread para genrenciar pontuação e  controles interno do jogo.
	 */
	
	/*
	 * Constantes
	 */
	private static final  String  LOG = "SPUTVICH";
	/*
	 * Variaveis Globais
	 */
	private float gConsumo;
	private boolean gAtivado;
	private SputVichGameActivity gGame;
	private int gContaTempo;
	/*
	 * Gets e Sets
	 */
	private float gCombustivel;
	public float getConsumo() {
		return gConsumo;
	}

	public void setConsumo(float gConsumo) {
		this.gConsumo = gConsumo;
	}

	public boolean isAtivado() {
		return gAtivado;
	}

	public void setAtivado(boolean gAtivado) {
		this.gAtivado = gAtivado;
	}
	
	/*
	 * Metodos
	 */
	
	public ThreadGame(final float pCombustivel,final float pConsumo,final SputVichGameActivity pGame){
		this.gCombustivel = pCombustivel;
		this.gConsumo = pConsumo;
		 this.gGame = pGame;
	}
	
	@Override
	public void run(){
		this.gAtivado = true;
		this.gContaTempo = 1;
		int contador=0;
	//	gGame.addMeteoro(10, 10, new Vector2(0, 0), new Vector2(0, 0));
		while (true) {
			gCombustivel = gCombustivel - gConsumo;
			gGame.getCombustivel().setText(""+ this.gCombustivel);
			if (gContaTempo %60 == 0&&gAtivado){				
				gGame.moveMeteoro(contador);//chama o meteoro na posi��o da lista 
				contador=(contador+1)%10;   //circula a lista 				 
				gGame.particulaMeteoro();
				gGame.criaVento((int)(Math.random()*10)); //
			}
			
			this.gContaTempo++;
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//gGame.chuvaDeMeteoro(8f);
		}
	}

}
