/**
 * 
 */
package com.gametravel.assetsbuilder.data.custom;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.data.CustomAssetData;
import com.gametravel.assetsbuilder.scan.AssetBundle;
import com.gametravel.assetsbuilder.util.Util;

/**
 * @author Daniel Chow Mar 31, 2014 1:26:46 AM
 */
public class FragmentShaderAssetData extends CustomAssetData {
    public String fieldName;
    public FileHandle file;
    public String path;
    
    public FragmentShaderAssetData(FileHandle file) {
    	super(file.name(), file.nameWithoutExtension());
        this.file = file;
        this.path = Util.getRelativePath(file);
        fieldName = Util.toValidJavaFieldName(path);
    }

    @Override
    public boolean resolve(AssetBundle bundle, Options options) {
        super.resolve(bundle, options);
        Array<VertexShaderAssetData> vertexShaders = new Array<VertexShaderAssetData>(bundle.getAssets(VertexShaderAssetData.class));
        String defaultVertexShaderName = options.CodeExport.DefaultVertexShaderName;
        String fragmentShaderFileName = file.nameWithoutExtension();
        for (int i = 0; i < vertexShaders.size; i++) {
            VertexShaderAssetData vertexShader = vertexShaders.get(i);
            String fileName = vertexShader.file.nameWithoutExtension();
            if (defaultVertexShaderName.equalsIgnoreCase(fileName) || fragmentShaderFileName.equalsIgnoreCase(fileName)) {
                return resolved = true;
            }
        }
        message = "can't find vertex shader asset with same name or default vertex shader";
        return resolved = false;
    }
}
