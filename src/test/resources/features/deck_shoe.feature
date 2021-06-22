Feature: Deck Shoe
  The simulated Deck Shoe follows certain rules of dealing cards and reloading the shoe when necessary, depending on the
  game rules that are selected at runtime, or a default set of rules if none are selected.

  Scenario: New deck, no cards dealt
    Given there is a new deck that has just been created
    When we have rules that require at least one deck to be loaded
    Then you can request a card to be dealt and will receive a Card object