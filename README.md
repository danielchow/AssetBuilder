AssetBuilder
============

AssetBuilder is a library for automatically scanning asset files and generating Java code that does the asset loading/disposing.

Previously, whenever you update assets for your `libGDX` project, you might also need to update your code that did asset load/reference/dispose work. For example, when you add a `TextureAtlas` to your project, you might need to add the following code:
```java
public class AssetBundle {

    protected AssetManager assetManager;
    
    // The new added TextureAtlas
    public TextureAtlas newAtlas;
    
    // you might also want to add a field that reference the TextureRegion in TextureAtlas
    public TextureRegion newRegion1;
    public TextureRegion newRegion2;

    public void loadAsset() {
        assetManager.load("new_atlas.atlas", TextureAtlas.class);
        // ...
        // after the AssetManager actually loads the new TextureAtlas
        newAtlas = assetManager.get("new_atlas.atlas", TextureAtlas.class);
        newRegion1 = newAtlas.findRegion("newRegion1");
        newRegion2 = newAtlas.findRegion("newRegion2");
    }

}
```

This can be even boring when the `TextureAtlas` contains a lot of `TextureRegion`s or when you have to add/update/remove a lot of assets.


However, with AssetBuilder, you only need to press the hotkey inside your IDE and the plugin will generate the code for you within **less** than `1` second. See an example code generated by AssetBuilder inside the `tests` folder [here][1].


Code Structure
============

The `library` folder contains the library code, while the `plugins` folder contains plugin project for each IDE. Plugin projects depends on the library code. Currently there is only a `Intellij IDEA` plugin for libGDX gradle-based projects. However, you can easily create plugins based on the library code.

Note that the library scans and resolves asset files based on **file extension**, e.g., by default it resolves "libgdx.atlas" as a `TextureAtlas` asset then generate all the `TextureRegion` field for you,and it resolves "hello.particle" as a `ParticleEffect` asset, this can be adjusted in `AssetBuilderOptions.json` mentioned below.


Usage
============

There is already a installable IDEA plugin zip inside the `Intellij IDEA` plugin project folder, i.e., `AssetBuilderForIDEA.zip`, you can build the plugin project youself or you can directly install this zip to IDEA.

To use the plugin, open your gradle-based libGDX project, select your project and press `ctrl+alt+1` which is the default hotkey for the IDEA plugin, then a balloon tooltip should appear and shows the result. The first time plugin is run, it will generate Java code under `core` project, and the package name and class name is `com/gametravel/assets/gen/AssetBundle.java`, it will also create a file named `AssetBuilderOptions.json` inside which you can adjust the plugin options for current libGDX project.

Using the code generated by AssetBuilder is quite easy and a lot like `AssetManager` except you don't have to manually load each asset:
```java
public class AssetBuilderTest extends ApplicationAdapter {
    static AssetBundle asset;
    private SpriteBatch spriteBatch;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        // get instance of AssetBundle
        asset = AssetBundle.getInstance();
        // you can call asset.update() or asset.finishLoading() just like what you did with AssetManager
        asset.finishLoading();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // directly use the newRegion1 field..
        spriteBatch.draw(asset.newRegion1, 20f, 20f);
    }

    @Override
    public void dispose() {
        super.dispose();
        // remember to dispose the AssetBundle instance
        asset.dispose();
    }
}
```


Current Status
============

Currently the library supports automatic loading/dispose of `Shader/SpineBinary/SpineJson/BitmapFont/Music/ParticleEffect/Sound/Texture/TextureAtlas` assets. `Model/Pixmap/Skin` are not yet supported.

For `Shader`, it generates field for `ShaderProgram`;

For `SpineBinaray` and `SpineJson`, it generates field for `SkeletonData` and `AnimationStateData`, you can modfify the code to generate more fields, like `com.esotericsoftware.spine.Animation`;

For `BitmapFont`, `Music`, `Sound`, `ParticleEffect`, `Texture`, it will generate corresponding fields;

For `TextureAtlas`, it will generate `TextureAtlas` field and a lot of `TextureRegion` fields. It also avoids generating `TextureRegion` fields of those for other assets, e.g., if it found a `TextureRegion` **goblin_arm** is used for a `SpineJson` asset, then the **goblin_arm** field will not be generated.


License
============

    Copyright 2014 Daniel Chow

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


Credit
============

The AssetBuilder uses the following libraries:

MP3SPI - http://www.javazoom.net/mp3spi/mp3spi.html

Spine libGDX runtime - https://github.com/EsotericSoftware/spine-runtimes

jorbis - http://www.jcraft.com/jorbis/



[1]: https://github.com/danielchow/AssetBuilder/blob/master/tests/sample/AssetBundle.java
