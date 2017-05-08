package com.islavstan.lunahod.player;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;


public interface Ibody {
    public Body createBody(World world);
}
