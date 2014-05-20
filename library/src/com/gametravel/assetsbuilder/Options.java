/**
 *
 */
package com.gametravel.assetsbuilder;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.gametravel.assetsbuilder.util.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Various options that applies to AssetsBuilder tool.
 *
 * @author Daniel Chow Mar 30, 2014 11:51:42 PM
 */
public class Options {

    public CodeStyle CodeStyle = new CodeStyle();
    public Output Output = new Output();
    public AssetExtensions AssetExtensions = new AssetExtensions();
    public CodeExport CodeExport = new CodeExport();
    public LogOption LogOption = new LogOption();

    /**
     * If {@link com.badlogic.gdx.assets.loaders.resolvers.ResolutionFileResolver} is used, then {@link
     * com.badlogic.gdx.assets.loaders.resolvers.ResolutionFileResolver.Resolution#suffix} should be put in these array
     * so that assets for different resolutions will only be scanned once.
     */
    private Array<String> resolutions = new Array<String>(false, 2);

    /**
     * The default option for AssetsBuilder tool.
     */
    public static final Options DEFAULT = new Options();

    static {
        DEFAULT.toDefault();
    }

    public Options toDefault() {
        Output.setToDefault();
        CodeExport.setToDefault();
        CodeStyle.setToDefault();
        AssetExtensions.setToDefault();
        LogOption.setToDefault();
        return this;
    }

    public boolean isAudio(FileHandle file) {
        return AssetExtensions.AudioExtensions.contains(file.extension(), false);
    }

    public boolean isSound(FileHandle file) {
        return isAudio(file) && Util.getAudioDuration(file) < 5.0;
    }

    public boolean isMusic(FileHandle file) {
        return isAudio(file) && Util.getAudioDuration(file) > 4.9;
    }

    public boolean isTexture(FileHandle file) {
        return AssetExtensions.TextureExtensions.contains(file.extension(), false);
    }

    public boolean isTextureAtlas(FileHandle file) {
        return AssetExtensions.TextureAtlasExtensions.contains(file.extension(), false);
    }

    public boolean isFont(FileHandle file) {
        return AssetExtensions.BitmapFontExtensions.contains(file.extension(), false);
    }

    public boolean isParticle(FileHandle file) {
        return AssetExtensions.ParticleExtensions.contains(file.extension(), false);
    }

    public boolean isPixmap(FileHandle file) {
        return AssetExtensions.PixmapExtensions.contains(file.extension(), false);
    }

    public boolean isSkin(FileHandle file) {
        return AssetExtensions.SkinExtensions.contains(file.extension(), false);
    }

    public boolean isModel(FileHandle file) {
        return AssetExtensions.ModelExtensions.contains(file.extension(), false);
    }

    public boolean isFragmentShader(FileHandle file) {
        return AssetExtensions.FragmentShaderExtensions.contains(file.extension(), false);
    }

    public boolean isVertexShader(FileHandle file) {
        return AssetExtensions.VertexShaderExtensions.contains(file.extension(), false);
    }

    public boolean isSpineJson(FileHandle file) {
        return AssetExtensions.SpineJsonExtensions.contains(file.extension(), false);
    }

    public boolean isSpineBinary(FileHandle file) {
        return AssetExtensions.SpineBinaryExtensions.contains(file.extension(), false);
    }

    /**
     * Returns if the asset file is for a specific resolution, e.g. the path of asset is {@code
     * "graphics/1280x800/game.png"} and {@code "1280x800"} is in the resolution array.
     *
     * @param file The asset file
     * @return {@code true} if the asset file is for a specific resolution, otherwise {@code false}
     */
    public boolean isSizeSpecificAsset(FileHandle file) {
        return resolutions.contains(file.path(), false);
    }

