import dataAnalysis.FileManager;
import dataAnalysis.RandomProblemsGenerator;
import entities.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PseudorandomExperimentsMain {

    static List<Long> lenientLowVarianceTimes = new ArrayList<>();
    static List<Long> lenientLowVarianceTardiness = new ArrayList<>();
    static List<Long> lenientHighVarianceTimes = new ArrayList<>();
    static List<Long> lenientHighVarianceTardiness = new ArrayList<>();
    static List<Long> strictLowVarianceTimes = new ArrayList<>();
    static List<Long> strictLowVarianceTardiness = new ArrayList<>();
    static List<Long> strictHighVarianceTimes = new ArrayList<>();
    static List<Long> strictHighVarianceTardiness = new ArrayList<>();


    public static void main(String[] args) {

        // lenient deadline, low variance processing
        for (int i = 0; i < 200; i++) {
            System.out.println("type ONE problem " + i);
            List<Job> jobList = RandomProblemsGenerator.generate(50, false, false);
            timeExecution(jobList, lenientLowVarianceTimes, lenientLowVarianceTardiness);
        }

        // lenient deadline, high variance processing
        for (int i = 0; i < 200; i++) {
            System.out.println("type TWO problem " + i);
            List<Job> jobList = RandomProblemsGenerator.generate(50, false, true);
            timeExecution(jobList, lenientHighVarianceTimes, lenientHighVarianceTardiness);
        }

        // strict deadline, low variance processing
        for (int i = 0; i < 200; i++) {
            System.out.println("type THREE problem " + i);
            List<Job> jobList = RandomProblemsGenerator.generate(50, true, false);
            timeExecution(jobList, strictLowVarianceTimes, strictLowVarianceTardiness);
        }

        // strict deadline, high variance processing
        for (int i = 0; i < 200; i++) {
            System.out.println("type FOUR problem " + i);
            List<Job> jobList = RandomProblemsGenerator.generate(50, true, true);
            timeExecution(jobList, strictHighVarianceTimes, strictHighVarianceTardiness);
        }

        FileManager.saveDataToFile("lenientLowVarianceTimes", lenientLowVarianceTimes, lenientLowVarianceTardiness);
        FileManager.saveDataToFile("lenientHighVarianceTimes", lenientHighVarianceTimes, lenientHighVarianceTardiness);
        FileManager.saveDataToFile("strictLowVarianceTimes", strictLowVarianceTimes, strictLowVarianceTardiness);
        FileManager.saveDataToFile("strictHighVarianceTimes", strictHighVarianceTimes, strictHighVarianceTardiness);
    }

    private static void timeExecution(List<Job> jobs, List<Long> executionTimes, List<Long> tardinessList) {
        LawlerAlgorythm solver = new LawlerAlgorythm(jobs);
        ExecutorService service = Executors.newSingleThreadExecutor();

        try {
            Runnable r = () -> {
                long startTime = System.currentTimeMillis();
                try {
                    long tardiness = solver.solve();
                    long finalTime = System.currentTimeMillis();

                    long elapsedTime = finalTime - startTime;
                    executionTimes.add(elapsedTime);
                    tardinessList.add(tardiness);

                } catch (TimeoutException ignore) {
                }

            };

            Future<?> f = service.submit(r);

            f.get(5, TimeUnit.MINUTES);     // attempt the task for timeout minutes

        } catch (final TimeoutException | InterruptedException | ExecutionException e) {
            executionTimes.add((long) -10);
            tardinessList.add((long) -10);
        } finally {
            service.shutdownNow();
        }
    }
}
