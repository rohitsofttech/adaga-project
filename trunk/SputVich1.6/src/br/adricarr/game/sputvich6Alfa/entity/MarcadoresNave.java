package br.adricarr.game.sputvich6Alfa.entity;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.layer.ILayer;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import br.adricarr.game.sputvich6Alfa.intefaces.INave;
import android.content.Context;
import android.util.Log;

public class MarcadoresNave {
    private static final String LOG = "CARCADORES";
    private Sprite gVelocimetro;
    private Sprite gMarcaVelocidade;
    private Sprite gMarcaAngulo;
    private float gLarguraVelocimentro;

    public MarcadoresNave(final int pPosicaoX, final int pPosicaoY,
	    final Context pContext, final Engine pEngine) {
	Texture lTexureMarcadores = new Texture(256, 256,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	TextureRegion lMedidorAnguloTextureRegion = TextureRegionFactory
		.createFromAsset(lTexureMarcadores, pContext,
			"senarios/imagens/medidorAngulo.png", 0, 0);
	TextureRegion lVelocimetroTextureRegion = TextureRegionFactory
		.createFromAsset(lTexureMarcadores, pContext,
			"senarios/imagens/velocimetro.png", 128, 0);
	TextureRegion lPonteiroTextureRegion = TextureRegionFactory
		.createFromAsset(lTexureMarcadores, pContext,
			"senarios/imagens/ponteiro2.png", 0, 128);
	pEngine.getTextureManager().loadTexture(lTexureMarcadores);

	this.gLarguraVelocimentro = lVelocimetroTextureRegion.getWidth();
	this.gVelocimetro = new Sprite(pPosicaoX - lVelocimetroTextureRegion.getWidth(), pPosicaoY, lVelocimetroTextureRegion);
	this.gVelocimetro.setScale((float) 1.3);
	this.gMarcaVelocidade = new Sprite(pPosicaoX - lVelocimetroTextureRegion.getWidth(), pPosicaoY, lPonteiroTextureRegion);
	this.gMarcaAngulo = new Sprite(pPosicaoX - lVelocimetroTextureRegion.getWidth(), pPosicaoY, lMedidorAnguloTextureRegion);
    }
    
    public void addNaScene(final ILayer pLayer) {
	try {
	    pLayer.addEntity(this.gVelocimetro);
	    pLayer.addEntity(this.gMarcaAngulo);
	    pLayer.addEntity(this.gMarcaVelocidade);
	    
	} catch (Exception e) {
	    Log.e(LOG, "Erro Ao Inserir Marcadores:" + e.getMessage());
	}
    }

    public void setInfMarcadores(final INave pNave) {
	this.gMarcaAngulo.setRotation((float) Math.toDegrees(pNave.getBody()
		.getAngle()));
	this.gMarcaVelocidade.setRotation(Math.abs(pNave.getBody()
		.getLinearVelocity().y * 15) - 43);
    }
    
    public void setPosicao(final float pPosicaoX, final float pPosicaoY) {
	float lPosicaoX =  pPosicaoX - this.gLarguraVelocimentro;
	float lPosicaoY = pPosicaoY;
	
	this.gVelocimetro.setPosition(lPosicaoX, lPosicaoY);
	this.gMarcaAngulo.setPosition(lPosicaoX, lPosicaoY);
	this.gMarcaVelocidade.setPosition(lPosicaoX, lPosicaoY);
    }
}
