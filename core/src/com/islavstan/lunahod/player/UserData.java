package com.islavstan.lunahod.player;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by islav on 08.05.2017.
 */

public class UserData {
    public Actor actor;
    public String name = "";

    public UserData(Actor actor, String name) {
        this.actor = actor;
        this.name = name;
    }

    public UserData() {
    }
}
