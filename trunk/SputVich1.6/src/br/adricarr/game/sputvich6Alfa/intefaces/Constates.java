package br.adricarr.game.sputvich6Alfa.intefaces;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;

public interface Constates {
	
	public static final Vector2 VETOR_GRAVIDADE = new Vector2(0,
			SensorManager.GRAVITY_MOON);
	public static final String LOG = "SPUTVICH";
	
	public static final int CONTROL_NULL = 0; 
	public static final int CONTROL_CIMA = 1; 
	public static final int CONTROL_BAIXO = 2;
	public static final int CONTROL_ESQUERDA = 3;
	public static final int CONTROL_DIREITA = 4;
	
	public static final int ACTIVITY_CONFIGURACAO_CONTROL = 1;

}
