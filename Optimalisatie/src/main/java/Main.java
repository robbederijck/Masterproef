import java.io.File;
import java.io.FileWriter;
import java.util.Date;

public class Main {

    public static int numberOfIterations = 0;

    public static void main(String[] args) throws Exception {
        long availableTime = 1300000;

        FileWriter fw5 = new FileWriter(new File("src/results.csv"));
        fw5.close();

        int[][] initialSolution = null;
        String inputFileName = "src/inputPrestatiesFSD_ReeksA.xml";
        NMBSFactory nmbsFactory = NMBSFileReader.getNMBSFactoryFromInput(inputFileName);

        int amountOfLoops = 1;
        availableTime = (availableTime / amountOfLoops);

        long starttime = new Date().getTime();

        boolean initialCalculated = false;
        while (!initialCalculated) {
            numberOfIterations++;

            initialSolution = nmbsFactory.getEmptyPersonnelSchedule();
            initialCalculated = nmbsFactory.calculateInitialSchedule(initialSolution);
        }

        NMBSFileWriter.writeOutputFile(initialSolution, nmbsFactory, "initialSolution.xml");
        System.out.println("numberOfIterations = " + numberOfIterations + "\n");

        long currentTime = new Date().getTime();
        long expiredTime = currentTime - starttime;

        int[][] finalAssigned = SimulatedAnnealing.Optimize(nmbsFactory, initialSolution, availableTime - expiredTime);
        NMBSFileWriter.writeOutputFile(finalAssigned, nmbsFactory, "finalSolution.xml");

    }
}
