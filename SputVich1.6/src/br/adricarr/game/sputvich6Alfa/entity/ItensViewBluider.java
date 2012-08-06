package br.adricarr.game.sputvich6Alfa.entity;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.content.Context;
import br.adricarr.game.sputvich6Alfa.intefaces.INave;


public abstract class ItensViewBluider {

    public static  DigitalOnScreenControl addControles(final int pPosicaoX, final int pPosicaoY, final Camera pCamera,
	    final INave pNave, final Context vContext, final Engine vEngine) {
	
	Texture vOnScreenControlTexture = new Texture(256, 128,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	TextureRegion vOnScreenControlBaseTextureRegion = TextureRegionFactory
		.createFromAsset(vOnScreenControlTexture, vContext,
			"base/imagens/onscreen_control_base.png", 0, 0);
	TextureRegion vOnScreenControlKnobTextureRegion = TextureRegionFactory
		.createFromAsset(vOnScreenControlTexture, vContext,
			"base/imagens/onscreen_control_knob.png", 128, 0);
	vEngine.getTextureManager().loadTexture(vOnScreenControlTexture);
	
	DigitalOnScreenControl vControle = new DigitalOnScreenControl(
		pPosicaoX, pPosicaoY, pCamera,
		vOnScreenControlBaseTextureRegion,
		vOnScreenControlKnobTextureRegion, 0.1f,
		pNave.GetScreenControlListener());
	
	vControle.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA,
		GL10.GL_ONE_MINUS_SRC_ALPHA);
	vControle.getControlBase().setAlpha(0.5f);
	vControle.getControlBase().setScaleCenter(0, 128);
	vControle.getControlBase().setScale(1.25f);
	vControle.getControlKnob().setScale(1.25f);
	vControle.refreshControlKnobPosition();

	return vControle;
   }
    
}
