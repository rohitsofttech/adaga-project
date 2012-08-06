package br.adricarr.game.sputvich6Alfa.Antigo;

import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_BAIXO;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_CIMA;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_DIREITA;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_ESQUERDA;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.CONTROL_NULL;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.LOG;
import static org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import br.adricarr.game.sputvich6Alfa.BaseJogo;
import br.adricarr.game.sputvich6Alfa.R;
import br.adricarr.game.sputvich6Alfa.ThreadGame;
import br.adricarr.game.sputvich6Alfa.dao.ConfiguracaoDao;
import br.adricarr.game.sputvich6Alfa.dao.DadosSputvich;
import br.adricarr.game.sputvich6Alfa.entity.AbstractNave;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class AntigoBaseGAme extends BaseGameActivity implements
	IOnMenuItemClickListener, SensorEventListener, IOnSceneTouchListener {

    private DadosSputvich gDados;
    protected int CAMERA_LARGURA = 600;
    protected int CAMERA_ALTURA = 480;
    protected Camera gCamera;
    protected PhysicsWorld gMundoFisico;
    
    protected Scene gScene;
    protected ThreadGame gThreadGame;
    protected MenuScene gMenuScene;
    private SensorManager gSensorManager;
    protected final FixtureDef FIXTURE_NAVE = PhysicsFactory.createFixtureDef(
	    0.0f, .0f, 0.0f);
    protected static final Vector2 gVetor_Gravidade = new Vector2(0,
	    SensorManager.GRAVITY_MOON);
    protected static final int MENU_RESET = 0;
    protected static final int MENU_QUIT = MENU_RESET + 1;
    protected static final int MENU_CONTROLES = MENU_QUIT + 1;

    protected int cControleX;
    protected int cControleY;
    protected int cControleZ;
    private float gTouchX;
    private float gTouchY;
    private boolean gControlePorArea = false;
    private boolean gControlePorPosicaoNave = true;
    protected boolean cControlerSetado = true;
    private boolean gTouchDown = false;
    private boolean gTouchAbilitado = true;
    private boolean gControleAcelerometro;

    private AbstractNave gNave;

    protected Sprite marcaVelocidade;
    protected Sprite marcaAngulo;

    /* Texturas */

    protected Texture gMenuTexture;
    protected Texture gTextureFont;
    protected Texture gPlacarTexture;
    protected Texture gMedidorAnguloTexture;
    protected Texture gVelocimetroTexture;
    protected Texture gPonteiroTexture;
    protected Texture gFundoTexture;
    protected Texture gOnScreenControlTexture;

    protected TextureRegion mMenuResetTextureRegion;
    protected TextureRegion mMenuQuitTextureRegion;
    protected TextureRegion mMenuSensorTextureRegion;
    protected TextureRegion gMedidorAnguloTextureRegion;
    protected TextureRegion gVelocimetroTextureRegion;
    protected TextureRegion gPonteiroTextureRegion;
    protected TextureRegion gFundoTextureRegion;
    protected TextureRegion gOnScreenControlBaseTextureRegion;
    protected TextureRegion gOnScreenControlKnobTextureRegion;

    protected DigitalOnScreenControl gDigitalOnScreenControl;
    protected Font gFont;
    protected TextureRegion gPlacarTextureRegion;
    protected Sprite gPlacar;
    protected ChangeableText gCombustivel;
    protected Texture gTextureTeste;
    protected TiledTextureRegion gRTextureNave;
    private ConfiguracaoDao gTabelaConfiguracao;

    @Override
    public Engine onLoadEngine() {
	Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
		.getDefaultDisplay();
	this.CAMERA_LARGURA = display.getWidth();
	this.CAMERA_ALTURA = display.getHeight();
	this.gCamera = new Camera(0, 0, this.CAMERA_LARGURA, this.CAMERA_ALTURA);
	Engine engine = new Engine(new EngineOptions(true,
		ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(
			this.CAMERA_LARGURA, this.CAMERA_ALTURA), this.gCamera));
	return engine;
    }

    @Override
    public void onLoadResources() {

	this.gMenuTexture = new Texture(256, 256,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	this.mMenuResetTextureRegion = TextureRegionFactory.createFromAsset(
		this.gMenuTexture, this, "base/imagens/menu_reset.png", 0, 0);
	this.mMenuQuitTextureRegion = TextureRegionFactory.createFromAsset(
		this.gMenuTexture, this, "base/imagens/menu_quit.png", 0, 50);
	this.mMenuSensorTextureRegion = TextureRegionFactory.createFromAsset(
		this.gMenuTexture, this, "base/imagens/menu_ controles.png", 0,
		100);

	this.gTextureFont = new Texture(256, 256,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	this.gFont = new Font(this.gTextureFont, Typeface.create(
		Typeface.DEFAULT, Typeface.BOLD), 16, true, Color.BLACK);

	this.gPlacarTexture = new Texture(512, 64,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	this.gPlacarTextureRegion = TextureRegionFactory.createFromAsset(
		this.gPlacarTexture, this, "base/imagens/placar.png", 0, 0);

	this.gMedidorAnguloTexture = new Texture(128, 128,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	this.gMedidorAnguloTextureRegion = TextureRegionFactory
		.createFromAsset(this.gMedidorAnguloTexture, this,
			"senarios/imagens/medidorAngulo.png", 0, 0);

	this.gVelocimetroTexture = new Texture(128, 128,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	this.gVelocimetroTextureRegion = TextureRegionFactory.createFromAsset(
		this.gVelocimetroTexture, this,
		"senarios/imagens/velocimetro.png", 0, 0);

	this.gPonteiroTexture = new Texture(128, 128,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	this.gPonteiroTextureRegion = TextureRegionFactory.createFromAsset(
		this.gPonteiroTexture, this, "senarios/imagens/ponteiro2.png",
		0, 0);

	this.gOnScreenControlTexture = new Texture(256, 128,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	this.gOnScreenControlBaseTextureRegion = TextureRegionFactory
		.createFromAsset(this.gOnScreenControlTexture, this,
			"base/imagens/onscreen_control_base.png", 0, 0);
	this.gOnScreenControlKnobTextureRegion = TextureRegionFactory
		.createFromAsset(this.gOnScreenControlTexture, this,
			"base/imagens/onscreen_control_knob.png", 128, 0);

	this.gFundoTexture = new Texture(512, 1024, TextureOptions.DEFAULT);
	this.gFundoTextureRegion = TextureRegionFactory.createFromAsset(
		this.gFundoTexture, this, "senarios/imagens/fudo.png", 0, 0);

	this.gTextureTeste = new Texture(1024, 1024,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	this.gRTextureNave = TextureRegionFactory.createTiledFromAsset(
		this.gTextureTeste, this, "nave/imagens/nave_tiled.png", 0, 0,
		2, 2);

	this.mEngine.getTextureManager().loadTextures(this.gMenuTexture,
		this.gTextureFont, this.gPlacarTexture, this.gTextureTeste,
		this.gVelocimetroTexture, this.gOnScreenControlTexture,
		this.gMedidorAnguloTexture, this.gPonteiroTexture,
		this.gFundoTexture);
	this.mEngine.getFontManager().loadFont(this.gFont);
    }

    @Override
    public Scene onLoadScene() {
	this.mEngine.registerUpdateHandler(new FPSLogger());
	this.gScene = new Scene(3);
	this.gSensorManager = (SensorManager) this
		.getSystemService(Context.SENSOR_SERVICE);
	final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(
		0, 0, 0, 5);
	autoParallaxBackground.addParallaxEntity(new ParallaxEntity(0,
		new Sprite(0, 0, this.gFundoTextureRegion)));
	this.gScene.setBackground(autoParallaxBackground);
	this.gMundoFisico = new PhysicsWorld(gVetor_Gravidade, false);
	this.gScene.registerUpdateHandler(this.gMundoFisico);
	this.createMenuScene();
	this.createPontuacao();
	//this.setNave(new AbstractNave());
	this.gScene.setOnSceneTouchListener(this);

	// this.gDigitalOnScreenControl = this.addControles();
	// gScene.setChildScene(this.gDigitalOnScreenControl);

	Shape esquerda = new Rectangle(0, 0, 2, this.CAMERA_ALTURA);

	PhysicsFactory.createBoxBody(this.gMundoFisico, esquerda,
		BodyType.StaticBody, this.FIXTURE_NAVE);
	this.gScene.getBottomLayer().addEntity(esquerda);

	Shape direita = new Rectangle(this.CAMERA_LARGURA - 2, 0, 2,
		this.CAMERA_ALTURA);
	PhysicsFactory.createBoxBody(this.gMundoFisico, direita,
		BodyType.StaticBody, this.FIXTURE_NAVE);
	this.gScene.getBottomLayer().addEntity(direita);

	Shape baixo = new Rectangle(0, this.CAMERA_ALTURA - 50,
		this.CAMERA_LARGURA, 2);
	PhysicsFactory.createBoxBody(this.gMundoFisico, baixo,
		BodyType.StaticBody, this.FIXTURE_NAVE);
	this.gScene.getBottomLayer().addEntity(baixo);

	this.createMedidores();
	// this.gThreadGame = new ThreadGame(1000, 1, this);
	// this.gThreadGame.start();
	// Trecho de testes para o App.

	this.getNave().setSprite(new AnimatedSprite(0, 0, this.gRTextureNave));
	this.getNave().setBody(
		PhysicsFactory.createCircleBody(this.gMundoFisico, this
			.getNave().getSprite(), BodyType.DynamicBody,
			this.FIXTURE_NAVE, PIXEL_TO_METER_RATIO_DEFAULT));
	this.gMundoFisico.registerPhysicsConnector(new PhysicsConnector(this
		.getNave().getSprite(), this.getNave().getBody()));
	this.gScene.getLayer(1).addEntity(this.getNave().getSprite());

	this.gScene.registerUpdateHandler(new IUpdateHandler() {
	    @Override
	    public void reset() {
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
	this.gDados = new DadosSputvich(this);
	this.gTabelaConfiguracao = new ConfiguracaoDao(this.gDados);
    }

    private void createMedidores() {
	Sprite Velocimetro = new Sprite(this.CAMERA_LARGURA - 128,
		this.CAMERA_ALTURA - 128, this.gVelocimetroTextureRegion);
	Velocimetro.setScale((float) 1.3);
	this.gScene.getLayer(1).addEntity(Velocimetro);

	this.marcaVelocidade = new Sprite(this.CAMERA_LARGURA - 128,
		this.CAMERA_ALTURA - 128, this.gPonteiroTextureRegion);
	this.gScene.getLayer(2).addEntity(this.marcaVelocidade);
	this.marcaVelocidade.setScale((float) 1.3);

	this.marcaAngulo = new Sprite(this.CAMERA_LARGURA - 128,
		this.CAMERA_ALTURA - 128, this.gMedidorAnguloTextureRegion);
	this.gScene.getLayer(2).addEntity(this.marcaAngulo);
	this.marcaAngulo.setScale((float) 1.3);
    }

    @Override
    public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
	if (pKeyCode == KeyEvent.KEYCODE_MENU) {
	    if (this.gScene.getChildScene() == this.gMenuScene) {
		/* Remove the menu and reset it. */
		this.gMenuScene.back();
		this.gScene.setChildScene(this.gDigitalOnScreenControl);
	    } else 
		this.gScene.setChildScene(this.gMenuScene, false, true, true);
	    return true;
	}
	return super.onKeyDown(pKeyCode, pEvent);
    }

    private void createMenuScene() {
	Log.d(LOG, "Menu Sendo Iniciado");
	this.gMenuScene = new MenuScene(this.gCamera);

	final SpriteMenuItem resetMenuItem = new SpriteMenuItem(MENU_RESET,
		this.mMenuResetTextureRegion);
	resetMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
		GL10.GL_ONE_MINUS_SRC_ALPHA);
	this.gMenuScene.addMenuItem(resetMenuItem);

	final SpriteMenuItem quitMenuItem = new SpriteMenuItem(MENU_QUIT,
		this.mMenuQuitTextureRegion);
	quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
		GL10.GL_ONE_MINUS_SRC_ALPHA);
	this.gMenuScene.addMenuItem(quitMenuItem);

	final SpriteMenuItem sensorMenuItem = new SpriteMenuItem(
		MENU_CONTROLES, this.mMenuSensorTextureRegion);
	sensorMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
		GL10.GL_ONE_MINUS_SRC_ALPHA);
	this.gMenuScene.addMenuItem(sensorMenuItem);

	this.gMenuScene.buildAnimations();

	this.gMenuScene.setBackgroundEnabled(false);

	this.gMenuScene.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
	    float pMenuItemLocalX, float pMenuItemLocalY) {
	switch (pMenuItem.getID()) {
	case MENU_QUIT:
	    this.mEngine.stop();
	    this.onStop(); // Para fazer algumas coisas quando sair.
	    return true;
	case MENU_RESET:
	    this.mEngine.onPause();
	    return true;
	case MENU_CONTROLES:
	    this.mEngine.onPause();
	    showConfiguracaoControles();
	    this.gMenuScene.back();
	    return true;
	}
	return false;
    }

    private void showConfiguracaoControles() {
	final Dialog dialog = new Dialog(this);
	dialog.setContentView(R.layout.configuracao);
	dialog.setTitle("Configuração");
	dialog.show();
	Button vCancelar = (Button) dialog
		.findViewById(R.configuracao.cancelar);
	Button vConfirmar = (Button) dialog
		.findViewById(R.configuracao.confirmar);

	vCancelar.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		dialog.cancel();
	    }
	});

	vConfirmar.setOnClickListener(new OnClickListener() {
	    RadioGroup vRadioGrupo = (RadioGroup) dialog
		    .findViewById(R.configuracao.radioGroup);
	    BaseJogo janela;
	    int vTipo;

	    @Override
	    public void onClick(View v) {
		this.janela = (BaseJogo) dialog.getOwnerActivity();
		switch (this.vRadioGrupo.getCheckedRadioButtonId()) {
		case R.configuracao.porArena:
		    this.vTipo = 0;
		    break;
		case R.configuracao.proporcional:
		    this.vTipo = 1;
		    break;
		case R.configuracao.acelerometro:
		    this.vTipo = 2;
		    break;
		}
		//this.janela.setTipoControle(this.vTipo);
	    }
	});
    }

    public void setTipoControle(int vTipo) {
	switch (vTipo) {
	case 0:
	    this.gControlePorArea = true;
	    this.gControlePorPosicaoNave = false;
	    this.gControleAcelerometro = false;
	    this.gSensorManager.unregisterListener(this);
	    this.gScene.setOnSceneTouchListener(this);
	    break;
	case 1:
	    this.gControlePorArea = false;
	    this.gControlePorPosicaoNave = true;
	    this.gControleAcelerometro = false;
	    this.gSensorManager.unregisterListener(this);
	    this.gScene.setOnSceneTouchListener(this);
	    break;
	case 2:
	    this.gControlePorArea = false;
	    this.gControlePorPosicaoNave = false;
	    this.gControleAcelerometro = true;
	    this.gScene.setOnSceneTouchListener(null);
	    this.gSensorManager.registerListener(this, this.gSensorManager
		    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		    this.gSensorManager.SENSOR_DELAY_GAME);
	    break;
	}
    }

    private void createPontuacao() {
	this.gPlacar = new Sprite(0, 0, this.gPlacarTextureRegion);
	this.gScene.getBottomLayer().addEntity(this.gPlacar);
	final ChangeableText gravidade = new ChangeableText(0, 0, this.gFont,
		"Gravidade da Lua");
	this.gScene.getBottomLayer().addEntity(gravidade);
	this.gCombustivel = new ChangeableText(
		(float) (this.CAMERA_LARGURA / 1.75), 0, this.gFont,
		"Pontua��o do jogo");
	this.gScene.getBottomLayer().addEntity(this.gCombustivel);
	final ChangeableText recorde = new ChangeableText(
		(float) (this.CAMERA_LARGURA / 1.75), 20, this.gFont,
		"Recorde Do jogo");
	this.gScene.getBottomLayer().addEntity(recorde);
	final ChangeableText tempo = new ChangeableText(
		this.CAMERA_LARGURA / 2, 0, this.gFont, "TP", "XXXXXX".length());
	this.gScene.getBottomLayer().addEntity(tempo);
    }

    public void sistemaDeInteracao() {
	this.marcaAngulo.setRotation((float) Math.toDegrees(this.getNave()
		.getBody().getAngle()));
	this.marcaVelocidade.setRotation(Math.abs(this.getNave().getBody()
		.getLinearVelocity().y * 15) - 43);

	if (this.gTouchAbilitado) {
	    if (this.gTouchDown) {
		if (this.gControlePorArea)
		    controlaPorArea(this.gTouchX, this.gTouchY);
		else if (this.gControlePorPosicaoNave)
		    controlaPorPosicaoNave(this.getNave().getSprite(),
			    this.gTouchX, this.gTouchY);
	    } else
		this.getNave().controlaBodyNave(CONTROL_NULL);
	}
    }

    private DigitalOnScreenControl addControles() {
	Log.d(LOG, "Controles sendo inciada");
	DigitalOnScreenControl controle = new DigitalOnScreenControl(
		(this.CAMERA_LARGURA - this.gOnScreenControlBaseTextureRegion
			.getWidth()) / 2,
		(this.CAMERA_ALTURA - this.gOnScreenControlBaseTextureRegion
			.getHeight()), this.gCamera,
		this.gOnScreenControlBaseTextureRegion,
		this.gOnScreenControlKnobTextureRegion, 0.1f,
		new IOnScreenControlListener() {

		    @Override
		    public void onControlChange(
			    final BaseOnScreenControl pBaseOnScreenControl,
			    final float pValueX, final float pValueY) {
			if ((pValueX != 0.0) || (pValueY != 0.0)) {
			    if (pValueY < 0.0f)
				getNave().controlaBodyNave(CONTROL_CIMA);
			    if (pValueY > 0.0f)
				getNave().controlaBodyNave(CONTROL_BAIXO);
			    if (pValueX < 0.0f)
				getNave().controlaBodyNave(CONTROL_ESQUERDA);
			    if (pValueX > 0.0f)
				getNave().controlaBodyNave(CONTROL_DIREITA);
			}
			getNave().controlaBodyNave(CONTROL_NULL);
		    }
		});
	controle.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA,
		GL10.GL_ONE_MINUS_SRC_ALPHA);
	controle.getControlBase().setAlpha(0.5f);
	controle.getControlBase().setScaleCenter(0, 128);
	controle.getControlBase().setScale(1.25f);
	controle.getControlKnob().setScale(1.25f);
	controle.refreshControlKnobPosition();

	return controle;
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
			getNave().controlaBodyNave(CONTROL_DIREITA);
		    else if (x > this.cControleX - 1)
			getNave().controlaBodyNave(CONTROL_ESQUERDA);

		    if (z < this.cControleZ + 1)
			getNave().controlaBodyNave(CONTROL_CIMA);
		    else if (z > this.cControleZ - 1)
			getNave().controlaBodyNave(CONTROL_BAIXO);
		} else if (this.cControleY <= 6) {
		    if (x < this.cControleX + 1)
			getNave().controlaBodyNave(CONTROL_DIREITA);
		    else if (x > this.cControleX - 1)
			getNave().controlaBodyNave(CONTROL_ESQUERDA);

		    if (y < this.cControleY + 1)
			getNave().controlaBodyNave(CONTROL_CIMA);
		    else if (y < this.cControleY - 1)
			getNave().controlaBodyNave(CONTROL_BAIXO);
		}
		getNave().controlaBodyNave(CONTROL_NULL);
		break;

	    }
	}
    }

    private void SetVarControles(SensorEvent event) {
	this.cControleZ = (int) event.values[2];
	this.cControleY = (int) event.values[1];
	this.cControleX = (int) event.values[0];
	Log.d(LOG, " X: " + this.cControleX + " Y: " + this.cControleY + " Z: "
		+ this.cControleZ);
	this.cControlerSetado = true;
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
	switch (pSceneTouchEvent.getAction()) {
	case TouchEvent.ACTION_DOWN:
	    this.gTouchX = pSceneTouchEvent.getX();
	    this.gTouchY = pSceneTouchEvent.getY();
	    this.gTouchDown = true;
	    Log.d(LOG, "Down");
	    return true;
	case TouchEvent.ACTION_MOVE:
	    this.gTouchX = pSceneTouchEvent.getX();
	    this.gTouchY = pSceneTouchEvent.getY();
	    return true;
	case TouchEvent.ACTION_UP:
	    Log.d(LOG, "Up");
	    this.gTouchDown = false;
	    return true;
	}
	return false;
    }

    public void controlaPorArea(float pX, float pY) {
	if (pX == 0 && pY == 0)
	    onControleTouch(CONTROL_NULL, pX, pY);
	else if (pY < this.CAMERA_ALTURA * 0.33)
	    onControleTouch(CONTROL_CIMA, pX, pY);
	else if (pY > this.CAMERA_ALTURA * 0.66)
	    onControleTouch(CONTROL_BAIXO, pX, pY);
	else {
	    if (pX < this.CAMERA_LARGURA / 2)
		onControleTouch(CONTROL_ESQUERDA, pX, pY);
	    else if (pX > this.CAMERA_LARGURA / 2)
		onControleTouch(CONTROL_DIREITA, pX, pY);
	}
    }

    public void controlaPorPosicaoNave(AnimatedSprite pSprite, float pX,
	    float pY) {
	if (pX == 0 && pY == 0)
	    onControleTouch(CONTROL_NULL, pX, pY);
	else {
	    if (pY < pSprite.getY() - pSprite.getWidth())
		onControleTouch(CONTROL_CIMA, pX, pY);
	    else if (pY > pSprite.getY() + pSprite.getWidth())
		onControleTouch(CONTROL_BAIXO, pX, pY);
	    if (pX < pSprite.getX() - pSprite.getHeight())
		onControleTouch(CONTROL_ESQUERDA, pX, pY);
	    else if (pX > pSprite.getX() + pSprite.getHeight())
		onControleTouch(CONTROL_DIREITA, pX, pY);

	}
    }

    public void onControleTouch(int pDirecao, float pTouchX, float pTouchY) {
	switch (pDirecao) {
	case CONTROL_CIMA:
	    getNave().controlaBodyNave(CONTROL_CIMA);
	    getNave().controlaBodyNave(CONTROL_NULL);
	    break;
	case CONTROL_BAIXO:
	    getNave().controlaBodyNave(CONTROL_BAIXO);
	    getNave().controlaBodyNave(CONTROL_NULL);
	    break;
	case CONTROL_DIREITA:
	    getNave().controlaBodyNave(CONTROL_DIREITA);
	    break;
	case CONTROL_ESQUERDA:
	    getNave().controlaBodyNave(CONTROL_ESQUERDA);
	    break;
	default:
	    getNave().controlaBodyNave(CONTROL_NULL);
	    break;
	}
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	// TODO Auto-generated method stub
    }

    public AbstractNave getNave() {
	return this.gNave;
    }

    public void setNave(AbstractNave gNave) {
	this.gNave = gNave;
    }

}
