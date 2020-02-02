# BackGammon Project

This is a sample coding project I worked on(in a team of 3) as part of my University Programme(second year of BSc).

It culminated in a Java Application which allows two players to play a full featured BackGammon game. The GUI was developed using the Java Swing toolkit.

The gameplay is as follows. Two players enter their names and specify how many points they would like to play to. Then, a die is rolled to determine who has the first turn, and the initial positions are displayed on the board. Information about the game is communicated to the players via the Information Panel next to the board. Underneath that panel there is a text box through which the players interact with the game. When it is a player's move, all the legal moves will be displayed in the information panel(labeled using capital letters[A-Z]). To make a move, the player enters one of the labels of the displayed moves. The rest of the game proceeds according to the usual BackGammon rules. A player may type "double" into the text box, to offer a double; and a player may type "quit" to exit the application. Additionally, for debugging purposes, "next" allows a player to skip a move, and "cheat" moves all pieces to the end of the board. 

The source files are divided into 4 packages:

* **game_controller**: contains the classes responsible for the overall execution of the application. In particular Game.java contains the main class used to run the application.
* **logic**: contains the classes which deal with the game data, and control and track all the gameplay logic(with an abstract data structures)
* **graphical_display**: handles and displays the graphical elements of the game(by reading the state of the game data in logic package)
* **user_interface**: contains the classes which deal with user input, also contains some graphical elements.


Additionally, there is a folder **bots** which contains a different BackGammon application, which allows two bots to play against each other. The "BackGammon" class contains the main class for the application, and the classes "Bot0" and "Bot1" contain the implementation of a Bot API(where the API was given). The "Bot1" class chooses its moves at random, while "Bot0" operates in the following way:
* It scores every possible move according to 6 criteria. The score for each criterium is multiplied by a weight stored in "Bot0_scoreFunctionValues.txt". 
* It takes the sum of those scores selects the move with the highest total.
* For every possible move, it computes the proportion of the total sum that each of the criteria were responsible for. Every turn it compares the proportions of the selected move with the average proportions of all the non-selected moves. It saves this information.
* When the game is over, the bot will alter the weights according to whether it won or lost, so that the most important criteria of the selected moves are made either more important or less important in future decision making.
