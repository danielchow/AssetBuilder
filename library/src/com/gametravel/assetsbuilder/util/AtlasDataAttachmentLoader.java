package com.gametravel.assetsbuilder.util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Region;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.attachments.*;

public class AtlasDataAttachmentLoader implements AttachmentLoader {

	private TextureAtlasData atlasData;

	public AtlasDataAttachmentLoader(TextureAtlasData atlasData) {
		setAtlasData(atlasData);
	}

	public void setAtlasData(TextureAtlasData atlas) {
		if (atlas == null) throw new IllegalArgumentException("atlas cannot be null.");
		this.atlasData = atlas;
	}

	public RegionAttachment newRegionAttachment(Skin skin, String name, String path) {
		RegionAttachment attachment = new RegionAttachment(name);
		attachment.setPath(path);
		Region region = findRegion(atlasData, path);
		if (region == null)
			throw new RuntimeException("Region not found in atlas: " + attachment
					+ " (region attachment: " + name + ")");
		return attachment;
	}

	public MeshAttachment newMeshAttachment(Skin skin, String name, String path) {
		MeshAttachment attachment = new MeshAttachment(name);
		attachment.setPath(path);
		Region region = findRegion(atlasData, path);
		if (region == null)
			throw new RuntimeException("Region not found in atlas: " + attachment
					+ " (mesh attachment: " + name + ")");
		return attachment;
	}

    @Override
    public SkinnedMeshAttachment newSkinnedMeshAttachment(Skin skin, String name, String path) {
        SkinnedMeshAttachment attachment = new SkinnedMeshAttachment(name);
        attachment.setPath(path);
        Region region = findRegion(atlasData, path);
        if (region == null) {
            throw new RuntimeException("Region not found in atlas, path: " + path + " (skinned mesh attachment: " + name + ")");
        }
        return attachment;
    }

    public BoundingBoxAttachment newBoundingBoxAttachment(Skin skin, String name) {
		return new BoundingBoxAttachment(name);
	}

	private Region findRegion(TextureAtlasData atlasData, String name) {
		Array<Region> regions = atlasData.getRegions();
		for (int i = 0, n = regions.size; i < n; i++)
			if (regions.get(i).name.equals(name)) return regions.get(i);
		
//		Util.log(name);
		return null;
	}

}
