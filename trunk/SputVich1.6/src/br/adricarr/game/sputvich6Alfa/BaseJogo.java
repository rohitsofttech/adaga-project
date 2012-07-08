/* 
 * @Autor: Adriano Cássio
 * @Classe: Base Jogo
 * @Descrição: Base para os senarios e toda sua interecação
 *  
 *  
 *  
 *  
 *  */





package br.adricarr.game.sputvich6Alfa;
import static br.adricarr.game.sputvich6Alfa.intefaces.Constates.*;
import static org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
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
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import br.adricarr.game.sputvich6Alfa.entity.Nave;
import br.adricarr.game.sputvich6Alfa.intefaces.IBaseGame;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;



public class BaseJogo extends BaseGameActivity implements IBaseGame,  IOnMenuItemClickListener, SensorEventListener,
	IOnAreaTouchListener{
	
	protected static final String LOG = "SPUTVICH";
	protected  int CAMERA_WIDTH = 600;
	protected  int CAMERA_HEIGHT = 480;
	protected Camera gCamera;
	protected PhysicsWorld gMundoFisico;
	protected ThreadGame gThreadGame;
	protected Scene gScene;
	protected MenuScene gMenuScene;
	protected final FixtureDef FIXTURE_NAVE = PhysicsFactory.createFixtureDef(
			0.0f, .0f, 0.0f);
	protected static final Vector2 gVetor_Gravidade = new Vector2(0,
			SensorManager.GRAVITY_MOON);
	protected static final int MENU_RESET = 0;
	protected static final int MENU_QUIT = MENU_RESET + 1;
	protected static final int MENU_SENSOR = MENU_QUIT + 1;
	
	protected int cControleX;
	protected int cControleY;
	protected int cControleZ;
	protected boolean cControlerSetado = false;
	protected boolean gToqueArea = true;
	protected boolean gToqueRelacional = false;
	
	protected Nave gNave;
	
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

	
	
	public Engine onLoadEngine() {
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		CAMERA_WIDTH = display.getWidth();  
		CAMERA_HEIGHT = display.getHeight();
		this.gCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		Engine engine =  new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.gCamera));
		return engine;
	}

	public void onLoadResources() {
		
		this.gMenuTexture = new Texture(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuResetTextureRegion = TextureRegionFactory.createFromAsset(
				this.gMenuTexture, this, "base/imagens/menu_reset.png", 0, 0);
		this.mMenuQuitTextureRegion = TextureRegionFactory.createFromAsset(
				this.gMenuTexture, this, "base/imagens/menu_quit.png", 0, 50);
		this.mMenuSensorTextureRegion = TextureRegionFactory.createFromAsset(
				this.gMenuTexture, this, "base/imagens/menu_ habi_sensor.png", 0, 100);
		
		this.gTextureFont = new Texture(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gFont = new Font(this.gTextureFont, Typeface.create(
				Typeface.DEFAULT, Typeface.BOLD), 16, true, Color.BLACK);
		
		this.gPlacarTexture = new Texture(512, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gPlacarTextureRegion = TextureRegionFactory.createFromAsset(
				this.gPlacarTexture, this, "base/imagens/placar.png", 0, 0);

		
		this.gMedidorAnguloTexture = new Texture(128,128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gMedidorAnguloTextureRegion = TextureRegionFactory.createFromAsset(
				this.gMedidorAnguloTexture, this, "senarios/imagens/medidorAngulo.png", 0, 0);	
		
		this.gVelocimetroTexture = new Texture(128,128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gVelocimetroTextureRegion = TextureRegionFactory.createFromAsset(
				this.gVelocimetroTexture, this, "senarios/imagens/velocimetro.png", 0, 0);
		
		this.gPonteiroTexture = new Texture(128,128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gPonteiroTextureRegion = TextureRegionFactory.createFromAsset(
				this.gPonteiroTexture, this, "senarios/imagens/ponteiro2.png", 0, 0);
	
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
		
		
		
		this.gTextureTeste = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gRTextureNave = TextureRegionFactory.createTiledFromAsset(this.gTextureTeste,
				this, "nave/imagens/nave_tiled.png", 0, 0, 2, 2);
		
		this.mEngine.getTextureManager().loadTextures(this.gMenuTexture, this.gTextureFont,
				this.gPlacarTexture, this.gTextureTeste, this.gVelocimetroTexture, this.gOnScreenControlTexture,  
				this.gMedidorAnguloTexture, this.gPonteiroTexture, this.gFundoTexture);
		this.mEngine.getFontManager().loadFont(gFont);
	}

	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.gScene = new Scene(3);
		
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(
				0, 0, 0, 5);
		autoParallaxBackground.addParallaxEntity(new ParallaxEntity(0,
				new Sprite(0, 0, this.gFundoTextureRegion)));
		gScene.setBackground(autoParallaxBackground);
		
		this.gMundoFisico = new PhysicsWorld(gVetor_Gravidade, false);
		gScene.registerUpdateHandler(this.gMundoFisico);
		this.createMenuScene();
		this.createPontuacao();
		this.gNave = new Nave();
		
		//this.gDigitalOnScreenControl = this.addControles();
		//gScene.setChildScene(this.gDigitalOnScreenControl);
		
		Shape esquerda = new Rectangle(0, 0, 2, CAMERA_HEIGHT);

		PhysicsFactory.createBoxBody(this.gMundoFisico, esquerda,
				BodyType.StaticBody, FIXTURE_NAVE);
		this.gScene.getBottomLayer().addEntity(esquerda);

		Shape direita = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);
		PhysicsFactory.createBoxBody(this.gMundoFisico, direita,
				BodyType.StaticBody, FIXTURE_NAVE);
		this.gScene.getBottomLayer().addEntity(direita);
		
		Shape baixo = new Rectangle(0, CAMERA_HEIGHT - 50, CAMERA_WIDTH, 2);
		PhysicsFactory.createBoxBody(this.gMundoFisico, baixo,
				BodyType.StaticBody, FIXTURE_NAVE);
		this.gScene.getBottomLayer().addEntity(baixo);

		this.createMedidores();
		//this.gThreadGame = new ThreadGame(1000, 1, this);
		//this.gThreadGame.start();
		//Trecho de testes para o App.
		
		this.gNave.setSprite(new AnimatedSprite(0,0, this.gRTextureNave));
		this.gNave.setBody(PhysicsFactory.createCircleBody(this.gMundoFisico,
				gNave.getSprite(), BodyType.DynamicBody, FIXTURE_NAVE,
				PIXEL_TO_METER_RATIO_DEFAULT));	
		this.gMundoFisico.registerPhysicsConnector(
				new PhysicsConnector(gNave.getSprite(), gNave.getBody()));		
		this.gScene.getLayer(1).addEntity(gNave.getSprite());
		
		
		
		gScene.registerUpdateHandler(new IUpdateHandler() {
			public void reset() {
			}

			public void onUpdate(float pSecondsElapsed) {
				sistemaDeInteracao();
			}
		});
		
		return gScene;
	}
		
	private void createMedidores() {
		Sprite Velocimetro = new Sprite(CAMERA_WIDTH -128, CAMERA_HEIGHT-128, this.gVelocimetroTextureRegion);
		Velocimetro.setScale((float) 1.3);
		this.gScene.getLayer(1).addEntity(Velocimetro);
		
	    marcaVelocidade = new Sprite(CAMERA_WIDTH -128, CAMERA_HEIGHT-128, this.gPonteiroTextureRegion);
		this.gScene.getLayer(2).addEntity(marcaVelocidade);
		marcaVelocidade.setScale((float) 1.3);	

	    marcaAngulo = new Sprite(CAMERA_WIDTH -128, CAMERA_HEIGHT-128, this.gMedidorAnguloTextureRegion);
		this.gScene.getLayer(2).addEntity(marcaAngulo);
		marcaAngulo.setScale((float) 1.3);			
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
		
		final SpriteMenuItem sensorMenuItem = new SpriteMenuItem(MENU_SENSOR,
				this.mMenuSensorTextureRegion);
		sensorMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.gMenuScene.addMenuItem(sensorMenuItem);

		this.gMenuScene.buildAnimations();

		this.gMenuScene.setBackgroundEnabled(false);

		this.gMenuScene.setOnMenuItemClickListener(this);

	}
	
	private void createPontuacao() {
		this.gPlacar = new Sprite(0, 0, this.gPlacarTextureRegion);
		gScene.getBottomLayer().addEntity(this.gPlacar);
		final ChangeableText gravidade = new ChangeableText(0, 0, this.gFont,
				"Gravidade da Lua");
		gScene.getBottomLayer().addEntity(gravidade);
		this.gCombustivel = new ChangeableText((float) (CAMERA_WIDTH / 1.75),
				0, this.gFont, "Pontua��o do jogo");
		gScene.getBottomLayer().addEntity(this.gCombustivel);
		final ChangeableText recorde = new ChangeableText(
				(float) (CAMERA_WIDTH / 1.75), 20, this.gFont,
				"Recorde Do jogo");
		gScene.getBottomLayer().addEntity(recorde);
		final ChangeableText tempo = new ChangeableText(
				(float) (CAMERA_WIDTH / 2), 0, this.gFont, "TP",
				"XXXXXX".length());
		gScene.getBottomLayer().addEntity(tempo);
	}
	
	
	public void sistemaDeInteracao() {
		marcaAngulo.setRotation((float) Math.toDegrees(this.gNave.getBody().getAngle()));
	    	marcaVelocidade.setRotation(Math.abs(this.gNave.getBody().getLinearVelocity().y*15)-43);
	}

	public void onLoadComplete() {	
	}
	
	private DigitalOnScreenControl addControles() {
		Log.d(LOG, "Controles sendo inciada");
		DigitalOnScreenControl controle = new DigitalOnScreenControl(
				(CAMERA_WIDTH - this.gOnScreenControlBaseTextureRegion
						.getWidth()) / 2,
				(CAMERA_HEIGHT - this.gOnScreenControlBaseTextureRegion
						.getHeight()), this.gCamera,
				this.gOnScreenControlBaseTextureRegion,
				this.gOnScreenControlKnobTextureRegion, 0.1f,
				new IOnScreenControlListener() {

					public void onControlChange(
							final BaseOnScreenControl pBaseOnScreenControl,
							final float pValueX, final float pValueY){
						if ((pValueX != 0.0) || (pValueY != 0.0)) {
							if (pValueY < 0.0f)
								gNave.controlaBodyNave(CONTROL_CIMA);
							if (pValueY > 0.0f)
								gNave.controlaBodyNave(CONTROL_BAIXO);
							if (pValueX < 0.0f)
								gNave.controlaBodyNave(CONTROL_ESQUERDA);
							if (pValueX > 0.0f)
								gNave.controlaBodyNave(CONTROL_DIREITA);
						}
						gNave.controlaBodyNave(CONTROL_NULL);
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

	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		
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
				if  (!cControlerSetado)
					SetVarControles(event);
				
				int z = (int) event.values[2];
				int y = (int) event.values[1];
				int x = (int) event.values[0];			
				
				if (cControleY > 6) 
				{
					if (x < cControleX+1)
					{
						gNave.controlaBodyNave(CONTROL_DIREITA);
					} else if (x > cControleX-1) 
					{
						gNave.controlaBodyNave(CONTROL_ESQUERDA);
					}
					
					if (z < cControleZ +1)
					{
						gNave.controlaBodyNave(CONTROL_CIMA);
					} else if (z > cControleZ -1) 
					{
						gNave.controlaBodyNave(CONTROL_BAIXO);
					}
				} else if (cControleY <= 6) 
				{
					if (x < cControleX+1)
					{
						gNave.controlaBodyNave(CONTROL_DIREITA);
					} else if (x > cControleX-1) 
					{
						gNave.controlaBodyNave(CONTROL_ESQUERDA);
					}
					
					if (y < cControleY +1)
					{
						gNave.controlaBodyNave(CONTROL_CIMA);
					} else if (y < cControleY -1) 
					{
						gNave.controlaBodyNave(CONTROL_BAIXO);
					}	
				}
				gNave.controlaBodyNave(CONTROL_NULL);
				break;
			
			}
		}
	}

	private void SetVarControles(SensorEvent event) {
		cControleZ = (int) event.values[2];
		cControleY = (int) event.values[1];
		cControleX = (int) event.values[0];
		Log.d(LOG, " X: " + cControleX +  " Y: "+ cControleY +  " Z: "+ cControleZ);
		cControlerSetado = true;
	}

		//Probelam aqui
	
	private void controlaPorArea(float pX, float pY){
		Log.d(LOG, "Toque sensor: X: " + pX + " Y: "+ pY);
		if (pY < CAMERA_WIDTH * 0.33)
			gNave.controlaBodyNave(CONTROL_CIMA);
		else if (pY > CAMERA_WIDTH * 0.66) 
			gNave.controlaBodyNave(CONTROL_BAIXO);
		else 
			if (pX < CAMERA_WIDTH /2)
				gNave.controlaBodyNave(CONTROL_ESQUERDA);
			else if (pX > CAMERA_WIDTH /2)
				gNave.controlaBodyNave(CONTROL_DIREITA);
		
	}

	private void controlaPropocional(float pX, float pY) {
		// TODO Auto-generated method stub	
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		Log.d(LOG, "Toque sensor: X: " + pTouchAreaLocalX + " Y: "+ pTouchAreaLocalY);
		if (pTouchAreaLocalY < CAMERA_WIDTH * 0.33)
			gNave.controlaBodyNave(CONTROL_CIMA);
		else if (pTouchAreaLocalY > CAMERA_WIDTH * 0.66) 
			gNave.controlaBodyNave(CONTROL_BAIXO);
		else 
			if (pTouchAreaLocalX < CAMERA_WIDTH /2)
				gNave.controlaBodyNave(CONTROL_ESQUERDA);
			else if (pTouchAreaLocalX > CAMERA_WIDTH /2)
				gNave.controlaBodyNave(CONTROL_DIREITA);
		return true;
	}

}


