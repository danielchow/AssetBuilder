/**
 * 
 */
package com.gametravel.assetsbuilder.data.manageable;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.data.ManageableAssetData;
import com.gametravel.assetsbuilder.scan.AssetBundle;
import com.gametravel.assetsbuilder.util.Util;

/**
 * @author Daniel Chow Mar 31, 2014 1:11:56 AM
 */
public class ModelAssetData extends ManageableAssetData {

    public ModelAssetData(FileHandle file) {
        super(Model.class, file);
    }

    @Override
    public boolean resolve(AssetBundle bundle, Options options) {
        if (options.LogOption.WARNING) Util.err("ModelAssetData haven't implement resolve()");
        resolved = false;
        return super.resolve(bundle, options);
    }

    @Override
    public String getPrefix(Options options) {
        return options.CodeStyle.PrefixModel;
    }
}
