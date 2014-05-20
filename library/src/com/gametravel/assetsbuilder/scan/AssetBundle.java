/**
 * 
 */
package com.gametravel.assetsbuilder.scan;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonJson;
import com.gametravel.assetsbuilder.BuildException;
import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.data.AssetData;
import com.gametravel.assetsbuilder.data.custom.*;
import com.gametravel.assetsbuilder.data.manageable.BitmapFontAssetData;
import com.gametravel.assetsbuilder.data.manageable.ParticleEffectAssetData;
import com.gametravel.assetsbuilder.data.manageable.TextureAssetData;
import com.gametravel.assetsbuilder.data.manageable.TextureAtlasAssetData;
import com.gametravel.assetsbuilder.util.RegionTagAttachmentLoader;
import com.gametravel.assetsbuilder.util.Util;

/**
 * A class that holds various information about assets in a specific location.
 * @author Daniel Chow Mar 30, 2014 11:33:50 PM
 */
public class AssetBundle {
    private FileHandle root;
    private ObjectMap<Class, Array<AssetData>> assetMap = new ObjectMap<Class, Array<AssetData>>(16);

    protected AssetBundle(FileHandle root) {
        if (root == null || !root.isDirectory()) {
            throw new IllegalArgumentException("root must be a folder!");
        }
        this.root = root;
    }

    public FileHandle getRoot() {
        return this.root;
    }

    public <T extends AssetData> Array<T> getAssets(java.lang.Class<T> type) {
        Array<AssetData> assets = assetMap.get(type);
        return assets == null ? new Array<T>() : (Array<T>) assets;
    }

    public void addAsset(AssetData asset) {
        Class type = asset.getClass();
        Array<AssetData> assets = assetMap.get(type);
        if (assets == null) {
            assets = new Array<AssetData>(false, 8);
            assetMap.put(type, assets);
        }
        assets.add(asset);
    }

    protected AssetBundle build(Options options) throws BuildException {
        ObjectMap.Keys<Class> keys = assetMap.keys();
        while (keys.hasNext()) {
            Class type = keys.next();
            Array<AssetData> assets = assetMap.get(type);
            for (int i = 0; i < assets.size; i++) {
                AssetData assetData = assets.get(i);
                if (assetData.resolve(this,options)) {
                    if (options.LogOption.INFO) Util.log(assetData.toString());
                } else {
                    if (options.LogOption.WARNING) Util.err(assetData.toString());
                    if (!options.CodeExport.TolerateBuildError)
                        throw new BuildException("can't resolve " + assetData.toString());
                }
            }
        }
        linkShaders(options);
        if (options.CodeExport.ExcludeTextureOfTextureAtlas) tagTextureOfAtlas(options);
        if (options.CodeExport.ExcludeTextureOfBitmapFont) tagTextureOfBitmapFont(options);
        if (options.CodeExport.ExcludeTextureOrRegionOfParticle) tagTextureOrRegionOfParticle(options);
        if (options.CodeExport.ExcludeTextureRegionOfSpine) tagRegionOfSpine(options);
        return this;
    }

    private void tagTextureOfAtlas(Options options) {
        Array<TextureAtlasAssetData> textureAtlases = getAssets(TextureAtlasAssetData.class);
        Array<TextureAssetData> textures = getAssets(TextureAssetData.class);
        for (int i = 0; i < textureAtlases.size; i++) {
            TextureAtlasAssetData atlasAssetData = textureAtlases.get(i);
            TextureAtlasData atlasData = atlasAssetData.getAtlasData();
            Array<Page> pages = atlasData.getPages();
            for (int j = 0; j < pages.size; j++) {
                Page page = pages.get(j);
                String textureName = page.textureFile.nameWithoutExtension();
                for (int k = textures.size - 1; k > -1; k--) {
                    TextureAssetData texture = textures.get(k);
                    if (texture.simpleName().equals(textureName)) {
                        texture.tag(true, options);
                        break;
                    }
                }
            }
        }
    }

    private void tagTextureOfBitmapFont(Options options) {
        Array<BitmapFontAssetData> bitmapFonts = getAssets(BitmapFontAssetData.class);
        Array<TextureAssetData> textures = getAssets(TextureAssetData.class);
        for (int i = 0; i < bitmapFonts.size; i++) {
            BitmapFontAssetData bitmapFontAssetData = bitmapFonts.get(i);
            BitmapFontData bitmapFontData = bitmapFontAssetData.getBitmapFontData();
            for (int j = 0; j < bitmapFontData.imagePaths.length; j++) {
                String imageName = new FileHandle(bitmapFontData.imagePaths[j]).name();
                for (int k = textures.size - 1; k > -1; k--) {
                    TextureAssetData texture = textures.get(k);
                    if (texture.fullName().equals(imageName)) {
                        texture.tag(true, options);
                        break;
                    }
                }
            }
        }
    }

    private void tagTextureOrRegionOfParticle(Options options) {
        Array<ParticleEffectAssetData> particles = getAssets(ParticleEffectAssetData.class);
        for (int i = 0; i < particles.size; i++) {
            ParticleEffectAssetData particle = particles.get(i);
            Array<String> imageNames = particle.imageNames;
            if (particle.atlasAssetData != null) {
                Array<TextureAtlasAssetData.TextureRegionAssetData> regions = particle.atlasAssetData.regions;
                for (int j = 0; j < regions.size; j++) {
                    TextureAtlasAssetData.TextureRegionAssetData region = regions.get(j);
                    if (imageNames.contains(region.name, false)) {
                        region.tag(true, options);
                    }
                }
            } else {
                Array<TextureAssetData> textures = getAssets(TextureAssetData.class);
                for (int j = 0; j < textures.size; j++) {
                    TextureAssetData texture = textures.get(j);
                    if (imageNames.contains(texture.simpleName(), false)) {
                        texture.tag(true, options);
                    }
                }
            }
        }
    }

