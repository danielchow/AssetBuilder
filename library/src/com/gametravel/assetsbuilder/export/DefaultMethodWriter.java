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
 * @date 5/11/2014 10:17 PM
 */
public class DefaultMethodWriter implements MethodWriter {

    public String writeMethods(AssetBundle bundle, Options options) {
        StringBuilder builder = new StringBuilder(4096);
        writeMethodCtor(builder, options);
        writeMethodGetInstance(builder, options);
        writeMethodUpdate(builder, bundle, options);
        writeMethodFinishLoading(builder, bundle, options);
        writeMethodLoadAssets(builder, bundle, options);
        writeMethodLoadShaders(builder, bundle, options);
        writeMethodDispose(builder, options);
        writeMethodLoadSpineData(builder, bundle, options);
        writeMethodUpdateFieldReference(builder, bundle, options);
        return builder.toString();
    }

    private void writeMethodCtor(StringBuilder builder, Options options) {
        builder.append("    /**\n");
        builder.append("     * Protected Constructor.\n");
        builder.append("     */\n");
        builder.append("    protected ").append(options.Output.ClassName).append("() {\n");
        builder.append("        assetManager = new AssetManager();\n");
        builder.append("        assetManager.setErrorListener(assetErrorListener);\n");
        builder.append("        Texture.setAssetManager(assetManager);\n");
        builder.append("        loadAssets();\n");
        builder.append("    }\n");
        builder.append('\n');
    }

    private void writeMethodGetInstance(StringBuilder builder, Options options) {
        builder.append("    /**\n");
        builder.append("     * Returns singleton instance of {@link ").append(options.Output.ClassName).append(("}.\n"));
        builder.append("     * @return A singleton instance\n");
        builder.append("     */\n");
        builder.append("    public synchronized static ").append(options.Output.ClassName).append(" getInstance() {\n");
        builder.append("        if (instance == null) {\n");
        builder.append("            instance = new ").append(options.Output.ClassName).append("();\n");
        builder.append("        }\n");
        builder.append("        return instance;\n");
        builder.append("    }\n");
        builder.append('\n');
    }

    private void writeMethodDispose(StringBuilder builder, Options options) {
        builder.append("    @Override\n");
        builder.append("    public void dispose() {\n");
        builder.append("        assetManager.dispose();\n");
        builder.append("        synchronized (").append(options.Output.ClassName).append(".class) {\n");
        builder.append("            instance = null;\n");
        builder.append("        }\n");
        builder.append("    }\n");
        builder.append('\n');
    }

    private void writeMethodLoadAssets(StringBuilder builder, AssetBundle bundle, Options options) {
        builder.append("    /**\n");
        builder.append("     * Initializes asset loading queue of asset manager.\n");
        builder.append("     */\n");
        builder.append("    protected void loadAssets() {\n");
        writeLoadManageableAssets(builder, bundle, TextureAtlasAssetData.class, options);
        builder.append('\n');
        writeLoadManageableAssets(builder, bundle, TextureAssetData.class, options);
        builder.append('\n');
        writeLoadManageableAssets(builder, bundle, SoundAssetData.class, options);
        builder.append('\n');
        writeLoadManageableAssets(builder, bundle, MusicAssetData.class, options);
        builder.append('\n');
        writeLoadManageableAssets(builder, bundle, ParticleEffectAssetData.class, options);
        builder.append('\n');
        writeLoadManageableAssets(builder, bundle, BitmapFontAssetData.class, options);
        builder.append('\n');
        // TODO model, skin, pixmap, polygon region
        builder.append("        // ShaderProgram\n");
        builder.append("        loadShaders();\n");
        builder.append("    }\n");
        builder.append('\n');
    }

