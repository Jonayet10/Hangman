# Hangman
This project implements a game of hangman. 

In this project, we represent choosers and guessers using the interfaces IHangmanGuesser and IHangmanChooser.

Because a game of hangman is parameterized on (1) the type of guesser, and (2) the type of chooser, a console-based main “runner” program called HangmanGame is provided, which instantiates any guesser and chooser of your choice and plays them against each other.
