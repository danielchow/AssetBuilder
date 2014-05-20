package com.gametravel.assetsbuilder.export;

import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.scan.AssetBundle;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Daniel Chow
 * @date 5/11/2014 7:10 PM
 */
public interface FieldWriter {
    public String writeFields(AssetBundle bundle, Options options);
}
