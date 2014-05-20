package com.gametravel.assetsbuilder.export;

import com.gametravel.assetsbuilder.Options;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Daniel Chow
 * @date 5/11/2014 4:06 PM
 */
public interface AssetCodeField {
    public String getFieldName();
    public String getPrefix(Options options);
}
