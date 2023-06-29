package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EvilHangmanChooser implements IHangmanChooser {
  private int maxGuesses;
  private SortedSet<Character> allGuesses = new TreeSet<>();
  private TreeSet<String> possibleWords = new TreeSet<>();    // possible words that computer will create families from
  private String currPattern;

  public EvilHangmanChooser(int wordLength, int maxGuesses) throws FileNotFoundException {
    this.maxGuesses = maxGuesses;

    if (wordLength < 1 || maxGuesses < 1) {
      throw new IllegalArgumentException();
    }
    File scrabble = new File("data/scrabble.txt");
    Scanner scan = new Scanner(scrabble);
    String line;
    while (scan.hasNextLine()) {
      line = scan.nextLine();
      if (line.length() == wordLength) {
        possibleWords.add(line);
      }
    }
    if (possibleWords.size() == 0) {
      throw new IllegalStateException();
    }
    this.currPattern = "-".repeat(wordLength);

  }

  public String obtainPattern(String word, char letter) {
    String pattern = "";
    for (int i = 0; i < word.length(); i++) {
      if (word.charAt(i) == letter) {
        pattern += letter;
      } else {
        pattern += currPattern.charAt(i);
      }
    }
    return pattern;
  }

  @Override
  public int makeGuess(char letter) {
    if (maxGuesses <  1) {
      throw new IllegalStateException();
    }
    if (allGuesses.contains(letter)) {
      throw new IllegalArgumentException();
    }
    if (!(letter >= 97 && letter <= 122)) {
      throw new IllegalArgumentException();
    }

    allGuesses.add(letter);

    Map<String,TreeSet<String>> families = new TreeMap<>();

    for (String word : possibleWords) {
      String aPattern = obtainPattern(word,letter);
      if (!(families.containsKey(aPattern))) {
        families.put(aPattern, new TreeSet<>());
      }
      families.get(aPattern).add(word);
    }

    String chosenPattern = "";
    int valueSize = 0;
    for (String pattern : families.keySet()) {
      if (families.get(pattern).size() > valueSize) {
        valueSize = families.get(pattern).size();
        chosenPattern = pattern;
      }
    }

    int guessCorrectionCounter = 0;
    for (String word : families.get(chosenPattern)) {
      for (int i = 0; i < word.length(); i++) {
        if (word.charAt(i) == letter) {
          guessCorrectionCounter += 1;
        }
      }
    }
    if (guessCorrectionCounter == 0) {
      maxGuesses -= 1;
    }

    int oldNumRevealed = currPattern.replace("-", "").length();
    currPattern = chosenPattern;
    possibleWords = families.get(currPattern);

    int newNumRevealed = currPattern.replace("-", "").length();

    return newNumRevealed - oldNumRevealed;
  }



  @Override
  public boolean isGameOver() {
    int numRevealedLetters = 0;
    for (int i = 0; i < currPattern.length(); i++) {
      if (currPattern.charAt(i) != '-') {
        numRevealedLetters += 1;
      }
    }
    if (numRevealedLetters == currPattern.length() || maxGuesses == 0) {
      return true;
    }
    return false;
  }

  @Override
  public String getPattern() {
    return currPattern;
  }

  @Override
  public SortedSet<Character> getGuesses() {
    return allGuesses;
  }

  @Override
  public int getGuessesRemaining() {
    return maxGuesses;
  }

  @Override
  public String getWord() {
    maxGuesses = 0;
    return possibleWords.first();
  }
}
