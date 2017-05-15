package com.islavstan.lunahod.media;

/**
 * Created by islav on 09.05.2017.
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.islavstan.lunahod.Lunahod;


//класс для описания иконок уровней
public class LevelIcon extends Group{
    private int id;
    private Label label; // представляет собой текстовую надпись
    private Image lockImg, bg, bgDown, hiliteImg;
    private boolean isHilited = false;
    private boolean alphaUp = false;

    public LevelIcon(int id) {
        this.id = id;
       //для подсветки кнопки текущего уровня
        hiliteImg = new Image(Lunahod.atlas.findRegion("level_icon_hilite"));
        addActor(hiliteImg);
        hiliteImg.setVisible(false);//делаем невидимым

        bg = new Image(Lunahod.atlas.findRegion("level_icon_bg")); //фон для кнопки
        addActor(bg);
        setSize(bg.getWidth(), bg.getHeight()); //размер по размеру текстуры

        hiliteImg.setX((getWidth()-hiliteImg.getWidth())/2);
        hiliteImg.setY((getHeight()-hiliteImg.getHeight())/2);


          //фон нажатой кнопки
        bgDown = new Image(Lunahod.atlas.findRegion("level_icon_bg_down"));
        addActor(bgDown);

        //положение текстуры
        bgDown.setX(bg.getX() + (bg.getWidth()-bgDown.getWidth())/2);
        bgDown.setY(bg.getY() + (bg.getHeight()-bgDown.getHeight())/2);
        bgDown.setVisible(false);

        lockImg = new Image(Lunahod.atlas.findRegion("level_icon_lock"));
        lockImg.setX((getWidth()-lockImg.getWidth())/2);
        lockImg.setY((getHeight()-lockImg.getHeight())/2);

        //стиль надписи на кнопке
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Lunahod.font40;
        style.fontColor = new Color(0x000000ff);

        label = new Label(id + "", style);
        label.setX((getWidth() - label.getWidth())/2);
        label.setY((getHeight() - label.getHeight())/2);

        setLock(true);

        addCaptureListener(new EventListener() {//добавляет слушателя событий корневому элементу, отключая его для дочерних элементов
            @Override
            public boolean handle(Event event) {
                event.setTarget(LevelIcon.this);
                return true;
            }
        });

        addListener(new ClickListener(){//слушатель нажатия кнопки
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                bgDown.setVisible(true);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                bgDown.setVisible(false);
                super.touchUp(event, x, y, pointer, button);
            }
        });

    }

    public int getId() {
        return id;
    }

    public void setLock(boolean lock) {//метод для блокирования уровней
        if (lock) {
            label.remove();
            addActor(lockImg);
            setTouchable(Touchable.disabled);
        } else {
            lockImg.remove();
            addActor(label);
            setTouchable(Touchable.enabled);
        }
    }

    public void setHilite() { //делает видимой подсветку кнопки
        hiliteImg.setVisible(true);
        isHilited = true;
    }

    @Override
    public void act(float delta) {//вызывается каждый фрейм
        //код анимации мигания, подсветки кнопки текущего уровня
        if (isHilited) {
            float alpha = hiliteImg.getColor().a;

            if (alphaUp) {
                alpha += delta * 4;
                if (alpha >= 1) {
                    alpha = 1;
                    alphaUp = false;
                }
            } else {
                alpha -= delta * 4;
                if (alpha < 0) {
                    alpha = 0;
                    alphaUp = true;
                }
            }
            hiliteImg.setColor(1,1,1,alpha);
        }

        super.act(delta);
    }
}