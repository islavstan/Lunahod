package com.islavstan.lunahod.controls;


import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.islavstan.lunahod.Lunahod;

public class JoyStick extends Group {
    private Image idle, right, left; //кнопки
    public static final int IDLE = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    private int direction; // переменная направления

    public JoyStick(float minHeight) {
        idle = new Image(Lunahod.atlas.findRegion("joystick"));
        //actor используется для создания объектов в игре
        addActor(idle);
        float scale = 1;
        //настраиваем размер и расположение объекта кнопки на экране
        if (idle.getHeight() < minHeight) {
            scale = minHeight / idle.getHeight();
        }
        idle.setHeight(idle.getHeight() * scale);
        idle.setWidth(idle.getWidth() * scale);

        setSize(idle.getWidth(), idle.getHeight());

        right = new Image(Lunahod.atlas.findRegion("joystick_right"));
        right.setSize(getWidth(), getHeight());
        addActor(right);
        left = new Image(Lunahod.atlas.findRegion("joystick_left"));
        left.setSize(getWidth(), getHeight());
        addActor(left);

        setDir(IDLE);

        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //выполняется в момент нажатия пальца на экран
                handleTouch(x, y);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //выполняется в момент удержания пальца на экране
                handleTouch(x, y);
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //выполняется в момент поднятия пальца с экрана
                setDir(IDLE);
                super.touchUp(event, x, y, pointer, button);
            }
        });

    }

    public boolean isRight() {
        return direction == RIGHT;
    }

    public boolean isLeft() {
        return direction == LEFT;
    }

    private void handleTouch(float x, float y) {
        // определяем на какую половину кнопки было нажатие
        if (x > getWidth() / 2) {
            setDir(RIGHT);
        } else {
            setDir(LEFT);
        }
    }

    private void setDir(int dir) {
        //будет менять видимость актёров кнопки в зависимости от передаваемой константы
        idle.setVisible(false);
        right.setVisible(false);
        left.setVisible(false);

        if (dir == IDLE) idle.setVisible(true);
        if (dir == RIGHT) right.setVisible(true);
        if (dir == LEFT) left.setVisible(true);

        direction = dir;
    }


}