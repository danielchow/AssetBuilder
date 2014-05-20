package com.gametravel.assetsbuilder.export;

import com.badlogic.gdx.utils.Array;
import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.data.ManageableAssetData;
import com.gametravel.assetsbuilder.data.custom.ShaderProgramAssetData;
import com.gametravel.assetsbuilder.data.custom.SpineBinaryAssetData;
import com.gametravel.assetsbuilder.data.custom.SpineJsonAssetData;
import com.gametravel.assetsbuilder.data.manageable.*;
import com.gametravel.assetsbuilder.scan.AssetBundle;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Daniel Chow
 * @date 5/11/2014 9:46 PM
 */
public class DefaultFieldWriter implements FieldWriter {

    @Override
    public String writeFields(AssetBundle bundle, Options options) {
        StringBuilder builder = new StringBuilder(4096);
        writeFieldManageableAsset(builder, bundle, TextureAtlasAssetData.class, options);
        writeFieldManageableAsset(builder, bundle, TextureAssetData.class, options);
        writeFieldManageableAsset(builder, bundle, SoundAssetData.class, options);
        writeFieldManageableAsset(builder, bundle, MusicAssetData.class, options);
        writeFieldManageableAsset(builder, bundle, ParticleEffectAssetData.class, options);
        writeFieldManageableAsset(builder, bundle, BitmapFontAssetData.class, options);
        writeFieldTextureRegion(builder, bundle, options);
        writeFieldShader(builder, bundle, options);
        writeFieldSpine(builder, bundle, options);
        // TODO model, skin, pixmap
        writeFieldFixed(builder, options);
        return builder.toString();
    }

    private void writeFieldFixed(StringBuilder builder, Options options) {
        builder.append("    /** AssetManager instance. */\n");
        builder.append("    protected AssetManager assetManager;\n");
        builder.append("\n");
        builder.append("    /** Listener for asset error. */\n");
        builder.append("    protected AssetErrorListener assetErrorListener = new AssetErrorListener() {\n");
        builder.append("\n");
        builder.append("        @Override\n");
        builder.append("        public void error(@SuppressWarnings(\"rawtypes\") AssetDescriptor asset, Throwable throwable) {\n");
        builder.append("            Gdx.app.error(\"Assets\", \"couldn't load asset '\" + asset.fileName + \"'\",\n");
        builder.append("                   (Exception) throwable);\n");
        builder.append("        }\n");
        builder.append("\n");
        builder.append("    };\n");
        builder.append("\n");
        builder.append("    /** Singleton */\n");
        builder.append("    protected static ").append(options.Output.ClassName).append(" instance;\n");
        builder.append("\n");
    }

    private void writeFieldShader(StringBuilder builder, AssetBundle bundle, Options options) {
        Array<ShaderProgramAssetData> shaderAssets = bundle.getAssets(ShaderProgramAssetData.class);
        if (shaderAssets == null || shaderAssets.size == 0) return;
        boolean appendComment = false;
        for (int i = 0; i < shaderAssets.size; i++) {
            if (!appendComment) {
                builder.append("    // ShaderProgram\n");
                appendComment = true;
            }
            ShaderProgramAssetData shader = shaderAssets.get(i);
            builder.append("    public ShaderProgram ").append(getFieldName(shader, options)).append(";\n");
        }
        builder.append('\n');
    }

