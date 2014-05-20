/**
 * 
 */
package com.gametravel.assetsbuilder.data;

import com.badlogic.gdx.utils.Array;
import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.scan.AssetBundle;

/**
 * An {@link com.gametravel.assetsbuilder.data.AssetData} represents an {@code resolvable/compilable} asset unit.
 * @author Daniel Chow Mar 30, 2014 11:33:43 PM
 */
public interface AssetData {

    /**
     * Returns the simple name of this asset unit, e.g. {@code "awesome_texture"}.
     * @return The name
     */
    public String simpleName();

    /**
     * Returns the full name of this asset unit, e.g. {@code "awesome_texture.atlas"}.
     * @return The full name
     */
    public String fullName();

    /**
     * Try to resolve this {@link com.gametravel.assetsbuilder.data.AssetData}, the specified {@link AssetBundle} should contain all
     * available assets scanned, we can determine whether an AssetData can be resolved after scan
     * completed.
     * @param bundle The asset bundle
     * @param options The options
     * @return {@code true} if resolved, otherwise {@code false}
     */
    public boolean resolve(AssetBundle bundle, Options options);

    /**
     * Returns whether this {@link com.gametravel.assetsbuilder.data.AssetData} has been resolved.
     * @return {@code true} if resolved, otherwise {@code false}
     */
    public boolean isResolved();

    /**
     * Returns whether already tried resolving this {@link com.gametravel.assetsbuilder.data.AssetData}.
     * @return {@code true} if has tried resolving, otherwise {@code false}
     */
    public boolean hasTriedResolve();

    /**
     * Returns whether this asset is tagged.
     * @return {@code true} if tagged, otherwise {@code false}
     */
    public boolean isTagged();

    /**
     * Tags or cancel tagging this asset.
     * @param tag     {@code true} to tag, otherwise {@code false}
     * @param options The options
     */
    public void tag(boolean tag, Options options);

    /**
     * Returns array of assets that depends on this asset.
     * @return Array of dependants
     */
    public Array<AssetData> getDependants();

    /**
     * Adds a dependant of this asset.
     * @param dependant A asset that depends on this asset
     */
    public void addDependant(AssetData dependant);

}
