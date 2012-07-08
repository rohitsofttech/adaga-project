package br.adricarr.game.sputvich6Alfa.entity;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class SpriteNave extends AnimatedSprite {

	public SpriteNave(float pX, float pY, float pTileWidth, float pTileHeight,
			TiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTileWidth, pTileHeight, pTiledTextureRegion);
		// TODO Auto-generated constructor stub
	}

	public SpriteNave(int i, int j, TiledTextureRegion gRegionTNave) {
		super(i, j, gRegionTNave);
	}
	@Override
	public boolean collidesWith(final IShape pOtherShape) {
		if (pOtherShape instanceof Line) {
			return true;
		} else {
			return super.collidesWith(pOtherShape);
		}
	}
}
