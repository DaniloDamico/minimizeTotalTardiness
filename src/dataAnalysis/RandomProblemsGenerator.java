package dataAnalysis;

import entities.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomProblemsGenerator {

    public static List<Job> generate(int numberOfJobs, boolean strictDeadline, boolean highVarianceProcessing) {
        List<Job> jobList = new ArrayList<>();

        int totalProcessing = 0;
        for (int i = 0; i < numberOfJobs; i++) {
            int processing = computeProcessing(highVarianceProcessing);
            totalProcessing += processing;

            Job j = new Job();
            j.setId(Integer.toString(i));
            j.setProcessingTime(processing);
            jobList.add(j);
        }

        for (int i = 0; i < numberOfJobs; i++) {
            int deadline = computeDeadline(strictDeadline, totalProcessing);
            jobList.get(i).setDueDate(deadline);
        }

        return jobList;
    }

    private static int computeProcessing(boolean highVarianceProcessing) {
        Random random = new Random();

        if (highVarianceProcessing) {
            return random.nextInt(1, 101); // uniform distribution
            // variance = (100-1)/12 = 816,75
        } else {
            int processing = 0;
            do {
                processing = (int) random.nextGaussian(50, 16);

                /*
                68% of values are within 1 standard deviations of the mean: [34, 66]
                99.7% of values are within 3 standard deviations of the mean: [2, 98]

                variance = stddev^2 = 256
                 */

            } while (processing > 100 || processing < 1);

            return processing;
        }

    }

    private static int computeDeadline(boolean strictDeadline, int totalProcessing) {
        Random random = new Random();

        if (strictDeadline) {
            return (int) (random.nextDouble(0.1, 1) * totalProcessing);
        } else {
            return (int) (random.nextDouble(0.5, 1) * totalProcessing);
        }
    }

}
