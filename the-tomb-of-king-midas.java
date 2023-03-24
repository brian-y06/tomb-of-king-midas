import java.util.Scanner;


class Main {
  public static void main(String[] args) {
    Scanner in = new Scanner (System.in);
    boolean gameRunning = true; // Start game
    String[] gameStages = {"Game Over", "Stage 1.1: Entrance", "Stage 1.2: Crimson Chamber", "Stage 1.3: The Tomb of Daedalus", "Game Complete"}; // Avoiding use of double in case of binary misrepresentation
    String gameStage; // Used to keep tract of which stage to run, it will also be used to reset the game 
    int stageIndex = 1; // Start the game at Stage 1.1 which is in index 1 of gameStages
    String username = ""; // Initalize variables to empty strings, the user will set them later in the code
    String partnerName = ""; // We need to create global variables because 
    String partnerShortName = "";
    String partnerWeapon = ""; 
    int[] characterHealths = {100, 100};
    String[] userItems = {"Empty", "Bat Uric Acid - Entrance", "A Bushel of Oleanders - Entrance", "Divine Spider Silk - Entrance" , "Old Sword - Entrance", "Minotaur Axe - Crimson Chamber", "800 Gold Coins - The Tomb of Daedalus"};
    String[] backpack = new String [3];
    boolean[] charactersAlive = {true, true}; // Index 0 is user, index 1 is partner
    int[] statChanges = new int [5]; // Will be used to keep tract of how much damage user and partner took after each stage and to store the items that the user picked up after each stage
    int highscore = 0; // Used to keep track of high scores, original is 0 
    do {
      gameStage = gameStages[stageIndex];
      if (gameStage.equals("Stage 1.1: Entrance")) {
        
        gameInit(in, highscore);
        characterHealths[0] = 100; // Setting healths to 100 at start of game, index 0 is user, index 1 is partner
        characterHealths[1] = 100; // Reset healths in case this is not the first run (play again)
        charactersAlive[0] = true; // Setting both players to alive, used to check whether the game should continue or not, index 0 is user, index 1 is partner 
        charactersAlive[1] = true; // Setting both players back to alive if this is not the first run (play again)
        backpack[0] = userItems[0]; // Set all the backpack slots to "Empty"
        backpack[1] = userItems[0]; // Reset all slots to empty in case this is not the first run (play again)
        backpack[2] = userItems[0];
        statChanges[0] = 0; // Reset health changes and backpack slots
        statChanges[1] = 0;
        statChanges[2] = 0;
        statChanges[3] = 0;
        statChanges[4] = 0;
        username = chooseUsername(in); // Let user choose their name and return as the username
        partnerName = choosePartner(in); // Let user choose their partner and return as the partnerName
        partnerShortName = shortenParterName(partnerName); // Shortens the partner name to just the first name for dialogue between characters
        partnerWeapon = partnerWeaponAssignment(partnerName); // Chooses partner weapon based on who the user choose
        
        gameStats(in, username, partnerName, partnerShortName, partnerWeapon, backpack, characterHealths, gameStage, false, charactersAlive); // Output current game status and stats
        statChanges = floorOneSword(in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges); // Start game, user picks up sword
        backpack = updateBackpack(statChanges, backpack, userItems); // sword is updated into their backpack 
        statChanges = floorOneEntrance(in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges); // Starts floor one with a sword, will return an array int[] statChanges which holds {healthChangeForUser, healthChangeForPartner, backpackSlot1, backpackSlot2, backpackSlot3}
        characterHealths = updateCharacterHealth(characterHealths, statChanges); // Update character healths with health changes found in statChanges index 0 and 1
        backpack = updateBackpack(statChanges, backpack, userItems); // update backpack with any new items
        gameStats(in, username, partnerName, partnerShortName, partnerWeapon, backpack, characterHealths, gameStage, true, charactersAlive); // Display game status with completed stage
        charactersAlive = checkAlive(characterHealths); // Check if characters are alive
        
        if (!charactersAlive[0] || !charactersAlive[1]) { // If one of them died
          /*
          If one of the chracters are dead they will have a characters alive of false. (!false) -> true
          and this code will run. The boolean checks if user is dead OR if partner is dead
          */
          String deadCharacter = ""; // Initalize new String so we can access outside of if statements
          if (charactersAlive[0] == false) {
            deadCharacter = username; // Dependings on who died set the deadCharacter to that character
          } else if (charactersAlive[1] == false) {
            deadCharacter = partnerName;
          }
          
          System.out.println(deadCharacter + " has lost all their hp. Game over."); // Print out who lost their hp
          swordArt(); // Print losing output
          printLose(); // print "You Lose" Ascii art
          gameStage = "Game Over";
        }
      } else if (gameStage.equals("Stage 1.2: Crimson Chamber")) {
        // Same sequence as first stage. Display stats, go into stage, update healths, display stats, check if any character died etc.
        gameStats(in, username, partnerName, partnerShortName, partnerWeapon, backpack, characterHealths, gameStage, false, charactersAlive); 
        statChanges = floorOneMinotaur(in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges);
        characterHealths = updateCharacterHealth(characterHealths, statChanges);
        backpack = updateBackpack(statChanges, backpack, userItems);
        gameStats(in, username, partnerName, partnerShortName, partnerWeapon, backpack, characterHealths, gameStage, true, charactersAlive); 
        charactersAlive = checkAlive(characterHealths);
        if (!charactersAlive[0] || !charactersAlive[1]) { // Not creating a method because we need to access both stageIndex and gameRunning which would return two different data types 
          String deadCharacter = "";
          if (charactersAlive[0] == false) {
            deadCharacter = username;
          } else if (charactersAlive[1] == false) {
            deadCharacter = partnerName;
          }
          // All same as first stage
          System.out.println(deadCharacter + " has lost all their hp. Game over.");
          swordArt(); // Print losing output
          printLose(); // print "You Lose" Ascii art
          gameStage = "Game Over";
        } else {
          minotaurComplete(in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack); // Run code for defeating the minotaur.
        }
       
      } else if (gameStage.equals("Stage 1.3: The Tomb of Daedalus")) {
        // Same sequence as first stage. Display stats, go into stage, update healths, display stats, check if any character died etc.
        gameStats(in, username, partnerName, partnerShortName, partnerWeapon, backpack, characterHealths, gameStage, false, charactersAlive); 
        statChanges = floorOneFinal(in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges, userItems);
        characterHealths = updateCharacterHealth(characterHealths, statChanges);
        backpack = updateBackpack(statChanges, backpack, userItems);
        gameStats(in, username, partnerName, partnerShortName, partnerWeapon, backpack, characterHealths, gameStage, true, charactersAlive); 
        charactersAlive = checkAlive(characterHealths);
        if (!charactersAlive[0] || !charactersAlive[1]) { // Not creating a method because we need to access both stageIndex and gameRunning which would return two different data types 
          String deadCharacter = "";
          if (charactersAlive[0] == false) {
            deadCharacter = username;
          } else if (charactersAlive[1] == false) {
            deadCharacter = partnerName;
          }
          // Same as first stage 
          System.out.println(deadCharacter + " has lost all their hp. Game over.");
          swordArt(); // Print losing output
          printLose(); // print "You Lose" Ascii art
          gameStage = "Game Over"; 
        }
      } else if (gameStage.equals("Game Complete")) { 
          System.out.println(" ▄▄·        ▐ ▄  ▄▄ • ▄▄▄   ▄▄▄· ▄▄▄▄▄▄• ▄▌▄▄▌  ▄▄▄▄▄▪         ▐ ▄ .▄▄ · ▄▄ ");
          System.out.println("▐█ ▌▪▪     •█▌▐█▐█ ▀ ▪▀▄ █·▐█ ▀█ •██  █▪██▌██•  •██  ██ ▪     •█▌▐█▐█ ▀. ██▌");
          System.out.println("██ ▄▄ ▄█▀▄ ▐█▐▐▌▄█ ▀█▄▐▀▀▄ ▄█▀▀█  ▐█.▪█▌▐█▌██▪   ▐█.▪▐█· ▄█▀▄ ▐█▐▐▌▄▀▀▀█▄▐█·");
          System.out.println("▐███▌▐█▌.▐▌██▐█▌▐█▄▪▐█▐█•█▌▐█ ▪▐▌ ▐█▌·▐█▄█▌▐█▌▐▌ ▐█▌·▐█▌▐█▌.▐▌██▐█▌▐█▄▪▐█.▀ ");
          System.out.println("·▀▀▀  ▀█▄▀▪▀▀ █▪·▀▀▀▀ .▀  ▀ ▀  ▀  ▀▀▀  ▀▀▀ .▀▀▀  ▀▀▀ ▀▀▀ ▀█▄▀▪▀▀ █▪ ▀▀▀▀  ▀ ");
        
          System.out.println("You have completed the game. Well done");
          highscore = characterHealths[0] * 15 + characterHealths[1] * 15;
          System.out.println("High Score: " + highscore);
      }
      if (!gameStage.equals("Game Over")) { // After every stage finishes running this code will run, unless the game is over, which is never true because the game is either in a stage or asking the user if they want to resstart
        String userContinueAns = ""; // for error trap
        do {
          System.out.println("Would you like to continue (yes/no): ");
          userContinueAns = in.nextLine();
        } while (!userContinueAns.equalsIgnoreCase("yes") && !userContinueAns.equalsIgnoreCase("no")); // Keep asking until the user answers yes or no
        if (userContinueAns.equalsIgnoreCase("yes")) {
          if (gameStage.equals("Game Complete")) {
            stageIndex = 1; // If user completed game, reset game when they want to continue
          } else {
            stageIndex++; // Else, loop will run again with the next double in the gameStages array
          }
          
        } else if (userContinueAns.equalsIgnoreCase("no")) {
          System.out.println("You have ended your game.");
          boolean playAgainAns = playAgain(in);
          if (playAgainAns) {
            stageIndex = 1; // Reset Game
          } else {
            System.out.println("Thank you for playing. See you soon.");
            swordArt(); // Print losing output
            printLose(); // print "You Lose" Ascii art
            gameRunning =  false;
            
          }
        }
      } else { // If gameStage is "Game Over" run this code
        boolean playAgainAns = playAgain(in);
        if (playAgainAns) {
          stageIndex = 1; 
        } else {
          System.out.println("Thank you for playing. See you soon.");
          swordArt(); // Print losing output
          printLose(); // print "You Lose" Ascii art
          gameRunning =  false;
        }
      }
    } while (gameRunning);
  }

  public static void swordArt ()  { //displayed when the game is over / when the user is killed
    System.out.println("                       _..._");
    System.out.println("                     /MMMMM\\");
    System.out.println("                    (I8H#H8I)");
    System.out.println("                    (I8H#H8I)");
    System.out.println("                     \\WWWWW/");
    System.out.println("                      I._.I");
    System.out.println("                      I._.I");
    System.out.println("                      I._.I");
    System.out.println("                      I._.I");
    System.out.println("                      I._.I");
    System.out.println("                      I._.I");
    System.out.println("                      I._.I");
    System.out.println("                      I.,.I");
    System.out.println("                     / /#\\ \\");
    System.out.println("                   .dH# # #Hb.");
    System.out.println("               _.~d#XXP I 7XX#b~,_");
    System.out.println("               _.~d#XXP I 7XX#b~,_");
    System.out.println("            _.dXV^XP^ Y X Y ^7X^VXb._");
    System.out.println("           /AP^   \\PY   Y   Y7/   ^VA\\");
    System.out.println("          /8/      \\PP  I  77/      \\8\\");
    System.out.println("         /J/        IV     VI        \\L\\");
    System.out.println("         L|         |  \\ /  |         |J");
    System.out.println("         V          |  | |  |          V");
    System.out.println("                    |  | |  |");
    System.out.println("                    |  | |  |");
    System.out.println("                    |  | |  |");
    System.out.println("                    |  | |  |");
    System.out.println("                    |  | |  |");
    System.out.println(" _                  |  | |  |                  _");
    System.out.println("( \\                 |  | |  |                 / )");
    System.out.println(" \\ \\                |  | |  |                / /");
    System.out.println("('\\ \\               |  | |  |               / /`)");
    System.out.println(" \\ \\ \\              |  | |  |              / / /");
    System.out.println("('\\ \\ \\             |  | |  |             / / /`)");
    System.out.println(" \\ \\ \\ )            |  | |  |            ( / / /");
    System.out.println("('\\ \\( )            |  | |  |            ( )/ /`)");
    System.out.println(" \\ \\ ( |            |  | |  |            | ) / /");
    System.out.println("  \\ \\( |            |  | |  |            | )/ /");
    System.out.println("   \\ ( |            |  | |  |            | ) /");
    System.out.println("    \\( |            |   Y   |            | )/");
    System.out.println("     | |            |   |   |            | |");
    System.out.println("     J | ___...~~--'|   |   |`--~~...___ | L");
    System.out.println("     >-+<...___     |   |   |     ___...>+-<");
    System.out.println("    //     __   `--~.L___L___J.~--'   __     \\");
    System.out.println("    K    /  ` --.     d===b     .-- '  \\\\    H");
    System.out.println("    \\_._/        \\\\   // I \\\\   /        \\\\_._/");
    System.out.println("      `--~.._     \\\\__\\\\ I //__/     _..~--'");
    System.out.println("             `--~~..____ ____..~~--'");
    System.out.println("                    |   T   |");
    System.out.println("                    |   |   |");
    System.out.println("                    |   |   |");
    System.out.println("                    |   |   |");
    System.out.println("                    |   |   |");
    System.out.println("                    |   |   |");
    System.out.println("                    |   |   |");
    System.out.println("                    |   |   |");
    System.out.println("                    |   |   |");
    System.out.println("                    |   |   |");
    System.out.println("                    |   |   |");
    System.out.println("                    I   '   I");
    System.out.println("                     \\     /");
    System.out.println("                      \\   /");
    System.out.println("                       \\ /");
  }
  