    private void writeMethodLoadSpineData(StringBuilder builder, AssetBundle bundle, Options options) {
        Array<SpineJsonAssetData> spineJsonAssets = bundle.getAssets(SpineJsonAssetData.class);
        Array<SpineBinaryAssetData> spineBinaryAssets = bundle.getAssets(SpineBinaryAssetData.class);
        builder.append("    /**\n");
        builder.append("     * Loads all spine skeleton data and animation state data.\n");
        builder.append("     */\n");
        builder.append("    private void loadSpineData() {\n");
        for (int i = 0; i < spineJsonAssets.size; i++) {
            SpineJsonAssetData spineJson = spineJsonAssets.get(i);
            String skelDataFieldName = DefaultFieldWriter.getFieldName(spineJson, options);
            String animStateDataFieldName = options.CodeStyle.AddPrefixInFieldName ? (options.CodeStyle.PrefixSpineJsonAnimStateData
                    + "_" + spineJson.getFieldName()) : spineJson.getFieldName();
            builder.append("        if (").append(skelDataFieldName).append(" == null) {\n");
            builder.append("            SkeletonJson skeletonJson = new SkeletonJson(")
                    .append(DefaultFieldWriter.getFieldName(spineJson.atlasAssetData, options)).append(");\n");
            builder.append("            ").append(skelDataFieldName)
                    .append(" = skeletonJson.readSkeletonData(Gdx.files.internal(\"")
                    .append(spineJson.path).append("\"));\n");
            builder.append("            ").append(animStateDataFieldName).append(" = new AnimationStateData(")
                    .append(skelDataFieldName).append(");\n");
            builder.append("        }\n");
        }
        for (int i = 0; i < spineBinaryAssets.size; i++) {
            SpineBinaryAssetData spineBinary = spineBinaryAssets.get(i);
            String skelDataFieldName = DefaultFieldWriter.getFieldName(spineBinary, options);
            String animStateDataFieldName = options.CodeStyle.AddPrefixInFieldName ? (options.CodeStyle.PrefixSpineBinaryAnimStateData
                    + "_" + spineBinary.getFieldName()) : spineBinary.getFieldName();
            builder.append("        if (").append(skelDataFieldName).append(" == null) {\n");
            builder.append("            SkeletonBinary skeletonBinary = new SkeletonBinary(")
                    .append(DefaultFieldWriter.getFieldName(spineBinary.atlasAssetData, options)).append(");\n");
            builder.append("            ").append(skelDataFieldName)
                    .append(" = skeletonBinary.readSkeletonData(Gdx.files.internal(\"")
                    .append(spineBinary.path).append("\"));\n");
            builder.append("            ").append(animStateDataFieldName).append(" = new AnimationStateData(")
                    .append(skelDataFieldName).append(");\n");
            builder.append("        }\n");
        }
        builder.append("");
        builder.append("    }\n");
        builder.append("\n");
    }

    private void writeMethodLoadShaders(StringBuilder builder, AssetBundle bundle, Options options) {
        builder.append("    /**\n");
        builder.append("     * Loads shader assets.\n");
        builder.append("     */\n");
        builder.append("    protected void loadShaders() {\n");
        builder.append("        ShaderProgram.pedantic = false;\n");
        builder.append("\n");
        Array<ShaderProgramAssetData> shaderAssets = bundle.getAssets(ShaderProgramAssetData.class);
        for (int i = 0; i < shaderAssets.size; i++) {
            ShaderProgramAssetData shader = shaderAssets.get(i);
            String fieldName = DefaultFieldWriter.getFieldName(shader, options);
            builder.append("        ").append(fieldName).append(" = new ShaderProgram(Gdx.files.internal(\"")
                    .append(shader.vertexShaderAssetData.path).append("\"), Gdx.files.internal(\"")
                    .append(shader.fragmentShaderAssetData.path)
                    .append("\"));\n");
            builder.append("        if (!").append(fieldName).append(".isCompiled()) {\n");
            builder.append("            Gdx.app.error(\"").append(fieldName)
                    .append("\", \"compilation failed:\" + ").append(fieldName)
                    .append(".getLog());\n");
            builder.append("        }\n");
        }
        builder.append("    }\n");
        builder.append("\n");
    }

    private <T extends ManageableAssetData> void writeLoadManageableAssets(StringBuilder builder, AssetBundle bundle, Class<T> type, Options options) {
        Array<T> assets = bundle.getAssets(type);
        if (assets == null || assets.size == 0) return;
        boolean appendComment = false;
        for (int i = 0; i < assets.size; i++) {
            T asset = assets.get(i);
            if (!asset.isTagged()) {
                if (!appendComment) {
                    builder.append("        // ").append(assets.get(0).classType.getSimpleName()).append('\n');
                    appendComment = true;
                }
                if (asset instanceof ParticleEffectAssetData && ((ParticleEffectAssetData) asset).atlasAssetData != null) {
                    ParticleEffectAssetData particle = (ParticleEffectAssetData) asset;
                    String paramName = "particleParam_" + particle.fieldName;
                    builder.append("        ParticleEffectLoader.ParticleEffectParameter ").append(paramName)
                            .append(" = new ParticleEffectLoader.ParticleEffectParameter();\n");
                    builder.append("        ").append(paramName).append(".atlasFile = \"")
                            .append(particle.atlasAssetData.path).append("\";\n");
                    builder.append("        assetManager.load(\"").append(asset.path)
                            .append("\", ").append(asset.classType.getSimpleName()).append(".class, ")
                            .append(paramName).append(");\n\n");
                } else {
                    builder.append("        assetManager.load(\"").append(asset.path)
                            .append("\", ").append(asset.classType.getSimpleName()).append(".class);\n");
                }
            }
        }
    }

