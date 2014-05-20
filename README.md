AssetBuilder
============

AssetBuilder is a library for automatically scanning asset files and generating Java code that does the asset loading/disposing. So whenever you update Texture/TextureAtlas/Sound/BitmapFont/Particle/Shader/SpineAnimation for your libGDX project, you only need to press the hotkey and the plugin will create the code for you.


Code Structure
============

The "library" folder contains the library code, while the "plugins" folder contains plugin project for each IDE. Plugin projects depends on the library code. Currently there is only Intellij IDEA plugin for libGDX Gradle-based projects. However, you can easily create plugins based on the library.

Note that the library scans and resolves asset files based on file extension, e.g., by default it resolves "libgdx.atlas" as a TextureAtlas asset and resolve "hello.particle" as a ParticleEffect asset, this can be adjusted in "AssetBuilderOptions.json" mentioned below.


Usage
============

There is already a installable IDEA plugin zip inside the  IDEA plugin project folder, i.e., AssetBuilderForIDEA.zip, you can build the plugin project youself or you can directly install this zip to IDEA.

To use the plugin, open your gradle-based libGDX project, select your project and press "ctrl+alt+1"(the default hotkey for the plugin), then a balloon tooltip should appear and shows the result. The first time plugin is run, it will generate Java code under "core" project, and the package name and class name is "com/gametravel/assets/gen/AssetBundle.java", it will also create a file name "AssetBuilderOptions.json" in which you can adjust the plugin options for this project.


Current Status
============

Currently the library supports Shader/SpineBinary/SpineJson/BitmapFont/Music/ParticleEffect/Sound/Texture/TextureAtlas assets. Model/Pixmap/Skin are not yet supported.


Credit
============

The AssetBuilder uses the following libraries:

MP3SPI - http://www.javazoom.net/mp3spi/mp3spi.html
Spine libGDX runtime - https://github.com/EsotericSoftware/spine-runtimes
jorbis - http://www.jcraft.com/jorbis/