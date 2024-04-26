package cwk4;
import java.util.*;
import java.io.*;
/**
 * This interface specifies the behaviour expected from CARE
 * as required for 5COM2007 Cwk 4
 *
 * @author
 * @version
 */

public class Tournament implements CARE
{
    private String vizier;
    private ArrayList<Champion> champions;
    private ArrayList<Champion> reserves;
    private int treasury = 1000;
    private  ArrayList<Challenge> challengeList= new ArrayList<Challenge>();
    private  ArrayList<String> challengeListStr= new ArrayList<String>();

//**************** CARE **************************
    /** Constructor requires the name of the vizier
     * @param viz the name of the vizier
     */
    public Tournament(String viz)
    {
        this.vizier = viz;
        champions = new ArrayList<>();
        reserves = new ArrayList<>();
        setupChampions();
        setupChallenges();
        vizier = viz;
    }

    /** Constructor requires the name of the vizier and the
     * name of the file storing challenges
     * @param viz the name of the vizier
     * @param filename name of file storing challenges
     */
    public Tournament(String viz, String filename)  //Task 3.5
    {

        this.vizier = viz;
        champions = new ArrayList<>();
        reserves = new ArrayList<>();
        vizier = viz;
        setupChampions();
        readChallenges(filename);
    }


    /**Returns a String representation of the state of the game,
     * including the name of the vizier, state of the treasury,
     * whether defeated or not, and the champions currently in the
     * team,(or, "No champions" if team is empty)
     *
     * @return a String representation of the state of the game,
     * including the name of the vizier, state of the treasury,
     * whether defeated or not, and the champions currently in the
     * team,(or, "No champions" if team is empty)
     **/
    public String toString()
    {
        String s = "\nVizier: " + vizier ;
        s+="\nTreasury: "+treasury;
        s+="\nDefeated: ";
        if (isDefeated()){
            s+="Defeated";
        }else {
            s+="Is OK";
        }
        s+="\nIn Team: "+getTeam();

        return s;
    }


    /** returns true if Treasury <=0 and the vizier's team has no
     * champions which can be retired.
     * @returns true if Treasury <=0 and the vizier's team has no
     * champions which can be retired.
     */
    public boolean isDefeated()
    {
        if (treasury<=0){
            return champions.isEmpty();
        }
        return false;
    }

    /** returns the amount of money in the Treasury
     * @returns the amount of money in the Treasury
     */
    public int getMoney()
    {
        return treasury;
    }


    /**Returns a String representation of all champions in the reserves
     * @return a String representation of all champions in the reserves
     **/
    public String getReserve()
    {
        StringBuilder s = new StringBuilder("************ Champions in Reserve ********");
        for (Champion res : reserves) {
            s.append("\n").append(res.toString());
        }
        return s.toString();
    }


    /** Returns details of the champion with the given name.
     * Champion names are unique.
     * @return details of the champion with the given name
     **/
    public String getChampionDetails(String nme) {
        Champion xx = getChamp(nme);
        if(xx != null) {
            if (xx.getChampState() == ChampionState.DISQUALIFIED) {
                return xx.toString() + "\nStatus: Disqualified";
            }
            return xx.toString();
        }
        return "\nNo such champion";
    }

    /** returns whether champion is in reserve
     * @param nme champion's name
     * @return true if champion in reserve, false otherwise
     */
    public boolean isInReserve(String nme) {
        for (Champion champion : reserves) {
            if (champion.getName().equals(nme) && champion.getChampState()==ChampionState.WAITING) {
                return true;
            }
        }
        return false;
    }

    // ***************** Team champions ************************
    /** Allows a champion to be entered for the vizier's team, if there
     * is enough money in the Treasury for the entry fee.The champion's
     * state is set to "active"
     * 0 if champion is entered in the vizier's team,
     * 1 if champion is not in reserve,
     * 2 if not enough money in the treasury,
     * -1 if there is no such champion
     * @param nme represents the name of the champion
     * @return as shown above
     **/
     public int enterChampion(String nme) {
         Champion champion = getChamp(nme);

         if (champion != null) {
             int entryFee = champion.getEntryFee();
             if (isInViziersTeam(champion.getName())) {
                 return 1;
             } else if (treasury < entryFee) {
                 return 2; //Not enough guld
             } else {
                 reserves.remove(champion);
                 champions.add(champion);
                 treasury -= entryFee;
                 champion.setChampState(ChampionState.ENTERED);
                 return 0; //Entered Vizier's team
             }
         }
         else{
             return -1;
         }
     }
        
     /** Returns true if the champion with the name is in
     * the vizier's team, false otherwise.
     * @param nme is the name of the champion
     * @return returns true if the champion with the name
     * is in the vizier's team, false otherwise.
     **/
    public boolean isInViziersTeam(String nme)
    {
        for (Champion champion : champions) {
            if (champion.getName().equals(nme)) {
                return true;
            }
        }
        return false;
    }

