package br.adricarr.game.sputvich6Alfa.intefaces;

import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.anddev.andengine.entity.sprite.AnimatedSprite;

import br.adricarr.game.util.CustomException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public interface INave {
    public AnimatedSprite getSprite();

    public void setSprite(AnimatedSprite gSprite);

    public Body getBody();

    public void setBody(Body gBody);
    
    public void controlaBodyNave(int pControle);

    public IOnScreenControlListener GetScreenControlListener();

    public void controlaPorPosicaoNave(final float pX,final float pY);

    public void controlaPorArea(final float pX,final float pY);
    
    public void reset();
}
