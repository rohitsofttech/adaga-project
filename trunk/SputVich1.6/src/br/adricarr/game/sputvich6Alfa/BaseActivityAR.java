package br.adricarr.game.sputvich6Alfa;

import java.util.ArrayList;

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
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.augmentedreality.BaseAugmentedRealityGameActivity;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;

import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class BaseActivityAR extends BaseAugmentedRealityGameActivity implements
		IOnMenuItemClickListener {

	/**
	 * Constantes
	 */
	private static final String LOG = "SPUTVICH";

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 720;

	private static final FixtureDef FIXTURE_NAVE = PhysicsFactory
			.createFixtureDef(0.0f, .0f, 0.0f);
	private Vector2 gVetor_Gravidade = new Vector2(0,
			SensorManager.GRAVITY_MOON);

	protected static final int MENU_RESET = 0;
	protected static final int MENU_QUIT = MENU_RESET + 1;
	private int NUMERO_FLAGS = 0;

	private ThreadGame gThreadGame;
	/**
	 * Variaveis Globais
	 */
	private Camera gCamera;

	private Texture gOnScreenControlTexture;
	private TextureRegion gOnScreenControlBaseTextureRegion;
	private TextureRegion gOnScreenControlKnobTextureRegion;

	// Recursos da nave
	private AnimatedSprite gNave;
	private Texture gTextureNave;
	private TiledTextureRegion gRegionTNave;
	private Body gNaveBody;

	// Recursos da nave
	private Texture gTextureFlag;
	private TextureRegion gRegionTFlag;

	// Controle de tela
	private DigitalOnScreenControl gDigitalOnScreenControl;

	// Parte Fisica do jogo
	public PhysicsWorld gMundoFisico;

	// Menu
	protected MenuScene gMenuScene;
	protected Scene gScene;

	// textura dos menus
	private Texture mMenuTexture;
	private TextureRegion mMenuQuitTextureRegion;
	private TextureRegion mMenuResetTextureRegion;

	// variavel para controlar o controle
	private int ControleDoControle = 0;

	// tipo a fonte
	private Font gFont;
	private Texture gTextureFont;

	// placar
	private Sprite gPlacar;
	private Texture gPlacarTexture;
	private TextureRegion gPlacarTextureRegion;

	// texto do combustivo
	private ChangeableText gCombustivel;

	// obstaculos do jogo
	private Texture gMeteoroTexture;
	private TextureRegion gMeteroroTextureRegion;

	// Lista de Meteoros
	public ArrayList<PhysicsConnector> gListaConector = new ArrayList<PhysicsConnector>();

	// Ch�o
	private Shape gGround;

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ChangeableText getCombustivel() {
		return gCombustivel;
	}

	public void setCombustivel(ChangeableText pCombustivel) {
		this.gCombustivel = pCombustivel;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public Engine onLoadEngine() {

		this.gCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.gCamera));
	}

	public void onLoadResources() {
		TextureRegionFactory.setAssetBasePath("imagens/");

		this.gTextureNave = new Texture(128, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gRegionTNave = TextureRegionFactory.createTiledFromAsset(
				gTextureNave, this, "nave_tiled.png", 0, 0, 2, 2);

		this.gOnScreenControlTexture = new Texture(256, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gOnScreenControlBaseTextureRegion = TextureRegionFactory
				.createFromAsset(this.gOnScreenControlTexture, this,
						"onscreen_control_base.png", 0, 0);
		this.gOnScreenControlKnobTextureRegion = TextureRegionFactory
				.createFromAsset(this.gOnScreenControlTexture, this,
						"onscreen_control_knob.png", 128, 0);

		this.mMenuTexture = new Texture(256, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuResetTextureRegion = TextureRegionFactory.createFromAsset(
				this.mMenuTexture, this, "menu_reset.png", 0, 0);
		this.mMenuQuitTextureRegion = TextureRegionFactory.createFromAsset(
				this.mMenuTexture, this, "menu_quit.png", 0, 50);

		this.gTextureFlag = new Texture(32, 32,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gRegionTFlag = TextureRegionFactory.createFromAsset(
				this.gTextureFlag, this, "flag.png", 0, 0);

		this.gTextureFont = new Texture(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gFont = new Font(this.gTextureFont, Typeface.create(
				Typeface.DEFAULT, Typeface.BOLD), 16, true, Color.BLACK);

		this.gPlacarTexture = new Texture(512, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gPlacarTextureRegion = TextureRegionFactory.createFromAsset(
				this.gPlacarTexture, this, "placar.png", 0, 0);

		this.gMeteoroTexture = new Texture(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.gMeteroroTextureRegion = TextureRegionFactory.createFromAsset(
				this.gMeteoroTexture, this, "asteroide.png", 0, 0);

		this.mEngine.getTextureManager().loadTextures(
				this.gOnScreenControlTexture, this.gTextureNave,
				this.mMenuTexture, this.gTextureFlag, this.gTextureFont,
				this.gPlacarTexture, this.gMeteoroTexture);
		this.mEngine.getFontManager().loadFont(gFont);
	}

	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger(500000000));

		gScene = new Scene(1);
		gScene.setBackground(new ColorBackground(1, 1, 1));
		this.gMundoFisico = new PhysicsWorld(gVetor_Gravidade, false);
		gScene.registerUpdateHandler(this.gMundoFisico);

		this.createMenuScene();

		this.createNave();

		this.gDigitalOnScreenControl = this.addControles();
		gScene.setChildScene(this.gDigitalOnScreenControl);

		this.createPontuacao();

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0.0f,
				0.0f, 0.0f);
		this.gGround = new Rectangle(0, CAMERA_HEIGHT - 100, CAMERA_WIDTH - 20,
				2);
		gGround.setColor(0, 1, 0);
		PhysicsFactory.createBoxBody(this.gMundoFisico, gGround,
				BodyType.StaticBody, wallFixtureDef);
		this.gScene.getBottomLayer().addEntity(gGround);

		Shape esquerda = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
		esquerda.setColor(0, 1, 0);
		PhysicsFactory.createBoxBody(this.gMundoFisico, esquerda,
				BodyType.StaticBody, wallFixtureDef);
		this.gScene.getBottomLayer().addEntity(esquerda);

		Shape direita = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);
		direita.setColor(0, 1, 0);
		PhysicsFactory.createBoxBody(this.gMundoFisico, direita,
				BodyType.StaticBody, wallFixtureDef);
		this.gScene.getBottomLayer().addEntity(direita);
		// cria os meteoros que vai ser utilizado no jogo
		criaMeteoro(10, 10, new Vector2(0, 0), new Vector2(0, 0));

		gScene.registerUpdateHandler(new IUpdateHandler() {
			public void reset() {
			}

			public void onUpdate(float pSecondsElapsed) {
				sistemaDeInteracao();
			}
		});

/*		this.gThreadGame = new ThreadGame(1000, 1, this);
		this.gThreadGame.start();*/

		return gScene;
	}

	protected void sistemaDeInteracao() {
		if (gNave.collidesWith(gGround)) {
			if (NUMERO_FLAGS <= 0) {
				colocaFlags(gNave.getX(), gNave.getY()
						+ (gNave.getHeight() / 2));
				NUMERO_FLAGS++;
				gThreadGame.setAtivado(false);
			}
		} else {
		}
		// chama o metodo que verifica se os meteors chegaram ao ch�o
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
			// cria o corpo fisico mas deixa inativo ate ser usado
			bodyMeteoro.setActive(false);
		}
	}

	// metodo verifica se o meteoro colidiu, desativa o corpo fisico e move o
	// meteoro de lugar
	public synchronized void removeMeteoro() {
		for (int i = this.gListaConector.size() - 1; i >= 0; i--) {
			PhysicsConnector conector = this.gListaConector.get(i);
			if (gGround.collidesWith(conector.getShape())) {
				this.gListaConector.get(i).getBody()
						.setTransform(new Vector2(10f, 10f), 0);

				this.gListaConector.get(i).getBody().setActive(false);
				Log.d(LOG, "COnseguiu Remover MEteoro" + i + "  "
						+ this.gListaConector.size());
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
		Vector2 lista[] = new Vector2[4];
		lista[0] = new Vector2(0, 0);// tentativa de vetorizar
		lista[1] = new Vector2(1, 0);
		lista[2] = new Vector2(1, 0.7f);
		lista[3] = new Vector2(0.0f, 0.7f);
		// lista[4]=new Vector2(0.75f,0.6f);
		// lista[5]=new Vector2(0.3f,0.6f);

		Log.d(LOG, "Nave sendo inciada");
		this.gNave = new AnimatedSprite(
				(CAMERA_WIDTH - this.gTextureNave.getWidth()) / 2, 0,
				this.gRegionTNave);
		this.gNaveBody = PhysicsFactory.createPolygonBody(this.gMundoFisico,
				gNave, lista, BodyType.DynamicBody, FIXTURE_NAVE);
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
							final float pValueX, final float pValueY) {
						if ((pValueX != 0.0) || (pValueY != 0.0)) {
							if (pValueY < 0.0f) // Para Cima
							{
								ControleDoControle = 1; // 1 para cima
								Vector2 vetor = gNaveBody.getLinearVelocity();
								float aceleracao = ((-gVetor_Gravidade.y / 8) - (vetor.y / 10));
								gNaveBody.applyLinearImpulse(new Vector2(0.0f,
										aceleracao), new Vector2(0, 0));
								gThreadGame.setConsumo(3);
							}
							if (pValueY > 0.0f) // Para Baixo
							{
								ControleDoControle = 2; // 2 para baixo
								gNaveBody.applyLinearImpulse(new Vector2(0.0f,
										0.15f), new Vector2(0, 0));
								gThreadGame.setConsumo(0);
							}
							if (pValueX < 0.0f) // Para Esquerda
							{
								ControleDoControle = 3;
								gNaveBody.applyLinearImpulse(new Vector2(
										-0.05f, 0.0f), new Vector2(0, 0));
								gNaveBody.setAngularVelocity(-0.5f);
								float angulo = gNaveBody.getAngle();
								if (-angulo > Math.PI / 4) {
									gNaveBody.setAngularVelocity(0);
								}
								gThreadGame.setConsumo(2);

							}
							if (pValueX > 0.0f) // Para Direita
							{
								ControleDoControle = 4;
								gNaveBody.applyLinearImpulse(new Vector2(0.05f,
										0.0f), new Vector2(0, 0));
								gNaveBody.setAngularVelocity(.5f);
								float angulo = gNaveBody.getAngle();
								if (angulo > Math.PI / 4) {
									gNaveBody.setAngularVelocity(0);
								}
								gThreadGame.setConsumo(2);
							}
						}

						if ((ControleDoControle != 4)
								&& (ControleDoControle != 3)) {

							gNaveBody.setAngularVelocity(0);
							float angulo = gNaveBody.getAngle();
							if (angulo > 0.01f) {
								gNaveBody.setAngularVelocity(-0.3f);
							} else if (angulo < -0.01f) {
								gNaveBody.setAngularVelocity(0.3f);
							} else {
								gNaveBody.setAngularVelocity(0);
							}
						}
						if (ControleDoControle == 0)
							gThreadGame.setConsumo(1);

						ControleDoControle = 0;
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

	public void onAccelerometerChanged(AccelerometerData pAccelerometerData) {
		// TODO Auto-generated method stub

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

		this.gMenuScene.buildAnimations();

		this.gMenuScene.setBackgroundEnabled(false);

		this.gMenuScene.setOnMenuItemClickListener(this);

	}

	@Override
	protected void onStop() {
		gThreadGame.setAtivado(false);
		super.onStop();
	}

}
