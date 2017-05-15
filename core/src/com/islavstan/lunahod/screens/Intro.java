package com.islavstan.lunahod.screens;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.StringBuilder;
import com.boontaran.games.StageGame;
import com.islavstan.lunahod.Lunahod;

public class Intro extends StageGame {
    public static final int ON_PLAY = 1;
    public static final int ON_BACK = 2;
    private Image title;
    private ImageButton playBtn;

    public Intro() {

        Image bg = new Image(Lunahod.atlas.findRegion("intro_bg"));//изображение для фона
        addBackground(bg, true, false);

        title = new Image(Lunahod.atlas.findRegion("title"));//заголовок
        addChild(title);
        //позиция заголовка на экране
        centerActorX(title);
        title.setY(getHeight());
        //анимация появления заголовка используя MoveByAction

        MoveByAction move = new MoveByAction();
        move.setAmount(0, -title.getHeight() * 1.5f);
        move.setDuration(0.4f);
        move.setInterpolation(Interpolation.swingOut);
        move.setActor(title);

        title.addAction(Actions.delay(0.5f, move)); //задержка перед проигрыванием анимации

        //текстура нажатой кнопки
        playBtn = new ImageButton(
                new TextureRegionDrawable(Lunahod.atlas.findRegion("play_btn")),
                new TextureRegionDrawable(Lunahod.atlas.findRegion("play_btn_down"))
        );

        //задаём положение кнопки
        addChild(playBtn);
        centerActorXY(playBtn);
        playBtn.moveBy(0, -60);

        //анимация для появления кнопки, используем AlphaAction, это будет смена прозрачности
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setActor(playBtn);
        alphaAction.setDuration(0.8f);
        alphaAction.setAlpha(1);

        playBtn.setColor(1, 1, 1, 0);
        playBtn.addAction(Actions.delay(0.8f, alphaAction));

        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playBtn.clearActions(); //удаляет анимацию
                onClickPlay();
                Lunahod.media.playSound("click.ogg");
            }
        });
    }

    private void onClickPlay() {//анимация исчезания заголовка и кнопки
        playBtn.setTouchable(Touchable.disabled);
        playBtn.addAction(Actions.alpha(0, 0.3f));
        title.addAction(Actions.moveTo(title.getX(), getHeight(), 0.5f, Interpolation.swingIn));

        delayCall("delay1", 0.7f);
    }

    public boolean keyUp(int keycode) {// отработка кнопки ESCAPE на пк или назад на андроиде
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            call(ON_BACK);
            return true;
        }
        return super.keyUp(keycode);
    }

    protected void onDelayCall(String code) {
        if (code.equals("delay1")) {
            call(ON_PLAY);
        }
    }

}