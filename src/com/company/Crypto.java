package com.company;

public class Crypto {
    private static Crypto instance = null;
    private static int leftLimit = 97;
    private static int rightLimit = 122;

    private Crypto() {}

    public static Crypto getInstance() {
        if(instance == null) {
            instance = new Crypto();
        }

        return instance;
    }

    public String encryptVigenere(String plainText, String key) {
        int posKey = 0;
        int posText = 0;

        StringBuilder buffer = new StringBuilder();

        while(posText < plainText.length()) {
            buffer.append(shiftCharacter(plainText.charAt(posText), key.charAt(posKey)));
            posText++;
            posKey = (posKey + 1) % key.length(); //circular
        }

        return buffer.toString();
    }

    public void loadFreq(double[] freq) throws Exception {
        Generator gen = Generator.getInstance();

        String normalText = gen.getEnglishString();
        for(int i = 0; i < 26; i++) {
            freq[i] = 0.0;
        }

        for(int i = 0; i < normalText.length(); i++) {
            freq[(int)normalText.charAt(i) - (int)'a']++;
        }

        for(int i = 0; i < 26; i++) {
            freq[i] /= normalText.length();
        }

        /*freq[0] = 0.0855; // a
        freq[1] = 0.0160; // b
        freq[2] = 0.0316; // c
        freq[3] = 0.0387; // d
        freq[4] = 0.0121; // e
        freq[5] = 0.0218; // f
        freq[6] = 0.0209; // g
        freq[7] = 0.0496; // h
        freq[8] = 0.0733; // i
        freq[9] = 0.0022; // j
        freq[10] = 0.0081; // k
        freq[11] = 0.0421; // l
        freq[12] = 0.0253; // m
        freq[13] = 0.0717; // n
        freq[14] = 0.0747; // o
        freq[15] = 0.0207; // p
        freq[16] = 0.0010; // q
        freq[17] = 0.0633; // r
        freq[18] = 0.0673; // s
        freq[19] = 0.0894; // t
        freq[20] = 0.0268; // u
        freq[21] = 0.0106; // v
        freq[22] = 0.0183; // w
        freq[23] = 0.0019; // x
        freq[24] = 0.0172; // y
        freq[25] = 0.0011; // z*/
    }

    public String decryptVigenere(String cryptedText) throws Exception {
        int keyLength = computeVigenereKeyLength(cryptedText);
        //System.out.println("Found that key length is " + keyLength);

        double[] freq = new double[26];
        loadFreq(freq);

        double range;

        if(keyLength < 10) {
            range = 0.015;
        }
        else {
            range = 0.005;
        }

        StringBuilder key = new StringBuilder();

        for(int start = 0; start < keyLength; start++) {
            StringBuilder substring = new StringBuilder();

            //System.out.println("For character " + start);

            for(int idx = start; idx < cryptedText.length(); idx += keyLength) {
                substring.append(cryptedText.charAt(idx));
            }
            int shift = -1;
            int bestShift = -1;
            double minimum = 20.0;
            int pozMinimum = 0;
            double mic;
            do { //verific toate miscarile posibile pt a efectua o rotatie completa cu subsirul meu
                shift++;
                mic = computeMutualIndexOfCoincidence(substring.toString(), freq, shift);
                //System.out.print("  Shift = " + shift + " MIC = " + mic + " ; Corespondent = ");
                //System.out.println((char)((int)'a' + (26 - shift) % 26));
                if(Math.abs(mic - 0.065) < range && bestShift == -1) {
                    bestShift = shift;
                }
                if(Math.abs(mic - 0.065) < minimum) {
                    minimum = Math.abs(mic - 0.065);
                    pozMinimum = shift;
                }
            } while(shift < 25);

            if(bestShift == -1) {
                bestShift = pozMinimum;
            }

            //System.out.println("  BestShift = " + bestShift);
            key.append((char)((int)'a' + (26 - bestShift) % 26));
        }

        return key.toString();
    }

    private int computeVigenereKeyLength(String cryptedText) {
        int possibleLength = 0;
        double prc = 0.0;
        while(possibleLength <= 30) {
            possibleLength++;

            double md = 0.0;
            for(int start = 0; start < possibleLength; start++) {
                StringBuilder buffer = new StringBuilder();
                for(int idx = start; idx < cryptedText.length(); idx += possibleLength) {
                    buffer.append(cryptedText.charAt(idx));
                }

                md += computeIndexOfCoincidence(buffer.toString());
            }

            double difNow = Math.abs((md / (double)possibleLength) - 0.065);
            //System.out.println("Lungime " + possibleLength + ": " + difNow);
            if(difNow < 0.004 && Math.abs(prc - difNow) > 0.005) {
                return possibleLength;
            }

            prc = difNow;
        }

        return 1;
    }

    private double computeMutualIndexOfCoincidence(String text, double[] freq, int shift) {
        int[] ap = new int[26];

        for(int i = 0; i < text.length(); i++) {
            char newChar = shiftCharacter(text.charAt(i), (char)(shift + (int)'a'));
            ap[(int)newChar - (int)'a']++;
        }

        double mic = 0.0;

        for(int i = 0; i < 26; i++) {
            mic += freq[i] * (double) ap[i];
        }

        return mic/ (double) text.length();
    }

    private double computeIndexOfCoincidence(String text) {
        if(text.length() < 2) {
            return 0.0;
        }

        int[] frequency = new int[rightLimit - leftLimit + 1];

        for(int i = 0; i < text.length(); i++) { //fac frecventa subsirului
            frequency[(int) text.charAt(i) - leftLimit]++;
        }

        double index = 0.0;

        for(int i = 0; i < rightLimit - leftLimit; i++) { //calcularea indexului de coincidenta
            index += ((double) frequency[i] / (double) text.length()) *
                    ((double) (frequency[i] - 1) / (double) (text.length() - 1));
        }

        return index;
    }

    private char shiftCharacter(char plainChar, char keyChar) {
        int afterMod = ((int) plainChar + (int) keyChar - leftLimit) % (rightLimit + 1);

        if(afterMod < leftLimit) {
            afterMod += leftLimit;
        }

        return (char) (afterMod);
    }
}
