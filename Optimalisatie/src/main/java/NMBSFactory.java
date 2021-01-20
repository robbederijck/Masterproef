import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NMBSFactory {

    private int amountOfWeeks;
    private boolean moreThenTwoConsecutiveHomeDays = true;

    // WEIGHTS
    private int INFEASIBILITY_WEIGHT = 2000;
    private int EXTRA_INFEASIBILITY_PENALTY = 5;

    private int ROUTINE_WEIGHT = 25;
    private int OA_WEIGHT = 80; //WAS: 60 //WAS: 45
    private int RCXCY_WEIGHT = 7;
    private int CONSPRES_LESS_WEIGHT = 2;
    private int CONSPRES_MORE_WEIGHT = 25; //WAS: 10
    private int CONSHOME_WEIGHT = 9;
    private int SPLITWEEKEND_WEIGHT = 20;
    private int HUMANIZATION_WEIGHT = 30;
    private int DISTRIBUTION_WEEKENDS_WEIGHT = 20;
    private int MACHINE_LEARNING_WEIGHT = 100;

    private MachineLearningModel thisModel;

    private HashMap<Integer, Prestation> idToPrestation;
    private HashMap<Integer, Prestation> optimizationIdToPrestation;
    private HashMap<Integer, ArrayList<Prestation>> dayToPrestatations;

    public NMBSFactory(int amountOfWeeks, HashMap<Integer, Prestation> idToPrestation, HashMap<Integer, ArrayList<Prestation>> dayToPrestatations) {
       this.amountOfWeeks = amountOfWeeks;
       this.idToPrestation = idToPrestation;
       this.dayToPrestatations = dayToPrestatations;
       try{
           this.thisModel = new MachineLearningModel();
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    public double predictInMachineLearningModel(float[][][] dataNMBS){
        return this.thisModel.predict(dataNMBS);
    }

    public Prestation getPrestation(int id){
        return idToPrestation.get(id);
    }

    public int getAmountOfWeeks(){
        return this.amountOfWeeks;
    }

    public int INFEASIBILITY_WEIGHT(){
        return this.INFEASIBILITY_WEIGHT;
    }

    public int EXTRA_INFEASIBILITY_PENALTY(){
        return this.EXTRA_INFEASIBILITY_PENALTY;
    }

    public int ROUTINE_WEIGHT() {
        return ROUTINE_WEIGHT;
    }

    public int OA_WEIGHT() {
        return OA_WEIGHT;
    }

    public int RCXCY_WEIGHT() {
        return RCXCY_WEIGHT;
    }

    public int CONSPRES_LESS_WEIGHT() {
        return CONSPRES_LESS_WEIGHT;
    }

    public int CONSPRES_MORE_WEIGHT(){
        return CONSPRES_MORE_WEIGHT;
    }

    public int HUMANIZATION_WEIGHT(){
        return HUMANIZATION_WEIGHT;
    }

    public int MACHINE_LEARNING_WEIGHT(){
        return MACHINE_LEARNING_WEIGHT;
    }

    public int DISTRIBUTION_WEEKENDS_WEIGHT(){
        return DISTRIBUTION_WEEKENDS_WEIGHT;
    }

    public int CONSHOME_WEIGHT() {
        return CONSHOME_WEIGHT;
    }

    public int SPLITWEEKEND_WEIGHT() {
        return SPLITWEEKEND_WEIGHT;
    }

    public boolean getMoreThanTwoConsecutiveHomeDays(){
        return this.moreThenTwoConsecutiveHomeDays;
    }

    public int[][] getEmptyPersonnelSchedule(){
        return new int[7][amountOfWeeks];
    }

    // Het berekenen van een greedy eerste oplossing (mag infeasible zijn)
    // Idee: Eerste dag random - andere dagen wordt er 14u nadien een prestatie gepland (zo dicht mogelijk)
    public Boolean calculateInitialSchedule(int[][] assigned){

        // Hiermee wordt later bekeken welke indexes al in gebruik genomen zijn door prestaties
        HashMap<Integer, ArrayList<Integer>> unUsedIndexesPerDay = new HashMap<>();
        for(int day = 0; day < 7; day++){
            ArrayList<Integer> range = (ArrayList<Integer>) IntStream.rangeClosed(0, amountOfWeeks-1)
                    .boxed().collect(Collectors.toList());
            unUsedIndexesPerDay.put(day, range);
        }

        // Prestaties van maandag aan random weken / drivers toekennen
        ArrayList<Prestation> prestationsOfTheDay = dayToPrestatations.get(0);
        ArrayList<Integer> usedIndexes = new ArrayList<Integer>();
        int scheduledPrestations = 0;

        while(scheduledPrestations != prestationsOfTheDay.size()){
            int randomIndex = (int) (Math.random() * amountOfWeeks);
            if(!usedIndexes.contains(randomIndex)){
                assigned[0][randomIndex] = prestationsOfTheDay.get(scheduledPrestations).getOptimizationID();
                usedIndexes.add(randomIndex);
                scheduledPrestations++;
                unUsedIndexesPerDay.get(0).remove(Integer.valueOf(randomIndex));
            }
        }

        // Random index bekijken en zien welke prestaties daar het beste past (null -> Random prestatie)
        for(int day = 1; day < 7; day++){
            prestationsOfTheDay = new ArrayList<>(dayToPrestatations.get(day)); // Shallow copy
            int amountOfPrestations = prestationsOfTheDay.size();
            usedIndexes = new ArrayList<Integer>();
            scheduledPrestations = 0;

            int iterations = 0;

            while(scheduledPrestations != amountOfPrestations){
                int randomIndex = (int) (Math.random() * amountOfWeeks);
                iterations++;

                if(prestationsOfTheDay.size() > this.amountOfWeeks - usedIndexes.size()){
                    return false;
                }

                if(!usedIndexes.contains(randomIndex)){

                    // Case: previous day is unassigned -> Random prestation
                    if(assigned[day - 1][randomIndex] == 0){
                        int randomPrestation = (int) (Math.random() * prestationsOfTheDay.size());
                        Prestation p = prestationsOfTheDay.get(randomPrestation);
                        assigned[day][randomIndex] = p.getOptimizationID();
                        prestationsOfTheDay.remove(p);
                        usedIndexes.add(randomIndex);
                        unUsedIndexesPerDay.get(day).remove(Integer.valueOf(randomIndex));
                    }

                    // Case: previous day is assigned -> Prestation closed to 14h after previous prestation
                    else {
                        int endTime = idToPrestation.get(assigned[day - 1][randomIndex]).getEndTime();
                        HashMap<Prestation, Integer> differences = new HashMap<>();

                        for(Prestation p: prestationsOfTheDay){
                            int beginTime = p.getStartTime();
                            int difference;

                            // De prestaties is diezelfde dag nog geëindigd.
                            if(endTime < 180){
                                difference = beginTime - endTime;
                            }

                            //Prestatie is vorige dag geëindigd
                            else{
                                difference = (1440 - endTime) + (beginTime);
                            }
                            differences.put(p, difference);
                        }

                        int currentDifference = 1440;
                        Prestation currentPrestation = null;

                        for(Prestation p : differences.keySet()){
                            if(differences.get(p) > 880 && differences.get(p) < currentDifference){
                                currentPrestation = p;
                                currentDifference = differences.get(p);
                            }
                        }

                        // Wanneer er na de prestatie geen andere prestatie ingepland kan worden, dan moet een rustdag gepland worden
                        if(currentPrestation == null){
                            usedIndexes.add(randomIndex);
                            continue;
                        }

                        assigned[day][randomIndex] = currentPrestation.getOptimizationID();
                        prestationsOfTheDay.remove(currentPrestation);
                        unUsedIndexesPerDay.get(day).remove(Integer.valueOf(randomIndex));
                        usedIndexes.add(randomIndex);

                    }
                    scheduledPrestations++;
                }
            }
        }

        // Alle nullen vervangen door Rust / Compentatie / Specifiek Compentatieverlof / Overgang / Reserve
        for(int day = 0; day < 7; day++){
            for(int index: unUsedIndexesPerDay.get(day)) {
                Prestation previousPrestation;
                int previousEndTime;
                if (day == 0) {
                    if(assigned[6][(index + this.amountOfWeeks - 1) % this.amountOfWeeks] > 5){
                        previousPrestation = idToPrestation.get(assigned[6][(index + this.amountOfWeeks - 1) % this.amountOfWeeks]);
                        previousEndTime = previousPrestation.getEndTime();
                    }else{
                        previousEndTime = 720; // 12u 's middags --> Zo is constraint sowieso voldaan
                    }
                } else {
                    if(assigned[day - 1][index] > 5){
                        previousPrestation = idToPrestation.get(assigned[day - 1][index]);
                        previousEndTime = previousPrestation.getEndTime();
                    }else{
                        previousEndTime = 720; // 12u 's middags --> Zo is constraint sowieso voldaan
                    }
                }

                Prestation nextPrestation;
                int nextBeginTime;
                if (day == 6) {
                    if(assigned[0][(index + 1) % this.amountOfWeeks] > 5){
                        nextPrestation = idToPrestation.get(assigned[0][(index + 1) % this.amountOfWeeks]);
                        nextBeginTime = nextPrestation.getStartTime();
                    }else{
                        nextBeginTime = 720; // 12u 's middags --> Zo is constraint sowieso voldaan
                    }
                } else {
                    if(assigned[day + 1][index] > 5){
                        nextPrestation = idToPrestation.get(assigned[day + 1][index]);
                        nextBeginTime = nextPrestation.getStartTime();
                    }else{
                        nextBeginTime = 720; // 12u 's middags --> Zo is constraint sowieso voldaan
                    }
                }

                // > 300 is telkens om de nachttijden te coveren: 300 = 5u 's morgens
                // Rust constraints: prestatie ervoor moet eindigen voor 20u en erna beginnen na 6u, ertussen zeker 36 uur
                if((previousEndTime > 300 && previousEndTime <= 1200) &&
                        (nextBeginTime >= 360) && (1440 - previousEndTime + 1440 + nextBeginTime > 2160)){
                    assigned[day][index] = 1;
                }

                // CX constraints: prestatie ervoor moet eindigen voor 23u en erna beginnen na 5u, ertussen zeker 34 uur
                else if((previousEndTime > 300 && previousEndTime <= 1380) &&
                        (nextBeginTime >= 300) && (1440 - previousEndTime + 1440 + nextBeginTime > 2040)){
                    assigned[day][index] = 2;
                }

                // CY constraints: prestatie ervoor moet eindigen voor 1u en erna beginnen na 3u, ertussen zeker 38 uur
                else if((previousEndTime > 300 || previousEndTime <= 60) &&
                        (nextBeginTime >= 180) && (1440 - previousEndTime + 1440 + nextBeginTime > 2280)){
                    assigned[day][index] = 3;
                }

                // Anders een overgangsdag
                else{
                    assigned[day][index] = 4;
                }

            }
        }

        return true;
    }



}