    private void writeMethodUpdate(StringBuilder builder, AssetBundle bundle, Options options) {
        builder.append("    /**\n");
        builder.append("     * Updates the Assets, keeping it loading any assets in the preload queue.\n");
        builder.append("     * @return true if all loading is finished.\n");
        builder.append("     */\n");
        builder.append("    public boolean update() {\n");
        builder.append("        final boolean result = assetManager.update();\n");
        builder.append("        updateFieldRefs();\n");
        builder.append("        return result;\n");
        builder.append("    }\n");
        builder.append("\n");
    }

    private void writeMethodFinishLoading(StringBuilder builder, AssetBundle bundle, Options options) {
        builder.append("    /**\n");
        builder.append("     * Blocks until all assets are loaded.\n");
        builder.append("     */\n");
        builder.append("    public void finishLoading() {\n");
        builder.append("        assetManager.finishLoading();\n");
        builder.append("        updateFieldRefs();\n");
        builder.append("    }\n");
        builder.append("\n");
    }

    private void writeMethodUpdateFieldReference(StringBuilder builder, AssetBundle bundle, Options options) {
        builder.append("    /**\n");
        builder.append("     * Updates asset references in {@link ").append(options.Output.ClassName).append("}.\n");
        builder.append("     */\n");
        builder.append("    protected void updateFieldRefs() {\n");
        writeUpdateManageableAssetRef(builder, bundle, TextureAtlasAssetData.class, options);
        writeUpdateManageableAssetRef(builder, bundle, TextureAssetData.class, options);
        writeUpdateManageableAssetRef(builder, bundle, SoundAssetData.class, options);
        writeUpdateManageableAssetRef(builder, bundle, MusicAssetData.class, options);
        writeUpdateManageableAssetRef(builder, bundle, ParticleEffectAssetData.class, options);
        writeUpdateManageableAssetRef(builder, bundle, BitmapFontAssetData.class, options);
        builder.append("        loadSpineData();\n");
        builder.append("    }\n");
        builder.append("\n");
    }

    private <T extends ManageableAssetData> void writeUpdateManageableAssetRef(StringBuilder builder, AssetBundle bundle, Class<T> type, Options options) {
        Array<T> assets = bundle.getAssets(type);
        if (assets == null || assets.size == 0) return;
        for (int i = 0; i < assets.size; i++) {
            T asset = assets.get(i);
            if (asset.isTagged()) return;
            String fieldName = DefaultFieldWriter.getFieldName(asset, options);
            String path = asset.path;
            Class clz = asset.classType;
            builder.append("        if (").append(fieldName).append(" == null && assetManager.isLoaded(\"")
                    .append(path).append("\")) {\n");
            builder.append("            ").append(fieldName).append(" = assetManager.get(\"").append(path)
                    .append("\", ").append(clz.getSimpleName()).append(".class);\n");
            if (asset instanceof TextureAtlasAssetData) {
                TextureAtlasAssetData atlas = (TextureAtlasAssetData) asset;
                Array<TextureAtlasAssetData.TextureRegionAssetData> regions = atlas.regions;
                for (int j = 0; j < regions.size; j++) {
                    TextureAtlasAssetData.TextureRegionAssetData region = regions.get(j);
                    if (region.isTagged()) continue;
                    String regionFieldName = DefaultFieldWriter.getFieldName(region, options);
                    int count = region.count;
                    if (count == 1) {
                        builder.append("            ").append(regionFieldName).append(" = ").append(fieldName)
                                .append(".findRegion(\"").append(region.name).append("\");\n");
                    } else {
                        builder.append("            ").append(regionFieldName).append(" = ").append(fieldName)
                                .append(".findRegions(\"").append(region.name).append("\");\n");
                    }
                }
            }
            builder.append("        }\n");
        }
    }

}
