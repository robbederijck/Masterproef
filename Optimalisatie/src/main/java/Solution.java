import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Solution {

    private NMBSFactory nmbsFactory;
    private int[][] assigned;

    private int amountOfWeeks;
    private ArrayList<Integer> storedParameters;

    // HARD CONSTRAINTS PARAMETERS

    private int amountOfMonthsWithNoFreeWeekend = Integer.MAX_VALUE;
    private int lessDaysRestPerMonth = Integer.MAX_VALUE;
    private int lessDaysRestPerTwoMonths = Integer.MAX_VALUE;
    private int moreThan8DaysBetweenRestdays = Integer.MAX_VALUE;
    private int moreThan7ConsecutivePrestationdays = Integer.MAX_VALUE;
    private int lessThen8FreeDaysInMonth = Integer.MAX_VALUE;
    private int moreThan5ConsecutiveNightPrestations = Integer.MAX_VALUE;
    private int lessThan14HoursBetweenPrestations = Integer.MAX_VALUE;
    private int penaltiesForTimeBetweenDaysAtHome = Integer.MAX_VALUE;
    private int prestationsEndingTooLateBeforeRestday = Integer.MAX_VALUE;
    private int prestationsBeginningTooEarlyAfterRestday = Integer.MAX_VALUE;
    private int prestationsEndingTooLateBeforeCompentationDay = Integer.MAX_VALUE;
    private int prestationsBeginningTooEarlyAfterCompentationDay = Integer.MAX_VALUE;
    private int prestationsEndingTooLateBeforeSpecificCompentationDay = Integer.MAX_VALUE;
    private int prestationsBeginningTooEarlyAfterSpecificCompentationDay = Integer.MAX_VALUE;
    private int moreThanOneSpecCompdayIn4Weeks = Integer.MAX_VALUE;
    private int moreThan13SpecCompdayPerYear = Integer.MAX_VALUE;
    private int moreThan52CompdayPerYear = Integer.MAX_VALUE;
    private int moreThan65RestdaysPerYear = Integer.MAX_VALUE;
    private int moreThen2ConsecutiveHomeDays = Integer.MAX_VALUE;
    private int moreThen3ConsecutiveHomeDays = Integer.MAX_VALUE;
    private int meanMinutesLessOrMoreThanMinMaxPerDay = Integer.MAX_VALUE;
    private int meanMinutesLessOrMoreThanMinMaxPerWeek = Integer.MAX_VALUE;

    private int totalFeasibilityScore = Integer.MAX_VALUE;


    // SOFT CONSTRAINTS PARAMETERS

    private int totalScore = Integer.MAX_VALUE;
    private int routineScore = Integer.MAX_VALUE;
    private int splitWeekendScore = Integer.MAX_VALUE;
    private int amountOfOA = Integer.MAX_VALUE;
    private int daysRCXCYScore = Integer.MAX_VALUE;
    private int consecutiveLessPrestationDaysScore = Integer.MAX_VALUE;
    private int consecutiveMorePrestationDaysScore = Integer.MAX_VALUE;
    private int moreThen2ConsecutiveHomeDaysScore = Integer.MAX_VALUE;

    private int humanization = Integer.MAX_VALUE;
    private int distributionFreeWeekends = Integer.MAX_VALUE;
    private int machineLearningScore = Integer.MAX_VALUE;


    // MOVES PARAMETERS

    private boolean deltaChanged = false;
    private boolean swappedAssignmentsInDay = false;
    private boolean changedHomeDay = false;
    private boolean swappedWeeks = false;
    private boolean swappedRandomAmountOfDays = false;

    private int weekSwapped1 = -1;
    private int weekSwapped2 = -1;
    private int daySwapped = -1;
    private int swappedValue1 = -1;
    private int swappedValue2 = -1;
    private int previousChangedHomeValue = -1;
    private int initialDayOfSwap = -1;

    private int[] swappedWeek1 = null;
    private int[] swappedWeek2 = null;

    public Solution(NMBSFactory nmbsFactory, int[][] assigned){
        this.nmbsFactory = nmbsFactory;
        this.assigned = assigned;

        this.amountOfWeeks = nmbsFactory.getAmountOfWeeks();
        this.storedParameters = new ArrayList<Integer>();
    }

    public Solution(Solution s){
        this.nmbsFactory = s.getNmbsFactory();

        this.assigned = new int[7][s.getAmountOfWeeks()];
        int[][] copyAssigned = s.getAssigned();
        for(int d = 0; d < 7; d++){
            for(int w = 0; w < s.getAmountOfWeeks(); w++){
                this.assigned[d][w] = copyAssigned[d][w];
            }
        }

        this.amountOfWeeks = s.getAmountOfWeeks();
        this.storedParameters = s.getStoredParameters();

        this.amountOfMonthsWithNoFreeWeekend = s.getAmountOfMonthsWithNoFreeWeekend();
        this.lessDaysRestPerMonth = s.getLessDaysRestPerMonth();
        this.lessDaysRestPerTwoMonths = s.getLessDaysRestPerTwoMonths();
        this.moreThan8DaysBetweenRestdays = s.getMoreThan8DaysBetweenRestdays();
        this.moreThan7ConsecutivePrestationdays = s.getMoreThan7ConsecutivePrestationdays();
        this.lessThen8FreeDaysInMonth = s.getLessThen8FreeDaysInMonth();
        this.moreThan5ConsecutiveNightPrestations = s.getMoreThan5ConsecutiveNightPrestations();
        this.lessThan14HoursBetweenPrestations = s.getLessThan14HoursBetweenPrestations();
        this.penaltiesForTimeBetweenDaysAtHome = s.getPenaltiesForTimeBetweenDaysAtHome();
        this.prestationsEndingTooLateBeforeRestday = s.getPrestationsEndingTooLateBeforeRestday();
        this.prestationsBeginningTooEarlyAfterRestday = s.getPrestationsBeginningTooEarlyAfterRestday();
        this.prestationsEndingTooLateBeforeCompentationDay = s.getPrestationsEndingTooLateBeforeCompentationDay();
        this.prestationsBeginningTooEarlyAfterCompentationDay = s.getPrestationsBeginningTooEarlyAfterCompentationDay();
        this.prestationsEndingTooLateBeforeSpecificCompentationDay = s.getPrestationsEndingTooLateBeforeSpecificCompentationDay();
        this.prestationsBeginningTooEarlyAfterSpecificCompentationDay = s.getPrestationsBeginningTooEarlyAfterSpecificCompentationDay();
        this.moreThanOneSpecCompdayIn4Weeks = s.getMoreThanOneSpecCompdayIn4Weeks();
        this.moreThan13SpecCompdayPerYear = s.getMoreThan13SpecCompdayPerYear();
        this.moreThan52CompdayPerYear = s.getMoreThan52CompdayPerYear();
        this.moreThan65RestdaysPerYear = s.getMoreThan65RestdaysPerYear();
        this.moreThen2ConsecutiveHomeDays = s.getMoreThen2ConsecutiveHomeDays();
        this.moreThen3ConsecutiveHomeDays = s.getMoreThen3ConsecutiveHomeDays();
        this.meanMinutesLessOrMoreThanMinMaxPerDay = s.getMeanMinutesLessOrMoreThanMinMaxPerDay();
        this.meanMinutesLessOrMoreThanMinMaxPerWeek = s.getMeanMinutesLessOrMoreThanMinMaxPerWeek();

        this.totalFeasibilityScore = s.getTotalFeasibilityScore();

        this.totalScore = s.getTotalScore();
        this.routineScore = s.getRoutineScore();
        this.splitWeekendScore = s.getSplitWeekendScore();
        this.amountOfOA = s.getAmountOfOA();
        this.daysRCXCYScore = s.getDaysRCXCYScore();
        this.consecutiveLessPrestationDaysScore = s.getConsecutiveLessPrestationDaysScore();
        this.consecutiveMorePrestationDaysScore = s.getConsecutiveMorePrestationDaysScore();
        this.moreThen2ConsecutiveHomeDaysScore = s.getMoreThen2ConsecutiveHomeDaysScore();
        this.humanization = s.getHumanization();
        this.machineLearningScore = s.getMachineLearningScore();
        this.distributionFreeWeekends = s.getDistributionFreeWeekends();
    }

    public int calculateDeltaCost(){
        assert(this.deltaChanged);
        assert(this.swappedAssignmentsInDay || this.changedHomeDay || this.swappedWeeks || this.swappedRandomAmountOfDays);

        if(this.swappedAssignmentsInDay){
            return this.calculateCost();
        }

        else if(this.changedHomeDay){
            return this.calculateCost();
        }

        else if(this.swappedWeeks){
            return this.calculateCost();
        }

        else if(this.swappedRandomAmountOfDays){
            return this.calculateCost();
        }

        assert(false);
        return Integer.MAX_VALUE;
    }

    public void doDelta(){
        assert(!deltaChanged);
        double randomValue = Math.random();

        //METHOD 1: SWAP 2 ASSIGNED VALUES
        if(randomValue < 0.45){
            boolean switched = false;
            int day = (int) (Math.random() * 7);

            while(!switched){
                int firstWeekIndex = (int) (Math.random() * this.amountOfWeeks);
                int secondWeekIndex = (int) (Math.random() * this.amountOfWeeks);

                if(firstWeekIndex != secondWeekIndex){
                    this.daySwapped = day;
                    this.weekSwapped1 = firstWeekIndex;
                    this.weekSwapped2 = secondWeekIndex;

                    this.swappedValue1 = this.assigned[day][firstWeekIndex];
                    this.swappedValue2 = this.assigned[day][secondWeekIndex];

                    // CHANGE VALUES
                    this.assigned[day][firstWeekIndex] = this.swappedValue2;
                    this.assigned[day][secondWeekIndex] = this.swappedValue1;

                    this.deltaChanged = true;
                    switched = true;
                }
            }
            this.swappedAssignmentsInDay = true;
        }

        //METHOD 2: SWAP 2 WEEKS
        else if(randomValue < 0.6){

            boolean switched = false;

            while(!switched){
                int firstWeekIndex = (int) (Math.random() * this.amountOfWeeks);
                int secondWeekIndex = (int) (Math.random() * this.amountOfWeeks);

                if(firstWeekIndex != secondWeekIndex){
                    this.weekSwapped1 = firstWeekIndex;
                    this.weekSwapped2 = secondWeekIndex;

                    this.swappedWeek1 = new int[7];
                    this.swappedWeek2 = new int[7];

                    for(int i = 0; i < 7; i++){
                        this.swappedWeek1[i] = this.assigned[i][this.weekSwapped1];
                        this.swappedWeek2[i] = this.assigned[i][this.weekSwapped2];

                        int temp = this.assigned[i][this.weekSwapped1];

                        this.assigned[i][this.weekSwapped1] = this.assigned[i][this.weekSwapped2];
                        this.assigned[i][this.weekSwapped2] = temp;
                    }

                    this.deltaChanged = true;
                    switched = true;
                }
            }

            this.swappedWeeks = true;
        }

        //METHOD 3: CHANGE HOME-DAY
        else if(randomValue < 0.90){
            ArrayList<int[]> list = new ArrayList<>();
            for(int d = 0; d < 7; d++){
                for(int w = 0; w < this.amountOfWeeks; w++){
                    if(this.assigned[d][w] <= 5){
                        int[] arr = new int[2];
                        arr[0] = d;
                        arr[1] = w;
                        list.add(arr);
                    }
                }
            }

            assert(list.size() > 1);

            int randomIndex = (int) (Math.random() * list.size());
            int[] dayAndWeek = list.get(randomIndex);

            int value = this.assigned[dayAndWeek[0]][dayAndWeek[1]];
            int newValue = value;
            while(value == newValue){
                newValue = (int) Math.ceil(Math.random() * 5);
            }

            this.previousChangedHomeValue = value;
            this.daySwapped = dayAndWeek[0];
            this.weekSwapped1 = dayAndWeek[1];
            this.assigned[dayAndWeek[0]][dayAndWeek[1]] = newValue;

            this.changedHomeDay = true;
        }

        //METHOD 4: Change random amount of days
        else{
            boolean switched = false;
            int amountOfDays = (int) Math.ceil(Math.random() * 4) + 1;
            int initialDay = (int) Math.ceil(Math.random() * (8 - amountOfDays)) - 1;

            while(!switched){

                int firstWeekIndex = (int) (Math.random() * this.amountOfWeeks);
                int secondWeekIndex = (int) (Math.random() * this.amountOfWeeks);

                if(firstWeekIndex != secondWeekIndex){
                    this.weekSwapped1 = firstWeekIndex;
                    this.weekSwapped2 = secondWeekIndex;

                    this.swappedWeek1 = new int[amountOfDays];
                    this.swappedWeek2 = new int[amountOfDays];

                    for(int i = 0; i < amountOfDays; i++){
                        this.swappedWeek1[i] = this.assigned[i + initialDay][this.weekSwapped1];
                        this.swappedWeek2[i] = this.assigned[i + initialDay][this.weekSwapped2];

                        int temp = this.assigned[i + initialDay][this.weekSwapped1];

                        this.assigned[i + initialDay][this.weekSwapped1] = this.assigned[i + initialDay][this.weekSwapped2];
                        this.assigned[i + initialDay][this.weekSwapped2] = temp;
                    }

                    this.deltaChanged = true;
                    switched = true;
                }
            }

            this.swappedRandomAmountOfDays = true;
            this.initialDayOfSwap = initialDay;
        }

        this.deltaChanged = true;
    }

    public void commitDelta(){
        assert(this.deltaChanged);

        this.deltaChanged = false;
        this.swappedAssignmentsInDay = false;
        this.changedHomeDay = false;
        this.swappedWeeks = false;
        this.swappedRandomAmountOfDays = false;

        this.weekSwapped1 = -1;
        this.weekSwapped2 = -1;
        this.daySwapped = -1;
        this.swappedValue1 = -1;
        this.swappedValue2 = -1;
        this.previousChangedHomeValue = -1;
        this.initialDayOfSwap = -1;

        this.swappedWeek2 = null;
        this.swappedWeek1 = null;
    }

    public void revertDelta(){
        assert(this.deltaChanged);

        if(this.swappedAssignmentsInDay){
            this.assigned[this.daySwapped][this.weekSwapped1] = this.swappedValue1;
            this.assigned[this.daySwapped][this.weekSwapped2] = this.swappedValue2;
        }

        else if(this.changedHomeDay){
            this.assigned[this.daySwapped][this.weekSwapped1] = this.previousChangedHomeValue;
        }

        else if(this.swappedWeeks){
            for(int i = 0; i < 7; i++){
                this.assigned[i][this.weekSwapped1] = this.swappedWeek1[i];
                this.assigned[i][this.weekSwapped2] = this.swappedWeek2[i];
            }
        }

        else if(this.swappedRandomAmountOfDays){
            for(int i = 0; i < this.swappedWeek1.length; i++){
                this.assigned[this.initialDayOfSwap + i][this.weekSwapped1] = this.swappedWeek1[i];
                this.assigned[this.initialDayOfSwap + i][this.weekSwapped2] = this.swappedWeek2[i];
            }
        }

        this.calculateDeltaCost();

        this.deltaChanged = false;
        this.swappedAssignmentsInDay = false;
        this.changedHomeDay = false;
        this.swappedWeeks = false;
        this.swappedRandomAmountOfDays = false;

        this.weekSwapped1 = -1;
        this.weekSwapped2 = -1;
        this.daySwapped = -1;
        this.swappedValue1 = -1;
        this.swappedValue2 = -1;
        this.previousChangedHomeValue = -1;
        this.initialDayOfSwap = -1;

        this.swappedWeek2 = null;
        this.swappedWeek1 = null;
    }

    public boolean isFeasible(){
        if(this.totalFeasibilityScore > 0){
            return false;
        }else{
            return true;
        }
    }

    public int calculateCost(){
        this.setFeasibilityScore();
        this.setSoftScore();
        assert(this.totalFeasibilityScore < Integer.MAX_VALUE);

        /*
        if(this.isFeasible()){
            try{
                NMBSFileWriter.writeOutputFile(this.assigned, this.nmbsFactory, "finalsolution.xml");
            }catch (Exception e){
                e.printStackTrace();
            }
            int returnValue = this.certainTimeBetweenHomeDays();
            //System.out.println("STOP");
        }

         */

        /*
        int MLscore = 0;
        if(this.machineLearningScore < 30){ // 70% accuracy
            if(!(this.machineLearningScore > 20)){ // 80% accuracy
                MLscore = 20 - this.machineLearningScore;
            }
        }else{
            MLscore = this.machineLearningScore - 30;
        }
         */

        return this.totalFeasibilityScore * nmbsFactory.INFEASIBILITY_WEIGHT() +
                this.routineScore * nmbsFactory.ROUTINE_WEIGHT() +
                this.amountOfOA * nmbsFactory.OA_WEIGHT() +
                Math.abs(123 - this.daysRCXCYScore) * nmbsFactory.RCXCY_WEIGHT() +
                this.consecutiveLessPrestationDaysScore * nmbsFactory.CONSPRES_LESS_WEIGHT() +
                this.consecutiveMorePrestationDaysScore * nmbsFactory.CONSPRES_MORE_WEIGHT() +
                this.splitWeekendScore * nmbsFactory.SPLITWEEKEND_WEIGHT() +
                this.moreThen2ConsecutiveHomeDaysScore * nmbsFactory.CONSHOME_WEIGHT() +
                this.humanization * nmbsFactory.HUMANIZATION_WEIGHT() +
                this.distributionFreeWeekends * nmbsFactory.DISTRIBUTION_WEEKENDS_WEIGHT() +
                this.machineLearningScore; //The weight is calculated intern
                //MLscore * 4;
    }

    public void printSoftUpdate(){
        System.out.println("# OA: " + this.amountOfOA);
        System.out.println("# splitted weekends: " + this.splitWeekendScore);
        System.out.println("# Difference between 122 in RC / CX / CY days: " + this.daysRCXCYScore);
        System.out.println("# Less than 5 consecutive prestation-days: " + this.consecutiveLessPrestationDaysScore);
        System.out.println("# More than 5 consecutive prestation-days: " + this.consecutiveMorePrestationDaysScore);
        System.out.println("# More then 2 consecutive homedays: " + this.moreThen2ConsecutiveHomeDaysScore);
        System.out.println("# Routine: " + this.routineScore);
        System.out.println("# Humanization: " + this.humanization);
        System.out.println("# Distribution Free Weekends: " + this.distributionFreeWeekends);
        System.out.println("# Machine Learning Score: " + (100 - this.machineLearningScore)+"%");
    }

    public void printFeasibilityUpdate(){
        System.out.println("# months with no free weekends: " + this.amountOfMonthsWithNoFreeWeekend);
        System.out.println("# less days rest per month: " + this.lessDaysRestPerMonth);
        System.out.println("# less than ten restdays per two months: " + this.lessDaysRestPerTwoMonths);
        System.out.println("# more than 8 days between restdays: " + this.moreThan8DaysBetweenRestdays);
        System.out.println("# more than 7 consecutive prestation-days: " + this.moreThan7ConsecutivePrestationdays);
        System.out.println("# less than 8 free days in month: " + this.lessThen8FreeDaysInMonth);
        System.out.println("# more than 5 consecutive night-prestations: " + this.moreThan5ConsecutiveNightPrestations);
        System.out.println("# less than 14 hours between prestations: " + this.lessThan14HoursBetweenPrestations);
        System.out.println("# penalty's for time between days @ home: " + this.penaltiesForTimeBetweenDaysAtHome);
        System.out.println("# prestations ending too late before restday: " + this.prestationsEndingTooLateBeforeRestday);
        System.out.println("# prestations beginning too early after restday: " + this.prestationsBeginningTooEarlyAfterRestday);
        System.out.println("# prestations ending too late before compentationday: " + this.prestationsEndingTooLateBeforeCompentationDay);
        System.out.println("# prestations beginning too early after compentationday: " + this.prestationsBeginningTooEarlyAfterCompentationDay);
        System.out.println("# prestations ending too late before specific compentationday: " + this.prestationsEndingTooLateBeforeSpecificCompentationDay);
        System.out.println("# prestation beginning too early after specific compentationday: " + this.prestationsBeginningTooEarlyAfterSpecificCompentationDay);
        System.out.println("# more than one spec. compday in 4 weeks: " + this.moreThanOneSpecCompdayIn4Weeks);
        System.out.println("# more than 13 spec. compdays per year: " + this.moreThan13SpecCompdayPerYear);
        System.out.println("# more than 52 compentationdays per year: " + this.moreThan52CompdayPerYear);
        System.out.println("# more than 65 restdays per year: " + this.moreThan65RestdaysPerYear);
        System.out.println("# more than 2 consecutive home days: " + this.moreThen2ConsecutiveHomeDays);
        System.out.println("# more than 3 consesecutive home days: " + this.moreThen3ConsecutiveHomeDays);
        System.out.println("# minutes less or more than min/max allowed per day: " + this.meanMinutesLessOrMoreThanMinMaxPerDay);
        System.out.println("# minutes less or more than min/max allowed per week: " + this.meanMinutesLessOrMoreThanMinMaxPerWeek);
        System.out.println("--- TOTAL: "+ this.totalFeasibilityScore +" ---" + "\n");
    }

    public void setSoftScore(){
        assert(this.isAllAssigned());

        calculateAmountOfOA();
        calculateAmountOfSplittedWeekends();
        calculateRCXCY();
        calculateScoreMoreThen2ConsecutiveHomeDays();
        calculateScoreConsecutivePrestationDays();
        calculateRoutineScore();
        calculateHumanization();
        //calculateSimmularity();

    }

    public void setFeasibilityScore(){
        assert(this.isAllAssigned());

        int freeWeekendInMonth = this.freeWeekendInMonth();
        this.amountOfMonthsWithNoFreeWeekend = freeWeekendInMonth;

        int fourDaysRestPerMonth = this.fourDaysRestPerMonth();
        this.lessDaysRestPerMonth = fourDaysRestPerMonth;

        int tenDaysRestPerTwoMonths = this.tenRestDaysInTwoMonths();
        this.lessDaysRestPerTwoMonths = tenDaysRestPerTwoMonths;

        int maxEightDaysBetweenRestDays = this.maxEightDaysBetweenRestDays();
        this.moreThan8DaysBetweenRestdays = maxEightDaysBetweenRestDays;

        int maxSevenConsecutivePrestationDays = this.maxSevenConsecutivePrestationDays();
        this.moreThan7ConsecutivePrestationdays = maxSevenConsecutivePrestationDays;

        int eightFreeDaysInEvereyFourWeeks = this.eightFreeDaysInEveryFourWeeks();
        this.lessThen8FreeDaysInMonth = eightFreeDaysInEvereyFourWeeks;

        int maxFiveNightConsecutivePrestations = this.maxFiveConsecutiveNightPrestations();
        this.moreThan5ConsecutiveNightPrestations = maxFiveNightConsecutivePrestations;

        int fourTeenHoursBetweenPrestations = this.fourteenHoursBetweenPrestations();
        this.lessThan14HoursBetweenPrestations = fourTeenHoursBetweenPrestations;

        int timeBetweenHomeDays = this.certainTimeBetweenHomeDays();
        this.penaltiesForTimeBetweenDaysAtHome = timeBetweenHomeDays;

        int prestationEndHourBeforeRestDay = this.prestationEndHourBeforeRestday();
        this.prestationsEndingTooLateBeforeRestday = prestationEndHourBeforeRestDay;

        int prestationBeginHourAfterRestDay = this.prestationBeginHourAfterRestDay();
        this.prestationsBeginningTooEarlyAfterRestday = prestationBeginHourAfterRestDay;

        int prestationEndHourBeforeCompentationDay = this.prestationEndHourBeforeCompentationDay();
        this.prestationsEndingTooLateBeforeCompentationDay = prestationEndHourBeforeCompentationDay;

        int prestationBeginHourAfterCompentationDay = this.prestationBeginHourAfterCompentationDay();
        this.prestationsBeginningTooEarlyAfterCompentationDay = prestationBeginHourAfterCompentationDay;

        int prestationEndHourBeforeSpecificCompDay = this.prestationEndHourBeforeSpecificCompDay();
        this.prestationsEndingTooLateBeforeSpecificCompentationDay = prestationEndHourBeforeSpecificCompDay;

        int prestationBeginHourAfterSpecificCompDay = this.prestationBeginHourAfterSpecificCompDay();
        this.prestationsBeginningTooEarlyAfterSpecificCompentationDay = prestationBeginHourAfterSpecificCompDay;

        int onlyOneSpecDayInFourWeeks = this.onlyOneSpecCompDayInFourWeeks();
        this.moreThanOneSpecCompdayIn4Weeks = onlyOneSpecDayInFourWeeks;

        int max13SpecCompDaysInYear = this.max13SpecCompDaysInYear();
        this.moreThan13SpecCompdayPerYear = max13SpecCompDaysInYear;

        int max52CompDaysInYear = this.max52CompDaysInYear();
        this.moreThan52CompdayPerYear = max52CompDaysInYear;

        int max65RestDaysInYear = this.max65RestDaysInYear();
        this.moreThan65RestdaysPerYear = max65RestDaysInYear;

        int moreThanTwoConsecutiveHomeDays = this.moreThanTwoConsecutiveHomeDays();
        this.moreThen2ConsecutiveHomeDays = moreThanTwoConsecutiveHomeDays;

        int moreThanThreeConsecutiveHomeDays = this.moreThanThreeConsecutiveHomeDays();
        this.moreThen3ConsecutiveHomeDays = moreThanThreeConsecutiveHomeDays;

        int meanHoursLessOrMoreThanMinMaxPerDay = this.calculateMeanHoursLessOrMoreThanMinMaxPerDay();
        this.meanMinutesLessOrMoreThanMinMaxPerDay = meanHoursLessOrMoreThanMinMaxPerDay;

        int meanHoursLessOrMoreThanMinMaxPerWeek = this.calculateMeanHoursLessOrMoreThanMinMaxPerWeek();
        this.meanMinutesLessOrMoreThanMinMaxPerWeek = meanHoursLessOrMoreThanMinMaxPerWeek;

        int total = freeWeekendInMonth + fourDaysRestPerMonth + tenDaysRestPerTwoMonths
                + maxEightDaysBetweenRestDays + maxSevenConsecutivePrestationDays
                + eightFreeDaysInEvereyFourWeeks + maxFiveNightConsecutivePrestations
                + fourTeenHoursBetweenPrestations + timeBetweenHomeDays + prestationEndHourBeforeRestDay
                + prestationBeginHourAfterRestDay + prestationEndHourBeforeCompentationDay
                + prestationBeginHourAfterCompentationDay + prestationEndHourBeforeSpecificCompDay
                + prestationEndHourBeforeSpecificCompDay + prestationBeginHourAfterSpecificCompDay
                + onlyOneSpecDayInFourWeeks + max13SpecCompDaysInYear + max52CompDaysInYear
                + max65RestDaysInYear + moreThanTwoConsecutiveHomeDays + moreThanThreeConsecutiveHomeDays
                + meanHoursLessOrMoreThanMinMaxPerDay + meanHoursLessOrMoreThanMinMaxPerWeek + reserveConstraint()
                + forbiddenCombination() + prestationDurationAfterCY();

        this.totalFeasibilityScore = total;

    }


    // ---------------------------------------------------------------
    //                      HARD CONSTRAINTS
    // ---------------------------------------------------------------

    // Alles is toegewezen --> Geen nullen meer (= unassigned)
    public boolean isAllAssigned(){
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < this.amountOfWeeks; j++){
                if(assigned[i][j] == 0) return false;
            }
        }
        return true;
    }

    // Om de 4 weken moet er zeker een volledig vrij weekend in zitten
    // Returns int -> hoeveel maanden geen vrij weekend bevatten
    public int freeWeekendInMonth(){
        int returnValue = 0;
        for(int week = 0; week < this.amountOfWeeks; week++){
            int count = 0;
            for(int i = week; i < (week + 4); i++){
                if(assigned[5][i % this.amountOfWeeks] <= 2 && assigned[6][i % this.amountOfWeeks] <= 2){
                    count++;
                }
            }
            if(count == 0) returnValue++;
        }
        return returnValue;
    }

    // Per maand moet er minstens 4 dagen rust gegeven worden
    // Returns int -> Berekent gemiddelde per week, anders returnt het de afwijking met 4
    public int fourDaysRestPerMonth(){
        int returnValue = 0;
        int count = 0;
        for(int week = 0; week < this.amountOfWeeks; week++) {
            for(int d = 0; d < 7; d++){
                for (int i = week; i < (week + 4); i++) {
                    if(assigned[d][i % this.amountOfWeeks] == 1){
                        count++;
                    }
                }
            }
        }
        double mean = count / (double) this.amountOfWeeks;
        if(mean < 4) return (int) Math.ceil(4 - mean);
        return 0;
    }

    // Er moeten minstens 10 rustdagen ingepland worden binnen een periode van 2 maanden
    // Returns int -> Berekent gemiddelde per 2 maanden, anders returnt het de afwijking met 8
    public int tenRestDaysInTwoMonths(){
        int count = 0;
        for(int week = 0; week < this.amountOfWeeks; week++) {
            for(int d = 0; d < 7; d++){
                for (int i = week; i < (week + 8); i++) {
                    if(assigned[d][i % this.amountOfWeeks] == 1){
                        count++;
                    }
                }
            }
        }
        double mean = count / (double) this.amountOfWeeks;
        if(mean < 10) return (int) Math.ceil(10 - mean);
        return 0;
    }

    // Tussen twee rustdagen mag er een maximale tussenpoos van 8 dagen zitten
    // SLIDING WINDOW van 9 waar tenminste 1 rustdag in moet zitten
    // Returns int -> hoeveel keer dit niet het geval is
    public int maxEightDaysBetweenRestDays(){
        int returnValue = 0;
        for(int w = 0; w < this.amountOfWeeks; w++) {
            for (int d = 0; d < 7; d++) {
                int beginDay = d;
                int days = 0;
                int count = 0;
                for(int week = w; week < w + Math.ceil((d+9)/7.0); week++){
                    for(int i = beginDay; i < 7; i++){
                        if(this.assigned[i][week % this.amountOfWeeks] == 1){
                            count++;
                        }
                        if(days == 8){
                            break;
                        }else{
                            days++;
                        }
                    }
                    beginDay = 0;
                }
                if(count < 1) returnValue++;
            }
        }
        return returnValue * nmbsFactory.EXTRA_INFEASIBILITY_PENALTY();
    }

    // Het maximumaantal opeenvolgende prestaties zonder vrije dagen tussen is 7
    // SLIDING WINDOW 8 waar prestaties worden geteld (<= 7)
    // Returns int -> hoeveel keer dit het geval is
    public int maxSevenConsecutivePrestationDays(){
        int returnValue = 0;
        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                int beginDay = d;
                int days = 0;
                int count = 0;
                for(int week = w; week < w + Math.ceil((d+8)/7.0); week++){
                    for(int i = beginDay; i < 7; i++){
                        if(this.assigned[i][week % this.amountOfWeeks] >= 5){
                            count++;
                        }
                        if(days == 7){
                            break;
                        }else{
                            days++;
                        }
                    }
                    beginDay = 0;
                }
                if(count == 8) returnValue++;

            }
        }
        return returnValue * nmbsFactory.EXTRA_INFEASIBILITY_PENALTY();
    }

    // Per 4 weken moet er minstens 8 vrije dagen voorkomen, voor élke opeenvolging van 4 weken.
    // Returns int -> hoeveel keer dit het geval is
    // Gewicht: 8 - aantal rustdagen
    public int eightFreeDaysInEveryFourWeeks(){
        int returnValue = 0;
        for(int week = 0; week < this.amountOfWeeks; week++) {
            int count = 0;
            for (int i = week; i < (week + 4); i++) {
                for (int day = 0; day < 7; day++) {
                    if (assigned[day][i % this.amountOfWeeks] <= 3) {
                        count++;
                    }
                }
            }
            if (count < 8) returnValue += (8 - count);
        }
        return returnValue;
    }

    // Het maximumaantal opeenvolgende nachtprestaties is 5. (Nachtprestatie is eentje die eindigt na 2u00)
    // SLIDING WINDOW 6 waar nachtprestaties worden geteld (< 6)
    // Returns int -> hoeveel keer dit niet het geval is
    public int maxFiveConsecutiveNightPrestations(){
        int returnValue = 0;
        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                int beginDay = d;
                int days = 0;
                int count = 0;
                for(int week = w; week < w + Math.ceil((d+6)/7.0); week++){
                    for(int i = beginDay; i < 7; i++){
                        if((this.assigned[i][week % this.amountOfWeeks] == 5) || (this.assigned[i][week % this.amountOfWeeks] > 5 && nmbsFactory.getPrestation(assigned[i][week % this.amountOfWeeks]).getEndTime() > 120 && nmbsFactory.getPrestation(assigned[i][week % this.amountOfWeeks]).getEndTime() < 480)){
                            count++;
                        }
                        if(days == 5){
                            break;
                        }else{
                            days++;
                        }
                    }
                    beginDay = 0;
                }
                if(count == 6) returnValue++;
            }
        }
        return returnValue;
    }

    // Tussen twee werkdagen moet er minstens 14u (840min) zitten.
    // Returns int -> Hoeveel keer dit niet het geval is
    public int fourteenHoursBetweenPrestations(){
        int returnValue = 0;
        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                int deltaWeek = 0;
                if(d == 6) deltaWeek = 1;
                if(this.assigned[d][w] > 5
                    && this.assigned[(d+1) % 7][(w + deltaWeek) % this.amountOfWeeks] > 5){
                    Prestation firstPrestation = nmbsFactory.getPrestation(this.assigned[d][w]);
                    Prestation secondPrestation = nmbsFactory.getPrestation(this.assigned[(d+1) % 7][(w + deltaWeek) % this.amountOfWeeks]);

                    int delta;
                    if(firstPrestation.getEndTime() < 480){
                        delta = secondPrestation.getStartTime() - firstPrestation.getEndTime();
                    }else{
                        delta = 1440 - firstPrestation.getEndTime() + secondPrestation.getStartTime();
                    }

                    if(delta < 840) returnValue++;
                }
            }
        }
        return returnValue * nmbsFactory.EXTRA_INFEASIBILITY_PENALTY();
    }

    // Meerdere constraints in elkaar: uren tussen prestaties wanneer er rustdag(en) tussen zit(ten).
    // Returns int -> hoeveel keer dit overtreden wordt
    public int certainTimeBetweenHomeDays(){
        int returnValue = 0;
        int hoursR = 2160; // 36u
        int hoursCX = 2040; // 34u
        int hoursCY = 2280; // 38u

        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                int deltaWeek = 0;
                if(d == 0) deltaWeek = 1;

                // CX / CY or R day
                // Day before : prestation
                if(this.assigned[d][w] <= 3 && this.assigned[(d + 7 - 1) % 7][(w + this.amountOfWeeks - deltaWeek) % this.amountOfWeeks] > 5){
                    Prestation pBefore = nmbsFactory.getPrestation(this.assigned[(d + 7 - 1) % 7][(w + this.amountOfWeeks - deltaWeek) % this.amountOfWeeks]);
                    int countDays = 0;
                    int day = d;
                    deltaWeek = 0;

                    // Begint te tellen bij de dag die je inspecteert, is dus sowieso 1!
                    while(this.assigned[day % 7][(w + deltaWeek) % this.amountOfWeeks] <= 3){
                        countDays++;

                        if(day == 6) deltaWeek++;
                        day = (day + 1) % 7;
                    }

                    deltaWeek = (int) Math.ceil((d + countDays + 1) / 7.0) - 1;

                    if(this.assigned[(d + countDays) % 7][(w + deltaWeek) % this.amountOfWeeks] > 3 && this.assigned[(d + countDays) % 7][(w + deltaWeek) % this.amountOfWeeks] <= 5){
                        // do nothing
                    }else {
                        try {
                            assert (this.assigned[(d + countDays) % 7][(w + deltaWeek) % this.amountOfWeeks] > 5);
                        } catch (AssertionError ae) {
                            System.out.println("d: " + d);
                            System.out.println("w: " + w);
                            System.out.println("countDays: " + countDays);
                            System.out.println("deltaWeek: " + deltaWeek);
                            System.out.println("assigned[][]: " + this.assigned[(d + countDays) % 7][(w + deltaWeek) % this.amountOfWeeks]);
                            ae.printStackTrace();
                        }

                        Prestation pAfter = nmbsFactory.getPrestation(this.assigned[(d + countDays) % 7][(w + deltaWeek) % this.amountOfWeeks]);

                        int firstRestrictiveHours = 0;
                        switch (this.assigned[d][w]) {
                            case 1:
                                firstRestrictiveHours = hoursR;
                                break;
                            case 2:
                                firstRestrictiveHours = hoursCX;
                                break;
                            case 3:
                                firstRestrictiveHours = hoursCY;
                                break;
                        }

                        deltaWeek = 0;
                        if ((d + countDays - 1) % 7 < d) deltaWeek = 1;
                        int lastRestrictiveHours = 0;
                        switch (this.assigned[(d + countDays - 1) % 7][(w + deltaWeek) % this.amountOfWeeks]) {
                            case 1:
                                lastRestrictiveHours = hoursR;
                                break;
                            case 2:
                                lastRestrictiveHours = hoursCX;
                                break;
                            case 3:
                                lastRestrictiveHours = hoursCY;
                                break;
                        }

                        int longestRestrictedHours = Math.max(firstRestrictiveHours, lastRestrictiveHours);
                        int deltaTime = longestRestrictedHours;
                        int deltaDaysInHours = 1440;

                        for (int i = 0; i < (countDays - 1); i++) {
                            deltaTime += 1440;
                            deltaDaysInHours += 1440;
                        }


                        // Kijken of prestatie na 24u stopt of niet
                        if (pBefore.getStartTime() > pBefore.getEndTime()) {
                            if (deltaDaysInHours - pBefore.getEndTime() + pAfter.getStartTime() < deltaTime) {
                                returnValue++;
                            }
                        } else {
                            int hoursTillMidnight = 1440 - pBefore.getEndTime();
                            if (hoursTillMidnight + pAfter.getStartTime() + deltaDaysInHours < deltaTime) {
                                returnValue++;
                            }
                        }

                    }

                    deltaWeek = (int) Math.ceil((d + countDays) / 6.0) - 1;
                    if(((d + countDays - 1) % 7) != 6){
                        w += deltaWeek; // loop telt er zelf een week bij
                    }else{
                        if(w + deltaWeek >= this.amountOfWeeks) break;
                    }
                    d = (d + countDays - 1) % 7; // -1 omdat loop hierna er eentje zal bijtellen
                    if(w >= this.amountOfWeeks) break;

                }
            }
        }
        return returnValue  * nmbsFactory.EXTRA_INFEASIBILITY_PENALTY();
    }

    // Wanneer een rustdag ingepland wordt moet prestatie ervoor eindigen voor 20u (1200min na middernacht)
    // Returns int -> hoeveer keer deze regel overtreden is
    public int prestationEndHourBeforeRestday(){
        int returnValue = 0;
        for(int w = 0; w < this.amountOfWeeks; w++) {
            for (int d = 0; d < 7; d++) {
                int deltaWeek = 0;
                if((d + 7 - 1) % 7 > d) deltaWeek = 1;
                if(this.assigned[d][w] == 1 && this.assigned[(d + 7 - 1) % 7][(w + this.amountOfWeeks - deltaWeek) % this.amountOfWeeks] > 5){
                    Prestation p = nmbsFactory.getPrestation(this.assigned[(d + 7 - 1) % 7][(w + this.amountOfWeeks - deltaWeek) % this.amountOfWeeks]);

                    if(p.getEndTime() > 1200 || p.getEndTime() < 480){
                        returnValue++;
                    }
                }
            }
        }
        return returnValue  * nmbsFactory.EXTRA_INFEASIBILITY_PENALTY();
    }

    // Wanneer een prestatie ingepland wordt op de dag na een rustdag, mag die pas beginnen na 6u00 (360min na middernacht)
    // Returns int -> hoeveel keer deze regel overtreden is
    public int prestationBeginHourAfterRestDay(){
        int returnValue = 0;
        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                int deltaWeek = 0;
                if((d + 1) % 7 < d) deltaWeek = 1;
                if(this.assigned[d][w] == 1 && this.assigned[(d + 1) % 7][(w + deltaWeek) % this.amountOfWeeks] > 5){
                    Prestation p = nmbsFactory.getPrestation(this.assigned[(d + 1) % 7][(w + deltaWeek) % this.amountOfWeeks]);

                    if(p.getStartTime() < 360){
                        returnValue++;
                    }
                }
            }
        }
        return returnValue  * nmbsFactory.EXTRA_INFEASIBILITY_PENALTY();
    }

    // Wanneer een compensatiedag ingepland wordt dan moet de prestatie ervoor zeker voor 23u eindigen (1390min)
    // Returns int -> Hoeveel keer dit niet het geval is
    public int prestationEndHourBeforeCompentationDay(){
        int returnValue = 0;
        for(int w = 0; w < this.amountOfWeeks; w++) {
            for (int d = 0; d < 7; d++) {
                int deltaWeek = 0;
                if((d + 7 - 1) % 7 > d) deltaWeek = 1;
                if(this.assigned[d][w] == 2 && this.assigned[(d + 7 - 1) % 7][(w + this.amountOfWeeks - deltaWeek) % this.amountOfWeeks] > 5){
                    Prestation p = nmbsFactory.getPrestation(this.assigned[(d + 7 - 1) % 7][(w + this.amountOfWeeks - deltaWeek) % this.amountOfWeeks]);

                    if(p.getEndTime() > 1380 || p.getEndTime() < 480){
                        returnValue++;
                    }
                }
            }
        }
        return returnValue  * nmbsFactory.EXTRA_INFEASIBILITY_PENALTY();
    }


    public int prestationBeginHourAfterCompentationDay(){
        int returnValue = 0;
        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                int deltaWeek = 0;
                if((d + 1) % 7 < d) deltaWeek = 1;
                if(this.assigned[d][w] == 2 && this.assigned[(d + 1) % 7][(w + deltaWeek) % this.amountOfWeeks] > 5){
                    Prestation p = nmbsFactory.getPrestation(this.assigned[(d + 1) % 7][(w + deltaWeek) % this.amountOfWeeks]);

                    if(p.getStartTime() < 300){
                        returnValue++;
                    }
                }
            }
        }
        return returnValue  * nmbsFactory.EXTRA_INFEASIBILITY_PENALTY();
    }

    // Wanneer een specifieke compensatieverlofdag ingepland wordt dan moet de prestatie ervoor zeker eindigen voor 1u
    // Returns int -> Hoeveel keer dit niet het geval is
    public int prestationEndHourBeforeSpecificCompDay(){
        int returnValue = 0;
        for(int w = 0; w < this.amountOfWeeks; w++) {
            for (int d = 0; d < 7; d++) {
                int deltaWeek = 0;
                if((d + 7 - 1) % 7 > d) deltaWeek = 1;
                if(this.assigned[d][w] == 3 && this.assigned[(d + 7 - 1) % 7][(w + this.amountOfWeeks - deltaWeek) % this.amountOfWeeks] > 5){
                    Prestation p = nmbsFactory.getPrestation(this.assigned[(d + 7 - 1) % 7][(w + this.amountOfWeeks - deltaWeek) % this.amountOfWeeks]);

                    if(p.getEndTime() > 60 || p.getEndTime() < 480){
                        returnValue++;
                    }
                }
            }
        }
        return returnValue  * nmbsFactory.EXTRA_INFEASIBILITY_PENALTY();
    }


    // Wanneer een compensatiedag ingepland wordt dan mag de prestatie erna pas beginnen na 3u (180min na middernacht)
    // Returns int -> Hoeveel keer dit niet het geval is
    public int prestationBeginHourAfterSpecificCompDay(){
        int returnValue = 0;
        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                int deltaWeek = 0;
                if((d + 1) % 7 < d) deltaWeek = 1;
                if(this.assigned[d][w] == 3 && this.assigned[(d + 1) % 7][(w + deltaWeek) % this.amountOfWeeks] > 5){
                    Prestation p = nmbsFactory.getPrestation(this.assigned[(d + 1) % 7][(w + deltaWeek) % this.amountOfWeeks]);

                    if(p.getStartTime() < 180){
                        returnValue++;
                    }
                }
            }
        }
        return returnValue  * nmbsFactory.EXTRA_INFEASIBILITY_PENALTY();
    }

    // Spec. Compensatieverlofdagen mogen slechts 1 keer per periode van 4 kalenderweken ingepland worden
    // Returns int -> Elke 4 weken worden gecontroleerd, er wordt altijd 1 penalty gerekend per dag dat het erover zit
    public int onlyOneSpecCompDayInFourWeeks(){
        int returnValue = 0;
        for(int w = 0; w < this.amountOfWeeks; w++){
            int count = 0;
            for(int week = w; week < (w + 4); week++) {
                for (int d = 0; d < 7; d++){
                    if(this.assigned[d][week % this.amountOfWeeks] == 3){
                        count++;
                    }
                }
            }
            if(count > 1) returnValue += (count - 1);
        }
        return returnValue  * nmbsFactory.EXTRA_INFEASIBILITY_PENALTY();
    }

    // Maximaal 13 spec. compensatiedagen ingepland per jaar.
    // Returns int -> Verschil met 13 (gemiddelde over alle mogelijke jaren)
    public int max13SpecCompDaysInYear(){
        int returnValue = 0;
        int count = 0;
        for(int week = 0; week < 52; week++){
            for(int d = 0; d < 7; d++){
                if(this.assigned[d][week % this.amountOfWeeks] == 3){
                    count++;
                }
            }
        }
        if(count > 13) returnValue += (count - 13);

        return returnValue  * nmbsFactory.EXTRA_INFEASIBILITY_PENALTY();
    }

    // Maximaal 52 compensatiedagen ingepland per jaar.
    // Returns int -> Verschil met 52 (gemiddelde over alle mogelijke jaren)
    public int max52CompDaysInYear(){
        int returnValue = 0;
        int count = 0;
        for(int week = 0; week < 52; week++){
            for(int d = 0; d < 7; d++){
                if(this.assigned[d][week % this.amountOfWeeks] == 2){
                    count++;
                }
            }
        }

        if(count > 52) returnValue += (count - 52);
        return returnValue;
    }

    // Maximaal 65 compensatiedagen ingepland per jaar.
    // Returns int -> Verschil met 65
    public int max65RestDaysInYear(){
        int returnValue = 0;
        int count = 0;
        for(int week = 0; week < 52; week++){
            for(int d = 0; d < 7; d++){
                if(this.assigned[d][week % this.amountOfWeeks] == 1){
                    count++;
                }
            }
        }
        if(count > 65) returnValue += (count - 65);
        return returnValue;
    }

    // Combinaties van R / CX / CY mogen enkel langer zijn dan 2 dagen wanneer toegestaan door het personeel (paramter nmbsFactory)
    // Returns int, hoeveel keer dit wel het geval is
    // SLIDING WINDOW VAN 3
    public int moreThanTwoConsecutiveHomeDays(){
        if(nmbsFactory.getMoreThanTwoConsecutiveHomeDays()) return 0;
        int returnValue = 0;
        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                int beginDay = d;
                int days = 0;
                int count = 0;
                for(int week = w; week < w + Math.ceil((d+3)/7.0); week++){
                    for(int i = beginDay; i < 7; i++){
                        if(this.assigned[i][week % this.amountOfWeeks] <= 3){
                            count++;
                        }
                        if(days == 2){
                            break;
                        }else{
                            days++;
                        }
                    }
                    beginDay = 0;
                }
                if(count == 3) returnValue++;
            }
        }
        return returnValue;
    }

    // Combinaties van R / CX / CY mogen SOWIESO niet langer zijn dan 3
    // Returns int, hoeveel keer dit wel het geval is
    // SLIDING WINDOW VAN 4
    public int moreThanThreeConsecutiveHomeDays(){
        int returnValue = 0;
        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                int beginDay = d;
                int days = 0;
                int count = 0;
                for(int week = w; week < w + Math.ceil((d+4)/7.0); week++){
                    for(int i = beginDay; i < 7; i++){
                        if(this.assigned[i][week % this.amountOfWeeks] <= 3){
                            count++;
                        }
                        if(days == 3){
                            break;
                        }else{
                            days++;
                        }
                    }
                    beginDay = 0;
                }
                if(count == 4) returnValue++;
            }
        }
        return returnValue;
    }


    //
    public int calculateMeanHoursLessOrMoreThanMinMaxPerDay(){

        int returnValue = 0;

        for(int week = 0; week < this.amountOfWeeks; week++){

            /*
            int count = 0;
            for(int w = week; w < (week + 4); w++) {
                for (int day = 0; day < 7; day++) {
                    if (this.assigned[day][w % this.amountOfWeeks] == 4) {
                        count = count - 380;
                    } else if (this.assigned[day][w % this.amountOfWeeks] > 5) {
                        Prestation p = nmbsFactory.getPrestation(this.assigned[day][w % this.amountOfWeeks]);
                        int deltaTime;
                        if (p.getEndTime() < p.getStartTime()) {
                            deltaTime = p.getEndTime() + (1440 - p.getStartTime());
                        } else {
                            deltaTime = p.getEndTime() - p.getStartTime();
                        }
                        count += deltaTime;
                    }
                }
            }

            double mean = count / (7.0 * 4.0);
            if(mean > 480.0){
                returnValue += (Math.ceil(mean) - 480);
            }
             */

            int count = 0;
            for (int day = 0; day < 7; day++) {
                if (this.assigned[day][week % this.amountOfWeeks] == 4) {
                    count = count - 380;
                } else if (this.assigned[day][week % this.amountOfWeeks] > 5) {
                    Prestation p = nmbsFactory.getPrestation(this.assigned[day][week % this.amountOfWeeks]);
                    int deltaTime;
                    if (p.getEndTime() < p.getStartTime()) {
                        deltaTime = p.getEndTime() + (1440 - p.getStartTime());
                    } else {
                        deltaTime = p.getEndTime() - p.getStartTime();
                    }
                    count += deltaTime;
                }
            }

            double mean = count / 7.0;
            if(mean > 480.0){
                returnValue += (Math.ceil(mean) - 480.0);
            }
        }

        /*
        double mean = count / ((double) countDays);
        if(mean < 470.0){
            return (int) Math.ceil(470.0 - mean);
        } else if(mean > 500.0){
            return (int) Math.ceil(mean - 500.0);
        }
        */


        return returnValue;
    }

    public int calculateMeanHoursLessOrMoreThanMinMaxPerWeek(){

        int returnValue = 0;

        for(int week = 0; week < this.amountOfWeeks; week++){

            int count = 0;
            for(int w = week; w < (week + 4); w++) {
                for (int day = 0; day < 7; day++) {
                    if (this.assigned[day][w % this.amountOfWeeks] == 4) {
                        count = count - 380;
                    } else if (this.assigned[day][w % this.amountOfWeeks] > 5) {
                        Prestation p = nmbsFactory.getPrestation(this.assigned[day][w % this.amountOfWeeks]);
                        int deltaTime;
                        if (p.getEndTime() < p.getStartTime()) {
                            deltaTime = p.getEndTime() + (1440 - p.getStartTime());
                        } else {
                            deltaTime = p.getEndTime() - p.getStartTime();
                        }
                        count += deltaTime;
                    }
                }
            }

            double mean = count / 4.0;
            if(mean > 2520.0){
                returnValue += (Math.ceil(mean) - 2520.0);
            }
        }

        return returnValue;

    }

    public int reserveConstraint(){
        int returnValue = 0;

        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                int deltaWeek = 0;
                if(d == 6) deltaWeek = 1;

                if(this.assigned[d][w] > 5 && this.assigned[(d + 1) % 7][(w + deltaWeek) % this.amountOfWeeks] == 5){
                    Prestation p = this.nmbsFactory.getPrestation(this.assigned[d][w]);
                    if((p.getEndTime() < p.getStartTime()) || (p.getEndTime() > 1380)){
                        returnValue++;
                    }
                }

                if(this.assigned[d][w] == 5 && this.assigned[(d + 1) % 7][(w + deltaWeek) % this.amountOfWeeks] > 5){
                    Prestation p = this.nmbsFactory.getPrestation(this.assigned[(d + 1) % 7][(w + deltaWeek) % this.amountOfWeeks]);
                    if(p.getStartTime() < 300){
                        returnValue++;
                    }
                }
            }
        }
        return returnValue;
    }

    // OA - CY verboden combinatie
    public int forbiddenCombination(){
        int returnValue = 0;

        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                int deltaWeek = 0;
                if(d == 6) deltaWeek = 1;

                if(this.assigned[d][w] == 3 && this.assigned[(d + 1) % 7][(w + deltaWeek) % this.amountOfWeeks] == 4){
                    returnValue++;
                }
            }
        }

        return returnValue;
    }

    public int prestationDurationAfterCY(){
        int returnValue = 0;

        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                int deltaWeek = 0;
                if(d == 6) deltaWeek = 1;

                if(this.assigned[d][w] == 3 && this.assigned[(d + 1) % 7][(w + deltaWeek) % this.amountOfWeeks] > 5){
                    Prestation p = this.nmbsFactory.getPrestation(this.assigned[(d + 1) % 7][(w + deltaWeek) % this.amountOfWeeks]);

                    int deltaTime;
                    if(p.getEndTime() < p.getStartTime()){
                        deltaTime = (1440 - p.getStartTime()) + p.getEndTime();
                    }else{
                        deltaTime = p.getEndTime() - p.getStartTime();
                    }

                    if(deltaTime > 480){
                        returnValue++;
                    }
                }

            }
        }
        return returnValue;
    }



    // ---------------------------------------------------------------
    //                      SOFT CONSTRAINTS
    // ---------------------------------------------------------------

    // Counts the amount of OA-days and sets this amount to YearPeriod (rule of 3: * 52/amountOfWeeks)
    public void calculateAmountOfOA(){
        int count = 0;
        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                if(this.assigned[d][w % this.amountOfWeeks] == 4){
                    count++;
                }
            }
        }
        this.amountOfOA = count;
    }

    public void calculateAmountOfSplittedWeekends(){
        int count = 0;
        for(int w = 0; w < this.amountOfWeeks; w++){
            if(!(this.assigned[5][w % this.amountOfWeeks] <= 2 && this.assigned[6][w % this.amountOfWeeks] <= 2)){
                count++;
            }
        }
        this.splitWeekendScore = count;

        int degreeOfDistribution = (int) (this.amountOfWeeks / ((double) count));
        int newCount = 0;
        for(int w = 0; w < this.amountOfWeeks; w++){
            if(!(this.assigned[5][w % this.amountOfWeeks] <= 2 && this.assigned[6][w % this.amountOfWeeks] <= 2)){
                int i = 1;
                while(this.assigned[5][(w+i) % this.amountOfWeeks] <= 2 && this.assigned[6][(w+i) % this.amountOfWeeks] <= 2){
                    i++;
                }
                if(Math.abs(degreeOfDistribution - i) > 0) newCount += Math.abs(degreeOfDistribution - i);
            }
        }

        this.distributionFreeWeekends = newCount;
    }

    public void calculateHumanization(){
        int returnValue = 0;

        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){

                int deltaWeek = 0;
                if((d + 2) > 6) deltaWeek = 1;

                if(this.assigned[d][w] > 5 && this.assigned[(d + 2) % 7][(w + deltaWeek) % this.amountOfWeeks] > 5){
                   Prestation firstPrestation = nmbsFactory.getPrestation(this.assigned[d][w]);
                   Prestation secondPrestation = nmbsFactory.getPrestation(this.assigned[(d + 2) % 7][(w + deltaWeek) % this.amountOfWeeks]);

                   /*
                   //Eerste prestatie eindigt na middernacht en tweede prestatie (2 dagen later) begint voor de middag
                   if(firstPrestation.getEndTime() < 480 && secondPrestation.getStartTime() < 720){
                       returnValue++;
                   }
                    */

                    // MINSTENS 33u verschil
                    if(firstPrestation.getEndTime() < firstPrestation.getStartTime()){
                        if(secondPrestation.getStartTime() - firstPrestation.getEndTime() < 600){
                            returnValue++;
                        }
                    }else{
                        if((1440 - firstPrestation.getEndTime()) + secondPrestation.getStartTime() < 600){
                            returnValue++;
                        }
                    }
                }
            }
        }
        this.humanization = returnValue * nmbsFactory.INFEASIBILITY_WEIGHT();
    }

    public void calculateRCXCY(){

        int count = 0;
        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                if(this.assigned[d][w % this.amountOfWeeks] <= 3){
                    count++;
                }
            }
        }

        double countPerYear = (count / (double) this.amountOfWeeks) * 52.0;
        this.daysRCXCYScore = (int) countPerYear;
    }

    public void calculateScoreMoreThen2ConsecutiveHomeDays(){
        int returnValue = 0;
        for(int w = 0; w < 52; w++){
            for(int d = 0; d < 7; d++){
                int beginDay = d;
                int days = 0;
                int count = 0;
                for(int week = (w % this.amountOfWeeks); week < (w % this.amountOfWeeks) + Math.ceil((d+3)/7.0); week++){
                    for(int i = beginDay; i < 7; i++){
                        if(this.assigned[i][week % this.amountOfWeeks] <= 3){
                            count++;
                        }
                        if(days == 2){
                            break;
                        }else{
                            days++;
                        }
                    }
                    beginDay = 0;
                }
                if(count == 3) returnValue++;
            }
        }
        this.moreThen2ConsecutiveHomeDaysScore = returnValue;
    }

    public void calculateScoreConsecutivePrestationDays(){
        int countLess = 0;
        int countMore = 0;

        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                int countDays = 0;
                int day = d;
                int deltaWeek = 0;
                boolean homeDays = false;

                // Begint te tellen bij de dag die je inspecteert, is dus sowieso 1!
                while(this.assigned[day % 7][(w + deltaWeek) % this.amountOfWeeks] >= 5){
                    countDays++;

                    if(day == 6) deltaWeek++;
                    day = (day + 1) % 7;

                    homeDays = true;

                    if(countDays > 1000){
                        System.out.println("Stuck in while calculateScoreConsecutivePrestationDays");
                    }
                }

                if(homeDays) {
                    if (countDays < 5) countLess += (5 - countDays);
                    if (countDays > 5) countMore += (countDays - 5);



                    deltaWeek = (int) Math.ceil((d + countDays) / 6.0) - 1;
                    if(((d + countDays - 1) % 7) != 6){
                        w += deltaWeek; // loop telt er zelf een week bij
                    }else{
                        if(w + deltaWeek >= this.amountOfWeeks) break;
                    }
                    d = (d + countDays - 1) % 7; // -1 omdat loop hierna er eentje zal bijtellen

                    //deltaWeek = (int) Math.ceil((d + countDays) / 6.0) - 1;
                    //w += deltaWeek;
                    //d = (d + countDays - 1) % 7; // -1 omdat loop hierna er eentje zal bijtellen
                }

                if(w >= this.amountOfWeeks) break;
            }
        }
        this.consecutiveLessPrestationDaysScore = countLess;
        this.consecutiveMorePrestationDaysScore = countMore;
    }

    public void calculateRoutineScore(){
        int returnValue = 0;
        for(int w = 0; w < this.amountOfWeeks; w++){
            for(int d = 0; d < 7; d++){
                int beginDay = d;
                int days = 0;
                int count = 0;
                int previousId = -1;
                for(int week = w; week < w + Math.ceil((d+3)/7.0); week++){
                    for(int i = beginDay; i < 7; i++){

                        if(this.assigned[i][week % this.amountOfWeeks] > 5){
                            Prestation p = nmbsFactory.getPrestation(this.assigned[i][week % this.amountOfWeeks]);
                            if(p.getId() == previousId){
                                count++;
                            }else{
                                previousId = p.getId();
                                count = 0;
                            }
                        }else{
                            break;
                        }

                        if(days == 2){
                            break;
                        }else{
                            days++;
                        }

                    }
                    beginDay = 0;
                }
                if(count == 2) returnValue++;
            }
        }
        this.routineScore = returnValue;
    }


    // ---------------------------------------------------------------
    //                     MACHINE LEARNING - PY
    // ---------------------------------------------------------------

    public void calculateSimmularity() {

        float[][][] dataNMBS = new float[1][322][7];
        for(int i = 0; i < 322; i++){
            for(int j = 0; j < 7; j++){
                dataNMBS[0][i][j] = -1.0f;
            }
        }

        int index = 0;
        for(int i = 0; i < amountOfWeeks; i++){
            for(int day = 0; day < 7; day++){
                float[] oneHotArray = new float[7];

                if(this.assigned[day][i] > 5){
                    Prestation p = nmbsFactory.getPrestation(this.assigned[day][i]);
                    if(p.getStartTime() < 420){
                        oneHotArray[0] = 1.0f;
                    }else if(p.getEndTime() <= 60 || p.getEndTime() > 1260){
                        oneHotArray[2] = 1.0f;
                    }else if(p.getEndTime() > 60 && p.getEndTime() < 480){
                        oneHotArray[3] = 1.0f;
                    }else {
                        oneHotArray[1] = 1.0f;
                    }
                }else if(this.assigned[day][i] <= 3){
                    oneHotArray[4] = 1.0f;
                }else if(this.assigned[day][i] == 4){
                    oneHotArray[5] = 1.0f;
                }else{
                    oneHotArray[1] = 1.0f;
                }

                if(day >= 5){
                    oneHotArray[6] = 1.0f;
                }

                dataNMBS[0][index] = oneHotArray;
                index++;
            }
        }

        double accuracy = nmbsFactory.predictInMachineLearningModel(dataNMBS);
        int thisScore = (int) ((1.0 - accuracy) * nmbsFactory.MACHINE_LEARNING_WEIGHT());

        this.machineLearningScore = thisScore;

    }


    // ---------------------------------------------------------------
    //                     GETTERS AND SETTERS
    // ---------------------------------------------------------------


    public int getAmountOfMonthsWithNoFreeWeekend() {
        return amountOfMonthsWithNoFreeWeekend;
    }

    public int getLessDaysRestPerMonth() {
        return lessDaysRestPerMonth;
    }

    public int getLessDaysRestPerTwoMonths() {
        return lessDaysRestPerTwoMonths;
    }

    public int getMoreThan8DaysBetweenRestdays() {
        return moreThan8DaysBetweenRestdays;
    }

    public int getMoreThan7ConsecutivePrestationdays() {
        return moreThan7ConsecutivePrestationdays;
    }

    public int getLessThen8FreeDaysInMonth() {
        return lessThen8FreeDaysInMonth;
    }

    public int getMoreThan5ConsecutiveNightPrestations() {
        return moreThan5ConsecutiveNightPrestations;
    }

    public int getLessThan14HoursBetweenPrestations() {
        return lessThan14HoursBetweenPrestations;
    }

    public int getPenaltiesForTimeBetweenDaysAtHome() {
        return penaltiesForTimeBetweenDaysAtHome;
    }

    public int getPrestationsEndingTooLateBeforeRestday() {
        return prestationsEndingTooLateBeforeRestday;
    }

    public int getPrestationsBeginningTooEarlyAfterRestday() {
        return prestationsBeginningTooEarlyAfterRestday;
    }

    public int getPrestationsEndingTooLateBeforeCompentationDay() {
        return prestationsEndingTooLateBeforeCompentationDay;
    }

    public int getPrestationsBeginningTooEarlyAfterCompentationDay() {
        return prestationsBeginningTooEarlyAfterCompentationDay;
    }

    public int getPrestationsEndingTooLateBeforeSpecificCompentationDay() {
        return prestationsEndingTooLateBeforeSpecificCompentationDay;
    }

    public int getPrestationsBeginningTooEarlyAfterSpecificCompentationDay() {
        return prestationsBeginningTooEarlyAfterSpecificCompentationDay;
    }

    public int getMeanMinutesLessOrMoreThanMinMaxPerDay(){
        return meanMinutesLessOrMoreThanMinMaxPerDay;
    }

    public int getMeanMinutesLessOrMoreThanMinMaxPerWeek(){
        return meanMinutesLessOrMoreThanMinMaxPerWeek;
    }

    public int getMoreThanOneSpecCompdayIn4Weeks() {
        return moreThanOneSpecCompdayIn4Weeks;
    }

    public int getMoreThan13SpecCompdayPerYear() {
        return moreThan13SpecCompdayPerYear;
    }

    public int getMoreThan52CompdayPerYear() {
        return moreThan52CompdayPerYear;
    }

    public int getMoreThan65RestdaysPerYear() {
        return moreThan65RestdaysPerYear;
    }

    public int getMoreThen2ConsecutiveHomeDays() {
        return moreThen2ConsecutiveHomeDays;
    }

    public int getMoreThen3ConsecutiveHomeDays() {
        return moreThen3ConsecutiveHomeDays;
    }

    public int getTotalFeasibilityScore() {
        return totalFeasibilityScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getRoutineScore() {
        return routineScore;
    }

    public int getSplitWeekendScore() {
        return splitWeekendScore;
    }

    public int getAmountOfOA() {
        return amountOfOA;
    }

    public int getDaysRCXCYScore() {
        return daysRCXCYScore;
    }

    public int getHumanization() {
        return humanization;
    }

    public int getDistributionFreeWeekends() {
        return distributionFreeWeekends;
    }

    public int getMachineLearningScore() {
        return machineLearningScore;
    }

    public int getConsecutiveLessPrestationDaysScore() {
        return consecutiveLessPrestationDaysScore;
    }

    public int getConsecutiveMorePrestationDaysScore(){
        return consecutiveMorePrestationDaysScore;
    }

    public int getMoreThen2ConsecutiveHomeDaysScore() {
        return moreThen2ConsecutiveHomeDaysScore;
    }

    public NMBSFactory getNmbsFactory() {
        return nmbsFactory;
    }

    public int[][] getAssigned() {
        return assigned;
    }

    public int getAmountOfWeeks() {
        return amountOfWeeks;
    }

    public ArrayList<Integer> getStoredParameters() {
        return new ArrayList<>(storedParameters);
    }
}
