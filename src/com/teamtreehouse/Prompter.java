package com.teamtreehouse;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Prompter {
    private final BufferedReader mReader;
    private final Set<String> mCensoredWords;

    public Prompter() {
        mReader = new BufferedReader(new InputStreamReader(System.in));
        mCensoredWords = new HashSet<>();
        loadCensoredWords();
    }

    private void loadCensoredWords() {
        Path file = Paths.get("resources", "censored_words.txt");
        System.out.println("File path: " + file.toAbsolutePath());
        List<String> words = null;
        try {
            words = Files.readAllLines(file);
            if (words != null) {
                mCensoredWords.addAll(words);
            } else {
                System.out.println("No words were loaded into censorWords, please check the file!");
            }
//            System.out.println("Censored words loaded: " + mCensoredWords); // debug: check loaded words
        } catch (IOException e) {
            System.out.println("Couldn't load censored words");
            e.printStackTrace();
        }
        if (mCensoredWords != null) {
            assert words != null;
            mCensoredWords.addAll(words);
        }
    }

    public void run(Template tmpl) {
        List<String> results = null;
        try {
            results = promptForWords(tmpl);
            System.out.println("User inputs collected: " + results);
        } catch (IOException e) {
            System.out.println("There was a problem prompting for words");
            e.printStackTrace();
            System.exit(0);
        }
        // TODO:csd - Print out the results that were gathered here by rendering the template
        String renderTemplate = tmpl.render(results);
        System.out.println(renderTemplate);
    }

    /**
     * Prompts user for each of the blanks
     *
     * @param tmpl The compiled template
     * @return
     * @throws IOException
     */
    public List<String> promptForWords(Template tmpl) throws IOException {
        List<String> words = new ArrayList<String>();
        for (String phrase : tmpl.getPlaceHolders()) {
            String word = promptForWord(phrase);
            words.add(word);
        }
        return words;
    }


    /**
     * Prompts the user for the answer to the fill in the blank.  Value is guaranteed to be not in the censored words list.
     *
     * @param phrase The word that the user should be prompted.  eg: adjective, proper noun, name
     * @return What the user responded
     */
    public String promptForWord(String phrase) {
        // TODO:csd - Prompt the user for the response to the phrase, make sure the word is censored, loop until you get a good response.
        String word = null;
        while (true) {
            System.out.printf("Please provide a %s: ", phrase);
            try{
                word = mReader.readLine();
                if (mCensoredWords.contains(word)) {
                    System.out.println("The word you entered is not allowed. Please try again.");
                } else {
                    break; // valid word provided, exit the loop
                }
            } catch (IOException e) {
                System.out.println("Error reading input. Please try again.");
            }
        }
        return word;
    }
}
