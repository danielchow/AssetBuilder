package com.gametravel.assetsbuilder.data;

import com.badlogic.gdx.utils.Array;
import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.scan.AssetBundle;
import com.gametravel.assetsbuilder.util.Util;

/**
 * Basic implementation of {@link com.gametravel.assetsbuilder.data.AssetData}.
 * @author Hang Zhou Apr 1, 2014 4:18:05 PM
 */
public class BaseAssetData implements AssetData {

    /** The full name of asset data, e.g. {@code "libgdx_boy_jump.png"}. */
    protected String fullName;

    /** The full name of asset data, e.g. {@code "libgdx_boy_jump"}. */
    protected String simpleName;

    /** Whether asset data is resolved. */
    protected boolean resolved = false;

    /** Whether has already tried resolving this asset data. */
    protected boolean triedResolve = false;

    protected boolean tagged = false;

    /**
     * The information about this asset unit, e.g. the reason of failed resolving or other info.
     */
    protected String message;
    
    /** Array of assets that depends on this asset. */
    protected Array<AssetData> dependants = new Array<AssetData>(false, 1);

    public BaseAssetData(String fullName, String simpleName) {
        this.fullName = fullName;
        this.simpleName = simpleName;
    }

    @Override
    public boolean resolve(AssetBundle bundle, Options options) {
        triedResolve = true;
        return false;
    }

    @Override
    public void tag(boolean tag, Options options) {
        this.tagged = tag;
        if (options.LogOption.DEBUG) Util.log((tag ? "Tag" : "Un") + " " + toString());
    }

    @Override
    public boolean isTagged() {
        return this.tagged;
    }

    @Override
    public boolean isResolved() {
        return resolved;
    }

    @Override
    public boolean hasTriedResolve() {
        return triedResolve;
    }

    /**
     * Returns the information about this asset unit, e.g. the reason of failed resolving or other
     * info.
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{ name:").append(fullName).append(", type:")
                .append(getClass().getSimpleName()).append(", status:");
        if (isResolved()) {
            builder.append("RESOLVED }");
        } else if (hasTriedResolve()) {
            builder.append("FAILED, Message:").append(getMessage()).append(" }");
        } else {
            builder.append("PENDING }");
        }
        return builder.toString();
    }

    @Override
    public String simpleName() {
        return simpleName;
    }

    @Override
    public String fullName() {
        return fullName;
    }

    @Override
    public Array<AssetData> getDependants() {
        return dependants;
    }

    @Override
    public void addDependant(AssetData dependant) {
        dependants.add(dependant);
    }
}
