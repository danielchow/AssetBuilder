package com.gametravel.assetsbuilder.util;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.attachments.*;
import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.data.manageable.TextureAtlasAssetData;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Daniel Chow
 * @date 5/10/2014 4:47 PM
 */
public class RegionTagAttachmentLoader implements AttachmentLoader {
    private TextureAtlasAssetData atlasAssetData;
    private Options options;

    public RegionTagAttachmentLoader(TextureAtlasAssetData atlasAssetData, Options options) {
        this.options = options;
        setAtlasAssetData(atlasAssetData);
    }

    public void setAtlasAssetData(TextureAtlasAssetData atlasAssetData) {
        if (atlasAssetData == null) throw new IllegalArgumentException("atlas cannot be null.");
        this.atlasAssetData = atlasAssetData;
    }

    public RegionAttachment newRegionAttachment(Skin skin, String name, String path) {
        RegionAttachment attachment = new RegionAttachment(name);
        attachment.setPath(path);
        TextureAtlasAssetData.TextureRegionAssetData region = findRegion(atlasAssetData, path);
        if (region == null) {
            throw new RuntimeException("Region not found in atlas: " + attachment
                    + " (region attachment: " + name + ")");
        } else {
            region.tag(true, options);
        }
        return attachment;
    }

    public MeshAttachment newMeshAttachment(Skin skin, String name, String path) {
        MeshAttachment attachment = new MeshAttachment(name);
        attachment.setPath(path);
        TextureAtlasAssetData.TextureRegionAssetData region = findRegion(atlasAssetData, path);
        if (region == null) {
            throw new RuntimeException("Region not found in atlas: " + attachment
                    + " (mesh attachment: " + name + ")");
        } else {
            region.tag(true, options);
        }
        return attachment;
    }

    @Override
    public SkinnedMeshAttachment newSkinnedMeshAttachment(Skin skin, String name, String path) {
        SkinnedMeshAttachment attachment = new SkinnedMeshAttachment(name);
        attachment.setPath(path);
        TextureAtlasAssetData.TextureRegionAssetData region = findRegion(atlasAssetData, path);
        if (region == null) {
            throw new RuntimeException("Region not found in atlas, path: " + path + " (skinned mesh attachment: " + name + ")");
        } else {
            region.tag(true, options);
        }
        return attachment;
    }

    public BoundingBoxAttachment newBoundingBoxAttachment(Skin skin, String name) {
        return new BoundingBoxAttachment(name);
    }

    private TextureAtlasAssetData.TextureRegionAssetData findRegion(TextureAtlasAssetData atlasAssetData, String name) {
        Array<TextureAtlasAssetData.TextureRegionAssetData> regions = atlasAssetData.regions;
        for (int i = 0, n = regions.size; i < n; i++)
            if (regions.get(i).name.equals(name)) return regions.get(i);
        return null;
    }

}
