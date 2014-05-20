/**
 * 
 */
package com.gametravel.assetsbuilder.data.custom;

import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.data.CustomAssetData;
import com.gametravel.assetsbuilder.export.AssetCodeField;
import com.gametravel.assetsbuilder.scan.AssetBundle;

/**
 * @author Daniel Chow Mar 31, 2014 1:26:46 AM
 */
public class ShaderProgramAssetData extends CustomAssetData implements AssetCodeField {
    public String fieldName;
    public VertexShaderAssetData vertexShaderAssetData;
    public FragmentShaderAssetData fragmentShaderAssetData;

    public ShaderProgramAssetData(String fieldName, VertexShaderAssetData vert,
            FragmentShaderAssetData frag) {
    	super(fieldName, fieldName);
        this.fieldName = fieldName;
        vertexShaderAssetData = vert;
        fragmentShaderAssetData = frag;
    }

    @Override
    public boolean resolve(AssetBundle bundle, Options options) {
        resolved = true;
        super.resolve(bundle, options);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(vertexShaderAssetData.toString());
        return builder.append(" : ").append(fragmentShaderAssetData).toString();
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getPrefix(Options options) {
        return options.CodeStyle.PrefixShader;
    }
}