    private void writeFieldSpine(StringBuilder builder, AssetBundle bundle, Options options) {
        // Spine Json Format & Spine Binary Format
        Array<SpineJsonAssetData> spineJsonAssets = bundle.getAssets(SpineJsonAssetData.class);
        Array<SpineBinaryAssetData> spineBinaryAssets = bundle.getAssets(SpineBinaryAssetData.class);
        if ((spineJsonAssets == null || spineJsonAssets.size == 0)
                && (spineBinaryAssets == null || spineBinaryAssets.size == 0)) return;
        builder.append("    // Spine Skeleton Data\n");
        if (spineJsonAssets != null) {
            for (int i = 0; i < spineJsonAssets.size; i++) {
                SpineJsonAssetData spineJson = spineJsonAssets.get(i);
                builder.append("    public SkeletonData ").append(getFieldName(spineJson, options)).append(";\n");
            }
        }
        if (spineBinaryAssets != null) {
            for (int i = 0; i < spineBinaryAssets.size; i++) {
                SpineBinaryAssetData spineBinary = spineBinaryAssets.get(i);
                builder.append("    public SkeletonData ").append(getFieldName(spineBinary, options)).append(";\n");
            }
        }
        builder.append("\n    // Spine Animation State Data\n");
        if (spineJsonAssets != null) {
            for (int i = 0; i < spineJsonAssets.size; i++) {
                SpineJsonAssetData spineJson = spineJsonAssets.get(i);
                String fieldName = options.CodeStyle.AddPrefixInFieldName ? (options.CodeStyle.PrefixSpineJsonAnimStateData
                        + "_" + spineJson.getFieldName()) : spineJson.getFieldName();
                builder.append("    public AnimationStateData ").append(fieldName).append(";\n");
            }
        }
        if (spineBinaryAssets != null) {
            for (int i = 0; i < spineBinaryAssets.size; i++) {
                SpineBinaryAssetData spineBinary = spineBinaryAssets.get(i);
                String fieldName = options.CodeStyle.AddPrefixInFieldName ? (options.CodeStyle.PrefixSpineBinaryAnimStateData
                        + "_" + spineBinary.getFieldName()) : spineBinary.getFieldName();
                builder.append("    public AnimationStateData ").append(fieldName).append(";\n");
            }
        }
        builder.append('\n');
    }

    private void writeFieldTextureRegion(StringBuilder builder, AssetBundle bundle, Options options) {
        Array<TextureAtlasAssetData> atlasAssets = bundle.getAssets(TextureAtlasAssetData.class);
        if (atlasAssets == null || atlasAssets.size == 0) return;
        boolean appendComment = false;
        for (int i = 0; i < atlasAssets.size; i++) {
            if (!appendComment) {
                builder.append("    // TextureRegion\n");
                appendComment = true;
            }
            TextureAtlasAssetData atlasAssetData = atlasAssets.get(i);
            Array<TextureAtlasAssetData.TextureRegionAssetData> regions = atlasAssetData.regions;
            for (int j = 0; j < regions.size; j++) {
                TextureAtlasAssetData.TextureRegionAssetData region = regions.get(j);
                if (!region.isTagged()) {
                    String fieldName = getFieldName(region, options);
                    if (region.count == 1) {
                        builder.append("    public TextureRegion ").append(fieldName).append(";\n");
                    } else {
                        builder.append("    public Array<TextureAtlas.AtlasRegion> ")
                                .append(fieldName).append(";\n");
                    }

                }
            }
        }
        builder.append('\n');
    }

    private <T extends ManageableAssetData> void writeFieldManageableAsset(StringBuilder builder, AssetBundle bundle, Class<T> type, Options options) {
        Array<T> assets = bundle.getAssets(type);
        if (assets == null || assets.size == 0) return;
        boolean appendComment = false;
        for (int i = 0; i < assets.size; i++) {
            T asset = assets.get(i);
            if (!asset.isTagged()) {
                if (!appendComment) {
                    builder.append("    // ").append(assets.get(i).classType.getSimpleName()).append("\n");
                    appendComment = true;
                }
                builder.append("	public ").append(asset.classType.getSimpleName())
                        .append(" ").append(getFieldName(asset, options)).append(";\n");
            }
        }
        builder.append('\n');
    }

    public static String getFieldName(AssetCodeField field, Options options) {
        if (!options.CodeStyle.AddPrefixInFieldName) return field.getFieldName();
        return field.getPrefix(options) + "_" + field.getFieldName();
    }
}