  public static void gameInit (Scanner in, int highscore) {
    System.out.println("Please enter full screen to receive the best user experience.");
    System.out.println("");
    
    System.out.println("Are you in full screen? (yes/no)"); // ask the user if they are in full screen
    String fullScreenCheck = in.nextLine(); // declare and initialize var fullScreenCheck

    while (!fullScreenCheck.equalsIgnoreCase("yes")){ // If the user's response is not yes...
      System.out.println("Please enter full screen."); // Remind them to enter full screen
      System.out.println("\nAre you in full screen? (yes/no)"); // then prompt them to enter another answer
      fullScreenCheck = in.nextLine(); // setting var fullScreenCheck to the user's new input
    }

    while ((!fullScreenCheck.equalsIgnoreCase("yes")) && (!fullScreenCheck.equalsIgnoreCase("no"))){ // If the user's answer isn't yes or no
      System.out.println("Please enter a valid response."); // ask them to enter a valid response
      System.out.println("\nAre you in full screen? (yes/no)"); // prompt userr to enter another answer
      fullScreenCheck = in.nextLine();
    }
    
    System.out.println("\nWelcome to..."); 
    System.out.println(""); // the following lines are ascii art of the name of our game
    
    System.out.println("\t\t\t\t   ▄▄▄▄▀ ▄  █ ▄███▄          ▄▄▄▄▀ ████▄ █▀▄▀█ ███       ████▄ ▄████  ");
    System.out.println("\t\t\t\t▀▀▀ █   █   █ █▀   ▀      ▀▀▀ █    █   █ █ █ █ █  █      █   █ █▀   ▀ ");
    System.out.println("\t\t\t\t    █   ██▀▀█ ██▄▄            █    █   █ █ ▄ █ █ ▀ ▄     █   █ █▀▀    ");
    System.out.println("\t\t\t\t   █    █   █ █▄   ▄▀        █     ▀████ █   █ █  ▄▀     ▀████ █      ");
    System.out.println("\t\t\t\t   ▀        █  ▀███▀         ▀               █  ███              █    ");
    System.out.println("\t\t\t\t           ▀                                ▀                     ▀   ");

    System.out.println("");
    System.out.println("");
    
    System.out.println("  ▄█   ▄█▄  ▄█  ███▄▄▄▄      ▄██████▄         ▄▄▄▄███▄▄▄▄    ▄█  ███▄▄▄▄    ▄██████▄     ▄████████  ");
    System.out.println("  ███ ▄███▀ ███  ███▀▀▀██▄   ███    ███      ▄██▀▀▀███▀▀▀██▄ ███  ███▀▀▀██▄ ███    ███   ███    ███ ");
    System.out.println("  ███▐██▀   ███▌ ███   ███   ███    █▀       ███   ███   ███ ███▌ ███   ███ ███    ███   ███    █▀  ");
    System.out.println(" ▄█████▀    ███▌ ███   ███  ▄███             ███   ███   ███ ███▌ ███   ███ ███    ███   ███        ");
    System.out.println(" ▀█████▄    ███▌ ███   ███ ▀▀███ ████▄       ███   ███   ███ ███▌ ███   ███ ███    ███ ▀███████████ ");
    System.out.println("  ███▐██▄   ███  ███   ███   ███    ███      ███   ███   ███ ███  ███   ███ ███    ███          ███ ");
    System.out.println("  ███ ▀███▄ ███  ███   ███   ███    ███      ███   ███   ███ ███  ███   ███ ███    ███    ▄█    ███ ");
    System.out.println("  ███   ▀█▀ █▀    ▀█   █▀    ████████▀        ▀█   ███   █▀  █▀    ▀█   █▀   ▀██████▀   ▄████████▀ ");
    System.out.println("  ▀  ");
    System.out.println("");


    // display information to help user better understand the game (instructions/rules, goal, highscore)
    System.out.println("Instructions/Rules: \n\t- Both you and your partner have to be safe and alive by the end of the game. You can be missing some health, but you can’t have any life-lasting injuries. ");
    System.out.println("\t- Press enter when prompted to or when the game pauses. \n\t- When you are prompted to choose between paths, please only respond with a word from the choices in brackets. \n\t- Remember that the choices you make are important, some give you better chances of survival!!");

    System.out.println("\nGoal: to conquer the dungeon and get the treasure.");
    System.out.println("Highscore: " + highscore);
    System.out.println("(Press Enter to continue.)");
    in.nextLine();
    
  }

  public static boolean playAgain (Scanner in) {
    boolean playAgain = true; // Must be set to some value in order to be initialized, irrelevant to the outcome
    boolean validAns = false; // Used to error trap
    do {
      
      System.out.println("Would you like to play again");
      String playAgainAns = in.nextLine(); // declaring and initializing var playAgainAns
      if (playAgainAns.equalsIgnoreCase("yes")) { // set the two checks to true if the user responds with yes
        playAgain = true; // if yes -> true
        validAns = true; // Once playAgainAns is yes or no validAns is true
      } else if (playAgainAns.equalsIgnoreCase("no")) { // if the user doesn't want to replay the game (responds with "no"), set play again to false and valid answer to true
        playAgain = false; // if no -> false
        validAns = true;
      } else { // if the user's response isn't a yes or no, ask the user to enter a valid answer
        System.out.println("Please Enter A Valid Answer");
      }
    } while (validAns == false);
    return playAgain; // Return the answer in boolean form to the function call
  }

  public static String chooseUsername (Scanner in) { // method for user to input their username
    String username = ""; // Initialize outside of do-while loop for error trap
    do {
      System.out.print("Enter your username, then press Enter to continue: ");
      username = in.nextLine();
    } while (username.length() == 0); // keep asking until the user enters a username

    // the following lines format the username that the user entered; makes it upper cased
    username = username.toLowerCase(); // Set all to lowercase
    char upperCaseFirstLetter = (char) ((int) (username.charAt(0)) - 32); // Capitalize first letter
    String formattedUsername = "";
    formattedUsername = formattedUsername + upperCaseFirstLetter; // Add uppercase first letter to String
    for (int x = 1; x < username.length(); x++) {
      formattedUsername = formattedUsername + username.charAt(x); // Accumulate the rest of the username in lowercase
    }
    return formattedUsername;
  }

  public static String choosePartner (Scanner in) {
    String partnerChoice; // For do-while condition
    do {
      System.out.println("\nChoose your partner (ENTER A NUMBER): "); 
      // the following lines of code introduce the character options and their abilities
      System.out.println("\t1. Merlin the Sorcerer. By having a sorcerer as a partner, you will get an extra chance to guess when asked a question.");
      System.out.println("\t2. Emilia the Healer. By having a healer as a partner, you will be able to retain your health for a longer period of time.");
      System.out.println("\t3. Callisto the Knight. By having a knight as a partner, you and your partner will receive less damage from the enemies."); 
      System.out.println("\nChoose wisely!");
      partnerChoice = in.nextLine();
      
      if (partnerChoice.equalsIgnoreCase("Merlin the Sorcerer") || partnerChoice.equalsIgnoreCase("1")) { // Error trap
        partnerChoice = "Merlin the Sorcerer"; // For do-while loop condition
        
      } else if (partnerChoice.equalsIgnoreCase("Emilia the Healer") || partnerChoice.equalsIgnoreCase("2")) { // Error trap
        partnerChoice = "Emilia the Healer";
        
      } else if (partnerChoice.equalsIgnoreCase("Callisto the Knight") || partnerChoice.equalsIgnoreCase("3")) { // Error trap
        partnerChoice = "Callisto the Knight"; 
        
      } else { // Error trapping if the user enters an option that's not offered
        System.out.println("That is not a valid choice. Enter your choice: ");
      }
    } while (!partnerChoice.equalsIgnoreCase("Merlin the Sorcerer") && !partnerChoice.equalsIgnoreCase("Emilia the Healer") && !partnerChoice.equalsIgnoreCase("Callisto the Knight"));
    return partnerChoice;
  }

  public static String shortenParterName (String partnerName) { // takes the first word of the initial name that was displayed to the user
    int spaceIndex = partnerName.indexOf(' '); // First first instance of the space
    String partnerShortName = "";
    for (int x = 0; x < spaceIndex; x++) {
      partnerShortName = partnerShortName + partnerName.charAt(x); // Accumulate from partnerName until before the space
    }
    return partnerShortName;
  }

  public static String partnerWeaponAssignment (String partnerName) { // assigning all of the partners a weapon
    String partnerWeapon = ""; // initialize with default values, add characters into it after checking who the partner is
    if (partnerName.equalsIgnoreCase("Merlin the Sorcerer")) {
      partnerWeapon = "staff"; 
      
    } else if (partnerName.equalsIgnoreCase("Emilia the Healer")) { 
      partnerWeapon = "dagger"; 
      
    } else if (partnerName.equalsIgnoreCase("Callisto the Knight")) { 
      partnerWeapon = "sword"; 
    } 
    return partnerWeapon;
  }

  public static int[] updateCharacterHealth (int[] characterHealths, int[] statChanges) {
    // adding the changes of health to the original/previous health
    characterHealths[0] = characterHealths[0] + statChanges[0]; 
    characterHealths[1] = characterHealths[1] + statChanges[1];
    return characterHealths;
  }

  public static String[] updateBackpack (int[] statChanges, String[] backpack, String[] userItems) {
    backpack[0] = userItems[statChanges[2]]; // Set the backpack slot to the user item given by the index of the value of statChanges
    backpack[1] = userItems[statChanges[3]];
    backpack[2] = userItems[statChanges[4]];
    return backpack; // Return updated backpack
  }

  public static boolean[] checkAlive (int[] characterHealths) {
    boolean[] charactersAlive = {characterHealths[0] > 0, characterHealths[1] > 0};
    return charactersAlive;
  }

  public static void gameStats (Scanner in, String username, String partnerName, String partnerShortName, String partnerWeapon, String[] backpack, int[] characterHealths, String gameStage, boolean stageComplete, boolean[] charactersAlive) {
    String stageCompleteStr;
    if (stageComplete) {
      stageCompleteStr = "true";
    } else {
      stageCompleteStr = "false";
    }
    if (characterHealths[0] < 0) { // If the character has negative health, display 0 instead of the negative number;
      characterHealths[0] = 0;
    }
    if (characterHealths[1] < 0) {
      characterHealths[1] = 0;
    }
    System.out.println("GAME STATUS: ");
    System.out.println("-------------------"); 
    System.out.println(gameStage);
    System.out.println("Stage Complete: " + stageCompleteStr);
    System.out.println("Current Healths: \n  " + username + ": " + characterHealths[0] + "\n  " + partnerName + ": " + characterHealths[1]);
    System.out.println("Item(s):");
    System.out.println(username + " (backpack): ");
    for (int x = 0; x < backpack.length; x++) {
      int itemNum = x + 1;
      System.out.println("  " + itemNum + ": "+ backpack[x]);
    }
    System.out.println(partnerName + ":\n  1: " + partnerWeapon);
    System.out.println("-------------------");
    System.out.println("(Press Enter to continue.)");
    in.nextLine();
  }
  // Before entering entrance, we need to grab a sword and return to main to update the backpack so items will fall into the right place.
  public static int[] floorOneSword (Scanner in, String username, String partnerName, String partnerShortName, String partnerWeapon, int[] characterHealths, String[] backpack, int[] statChanges) {
    System.out.println("\n«Welcome " + username + ". You've arrived in the dungeon.»");
    System.out.println("(Press Enter to continue.)");
    in.nextLine();

    System.out.println("\nYou pick up an old sword you find lying in the hands of a skeleton.");
    statChanges[2] = 4; // Set the first index of the backpack to the old sword. Index 4 is the value "Old Sword - Entrance". 
    return statChanges;
  }
    
