package com.islavstan.lunahod.utils;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

//упаковщик текстур
public class Packer {
    public static void main(String[] args) {
        TexturePacker.Settings set = new TexturePacker.Settings();
        set.filterMin = Texture.TextureFilter.MipMapLinearNearest;
        set.filterMag = Texture.TextureFilter.Linear;
        set.paddingX = 2;
        set.paddingY = 2;
        set.maxHeight = 2048;
        set.maxWidth = 2048;

        //указываем настройки, путь картинок, путь для сохранения атласа текстур и название атласа
        TexturePacker.process(set, "raw_images_en", "android/assets/images_en", "pack");

    }
}
