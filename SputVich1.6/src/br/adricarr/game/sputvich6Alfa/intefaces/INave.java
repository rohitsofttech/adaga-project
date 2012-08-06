package br.adricarr.game.sputvich6Alfa.intefaces;

import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.anddev.andengine.entity.sprite.AnimatedSprite;

import com.badlogic.gdx.physics.box2d.Body;

public interface INave {
    public AnimatedSprite getSprite();

    public void setSprite(AnimatedSprite gSprite);

    public Body getBody();

    public void setBody(Body gBody);

    public void controlaBodyNave(int pControle);

    public IOnScreenControlListener GetScreenControlListener();

    public void controlaPorPosicaoNave(AnimatedSprite pSprite, float pX, float pY);

    public void controlaPorArea(float pX, float pY);
}
