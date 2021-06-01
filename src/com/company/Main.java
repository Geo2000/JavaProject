package com.company;

public class Main {

    public static void main(String[] args) throws Exception {
        Generator gen = Generator.getInstance();
        Crypto crypt = Crypto.getInstance();

        String plainText = gen.getEnglishString();

        do {
            String key = gen.generateRandomKey();
            //String key = "imvpzezdvrxoyifwuc";
            String encryptedText = crypt.encryptVigenere(plainText, key);
            String keyFound = crypt.decryptVigenere(encryptedText);

            System.out.println("Plain text: " + plainText);
            System.out.println("Key       : " + key);
            System.out.println("Key length: " + key.length());
            System.out.println("Crypted   : " + encryptedText);
            System.out.println("Key found : " + keyFound);

            if (key.equals(keyFound)) {
                System.out.println("!!!!!!CRACKED KEY!!!!!!");
            } else {
                System.out.println("Failed mission");
                break;
            }

            System.out.println();
        } while(true);
    }
}
