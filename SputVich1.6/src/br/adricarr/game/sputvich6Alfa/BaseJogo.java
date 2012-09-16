/* 
 * @Autor: Adriano Cássio
 * @Classe: BaseJogo
 * @Descrição: Será a Classe principal de todas as fases do jogo
 * usando classes de apoio para controles e som. 
 *  */
package br.adricarr.game.sputvich6Alfa;

import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_BAIXO;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_CIMA;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_DIREITA;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_ESQUERDA;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_NULL;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.ZoomCamera;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;
import br.adricarr.game.sputvich6Alfa.dao.ConfiguracaoDao;
import br.adricarr.game.sputvich6Alfa.dao.ConfiguracaoDao.ConfiguracaoCursor;
import br.adricarr.game.sputvich6Alfa.dao.DadosSputvich;
import br.adricarr.game.sputvich6Alfa.entity.ItensViewBuilder;
import br.adricarr.game.sputvich6Alfa.entity.MarcadoresNave;
import br.adricarr.game.sputvich6Alfa.entity.SimpleNave;
import br.adricarr.game.sputvich6Alfa.intefaces.INave;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;

public class BaseJogo extends BaseGameActivity implements
	IOnMenuItemClickListener, SensorEventListener, IOnSceneTouchListener {

    private static final String LOG = "BASE GAME";
    /**
     * Objetos Globais
     */
    protected DadosSputvich gDados;
    protected ConfiguracaoDao gTabelaConfiguracao;
    protected int gCameraLargura;
    protected int gCameraAltura;
    protected ZoomCamera gCamera;
    protected PhysicsWorld gMundoFisico;
    protected Scene gScene;
    protected MenuScene gMenuScene;
    protected INave gNave;
    protected DigitalOnScreenControl gControle;
    protected MarcadoresNave gMarcadores;
    protected final FixtureDef FIXTURE_NAVE = PhysicsFactory.createFixtureDef(
	    0.0f, .0f, 0.0f);
    private boolean cControlerSetado;
    private int cControleZ;
    private int cControleY;
    private int cControleX;
    private float gTouchX;
    private float gTouchY;
    private boolean gTouchDown;
    private int gTipoConfiguracao;
    private String gNomeJogador;
    private TextureRegion mParallaxLayerBack;
    private Texture gTextureFundo;
    private TMXTiledMap gTmxTiledMap;
    
    

    @Override
    public Engine onLoadEngine() {
	Display lDisplay = ((WindowManager) getSystemService(WINDOW_SERVICE))
		.getDefaultDisplay();
	this.gCameraLargura = lDisplay.getWidth();
	this.gCameraAltura  = lDisplay.getHeight();
	
	this.gCamera = new ZoomCamera(0, 0, this.gCameraLargura,
		this.gCameraAltura);
	this.gCamera.setBounds(0, 4000, 0, 960);
	this.gCamera.setBoundsEnabled(true);
	return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
		new RatioResolutionPolicy(this.gCameraLargura,
			this.gCameraAltura), this.gCamera));
    }

    @Override
    public void onLoadResources() {
	this.gTextureFundo = new Texture(4096, 1024,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	this.mParallaxLayerBack = TextureRegionFactory.createFromAsset(
		this.gTextureFundo, this,
		"senarios/imagens/senarioJogoTeste.png", 0, 0);
	this.mEngine.getTextureManager().loadTexture(this.gTextureFundo);
	this.gDados = new DadosSputvich(this);
	this.gTabelaConfiguracao = new ConfiguracaoDao(this.gDados);
	this.gMundoFisico = new PhysicsWorld(new Vector2(0,
		SensorManager.GRAVITY_MOON), false);
	this.gNave = new SimpleNave(50, 50, this.mEngine, this,
		this.gMundoFisico, this.gCameraAltura, this.gCameraLargura);
	this.gControle =  ItensViewBuilder.getControles(0, this.gCameraAltura, this.gCamera,  this.gNave, this, this.mEngine);
	this.gMarcadores = new MarcadoresNave(this.gCameraLargura, 0, this, this.mEngine);
	this.gMenuScene = ItensViewBuilder.getMenuScene(this.gCamera, this,
		this.mEngine);
    }

    @Override
    public Scene onLoadScene() {
	this.mEngine.registerUpdateHandler(new FPSLogger());
	this.gScene = new Scene(3);
	try {
	    final TMXLoader tmxLoader = new TMXLoader(this,
		    this.mEngine.getTextureManager(), TextureOptions.NEAREST);
	    this.gTmxTiledMap = tmxLoader.loadFromAsset(this,
		    "senarios/tiled/tiledText.tmx");
	} catch (final TMXLoadException tmxle) {
	    Debug.e(tmxle);
	}
	final TMXLayer tmxLayer = this.gTmxTiledMap.getTMXLayers().get(0);
	this.gScene.getBottomLayer().addEntity(tmxLayer);
	this.gScene.registerUpdateHandler(this.gMundoFisico);
	this.gScene.setChildScene(this.gControle);
	this.gMarcadores.addNaScene(this.gScene.getTopLayer());
	CriaParedes();

	this.gScene.registerUpdateHandler(new IUpdateHandler() {
	    @Override
	    public void reset() {
		// TODO Auto-generated method stub
	    }

	    @Override
	    public void onUpdate(float pSecondsElapsed) {
		sistemaDeInteracao();
	    }
	});

	return this.gScene;
    }

    @Override
    public void onLoadComplete() {
	// TODO Auto-generated method stub
    }

    public void sistemaDeInteracao() {
	// TODO Auto-generated method stub
	switch (this.gTipoConfiguracao) {
	case R.configuracao.acelerometro:
	    break;
	case R.configuracao.porArena:
	    break;
	case R.configuracao.proporcional:
	    break;
	}
	onMoveCamera(this.gNave);
	this.gMarcadores.setInfMarcadores(this.gNave);
    }

    @Override
    public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
	if (pKeyCode == KeyEvent.KEYCODE_MENU) {
	    if (this.gScene.getChildScene() == this.gMenuScene) {
		this.gMenuScene.back();
		this.gScene.setChildScene(this.gControle);
	    } else
		this.gScene.setChildScene(this.gMenuScene, false, true, true);
	    return true;
	}
	return super.onKeyDown(pKeyCode, pEvent);
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
	switch (pSceneTouchEvent.getAction()) {
	case TouchEvent.ACTION_DOWN:
	    this.gTouchX = pSceneTouchEvent.getX();
	    this.gTouchY = pSceneTouchEvent.getY();
	    this.gTouchDown = true;
	    return true;
	case TouchEvent.ACTION_MOVE:
	    this.gTouchX = pSceneTouchEvent.getX();
	    this.gTouchY = pSceneTouchEvent.getY();
	    return true;
	case TouchEvent.ACTION_UP:
	    this.gTouchDown = false;
	    return true;
	}
	return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	// TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
	synchronized (this) {
	    switch (event.sensor.getType()) {
	    case Sensor.TYPE_ACCELEROMETER:
		if (!this.cControlerSetado)
		    SetVarControles(event);
		int z = (int) event.values[2];
		int y = (int) event.values[1];
		int x = (int) event.values[0];
		if (this.cControleY > 6) {
		    if (x < this.cControleX + 1)
			this.gNave.controlaBodyNave(CONTROL_DIREITA);
		    else if (x > this.cControleX - 1)
			this.gNave.controlaBodyNave(CONTROL_ESQUERDA);
		    if (z < this.cControleZ + 1)
			this.gNave.controlaBodyNave(CONTROL_CIMA);
		    else if (z > this.cControleZ - 1)
			this.gNave.controlaBodyNave(CONTROL_BAIXO);
		} else if (this.cControleY <= 6) {
		    if (x < this.cControleX + 1)
			this.gNave.controlaBodyNave(CONTROL_DIREITA);
		    else if (x > this.cControleX - 1)
			this.gNave.controlaBodyNave(CONTROL_ESQUERDA);
		    if (y < this.cControleY + 1)
			this.gNave.controlaBodyNave(CONTROL_CIMA);
		    else if (y < this.cControleY - 1)
			this.gNave.controlaBodyNave(CONTROL_BAIXO);
		}
		this.gNave.controlaBodyNave(CONTROL_NULL);
		break;
	    }
	}
    }

    private void SetVarControles(SensorEvent event) {
	this.cControleZ = (int) event.values[2];
	this.cControleY = (int) event.values[1];
	this.cControleX = (int) event.values[0];
	this.cControlerSetado = true;
    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
	    float pMenuItemLocalX, float pMenuItemLocalY) {
	switch (pMenuItem.getID()) {
	case ItensViewBuilder.MENU_QUIT:
	    this.finish();
	    break;
	case ItensViewBuilder.MENU_RESET:
	    onResetScene();
	    this.gMenuScene.back();
	    break;
	case ItensViewBuilder.MENU_CONTROLES:
	    ItensViewBuilder.showConfiguracaoControles(this,
		    this.gTabelaConfiguracao);
	    carregaConfiguracao();
	    this.gMenuScene.back();
	    break;
	}
	this.gScene.setChildScene(this.gControle);
	return true;
    }

    private void carregaConfiguracao() {
	ConfiguracaoCursor lConfigCorsor = this.gTabelaConfiguracao
		.getDaoCursor(
			"SELECT * FROM CONFIGURACAO WHERE ID_CONFIGURACAO = 0",
			null, null);
	this.gTipoConfiguracao = lConfigCorsor.getTipoControle();
	this.gNomeJogador = lConfigCorsor.getNomeJogador();
    }

    private void onResetScene() {
	this.gNave.reset();
    }

    private void CriaParedes() {
	Shape esquerda = new Rectangle(0, 0, 2, 900);
	PhysicsFactory.createBoxBody(this.gMundoFisico, esquerda,
		BodyType.StaticBody, this.FIXTURE_NAVE);
	this.gScene.getBottomLayer().addEntity(esquerda);
	Shape direita = new Rectangle(4000 - 2, 0, 2,
		900);
	PhysicsFactory.createBoxBody(this.gMundoFisico, direita,
		BodyType.StaticBody, this.FIXTURE_NAVE);
	this.gScene.getBottomLayer().addEntity(direita);
	Shape baixo = new Rectangle(0, 900 - 2,
		4000, 2);
	PhysicsFactory.createBoxBody(this.gMundoFisico, baixo,
		BodyType.StaticBody, this.FIXTURE_NAVE);
	this.gScene.getBottomLayer().addEntity(baixo);
	this.gScene.getLayer(2).addEntity(this.gNave.getSprite());
	this.gMundoFisico.registerPhysicsConnector(new PhysicsConnector(
		this.gNave.getSprite(), this.gNave.getBody()));
    }
    
    private void onMoveCamera(final INave pNave) {
	if (((pNave.getSprite().getX() > 240) || (pNave.getSprite().getX() < 640))
		|| ((pNave.getSprite().getY()) > 400)
		|| (pNave.getSprite().getY() < 3600)) {
	    this.gCamera.setCenter(pNave.getSprite().getX(), pNave.getSprite()
		    .getY());
	    this.gMarcadores.setPosicao(
		    this.gCamera.getCenterX() + this.gCameraLargura/2,
		    this.gCamera.getCenterY() - this.gCameraAltura/2);
	}
	
    }

}