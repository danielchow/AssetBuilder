/**
 * 
 */
package com.gametravel.assetsbuilder.data.manageable;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.utils.Array;
import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.data.ManageableAssetData;
import com.gametravel.assetsbuilder.scan.AssetBundle;

/**
 * @author Daniel Chow Mar 31, 2014 1:11:56 AM
 */
public class BitmapFontAssetData extends ManageableAssetData {

    private BitmapFontData bitmapFontData;

    public BitmapFontAssetData(FileHandle file) {
        super(BitmapFont.class, file);
    }

    public BitmapFontData getBitmapFontData() {
        if (bitmapFontData == null) bitmapFontData = new BitmapFontData(file, false);
        return bitmapFontData;
    }

    @Override
    public boolean resolve(AssetBundle bundle, Options options) {
        super.resolve(bundle, options);
        BitmapFontData bitmapFontData = getBitmapFontData();
        Array<TextureAssetData> textures = bundle.getAssets(TextureAssetData.class);
        for (int j = 0; j < bitmapFontData.imagePaths.length; j++) {
            String imageName = new FileHandle(bitmapFontData.imagePaths[j]).name();
            boolean found = false;
            for (int k = textures.size - 1; k > -1; k--) {
                if (textures.get(k).fullName().equals(imageName)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                message = "can't found " + bitmapFontData.imagePaths[j];
                return resolved = false;
            }
        }
        return resolved = true;
    }

    @Override
    public String getPrefix(Options options) {
        return options.CodeStyle.PrefixBitmapFont;
    }
}
