/**
 * 
 */
package com.gametravel.assetsbuilder.data.custom;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;
import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.data.CustomAssetData;
import com.gametravel.assetsbuilder.data.manageable.TextureAtlasAssetData;
import com.gametravel.assetsbuilder.export.AssetCodeField;
import com.gametravel.assetsbuilder.scan.AssetBundle;
import com.gametravel.assetsbuilder.util.AtlasDataAttachmentLoader;
import com.gametravel.assetsbuilder.util.Util;

/**
 * @author Daniel Chow Mar 31, 2014 1:27:25 AM
 */
public class SpineBinaryAssetData extends CustomAssetData implements AssetCodeField {

    public FileHandle file;
    public TextureAtlasAssetData atlasAssetData;
    public String fieldName;
    public String path;

    public SpineBinaryAssetData(FileHandle file) {
        super(file.name(), file.nameWithoutExtension());
        this.file = file;
        this.path = Util.getRelativePath(file);
        fieldName = Util.toValidJavaFieldName(file.nameWithoutExtension());
    }

    @Override
    public boolean resolve(AssetBundle bundle, Options options) {
        super.resolve(bundle, options);
        Array<TextureAtlasAssetData> textureAtlases = bundle.getAssets(TextureAtlasAssetData.class);
        for (int i = 0; i < textureAtlases.size; i++) {
            TextureAtlasAssetData atlasAssetData = textureAtlases.get(i);
            String atlasAssetName = atlasAssetData.simpleName();
            if (atlasAssetName.equals(simpleName)) {
                if (matchAtlasAsset(atlasAssetData, options)) {
                    return resolved = true;
                } else {
                    break;
                }
            }
        }
        for (int i = 0; i < textureAtlases.size; i++) {
            TextureAtlasAssetData atlasAssetData = textureAtlases.get(i);
            String atlasAssetName = atlasAssetData.simpleName();
            if (atlasAssetName.contains(simpleName) || simpleName.contains(atlasAssetName)) {
                if (matchAtlasAsset(atlasAssetData, options)) {
                    return resolved = true;
                }
            }
        }
        for (int i = 0; i < textureAtlases.size; i++) {
            TextureAtlasAssetData atlasAssetData = textureAtlases.get(i);
            if (matchAtlasAsset(atlasAssetData, options)) break;
        }
        return resolved = (this.atlasAssetData != null);
    }

    private boolean matchAtlasAsset(TextureAtlasAssetData atlasAssetData, Options options) {
        if (options.LogOption.DEBUG) {
            Util.log(simpleName() + " matching atlas asset: " + atlasAssetData.simpleName());
        }
        try {
            AtlasDataAttachmentLoader loader = new AtlasDataAttachmentLoader(
                    atlasAssetData.getAtlasData());
            SkeletonBinary skeletonBinary = new SkeletonBinary(loader);
            SkeletonData skeletonData = skeletonBinary.readSkeletonData(file);
            if (options.LogOption.DEBUG) Util.log(skeletonData.getAnimations().toString());
            this.atlasAssetData = atlasAssetData;
            if (options.LogOption.DEBUG) {
                Util.log(simpleName() + " matched atlas asset: "
                        + atlasAssetData.simpleName());
            }
            resolved = true;
            return true;
        } catch (Exception e) {
            message = e.getMessage();
            if (options.LogOption.DEBUG) {
                System.out.println(e.getMessage());
                Util.log(simpleName() + " not matched atlas asset: "
                        + atlasAssetData.simpleName());
            }
            return false;
        }
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getPrefix(Options options) {
        return options.CodeStyle.PrefixSpineBinarySkelData;
    }
}
