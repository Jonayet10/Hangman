package edu.caltech.cs2.project02.guessers;

import edu.caltech.cs2.project02.interfaces.IHangmanGuesser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AIHangmanGuesser implements IHangmanGuesser {
  private static String dictionary = "data/scrabble.txt";

  /**
   * This method takes the current pattern and a set of previous guesses,
   * and returns the next guess to make.
   */

  // helper method that takes a set of characters, and applies it to word to get the pattern
  public String retrievePattern(String word, Set<Character> guesses) {
    String pattern = "";
    for (int i = 0; i < word.length(); i++) {
      if (guesses.contains(word.charAt(i))) {
        pattern += word.charAt(i);
      } else {
        pattern += "-";
      }
    }
    return pattern;
  }

  @Override
  public char getGuess(String pattern, Set<Character> guesses) throws FileNotFoundException {
    SortedSet<String> scrabbleSet = new TreeSet<>();        // set of all words with correct word length
    SortedSet<String> wordWPatMatches = new TreeSet<>();    // set of words from scrabbleSet that matches the pattern
    Map<Character,Integer> frequencies = new TreeMap<>();   // map with letter key and frequency value
    SortedSet<Character> unGuessed = new TreeSet<>();       // set of letters not yet guessed
    for (char i = 97; i <= 122; i++) {
      if (!(guesses.contains(i))) {
        unGuessed.add(i);
      }
    }
    File scrabble = new File(dictionary);
    Scanner scan = new Scanner(scrabble);
    String line = "";
    while (scan.hasNextLine()) {
      line = scan.nextLine();
      if (line.length() == pattern.length()) {
        scrabbleSet.add(line);
      }                                       // all words of correct word length has been added to scrabbleSet
    }
    // for each word in scrabbleSet, run retrievePattern on it (gets pattern with every letter in guesses)
    // and if this pattern matches the argument pattern, then add the word to wordWPatMatches.
    for (String word : scrabbleSet) {
      String disPattern = retrievePattern(word,guesses);
      if (disPattern.equals(pattern)) {
        wordWPatMatches.add(word);
      }
    }
    // loop through unGuessed set (set of chars not in guesses). For each LETTER in unGuessed, for each word in wordWPat
    // Matches, for each character in word, if the character equals LETTER, then increment occurences (reassigning variable)
    // store this stuff in a map, with keys as letters from unGuessed and occurences as the values
    int occurrences = 0;
    for (Character c : unGuessed) {
      for (String word : wordWPatMatches) {
        for (int i = 0; i < word.length(); i++) {
          if (word.charAt(i) == c) {
            occurrences += 1;
          }
        }
      }
      frequencies.put(c,occurrences);
      occurrences = 0;
    }
    int counter = 0;
    Character bestGuess = null;             // can't do for each loop on <Character,Integer> map, and when I do normal for loop, can't retrieve keys from map
    for (Character ltr : frequencies.keySet()) {
      if (frequencies.get(ltr) > counter) {
        counter = frequencies.get(ltr);
        bestGuess = ltr;
      }
    }
    return bestGuess;
  }
}
