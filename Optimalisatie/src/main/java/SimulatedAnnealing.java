import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

public class SimulatedAnnealing {

    public static int[][] Optimize(NMBSFactory nmbsFactory, int[][] initialSolution, long availableTime){

        Solution currentSolution = new Solution(nmbsFactory, initialSolution);
        currentSolution.calculateSimmularity();

        int currentValue = currentSolution.calculateCost();
        int bestValue = currentValue;
        int iterations = 0;
        System.out.println("Initial value: " + currentValue);
        Solution bestSolution = new Solution(currentSolution);

        long startTime = new Date().getTime();
        long endTime = startTime + availableTime;

        double Tmax = nmbsFactory.INFEASIBILITY_WEIGHT() / 15.0;
        double Tmin = 2.0;
        System.out.println("Maximum temperature: " + Tmax);
        System.out.println("Minimum temperature: " + Tmin);
        System.out.println("Initial Machine Learning score " + (100 - currentSolution.getMachineLearningScore()) + "%");

        double T = Tmax;
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("src/results.csv"), true))) {
            bufferedWriter.write(T + ", " + currentValue + ", " + bestValue + ", " + (100 - currentSolution.getMachineLearningScore()) + "\n");
            while (T > Tmin) {
                for (int i = 0; i < 5000; i++) {
                    currentSolution.doDelta();
                    if((i+1) % 350 == 0 && currentSolution.isFeasible()) currentSolution.calculateSimmularity();
                    int newValue = currentSolution.calculateDeltaCost();

                    int dE = newValue - currentValue;

                    if (dE < 0) {
                        currentSolution.commitDelta();
                        currentValue = newValue;
                        if (currentValue < bestValue) {
                            bestValue = currentValue;
                            bestSolution = new Solution(currentSolution);
                        }
                    } else if (Math.random() < Math.exp(-dE / T)) {
                        currentSolution.commitDelta();
                        currentValue = newValue;

                    } else {
                        currentSolution.revertDelta();
                    }

                }

                iterations += 5000;
                long currentTime = new Date().getTime();
                boolean feasible = currentSolution.isFeasible();

                if (!feasible) {
                    T = Tmin - 1 + Math.pow((double) (Tmax - Tmin + 1), (endTime - currentTime) / (double) availableTime);
                } else {
                    T = Tmin - 1 + Math.pow((double) (20 - Tmin + 1), (endTime - currentTime) / (double) availableTime);
                }

                System.out.println("Temperatuur: " + T + ", Best value: " + bestValue + ", Current value: " + currentValue + ", Machine Learning score: " + (100 - currentSolution.getMachineLearningScore()) + "%, Remaining time: " + (endTime - currentTime) + "ms, Feasible: " + feasible + ", Iterations: " + iterations);
                if (currentTime > endTime) break;


                bufferedWriter.write(T + ", " + currentValue + ", " + bestValue + ", " + (100 - currentSolution.getMachineLearningScore()) + "\n");

                if (currentValue == 0) break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Finished with best value: " + bestValue);
        bestSolution.calculateSimmularity();
        bestSolution.printFeasibilityUpdate();
        bestSolution.printSoftUpdate();

        return bestSolution.getAssigned();
    }

}