  public static int[] floorOneEntrance (Scanner in, String username, String partnerName, String partnerShortName, String partnerWeapon, int[] characterHealths, String[] backpack, int[] statChanges) {
    
    
    System.out.println("(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("As you look around, it appears that you entered a dark cave.\n\nThere's a narrow path several feet away from you, \n\nand you also notice strange dotted animal footprints nearby.\n\nYou close your eyes and you can hear the faint sound of water rushing, and mice scurrying around.\n\nA breeze of wind hits your shoulder and you open your eyes shivering.");
    System.out.println("(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println(partnerShortName + ": Come on " + username + ", we need to move.");
    
    System.out.println("\n«What do you want to do (ENTER A NUMBER)?»\n\t1. Follow the path in front of you.\n\t2. Follow the sound of the water.\n\t3. Follow the animal footprints.");
    int userChoice = in.nextInt();
    in.nextLine(); // Reading in next line

    isNumOutOfRangeOfThree(in, userChoice);
    
    // Stat changes contains 5 values. 1: Health Changes to user 2: Health changes to partner 3: Backpack Item 1 [gameItems index] 4: Backpack Item 2 [gameItems index]  5: Backpack Item 3 [gameItems index] 
    if (userChoice == 1) {
      statChanges = entranceChoice1(in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges);
    } else if (userChoice == 2)  {
      statChanges = entranceChoice2(in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges);
    } else if (userChoice == 3) {
      statChanges = entranceChoice3(in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges);
    };
    return statChanges;
  };

  public static int[] entranceChoice1 (Scanner in, String username, String partnerName, String partnerShortName, String partnerWeapon, int[] characterHealths, String[] backpack, int[] statChanges) {
    System.out.println("«Choice 1 has been chosen: You follow the path in front of you...»");
    System.out.println("(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println(username +  ": Alright, let’s follow the path here."); // whats goin in here. "who commented this?" -Grace
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("You and " + partnerName + " walk for a few minutes. It isn’t very different from the start of the floor. All there was to see was more empty cave areas.");
    System.out.println("(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println(partnerName + ": Hey " + username + ", do you hear that?\n");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println (username + ": What do you mean?");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("The scurrying noises you heard before that you though were mice started to get louder.\n\nThe scurrying turned into screeches and bats started to fly in your direction.\n");
    System.out.println(partnerName + ": " + username + " DUCK!");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println(partnerName + " pulls out their " + partnerWeapon + " and gets ready to fight. \n\nThey handle the first wave of bats, but they keep coming.");
    System.out.println("(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("«You need to defend yourself. Choose a category and correctly guess the given word to get a weapon.» \n1. Terrestrial animals \n2. Aquatic animals \n3. Plants");
    int userChoice = in.nextInt(); 
    in.nextLine(); // Reading next line

    isNumOutOfRangeOfThree(in, userChoice);

    int[] healerChanges = {0,0};
    int[] sorcererChanges = {-15, -15};
    int[] knightChanges = {-10, -10};
    if (userChoice == 1) {
      String[] terrestrialAnimals = {"elephant", "rhino", "zebra", "snake", "giraffe", "squirrel"};
      String challengeStatus = hangman (in, "Terrestrial Animals", partnerName, terrestrialAnimals, 1);
      statChanges = updateHealthChanges(challengeStatus, partnerName, healerChanges, sorcererChanges, knightChanges, statChanges);
      statChanges = f1C1FinishedMessage(in, challengeStatus, userChoice, username, partnerName, partnerShortName, statChanges, backpack); // floor 1 choice 1 finished message
    } else if (userChoice == 2)  {
      String[] aquaticAnimals = {"dolphin", "whale shark", "orca", "seahorse", "tuna", "octopus", "sea cucumber", "king oyster", "plankton"};
      String challengeStatus = hangman (in, "Aquatic Animals", partnerName, aquaticAnimals, 1);
      statChanges = updateHealthChanges(challengeStatus, partnerName, healerChanges, sorcererChanges, knightChanges, statChanges);
      statChanges = f1C1FinishedMessage(in, challengeStatus, userChoice, username, partnerName, partnerShortName, statChanges, backpack); // floor 1 choice 2 finished message
    } else if (userChoice == 3) {
      String[] plants = {"sunflower", "rose", "cedar tree", "tulip", "poppy", "orchid", "cactus", "locust tree", "hogweed"};
      String challengeStatus = hangman (in, "Plants", partnerName, plants, 1);
      statChanges = updateHealthChanges(challengeStatus, partnerName, healerChanges, sorcererChanges, knightChanges, statChanges);
      statChanges = f1C1FinishedMessage(in, challengeStatus, userChoice, username, partnerName, partnerShortName, statChanges, backpack); // floor 1 choice 3 finished message
    };
    return statChanges;
  };
  
  public static int [] entranceChoice2 (Scanner in, String username, String partnerName, String partnerShortName, String partnerWeapon, int[] characterHealths, String[] backpack, int[] statChanges) { // changed public static void to public static int []
    System.out.println("\n«Choice 2 has been chosen: You follow the sounds of the water...»"); // vic added the texts for choice 2 :]
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("\n" + username +  ": Alright, let’s follow the sound of the water."); 
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("\nYou and " + partnerName + " descend several hills and through some dead ends. You reach a small pond with a few ducklings and surrounded by a beautiful area of pink flowers.");

     System.out.println("      _.-.       ");
    System.out.println("    -'    '      .-'-.");
    System.out.println("  .',      '    '     '");
    System.out.println("  ', `,     .  '.-.   '");
    System.out.println("   '   \\    ' .\"   \".'");
    System.out.println("    '.' \\   ;.\",    \"-._");
    System.out.println("     '   '. ,\"  \"-.\"    '.");
    System.out.println("      _.--'.    .\" ,.--.  .");
    System.out.println("   , '     \"-..\".-'     \\ '");
    System.out.println(" -`     _.''\".    ' .    '");
    System.out.println("'     -'   \"  '-     '.");
    System.out.println("'    '    \"     '      '");
    System.out.println(" '.'     \"       '    .'");
    System.out.println("   ',    \"        ' .'");
    System.out.println("         \"        ,'");
    System.out.println("         \"");
    System.out.println("         \"");
    System.out.println("         \"");
    System.out.println("         \"");
    System.out.println("       _.\"._");
    
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("\n" + partnerName + ": It's so peaceful and gorgeous here, how could it be a dungeon?");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println ("\n" +username + ": Be careful " + partnerShortName + ", there's something suspicious about this area...");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("\n" +partnerName + " walks towards the flowers and gets closer to smell them.\nAll of a sudden they start to cough and choke violently.");
    System.out.println("\nYou rush to " + partnerName + "'s side and immediately pull them away from the flowers.\nA few in the back were burning gently and the fumes are circulating in the air.");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("\n" +username + ": Are you ok " + partnerShortName + "? tch- Oleanders. I should have known they're poisonous.\n" + partnerName + " nods their head a bit while continuing to cough.");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("«You need to help " + partnerName + " and protect yourself. Choose a category and correctly guess the given word to get a healing potion.» \n1. Geographical region \n2. Famous scientists \n3. Parts of the nervous system");
    int userChoice = in.nextInt(); 
    in.nextLine(); // Reading next line

    isNumOutOfRangeOfThree(in, userChoice);

    int[] healerChanges = {-15, -15};
    int[] sorcererChanges = {-10, -35};
    int[] knightChanges = {-30, -5};
    if (userChoice == 1) {
      String[] geographicalRegion = {"africa", "asia", "oceania", "north america", "eastern europe"}; 
      String challengeStatus = hangman (in, "Geographical Region", partnerName, geographicalRegion, 1);
      statChanges = updateHealthChanges(challengeStatus, partnerName, healerChanges, knightChanges, sorcererChanges, statChanges);
      statChanges = f1C2FinishedMessage(in, challengeStatus, userChoice, username, partnerName, partnerShortName, statChanges, backpack);
    } else if (userChoice == 2)  {
      String[] famousScientist = {"albert einstein", "isaac newton", "charles darwin", "sigmund freud", "margaret mead"}; // need to fix this hehe 
      String challengeStatus = hangman (in, "Famous Scientist", partnerName, famousScientist, 1);
      statChanges = updateHealthChanges(challengeStatus, partnerName, healerChanges, knightChanges, sorcererChanges, statChanges);
      statChanges = f1C2FinishedMessage(in, challengeStatus, userChoice, username, partnerName, partnerShortName, statChanges, backpack);
    } else if (userChoice == 3) {
      String[] partOfNervousSystem = {"brain", "spinal cord", "neurons", "spine", "nerves"}; // need to fix this hehe 
      String challengeStatus = hangman (in, "Parts Of The Nervous System", partnerName, partOfNervousSystem, 1);
      statChanges = updateHealthChanges(challengeStatus, partnerName, healerChanges, knightChanges, sorcererChanges, statChanges);
      statChanges = f1C2FinishedMessage(in, challengeStatus, userChoice, username, partnerName, partnerShortName, statChanges, backpack);
      
    };
    return statChanges;
  };
  
  public static int [] entranceChoice3 (Scanner in, String username, String partnerName, String partnerShortName, String partnerWeapon, int[] characterHealths, String[] backpack, int[] statChanges) { // changed public static void to public static int []
    System.out.println("\n«Choice 3 has been chosen: You follow the animal footprints...»"); // vic also edited the text here :D
    System.out.println("(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("\n" +username +  ": Alright, let’s hunt this animal."); 
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("\nYou and " + partnerName + " follow the small animal footprints.");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("\n" + partnerName + ": Each print is a collection of several dots. What kind of animal do you think this is?");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println ("\n" +username + ": I’m not too sure, it doesn’t seem like a four legged animal though. Maybe an insect of some sort?");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("\nThe two of you start to hear a faint click-clack and turn your heads to a dark shadow at the end of the path. \nThe clicks and clacks get louder, and the big eight-legged creatures are revealed. Giant Spiders.");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("\n" + partnerName + " pulls out their " + partnerWeapon + " and gets ready to fight. \nThe spiders start to shoot acid and webs at you and " + partnerName + ".");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("\n«You need to slay the spiders. Choose a category and correctly guess the given word to get a waepon.» \n1. Terrestrial animals \n2. Aquatic animals \n3. Plants");
    int userChoice = in.nextInt(); 
    in.nextLine(); // Reading next line

    isNumOutOfRangeOfThree(in, userChoice);
    
    int[] healerChanges = {-10, -10};
    int[] sorcererChanges = {-35, -5};
    int[] knightChanges = {-30, 0};
    if (userChoice == 1) {
      String[] terrestrialAnimals = {"elephant", "rhino", "zebra", "snake", "giraffe", "squirrel"};  
      String challengeStatus = hangman (in, "Terrestrial Animals", partnerName, terrestrialAnimals, 1);
      statChanges = updateHealthChanges(challengeStatus, partnerName, healerChanges, knightChanges, sorcererChanges, statChanges);
      statChanges = f1C3FinishedMessage(in, challengeStatus, userChoice, username, partnerName, partnerShortName, statChanges, backpack);
      
    } else if (userChoice == 2)  {
      String[] aquaticAnimals = {"dolphin", "whale shark", "orca", "seahorse", "tuna", "octopus", "sea cucumber", "king oyster", "plankton"};
      String challengeStatus = hangman (in, "Aquatic Animals", partnerName, aquaticAnimals, 1);
      statChanges = updateHealthChanges(challengeStatus, partnerName, healerChanges, knightChanges, sorcererChanges, statChanges);
      statChanges = f1C3FinishedMessage(in, challengeStatus, userChoice, username, partnerName, partnerShortName, statChanges, backpack);
    } else if (userChoice == 3) {
      String[] plants = {"sunflower", "rose", "cedar tree", "tulip", "poppy", "orchid", "cactus", "locust tree", "hogweed"};
      String challengeStatus = hangman (in, "Plants", partnerName, plants, 1);
      statChanges = updateHealthChanges(challengeStatus, partnerName, healerChanges, knightChanges, sorcererChanges, statChanges);
      statChanges = f1C3FinishedMessage(in, challengeStatus, userChoice, username, partnerName, partnerShortName, statChanges, backpack);
    }
    return statChanges;
  }

  public static int[] floorOneMinotaur (Scanner in, String username, String partnerName, String partnerShortName, String partnerWeapon, int[] characterHealths, String[] backpack, int[] statChanges) {
    System.out.println("The two of you get up and continue to move. After a couple of minutes, you two start to hear screams.");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("You approach slowly and the screams start to get quieter, until is just silence. \nYou take a peek into the area and see a large minotaur, at least 8 feet tall, \ncarrying an axe standing above several motionless bodies.\n");

    System.out.println("       -\"\"\\");
    System.out.println("    .-\"  .`)     (");
    System.out.println("   j   .'_+     :[                )      .^--..");
    System.out.println(" ,\" .:j         `8o  _,,+.,.--,   d|   `:::;    b");
    System.out.println(" ,\" .:j         `8o  _,,+.,.--,   d|   `:::;    b");
    System.out.println(" i  :'|          \"88p;.  (-.\"_\"-.oP        \\.   :");
    System.out.println(" ; .  (            >,%%%   f),):8\"          \\:'  i");
    System.out.println("i  :: j          ,;%%%:; ; ; i:%%%.,        i.   `.");
    System.out.println("i  `: ( ____  ,-::::::' ::j  [:```          [8:   )");
    System.out.println("<  ..``'::::8888oooooo.  :(jj(,;,,,         [8::  <");
    System.out.println("`. ``:.      oo.8888888888:;%%%8o.::.+888+o.:`:'  |");
    System.out.println(" `.   `        `o`88888888b`%%%%%88< Y888P\"\"'-    ;");
    System.out.println("   \"`---`.       Y`888888888;;.,\"888b.\"\"\"..::::'-'");
    System.out.println("          \"-....  b`8888888:::::.`8888._::-\"");
    System.out.println("             `:::.  `:::::O:::::::.`%%'|");
    System.out.println("              `.      \"``::::::''    .'");
    System.out.println("                `.                   <");
    System.out.println("                  +:         `:   -';");
    System.out.println("                   `:         : .::/");
    System.out.println("                    ;+_  :::. :..;;;");
    System.out.println("                    ;;;;,;;;;;;;;,;;");
    
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();

    System.out.println("\nYou turn to " + partnerName + ". Both of you are nervous, but you know you're one of the strongest adventurers in the city.");
    System.out.println("\n" + username + ": Let's go bring down the minotaur.");

    System.out.println("\n«How do you want to attack the minotaur?»\n1. Charge at it with your sword, then get your partner to attack it when it's facing you.\n2. Release poison to weaken it then deal the finishing strike.\n3. Wait for it to go to sleep then attack it when it's most vulnerable.");
    int userChoice = in.nextInt(); 
    in.nextLine(); // Reading in next line

    isNumOutOfRangeOfThree(in, userChoice);
    
    if (userChoice == 1) {
        statChanges = minotaurAttack1(in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges);
    } else if (userChoice == 2 && (backpack[0].equals("A Bushel of Oleanders - Entrance") || backpack[1].equals("A Bushel of Oleanders - Entrance") || backpack[2].equals("A Bushel of Oleanders - Entrance")))  {
      // They may only choose option 2 if they have the proper item
        statChanges = minotaurAttack2(in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges);
    } else if (userChoice == 2)  {
      // If they don't have the item force user to choose 1 or 3
        System.out.println("You do not have \"A Bushel of Oleanders - Entrance\" in your backpack.");
        System.out.println("\n«How do you want to attack the minotaur?»\n1. Charge at it with your sword, then get your partner to attack it when it's facing you.\n3. Wait for it to go to sleep then attack it when it's most vulnerable.");
        int userChoice2 = in.nextInt(); 
        in.nextLine(); // Reading next line

        isNumOutOfRangeOfThree(in, userChoice2); 
      
      if (userChoice2 == 1) {
          statChanges = minotaurAttack1(in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges);
      } else if (userChoice2 == 3) {
          statChanges = minotaurAttack3(in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges);
      }
    } else if (userChoice == 3) {
      statChanges = minotaurAttack3(in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges);
    } 
    
    return statChanges;
  }

  public static int[] updateHealthChanges (String challengeStatus, String partnerName, int[] healerChanges, int[] sorcererChanges, int[] knightChanges, int[] statChanges) {
    if (challengeStatus.equals("failed")) { // We will only update the health if the user failed the challenge
      if (partnerName.equalsIgnoreCase("Emilia the Healer")) { 
        statChanges[0] = healerChanges[0]; // Index 0 is user
        statChanges[1] = healerChanges[1];
      } else if (partnerName.equalsIgnoreCase("Callisto the Knight")) { 
        statChanges[0] = knightChanges[0]; // Index 0 is user
        statChanges[1] = knightChanges[1]; // Index 1 is partner
      } else if (partnerName.equalsIgnoreCase("Merlin the Sorcerer")) {
        statChanges[0] = sorcererChanges[0];
        statChanges[1] = sorcererChanges[1];
      }
    } 
    return statChanges;
  }
  
  public static int[] f1C1FinishedMessage (Scanner in, String challengeStatus, int userChoice, String username, String partnerName, String partnerShortName, int[] statChanges, String[] backpack) {
    if (challengeStatus.equals("passed")) {
      if (userChoice == 1) {
        System.out.println("\nYou stand firmly sword in hand, and defend successfully against the swarm of bats");
      } else if (userChoice == 2) {
        System.out.println("\nA large barrier is enabled, protecting you and " + partnerName);
      } else if (userChoice == 3) {
        System.out.println("\nYou unleash a powerful wind attack spell completely wiping out the bats.");
      }
      System.out.println("\n" + partnerName + ": Thank you for the protection!");
      System.out.println("\n" + username + ": No worries, we're a team after all! C'mon let's continue.");
      System.out.println("\n" + partnerName + ": Wait " + username + ", there are some small vials here.");
      System.out.println("\n" + "You pick up the vial and open the lid. You pinch your nose and gag");
      System.out.println("\n" + username + ": What is that awful stench?");
      System.out.println("\n" + partnerName + ": let's take it, it could help future battles");

      System.out.println("      _____");
      System.out.println("     `.___,'");
      System.out.println("      (___)");
      System.out.println("      <   >");
      System.out.println("       ) (");
      System.out.println("      /`-.\\");
      System.out.println("     /     \\");
      System.out.println("    / _    _\\");
      System.out.println("   :,' `-.' `:");
      System.out.println("   |         |");
      System.out.println("   :         ;");
      System.out.println("    \\       /"); // illegal escape char FIXED
      System.out.println("     `.___.'");
      
      System.out.println("\n" + "«Would you like to pick up the Uric Acid (Yes/No)»");
      String uricAcidAns = in.nextLine();
      if (uricAcidAns.equalsIgnoreCase("yes")) {
        // Find empty backpack
        int emptyIndex = -1;
        for (int x = 0; x < backpack.length; x++) {
          if (backpack[x].equals("Empty")) {
            emptyIndex = x; // Once you find the empty index, set that index to the first empty index and break out of loop so you don't overwrite the index. 
            break;  
          }
        }
       
        statChanges[2 + emptyIndex] = 1; // Set the index of the first empty slot to 1 in stat changes. statChanges[healthChangesUser, helathChangesPartner, backpackSlot1, backpackslot2, backpackslot3]
        // for (int x = 0; x < statChanges.length; x++) {
        //   System.out.println(statChanges[x]);
        // }
      } 
    } else if (challengeStatus.equals("failed")) {
      System.out.println("\n" + username + ": ugh, where did those bats come from.");
      System.out.println("\n" + partnerName + ": I'm not sure, but there might be more, let's continue with caution.");
    }
    return statChanges;
  }

  public static int[] f1C2FinishedMessage (Scanner in, String challengeStatus, int userChoice, String username, String partnerName, String partnerShortName, int[] statChanges, String[] backpack) {
    if (challengeStatus.equals("passed")) {
      System.out.println("\n" + partnerName + ": Thank you for saving me, I’ll make sure to be more careful going forward.");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();
      
      System.out.println("\n" + username + ": No worries, we’re a party! C’mon let’s collect some Oleanders then continue.");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();

      System.out.println("\n«Would you like to pick up the Oleander? (Yes/No)»");
      String oleanderAns = in.nextLine();
      
      if (oleanderAns.equalsIgnoreCase("yes")) {
        // Find empty backpack
        int emptyIndex = -1;
        for (int x = 0; x < backpack.length; x++) {
          if (backpack[x].equals("Empty")) {
            emptyIndex = x; // Once you find the empty index, set that index to the first empty index and break out of loop so you don't overwrite the index. 
            break;  
          }
        }
        
        statChanges[2 + emptyIndex] = 2; // Set the index of the first empty slot to 2 in stat changes. statChanges[healthChangesUser, helathChangesPartner, backpackSlot1, backpackslot2, backpackslot3]
        // for (int x = 0; x < statChanges.length; x++) {
        //   System.out.println(statChanges[x]);
        // }
      }     
    } else if (challengeStatus.equals("failed")) {
      System.out.println("\n" + username + ": has the pain subsided now?");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();

      System.out.println("\n" + partnerName + " nods their head weakly and starts standing up.");
      System.out.println("\n" + partnerName + ": it’s alright now, we can continue moving.");
    }
    return statChanges;
  }

  public static int[] f1C3FinishedMessage (Scanner in, String challengeStatus, int userChoice, String username, String partnerName, String partnerShortName, int[] statChanges, String[] backpack) {
    if (challengeStatus.equals("passed")) {
      if (userChoice == 1) {
      System.out.println ("\nYou run through the mob of giant spiders slice them down.");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();
        
      } else if (userChoice == 2) {
      System.out.println ("\nYou recite a short chant and a rain of fireballs pours down on the mob of spiders. They’re burnt to ashes, and you and "+ partnerName +" are safe.");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();
        
      } else if (userChoice == 3) {
      System.out.println ("\nYou run through the mob of giant spiders slice them down. If they don’t die immediately, they slowly writhe in pain as they succumb to the poison.");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();
      }

      System.out.println("\n" + partnerName + ": Wow that was so cool!!");
      System.out.println("\n" + username + ": Ahh~ you're making me blush.");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();

      System.out.println("\n" + username + ": Oh wait! let’s gather some spider silk before we go. It could be useful in trapping future creatures.");
      System.out.println("\n" + partnerName + " nods their head in agreement, and you let out a mischievous chuckle.");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();
      
      System.out.println("\n«Would you like to pick up the Divine Spider Silk? (Yes/No)»");
      String spiderSilkAns = in.nextLine();
      
      if (spiderSilkAns.equalsIgnoreCase("yes")) {
        // Find empty backpack
        int emptyIndex = -1;
        for (int x = 0; x < backpack.length; x++) {
          if (backpack[x].equals("Empty")) {
            emptyIndex = x; // Once you find the empty index, set that index to the first empty index and break out of loop so you don't overwrite the index. 
            break;  
          }
        }
        
        statChanges[2 + emptyIndex] = 3; // Set the index of the first empty slot to 2 in stat changes. statChanges[healthChangesUser, helathChangesPartner, backpackSlot1, backpackslot2, backpackslot3]
        // 3 is the index of Divine Spider Silk in userItems
        /* DEBUGGING
        for (int x = 0; x < statChanges.length; x++) {
          System.out.println(statChanges[x]);
        }*/    
      }
      
    } else if (challengeStatus.equals("failed")) {
    System.out.println("\nYou absorbed most of the attacks from the spiders, but received a lot of damage.\n" + partnerName + " runs up to you in worry.");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
      
    System.out.println("\n" + partnerName + ": Are you ok?");
    System.out.println("\n" + username + ": It's not too bad, let's continue.");
    }
    return statChanges;
  }

  public static int[] minotaurAttack1 (Scanner in, String username, String partnerName, String partnerShortName, String partnerWeapon, int[] characterHealths, String[] backpack, int[] statChanges) {
    int minotaurHealth = 100;

    String challengeStatus = ""; // So we can access the variable outside of the if statements
    int[] healerChanges = {-100, -100}; // If the user loses they lose the game
    int[] sorcererChanges = {-100, -100};
    int[] knightChanges = {-100, -100};
    System.out.println("\n" + username + ": Here's the plan. I'll charge in as a distraction, and once I see an opening or if the minotaur's back is facing you, I'll give you a signal. When I do, release your most powerful attack.");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("\n" + partnerName + ": Will you be alright?");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println(username + ": Don't worry, I can handle a minotaur.");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    boolean initiated = initiateMinotaur(in);
    if (initiated == true) {
      System.out.println("\n" + "You smirk confidently and scream to grab the minotaur’s attention. You charge at it with your sword by your side. You jump off of the wall and launch yourself at the beast. You slash at its torso and arms, quickly preventing it from picking up its axe. The minotaur howls in pain and its eyes redden.");

      System.out.println("   ,:\\      /:.");
      System.out.println("  //  \\_()_/  \\");
      System.out.println(" ||   |    |   ||");
      System.out.println(" ||   |    |   ||");
      System.out.println(" ||   |____|   ||");
      System.out.println("  \\\\  / || \\  //");
      System.out.println("   `:/  ||  \\;'");
      System.out.println("        ||");
      System.out.println("        ||");
      System.out.println("        XX");
      System.out.println("        XX");
      System.out.println("        XX");
      System.out.println("        OO\n");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();
      
      System.out.println("\n" + "The minotaur has lost 20% of it's hp");
      minotaurHealth = minotaurHealth - 20;
      System.out.println("Minotaur Remaining Health: " + minotaurHealth + "%");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();
      
      System.out.println("\nIt claws at you, but you dodge its grasp and slice the area behind its knees. The minotaur falls down, but manages to grab you.");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();

      System.out.println(username + ": NOW!");
      System.out.println("«You successfully gave your partner the signal, but how does the attack go? Complete the challenge to decide the effectiveness of your partner’s attacks.»");
      String[] greekGods = {"zeus", "ares", "poseidon", "hades", "apollo", "artemis"};
      challengeStatus = hangman (in, "Greek Gods", partnerName, greekGods, 2);
      if (challengeStatus.equals("passed")) {
        minotaurHealth = minotaurHealth - 80;
        System.out.println("Minotaur Remaining Health: " + minotaurHealth + "%");
        System.out.println("\n(Press Enter to continue.)");
        in.nextLine();
      } else {
        System.out.println("\nDespite all your efforts, you and " + partnerName + " coudln't kill the minotaur. With it's remaining health, the minotaur moves sluggishly towards you and your partner.");
        System.out.println("\nIt picks up its axe and launches itself at you. You and your partner had already used up all of your energy before, it was no use to resist. This is the end.");
        // We don't need to do anything because challengeStatus is already failed and will result in the -100 define by our arrays
      }
    } else {
      System.out.println("You waited too long, the Minotaur gets up and kills you.");
      challengeStatus = "failed";
    }
    
    statChanges = updateHealthChanges(challengeStatus, partnerName, healerChanges, sorcererChanges, knightChanges, statChanges); // Arr1: healerChanges Arr2: sorcereChanges Arr3: knightChanges
    
    return statChanges;
  }

  public static int[] minotaurAttack2 (Scanner in, String username, String partnerName, String partnerShortName, String partnerWeapon, int[] characterHealths, String[] backpack, int[] statChanges) {
    int minotaurHealth = 100;
    int[] healerChanges = {-100, -100};
    int[] sorcererChanges = {-100, -100};
    int[] knightChanges = {-100, -100};
    String challengeStatus = ""; // So we can access the variable outside of the if statements
    
    System.out.println("\n" + username + ": Here’s the plan. I still have some back up healing potions. We’ll drink them then cover our mouths and noses to protect ourselves before we release poison spells and burn the Oleander we got before. After we’ve weakened the minotaur, we’ll work together to finish it.");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    statChanges = useItem(2, statChanges); // Pass in 2, the index of "A Bushel of Oleanders" and statChanges, so we can update the backpack once stage is complete

    System.out.println("\n" + partnerName + ": I trust you, let's do this.");
    boolean initiated = initiateMinotaur(in);
    if (initiated == true) {
      System.out.println("\n You hand " + partnerName + " a vial of healing potion and a handkerchief. Both of you down the potion and cover your faces, then you begin your chant for poison spells (while " + partnerName + " sets the Oleander on fire). You release your spells (and " + partnerName + " throws the Oleander) into the chamber and let the poison slowly spread.");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();

      System.out.println("\nAfter twenty minutes, the minotaur who had been feeding on the bodies, drops down.");
      System.out.println("\n" + partnerName + " : The poison was effective. ");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();

      System.out.println("\n" + "The minotaur has lost 40% of it's health.");
      minotaurHealth = minotaurHealth - 40;
      System.out.println("Minotaur Remaining Health: " + minotaurHealth + "%");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();

      System.out.println("\n" + username + " : Mm, are you ready to attack? I’ll count down from three then we’ll go in.");
      System.out.println("\n" + partnerName + " puts a thumb up and readies their " + partnerWeapon);
      boolean finished = finishMinotaur(in);
      if (finished == true) {
        System.out.println("\n«You and your partner run in the chamber to attack the minotaur, but how does the attack go? Complete the challenge to decide the effectiveness of your attack.»");
        System.out.println("\n(Press Enter to continue.)");
        in.nextLine();

        String[] mythologicalCreatures = {"golem", "cyclops", "orgre", "leprechaun", "gnome", "goblin"};
        challengeStatus = hangman (in, "Mythological Creatures", partnerName, mythologicalCreatures, 2);
        if (challengeStatus.equals("passed")) {
          System.out.println("\n" + "The minotaur has lost 60% of it's health.");
          minotaurHealth = minotaurHealth - 60;
          System.out.println("Minotaur Remaining Health: " + minotaurHealth + "%");
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();
        } else {
          System.out.println("\nDespite all your efforts, you and " + partnerName + " coudln't kill the minotaur. With it's remaining health, the minotaur moves sluggishly towards you and your partner.");
          System.out.println("\nIt picks up its axe and launches itself at you. You and your partner had already used up all of your energy before, it was no use to resist. This is the end.");
          // We don't need to do anything because challengeStatus is already failed and will result in the -100 define by our arrays
        }
      } else {
        System.out.println("You waited too long, the Minotaur gets up and kills you.");
        challengeStatus = "failed";
      }
      
    } else {
      System.out.println("You waited too long, the Minotaur gets up and kills you.");
      challengeStatus = "failed";
    }
    

    statChanges = updateHealthChanges(challengeStatus, partnerName, healerChanges, sorcererChanges, knightChanges, statChanges); // Arr1: healerChanges Arr2: sorcereChanges Arr3: knightChanges
    
    return statChanges;
  }

  public static int[] minotaurAttack3 (Scanner in, String username, String partnerName, String partnerShortName, String partnerWeapon, int[] characterHealths, String[] backpack, int[] statChanges) {
    int minotaurHealth = 100;
    int[] healerChanges = {-100, -100};
    int[] sorcererChanges = {-100, -100};
    int[] knightChanges = {-100, -100};
    String challengeStatus = ""; // So we can access the variable outside of the if statements

    System.out.println("\n" +username+ ": Here's the plan. We'll wait for the beast to fall asleep and then we'll attack it with all we have. We'll prepare our potions and spells while its still awake. Once it's asleep, we activate our potions and spells, then I'll finish him");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();

    System.out.println("\n" +partnerName+ ": Got it, let's slay this beast!");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();

    System.out.println("\nYou and " +partnerName+ " move away from the small chamber, far enough so the minotaur won't hear what you guys are up to.");
    System.out.println("You take out a bunch of potion materials and lay them out on the ground.");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();

    System.out.println("\n" +username+ ": Could you gring these together while I write up spells to attack the minotaur?\n " +partnerName+ " nods their head and immediately gets to work.");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();

    System.out.println("\nSeveral hours pass and the two of you have finished preparing your spells and potions. The minotaur is fast asleep after its filling meal");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();

    System.out.println("\nYou approach with caution and look over to " +partnerName+ ".");
    System.out.println("\n" +username+ ": " +partnerName+ ", are the potions ready?");
    System.out.println("\n" +partnerName+ " laughs and smirks.\n" +partnerName+ ": Are your spells ready?");
    System.out.println("\n" +username+ ": Well seeing how cheeky you are, let's go on the count of three.");
    boolean initiated = initiateMinotaur(in);
    if (initiated == true) {
      System.out.println("\nYou both attack and the minotaur wakes up to its body weakened. The potions and spells halved the beast's strength. Now it's swinging its body around carelessly, hoping to swat you or your partner.");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();
      
      System.out.println("\nThe minotaur has lost 30% of its health.");
      minotaurHealth = minotaurHealth - 30;
      System.out.println("Minotaur Remaining Health: " +minotaurHealth+ "%");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();

      System.out.println("\nYou turn towards " +partnerName+ ".\n" +username+ ": Ready?");
      System.out.println("\n" +partnerName+ ": Yeah, let's do this.");
      boolean finished = finishMinotaur(in);
      if (finished == true) {
        System.out.println("\n«You glance at your partner and go in to deal the final blow, but how does it go? Complete the challenge to decide the effectiveness of your attack.»");
        System.out.println("\n(Press Enter to continue.)");
        in.nextLine();

        String[] greekPhilosophers = {"aristotle", "plato", "socrates", "thales of miletus", "pythagoras"};
        challengeStatus = hangman (in, "Greek Philosophers", partnerName, greekPhilosophers, 2);
        if (challengeStatus.equals("passed")) {
          System.out.println("\nThe minotaur has lost 70% of its health.");
          minotaurHealth = minotaurHealth - 70;
          System.out.println("Minotaur Remaining Health: " +minotaurHealth+ "%");
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();
        } else {
          System.out.println("\nDespite all your efforts, you and " + partnerName + " couldn't kill the minotaur. With its remaining health, the minotaur moves sluggishly towards you and your partner.");
          System.out.println("\nIt picks up its axe and launches itself at you. You and your partner had already used up all of your energy before, so resisting would be futile. This is the end.");
        } 
      } else {
        System.out.println("You waited too long, the Minotaur gets up and kills you.");
        challengeStatus = "failed";
      }
      
    } else {
      System.out.println("You waited too long, the Minotaur gets up and kills you.");
      challengeStatus = "failed";
    }
    
    
    statChanges = updateHealthChanges(challengeStatus, partnerName, healerChanges, sorcererChanges, knightChanges, statChanges); // Arr1: healerChanges Arr2: sorcereChanges Arr3: knightChanges
    
    return statChanges;
  }

  public static boolean initiateMinotaur (Scanner in) {
    boolean initiated = false;
    System.out.println("«Initiate attack? (Yes/No)»");
    String initAns = in.nextLine();
    if (initAns.equalsIgnoreCase("yes")) {
      initiated = true;
    } else {
      System.out.println("\n" + "You take a deep breathe and wait a few seconds.");
      System.out.println("«Initiate attack? (Yes/No)»");
      String initAns2 = in.nextLine();
      if (initAns2.equalsIgnoreCase("yes")) {
      initiated = true;
      } else {
        initiated = false;
      }
    }
    return initiated;
  }

  public static boolean finishMinotaur (Scanner in) {
    boolean finished = false;
    System.out.println("«Finish the minotaur? (Yes/No)»");
    String finishAns = in.nextLine();
    if (finishAns.equalsIgnoreCase("yes")) {
      finished = true;
    } else {
      System.out.println("\n" + "You take a deep breathe and wait a few seconds.");
      System.out.println("«Finish the minotaur? (Yes/No)»");
      String initAns2 = in.nextLine();
      if (initAns2.equalsIgnoreCase("yes")) {
      finished = true;
      } else {
        finished = false;
      }
    }
    return finished;
  }

  public static void minotaurComplete(Scanner in, String username, String partnerName, String partnerShortName, String partnerWeapon, int[] characterHealths, String[] backpack) {
    System.out.println("\nThe two of you stand there breathing heavily. The minotaur’s body drops to the ground with a loud *flop*. Silence fills the chamber.");
    System.out.println("\n" + partnerName + ": We did it!");
    System.out.println("\n" + partnerName + " runs to you with their hands up and you two have a mini celebration.");
    System.out.println("\nLet's go collect our treasure");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();

    System.out.println("\nIn the back of the chamber laid a dull brown chest shut with a rusted lock.");
    System.out.println("\nYou two rush over excitedly, and started fumbling with the lock.");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();

    System.out.println("\n" + partnerName + ": Look at all this treasure!");
    System.out.println("\n" + partnerName + " aggressively taps your shoulder, but you’re not paying attention. Your attention shifted.");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("\nThe minotaur’s dead body was upright facing you with its eyes rolled back in its head.");
    System.out.println("\nYou and " + partnerName + "'s faces pale in fear.");
    System.out.println("\n" + partnerName + ": ……how?");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
  }

  public static int[] floorOneFinal (Scanner in, String username, String partnerName, String partnerShortName, String partnerWeapon, int[] characterHealths, String[] backpack, int[] statChanges, String[] userItems) {
    System.out.println("\n*kekekeke* Sinister laughter filled the cave sending chills down your spine.");
    System.out.println("\nYou grabbed your sword ready to defend against anything. ");
    System.out.println("A white ghost-like creature appears behind the minotaur");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();

    System.out.println("\nThen it clicked.");
    System.out.println("\n" + username + ": It’s Daedalus, the one who created this dungeon and clearly he’s too prideful to let us leave.");
    System.out.println("\n" + username+ ": How much hp do you have left?");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();

    System.out.print("\n" + partnerName + ": " + characterHealths[1] + "%, ");
    if (characterHealths[1] < 30) {
      System.out.println(" not much.");
    } else if (characterHealths[1] > 30 && characterHealths[1] < 60) {
      System.out.println(" enough for a little longer.");
    } else if (characterHealths[1] > 60) {
      System.out.println(" I can continue to fight.");
    }
    System.out.println("\nWhat about our items--what do we have left in our bag?");
    System.out.println("\n" + username + ": we have..");

    System.out.println("\n" + username + " (backpack): ");
    for (int x = 0; x < backpack.length; x++) {
      int itemNum = x + 1;
      System.out.println("  " + itemNum + ": " + backpack[x]);
    }
    if (backpack[0].equals("Empty") && backpack[1].equals("Empty") && backpack[2].equals("Empty")) {
      System.out.println("\nCrap, we're in a real tight spot");
    } else {
      System.out.println("\n" + partnerName + ": Okay good, we can use those to fight.");
    }
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();

    System.out.println("\n" + username + ": " + partnerShortName + " prepare everything we got, this is going to get messy.");
    
    String daedalusAns = ""; // Initialize to access for error trap
    do {
      System.out.println("«\nEngage in battle with Daedalus? (yes/no)»");
      daedalusAns = in.nextLine();
    } while (!daedalusAns.equalsIgnoreCase("yes") && !daedalusAns.equalsIgnoreCase("no")); // If the user accidentay resses enter

    
    
    if (daedalusAns.equalsIgnoreCase("yes")) {
      System.out.println("\n<How do you want to fight Daedalus? \n\t1. Sever the strings connecting the two, then you face Daedalus while " + partnerName + " holds off the minotaur. \n\t2. Conjure a fire spell to create an explosion and temporarily obstruct Daedalus’s view. Then rush in with " + partnerName + " to finish him.>>");
      int userAns = in.nextInt();
      in.nextLine(); // read in next line

      userAns = isNumOutOfRangeOfTwo(in, userAns);
      
      if (userAns == 1) {
        statChanges = daedalusChoice1 (in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges, userItems);
      } else if (userAns == 2) {
        statChanges = daedalusChoice2 (in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges, userItems);
      }
    } else if (daedalusAns.equalsIgnoreCase("no")) {
      System.out.println("\nDaedalus attacks you and you are forced to fight back. You start slashing at the strings.");
      System.out.println("\nYou continue to slash at the strings, but to no avail.");
      System.out.println("\n«Would you like to try the second strategy to fight Daedalus?»");
      String secondStratAns = in.nextLine();
      if (secondStratAns.equalsIgnoreCase("yes")) {
        statChanges = daedalusChoice2 (in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges, userItems);
      } else if (secondStratAns.equalsIgnoreCase("no")) {
        System.out.println("\nThis is it, you and " + partnerName + "'s doomed fate.");
        System.out.println("\nYou look over at " + partnerName + " and their exhausted fighting the minotaur and can’t hold on for much longer.");
        System.out.println("\nYou close your eyes, and activate an explosion spell.");
        statChanges[0] = -100; // Kill user
        statChanges[1] = -100; // Kill partner
      }
    }
    
    
    return statChanges;
  }
  public static int [] daedalusChoice1 (Scanner in, String username, String partnerName, String partnerShortName, String partnerWeapon, int[] characterHealths, String[] backpack, int[] statChanges, String[] userItems) {
    int minotaurHealth = 100;
    System.out.println("<<You have chosen Option A: Sever the strings then split forces. Choose a hangman category, then answer your word correctly to slash at the strings. \n\t1. Musical Instruments \n\t2. Muscles \n\t3. Elements of the Periodic table>>");
    int userAns = in.nextInt();
    in.nextLine(); // read in next line

    userAns = isNumOutOfRangeOfThree(in, userAns);
    
    String challengeStatus = ""; // Initalize outside of if statements so it can be accessesd outside those blocks
    if (userAns == 1) {
      String[] musicalInstruments  = {"piano", "guitar", "flute", "drums", "trumpet"};
     challengeStatus = hangman (in, "Musical Instruments", partnerName, musicalInstruments, 3);
    } else if (userAns == 2) {
      String[] muscles  = {"bicep", "tricep", "thighs", "calves", "chest"};
      challengeStatus = hangman (in, "Muscles", partnerName, muscles, 3);
    } else if (userAns == 3) {
      String[] elementsOfThePeriodicTable  = {"gold", "silver", "copper", "tin", "sulfur"};
      challengeStatus = hangman (in, "ElementsOf The Periodic Table", partnerName, elementsOfThePeriodicTable, 3);
    }
    int[] healerChanges = {-10, 0};
    int[] sorcererChanges = {-10, 0};
    int[] knightChanges = {-10, 0};
    
    statChanges = updateHealthChanges(challengeStatus, partnerName, healerChanges, sorcererChanges, knightChanges, statChanges); // Arr1: healerChanges Arr2: sorcereChanges Arr3: knightChanges
    System.out.println("\nYou push yourself over your limits and enhance your physical abilities. You zoom towards Daedalus and the minotaur and attempt to sever the strings, but they’re not made out of physical material.");
    System.out.println("«Would you like advice/a hint from the system or continue slashing at the strings? (Hint/Continue)»");
    String userHintAns = ""; // Declare outside of do while so we can access later
    do {
      userHintAns = in.nextLine();
    } while (!userHintAns.equalsIgnoreCase("continue") && !userHintAns.equalsIgnoreCase("hint"));
    
    if (userHintAns.equalsIgnoreCase("continue")) {
      System.out.println("\nYou continue to slash at the strings, but to no avail.");
      System.out.println("\n«Would you like to try the second strategy to fight Daedalus?»");
      String secondStratAns = in.nextLine();
      if (secondStratAns.equalsIgnoreCase("yes")) {
        statChanges = daedalusChoice2 (in, username, partnerName, partnerShortName, partnerWeapon, characterHealths, backpack, statChanges, userItems);
      } else if (secondStratAns.equalsIgnoreCase("no")) {
        System.out.println("\nThis is it, you and " + partnerName + "'s doomed fate.");
        System.out.println("\nYou look over at " + partnerName + " and their exhausted fighting the minotaur and can’t hold on for much longer.");
        System.out.println("\nYou close your eyes, and activate an explosion spell.");
        statChanges[0] = -100; // Kill user
        statChanges[1] = -100; // Kill partner
        
      }
    } else if (userHintAns.equalsIgnoreCase("hint")) {
      System.out.println("\n<<Here is your hint: to land attacks on Daedalus’s ghost or sever the strings you must attack with a blade completely covered with powerful blood either infused with your affection or strength.>>");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();

      System.out.println("\n" + username + ": What is that supposed to mean?!");
      System.out.println("\nYou rake your hands through your hair, losing control of your breath and panicking. Your vision is turning hazy partly because you’re tired, but also because you’re scared you understood the hint correctly.");
      System.out.println("\nYou need to hurt " + partnerName + " in order to get out of this dungeon alive.");
      System.out.println("\nYou and " + partnerName + " severely underestimated this dungeon. ");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();

      System.out.println("\n" + partnerName + ": " + username + " WHAT'S WRONG?");
      System.out.println("\n" + partnerName + " is defending against the minotaur right now, and Daedalus is getting closer to them." );
      System.out.println("\n " + username + ": I can’t find a way to get out of here.");
      System.out.println("\n" + partnerName + ": …what did the system tell you?");
      System.out.println("\n" + username + ": No I can’t do what the system told me. ");
      System.out.println("\n" + partnerName + ": What did the system tell you?");
      System.out.println("\n" + username + ": …I have to cover my sword in your blood or in other words, kill you to kill the minotaur.");
      System.out.println("\n" + partnerName + ": ...");
      System.out.println("\n" + username + ": I know, it’s absolutely ridiculous, we can’t- \n" + partnerName + ": Do it. You can have my blood.");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();
      
      System.out.println("\n«Do you want to:» \n\t1. Stab " + partnerName + ". \n\t2. Retreat." );
      int stabAns = in.nextInt();
      in.nextLine(); // read in next line;

      stabAns = isNumOutOfRangeOfTwo(in, stabAns);
      
      if (stabAns == 1) {
        System.out.println("\nYou have chosen option 1: stab.");
        System.out.println("\n(Press Enter to continue.)");
        in.nextLine();

        System.out.println("\nWith trembling hands you clean your sword with water, and look at " + partnerName + ".");
        System.out.println("\n" + partnerName + ": Don’t worry, just avoid my major arteries and nerves, and I can heal myself with the elixirs we have. ");
        System.out.println("\nYour eyes are full of fear, but " + partnerName + " grabs your hand and looks straight into your eyes, telling you to do it.");
        System.out.println("\n(Press Enter to continue.)");
        in.nextLine();

        System.out.println("\nYou quickly run your sword through their stomach.\n" + partnerName + " lets out a muffled grunt, and breathes heavily. You pull out your sword, and turn around to face Daedalus and the minotaur.");
        System.out.println("\n(Press Enter to continue.)");
        in.nextLine();

        System.out.println("\n" + username + ": I’ll make sure to take down this good for nothing engineer. ");
        System.out.println("\nYour heart burns with anger and you run as fast as your legs can take you. Your heart is pumping at inhumane speeds as you slash your blood covered sword at the strings.");
        System.out.println("\n(Press Enter to continue.)");
        in.nextLine();

        System.out.println("\nThe minotaur slumps down again like a corpse should, and Daedalus’s ghost starts to back away.");
        System.out.println("\n" + username + ": Not so cheeky now huh");
        System.out.println("\nThe stench of blood starts to fill your nostrils and you slowly walk toward the now cowering away ghost.");
        System.out.println("\n(Press Enter to continue.)");
        in.nextLine();

        System.out.println("Choose one of the following hangman categories and correctly guess the word to commence your attack. \n\t1. Personality traits \n\t2. Body organs \n\t3. Diseases/medical conditions ");
        int attackChoice = in.nextInt();
        in.nextLine(); // Read in next line

        attackChoice = isNumOutOfRangeOfThree(in, attackChoice);
        
        String challengeStatus2 = ""; // Initalize outside of if statements so we can access it outside the blocks
        if (attackChoice == 1) { 
          String[] personalityTraits  = {"funny", "happy", "shy", "humble", "gracious", "intelligent", "introverted", "extroverted", "bubbly", "outspoken", "arrogant"};
          challengeStatus2 = hangman (in, "Personality Traits", partnerName, personalityTraits, 3); // Challenge status 2 to denote that it's the second hangman game and because it will avoid duplicate variables
        }
        else if (attackChoice == 2) { 
          String[] bodyOrgans  = {"brain", "lungs", "liver", "heart", "stomach", "kidney", "pancreas", "intestines", "bladder", "appendix", "eye", "tongue", "gallbladder"};
          challengeStatus2 = hangman (in, "Body Organs", partnerName, bodyOrgans, 3); // Challenge status 2 to denote that it's the second hangman game and because it will avoid duplicate variables
        }
        else if (attackChoice == 3) { 
          String[] disMedConditions  = {"coronavirus", "cancer", "asthma", "diabetes", "osteoporosis", "anemia", "eczema", "flu", "strep throat", "cold", "chickenpox", "tuberculosis", "tetanus"};
          challengeStatus2 = hangman (in, "Diseases/Medical Conditions", partnerName, disMedConditions, 3); // Challenge status 2 to denote that it's the second hangman game and because it will avoid duplicate variables
        }
        healerChanges[0] = -100;
        sorcererChanges[0] = -100;
        knightChanges[0] = 100;
    
        statChanges = updateHealthChanges (challengeStatus2, partnerName, healerChanges, sorcererChanges, knightChanges, statChanges); // If failed, this will update the health changes to be -100
        if (challengeStatus2.equals("passed")) {
          
          System.out.println ("You run at the defenseless ghost and tear its limbs off. The ghost writhes in pain–if the dead can even feel pain, and you continue to attack it. ");
          System.out.println ("You remember " + partnerName + "'s body sitting at the side of the chamber, and quicken your attack. You quickly behead the ghost, and Daedalus fades away with an echoing groan. ");
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();

          System.out.println("\n«You have defeated the boss of the Tomb of King Minos.»");
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();

          System.out.println("\nYou rush over to " + partnerName + " and check for a pulse. After a few seconds you can’t feel something and your mind blanks. ");
          System.out.println("\nYou’re crying and there’s snot dripping down your nose.");
          System.out.println("\n" + username + ": I... killed you " + partnerName);
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();

          System.out.println("A cold hand grabs yours, interrupting your unsightly sobbing.");
          System.out.println("\n" + partnerName + ": I’m not dead yet, bozo–just have a weak pulse.");
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();

          System.out.println("\n" + partnerName + ": Could you grab some more elixirs from your bag for me?");
          System.out.println("You hand over several elixirs and watch " + partnerName + "down each of them like a ravenous wolf. You also grab some gauze and wrap "+ partnerName +"’s wound tightly.");
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();  

          System.out.println(partnerName + " taps you and points to the minotaur's body");
          System.out.println("\n" + partnerName + ": it looks like there’s something there.");

          System.out.println("«Would you like to pick up 800 gold coins and Daedalus’s Will? (yes/no)»");
          String userChoice = in.nextLine();

          if (userChoice.equalsIgnoreCase("yes")){
            statChanges = daedalusAAYes(in, username, partnerName, statChanges, backpack);
          } else {
            System.out.println("\n«Alright, your loss.»");
            System.out.println("\n" + partnerName + " stares at you speechless.");
            System.out.println("\n" + partnerName + ": Go collect those or else we’re never going to be partners again and I’m going to tell the adventurer’s guild that you attempted murder.");
            System.out.println("\n" + username + ": It was an accident I swear! Let me try again.");
            statChanges = daedalusAAYes(in, username, partnerName, statChanges, backpack);
          }

          System.out.println("\n" + username + ": Let’s make our way back to the guild to make our report and get treated.");
          System.out.println("\nOn your way back, none of the monsters tried to attack you, probably because of Daedalus’s will and spirit following you around.");
          System.out.println("\nThe two of you leaned on each other limping out of the cave. Today’s adventure was finally over.");

          System.out.println("\n\n«Congratulations you have conquered the Tomb of King Minos»");
          
        } 
      } else if (stabAns == 2) {
        System.out.println("\nYou have chosen option 2: retreat.");
        System.out.println("\n(Press Enter to continue.)");
        in.nextLine();
      
        System.out.println("\n" + username + ": No, I can't do this. We're going to retreat.");
        System.out.println("\n" + partnerName + ": We can’t! We’ve already made it this far, there’s no way we’re getting out of this alive either way. ");
        System.out.println("\n" + username + ": Shut up, this is my decision as the party lead. We’re going. If you have any objections, I’m knocking you out.");
        System.out.println("\n " + partnerName + " holds their " + partnerWeapon + " and takes a step forward, but just as " + partnerName + " is about to attack the minotaur, the beast’s large fur-covered arm smacks " + partnerShortName + " into the wall of the chamber.");
        System.out.println("\n" + partnerName + " is instantly knocked out, and the minotaur moves robotically toward " + partnerShortName + ".");
        System.out.println("\nYou jump in to try to save " + partnerName + ", but it’s too late.");
        statChanges[0] = -100; // user dies
        statChanges[1] = -100; // partner dies
        
      }
    }
     
    return statChanges;
  }
  public static int [] daedalusChoice2 (Scanner in, String username, String partnerName, String partnerShortName, String partnerWeapon, int[] characterHealths, String[] backpack, int[] statChanges, String[] userItems) {
    int minotaurHealth = 100;
    System.out.println("\n<<You have chosen Option B: Obstruct Daedalus’s view and finish him. Choose a hangman category, then answer your word correctly to conjure a fire spell. \n\t1. Olympic Sports \n\t2. Famous Olympians \n\t3. Muscles >>");
    int userAns = in.nextInt();
    in.nextLine(); // reading in next line

    userAns = isNumOutOfRangeOfThree(in, userAns);
    
    String challengeStatus = "";
    if (userAns == 1) {
      String[] olympicSports  = {"curling", "volleyball", "basketball", "fencing", "soccer"};
      challengeStatus = hangman (in, "Olympic Sports", partnerName, olympicSports, 3); 
    } else if (userAns == 2) {
      String[] famousOlympians  = {"michael phelps", "usain bolt", "michael jordan", "kobe bryant"};
      challengeStatus = hangman (in, "Famous Olympians", partnerName, famousOlympians, 3); 
    } else if (userAns == 3) {
      String[] muscles  = {"bicep", "tricep", "thighs", "calves", "chest"};
      challengeStatus = hangman (in, "Muscles", partnerName, muscles, 3);
    } 
    int[] healerChanges = {-10, 0};
    int[] sorcererChanges = {-10, 0};
    int[] knightChanges = {-10, 0};
    if (challengeStatus.equalsIgnoreCase("failed")) {
      System.out.println("\n«You have lost 10% hp»");
      statChanges = updateHealthChanges(challengeStatus, partnerName, healerChanges, sorcererChanges, knightChanges, statChanges); // Arr1: healerChanges Arr2: sorcereChanges Arr3: knightChanges
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();
      
      System.out.println("\nAlbeit carelessly, you activate your spell.");
    }
    System.out.println("\n" + username + ": HELLFIRE’S BLAST!");
    System.out.println("\nThe floor beneath your feet shake and dust starts to fall and spread in the chamber. It’s hard to see, even for you, but that glowy white shine from Daedalus’s ghost can’t be missed.");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();

    System.out.println("\nYou run in to attack the minotaur and successfully trap it against the wall.");
    System.out.println("\n" + username + ": " + partnerShortName + ", it’s your turn!");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();
    
    System.out.println("\n" + partnerName + " fires their most powerful attack at Daedalus’s ghost, and the dust dissipates from the chamber.");
    System.out.println("\n...");
    System.out.println("\n*kekeke*");
    System.out.println("\nNo matter how hard " + partnerName + " attacked, everything seemed like it was for naught.");
    System.out.println("\n(Press Enter to continue.)");
    in.nextLine();

    System.out.println("«Would you like advice/a hint from the system or continue slashing at the strings? (Hint/continue)»");
    String userHintAns = ""; // Declare outside of do while so we can access later
    do {
      userHintAns = in.nextLine();
    } while (!userHintAns.equalsIgnoreCase("continue") && !userHintAns.equalsIgnoreCase("hint"));

    if (userHintAns.equalsIgnoreCase("hint")) {
      System.out.println("\n<<Here is your hint: to land attacks on Daedalus’s ghost you need to attack it with spells infused with divine power. You may not have divine power now, but you can create it by sacrificing one of your five senses.>>");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();


      System.out.println("\n" + username + ": I'll do it.");
      System.out.println("\n" + partnerName + ": What do you mean?");
      System.out.println("\n" + username + ": I’ll sacrifice my hearing to conquer this dungeon.");
      System.out.println("\n" + partnerName + ": You’ll what? No, I’m not letting you do this. We’re both getting out of this dungeon in one piece, all senses still functioning properly.");
      System.out.println("\n(Press Enter to continue.)");
      in.nextLine();

      System.out.println("\n«Do you want to: \n\t1. Sacrifice your hearing.\n\t2. Retreat. \n\t3. Use a backpack item.»");
      int hearingAns = in.nextInt();
      in.nextLine();

      hearingAns = isNumOutOfRangeOfThree(in, hearingAns);
      
      if (hearingAns == 1) {
        System.out.println("\nYou have chosen option 1: sacrifice your hearing.");
        System.out.println("\n(Press Enter to continue.)");
        in.nextLine();

        System.out.println("\nYour hearing starts to fade away. You can still faintly hear " + partnerName + " screaming for you to stop, but you already made your decision.");
        System.out.println("\n(Press Enter to continue.)");
        in.nextLine();
        
        do {
          System.out.println("\nChoose one of the following hangman categories and correctly guess the word to commence your attack. \n\t1. Personality traits \n\t2. Body organs \n\t3. Diseases/medical conditions");
          int attackChoice = in.nextInt();
          in.nextLine(); // Read in enter

          attackChoice = isNumOutOfRangeOfThree(in, attackChoice);
          
          String challengeStatus2 = ""; // Create variable outside of if statements for later access
          if (attackChoice == 1) {
            String[] personalityTraits  = {"funny", "happy", "shy", "humble", "gracious"};
            challengeStatus2 = hangman (in, "Personality Traits", partnerName, personalityTraits, 3); // Challenge status 2 to denote that it's the second hangman game and because it will avoid duplicate variables
          } else if (attackChoice == 2) {
            String[] bodyOrgans = {"liver", "lung", "heart", "stomach", "eye", "tongue"};
            challengeStatus2 = hangman (in, "Body Organs", partnerName, bodyOrgans, 3); // Challenge status 2 to denote that it's the second hangman game and because it will avoid duplicate variables
          } else if (attackChoice == 3) {
            String[] disMedConditions  = {"coronavirus", "cancer", "asthma", "diabetes", "osteoporosis"};
            challengeStatus2 = hangman (in, "Diseases/Medical Conditions", partnerName, disMedConditions, 3); // Challenge status 2 to denote that it's the second hangman game and because it will avoid duplicate variables
          } // vic changed challengeStatus and added a 2. vic doesn't know if this is right tho
          if (challengeStatus2.equals("passed")) { // If the user won the hangman we don't want to run the rest of the loop, break
            break; 
          }
          
          minotaurHealth = minotaurHealth - 10;
          System.out.println("\nThe minotaur's hp has decreased by 10%");
          System.out.println("Minotaur Remaining Health: " + minotaurHealth + "%");
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();
          
          System.out.println("\nYou slug your sword at the minotaur, but it’s not enough to defeat it. Try again.");
        } while (minotaurHealth > 0);
        
        System.out.println("\nYou close your eyes focusing on the sword in your hands and the vibrations through the ground. ");
        System.out.println("\nYou open your eyes and make your final attack. You cut down the Minotaur, and go straight for Daedalus. With one firm strike, you bring Daedalus down.");
        System.out.println("\n(Press Enter to continue.)");
        in.nextLine();
          
        System.out.println("\n«Congratulations you have conquered the Tomb of King Minos»");
        System.out.println("\n(Press Enter to continue.)");
        in.nextLine();

        System.out.println("\nIt feels as if you’ve won, but you can’t hear any cheers of celebration. In fact, you can’t hear anything now.");
        System.out.println("\nYou turn around only to see " + partnerShortName + " with a face of regret. Regret that the two of you entered the dungeon, and regret that they couldn’t derail you from making the decision that you did.");
        System.out.println("\nYou did not live through this dungeon.");
        statChanges[0] = -100;
      } else if (hearingAns == 2) {
        System.out.println("\nYou have chosen option 2: retreat.");
        System.out.println("\n(Press Enter to continue.)");
        in.nextLine();

        System.out.println("\n" + username + ": No, I can't do this. We're going to retreat.");
        System.out.println("\n" + partnerName + ": We can’t! We’ve already made it this far, there’s no way we’re getting out of this alive either way. ");
        System.out.println("\n" + username + ": Shut up, this is my decision as the party lead. We’re going. If you have any objections, I’m knocking you out.");
        System.out.println("\n " + partnerName + " holds their " + partnerWeapon + " and takes a step forward, but just as " + partnerName + " is about to attack the minotaur, the beast’s large fur-covered arm smacks " + partnerShortName + " into the wall of the chamber.");
        System.out.println("\n" + partnerName + " is instantly knocked out, and the minotaur moves robotically toward " + partnerShortName + ".");
        System.out.println("\nYou jump in to try to save " + partnerName + ", but it’s too late.");
        statChanges[0] = -100;
        statChanges[1] = -100;
        
        
      } else if (hearingAns == 3) {
        System.out.println("\nYou have chosen option 3: use a backpack item.");
        System.out.println("\n(Press Enter to continue.)");
        in.nextLine();

        System.out.println("\nWhat about our items--what do we have left in our bag?");
        System.out.println("\n" + username + ": we have..");
      
        System.out.println("\n" + username + " (backpack): ");
        for (int x = 0; x < backpack.length; x++) {
          int itemNum = x + 1;
          System.out.println("  " + itemNum + ": " + backpack[x]);
        }
        int itemIndex = -1; // Initialize outside of loop so we can access after
        boolean isItem = false;
        do {
          System.out.println("«Choose an Item (1/2/3)»");
          itemIndex = in.nextInt();
          in.nextLine(); // Read in next line
          if (backpack[itemIndex-1].equals("Empty")) {
            System.out.println("That is an empty slot"); // If the slot is empty make the user chose again
          } else {
            isItem = true; // Once the chosen slot is not empty continue
          }
        } while (!isItem);
        
        String itemLong = backpack[itemIndex - 1]; // Index are zero based, the choice is 1-3 therefore we must subtract 1  
        int indexOfItem = -1; // Initialize index outside of for loop so we can use later
        for (int x = 0; x < userItems.length; x++) { // Find the index of the item in userItems which will be used later in useItem()
          if (itemLong.equals(userItems[x])) {
            indexOfItem = x; // Set the indexOfItem to where the item was found in userItems
          }
        }
        
        String item = "";
        int itemCutIndex = itemLong.indexOf('-'); // Find the index of the - which seperates the name and where the item was found
        for (int x = 0; x < itemCutIndex; x++) {
          item = item + itemLong.charAt(x); // Accumulate the long item name until 1 before the - 
        }
        if (itemLong.equals("Divine Spider Silk - Entrance")) {
          System.out.println("\n«You have activated Divine Spider Silk»");
          statChanges = useItem(3, statChanges); // Pass in 3, the index of "Divine Spider Silk" and statChanges, so we can update the backpack once stage is complete
          System.out.println("«Hidden Route: Activated»");
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();
          
          System.out.println("\n" + username + ": Huh- What does that mean?\nUpon closer realization you see that the specific item you chose had the word “divine” in it. ");
          System.out.println("\n" + username + ": Could it be?");
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();
          
          System.out.println("\n" + partnerName + ": Whatever you’re doing, hurry! The minotaur is closing in on us.");
          System.out.println("Then you had a stroke of genius. You grabbed a bundle of spider silk and launched it at the minotaur and Daedalus. Confirming your suspicions, both monsters were trapped onto the wall. ");
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();

          System.out.println("\n" + username + ": I figured it out!");
          System.out.println("\n" + partnerName + ": What is it?");
          System.out.println("\n" + username + ": Do we have any more of the spider silk?");
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();
          
          System.out.println("\n" + partnerName + ": Yea, I collected a bit more after that attack.");
          System.out.println("\n" + username + ": Great, we're going to need it.");
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();
          do {
            System.out.println("To attack the minotaur and Daedalus, choose and correctly guess a word from the following hangman categories: \n\t1. Forms of art \n\t2. Snacks \n\t3. Famous artists.");
            int itemAns = in.nextInt();
            in.nextLine(); // read in next line
            
            String challengeStatus2 = itemHangman(in, partnerName, itemAns); // 2nd instance of challengeStatus, new variable to avoid duplicate
            if (challengeStatus2.equals("passed")) { // If the user won the hangman we don't want to run the rest of the loop, break
              break; 
            }
            
            minotaurHealth = minotaurHealth - 10;
            System.out.println("\nThe minotaur's hp has decreased by 10%");
            System.out.println("Minotaur Remaining Health: " + minotaurHealth + "%");
            System.out.println("\n(Press Enter to continue.)");
            in.nextLine();
            
            System.out.println("\nYou slug your sword at the minotaur, but it’s not enough to defeat it. Try again.");
          } while (minotaurHealth > 0);
            
          
          System.out.println("\nYou imbue the spider silk in your sword and prepare to attack Daedalus.");
          System.out.println("You ready your stance and attack the two. You cut the minotaur in half and hastily make your way to the ghost that’s about to slip out of the chamber.");
          System.out.println("You slice the ghost, and like a demon being exorcised its laughter turns into screeches and shrills.");
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();

          System.out.println("A while passes, and then Daedalus completely fades away leaving nothing behind. ");
          System.out.println("You turn to " + partnerName + " and both your eyes light up. \nYou finally defeated Daedalus.");
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();
          
          System.out.println("\n«Congratulations you have conquered the Tomb of King Minos»");
          System.out.println("\n" + partnerName + " runs over to you and gives you a high five that oculd knock your arm right off your shoulder.");
          System.out.println("\n" + partnerName + ": We finally conquered this god awful place.");
          System.out.println("\n(Press Enter to continue.)");
          in.nextLine();
            
          System.out.println("\n" + partnerName + ": Once we head back let’s have a drinking party to celebrate our hard work.");
          System.out.println("\nYou smile fondly at your partner and relieved that it's all over.");
          System.out.println("\n" + username + ": Yeah, cheers to that.");
          
        } else {
          System.out.println("\n«You have activated (" + item + ")»");
          statChanges = useItem(indexOfItem, statChanges); // Use the chosen item and update statChanges 
          System.out.println("To use (" + item + ") choose and correctly guess a word from the following hangman categories: \n\t1. Forms of art \n\t2. Snacks \n\t3. Famous artists.");
          int itemAns = in.nextInt();
          in.nextLine(); // read in next line

          itemAns = isNumOutOfRangeOfThree(in, itemAns);
            
          String challengeStatus2 = itemHangman(in, partnerName, itemAns); // Challenge status if not spider silk
          if (challengeStatus2.equals("passed")) {
            System.out.println("\nYou use your " + item + " on the minotaur, and although it doesn’t seem to harm it that much. A little bit of damage is done to the minotaur every time you use it. \n\n<<Minotaur hp decreased by 3%>> \n<<Minotaur hp decreased by 3%>> \n<<Minotaur hp decreased by 3%>>");
            System.out.println("\n" + username + ": There’s no way we can win against the minotaur, let’s just weaken it and escape this hellhole.");
            System.out.println("\n(Press Enter to continue.)");
            in.nextLine();

            System.out.println("\n" + partnerName + ": Got it!");
            System.out.println("\n" + partnerName + " grabs the " + item + " from their backpack and starts attacking the minotaur with it.");
            System.out.println("\nA few minutes pass and the Minotaur can no longer stand properly.");
            System.out.println("\n" + username + ": This is our chance, let's escape.");
            System.out.println("\n(Press Enter to continue.)");
            in.nextLine();
            
            System.out.println("\n<<Would you like to retreat? (yes/no)>>");
            String retreatAns = in.nextLine();
            if (retreatAns.equalsIgnoreCase("yes")) {
              System.out.println("\nYou and your partner quickly scurry out of the chamber and run for your lives to get out of this dungeon.");
              System.out.println("\nYou two make it out safely, but you failed to conquer the dungeon");
              System.out.println("\n(Press Enter to continue.)");
              in.nextLine();

              System.out.println("\n" + partnerName + ": I’m glad we’re alive, but I don’t think I’m ever going to enter a dungeon ever again.");
              System.out.println("\n" + username + ":It’s alright, even though that was my dream, I don’t think I will enter a dungeon again either.");
              System.out.println("\nYou may have survived but you have killed your courageous spirit. You lose.");
              statChanges[0] = -100; // Kill user
              
              
            } else if (retreatAns.equalsIgnoreCase("no")) {
              System.out.println("\nYou choose to stay here and continue your attack against the Minotaur thinking you can end it, but what a foolish choice that was.");
              System.out.println("\nDespite the minotaur’s lack of strength, Daedalus commands it to charge at you and within seconds your vision darkens.");
              statChanges[0] = -100;
              
            }
          } else if (challengeStatus2.equals("failed")) {
            System.out.println("Would you like to try again? (yes/no)");
            String tryAgainAns = in.nextLine();
            if (tryAgainAns.equalsIgnoreCase("no")) { 
              System.out.println("\nAll your attacks were ineffective. Daedalus commands the minotaur to charge at you and within seconds your vision darkens.");
              statChanges[0] = -100; // Kill user
              
            } else if (tryAgainAns.equalsIgnoreCase("yes")) {
              System.out.println("To use (" + item + ") choose and correctly guess a word from the following hangman categories: \n\t1. Forms of art \n\t2. Snacks \n\t3. Famous artists.");
              itemAns = in.nextInt();
              in.nextLine(); // read in next line
              challengeStatus = itemHangman(in, partnerName, itemAns);
              if (challengeStatus.equals("failed")) {
                System.out.println("\nAll your attacks were ineffective. Daedalus commands the minotaur to charge at you and within seconds your vision darkens.");
                statChanges[0] = -100; // Kill user
                
              } else if (challengeStatus.equals("passed")) {
                System.out.println("\nYou use your " + item + " on the minotaur, and although it doesn’t seem to harm it that much. A little bit of damage is done to the minotaur every time you use it. \n\n<<Minotaur hp decreased by 3%>> \n<<Minotaur hp decreased by 3%>> \n<<Minotaur hp decreased by 3%>>");
                System.out.println("\n" + username + ": There’s no way we can win against the minotaur, let’s just weaken it and escape this hellhole.");
                System.out.println("\n(Press Enter to continue.)");
                in.nextLine();

                System.out.println("\n" + partnerName + ": Got it!");
                System.out.println("\n" + partnerName + " grabs the " + item + " from their backpack and starts attacking the minotaur with it.");
                System.out.println("\nA few minutes pass and the Minotaur can no longer stand properly.");
                System.out.println("\n" + username + ": This is our chance, let's escape.");
                System.out.println("\n(Press Enter to continue.)");
                in.nextLine();
                
                
                String retreatAns = ""; // Initialize to access for error trap
                do {
                  System.out.println("\n<<Would you like to retreat? (yes/no)>>");
                  retreatAns = in.nextLine();
                } while (!retreatAns.equalsIgnoreCase("yes") && !retreatAns.equalsIgnoreCase("no"));
                
                if (retreatAns.equalsIgnoreCase("yes")) {
                  System.out.println("\nYou and your partner quickly scurry out of the chamber and run for your lives to get out of this dungeon.");
                  System.out.println("\nYou two make it out safely, but you failed to conquer the dungeon");
                  System.out.println("\n(Press Enter to continue.)");
                  in.nextLine();

                  System.out.println("\n" + partnerName + ": I’m glad we’re alive, but I don’t think I’m ever going to enter a dungeon ever again.");
                  System.out.println("\n" + username + ":It’s alright, even though that was my dream, I don’t think I will enter a dungeon again either.");
                  System.out.println("\nYou may have survived but you have killed your courageous spirit. You lose.");
                  statChanges[0] = -100; // Kill user
                  
                } else if (retreatAns.equalsIgnoreCase("no")) {
                  System.out.println("\nYou choose to stay here and continue your attack against the Minotaur thinking you can end it, but what a foolish choice that was.");
                  System.out.println("\nDespite the minotaur’s lack of strength, Daedalus commands it to charge at you and within seconds your vision darkens.");
                  statChanges[0] = -100;
                  
                }
              }
            }
          }
        }
      } else {
        System.out.println("\nYou keep attacking the Minotaur, but it keeps standing back up.");
        System.out.println("\nThere’s nothing you can do. You lose all hope.");
        System.out.println("\nEyes darkened, mind filled with only negative thoughts, you fall onto your knees, and your vision goes black.");
        statChanges[0] = -100;
      }
    }
    return statChanges;
  }

  public static int[] daedalusAAYes (Scanner in, String username, String partnerName, int[] statChanges, String[] backpack){ //method named after the various major decisions the user needs to make (so ignoring the hangman choices)
    
    
    System.out.println("\n«You have gained 800 gold coins and Daedalus’s Will.»");
   
    // Find empty backpack
    int emptyIndex = -1;
    for (int x = 0; x < backpack.length; x++) {
      if (backpack[x].equals("Empty")) {
        emptyIndex = x; // Once you find the empty index, set that index to the first empty index and break out of loop so you don't overwrite the index. 
        break;  
      }
    }
    
    statChanges[2 + emptyIndex] = 6; // Set the index of the first empty slot to 6 in stat changes. statChanges[healthChangesUser, helathChangesPartner, backpackSlot1, backpackslot2, backpackslot3]
    // 6 is the index of gold coins in userItems
    
    System.out.println("\n«Would you like to read Daedalus’s Will? (yes/no)»");
    String userChoice = in.nextLine();

    while((!userChoice.equalsIgnoreCase("yes")) && (!userChoice.equalsIgnoreCase("no"))){
      System.out.println("Invalid Input.");
      System.out.println("«Would you like to read Daedalus’s Will? (yes/no)»");
      userChoice = in.nextLine();
    }

    if (userChoice.equalsIgnoreCase("yes")){
      System.out.println("\nMy name is Daedalus, and I’m the one who created the labyrinth for King Minos. It took me over a decade to create, but it was only possible because of my genius.");
      System.out.println("(Press Enter to Continue)");
      in.nextLine();

      System.out.println("\nHowever, I’m not just an architect and craftsman, I’m a father—or used to be one at least. My son, Icarus, died. I’m sure you think he died because he turned arrogant, but that’s all a lie that King Minos fed you. My son was an obedient and smart child. He knew the consequences of his actions, and would never do something as reckless as flying towards the sun against what I had warned. ");
      System.out.println("(Press Enter to Continue)");
      in.nextLine();

      System.out.println("\nIt was King Minos that tricked my Icarus to fly towards the sun. Like the serpent from Adam and Eve, King Minos told my Icarus that he could be a God using hallucinogenic substances. I know King Minos did this because I found these substances in some food that he graciously gifted to my son. Despite all that I did for him, he wanted the labyrinth for himself.");
      System.out.println("(Press Enter to Continue)");
      in.nextLine();

      System.out.println("\nAfter my son’s death, I swore to avenge my son by luring King Minos into the dungeon and killing anyone who entered. I put the minotaur to sleep in hopes that King Minos would come back and this place would become his tomb, but I’ll most likely not live long enough to see my son avenged. ");
      System.out.println("(Press Enter to Continue)");
      in.nextLine();

      System.out.println("\nIt is with great sorrow and anger that I curse whomever enters this wretched dungeon—mine and Icarus’s tomb, but also the Tomb of King Minos.");
      System.out.println("(Press Enter to Continue)");
      in.nextLine();
    } else {
      System.out.println("\nThe beige scroll flies into your backpack, and you look at it a bit confused.");
      System.out.println("\n"+username+"I guess the ghost of Daedalus will haunt us forever.");
      System.out.println("\nYour partner lets out a light chuckle. ");
      System.out.println("\n" + partnerName+ ": As long as we’re alive, we’re all good.");
      System.out.println("\nYou laugh in agreement.");
    }
    return statChanges;
  }

  //DEBUGGING: Grace added int itemAns into the parameter // Brian deleted question prompt in method, 
  public static String itemHangman (Scanner in, String partnerName, int itemAns) { // Used because there are multiple instances of the same hangman in item pathway
      
    
      String challengeStatus = ""; // Create variable outside of if statements for later access
      if (itemAns == 1) {
        String[] formsOfArt  = {"music", "painting", "film", "theater", "literature"};
        challengeStatus = hangman (in, "Forms of Art", partnerName, formsOfArt, 3); // Challenge status 2 to denote that it's the second hangman game and because it will avoid duplicate variables
      } else if (itemAns == 2) {
        String[] snacks = {"chips", "popcorn", "oreos", "cookies", "fruit", "yogurt"};
        challengeStatus = hangman (in, "Snacks", partnerName, snacks, 3); // Challenge status 2 to denote that it's the second hangman game and because it will avoid duplicate variables
      } else if (itemAns == 3) {
        String[] famousArtists  = {"michelangelo", "johannes vermeer", "leonardo da vinci", "vincent van gogh", "pablo picasso"};
        challengeStatus = hangman (in, "Famous Artists", partnerName, famousArtists, 3); // Challenge status 2 to denote that it's the second hangman game and because it will avoid duplicate variables
      }
      return challengeStatus;
  }

  public static int[] useItem (int itemIndex, int[] statChanges) {
    int indexOfItem = -1; // Intalize outside of loop so we can access later
    for (int x = 0; x < statChanges.length; x++) {
      if (statChanges[x] == itemIndex) {
        indexOfItem = x;
      }
    }
    statChanges[indexOfItem] = 0; // Set the indexOfItem in statChange to 0 which is "Empty";
    return statChanges;
  }
            
  public static String hangman (Scanner in, String topic, String partnerName, String[] challengeWords, int difficultyLevel) {

    int randomIndex = (int) (Math.random() * challengeWords.length); 
    /*
    Creates a random index in the range of 0 - challengeWords.length. It will never be challengeWords.length because Math.random() gives a number from 0 to 0.999999.
    This means the largest number (Math.random() * challengeWords.length) will produce with a length of 8 will be 7.9999. By datacasting the value using (int) it will remove the decimal place and 
    return only the integer value. 7.999 -> 7. Therefore, the largest value will be Array.length - 1 and the smallest value will be 0; giving us a random index of the give array.
    */
    String chosenWord = challengeWords[randomIndex]; // Set the chosen word to the value at randomIndex
    String chosenWordString =  ""; // To accumulate the backwords word for difficulty 3
    String guessedLetters = ""; // To store letters that were already guessed
    

    System.out.println("\nThis challenge is difficulty level " + difficultyLevel);
    if (difficultyLevel == 1) { // One Word With Hint
      for (int x = 0; x < chosenWord.length(); x++) {
        chosenWordString = chosenWordString + chosenWord.charAt(x); // Accumulate chosenWordString
      }
      int randomHintIndex = (int) (Math.random() * chosenWordString.length()); 
      // Generates a random index and then print that random character
      char hintLetter = chosenWordString.charAt(randomHintIndex);
      System.out.println("Hint: This word contains a(n): " + hintLetter);
    } else if (difficultyLevel == 2) { // One Word no Hint
        for (int x = 0; x < chosenWord.length(); x++) {
          chosenWordString = chosenWordString + chosenWord.charAt(x); // Accumulate chosenWordString
        }
      System.out.println("No Hint!");
    } else if (difficultyLevel == 3) { // Backwards word
      for (int x = chosenWord.length() - 1; x >= 0; x--) {
        chosenWordString = chosenWordString + chosenWord.charAt(x); // Accumulate backword chosenWordString
      }
      System.out.println("\nThis word is backwards! NOTE: When guessing for the full word, write the word in the right direction.");
    }

    
    
    // Creating output array with "_" 's
    char[] outputArray = new char [chosenWord.length()]; // We create a char array instead of a Sring because we will mutate this array
    for (int x = 0; x < outputArray.length; x++) {
      outputArray[x] = '_'; // set all index's to _
    };
    // Lives
    int lives = 2 * chosenWord.length(); // Calculate lives based on length of the word
    if (lives > 15) {
      lives = 15; // If lives is too many set it to 15
    }
    if (partnerName.equals("Merlin the Sorcerer")) {
      System.out.println("Merlin casts a spell and gives you an extra life!");
      lives++;
    }
    

    System.out.println("Topic: " + topic);
    System.out.println("This is your word: ");
    for (int x = 0; x < outputArray.length; x++) { // Output empty string with ______ 
      System.out.print(outputArray[x]);
    }
    System.out.println(); // New Line
    boolean alive = true;
    String challengeStatus = ""; // Used to return String 
    do {
      String tempWord = ""; // Used to check if the user has finished hangman
      
      String userGuessString = ""; //For error trap
      do {
        System.out.println("Enter a guess: ");
        userGuessString = in.nextLine(); 
      } while (userGuessString.length() == 0); // If the user enters by accident the length will be zero and we ask again
      
      if (userGuessString.length() != 1) { // If the user doesn't enter a character we will check if the word is correct
        if (userGuessString.equalsIgnoreCase(chosenWord)) { // If the word is choseo
          System.out.println("You've passed!"); // If the user guesses the full word they've passed the challenge, 
          challengeStatus = "passed";
          System.out.println("The word was: " + chosenWord);
          break; // Once the user has guessed the word we want to break out of the while loop so the rest of the code doesn't run
        } else {
            System.out.println("That is the incorrect word.");
            lives--;
        }
      } else {
        char userGuessChar = userGuessString.charAt(0); // If the user doens't gusses a word
      
        if (guessedLetters.indexOf(userGuessChar) != -1) { // Check if letter was guessed
          System.out.println("You've already gusssed that letter");
        }
        if (chosenWord.indexOf(userGuessChar) != -1) { // If the indexOf userGuessChar is not -1 that means the character is in chosenWord
          for (int x = 0; x < outputArray.length; x++) { // Replacing the index of correct guess
            if (chosenWordString.charAt(x) == userGuessChar) {
              outputArray[x] = userGuessChar; // Replace the index of output array with the gussed letter
            }
          }
        } else {
          if (guessedLetters.indexOf(userGuessChar) == -1) {
            lives--;
          }
        }
        guessedLetters = guessedLetters + userGuessChar; // Add the gussed letter to the String of gussed leter for later reference.
        // Creating temperatory word
        for (int x = 0; x < outputArray.length; x++) {
          tempWord = tempWord + outputArray[x];
        }
  
        if (tempWord.indexOf('_') == -1 ) { // If there are no more _'s in the temporary word that means that the user has gussed all letters, set challenge status to passed and break out of while loop.
          System.out.println("You've passed!"); 
          challengeStatus = "passed";
          System.out.println("The word was: " + chosenWord);
          break; // Once the user has guessed the word we want to break out of the code
        }
        
        for (int x = 0; x < outputArray.length; x++) { // If user has not completed the hangman, output the String 
          System.out.print(outputArray[x]);
        }
      }
      
      
      System.out.println(); // New Line
      System.out.println("Number of lives left: " + lives);
      if (lives == 0) { // Check if they are out of lives, if they are set challengeStatus to failed and alive to false so the while loop won't run again.
        challengeStatus = "failed";
        System.out.println("The word was: " + chosenWord);
        System.out.println("You lost.");
        alive = false;
      }
    } while (alive);
    return challengeStatus; // Returns the value of the challenge 
  }

  public static int isNumOutOfRangeOfThree(Scanner in, int userInput){
    
    while ((userInput > 3) || (userInput < 1)){ // error trap for out of range numbers
      System.out.println("Please Enter a Valid Value.");
      userInput = in.nextInt();
    } 

    return userInput;
  }
  
  public static int isNumOutOfRangeOfTwo(Scanner in, int userInput){

    while ((userInput > 2) || (userInput < 1)){ // error trap for out of range numbers, if the number is already from 1-3 then this code won't run and the original answer will be returned
      System.out.println("Please Enter a Valid Value.");
      userInput = in.nextInt();
    } 

    return userInput;
  }

  public static void printLose(){ 
    System.out.println("▓██   ██▓ ▒█████   █    ██     ██▓     ▒█████    ██████ ▓█████  ▐██▌ ");
    System.out.println(" ▒██  ██▒▒██▒  ██▒ ██  ▓██▒   ▓██▒    ▒██▒  ██▒▒██    ▒ ▓█   ▀  ▐██▌ ");
    System.out.println("  ▒██ ██░▒██░  ██▒▓██  ▒██░   ▒██░    ▒██░  ██▒░ ▓██▄   ▒███    ▐██▌ ");
    System.out.println("  ░ ▐██▓░▒██   ██░▓▓█  ░██░   ▒██░    ▒██   ██░  ▒   ██▒▒▓█  ▄  ▓██▒ ");
    System.out.println("  ░ ██▒▓░░ ████▓▒░▒▒█████▓    ░██████▒░ ████▓▒░▒██████▒▒░▒████▒ ▒▄▄  ");
    System.out.println("   ██▒▒▒ ░ ▒░▒░▒░ ░▒▓▒ ▒ ▒    ░ ▒░▓  ░░ ▒░▒░▒░ ▒ ▒▓▒ ▒ ░░░ ▒░ ░ ░▀▀▒ ");
    System.out.println(" ▓██ ░▒░   ░ ▒ ▒░ ░░▒░ ░ ░    ░ ░ ▒  ░  ░ ▒ ▒░ ░ ░▒  ░ ░ ░ ░  ░ ░  ░ ");
    System.out.println(" ▒ ▒ ░░  ░ ░ ░ ▒   ░░░ ░ ░      ░ ░   ░ ░ ░ ▒  ░  ░  ░     ░       ░ ");
    System.out.println(" ░ ░         ░ ░     ░            ░  ░    ░ ░        ░     ░  ░ ░   ");
    System.out.println(" ░ ░                                                                 ");
  
  }
}