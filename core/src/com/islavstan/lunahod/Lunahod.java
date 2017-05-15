package com.islavstan.lunahod;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.I18NBundle;
import com.boontaran.games.StageGame;
import com.islavstan.lunahod.level.LevelList;
import com.islavstan.lunahod.media.Media;
import com.islavstan.lunahod.screens.Intro;
import com.islavstan.lunahod.utils.Data;
import com.islavstan.lunahod.utils.GameCallback;

import java.util.Locale;

public class Lunahod extends Game {

	public static final int SHOW_BANNER = 1;
	public static final int HIDE_BANNER = 2;
	public static final int LOAD_INTERSTITIAL = 3;
	public static final int SHOW_INTERSTITIAL = 4;
	public static final int OPEN_MARKET = 5;
	public static final int SHARE = 6;

	private GameCallback gameCallback;
	private I18NBundle bundle; //класс для выбора ресурсов для локализации
	private String pathToAtlas;
	private Intro intro;

	private LevelList levelList;

	public Lunahod(GameCallback gameCallback) {
		this.gameCallback = gameCallback;
	}

	private boolean loadingAssets = false;
	//управляет загрузкой ресурсов
	private AssetManager assetManager;
	//через него работаем с созданным атласом
	public static TextureAtlas atlas;
	//для работы с шрифтом
	public static BitmapFont font40;

	public static Media media;
	public static Data data;

	@Override
	public void create() {
		//размер экрана
		StageGame.setAppSize(800, 400);
		//перехватываем кнопку назад на устройстве
		Gdx.input.setCatchBackKey(true);

		//определяем локаль устройства для выбора языка
          Locale locale = Locale.getDefault();
		bundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);//в зависимости от локали берём нужный bundle
		pathToAtlas = bundle.get("path");




		loadingAssets = true;
		assetManager = new AssetManager();
		//загружаем атлас
		assetManager.load(pathToAtlas, TextureAtlas.class);
		//загружаем музыку и звуки
		assetManager.load("musics/music1.ogg", Music.class);
		assetManager.load("musics/level_failed.ogg", Music.class);
		assetManager.load("musics/level_win.ogg", Music.class);
		assetManager.load("sounds/level_completed.ogg", Sound.class);
		assetManager.load("sounds/fail.ogg", Sound.class);
		assetManager.load("sounds/click.ogg", Sound.class);
		assetManager.load("sounds/crash.ogg", Sound.class);

		//подготавливаем шрифт для работы
		FileHandleResolver resolver = new InternalFileHandleResolver();
		//загружаем лоадер
		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		//задаём местоположение и размер шрифта
		FreetypeFontLoader.FreeTypeFontLoaderParameter sizeParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		sizeParams.fontFileName = "fonts/GROBOLD.ttf";
		sizeParams.fontParameters.size = 40;

		assetManager.load("font40.ttf", BitmapFont.class, sizeParams);

		media = new Media(assetManager);

		data = new Data();

	}

	@Override
	public void render() {//вызывается каждый раз когда должна быть выполнена визуализация
		if (loadingAssets) {
			if (assetManager.update()) {
				loadingAssets = false;
				onAssetsLoaded();
			}
		}
		super.render();


	}

	@Override
	public void dispose() {
		//вызывается перед остановкой игры, здесь освобождаем ресурсы
		assetManager.dispose();
		super.dispose();


	}

	private void onAssetsLoaded() {
		// получаем загруженные ресурсы и шрифты
		atlas = assetManager.get(pathToAtlas, TextureAtlas.class);
		font40 = assetManager.get("font40.ttf", BitmapFont.class);
		showIntro();
	}


	private void exitApp() {
		Gdx.app.exit();
	}



	private void showIntro() {
		intro = new Intro();
		setScreen(intro);//активируем экран

		intro.setCallback(new StageGame.Callback() {//cлушатель нажатий
			@Override
			public void call(int code) {
				if (code == Intro.ON_PLAY) {
					showLevelList();//показывать список уровней
					hideIntro();
				} else if (code == Intro.ON_BACK) {
					exitApp();
				}
			}
		});

		media.playMusic("music1.ogg", true);
	}
	private void hideIntro() {
		intro = null;
	}

	private void showLevelList() {
		levelList = new LevelList();
		setScreen(levelList);

		levelList.setCallback(new StageGame.Callback() {
			@Override
			public void call(int code) {

				if (code == LevelList.ON_BACK) {
					showIntro();
					hideLevelList();
				} else if (code == LevelList.ON_LEVEL_SELECTED) {
					//showLevel();
					hideLevelList();
				} else if(code == LevelList.ON_OPEN_MARKET) {
					gameCallback.sendMessage(OPEN_MARKET);
				} else if (code == LevelList.ON_SHARE) {
					gameCallback.sendMessage(SHARE);
				}

			}
		});

		gameCallback.sendMessage(SHOW_BANNER);
		media.playMusic("music1.ogg", true);
	}

	private void hideLevelList() {
		levelList = null;
		gameCallback.sendMessage(HIDE_BANNER);
	}


}

