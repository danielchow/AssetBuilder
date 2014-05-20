/**
 *
 */
package com.gametravel.assetsbuilder.data.manageable;

import java.io.File;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Region;
import com.badlogic.gdx.utils.Array;
import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.data.ManageableAssetData;
import com.gametravel.assetsbuilder.scan.AssetBundle;

/**
 * @author Daniel Chow Mar 31, 2014 1:11:56 AM
 */
public class ParticleEffectAssetData extends ManageableAssetData {

    public TextureAtlasAssetData atlasAssetData;
    public Array<TextureAssetData> textureArray;
    public Array<String> imageNames = new Array<String>(false, 8);

    public ParticleEffectAssetData(FileHandle file) {
        super(ParticleEffect.class, file);
    }

    @Override
    public boolean resolve(AssetBundle bundle, Options options) {
        super.resolve(bundle, options);
        imageNames.clear();
        ParticleEffect effect = new ParticleEffect();
        effect.loadEmitters(file);
        Array<ParticleEmitter> emitters = effect.getEmitters();
        for (int i = 0; i < emitters.size; i++) {
            ParticleEmitter emitter = emitters.get(i);
            String imagePath = emitter.getImagePath();
            String imageName = new File(imagePath.replace('\\', '/')).getName();
            int lastDotIndex = imageName.lastIndexOf('.');
            if (lastDotIndex != -1) imageName = imageName.substring(0, lastDotIndex);
            imageNames.add(imageName);
        }
        Array<TextureAssetData> texturesToRemove = new Array<TextureAssetData>(false, 1);

        Array<TextureAssetData> textures = bundle.getAssets(TextureAssetData.class);
        // assume image should be loaded from texture
        for (int i = 0; i < imageNames.size; i++) {
            String imageName = imageNames.get(i);
            for (int j = 0; j < textures.size; j++) {
                TextureAssetData textureAssetData = textures.get(j);
                if (textureAssetData.simpleName().equals(imageName)) {
                    texturesToRemove.add(textureAssetData);
                    break;
                } else {
                    if (j == textures.size - 1) {
                        // texture not found
                        texturesToRemove.clear();
                        message = "can't find " + imageName + " in texture assets";
                    }
                }
            }
        }
        if (texturesToRemove.size == 0) {
            Array<TextureAtlasAssetData> textureAtlases = bundle.getAssets(TextureAtlasAssetData.class);
            // assume image should be loaded from texture atlas
            for (int i = 0; i < textureAtlases.size; i++) {
                TextureAtlasAssetData atlasAssetData = textureAtlases.get(i);
                TextureAtlasData atlasData = atlasAssetData.getAtlasData();
                Array<Region> regions = atlasData.getRegions();
                boolean allFound = true;
                for (int j = 0; j < imageNames.size; j++) {
                    String imageName = imageNames.get(i);
                    boolean found = false;
                    for (int k = 0; k < regions.size; k++) {
                        if (regions.get(k).name.equals(imageName)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        allFound = false;
                        break;
                    }
                }
                if (allFound) {
                    this.atlasAssetData = atlasAssetData;
                    break;
                }
            }
            if (this.atlasAssetData != null) {
                resolved = true;
            } else {
                resolved = false;
                message = "can't find a atlas that has all regions this particle effect need";
            }
        } else {
            textureArray = new Array<TextureAssetData>(false, texturesToRemove.size);
            textureArray.addAll(texturesToRemove);
            resolved = true;
        }
        return resolved;
    }

    @Override
    public String getPrefix(Options options) {
        return options.CodeStyle.PrefixParticle;
    }
}