    /** Removes a champion from the team back to the reserves (if they are in the team)
     * Pre-condition: isChampion()
     * 0 - if champion is retired to reserves
     * 1 - if champion not retired because disqualified
     * 2 - if champion not retired because not in team
     * -1 - if no such champion
     * @param nme is the name of the champion
     * @return as shown above
     **/
    public int retireChampion(String nme)
    {
        Champion champ = getChamp(nme);
        if (!isInViziersTeam(nme)){
            return 2;
        } else if (champ.getChampState() == ChampionState.DISQUALIFIED) { //checks if champion has been disqualified
            return 1;
        } else if (champ == null) {
            return -1;
        } else{
            champ.setChampState(ChampionState.WAITING);
            champions.remove(champ);
            reserves.add(champ);
            treasury = treasury + (champ.getEntryFee() / 2);
            return 0;
        }
    }



    /**Returns a String representation of the champions in the vizier's team
     * or the message "No champions entered"
     * @return a String representation of the champions in the vizier's team
     **/
    public String getTeam()
    {
        StringBuilder s = new StringBuilder("************ Vizier's Team of champions********");
        if(champions.isEmpty()){
            return "\nNo champions entered";
        }
        for(Champion champ: champions){
            s.append("\n"+champ.toString());
        }
        return s.toString();
    }

    /**Returns a String representation of the disquakified champions in the vizier's team
     * or the message "No disqualified champions "
     * @return a String representation of the disqualified champions in the vizier's team
     **/
    public String getDisqualified()
    {
        StringBuilder s = new StringBuilder("************ Vizier's Disqualified champions********");
        int counter = 0;
        for(Champion champ: champions){
            if(champ.getChampState()== ChampionState.DISQUALIFIED){
                s.append("\n").append(champ.toString());
                counter++;
            }
        }
        if(counter > 0){return "No disqualified champions";}
        return s.toString();
    }

//**********************Challenges*************************
    /** returns true if the number represents a challenge
     * @param num is the  number of the challenge
     * @return true if the  number represents a challenge
     **/
    public boolean isChallenge(int num)
    {
        return num > 0 && num <= challengeList.size();
    }

    /** Provides a String representation of a challenge given by
     * the challenge number
     * @param num the number of the challenge
     * @return returns a String representation of a challenge given by
     * the challenge number
     **/
    public String getChallenge(int num)
    {
        if(isChallenge(num)) {
            return getSpecificChallenge(num).toString();
        }
        return "Challenge does not exist";
    }

    /** Provides a String representation of all challenges
     * @return returns a String representation of all challenges
     **/
    public String getAllChallenges()
    {
        StringBuilder s = new StringBuilder("\n************ All Challenges ************\n");
        if(challengeList.isEmpty()){
            return "There are no challenges";
        }
        for (Challenge xx: challengeList){
            s.append("\n").append(xx.toString());
        }


        return s.toString();
    }


    /** Retrieves the challenge represented by the challenge
     * number.Finds a champion from the team who can meet the
     * challenge. The results of meeting a challenge will be
     * one of the following:
     * 0 - challenge won by champion, add reward to the treasury,
     * 1 - challenge lost on skills  - deduct reward from
     * treasury and record champion as "disqualified"
     * 2 - challenge lost as no suitable champion is  available, deduct
     * the reward from treasury
     * 3 - If a challenge is lost and vizier completely defeated (no money and
     * no champions to withdraw)
     * -1 - no such challenge
     * @param chalNo is the number of the challenge
     * @return an int showing the result(as above) of fighting the challenge
     */
    public int meetChallenge(int chalNo) {
        Challenge chal = getSpecificChallenge(chalNo);
        if (chal != null) {
            Champion fighter = getChampionForChallenge(chalNo);
            boolean result = chal.doChallenge(fighter);
            if (fighter == null) {
                this.treasury -= chal.getReward();
                return 2;
            } else if (result) {
                this.treasury += chal.getReward();
                return 0;
            } else {
                this.treasury -= chal.getReward();
                fighter.setChampState(ChampionState.DISQUALIFIED);
                return 1;
            }
        }
        if (this.isDefeated()) return 3;
        return -1;
    }


    //****************** private methods for Task 3 functionality*******************
    //*******************************************************************************
    private void setupChampions() {
        reserves.add(new Wizard("Ganfrank", 7, 400, true, "transmutation"));
        reserves.add(new Wizard("Rudolf", 6, 400, true, "invisibility"));
        reserves.add(new Warrior("Elblond", 1, 150, "sword"));
        reserves.add(new Warrior("Flimsi", 2, 200, "bow"));
        reserves.add(new Dragon("Drabina", 7, 500, false));
        reserves.add(new Dragon("Golum", 7, 500, true));
        reserves.add(new Warrior("Argon", 9, 900, "mace"));
        reserves.add(new Wizard("Neon", 2, 300, false, "translocation"));
        reserves.add(new Dragon("Xenon", 7, 500, true));
        reserves.add(new Warrior("Atlanta", 5, 500, "bow"));
        reserves.add(new Wizard("Krypton", 8, 300, false, "fireballs"));
        reserves.add(new Wizard("Hedwig", 1, 400, true, "flying"));
    }

