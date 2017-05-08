package com.islavstan.lunahod.player;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.boontaran.games.ActorClip;
import com.boontaran.marchingSquare.MarchingSquare;
import com.islavstan.lunahod.Lunahod;
import com.islavstan.lunahod.level.Level;
import com.islavstan.lunahod.utils.Setting;

import java.util.ArrayList;


public class Player extends ActorClip implements  Ibody {
    private Image roverImage, astronautImage, astronautFallImage, frontWheelImage, rearWheelImage;
    private Group frontWheelCont, rearWheelCont, astronautFallCont;//контейнеры для картинок колёс и астронавта при падении
    public Body rover, frontWheel, rearWheel, astronaut;//кузов, колёса, астронавт
    private Joint frontWheelJoint, rearWheelJoint, astroJoint; // для соединения колёс и автронавта
    private World world;
    private boolean hasDestoyed = false;
    private boolean destroyOnNextUpdate = false;
    private boolean isTouchGround = true;
    private float jumpImpulse = Setting.JUMP_IMPULSE;
    private float jumpWait = 0;

    private Level level;


    public Player(Level level) {
        this.level = level;


        roverImage = new Image(Lunahod.atlas.findRegion("rover"));//находим картинку лунахода в атласе
        childs.addActor(roverImage);
        roverImage.setX(-roverImage.getWidth() / 2);
        roverImage.setY(-15);

        astronautImage = new Image(Lunahod.atlas.findRegion("astronaut"));
        childs.addActor(astronautImage);
        astronautImage.setX(-35);
        astronautImage.setY(20);

        //при ударе о землю астронавт выпадает из лунахода, создаём группу для него
        astronautFallCont = new Group();
        astronautFallImage = new Image(Lunahod.atlas.findRegion("astronaut_fall"));
        astronautFallCont.addActor(astronautFallImage);
        astronautFallImage.setX(-astronautFallImage.getWidth() / 2);
        astronautFallImage.setY(-astronautFallImage.getHeight() / 2);

    }


    public void touchGround() {
        isTouchGround = true; // если объект коснулся земли
    }

    public boolean isTouchedGround() {
        if (jumpWait > 0) return false;
        return isTouchGround;
    }


    @Override
    public Body createBody(World world) {
        this.world = world;

        BodyDef def = new BodyDef();//содержит все данные для построения твёрдого тела
        def.type = BodyDef.BodyType.DynamicBody; //задаём тип тела
        def.linearDamping = 0; //для уменьшения линейной скорости

//передние колёса
        frontWheelCont = new Group();
        frontWheelImage = new Image(Lunahod.atlas.findRegion("front_wheel"));
        frontWheelCont.addActor(frontWheelImage);
        frontWheelImage.setX(-frontWheelImage.getWidth() / 2);
        frontWheelImage.setY(-frontWheelImage.getHeight() / 2);

        getParent().addActor(frontWheelImage);

        UserData data = new UserData();
        data.actor = frontWheelCont;
        frontWheel.setUserData(data);

        RevoluteJointDef rDef = new RevoluteJointDef();
        rDef.initialize(rover, frontWheel, new Vector2(frontWheel.getPosition()));//инициализируем точки привязки и опорный угол
        frontWheelJoint = world.createJoint(rDef);//связывает тела вместе

//задние колёса
        rearWheelCont = new Group();
        rearWheelImage = new Image(Lunahod.atlas.findRegion("rear_wheel"));
        rearWheelCont.addActor(rearWheelImage);
        rearWheelImage.setX(-rearWheelImage.getWidth() / 2);
        rearWheelImage.setY(-rearWheelImage.getHeight() / 2);
        getParent().addActor(rearWheelImage);
        data = new UserData();
        data.actor = rearWheelCont;
        rearWheel.setUserData(data);

        rDef = new RevoluteJointDef();
        rDef.initialize(rover, rearWheel, new Vector2(rearWheel.getPosition()));//инициализируем точки привязки и опорный угол
        rearWheelJoint = world.createJoint(rDef);//связывает тела вместе


        return rover;
    }

    private Body createWheel(World world, float rad) {//создаём твёрдые тела - колёса
        BodyDef def = new BodyDef();//содержит все данные для построения твёрдого тела
        def.type = BodyDef.BodyType.DynamicBody; //задаём тип тела
        def.linearDamping = 0; //для уменьшения линейной скорости
        def.angularDamping = 1f; // для снижения угловой скорости

        Body body = world.createBody(def);
        FixtureDef fixtureDef = new FixtureDef(); //конструкция с набором физических свойств
        CircleShape shape = new CircleShape();
        shape.setRadius(rad);

        fixtureDef.shape = shape;
        fixtureDef.restitution = 0.5f; //задаём эластичность в диапозоне от 0 до 1
        fixtureDef.friction = 0.4f; // коэфициэнт трения от 0 до 1
        fixtureDef.density = 1; //задаём плотность в килограммах на квадратный метр

        body.createFixture(fixtureDef);//создаёт конструкцию и прикрепляет её к этому телу
        shape.dispose(); //вызываем когда фигура уже не нужна

        return body;

    }

