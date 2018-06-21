/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwjgl.testing.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Owner
 */
public class FileUtil {
    
    public static String loadAsString(String path) throws IOException {
        return loadAsStringBuilder(path).toString();
    }
    
    private static StringBuilder loadAsStringBuilder(String path) throws IOException {
        StringBuilder out = new StringBuilder();
        BufferedReader br = loadAsBufferedReader(path);
        String line = "";
        while((line = br.readLine()) != null) {
            out.append(line);
            out.append("\n");
        }
        br.close();
        return out;
    }
    
    private static BufferedReader loadAsBufferedReader(String path) throws FileNotFoundException {
        return new BufferedReader(new FileReader(path));
    }
    
    public static BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(new File(path));
    }
    
    public static String getExtension(String path) {
        int periodIndex = path.lastIndexOf(".");
        return path.substring(periodIndex + 1);
    }
}