    private void tagRegionOfSpine(Options options) {
        Array<SpineBinaryAssetData> spineBinaries = getAssets(SpineBinaryAssetData.class);
        for (int i = 0; i < spineBinaries.size; i++) {
            SpineBinaryAssetData spineBinary = spineBinaries.get(i);
            TextureAtlasAssetData atlasAssetData = spineBinary.atlasAssetData;
            if (atlasAssetData == null) return;
            try {
                RegionTagAttachmentLoader loader = new RegionTagAttachmentLoader(atlasAssetData, options);
                SkeletonBinary skeletonBinary = new SkeletonBinary(loader);
                skeletonBinary.readSkeletonData(spineBinary.file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Array<SpineJsonAssetData> spineJsons = getAssets(SpineJsonAssetData.class);
        for (int i = 0; i < spineJsons.size; i++) {
            SpineJsonAssetData spineJson = spineJsons.get(i);
            TextureAtlasAssetData atlasAssetData = spineJson.atlasAssetData;
            if (atlasAssetData == null) return;
            try {
                RegionTagAttachmentLoader loader = new RegionTagAttachmentLoader(atlasAssetData, options);
                SkeletonJson skeletonJson = new SkeletonJson(loader);
                skeletonJson.readSkeletonData(spineJson.file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Link Vertex Shader and Fragment Shader to a ShaderProgram, based on options.
     * @param options The options
     */
    private void linkShaders(Options options) {
        Array<VertexShaderAssetData> vertexShaders = new Array<VertexShaderAssetData>(getAssets(VertexShaderAssetData.class));
        Array<FragmentShaderAssetData> fragmentShaders = new Array<FragmentShaderAssetData>(getAssets(FragmentShaderAssetData.class));
        VertexShaderAssetData defaultVertexShader = null;
        FragmentShaderAssetData defaultFragmentShader = null;
        String defaultVertexShaderName = options.CodeExport.DefaultVertexShaderName;
        String defaultFragmentShaderName = options.CodeExport.DefaultFragmentShaderName;
        // find default vertex shader file
        for (int i = 0; i < vertexShaders.size; i++) {
            VertexShaderAssetData vertexShader = vertexShaders.get(i);
            String fileName = vertexShader.file.nameWithoutExtension();
            if (defaultVertexShaderName.equalsIgnoreCase(fileName)) {
                defaultVertexShader = vertexShader;
                break;
            }
        }
        // find default fragment shader file
        for (int i = 0; i < fragmentShaders.size; i++) {
            FragmentShaderAssetData fragmentShader = fragmentShaders.get(i);
            String fileName = fragmentShader.file.nameWithoutExtension();
            if (defaultFragmentShaderName.equalsIgnoreCase(fileName)) {
                defaultFragmentShader = fragmentShader;
                break;
            }
        }
        for (int i = vertexShaders.size - 1; i > -1; i--) {
            VertexShaderAssetData vertexShader = vertexShaders.get(i);
            String nameWithoutExtension = vertexShader.file.nameWithoutExtension();
            ShaderProgramAssetData shader = null;
            String fieldName = Util.toValidJavaFieldName(nameWithoutExtension);
            for (int j = fragmentShaders.size - 1; j > -1; j--) {
                FragmentShaderAssetData fragmentShader = fragmentShaders.get(j);
                if (nameWithoutExtension.equalsIgnoreCase(fragmentShader.file
                        .nameWithoutExtension())) {
                    shader = new ShaderProgramAssetData(fieldName, vertexShader, fragmentShader);
                    if (!defaultVertexShaderName.equalsIgnoreCase(nameWithoutExtension)) {
                        vertexShaders.removeIndex(i);
                    }
                    if (!defaultFragmentShaderName.equalsIgnoreCase(nameWithoutExtension)) {
                        fragmentShaders.removeIndex(j);
                    }
                    break;
                }
            }
            if (shader == null && vertexShader != defaultVertexShader) {
                if (defaultFragmentShader == null) {
                    throw new IllegalStateException(vertexShader
                            + " : vertex shader has no corresponding fragment shader");
                } else {
                    shader = new ShaderProgramAssetData(fieldName, vertexShader,
                            defaultFragmentShader);
                    if (!defaultVertexShaderName.equalsIgnoreCase(nameWithoutExtension)) {
                        vertexShaders.removeIndex(i);
                    }
                }
            }
            if (shader != null) addAsset(shader);
        }
        if (defaultVertexShader == null
                && fragmentShaders.size > (defaultFragmentShader == null ? 0 : 1)) {
            throw new IllegalStateException(fragmentShaders
                    + " : fragment shader has no corresponding vertex shader");
        }
        for (int i = fragmentShaders.size - 1; i > -1; i--) {
            FragmentShaderAssetData fragmentShader = fragmentShaders.get(i);
            if (fragmentShader == defaultFragmentShader) continue;
            String nameWithoutExtension = fragmentShader.file.nameWithoutExtension();
            String fieldName = Util.toValidJavaFieldName(nameWithoutExtension);
            addAsset(new ShaderProgramAssetData(fieldName, defaultVertexShader, fragmentShader));
            fragmentShaders.removeIndex(i);
        }

        if (options.LogOption.DEBUG) {
            Array<ShaderProgramAssetData> shaders = getAssets(ShaderProgramAssetData.class);
//            if (shaders != null) {
                for (int i = 0; i < shaders.size; i++) {
                    Util.log(shaders.get(i).toString());
                }
//            }
        }

    }

}