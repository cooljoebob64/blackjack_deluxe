# BlackJack Deluxe
## By Joshua Luppes

## 👋 Introduction
This application will allow you to play the card game Blackjack via command-line interface. The game design offers an
accurate rule-set and configurable options to suit player preference.

The goal of this project is to demonstrate good coding practices, including testing, documentation, encapsulation, and 
modularity.

This application is a work in progress. Check back soon for updates!

## 📚 Table of Contents
- [Features](#-features)
- [Installation](#-installation)
- [Usage](#-usage)
- [Technologies](#-technologies)
- [Known Issues](#-known-issues)
- [Code Examples](#-code-examples)
- [Acknowledgements](#-acknowledgements)

## 🌟 Features
- Accurate Blackjack gameplay rules
- Text-based console interface
- Fully customizable rule set, including shoe size, insurance, surrenders, payouts, and dealer soft-17s
- Various difficulty modes built-in via pre-set rule configurations

## 📦 Installation
### Building from source
- Make sure to have [JRE version 15 or 16](https://www.oracle.com/java/technologies/javase-downloads.html#JDK16) installed
- Clone the repository to your machine
- Build via Gradle with `gradle build`
### Release version
- The latest release is available on the [GitHub Page](https://github.com/cooljoebob64/blackjack_deluxe)

## 🚀 Usage
### Source Build
- If you have Gradle installed globally, you can run the program in your terminal by navigating to the project directory, and running the command 
  `gradle --console plain run`
- You can also run the Main class in your Java IDE of choice
### Release Version
- Once extracted, the application can be executed by running  the batch file in the `bin` directory.

## 💻 Technologies
- [Java JDK 15](https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html)
- Authored in [IntelliJ IDEA Community Edition 2021.1](https://www.jetbrains.com/idea/)

## 🦺 Known Issues
- Player can only split their hand once, not multiple times.

## 🙏 Acknowledgements
- .gitignore filed generated by [gitignore.io](https://www.toptal.com/developers/gitignore)
- Ascii art generated by [TextKool](https://textkool.com/en/ascii-art-generator)

## 🎁 Code Examples
Part of the Deck class that handles reloading the shoe, utilizing dynamic game rules
```Java
public class Deck {
  /**
   * Adds decks into the Shoe, optionally clearing beforehand according to game rules
   * Loads the number of decks specified by the game rules
   */
  public void reloadShoe() {
    // If the rules require it, clear the shoe before reloading
    if (gameRules.isClearShoeOnLoad()) {
      currentShoe.clear();
      System.out.println("Clearing shoe...");
    }

    // Determine the number of decks that must be loaded at once
    int decksToLoad = gameRules.getDecksToLoad();
    System.out.println("Reloading shoe with " + decksToLoad + " decks.");

    // For each deck that must be loaded, make a shuffled deck and add each card to the shoe
    for (int i = 0; i < decksToLoad; i++) {
      Card[] deckToLoad = getShuffledDeck();
      for (Card card : deckToLoad) {
        currentShoe.push(card);
      }
    }
  }
}
```

## 📑 License
[MIT](https://choosealicense.com/licenses/mit/)

GitHub Link: https://github.com/cooljoebob64/blackjack_deluxe