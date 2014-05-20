/**
 * 
 */
package com.gametravel.assetsbuilder.data;

import com.badlogic.gdx.files.FileHandle;
import com.gametravel.assetsbuilder.export.AssetCodeField;
import com.gametravel.assetsbuilder.util.Util;

/**
 * Data of those assets that can be managed by {@link com.badlogic.gdx.assets.AssetManager}.
 * @author Daniel Chow Mar 31, 2014 12:51:58 AM
 */
@SuppressWarnings("rawtypes")
public abstract class ManageableAssetData extends BaseAssetData implements AssetCodeField {

    public Class classType;
    public FileHandle file;
    public String path;
    public String fieldName;

    public ManageableAssetData(Class classType, FileHandle file) {
    	super(file.name(), file.nameWithoutExtension());
        this.classType = classType;
        this.file = file;
        this.path = Util.getRelativePath(file);
        fieldName = Util.toValidJavaFieldName(file.nameWithoutExtension());
    }

    public Class getClassType() {
        return classType;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
