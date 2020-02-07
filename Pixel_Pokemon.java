// PIXEL POKEMON

import java.util.*;
import java.io.*;

public class Pixel_Pokemon{
  private static ArrayList <Pokemon> pokemon;     						// arrayList of all pokemon
  private static ArrayList <Integer> pPokemon;     			 			// arrayList of player's pokemon
  private static ArrayList <Integer> ePokemon;      					// arrayList of enemy pokemon
  
  private static int turn = 0;     										// keeps track of turns
  private static int PLAYER = 0, ENEMY = 1;     
  private static String ATTACK = "A", RETREAT = "R", PASS = "P";      	// possible actions
  
  private static String action;
  
  private static int choosePlayer(){     								// player picks one of four chosen pokemon to begin round
    Scanner kb = new Scanner(System.in);
    System.out.println("\nChoose Pokemon to battle:");
    
    for (int i = 0; i < 4; i++){      									// displays pokemon team
      System.out.printf("%2d. %s\n", i, ((pokemon.get(pPokemon.get(i))).getName()).toUpperCase());
    }
    
    int p = kb.nextInt();
    Pokemon player = pokemon.get(pPokemon.get(p));
    String pName = player.getName();
    
    while (player.KO() == true){     									// checks if pokemon is defeated
      System.out.println("\n" + pName.toUpperCase() + " has been defeated.");
      System.out.println("Please choose another Pokemon.");
      for (int i = 0; i < 4; i++){ 
        System.out.printf("%2d. %s\n", i, ((pokemon.get(pPokemon.get(i))).getName()).toUpperCase());
      }
      
      p = kb.nextInt();
      player = pokemon.get(pPokemon.get(p));
      pName = player.getName();
    }
    
    System.out.println("\n" + pName.toUpperCase() + ", I choose you !");
    player.displayStats();
    player.displayAtks();
    
    return p;     														// chosen player pokemon
  }
  
  
  public static int chooseEnemy(){      								// randomly generates enemy pokemon
    int e = (int)(Math.random()*(ePokemon.size()));   
    
    Pokemon enemy = pokemon.get(ePokemon.get(e));
    String eName = enemy.getName();
    
    System.out.println("\nYour opponent chooses " + eName.toUpperCase() + " !");
    enemy.displayStats();
    enemy.displayAtks();

    return e;     														// chosen enemy pokemon
  }
  
