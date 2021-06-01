package com.company;
//import com.sun.prism.shader.Solid_TextureRGB_AlphaTest_Loader;

import java.io.*;
import java.util.Random;

public class Generator {
    private static Generator instance = null;
    private static int leftLimit = 97;
    private static int rightLimit = 122;

    private Generator() {}

    public static Generator getInstance() {
        if(instance == null) {
            instance = new Generator();
        }

        return instance;
    }

    public String generateRandomKey() {
        Random random = new Random();
        int targetKeyLength = random.nextInt(20) + 1;
        StringBuilder buffer = new StringBuilder();

        for(int i = 0; i < targetKeyLength; i++) { //caracter random
            int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (rightLimit - leftLimit + 1));

            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();
    }

    public String getEnglishString() throws Exception {
        String readRawText = readEnglishString();
        StringBuilder buffer = new StringBuilder();

        for(int i = 0; i < readRawText.length(); i++) {
            if(readRawText.charAt(i) <= 'Z' && readRawText.charAt(i) >= 'A') {
                buffer.append((char)((int)(readRawText.charAt(i)) + 32));
            }

            if(readRawText.charAt(i) <= 'z' && readRawText.charAt(i) >= 'a') {
                buffer.append(readRawText.charAt(i));
            }
        }

        return buffer.toString();
    }

    public String readEnglishString() throws Exception {
        File file = new File("D:\\englishText.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String str;
        StringBuilder buffer = new StringBuilder();

        while ((str = br.readLine()) != null) {
            buffer.append(str);
        }

        return buffer.toString();
    }
}
