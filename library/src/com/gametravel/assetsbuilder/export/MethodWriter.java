package com.gametravel.assetsbuilder.export;

import com.gametravel.assetsbuilder.Options;
import com.gametravel.assetsbuilder.scan.AssetBundle;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Daniel Chow
 * @date 5/11/2014 10:35 PM
 */
public interface MethodWriter {
    public String writeMethods(AssetBundle bundle, Options options);
}
