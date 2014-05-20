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
public class VertexShaderAssetData extends CustomAssetData {
    public String fieldName;
    public FileHandle file;
    public String path;
    
    public VertexShaderAssetData(FileHandle file) {
    	super(file.name(), file.nameWithoutExtension());
        this.file = file;
        this.path = Util.getRelativePath(file);
        fieldName = Util.toValidJavaFieldName(path);
    }

    @Override
    public boolean resolve(AssetBundle bundle, Options options) {
        super.resolve(bundle, options);
        Array<FragmentShaderAssetData> fragmentShaders = new Array<FragmentShaderAssetData>(bundle.getAssets(FragmentShaderAssetData.class));
        String defaultFragmentShaderName = options.CodeExport.DefaultFragmentShaderName;
        String vertexShaderFileName = file.nameWithoutExtension();
        for (int i = 0; i < fragmentShaders.size; i++) {
            FragmentShaderAssetData fragmentShader = fragmentShaders.get(i);
            String fileName = fragmentShader.file.nameWithoutExtension();
            if (defaultFragmentShaderName.equalsIgnoreCase(fileName) || vertexShaderFileName.equalsIgnoreCase(fileName)) {
                return resolved = true;
            }
        }
        message = "can't find fragment shader asset with same name or default fragment shader";
        return resolved = false;
    }
}
