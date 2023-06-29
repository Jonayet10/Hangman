package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.Character.isUpperCase;

public class RandomHangmanChooser implements IHangmanChooser {
  private int maxGuesses;
  final private static Random rand = new Random();
  final private String daWord;
  private ArrayList<Character> allGuesses = new ArrayList<>();
    
  public RandomHangmanChooser(int wordLength, int maxGuesses) throws FileNotFoundException {
    this.maxGuesses = maxGuesses;

    if (wordLength < 1 || maxGuesses < 1) {
      throw new IllegalArgumentException();
    }
    File scrabble = new File("data/scrabble.txt");
    Scanner scan = new Scanner(scrabble);
    SortedSet<String> scrabbleSet = new TreeSet<>();
    String line;
    while (scan.hasNextLine()) {
      line = scan.nextLine();
      if (line.length() == wordLength) {
        scrabbleSet.add(line);
      }
    }
    if (scrabbleSet.size() == 0)  {
      throw new IllegalStateException();
    }
    int scrabbleSetSize = scrabbleSet.size();           // turn tree set into an arraylist to iterate over
    List<String> scrabbleList = new ArrayList<>(scrabbleSet);
    String theRandWord = "";

    int idx = rand.nextInt(scrabbleSetSize);
    theRandWord = scrabbleList.get(idx);

    this.daWord = theRandWord;
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
    int occurrences = 0;
    for (int i = 0; i < daWord.length(); i++) {
      if (daWord.charAt(i) == letter) {
        occurrences +=1;
      }
    }
    if (occurrences == 0) {
      maxGuesses -= 1;
    }
    return occurrences;
  }

  // For every char in daWord, if the char is in allGuesses, then add 1 to matches. If the size of the daword
  // is equal to the number of matches, then true
  @Override
  public boolean isGameOver() {
    int matches = 0;
    for (int i = 0; i < daWord.length(); i++) {
      if (allGuesses.contains(daWord.charAt(i))) {
        matches += 1;
      }
    }
    if (matches == daWord.length() || maxGuesses == 0) {
      return true;
    }
    return false;
  }

  @Override
  public String getPattern() {
    String pattern = "";
    for (int i = 0; i < daWord.length(); i++) {
      if (allGuesses.contains(daWord.charAt(i))) {
        pattern += daWord.charAt(i);
      } else {
        pattern += '-';
      }
    }
    return pattern;
  }

  @Override
  public SortedSet<Character> getGuesses() {
    SortedSet<Character> allGuessesSet = new TreeSet<Character>();
    for (int i = 0; i < allGuesses.size(); i++) {
      allGuessesSet.add(allGuesses.get(i));
    }
    return allGuessesSet;
  }

  @Override
  public int getGuessesRemaining() {
    return maxGuesses;
  }

  @Override
  public String getWord() {
    maxGuesses = 0;
    return daWord;
  }
}