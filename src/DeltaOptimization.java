import entities.Job;

import java.util.ArrayList;
import java.util.List;

public class DeltaOptimization {

    public static List<Integer> optimize(List<Job> jobList, int startingTime){
        List<Integer> deltaList = new ArrayList<>();
        int k = Utils.largestProcessingTimeJobIndex(jobList);
        int deadlineK = jobList.get(k).getDueDate(); // d(k)
        int minimumTotalDeadline; // d'(k)

        while(true){

            // step 1 and 2
            minimumTotalDeadline = computeKMinimumCompletionTime(jobList, startingTime, deadlineK);
            while (minimumTotalDeadline > deadlineK){
                deadlineK = minimumTotalDeadline;
                minimumTotalDeadline = computeKMinimumCompletionTime(jobList, startingTime, deadlineK);
            }

            int j;
            for(j=jobList.size()-1; j>=0; j-- ){
                if(jobList.get(j).getDueDate()<=deadlineK) break;
            }

            deltaList.add(j-k); // worthwhile delta

            List<Integer> SubsetSecond = new ArrayList<>();
            for(int index=0; index<jobList.size(); index++){
                if(jobList.get(index).getDueDate() > deadlineK){
                    SubsetSecond.add(index);
                }
            }

            if(SubsetSecond.isEmpty()) break;

            int smallestDeadline = -1;
            for(int index : SubsetSecond){
                if(smallestDeadline == -1 || jobList.get(index).getDueDate() < smallestDeadline)
                    smallestDeadline = jobList.get(index).getDueDate();
            }

            deadlineK = smallestDeadline;
        }


        return deltaList;
    }

    private static int computeKMinimumCompletionTime(List<Job> jobList, int startingTime, int deadlineK){
        int minimumTotalDeadline = startingTime;
        for(Job j : jobList){
            if(j.getDueDate()<=deadlineK){
                minimumTotalDeadline+=j.getProcessingTime();
            }
        }

        return minimumTotalDeadline;
    }
}
