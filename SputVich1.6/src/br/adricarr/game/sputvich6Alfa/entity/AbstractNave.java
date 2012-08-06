package br.adricarr.game.sputvich6Alfa.entity;

import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_BAIXO;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_CIMA;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_DIREITA;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_ESQUERDA;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_NULL;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.VETOR_GRAVIDADE;

import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.anddev.andengine.entity.sprite.AnimatedSprite;

import br.adricarr.game.sputvich6Alfa.intefaces.INave;
import br.adricarr.game.util.CustomException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class AbstractNave implements INave{
    protected AnimatedSprite gSprite;
    protected Body gBody;
    protected int gCameraAltura;
    protected int gCameraLargura;


    @Override
    public AnimatedSprite getSprite() {
	return this.gSprite;
    }

    @Override
    public void setSprite(AnimatedSprite gSprite) {
	this.gSprite = gSprite;
    }

    @Override
    public Body getBody() {
	return this.gBody;
    }

    @Override
    public void setBody(Body gBody) {
	this.gBody = gBody;
    }

    @Override
    public void controlaBodyNave(int pControle) {

	if (this.gBody == null) {
	    try {
		throw new CustomException(
			"Exception Null: Body da nave não instanciado");
	    } catch (CustomException e) {
		e.printStackTrace();
	    }
	}

	int vComandoExecultado = 0;
	float vAnguloAtual, vAngulo = 0;
	Vector2 vImpulso = null;

	vAnguloAtual = this.gBody.getAngle();

	switch (pControle) {
	/** Nota: Ver se a gravidade é realmente necessaria aqui! */
	case CONTROL_CIMA:
	    vComandoExecultado = 1;
	    Vector2 vetor = this.gBody.getLinearVelocity();
	    float aceleracao = ((-VETOR_GRAVIDADE.y / 8) - (vetor.y / 10));
	    vImpulso = new Vector2(0.0f, aceleracao);
	    break;
	case CONTROL_BAIXO:
	    vComandoExecultado = 2;
	    vImpulso = new Vector2(0.0f, 0.15f);
	    break;
	case CONTROL_ESQUERDA:
	    vComandoExecultado = 3;
	    vAngulo = -0.5f;
	    vImpulso = new Vector2(-0.05f, 0.0f);
	    break;
	case CONTROL_DIREITA:
	    vComandoExecultado = 4;
	    vAngulo = .5f;
	    vImpulso = new Vector2(0.05f, 0.0f);
	    break;
	}

	if (pControle == CONTROL_NULL) {
	    if ((vComandoExecultado != 4) && (vComandoExecultado != 3)) {
		this.gBody.setAngularVelocity(0);
		if (vAnguloAtual > 0.01f)
		    this.gBody.setAngularVelocity(-0.3f);
		else if (vAnguloAtual < -0.01f)
		    this.gBody.setAngularVelocity(0.3f);
		else
		    this.gBody.setAngularVelocity(0);
	    }
	} else {
	    this.gBody.applyLinearImpulse(vImpulso, new Vector2(0, 0));
	    this.gBody.setAngularVelocity(vAngulo);
	    if ((Math.abs(vAnguloAtual) > Math.PI / 4))
		this.gBody.setAngularVelocity(0);
	}
    }
    
    @Override
    public IOnScreenControlListener GetScreenControlListener(){
        return new IOnScreenControlListener() {
    
    	    @Override
    	    public void onControlChange(
    		    final BaseOnScreenControl pBaseOnScreenControl,
    		    final float pValueX, final float pValueY) {
    		if ((pValueX != 0.0) || (pValueY != 0.0)) {
    		    if (pValueY < 0.0f)
    			controlaBodyNave(CONTROL_CIMA);
    		    if (pValueY > 0.0f)
    			controlaBodyNave(CONTROL_BAIXO);
    		    if (pValueX < 0.0f)
    			controlaBodyNave(CONTROL_ESQUERDA);
    		    if (pValueX > 0.0f)
    			controlaBodyNave(CONTROL_DIREITA);
    		}
    		controlaBodyNave(CONTROL_NULL);
    	    }
    	};
    }
    
    public void controlaPorArea(float pX, float pY) {
	if (pX == 0 && pY == 0)
	    controlaBodyNave(CONTROL_NULL);
	else if (pY < this.gCameraAltura * 0.33)
	    controlaBodyNave(CONTROL_CIMA);
	else if (pY > this.gCameraAltura * 0.66)
	    controlaBodyNave(CONTROL_BAIXO);
	else {
	    if (pX < this.gCameraAltura / 2)
		controlaBodyNave(CONTROL_ESQUERDA);
	    else if (pX > this.gCameraLargura / 2)
		controlaBodyNave(CONTROL_DIREITA);
	} 	    
    }

    public void controlaPorPosicaoNave(AnimatedSprite pSprite, float pX,
	    float pY) {
	if (pX == 0 && pY == 0)
	    controlaBodyNave(CONTROL_NULL);
	else {
	    if (pY < pSprite.getY() - pSprite.getWidth())
		controlaBodyNave(CONTROL_CIMA);
	    else if (pY > pSprite.getY() + pSprite.getWidth())
		controlaBodyNave(CONTROL_BAIXO);
	    if (pX < pSprite.getX() - pSprite.getHeight())
		controlaBodyNave(CONTROL_ESQUERDA);
	    else if (pX > pSprite.getX() + pSprite.getHeight())
		controlaBodyNave(CONTROL_DIREITA);

	}
    }
}
