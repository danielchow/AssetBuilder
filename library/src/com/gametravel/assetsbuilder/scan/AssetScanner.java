/**
 * 
 */
package com.gametravel.assetsbuilder.scan;

import com.badlogic.gdx.files.FileHandle;
import com.gametravel.assetsbuilder.BuildException;
import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.data.custom.FragmentShaderAssetData;
import com.gametravel.assetsbuilder.data.custom.SpineBinaryAssetData;
import com.gametravel.assetsbuilder.data.custom.SpineJsonAssetData;
import com.gametravel.assetsbuilder.data.custom.VertexShaderAssetData;
import com.gametravel.assetsbuilder.data.manageable.BitmapFontAssetData;
import com.gametravel.assetsbuilder.data.manageable.ModelAssetData;
import com.gametravel.assetsbuilder.data.manageable.MusicAssetData;
import com.gametravel.assetsbuilder.data.manageable.ParticleEffectAssetData;
import com.gametravel.assetsbuilder.data.manageable.PixmapAssetData;
import com.gametravel.assetsbuilder.data.manageable.SkinAssetData;
import com.gametravel.assetsbuilder.data.manageable.SoundAssetData;
import com.gametravel.assetsbuilder.data.manageable.TextureAssetData;
import com.gametravel.assetsbuilder.data.manageable.TextureAtlasAssetData;
import com.gametravel.assetsbuilder.util.Util;

/**
 * AssetScanner is used for scanning all assets under a specific directory, scanning rule is based
 * on the specified {@link Options}. After scan completed, an {@link com.gametravel.assetsbuilder.scan.AssetBundle} is created, which
 * can be used to automatically generate asset-managing Java code.
 * <p>
 * Supported assets type can be found in {@link com.gametravel.assetsbuilder.data.AssetType}.
 * @author Daniel Chow Mar 23, 2014 12:31:15 AM
 */
public class AssetScanner {

	private Options options;

	/**
	 * Create an {@link com.gametravel.assetsbuilder.scan.AssetScanner} with the {@link Options#DEFAULT} option.
	 */
	public AssetScanner() {
		this(Options.DEFAULT);
	}

	/**
	 * Create an {@link com.gametravel.assetsbuilder.scan.AssetScanner} with the specified options.
	 * @param opts The option applied to AssetsBuilder tool
	 */
	public AssetScanner(Options opts) {
		this.options = opts;
	}

	public AssetBundle scanPath(FileHandle root) throws BuildException {
		if (root == null || !root.isDirectory()) {
			throw new IllegalArgumentException("root must be a folder!");
		}
		AssetBundle output = new AssetBundle(root);
		scanFolder(root, output);
		return output.build(options);
	}

	private void scanFolder(FileHandle folder, AssetBundle output) {
		if (folder == null || !folder.isDirectory() || !folder.exists()) {
            if (options.LogOption.WARNING) Util.err("invalid file when scanning folder!");
            return;
		}
		FileHandle[] listedFiles = folder.list();
		if (listedFiles == null || listedFiles.length < 1) {
			return;
		}
        for (FileHandle file : listedFiles) {
            if (options.isSizeSpecificAsset(file)) {
                continue;
            } else if (file.isDirectory()) {
                scanFolder(file, output);
            } else {
                // classify assets
                if (options.isAudio(file)) {
                    if (options.isSound(file)) {
                        if (options.LogOption.DEBUG) Util.log("Sound           : " + Util.getRelativePath(file));
                        output.addAsset(new SoundAssetData(file));
                    } else {
                        if (options.LogOption.DEBUG) Util.log("Music           : " + Util.getRelativePath(file));
                        output.addAsset(new MusicAssetData(file));
                    }
                } else if (options.isParticle(file)) {
                    if (options.LogOption.DEBUG) Util.log("Particle        : " + Util.getRelativePath(file));
                    output.addAsset(new ParticleEffectAssetData(file));
                } else if (options.isTexture(file)) {
                    if (options.LogOption.DEBUG) Util.log("Texture         : " + Util.getRelativePath(file));
                    output.addAsset(new TextureAssetData(file));
                } else if (options.isTextureAtlas(file)) {
                    if (options.LogOption.DEBUG) Util.log("TextureAtlas    : " + Util.getRelativePath(file));
                    output.addAsset(new TextureAtlasAssetData(file));
                } else if (options.isFont(file)) {
                    if (options.LogOption.DEBUG) Util.log("BitmapFont      : " + Util.getRelativePath(file));
                    output.addAsset(new BitmapFontAssetData(file));
                } else if (options.isPixmap(file)) {
                    if (options.LogOption.DEBUG) Util.log("Pixmap          : " + Util.getRelativePath(file));
                    output.addAsset(new PixmapAssetData(file));
                } else if (options.isSkin(file)) {
                    if (options.LogOption.DEBUG) Util.log("Skin            : " + Util.getRelativePath(file));
                    output.addAsset(new SkinAssetData(file));
                } else if (options.isModel(file)) {
                    if (options.LogOption.DEBUG) Util.log("Model           : " + Util.getRelativePath(file));
                    output.addAsset(new ModelAssetData(file));
                } else if (options.isFragmentShader(file)) {
                    if (options.LogOption.DEBUG) Util.log("FragmentShader  : " + Util.getRelativePath(file));
                    output.addAsset(new FragmentShaderAssetData(file));
                } else if (options.isVertexShader(file)) {
                    if (options.LogOption.DEBUG) Util.log("VertexShader    : " + Util.getRelativePath(file));
                    output.addAsset(new VertexShaderAssetData(file));
                } else if (options.isSpineJson(file)) {
                    if (options.LogOption.DEBUG) Util.log("SpineJson       : " + Util.getRelativePath(file));
                    output.addAsset(new SpineJsonAssetData(file));
                } else if (options.isSpineBinary(file)) {
                    if (options.LogOption.DEBUG) Util.log("SpineBinary     : " + Util.getRelativePath(file));
                    output.addAsset(new SpineBinaryAssetData(file));
                } else {
                    if (options.LogOption.WARNING) Util.err("Unknown Asset   : " + Util.getRelativePath(file));
                }
            }
        }
	}

	public static void main(String[] args) throws BuildException {
		AssetScanner scanner = new AssetScanner();
		FileHandle file1 = new FileHandle("E:\\repos\\BoxClear\\BoxClear-android\\assets");
		FileHandle file2 = new FileHandle("E:\\workspace\\ThiefD-android\\assets");
		scanner.scanPath(file1);
	}
}