    public boolean save(FileHandle file) {
        Json json = new Json(JsonWriter.OutputType.json);
        String optsJsonStr = json.prettyPrint(this);
        OutputStream outputStream = null;
        try {
            outputStream = file.write(false);
            outputStream.write(optsJsonStr.getBytes("UTF-8"));
            outputStream.flush();
            return true;
        } catch (GdxRuntimeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

    public static Options load(FileHandle file) {
        Json json = new Json(JsonWriter.OutputType.json);
        return json.fromJson(Options.class, file);
    }

    public static void main(String[] args) {
        Options.DEFAULT.save(new FileHandle("E:/AssetBuilder.options"));
        Options options = Options.load(new FileHandle("E:/AssetBuilder.options"));
    }

    public static class LogOption {
        public boolean DEBUG;
        public boolean INFO;
        public boolean WARNING;

        public void setToDefault() {
            DEBUG = true;
            INFO = true;
            WARNING = true;
        }
    }

    public static class CodeExport {
        public String DefaultVertexShaderName;
        public String DefaultFragmentShaderName;
        public boolean ExportTextureRegionToCodeField;
        public boolean ExcludeTextureOfTextureAtlas;
        public boolean ExcludeTextureOfBitmapFont;
        public boolean ExcludeTextureOrRegionOfParticle;
        public boolean ExcludeTextureRegionOfSpine;
        /**
         * When this is {@code true}, the Java code will still be generated if several assets build failed, e.g. BitmapFont
         * can't find corresponding Texture, the failed assets will not be put in generated Java code.
         */
        public boolean TolerateBuildError;

        public void setToDefault() {
            DefaultVertexShaderName = "default";
            DefaultFragmentShaderName = "default";
            ExportTextureRegionToCodeField = true;
            ExcludeTextureOfTextureAtlas = true;
            ExcludeTextureOfBitmapFont = true;
            ExcludeTextureOrRegionOfParticle = true;
            ExcludeTextureRegionOfSpine = true;
            TolerateBuildError = true;
        }
    }

    public static class Output {
        public String PackageName;
        public String ClassName;

        public void setToDefault() {
            PackageName = "com.gametravel.assets.gen";
            ClassName = "AssetBundle";
        }
    }

    public static class AssetExtensions {
        /**
         * Array of extensions of audio files, files with these extensions will be loaded as audios.
         */
        private Array<String> AudioExtensions = new Array<String>(false, 3);

        /**
         * Array of extensions of texture files.
         */
        private Array<String> TextureExtensions = new Array<String>(false, 2);

        /**
         * Array of extensions of texture atlas files.
         */
        private Array<String> TextureAtlasExtensions = new Array<String>(false, 1);

        /**
         * Array of extensions of bitmap font files.
         */
        private Array<String> BitmapFontExtensions = new Array<String>(false, 1);

        /**
         * Array of extensions of particle files.
         */
        private Array<String> ParticleExtensions = new Array<String>(false, 2);

        /**
         * Array of extensions of pixmap files.
         */
        private Array<String> PixmapExtensions = new Array<String>(false, 1);

        /**
         * Array of extensions of skin files.
         */
        private Array<String> SkinExtensions = new Array<String>(false, 1);

        /**
         * Array of extensions of particle files.
         */
        private Array<String> ModelExtensions = new Array<String>(false, 2);

        /**
         * Array of extensions of fragment shader files.
         */
        private Array<String> FragmentShaderExtensions = new Array<String>(false, 1);

        /**
         * Array of extensions of vertex shader files.
         */
        private Array<String> VertexShaderExtensions = new Array<String>(false, 1);

        /**
         * Array of extensions of spine json files.
         */
        private Array<String> SpineJsonExtensions = new Array<String>(false, 1);

        /**
         * Array of extensions of spine binary files.
         */
        private Array<String> SpineBinaryExtensions = new Array<String>(false, 2);

        public void setToDefault() {
            AudioExtensions.clear();
            AudioExtensions.add("mp3");
            AudioExtensions.add("ogg");
            AudioExtensions.add("wav");

            TextureExtensions.clear();
            TextureExtensions.add("png");
            TextureExtensions.add("jpg");

            TextureAtlasExtensions.clear();
            TextureAtlasExtensions.add("atlas");

            BitmapFontExtensions.clear();
            BitmapFontExtensions.add("fnt");

            ParticleExtensions.clear();
            ParticleExtensions.add("particle");
            ParticleExtensions.add("p");

            PixmapExtensions.clear();
            PixmapExtensions.add("pixmap");

            SkinExtensions.clear();
            SkinExtensions.add("skin");

            ModelExtensions.clear();
            ModelExtensions.add("g3dj");

            VertexShaderExtensions.clear();
            VertexShaderExtensions.add("vert");

            FragmentShaderExtensions.clear();
            FragmentShaderExtensions.add("frag");

            SpineJsonExtensions.clear();
            SpineJsonExtensions.add("spj");

            SpineBinaryExtensions.clear();
            SpineBinaryExtensions.add("spb");
            SpineBinaryExtensions.add("skel");
        }
    }

    public static class CodeStyle {
        public boolean AddPrefixInFieldName;
        public String PrefixBitmapFont;
        public String PrefixModel;
        public String PrefixMusic;
        public String PrefixParticle;
        public String PrefixPixmap;
        public String PrefixSkin;
        public String PrefixSound;
        public String PrefixTexture;
        public String PrefixTextureAtlas;
        public String PrefixTextureRegion;
        public String PrefixAnimation;
        public String PrefixShader;
        public String PrefixSpineJsonSkelData;
        public String PrefixSpineBinarySkelData;
        public String PrefixSpineJsonAnimStateData;
        public String PrefixSpineBinaryAnimStateData;

        public void setToDefault() {
            AddPrefixInFieldName = true;
            PrefixBitmapFont = "font";
            PrefixModel = "model";
            PrefixMusic = "music";
            PrefixParticle = "particle";
            PrefixPixmap = "pixmap";
            PrefixSkin = "skin";
            PrefixSound = "sound";
            PrefixTexture = "texture";
            PrefixTextureAtlas = "atlas";
            PrefixTextureRegion = "region";
            PrefixAnimation = "anim";
            PrefixShader = "shader";
            PrefixSpineJsonSkelData = "skelj";
            PrefixSpineBinarySkelData = "skelb";
            PrefixSpineJsonAnimStateData = "anim_statej";
            PrefixSpineBinaryAnimStateData = "anim_stateb";
        }
    }

}