    private float[] traceOutline(String regionName) {//принимает на вход текстуру модели и по её контурам создаёт полигон
        // который будет использоваться для расчета столкновений

        Texture bodyOutLine = Lunahod.atlas.findRegion(regionName).getTexture();
        TextureAtlas.AtlasRegion reg = Lunahod.atlas.findRegion(regionName);
        //возвращают ширину и высоту области изображения
        int w = reg.getRegionWidth();
        int h = reg.getRegionHeight();
        //возвращают координаты

        int x = reg.getRegionX();
        int y = reg.getRegionY();


        bodyOutLine.getTextureData().prepare();
        //получаем пиксельные данные текстуры
        Pixmap allPixmap = bodyOutLine.getTextureData().consumePixmap();
        //строим контур изображения, на вход идут высота и ширина а также кол-во и порядок цветовых компонентов на пиксель
        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pixmap.drawPixmap(allPixmap, 0, 0, x, y, w, h);

        allPixmap.dispose();

        int pixel;
        //схраняем ширину и высоту объектов pixmap в переменную
        w = pixmap.getWidth();
        h = pixmap.getHeight();

        int[][] map;
        map = new int[w][h];
        for (x = 0; x < w; x++) {
            for (y = 0; y < h; y++) {
                pixel = pixmap.getPixel(x, y);
                if ((pixel & 0x000000ff) == 0) {
                    map[x][y] = 0; //0 означает прозрачный
                } else {
                    map[x][y] = 1;
                }
            }
        }

        pixmap.dispose();

        //используем массив map для создания полигона
        MarchingSquare ms = new MarchingSquare(map);
        ms.invertY();
        ArrayList<float[]> traces = ms.traceMap();

        float[] polyVertices = traces.get(0);
        return polyVertices;
    }

    private Body createBodyFromTriangles(World world, Array<Polygon> triangles) {//создаёт тело из треугольников
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.linearDamping = 0;
        Body body = world.createBody(def);

        // прикреплям несколько конструкций к телу
        for (Polygon triangle : triangles) {
            FixtureDef fDef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.set(triangle.getTransformedVertices());

            fDef.shape = shape;
            fDef.restitution = 0.3f;
            fDef.density = 1;

            body.createFixture(fDef);
            shape.dispose();
        }
        return body;
    }

    public void onKey(boolean moveFrontKey, boolean moveBackKey) {//вызывается при нажатии кнопок движения
        float torque = Setting.WHEEL_TORQUE;
        float maxAV = 18;

        if (moveFrontKey) {
            if (-rearWheel.getAngularVelocity() < maxAV) {
                rearWheel.applyTorque(-torque, true);
            }
            if (-frontWheel.getAngularVelocity() < maxAV) {
                frontWheel.applyTorque(-torque, true);
            }
        }
        if (moveBackKey) {
            if (rearWheel.getAngularVelocity() < maxAV) {
                rearWheel.applyTorque(torque, true);
            }
            if (frontWheel.getAngularVelocity() < maxAV) {
                frontWheel.applyTorque(torque, true);
            }
        }
    }

    public void jumpBack(float value) {//метод при нажатии кнопки прыжка
        if (value < 0.2f) value = 0.2f;

        rover.applyLinearImpulse(0, jumpImpulse * value,
                rover.getWorldCenter().x + 5 / Level.WORLD_SCALE,
                rover.getWorldCenter().y, true);
        isTouchGround = false;
        jumpWait = 0.3f;
    }

    public void jumpForward(float value) {//прыжок вперёд
        if (value < 0.2f) value = 0.2f;

        rover.applyLinearImpulse(0, jumpImpulse * value,
                rover.getWorldCenter().x - 4 / Level.WORLD_SCALE,
                rover.getWorldCenter().y, true);
        isTouchGround = false;
        jumpWait = 0.3f;
    }

    @Override
    public void act(float delta) {//обновляет актёров, обычно вызывается каждый фрейм
        if (jumpWait > 0) {
            jumpWait -= delta;
        }

        if (destroyOnNextUpdate) {//удаляем все игровые объекты
            destroyOnNextUpdate = false;
            world.destroyJoint(frontWheelJoint);
            world.destroyJoint(rearWheelJoint);
            world.destroyJoint(astroJoint);
            world.destroyBody(astronaut);
            astronautImage.remove();

            astronautFall();
        }

        super.act(delta);
    }

    private void astronautFall() {//анимация падения астронавта
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.linearDamping = 0;
        def.angularDamping = 0;
        //получаем позицию
        def.position.x = astronaut.getPosition().x;
        def.position.y = astronaut.getPosition().y;
        //угол
        def.angle = getRotation() * 3.1416f / 180;
        //угловую скорость
        def.angularVelocity = astronaut.getAngularVelocity();

        Body body = world.createBody(def);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / Level.WORLD_SCALE);

        fDef.shape = shape;
        fDef.restitution = 0.5f;
        fDef.friction = 0.4f;
        fDef.density = 1;
        fDef.isSensor = true;// собирает инфу о контакте объектов с другими объектами

        body.createFixture(fDef);

        body.setLinearVelocity(astronaut.getLinearVelocity());// задаём линейную скорость

        shape.dispose();

        level.addChild(astronautFallCont);
        astronautFallCont.setPosition(getX(), getY());

        //прикрепляем актёра к box2d телу
        UserData data = new UserData();
        data.actor = astronautFallCont;
        body.setUserData(data);
    }

    public void destroy() {
        if (hasDestoyed) return;
        hasDestoyed = true;

        destroyOnNextUpdate = true;
    }

    public boolean isHasDestoyed() {
        return hasDestoyed;
    }
}