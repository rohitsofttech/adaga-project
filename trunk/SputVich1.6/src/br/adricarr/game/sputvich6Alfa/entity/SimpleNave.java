package br.adricarr.game.sputvich6Alfa.entity;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class SimpleNave extends AbstractNave {

    private Texture gTexture;
    protected TiledTextureRegion gRTexture;

    public SimpleNave(final Engine pEngine, final Context pContext,
	    final PhysicsWorld pMundoFisico, final int pCameraAltura,
	    final int pCameraLargura) {
	this.gCameraAltura = pCameraAltura;
	this.gCameraLargura = pCameraLargura;

	this.gTexture = new Texture(1024, 1024,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	this.gRTexture = TextureRegionFactory.createTiledFromAsset(
		this.gTexture, pContext, "nave/imagens/nave_tiled.png", 0, 0,
		2, 2);
	pEngine.getTextureManager().loadTexture(this.gTexture);
	this.gSprite = new AnimatedSprite(0, 0, this.gRTexture);
	this.gBody = PhysicsFactory.createCircleBody(pMundoFisico,
		this.gSprite, BodyType.DynamicBody,
		PhysicsFactory.createFixtureDef(0.0f, .0f, 0.0f));
    }
}
