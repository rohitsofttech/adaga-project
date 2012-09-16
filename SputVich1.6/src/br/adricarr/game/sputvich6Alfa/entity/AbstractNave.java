package br.adricarr.game.sputvich6Alfa.entity;

import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_BAIXO;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_CIMA;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_DIREITA;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_ESQUERDA;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_NULL;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.anddev.andengine.entity.sprite.AnimatedSprite;

import android.util.Log;
import br.adricarr.game.sputvich6Alfa.intefaces.INave;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class AbstractNave implements INave {
    private static final String LOG = "NAVE";
    protected AnimatedSprite gSprite;
    protected Body gBody;
    protected int gCameraAltura;
    protected int gCameraLargura;
    protected int gPosicaoInicialX;
    protected int gPosicaoInicialY;
    protected Vector2 gImpolsoCima = new Vector2(0.0f, 1.0f);
    protected Vector2 gImpolsoBaixo = new Vector2(0.0f, 0.0f);
    protected Vector2 gImpolsoEsqueda = new Vector2(-0.05f, 0.0f);
    protected Vector2 gImpolsoDireta = new Vector2(0.05f, 0.0f);
    

    public AbstractNave(int pX, int pY, int pCameraAltura, int pCameraLargura) {
	this.gCameraAltura = pCameraAltura;
	this.gCameraLargura = pCameraLargura;
	this.gPosicaoInicialX = pX;
	this.gPosicaoInicialY = pY;
    }

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

	int vComandoExecultado = 0;
	float vAnguloAtual, vAngulo = 0;
	Vector2 vImpulso = null;
	vAnguloAtual = this.gBody.getAngle();

	switch (pControle) {
	/** Nota: Ver se a gravidade é realmente necessaria aqui! */
	case CONTROL_CIMA:
	    vComandoExecultado = 1;
	    vImpulso = this.gImpolsoCima;
	    break;
	case CONTROL_BAIXO:
	    vComandoExecultado = 2;
	    vImpulso = this.gImpolsoBaixo;
	    break;
	case CONTROL_ESQUERDA:
	    vComandoExecultado = 3;
	    vAngulo = -0.5f;
	    vImpulso = this.gImpolsoEsqueda;
	    break;
	case CONTROL_DIREITA:
	    vComandoExecultado = 4;
	    vAngulo = .5f;
	    vImpulso = this.gImpolsoDireta;
	    break;
	}

	if (vImpulso != null) {
	    this.gBody.applyLinearImpulse(vImpulso, new Vector2(0, 0));
	    Log.d(LOG, "O Angulo atual é: " + vAnguloAtual);
	    if ((Math.abs(vAnguloAtual) > Math.PI / 4))
		this.gBody.setAngularVelocity(0);
	    else if (vAngulo != 0)
		this.gBody.setAngularVelocity(vAngulo);
	}

	if ((vComandoExecultado != 4) && (vComandoExecultado != 3)) {
	    this.gBody.setAngularVelocity(0);
	    if (vAnguloAtual > 0.01f)
		this.gBody.setAngularVelocity(-0.3f);
	    else if (vAnguloAtual < -0.01f)
		this.gBody.setAngularVelocity(0.3f);
	    else
		this.gBody.setAngularVelocity(0);
	}
    }

    @Override
    public IOnScreenControlListener GetScreenControlListener() {
	return new IOnScreenControlListener() {

	    @Override
	    public void onControlChange(
		    final BaseOnScreenControl pBaseOnScreenControl,
		    final float pValueX, final float pValueY) {
		if ((pValueX != 0) || (pValueY != 0)) {
		    if (pValueY < 0.0f)
			controlaBodyNave(CONTROL_CIMA);
		    if (pValueY > 0.0f)
			controlaBodyNave(CONTROL_BAIXO);
		    if (pValueX < 0.0f)
			controlaBodyNave(CONTROL_ESQUERDA);
		    if (pValueX > 0.0f)
			controlaBodyNave(CONTROL_DIREITA);
		} else
		    controlaBodyNave(CONTROL_NULL);
	    }
	};
    }

    public void controlaPorArea(final float pX, final float pY) {
	if (pY < this.gCameraAltura * 0.33)
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

    public void controlaPorPosicaoNave(final float pX, final float pY) {
	if (pY < this.gSprite.getY() - this.gSprite.getWidth())
	    controlaBodyNave(CONTROL_CIMA);
	else if (pY > this.gSprite.getY() + this.gSprite.getWidth())
	    controlaBodyNave(CONTROL_BAIXO);
	if (pX < this.gSprite.getX() - this.gSprite.getHeight())
	    controlaBodyNave(CONTROL_ESQUERDA);
	else if (pX > this.gSprite.getX() + this.gSprite.getHeight())
	    controlaBodyNave(CONTROL_DIREITA);

    }
    
    @Override
    public void reset() {
	// TODO Auto-generated method stub
	this.gBody.setTransform(new Vector2( (this.gPosicaoInicialX +32)/ 32  , (this.gPosicaoInicialY+32)/32), 0);
	this.gBody.setLinearVelocity(new Vector2(0, 0));
	this.gBody.setAngularVelocity(0);
    }

}
 