  public static void battle(){
    
    int t = (int)(Math.random()*(2));     								// randomly chooses turn to begin
    int r = chooseEnemy();
    int e = ePokemon.get(r);     
    int p = pPokemon.get(choosePlayer());    
    Pokemon player = pokemon.get(p);      								// chosen player pokemon
    Pokemon enemy = pokemon.get(e);     								// chosen enemy pokemon
    
    String pName = player.getName();      								// player pokemon name
    String eName = enemy.getName();     								// enemy pokemon name
    
    while(enemy.KO() == false){   										// while enemy has above 0 HP
      
      // PLAYER TURN
      
      if (t == PLAYER){
        
        Scanner kb = new Scanner(System.in);
        
        while (player.KO() == true){    
          System.out.println("Please choose another Pokemon.");
          for (int i = 0; i < 4; i++){      
            System.out.printf("%2d. %s\n", i, ((pokemon.get(pPokemon.get(i))).getName()).toUpperCase());
          }
          
          p = pPokemon.get(kb.nextInt());
          player = pokemon.get(p);
          pName = player.getName();
          System.out.println("\n");
        }
        
        if (player.stunned() == true){      							// checks if pokemon is stunned
          System.out.println(pName.toUpperCase() + " is stunned!");
          action = PASS;
          player.unstun();
        }
        
        else{
          System.out.println("<< Your turn. >>\n");
          System.out.println("Choose action:");     					// player chooses action
          System.out.println("['A' to Attack, 'R' to Retreat, 'P' to Pass]");
        
          action = (kb.nextLine()).toUpperCase();     					// chosen action
        }
        
        // ATTACK
        
        if (action.equals(ATTACK)){   
          if (player.checkEnergy() == true){      						// checks if pokemon has enough energy to attack
            player.attack(enemy, t);      								// performs attack
            player.getStats(enemy);
          }
          else{
            System.out.println("Pokemon does not have enough energy to attack.");
            System.out.println("Please select another action.");
            action = kb.nextLine(); 
          }
        }
        
        // RETREAT
        
        if (action.equals(RETREAT)){     
          
          System.out.println("\nChoose Pokemon to battle:");
      
          for (int i = 0; i < 4; i++){      							// displays pokemon team
            System.out.printf("%2d. %s\n", i, (pokemon.get(pPokemon.get(i))).getName());
          }
          
          p = pPokemon.get(kb.nextInt());
          player = pokemon.get(p);
          pName = player.getName();
          
          while (player.KO() == true){    
            System.out.println("\n" + pName.toUpperCase() + " has been defeated.");
            System.out.println("Please choose another Pokemon.");
            for (int i = 0; i < 4; i++){      
              System.out.printf("%2d. %s\n", i, ((pokemon.get(pPokemon.get(i))).getName()).toUpperCase());
            }
            
            p = pPokemon.get(kb.nextInt());
            player = pokemon.get(p);
            pName = player.getName();
          }
        
          System.out.println("\n" + pName.toUpperCase() + ", I choose you !");
          player.getStats(enemy);
        }
  
        // PASS
        
        if (action.equals(PASS)){    
          System.out.println("\n" + pName.toUpperCase() + " passes !");
          player.getStats(enemy);
        }
      
      t = 1;      														// alternates turn
      System.out.println("");
      
      }
      
      // ENEMY TURN
      
      else if (t == ENEMY){
        
        String [] actions = {"A", "P"};     							// possible actions
        int n = (int)(Math.random()*(2));     							// opponent chooses random action
        
        if (enemy.stunned() == true){      								// checks if pokemon is stunned
          action = PASS;
          enemy.unstun();
        }
        
        else{
          action = actions[n];     										// chosen action
          System.out.println("\n<< Opponent's turn. >>\n");
        }
        
        // ATTACK
        
        if (action.equals(ATTACK)){ 
          if (enemy.checkEnergy() == true){      						// checks if pokemon has enough energy to attack
            enemy.attack(player, t);      								// performs attack
            enemy.getStats(player);
          }
          else{
            action = PASS;
          }
        }
        
        // PASS
        
        if (action.equals(PASS)){  
          System.out.println(eName.toUpperCase() + " passes !");
          enemy.getStats(player);
        }
      
      t = 0;      														// alternates turn
      System.out.println("");
      
      }
      
      if (defeat() == true){
        gameOver();
        break;
      }
      
      turn ++;    
      
      if (player.KO() == true){     									// checks if player pokemon is defeated
        System.out.println("\nOh no! " + pName.toUpperCase() + " has been defeated.");
      }
    
      // CHARGE ENERGY         [ charges all pokemon's energies at end of each round ]
      
      if (turn%2 == 0){
        for (int i = 0; i < 4; i ++){     
        (pokemon.get(pPokemon.get(i))).charge();
      }
        
        enemy.charge();
      }
    
    }
    
    // HEAL HP        [ heals player's pokemon at end of each battle ]
    
    if (defeat() == false){    
      if (ePokemon.size() > 1){     									// while enemy list contains pokemon to battle
        for (int i = 0; i < 4; i ++){
          if (pokemon.get(pPokemon.get(i)).KO() == false){      		// checks if pokemon has been defeated
            (pokemon.get(pPokemon.get(i))).heal();              		// if not, heal and undo disable effect
          }
        }
        
        System.out.println("\n<< VICTORY ! >>");
        System.out.println(eName.toUpperCase() + " has been defeated !");
        ePokemon.remove(r);     										// removes enemy pokemon from possible enemies 
        battle();     													// next round
      }
      
      else{     														// if enemy list is empty player wins
        victory();
      }
    }
   
  }
  
