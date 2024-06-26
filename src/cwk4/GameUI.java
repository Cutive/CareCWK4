package cwk4;
import java.io.*;
import java.util.*;
/**
 * Command line interface
 * 
 * @author A.A.Marczyk
 * @version 10/03/2024
 */
public class GameUI
{
    
    private Scanner myIn = new Scanner(System.in);

    public void runGame()
    {
        Tournament tr ;
        int choice;
        String vizierName;
        String output = "";
        int result = -1;
        try
        {
            System.out.println("Enter vizier's name");
            String s = myIn.nextLine();
            //myIn.nextLine();
            tr = new Tournament(s); // create
            tr = new Tournament(s,"challengesAM.txt"); // alternative create task 3.5
            choice = 100;
            while (choice != 0 )
            //0 = quit
            {
                choice = getMenuItem();
                if (choice == 1)
                //1 = list champions in reserve
                {
                    System.out.println(tr.getReserve());
                }
                else if (choice == 2)
                //2 = list champions in viziers team
                {
                    System.out.println(tr.getTeam());
                }
                else if (choice == 3)
                //3 = view a champion
                {
                    System.out.println("Enter Champion name");
                    String champ = (myIn.nextLine()).trim();
                    System.out.println(tr.getChampionDetails(champ));
                } 
                else if (choice == 4)
                //4 = enter champion into viziers teamshi
                {   
                    System.out.println("Enter Champion name");
                    String champ = myIn.nextLine();
                    if (!tr.isInViziersTeam(champ))
                    //if the champion is NOT in the viziers team already
                    {
                        System.out.println(tr.enterChampion(champ));
                    }

                }
                else if (choice == 5)
                //5 = meet a challenge
                {
                    System.out.println("Enter the Challenge ID");
                    int chalNo = myIn.nextInt();
                    System.out.println(tr.meetChallenge(chalNo));
                }
                else if (choice == 6)
                //6 = retire a champion
                {
                    System.out.println("Enter Champion name that you want to retire");
                    String champ = myIn.nextLine();
                    System.out.println(tr.retireChampion(champ));
                }
                else if (choice == 7)
                //7 = view game state
                {
                    System.out.println(tr.toString());
                }
                else if (choice == 8)
                //8 = see all challenges
                {
                    System.out.println(tr.getAllChallenges());
                }
                else if (choice == 9) // Task 3.5 only
                //9 = save game
                {
                    System.out.println("Write to file");
                    System.out.println("Enter file name");
                    String filename = myIn.nextLine();
                    tr.saveGame(filename);
                }
                else if (choice == 10) // Task 3.5 only
                //10 = load game
                {
                    System.out.println("Restore from file");
                    System.out.println("Enter file name");
                    String filename = myIn.nextLine();
                    CARE tr2= tr.loadGame(filename);
                    if (tr2 != null)
                    {
                        System.out.println(tr2.toString());
                    }
                    else
                    {
                        System.out.println("No such file");
                    }
                }
            }     
        }
        catch (IOException e) {System.out.println (e);}   
        System.out.println("Thank-you");
    }
    
    private int getMenuItem()throws IOException
    {   int choice = 100;  
        System.out.println("\nMain Menu");
        System.out.println("0. Quit");
        System.out.println("1. List champions in reserve");
        System.out.println("2. List champions in viziers team"); 
        System.out.println("3. View a champion");
        System.out.println("4. Enter champion into vizier's team");
        System.out.println("5. Meet a challenge");
        System.out.println("6. Retire a champion");
        System.out.println("7. View game state");
        System.out.println("8. See all challenges");
        System.out.println("9. Save this game");
        System.out.println("10. Load this game");
       
        
        while (choice < 0 || choice  > 10)
        {
            System.out.println("Enter the number of your choice");
            choice =  myIn.nextInt();
        }
        myIn.nextLine();
        return choice;        
    }  
    
    private String processChallengeResult(int res) {
    
        String out;
        if (res ==0)
        {
            out = "Challenge won";
        }
        else if (res ==1)
        {
            out = "Challenge lost on skill level";
        }
        else if (res ==2)
        {
            out = "Challenge lost as no champion available";
        }
        else if (res ==3)
        {
            out = "Challenge lost with no further resources. You lose the game ";
        }
        else if (res == -1)
        {
            out = "No such challenge";
        }
        else
        {
            out = "No such result";
        }
        return out; 
    }
    
    public static void main(String[] args)
    {
        new GameUI().runGame();
    }
}