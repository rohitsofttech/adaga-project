package br.adricarr.game.sputvich6Alfa.entity;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import br.adricarr.game.sputvich6Alfa.R;
import br.adricarr.game.sputvich6Alfa.dao.ConfiguracaoDao;
import br.adricarr.game.sputvich6Alfa.dao.ConfiguracaoDao.ConfiguracaoCursor;
import br.adricarr.game.sputvich6Alfa.dao.DadosSputvich;
import br.adricarr.game.sputvich6Alfa.intefaces.INave;

public abstract class ItensViewBuilder {

    public static final int MENU_RESET = 0;
    public static final int MENU_QUIT = MENU_RESET + 1;
    public static final int MENU_CONTROLES = MENU_QUIT + 1;

    public static DigitalOnScreenControl getControles(final int pPosicaoX,
	    final int pPosicaoY, final Camera pCamera, final INave pNave,
	    final Context pContext, final Engine pEngine) {

	Texture vOnScreenControlTexture = new Texture(256, 128,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	TextureRegion vOnScreenControlBaseTextureRegion = TextureRegionFactory
		.createFromAsset(vOnScreenControlTexture, pContext,
			"base/imagens/onscreen_control_base.png", 0, 0);
	TextureRegion vOnScreenControlKnobTextureRegion = TextureRegionFactory
		.createFromAsset(vOnScreenControlTexture, pContext,
			"base/imagens/onscreen_control_knob.png", 128, 0);
	pEngine.getTextureManager().loadTexture(vOnScreenControlTexture);

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

    public static MenuScene getMenuScene(final Camera pCamera,
	    final Context pContext, final Engine pEngine) {
	MenuScene vMenuScene = new MenuScene(pCamera);
	Texture lTexturaMenu = new Texture(256, 256,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	TextureRegion lMenuResetTextureRegion = TextureRegionFactory
		.createFromAsset(lTexturaMenu, pContext,
			"base/imagens/menu_reset.png", 0, 0);
	TextureRegion lMenuControlesTextureRegion = TextureRegionFactory
		.createFromAsset(lTexturaMenu, pContext,
			"base/imagens/menu_ controles.png", 0, 100);
	TextureRegion lMenuQuitTextureRegion = TextureRegionFactory
		.createFromAsset(lTexturaMenu, pContext,
			"base/imagens/menu_quit.png", 0, 50);
	pEngine.getTextureManager().loadTexture(lTexturaMenu);

	final SpriteMenuItem resetMenuItem = new SpriteMenuItem(MENU_RESET,
		lMenuResetTextureRegion);
	resetMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
		GL10.GL_ONE_MINUS_SRC_ALPHA);
	vMenuScene.addMenuItem(resetMenuItem);

	final SpriteMenuItem quitMenuItem = new SpriteMenuItem(MENU_QUIT,
		lMenuQuitTextureRegion);
	quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
		GL10.GL_ONE_MINUS_SRC_ALPHA);
	vMenuScene.addMenuItem(quitMenuItem);

	final SpriteMenuItem sensorMenuItem = new SpriteMenuItem(
		MENU_CONTROLES, lMenuControlesTextureRegion);
	sensorMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
		GL10.GL_ONE_MINUS_SRC_ALPHA);
	vMenuScene.addMenuItem(sensorMenuItem);

	vMenuScene.buildAnimations();
	vMenuScene.setBackgroundEnabled(false);
	vMenuScene.setOnMenuItemClickListener((IOnMenuItemClickListener) pContext);
	return vMenuScene;
    }
    
     public static void showConfiguracaoControles(final Context pContext, final ConfiguracaoDao gTabelaConfiguracao) {
	
	final Dialog dialog = new Dialog(pContext);
	final int lTipoConfiguracao;
	dialog.setContentView(R.layout.configuracao);
	dialog.setTitle("Configuração");
	dialog.show();
	
	Button lCancelar = (Button) dialog
		.findViewById(R.configuracao.cancelar);
	Button lConfirmar = (Button) dialog
		.findViewById(R.configuracao.confirmar);
	final RadioGroup lRadioGrupo = (RadioGroup) dialog
		.findViewById(R.configuracao.radioGroup);
	lTipoConfiguracao = gTabelaConfiguracao.getDaoCursor("SELECT * FROM CONFIGURACAO WHERE ID_CONFIGURACAO = 0", null, null).getTipoControle();
	lRadioGrupo.check(lTipoConfiguracao);
	
	lCancelar.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		dialog.cancel();
	    }
	});

	lConfirmar.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		ContentValues lUpdateValuess = new ContentValues();
		lUpdateValuess.put("TP_CONTROLES", lRadioGrupo.getCheckedRadioButtonId());
		if (gTabelaConfiguracao.update("CONFIGURACAO", lUpdateValuess, "ID_CONFIGURACAO = 0" , null) > 0)
		    dialog.cancel();
		
	    }
	});
    }


}
