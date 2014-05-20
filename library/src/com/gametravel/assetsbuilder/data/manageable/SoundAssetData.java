/**
 * 
 */
package com.gametravel.assetsbuilder.data.manageable;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.data.ManageableAssetData;
import com.gametravel.assetsbuilder.scan.AssetBundle;

/**
 * @author Daniel Chow Mar 31, 2014 1:11:56 AM
 */
public class SoundAssetData extends ManageableAssetData {

    public SoundAssetData(FileHandle file) {
        super(Sound.class, file);
    }

    @Override
    public boolean resolve(AssetBundle bundle, Options options) {
        super.resolve(bundle, options);
        return resolved = true;
    }

    @Override
    public String getPrefix(Options options) {
        return options.CodeStyle.PrefixSound;
    }
}
