/**
 * 
 */
package com.gametravel.assetsbuilder.util;

import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.file.TAudioFileFormat;

import com.badlogic.gdx.files.FileHandle;

/**
 * @author Daniel Chow Mar 30, 2014 11:22:59 AM
 */
public class Util {
    public static double getAudioDuration(FileHandle file) {
        String ext = file.extension();
        double duration = 0;
        try {
            if ("wav".equalsIgnoreCase(ext)) {
                AudioInputStream audioInputStream;
                audioInputStream = AudioSystem.getAudioInputStream(file.file());
                AudioFormat format = audioInputStream.getFormat();
                long frames = audioInputStream.getFrameLength();
                duration = (frames + 0.0) / format.getFrameRate();
            } else if ("mp3".equalsIgnoreCase(ext) || "ogg".equalsIgnoreCase(ext)) {
                AudioFileFormat fileFormat;
                fileFormat = AudioSystem.getAudioFileFormat(file.file());
                if (fileFormat instanceof TAudioFileFormat) {
                    Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
                    String key = "duration";
                    Long microseconds = (Long) properties.get(key);
                    duration = microseconds / 1000000.0;
                }
            } else {
                throw new IllegalArgumentException("unsupported audio: " + ext);
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return duration;
    }

    public static String getRelativePath(FileHandle file) {
        String absolutePath = file.path();
        final int lastIndex = absolutePath.lastIndexOf("assets/");
        if (lastIndex < 0) {
            return absolutePath;
        } else {
            return absolutePath.substring(lastIndex + 7);
        }
    }
    
    public static String toValidJavaFieldName(String raw) {
        String fieldName;
        int slashIndex = raw.lastIndexOf('/');
        if (slashIndex == -1) {
            fieldName = raw;
        } else {
            fieldName = raw.substring(slashIndex + 1);
        }
        fieldName = fieldName.toLowerCase();
        StringBuilder builder = new StringBuilder();
        for (int i = 0, n = fieldName.length(); i < n; i++) {
            char ch = fieldName.charAt(i);
            if (Character.isLetter(ch) || ch == '_' || ch == '$'
                    || (Character.isDigit(ch) && i != 0)) {
                builder.append(ch);
            } else {
                builder.append('_');
            }
        }
        return builder.toString();
    }

    public static void err(String msg) {
        System.err.println(msg);
    }

    public static void log(String msg) {
        System.out.println(msg);
    }
}
