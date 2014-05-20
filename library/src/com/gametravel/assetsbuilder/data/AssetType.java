/**
 * 
 */
package com.gametravel.assetsbuilder.data;

/**
 * Enumeration of various asset types that supported by AssetsBuilder tool.
 * @author Daniel Chow Mar 30, 2014 11:42:31 PM
 */
public enum AssetType {

    /**
     * Audio assets, class type can be {@link com.badlogic.gdx.audio.Sound} or
     * {@link com.badlogic.gdx.audio.Music}
     */
    AUDIO("audio"),

    /** Texture assets, class type is {@link com.badlogic.gdx.graphics.Texture} */
    TEXTURE("texture"),

    /** TextureAtlas assets, class type is {@link com.badlogic.gdx.graphics.g2d.TextureAtlas} */
    TEXTURE_ATLAS("texture_atlas"),

    /** BitmapFont assets, class type is {@link com.badlogic.gdx.graphics.g2d.BitmapFont} */
    BITMAP_FONT("bitmap_font"),

    /** Particle assets, class type is {@link com.badlogic.gdx.graphics.g2d.ParticleEffect} */
    PARTICLE("particle"),

    /** Fragment Shader assets, class type is {@link com.badlogic.gdx.graphics.glutils.ShaderProgram} */
    FRAGMENT_SHADER("fragment_shader"),

    /** Vertex Shader assets, class type is {@link com.badlogic.gdx.graphics.glutils.ShaderProgram} */
    VERTEX_SHADER("vertex_shader"),

    /** Spine json format assets, class type is {@link com.esotericsoftware.spine.SkeletonJson} */
    SPINE_JSON("spine_json"),

    /** Spine binary format assets, class type is {@link com.esotericsoftware.spine.SkeletonBinary} */
    SPINE_BINARY("spine_binary");
    
    // TODO Pixmap, Skin, Model

    /** Name of asset type. */
    private String name;

    private AssetType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
