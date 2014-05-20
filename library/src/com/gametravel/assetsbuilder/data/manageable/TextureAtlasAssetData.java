/**
 * 
 */
package com.gametravel.assetsbuilder.data.manageable;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Region;
import com.badlogic.gdx.utils.Array;
import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.data.CustomAssetData;
import com.gametravel.assetsbuilder.data.ManageableAssetData;
import com.gametravel.assetsbuilder.export.AssetCodeField;
import com.gametravel.assetsbuilder.scan.AssetBundle;
import com.gametravel.assetsbuilder.util.Util;

/**
 * @author Daniel Chow Mar 31, 2014 1:11:56 AM
 */
public class TextureAtlasAssetData extends ManageableAssetData {

    public Array<TextureRegionAssetData> regions;

    public TextureAtlasAssetData(FileHandle file) {
        super(TextureAtlas.class, file);
        split();
    }

    private void split() {
        TextureAtlasData textureAtlasData = new TextureAtlasData(file, file.parent(), false);
        Array<Region> regionArray = textureAtlasData.getRegions();
        regions = new Array<TextureRegionAssetData>(false, regionArray.size);
        for (int i = 0; i < regionArray.size; i++) {
            Region region = regionArray.get(i);
            TextureRegionAssetData textureRegionAssetData = null;
            for (int j = 0; j < regions.size; j++) {
                TextureRegionAssetData data = regions.get(j);
                if (data.name.equalsIgnoreCase(region.name)) {
                    textureRegionAssetData = data;
                    break;
                }
            }
            if (textureRegionAssetData == null) {
                textureRegionAssetData = new TextureRegionAssetData(region.name, 0);
                regions.add(textureRegionAssetData);
            }
            textureRegionAssetData.count++;
        }
    }

    @Override
    public boolean resolve(AssetBundle bundle, Options options) {
        super.resolve(bundle, options);
        Array<TextureAssetData> textures = bundle.getAssets(TextureAssetData.class);
        TextureAtlasData atlasData = getAtlasData();
        Array<TextureAtlasData.Page> pages = atlasData.getPages();
        for (int j = 0; j < pages.size; j++) {
            TextureAtlasData.Page page = pages.get(j);
            String textureName = page.textureFile.nameWithoutExtension();
            boolean found = false;
            for (int k = textures.size - 1; k > -1; k--) {
                if (textures.get(k).simpleName().equals(textureName)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                message = "can't find " + page.textureFile.path();
                return resolved = false;
            }
        }
        return resolved = true;
    }
    
    public TextureAtlasData getAtlasData() {
    	return new TextureAtlasData(file, file.parent(), false);
    }

    @Override
    public String getPrefix(Options options) {
        return options.CodeStyle.PrefixTextureAtlas;
    }

    public class TextureRegionAssetData extends CustomAssetData implements AssetCodeField {
        public String fieldName;
        public String name;
        public int count;

        public TextureRegionAssetData(String name, int count) {
        	super(name, name);
            this.name = name;
            this.count = count;
            fieldName = Util.toValidJavaFieldName(name);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(name);
            builder.append(':').append(count);
            return builder.toString();
        }

        @Override
        public String getFieldName() {
            return fieldName;
        }

        @Override
        public String getPrefix(Options options) {
            return count > 1 ? options.CodeStyle.PrefixAnimation : options.CodeStyle.PrefixTextureRegion;
        }
    }

}