    private void setupChallenges()
    {
        challengeList.add(new Challenge(1,ChallengeType.MAGIC, "Borg", 3, 100));
        challengeList.add(new Challenge(2,ChallengeType.FIGHT, "Huns", 3, 120));
        challengeList.add(new Challenge(3,ChallengeType.MYSTERY, "Ferengi", 3, 150));
        challengeList.add(new Challenge(4,ChallengeType.MAGIC, "Vandal", 9, 200));
        challengeList.add(new Challenge(5,ChallengeType.MYSTERY, "Borg", 7, 90));
        challengeList.add(new Challenge(6,ChallengeType.FIGHT, "Goth", 8, 45));
        challengeList.add(new Challenge(7,ChallengeType.MAGIC, "Frank", 10, 200));
        challengeList.add(new Challenge(8,ChallengeType.FIGHT, "Sith", 10, 170));
        challengeList.add(new Challenge(9,ChallengeType.MYSTERY, "Cardashian", 9, 300));
        challengeList.add(new Challenge(10,ChallengeType.FIGHT, "Jute", 2, 300));
        challengeList.add(new Challenge(11,ChallengeType.MAGIC, "Celt", 2, 250));
        challengeList.add(new Challenge(12,ChallengeType.MYSTERY, "Celt", 1, 250));

    }
    /*---------Helper Functions--------*/
    public Champion getChampionForChallenge(int chalNo){
        Challenge xx = getSpecificChallenge(chalNo);
        for(Champion ww: champions){

            if(ww.canMeetChallenge(xx.getChallengeType()) || ww.available()){
                return ww;
            }
        }
        return null;
    }

    public Champion getChamp(String nme){
        for(Champion xx:champions){
            if(xx.getName().equals(nme)){
                return xx;
            }
        }
        for(Champion yy:reserves){
            if(yy.getName().equals(nme)){
                return yy;
            }
        }
        return null;
    }

    public Challenge getSpecificChallenge(int No){
        for(Challenge xx: challengeList){
            if(xx.getChalNo() == No){
                return xx;
            }
        }
        return null;

    }

    // Possible useful private methods
//     private Challenge getAChallenge(int no)
//     {
//
//         return null;
//     }
//
//     private Champion getChampionForChallenge(Challenge chal)
//     {
//
//         return null;
//     }

    //*******************************************************************************
    //*******************************************************************************

    /************************ Task 3.5 ************************************************/

    // ***************   file write/read  *********************
    /**
     * reads challenges from a comma-separated textfile and stores in the game
     * @param filename of the comma-separated textfile storing information about challenges
     */

    public void readChallenges(String filename) {
        try {
            File challengeFile = new File("challengesAM.txt");
            Scanner myReader = new Scanner(challengeFile);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                challengeListStr.add(line);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            return;
        }
        int challengeNum = challengeListStr.size();
        for (int num = 0; num < challengeListStr.size(); num++) {
            ArrayList<String> challengeFields = getStrings(num);
            ChallengeType chalEnum = switch (challengeFields.get(0)) {
                case "Magic" -> ChallengeType.MAGIC;
                case "Fight" -> ChallengeType.FIGHT;
                case "Mystery" -> ChallengeType.MYSTERY;
                default -> null;
            };

            challengeList.add(new Challenge(num, chalEnum,
                    challengeFields.get(1), Integer.parseInt(challengeFields.get(2))
                    , Integer.parseInt(challengeFields.get(3))));
        }
    }

    private ArrayList<String> getStrings(int num) {
        String toBeArray = challengeListStr.get(num);
        int counter = 0;
        StringBuilder word = new StringBuilder();
        char breaker = ',';
        ArrayList<String> challengeFields = new ArrayList<String>();
        while (counter != toBeArray.length()) {
            char letter = toBeArray.charAt(counter);
            if (letter == breaker) {
                challengeFields.add(word.toString());
                word = new StringBuilder();
            } else {
                word.append(Character.toString(letter));
            }
            counter += 1;
        }
        return challengeFields;
    }

    /** reads all information about the game from the specified file
     * and returns a CARE reference to a Tournament object, or null
     * @param fname name of file storing the game
     * @return the game (as a Tournament object)
     */
    public Tournament loadGame(String fname)
    {   // uses object serialisation
        Tournament yyy;
        try{
            FileInputStream loadFile = new FileInputStream(fname);
            ObjectInputStream loadObject = new ObjectInputStream(loadFile);
            yyy = (Tournament) loadObject.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return yyy;
    }

    /** Writes whole game to the specified file
     * @param fname name of file storing requests
     */
    public void saveGame(String fname){
        // uses object serialisation
        try {
            FileOutputStream saveFile = new FileOutputStream(fname);
            ObjectOutputStream saveObject = new ObjectOutputStream(saveFile);
            saveObject.writeObject(this);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


}