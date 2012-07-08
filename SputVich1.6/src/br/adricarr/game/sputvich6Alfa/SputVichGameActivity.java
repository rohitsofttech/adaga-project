package br.adricarr.game.sputvich6Alfa;

import static org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import java.util.ArrayList;

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
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.emitter.IParticleEmitter;
import org.anddev.andengine.entity.particle.modifier.AccelerationInitializer;
import org.anddev.andengine.entity.particle.modifier.RotationInitializer;
import org.anddev.andengine.entity.particle.modifier.ScaleModifier;
import org.anddev.andengine.entity.particle.modifier.VelocityInitializer;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.shape.modifier.PathModifier;
import org.anddev.andengine.entity.shape.modifier.ease.IEaseFunction;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Path;
import org.anddev.andengine.util.modifier.IModifier;

import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;
import br.adricarr.game.sputvich6Alfa.entity.SpriteNave;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class SputVichGameActivity extends BaseGameActivity implements
		IOnMenuItemClickListener, SensorEventListener {

	// ===========================================================
	// Constantes
	// ===========================================================
	private static final String LOG = "SPUTVICH";

	private final FixtureDef FIXTURE_NAVE = PhysicsFactory.createFixtureDef(
			0.0f, .0f, 0.0f);
	private static final Vector2 gVetor_Gravidade = new Vector2(0,
			SensorManager.GRAVITY_MOON);
	protected static final int MENU_RESET = 0;
	protected static final int MENU_QUIT = MENU_RESET + 1;
	protected static final int MENU_SENSOR = MENU_QUIT + 1;
	
	private enum CONTROLES {cCima, cBaixo, cDireita, cEsquerda, cSemControle};
	protected int cControleX;
	protected int cControleY;
	protected int cControleZ;
	private boolean cControlerSetado = false;

	// ===========================================================
	// Variaveis Globais
	// ===========================================================
	private Camera gCamera;
	public PhysicsWorld gMundoFisico;
	private ThreadGame gThreadGame;
	protected Scene gScene;
	protected MenuScene gMenuScene;
	private SensorManager sensorManager;

	
	//Texturas e Imagems
	private SpriteNave gNave;
	private Font gFont;
	private Sprite gPlacar;
	private Sprite gFolha;
	private Shape marcaAngulo;
	private Shape marcaVelocidade;
	
	
	private Texture gOnScreenControlTexture;
	private Texture gTextureNave;
	private Texture gTextureFlag;
	private Texture gMenuTexture;
	private Texture gTextureFont;
	private Texture gMeteoroTexture;
	private Texture gPlacarTexture;
	private Texture gFundoTexture;
	public Texture gVentoTexture;
	private Texture gVelocimetroTexture;
	private Texture gPonteiroTexture;
	private Texture gMedidorAnguloTexture;

	private TextureRegion gOnScreenControlBaseTextureRegion;
	private TextureRegion gOnScreenControlKnobTextureRegion;
	private TextureRegion gMeteroroTextureRegion;
	private TiledTextureRegion gRegionTNave;
	public TextureRegion gVentoTextureRegion;
	private TextureRegion gRegionTFlag;
	private TextureRegion mMenuQuitTextureRegion;
	private TextureRegion mMenuResetTextureRegion;
	private TextureRegion gPlacarTextureRegion;
	private TextureRegion gFundoTextureRegion;
	private TextureRegion gVelocimetroTextureRegion;
	private TextureRegion gPonteiroTextureRegion;
	private TextureRegion gMedidorAnguloTextureRegion;

	private Body gNaveBody;
	private DigitalOnScreenControl gDigitalOnScreenControl;

	private int ControleDoControle = 0;
	private int NUMERO_FLAGS = 0;
	private ChangeableText gCombustivel;
	private ArrayList<PhysicsConnector> gListaConector = new ArrayList<PhysicsConnector>();
	private ArrayList<Shape> gListaChao = new ArrayList<Shape>();
	private TextureRegion mMenuSensorTextureRegion;
	private boolean sensorHabilitado = false;
	private ParticleSystem particleSystemMeteoro;

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ChangeableText getCombustivel() {
		return gCombustivel;
	}

	public void setCombustivel(ChangeableText pCombustivel) {
		this.gCombustivel = pCombustivel;
	}
	
	public boolean iscControlerSetado() {
		return cControlerSetado;
	}

	public void setcControlerSetado(boolean cControlerSetado) {
		this.cControlerSetado = cControlerSetado;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	private  int CAMERA_WIDTH;
	private  int CAMERA_HEIGHT;
	public Engine onLoadEngine() {
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		CAMERA_WIDTH = display.getWidth();  
		CAMERA_HEIGHT = display.getHeight();
		
		this.gCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.gCamera));
	}

	public void onLoadResources() {
		this.gTextureNave = new Texture(128, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gRegionTNave = TextureRegionFactory.createTiledFromAsset(
				gTextureNave, this, "nave/imagens/nave_tiled.png", 0, 0, 2, 2);

		this.gOnScreenControlTexture = new Texture(256, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gOnScreenControlBaseTextureRegion = TextureRegionFactory
				.createFromAsset(this.gOnScreenControlTexture, this,
						"base/imagens/onscreen_control_base.png", 0, 0);
		this.gOnScreenControlKnobTextureRegion = TextureRegionFactory
				.createFromAsset(this.gOnScreenControlTexture, this,
						"base/imagens/onscreen_control_knob.png", 128, 0);

		this.gMenuTexture = new Texture(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuResetTextureRegion = TextureRegionFactory.createFromAsset(
				this.gMenuTexture, this, "base/imagens/menu_reset.png", 0, 0);
		this.mMenuQuitTextureRegion = TextureRegionFactory.createFromAsset(
				this.gMenuTexture, this, "base/imagens/menu_quit.png", 0, 50);
		this.mMenuSensorTextureRegion = TextureRegionFactory.createFromAsset(
				this.gMenuTexture, this, "base/imagens/menu_ habi_sensor.png", 0, 100);

		this.gTextureFlag = new Texture(32, 32,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gRegionTFlag = TextureRegionFactory.createFromAsset(
				this.gTextureFlag, this, "senarios/imagens/flag.png", 0, 0);

		this.gTextureFont = new Texture(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gFont = new Font(this.gTextureFont, Typeface.create(
				Typeface.DEFAULT, Typeface.BOLD), 16, true, Color.BLACK);

		this.gPlacarTexture = new Texture(512, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gPlacarTextureRegion = TextureRegionFactory.createFromAsset(
				this.gPlacarTexture, this, "base/imagens/placar.png", 0, 0);
		
		this.gPonteiroTexture = new Texture(128,128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gPonteiroTextureRegion = TextureRegionFactory.createFromAsset(
				this.gPonteiroTexture, this, "senarios/imagens/ponteiro2.png", 0, 0);	
        
		this.gMedidorAnguloTexture = new Texture(128,128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gMedidorAnguloTextureRegion = TextureRegionFactory.createFromAsset(
				this.gMedidorAnguloTexture, this, "senarios/imagens/medidorAngulo.png", 0, 0);	
		
		this.gVelocimetroTexture = new Texture(128,128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gVelocimetroTextureRegion = TextureRegionFactory.createFromAsset(
				this.gVelocimetroTexture, this, "senarios/imagens/velocimetro.png", 0, 0);	
		
		this.gMeteoroTexture = new Texture(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gMeteroroTextureRegion = TextureRegionFactory.createFromAsset(
				this.gMeteoroTexture, this, "obstaculos/imagens/asteroide.png", 0, 0);

		this.gFundoTexture = new Texture(512, 1024, TextureOptions.DEFAULT);
		this.gFundoTextureRegion = TextureRegionFactory.createFromAsset(
				this.gFundoTexture, this, "senarios/imagens/fudo.png", 0, 0);
		this.gVentoTexture = new Texture(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gVentoTextureRegion = TextureRegionFactory.createFromAsset(
				this.gVentoTexture, this, "obstaculos/imagens/vento2.png", 0, 0);
		
		
		this.mEngine.getTextureManager().loadTextures(
				this.gOnScreenControlTexture, this.gTextureNave,
				this.gMenuTexture, this.gTextureFlag, this.gTextureFont,
				this.gPlacarTexture, this.gMeteoroTexture, this.gFundoTexture,
				this.gVentoTexture,this.gVelocimetroTexture,this.gPonteiroTexture,
				this.gMedidorAnguloTexture);
		this.mEngine.getFontManager().loadFont(gFont);
	}

	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		sensorManager = (SensorManager) this
				.getSystemService(this.SENSOR_SERVICE);		
		this.gScene = new Scene(3);
		this.addFundo();		
		this.gMundoFisico = new PhysicsWorld(gVetor_Gravidade, false);
		gScene.registerUpdateHandler(this.gMundoFisico);
		this.gDigitalOnScreenControl = this.addControles();
		gScene.setChildScene(this.gDigitalOnScreenControl);
		this.addChao();
		this.createMenuScene();
		this.createNave();
		this.createPontuacao();
		Sprite Velocimetro = new Sprite(CAMERA_WIDTH -128, CAMERA_HEIGHT-128, this.gVelocimetroTextureRegion);
		Velocimetro.setScale((float) 1.3);
		this.gScene.getLayer(1).addEntity(Velocimetro);
		
	    marcaVelocidade = new Sprite(CAMERA_WIDTH -128, CAMERA_HEIGHT-128, this.gPonteiroTextureRegion);
		this.gScene.getLayer(2).addEntity(marcaVelocidade);
		marcaVelocidade.setScale((float) 1.3);	

	    marcaAngulo = new Sprite(CAMERA_WIDTH -128, CAMERA_HEIGHT-128, this.gMedidorAnguloTextureRegion);
		this.gScene.getLayer(2).addEntity(marcaAngulo);
		marcaAngulo.setScale((float) 1.3);	
		
		criaMeteoro(10, 10, new Vector2(0, 0), new Vector2(0, 0));
		gFolha = new Sprite(-100,-100, this.gVentoTextureRegion);
		
		
		Shape esquerda = new Rectangle(0, 0, 2, CAMERA_HEIGHT);

		PhysicsFactory.createBoxBody(this.gMundoFisico, esquerda,
				BodyType.StaticBody, FIXTURE_NAVE);
		this.gScene.getBottomLayer().addEntity(esquerda);

		Shape direita = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);
		PhysicsFactory.createBoxBody(this.gMundoFisico, direita,
				BodyType.StaticBody, FIXTURE_NAVE);
		this.gScene.getBottomLayer().addEntity(direita);
		
        
		this.gThreadGame = new ThreadGame(1000, 1, this);
		this.gThreadGame.start();
		
		gScene.registerUpdateHandler(new IUpdateHandler() {
			public void reset() {}

			public void onUpdate(float pSecondsElapsed) {
				sistemaDeInteracao();
			}
		});		
        particulaMeteoro();
		
		gFolha = new Sprite(-100,-100, this.gVentoTextureRegion);
		gScene.getLayer(1).addEntity(gFolha);
		return gScene;
	}

	
	private void addChao() {
		this.gListaChao.add(addLinha(0f, 580f, 16f, 580f));
		this.gListaChao.add(addLinha(16f, 580f, 97f, 539f));
		this.gListaChao.add(addLinha(97f, 539f, 145f, 541f));
		this.gListaChao.add(addLinha(145f, 541f, 155f, 600f));
		this.gListaChao.add(addLinha(155f, 600f, 189f, 616f));
		this.gListaChao.add(addLinha(189f, 616f, 200f, 568f));
		this.gListaChao.add(addLinha(200f, 568f, 211f, 550f));
		this.gListaChao.add(addLinha(211f, 550f, 237f, 552f));
		this.gListaChao.add(addLinha(237f, 552f, 268f, 605f));
		this.gListaChao.add(addLinha(268f, 605f, 288f, 559f));
		this.gListaChao.add(addLinha(288f, 559f, 365f, 558f));
		this.gListaChao.add(addLinha(365f, 558f, 367f, 614f));
		this.gListaChao.add(addLinha(367f, 614f, 416f, 612f));
		this.gListaChao.add(addLinha(416f, 612f, 395f, 526f));
		this.gListaChao.add(addLinha(395f, 526f, 442f, 497f));
		this.gListaChao.add(addLinha(442f, 497f, 477f, 392f));
		this.gListaChao.add(addLinha(440f, 490f, 479f, 393f));
		
		for (int i = 0; i <= gListaChao.size() - 1; i++) {
			Shape linha = gListaChao.get( i);
			Body bodyLinha = PhysicsFactory.createBoxBody(gMundoFisico, linha, BodyType.StaticBody, FIXTURE_NAVE);
			PhysicsConnector conector = new PhysicsConnector(linha, bodyLinha,
					false, false, false, false);
			gMundoFisico.registerPhysicsConnector(conector);
			gScene.getTopLayer().addEntity(linha);
		}
	}

	private void addFundo() {
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(
				0, 0, 0, 5);
		autoParallaxBackground.addParallaxEntity(new ParallaxEntity(0,
				new Sprite(0, 0, this.gFundoTextureRegion)));
		gScene.setBackground(autoParallaxBackground);
	}

	protected void sistemaDeInteracao() {
		if (estaBatendoNoChao(gNave)) {
			if (NUMERO_FLAGS <= 0) {
				colocaFlags(gNave.getX(), gNave.getY()
						+ (gNave.getHeight() / 2));
				NUMERO_FLAGS++;
				gThreadGame.setAtivado(false);
			}
		}
		removeMeteoro();
	}

	public void criaMeteoro(final int pX, final int pY, final Vector2 pImpulso,
			final Vector2 pPonto) {
		for (int i = 0; i < 10; i++) {
			Sprite meteoro = new Sprite(pX, pY, this.gMeteroroTextureRegion);
			Body bodyMeteoro = PhysicsFactory.createCircleBody(
					this.gMundoFisico, meteoro, BodyType.DynamicBody,
					this.FIXTURE_NAVE);
			PhysicsConnector conector = new PhysicsConnector(meteoro,
					bodyMeteoro);
			this.gMundoFisico.registerPhysicsConnector(conector);
			gScene.getBottomLayer().addEntity(meteoro);
			bodyMeteoro.applyLinearImpulse(pImpulso, pPonto);
			this.gListaConector.add(conector);
			bodyMeteoro.setActive(false);
		}
	}
	
	
    public void particulaMeteoro(){  	
    	
    	gScene.getLayer(0).removeEntity(particleSystemMeteoro);
           IParticleEmitter IP=new IParticleEmitter() {
			
			public void reset() {
				// TODO Auto-generated method stub				
			}			
			
			public void onUpdate(float pSecondsElapsed) {
				// TODO Auto-generated method stub				
			}			
			
			public void getPositionOffset(float[] pOffset) {
				pOffset[0]=-200;
				pOffset[1]=-200;				
			}
		};
		 particleSystemMeteoro = new ParticleSystem(IP,4f,6f,20,this.gMeteroroTextureRegion);
		//particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

		particleSystemMeteoro.addParticleInitializer(new VelocityInitializer(15, 150, 150, 10));
		particleSystemMeteoro.addParticleInitializer(new AccelerationInitializer(1, 1));
		particleSystemMeteoro.addParticleInitializer(new RotationInitializer(1.0f, 360.0f));
		//particleSystem.addParticleInitializer(new ColorInitializer(1.0f, 1.0f, 0.0f));

		particleSystemMeteoro.addParticleModifier(new ScaleModifier(1f, 0.2f, 0, 5));
		//particleSystem.addParticleModifier(new ExpireModifier(6.5f));
		//particleSystem.addParticleModifier(new ColorModifier(1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 2.5f, 5.5f));
		//particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 2.5f, 6.5f));
		gScene.getLayer(0).addEntity(particleSystemMeteoro);
    }
	
	
	public void criaVento(int mdirecao){    	    
		final float width=CAMERA_WIDTH/5;
		final float height=CAMERA_HEIGHT/5;	
		if(mdirecao%2==0){
	    	  
		final Path path = new Path(33)
		.to(-1.30019f*width, +2.09444f*height)
		.to(-0.10019f*width, +2.09444f*height)
		.to(-0.00019f*width, +2.09444f*height)
		.to(+0.35346f*width, +2.12659f*height)
		.to(+0.62674f*width, +2.14267f*height)
		.to(+0.98039f*width, +2.14267f*height)
		.to(+1.38227f*width, +2.11052f*height)
		.to(+1.78415f*width, +2.04622f*height)
		.to(+2.07350f*width, +1.96584f*height)
		.to(+2.28248f*width, +1.83724f*height)
		.to(+2.49145f*width, +1.61219f*height)
		.to(+2.60398f*width, +1.38714f*height)
		.to(+2.57183f*width, +1.14601f*height)
		.to(+2.39500f*width, +1.00133f*height)
		.to(+2.23425f*width, +0.92096f*height)
		.to(+2.02527f*width, +0.95311f*height)
		.to(+1.88060f*width, +1.09778f*height)
		.to(+1.81630f*width, +1.30676f*height)
		.to(+1.83237f*width, +1.48359f*height)
		.to(+1.88060f*width, +1.66041f*height)
		.to(+2.02527f*width, +1.83724f*height)
		.to(+2.23425f*width, +1.94977f*height)
		.to(+2.45930f*width, +1.98192f*height)
		.to(+2.70043f*width, +2.03014f*height)
		.to(+2.94156f*width, +2.03014f*height)
		.to(+3.27913f*width, +2.03014f*height)
		.to(+3.64886f*width, +1.99799f*height)
		.to(+3.97036f*width, +1.96584f*height)
		.to(+4.22756f*width, +1.90154f*height)
		.to(+4.58122f*width, +1.80509f*height)
		.to(+4.82234f*width, +1.66041f*height)
		.to(+5.04739f*width, +1.48359f*height)	
		.to(+6.04739f*width, +1.48359f*height)
				;
		gFolha.addShapeModifier(new PathModifier(1.3f, path,new IEaseFunction() {					
		
			public float getPercentageDone(float pSecondsElapsed, float pDuration,
					float pMinValue, float pMaxValue) {
				gNaveBody.applyLinearImpulse(new Vector2(0.07f,0), new Vector2(0,0));
				//Log.d(LOG, "Vento ");
				return 0;
			}
		}));
		}
		else {
		final Path path2 = new Path(32)	
		.to(+6.04739f*width, +1.48359f*height)
		.to(+5.04739f*width, +1.48359f*height)
		.to(+4.82234f*width, +1.66041f*height)
		.to(+4.58122f*width, +1.80509f*height)
		.to(+4.22756f*width, +1.90154f*height)
		.to(+3.97036f*width, +1.96584f*height)
		.to(+3.64886f*width, +1.99799f*height)
		.to(+3.27913f*width, +2.03014f*height)
		.to(+2.94156f*width, +2.03014f*height)
		.to(+2.70043f*width, +2.03014f*height)
		.to(+2.45930f*width, +1.98192f*height)
		.to(+2.23425f*width, +1.94977f*height)
		.to(+2.02527f*width, +1.83724f*height)
		.to(+1.88060f*width, +1.66041f*height)
		.to(+1.83237f*width, +1.48359f*height)
		.to(+1.81630f*width, +1.30676f*height)
		.to(+1.88060f*width, +1.09778f*height)
		.to(+2.02527f*width, +0.95311f*height)
		.to(+2.23425f*width, +0.92096f*height)
		.to(+2.39500f*width, +1.00133f*height)
		.to(+2.57183f*width, +1.14601f*height)
		.to(+2.60398f*width, +1.38714f*height)
		.to(+2.49145f*width, +1.61219f*height)
		.to(+2.28248f*width, +1.83724f*height)
		.to(+2.07350f*width, +1.96584f*height)
		.to(+1.78415f*width, +2.04622f*height)
		.to(+1.38227f*width, +2.11052f*height)
		.to(+0.98039f*width, +2.14267f*height)
		.to(+0.62674f*width, +2.14267f*height)
		.to(+0.35346f*width, +2.12659f*height)		
		.to(-0.00019f*width, +2.09444f*height)		
		;
		 gFolha.addShapeModifier( new PathModifier(1.3f, path2,new IEaseFunction() {					
		
			public float getPercentageDone(float pSecondsElapsed, float pDuration,
					float pMinValue, float pMaxValue) {
				gNaveBody.applyLinearImpulse(new Vector2(-0.07f,0), new Vector2(0,0));
				//Log.d(LOG, "Vento es");
				return 0;
			}
		}));
		}
						
    }
	private boolean estaBatendoNoChao(final Shape pShape) {
		for (int i =0; i <= gListaChao.size() - 1; i++) {
			if (pShape.collidesWith(this.gListaChao.get(i))) {
				Rectangle r = (Rectangle) this.gListaChao.get(i);
				//Log.d(LOG, "Agulação igual a: " + r.getRotation());
				return true;
			}
		}
		return false;
	}

	// metodo verifica se o meteoro colidiu, desativa o corpo fisico e move o
	// meteoro de lugar
	public synchronized void removeMeteoro() {
		for (int i = this.gListaConector.size() - 1; i >= 0; i--) {
			PhysicsConnector conector = this.gListaConector.get(i);
			if (estaBatendoNoChao(conector.getShape())) {
				this.gListaConector.get(i).getBody()
						.setTransform(new Vector2(10f, 10f), 0);
				this.gListaConector.get(i).getBody().setActive(false);
			}
		}
	}

	// metodo move meteoro da um lugar de inicio, ativa o corpo fisico e aplica
	// uma for�a
	public void moveMeteoro(int i) {
		if (this.gListaConector.size() > i)

			this.gListaConector
					.get(i)
					.getBody()
					.setTransform(new Vector2((float) (Math.random() * 10), 0),
							0);// posi��o
		this.gListaConector.get(i).getBody().setActive(true);// ativa
		this.gListaConector
				.get(i)
				.getBody()
				.applyLinearImpulse(
						new Vector2((float) (Math.random() * 5), 1),
						new Vector2(0, 0)); // aplica for�a
	}

	private void createNave() {
		this.gNave = new SpriteNave(
				(CAMERA_WIDTH - this.gTextureNave.getWidth()) / 2,
				0,
				this.gRegionTNave);
		float width = gNave.getWidthScaled() / PIXEL_TO_METER_RATIO_DEFAULT;
		float height = gNave.getHeightScaled() / PIXEL_TO_METER_RATIO_DEFAULT;
		Vector2[] lista = { new Vector2(-0.48345f * width, -0.01126f * height),
				new Vector2(-0.48781f * width, -0.18133f * height) };

		Log.d(LOG, "Nave sendo inciada");

		this.gNaveBody = PhysicsFactory.createCircleBody(this.gMundoFisico,
				gNave, BodyType.DynamicBody, FIXTURE_NAVE,
				PIXEL_TO_METER_RATIO_DEFAULT);
		this.gMundoFisico.registerPhysicsConnector(new PhysicsConnector(
				this.gNave, this.gNaveBody));
		gScene.getBottomLayer().addEntity(gNave);
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

	protected void colocaFlags(float x, float y) {
		Sprite flag = new Sprite(x, y, this.gRegionTFlag);
		gScene.getBottomLayer().addEntity(flag);
		Log.d(LOG, "Flag adicionando no X: " + x + " e no Y: " + y);
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
								controlaNave(CONTROLES.cCima);
							if (pValueY > 0.0f)
								controlaNave(CONTROLES.cBaixo);
							if (pValueX < 0.0f)
								controlaNave(CONTROLES.cEsquerda);
							if (pValueX > 0.0f)
								controlaNave(CONTROLES.cDireita);
						}
						controlaNave(CONTROLES.cSemControle);
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
	
	public void onLoadComplete() {
	}

	
	protected synchronized void  controlaNave(CONTROLES pControle){
		float angulo;	
		
		//Log.d(LOG, "Agulação igual a: " +gNaveBody.getLinearVelocity());
		switch (pControle) {
		case cCima:
			ControleDoControle = 1; 
			Vector2 vetor = gNaveBody.getLinearVelocity();
			float aceleracao = ((-gVetor_Gravidade.y / 8) - (vetor.y / 10));
			gNaveBody.applyLinearImpulse(new Vector2(0.0f,
					aceleracao), new Vector2(0, 0));
			gThreadGame.setConsumo(3);
			break;
		case cBaixo:
			ControleDoControle = 2;
			gNaveBody.applyLinearImpulse(new Vector2(0.0f,
					0.15f), new Vector2(0, 0));
			gThreadGame.setConsumo(0);
			break;
		case cEsquerda:
			ControleDoControle = 3;
			gNaveBody.applyLinearImpulse(new Vector2(
					-0.05f, 0.0f), new Vector2(0, 0));
			gNaveBody.setAngularVelocity(-0.5f);
			angulo = gNaveBody.getAngle();
			if (-angulo > Math.PI / 4)
				gNaveBody.setAngularVelocity(0);
			gThreadGame.setConsumo(2);
			break;
		case cDireita:
			ControleDoControle = 4;
			angulo = gNaveBody.getAngle();
			gNaveBody.applyLinearImpulse(new Vector2(0.05f,
					0.0f), new Vector2(0, 0));
			gNaveBody.setAngularVelocity(.5f);
			if (angulo > Math.PI / 4)
				gNaveBody.setAngularVelocity(0);
			gThreadGame.setConsumo(2);
			break;
		case cSemControle:
			if ((ControleDoControle != 4)
					&& (ControleDoControle != 3)) {
				gNaveBody.setAngularVelocity(0);
				angulo = gNaveBody.getAngle();
				if (angulo > 0.01f)
					gNaveBody.setAngularVelocity(-0.3f);
				 else if (angulo < -0.01f)
					gNaveBody.setAngularVelocity(0.3f);
				 else 
					gNaveBody.setAngularVelocity(0);
			}
			if (ControleDoControle == 0)
				gThreadGame.setConsumo(1);
			ControleDoControle = 0;
			break;
		}
	}
	
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if (pKeyCode == KeyEvent.KEYCODE_MENU) {
			if (this.gScene.getChildScene() == gMenuScene) {
				/* Remove the menu and reset it. */
				this.gMenuScene.back();
				this.gScene.setChildScene(this.gDigitalOnScreenControl);

			} else {
				this.gScene.setChildScene(this.gMenuScene, false, true, true);
			}
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}

	public boolean onMenuItemClicked(final MenuScene pMenuScene,
			final IMenuItem pMenuItem, final float pMenuItemLocalX,
			final float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case MENU_RESET:
			/* Restart the animation. */
			this.gScene.reset();
			/* Remove the menu and reset it. */
			this.gScene.setChildScene(gDigitalOnScreenControl);
			return true;
		case MENU_QUIT:
			/* End Activity. */
			this.gMenuScene.back();
			this.gScene.setChildScene(gDigitalOnScreenControl);
			return true;
		case MENU_SENSOR:
			if (sensorHabilitado){
				sensorManager.unregisterListener(this);
				Log.d(LOG, "Sensor Desabilitado");
				sensorHabilitado = false;
			} else{
				sensorManager.registerListener(this,
						sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						sensorManager.SENSOR_DELAY_GAME);
				Log.d(LOG, "Sensor Habilitado");
				sensorHabilitado = true;
			}
			this.gMenuScene.back();
			return true;	
		default:
			return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void createMenuScene() {
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
	
	public Shape addLinha(float f ,float g, float h,float i){

		double tamanho=Math.sqrt((f-h)*(f-h)+(g-i)*(g-i));
		double angulo=((f-h)*(f-h))/((f-h)*tamanho);
		double graus=Math.toDegrees(Math.acos(angulo)); 
		float x=f;
		float y=g;
		if(i<g){
		x=h;
		y=i;
		}
		else 
		graus=180-graus;

		Shape linha = new Rectangle(x, y,(float) tamanho, 1);
		linha.setRotationCenter(0, 0);
		linha.setRotation((float)graus);
		linha.setColor(0, 1, 0);	
		return linha;
		}

	@Override
	protected void onStop() {
		gThreadGame.setAtivado(false);
		super.onStop();
	}
	
	@Override
	protected void onStart(){
		super.onStart();
	}


	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}


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
						controlaNave(CONTROLES.cDireita);
					} else if (x > cControleX-1) 
					{
						controlaNave(CONTROLES.cEsquerda);
					}
					
					if (z < cControleZ +1)
					{
						controlaNave(CONTROLES.cCima);
					} else if (z > cControleZ -1) 
					{
						controlaNave(CONTROLES.cBaixo);
					}
				} else if (cControleY <= 6) 
				{
					if (x < cControleX+1)
					{
						controlaNave(CONTROLES.cDireita);
					} else if (x > cControleX-1) 
					{
						controlaNave(CONTROLES.cEsquerda);
					}
					
					if (y < cControleY +1)
					{
						controlaNave(CONTROLES.cCima);
					} else if (y < cControleY -1) 
					{
						controlaNave(CONTROLES.cBaixo);
					}	
				}
				
				//Log.d(LOG, " X: " + x +  " Y: "+ y +  " Z: "+ z);
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

}