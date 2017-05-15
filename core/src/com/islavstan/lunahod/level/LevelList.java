package com.islavstan.lunahod.level;



import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.boontaran.games.StageGame;
import com.islavstan.lunahod.Lunahod;
import com.islavstan.lunahod.media.LevelIcon;
import com.islavstan.lunahod.utils.Setting;

public class LevelList  extends StageGame {

    public static final int ON_BACK = 1;
    public static final int ON_LEVEL_SELECTED = 2;
    public static final int ON_OPEN_MARKET = 3;
    public static final int ON_SHARE = 4;

    private Group container; //контейнер для кнопок уровней
    private int selectedLevelId = 0; //для хранения активного уровня

    public LevelList() {
        Image bg = new Image(Lunahod.atlas.findRegion("intro_bg"));// фоновое изображение
        addBackground(bg, true, false);

        container = new Group();
        addChild(container);

        //создаём таблицу 4 на 4 для отображения иконок
        int row = 4, col = 4;
        //расстояние между кнопками уровней
        float space = 20;

        float iconWidht = 0, iconHeight = 0;
        int id = 1;
        int x, y;

        int progress = Lunahod.data.getProgress(); //для получения текущего прогресса

        for (y = 0; y < row; y++) {
            for (x = 0; x < col; x++) {
                LevelIcon icon = new LevelIcon(id);
                container.addActor(icon);

                if (iconWidht == 0) {
                    iconWidht = icon.getWidth();
                    iconHeight = icon.getHeight();
                }
//устанавливаем позицию кнопок на экране
                icon.setX(x * (iconWidht + space));
                icon.setY(((row - 1) - y) * (iconHeight + space));

                //снимаем блокировку всех уровней от текущего и ниже
                if (id <= progress) {
                    icon.setLock(false);
                }
                if (id == progress) {
                    icon.setHilite(); //подсвечиваем текущий уровень
                }

                if (Setting.DEBUG_GAME) {
                    icon.setLock(false);
                }

                icon.addListener(iconlistener);// добавляем слушатель для кнопок
                id++;
            }
        }
  // задаем размер и положение контейнера с кнопками и добавляем анимацию
        container.setWidth(col * iconWidht + (col - 1) * space);
        container.setHeight(row * iconHeight + (row - 1) * space);

        container.setX(30);
        container.setY(getHeight() - container.getHeight() - 30);

        container.setColor(1, 1, 1, 0);
        container.addAction(Actions.alpha(1, 0.4f));

        ImageButton rateBtn = new ImageButton(new TextureRegionDrawable(Lunahod.atlas.findRegion("rate")), //кнопка рейтинга
                new TextureRegionDrawable(Lunahod.atlas.findRegion("rate_down")));

        addChild(rateBtn);
        rateBtn.setX(getWidth() - rateBtn.getWidth() - 20);
        rateBtn.setY(getHeight() - rateBtn.getHeight() - 20);

        rateBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                call(ON_OPEN_MARKET);
            }
        });

        ImageButton shareBtn = new ImageButton(new TextureRegionDrawable(Lunahod.atlas.findRegion("share")), // кнопка поделиться
                new TextureRegionDrawable(Lunahod.atlas.findRegion("share_down")));
        addChild(shareBtn);
        shareBtn.setX(getWidth() - shareBtn.getWidth() - 20);
        shareBtn.setY(20);
        shareBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                call(ON_SHARE);
            }
        });
    }

    public int getSelectedLevelId() { //возвращает id выбранного уровня
        return selectedLevelId;
    }

    private ClickListener iconlistener = new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            selectedLevelId = ((LevelIcon) event.getTarget()).getId();
            Lunahod.media.playSound("click.ogg");// звук щелчка
            call(ON_LEVEL_SELECTED);
        }
    };

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            Lunahod.media.playSound("click.ogg");
            call(ON_BACK);
            return true;
        }

        return super.keyUp(keycode);
    }
}