package br.adricarr.game.sputvich6Alfa.entity;

import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.*;

import org.anddev.andengine.entity.sprite.AnimatedSprite;

import android.util.Log;
import br.adricarr.game.util.CustomException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Nave {
	private AnimatedSprite gSprite;
	private Body gBody;
	
	public Nave(){}
	
	public Nave(AnimatedSprite pSprite){
		this.gSprite = pSprite;
	}
	
	public Nave(AnimatedSprite pSprite, Body pBody){
		this.gSprite = pSprite;
		this.gBody = pBody;
	}
	
	public AnimatedSprite getSprite() {
		return gSprite;
	}
	
	public void setSprite(AnimatedSprite gSprite) {
		this.gSprite = gSprite;
	}
	
	public Body getBody() {
		return gBody;
	}
	
	public void setBody(Body gBody) {
		this.gBody = gBody;
	}
	
	public void  controlaBodyNave(int pControle){
		
		if (this.gBody == null) {
			try {
				throw new CustomException("Exception Null: Body da nave não instanciado");
			} catch (CustomException e) {
				e.printStackTrace();
			}
		}
		
		int vComandoExecultado = 0;
		float vAngulo;
		vAngulo = gBody.getAngle();
		Log.d(LOG, "Angulo da nave : "+ vAngulo);
		switch (pControle) {
		case CONTROL_CIMA:
			vComandoExecultado = 1; 
			Vector2 vetor = gBody.getLinearVelocity();
			float aceleracao = ((-VETOR_GRAVIDADE.y / 8) - (vetor.y / 10));
			gBody.applyLinearImpulse(new Vector2(0.0f,
					aceleracao), new Vector2(0, 0));
			break;
		case CONTROL_BAIXO:
			vComandoExecultado = 2;
			gBody.applyLinearImpulse(new Vector2(0.0f,
					0.15f), new Vector2(0, 0));
			break;
		case CONTROL_ESQUERDA:
			vComandoExecultado = 3;
			gBody.applyLinearImpulse(new Vector2(
					-0.05f, 0.0f), new Vector2(0, 0));
			gBody.setAngularVelocity(-0.5f);
			if (-vAngulo > Math.PI / 4)
				gBody.setAngularVelocity(0);
			break;
		case CONTROL_DIREITA:
			vComandoExecultado = 4;
			gBody.applyLinearImpulse(new Vector2(0.05f,
					0.0f), new Vector2(0, 0));
			gBody.setAngularVelocity(.5f);
			if (vAngulo > Math.PI / 4)
				gBody.setAngularVelocity(0);
			break;
		case CONTROL_NULL:
			if ((vComandoExecultado != 4)
					&& (vComandoExecultado != 3)) {
				gBody.setAngularVelocity(0);
				vAngulo = gBody.getAngle();
				if (vAngulo > 0.01f)
					gBody.setAngularVelocity(-0.3f);
				 else if (vAngulo < -0.01f)
					gBody.setAngularVelocity(0.3f);
				 else 
					gBody.setAngularVelocity(0);
			}
			break;
		}
	}

}
