package com.islavstan.lunahod.level;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.boontaran.games.StageGame;



public class Level extends StageGame {
    public static final float WORLD_SCALE = 30; //коэфициэнт масштабирования
    private String directory;

    public Level(String directory) {
        this.directory = directory;
    }

    public void addChild(Actor actor) {//добавляет актёра на 2д сцену
        this.stage.addActor(actor);
    }

    public void addChild(Actor actor, float x, float y) {//добавляет актёра на 2д сцену и задаём координаты актёру
        this.addChild(actor);
        actor.setX(x);
        actor.setY(y);
    }
}