  public static void victory(){     									// player wins
    System.out.println("\nCONGRATS ! You have won the POKEMON BATTLE ! You are now crowned << TRAINER SUPREME ! >>");
  }
  
  public static void gameOver(){      									//player loses
    System.out.println("\nOh no! All your pokemon have been defeated and you are unable to fight.");
    System.out.println("Your opponent has won the POKEMON BATTLE.");
  }
  
  public static boolean defeat(){
    int d = 0;
    
    for (int i = 0; i < 4; i ++){
      if (pokemon.get(pPokemon.get(i)).KO() == true){
        d++;
      }  
    }
    
    if (d == 4){
      return true;
    }
    else{
      return false;
    }
  }
  
  public static void main(String [] args){
    
    pokemon = new ArrayList <Pokemon> ();   
    pPokemon = new ArrayList <Integer> (); 
    ePokemon = new ArrayList <Integer> ();      
        
    try{ 
      
      Scanner inFile = new Scanner(new File("pokemon.txt"));      		// inputs pokemon data from text file
      int n = inFile.nextInt();
      inFile.nextLine();   												// remove extra newline left by nextInt()
      
      for(int i = 0; i < n; i++){
        String stats = inFile.nextLine();   							// add stats from data file
        pokemon.add(new Pokemon(stats));
      }
      
    }
    
    catch(IOException ex){
      System.out.println("File not Found.");
    }
    
    // MENU
    
    Scanner kb = new Scanner(System.in);
    
    System.out.println("Welcome To...");
    System.out.println("POKEMON ARENA: Gotta Beat 'Em All ! \n");
    System.out.println("Would you like view the rules? (Y/N)");
    
    String r = (kb.nextLine()).toUpperCase();
    
    if (r.equals("Y")){
      System.out.println("\nRULES:");
      System.out.println("Choose a team of 4 Pokemon to enter the battle and defeat your opponent"); 
      System.out.println("to win the game and become �Trainer Supreme�. Take turns to attack, one"); 
      System.out.println("Pokemon at a time. If a Pokemon is hit by a Pokemon whose type is his"); 
      System.out.println("weakness, the attack damage is doubled. However, if he is hit by a Pokemon"); 
      System.out.println("he is resistant to, the damage is cut in half. Each Pokemon recovers 10");
      System.out.println("energy after each round and 20 HP after each battle.\n");
      System.out.println("Let's begin !\n");
    }
    
    else{
      System.out.println("Let's begin !\n");
    }
    
    System.out.println("| # |   NAME   |  HP  |  TYPE  |  RESIST  | WEAKNESS |");
    //System.out.println("--------------------------------------------------------");
    
    for(int i = 0; i < pokemon.size(); i ++){       					// displays list of pokemon stats
      System.out.printf(" %2d.  %s\n", i, (pokemon.get(i)).displayMenu());
    }
    
    // CHOOSE PLAYER POKEMON
    
    System.out.println("\nChoose your Pokemon Team !");
    
    int c = 0;

    while(c < 4){     													// checks if pokemon has already been chosen
      int p = kb.nextInt();
      
      if(!pPokemon.contains(p) && p >= 0 && p <= 27){     				// checks if number is within range of available pokemon
        pPokemon.add(p);
        c ++;
      }     
      
      else{
        System.out.println("Invalid choice.");
      }
    }
    
    // ENEMY POKEMON
    
    for(int i = 0; i < pokemon.size(); i ++){     						// adds all pokemon unchosen by player into opponent's pokemon list
      if (!pPokemon.contains(i)){
        ePokemon.add(i);
      }
    }
    
    battle();     														// start battle

  }
}