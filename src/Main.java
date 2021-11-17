import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *  Assignment 1    COMP 4476
 *  Author:         Wilana Matthews
 *  Last Modified:  Sept. 25, 2021
 */
public class Main {

    // to store the alphabet, including a-z, space, comma and period
    private static final ArrayList<Character> alphabet = new ArrayList<>();

    public static void main(String[] args) {
        // Fill alphabet array list
        alphabet.addAll(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ', ',', '.'));

        // Variables to texts
        String plaintext = "This is assignment one from cryptography and network security. It is submitted by Wilana Matthews, a third year computer science student. I hope I do well.";
        String encryptedText = "";
        String decryptedText = "";

        // Random key permutations and m
        ArrayList<Integer> randomSubstitutionKey = new ArrayList<>();
        randomSubstitutionKey.addAll(Arrays.asList(16, 2, 22, 0, 21, 25, 3, 8, 20, 14, 5, 10, 26, 9, 15, 12, 24, 6, 27, 7, 13, 19,
                18, 23, 17, 28, 4, 11, 1));
        ArrayList<Integer> randomPermutationKey4 = new ArrayList<>();
        randomPermutationKey4.addAll(Arrays.asList(3, 4, 1, 2));
        int m4 = 4;
        ArrayList<Integer> randomPermutationKey5 = new ArrayList<>();
        randomPermutationKey5.addAll(Arrays.asList(3, 1, 5, 2, 4));
        int m5 = 5;


        //Intro documentation
        System.out.println("\nAssignment 1\t\tCOMP 4476");
        System.out.println("Wilana Matthews\t\t1120464");
        System.out.println("-------------------------------------------------\n\n");

        // Problem 1:
        System.out.println("Problem 1: \t\t\tSubstitution Cipher");
        System.out.println("-------------------------------------------------");
        System.out.print("Key Permutation:\t");
        for (int num : randomSubstitutionKey) {
            System.out.printf("%d, ", num);
        }
        System.out.printf("\nPlaintext:\t\t\t%s\n", plaintext);

        encryptedText = SubstitutionCipher.encrypt(plaintext, randomSubstitutionKey);
        System.out.printf("Encryption:\t\t\t%s\n", encryptedText);
        decryptedText = SubstitutionCipher.decrypt(encryptedText, randomSubstitutionKey);
        System.out.printf("Decryption:\t\t\t%s\n", decryptedText);


        // Problem 2
        System.out.println("\n\nProblem 2: \t\t\tPermutation Cipher");
        System.out.println("-------------------------------------------------");
        System.out.print("Key Permutation:\t");
        for (int num : randomPermutationKey4) {
            System.out.printf("%d, ", num);
        }
        // change plaintext to make it easier to see changes
        plaintext = "I like dogs";
        System.out.printf("\tfor m = %d\nPlaintext:\t\t\t%s\n", m4, plaintext);

        encryptedText = PermutationCipher.encrypt(plaintext, m4, randomPermutationKey4);
        System.out.printf("Encryption:\t\t\t%s\n", encryptedText);
        decryptedText = PermutationCipher.decrypt(encryptedText, m4, randomPermutationKey4);
        System.out.printf("Decryption:\t\t\t%s\n", decryptedText);


        // Problem 3
        System.out.println("\n\nProblem 3: \t\t\tPermutation Cipher");
        System.out.println("-------------------------------------------------");
        plaintext = PermutationCipher.simplifyPlaintext(plaintext, m4);
        System.out.printf("Plaintext:\t\t\t%s\n", plaintext);
        System.out.printf("Ciphertext:\t\t\t%s\n", encryptedText);
        System.out.printf("%s\n", PermutationCipher.attack(plaintext, encryptedText));

        // change plaintext and m
        plaintext = PermutationCipher.simplifyPlaintext("The dog will walk at eleven", m5);
        encryptedText = PermutationCipher.encrypt(plaintext, m5, randomPermutationKey5);
        System.out.printf("Plaintext:\t\t\t%s\n", plaintext);
        System.out.printf("Ciphertext:\t\t\t%s\n", encryptedText);
        System.out.printf("%s\n", PermutationCipher.attack(plaintext, encryptedText));
    }

    /**
     * Problem 1
     */
    public static class SubstitutionCipher {

        /**
         * Takes the plaintext and uses the key to form a ciphertext
         * @param plaintext a paragraph consisting English characters a-z, space, comma, and period
         * @param key a random permutation of the set {0,1, ..., 27, 28}
         * @return the ciphertext in the characters of the English alphabet
         */
        public static String encrypt(String plaintext, ArrayList<Integer> key){
            String ciphertext = "";
            plaintext = plaintext.toUpperCase();
            // work through each character of the plaintext
            for (int i = 0; i < plaintext.length(); i++) {
                // get the current letter
                char letter = plaintext.charAt(i);
                // find the letter's position in the alphabet
                int alphabetPosition = alphabet.indexOf(letter);

                if (alphabetPosition >= 0) {
                    // find the key in the same index as the alphabetical position
                    int keyPosition = key.get(alphabetPosition);

                    if (keyPosition >= 0)
                        // add the letter at the encrypted position of the alphabet via the key
                        ciphertext = ciphertext + alphabet.get(keyPosition);
                }
            }
            return ciphertext;
        } // encrypt

        /**
         * Finds the inverse key permutation, then calls the encrypt function to decrypt ciphertext
         * @param ciphertext a paragraph consisting English characters a-z, space, comma, and period
         * @param key a random permutation of the set {0,1, ..., 27, 28} to be used to find the inverse
         * @return the plaintext in the characters of the English alphabet, decrypted.
         */
        public static String decrypt (String ciphertext, ArrayList<Integer> key) {
            String plaintext = "";

            // find the inverse permutation for the decrypting key
            ArrayList<Integer> inverseKey = new ArrayList<>();
            for (int i = 0; i<29; i++)
                inverseKey.add(key.indexOf(i));

            // use inverse permutation in encrypt function to decrypt
            plaintext = encrypt(ciphertext, inverseKey);

             return plaintext;
        } // decrypt
    } // SubstitutionCipher

    /**
     * Problems 2 and 3
     */
    public static class PermutationCipher {

        /**
         * Ensures the plaintext is in all uppercase letters, with no spaces, periods or commas
         * and enough padding is added to be useable in a permutation with the m value
         * @param plaintext a string of english letters
         * @param m the m value
         * @return an all uppercase string of english characters and x paddding
         */
        public static String simplifyPlaintext (String plaintext, int m) {
            // remove spaces and capitalize all letters
            plaintext = plaintext.replaceAll("\\s+","").toUpperCase();
            // remove punctuation
            plaintext = plaintext.replaceAll("[^a-zA-Z ]", "");

            int paddingNeeded = m - (plaintext.length()%m);
            if (paddingNeeded != m)
            {
                // use x for padding because it isn't a commonly used letter, and it is often used as a sign-off
                for (int i = 0; i < paddingNeeded; i++)
                    plaintext = plaintext.concat("X");
            }

            return plaintext;
        }

        /**
         * Encrypts a plaintext using permutation cipher with m value and key
         * @param plaintext the plaintext in English characters a-z, space, comma and period.
         * @param m the size of the permutation
         * @param key a permutation of {1, 2, ... m}
         * @return the ciphertext as English characters
         */
        public static String encrypt(String plaintext, int m, ArrayList<Integer> key){
            String ciphertext = "";

            // make sure text is in proper form
            plaintext = simplifyPlaintext(plaintext, m);

            // make an array from characters of plaintext
            char[] plaintextChars = plaintext.toCharArray();
            // add all characters from array into a list, as strings
            ArrayList<String> plaintextList = new ArrayList<>();
            for (char c : plaintextChars)
                plaintextList.add(String.valueOf(c));

            int newIndex = 0;

            // work in blocks of m
            for (int i = 0; i < plaintextList.size(); i = i + m)
            {
                // work through each of the letters in the block
                for (int j = 0; j < m; j++)
                {
                    // find the index from the key and subtract one to go from 1-5 to 0-4
                    newIndex = key.get(j) - 1;
                    // add the letter to the cipher according to the new index from the key
                    ciphertext = ciphertext.concat(plaintextList.get(i + newIndex));
                }
            }

            return ciphertext;
        } // encrypt

        /**
         * This function decrypts a ciphertext from a permutation cipher into the plaintext
         * by first finding the inverse key, then using the encryption algorithm again
         * @param ciphertext English letters, encrypted
         * @param m the m value
         * @param key the key used to encrypt
         * @return the plaintext in english characters a-z
         */
        public static String decrypt (String ciphertext, int m, ArrayList<Integer> key) {
            String plaintext = "";

            // find the inverse permutation for the decrypting key
            ArrayList<Integer> inverseKey = new ArrayList<>();
            for (int i = 1; i<=m; i++)
                inverseKey.add(key.indexOf(i) + 1);

            // use inverse permutation in encrypt function to decrypt
            plaintext = encrypt(ciphertext, m, inverseKey);

            return plaintext;
        }


        /**
         * This function uses the plaintext and ciphertext to discover the key and m value.
         * It has limitations, in that an attack will be unsuccessful if it is unable to
         *  find a block that does not have any repeating characters.
         * @param plaintext English letters decrypted
         * @param ciphertext English letters encrypted
         * @return a string naming the value of m and the permutation/key
         */
        public static String attack (String plaintext, String ciphertext) {
            String key = "";

            // cycle through possible m values
            for (int m = 2; m <= plaintext.length(); m++)
            {

                // only work on m if the length is divisible by m
                if (plaintext.length()%m == 0)
                {
//                    System.out.println("\nm = " + m);

                    // work in blocks
                    for (int i = 0; i < plaintext.length(); i = i + m) {
                        // add the characters to a list as strings
                        ArrayList<String> plainBlockList = new ArrayList<>();
                        for (char c : plaintext.substring(i, i + m).toCharArray())
                            plainBlockList.add(String.valueOf(c));
                        ArrayList<String> cipherBlockList = new ArrayList<>();
                        for (char c : ciphertext.substring(i, i + m).toCharArray())
                            cipherBlockList.add(String.valueOf(c));
//
//                        System.out.printf("PT: %s \t\tCT: %s\n", plainBlockList.toString(), cipherBlockList.toString());


                        // Look for a repeated character in either plain or cipher that would make it difficult to gain the key from
                        boolean repeatedChar = false;

                        for (int j = 0; j < plainBlockList.size(); j++)
                            for (int k = j+1; k < plainBlockList.size(); k++) {
                                if (plainBlockList.get(j).equals(plainBlockList.get(k))) {
                                    repeatedChar = true;
                                    break;
                                }
                                if (cipherBlockList.get(j).equals(cipherBlockList.get(k))) {
                                    repeatedChar = true;
                                    break;
                                }
                            }


                        if (!repeatedChar)
                        {
                            // check for equivalent letters in each:
                            ArrayList<String> plainBlockCopy = new ArrayList<>(plainBlockList);
                            Collections.sort(plainBlockCopy);
                            ArrayList<String> cipherBlockCopy = new ArrayList<>(cipherBlockList);
                            Collections.sort(cipherBlockCopy);

                            if (plainBlockCopy.equals(cipherBlockCopy)) {
                                int foundIndex = 0;
                                // easier to get key from no repeated letters
                                for (int j = 0; j < cipherBlockList.size(); j++) {
                                    // find the index of the current ciphertext letter in the plaintext
                                    foundIndex = plainBlockList.indexOf(cipherBlockList.get(j)) + 1;
                                    key = key.concat(foundIndex + " ");
                                }

                                return (String.format("Attack successful: \tm = %d \t Key = %s\n",m,key));
                            }
                        }

                    }
                }
            }

            return "Attack unsuccessful, there may be too many repeated letters per block";
        }
    }
}


