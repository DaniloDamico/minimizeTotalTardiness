import entities.Job;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utils {

    public static void orderJobsByDueDate(List<Job> jobs){
        jobs.sort(Comparator.comparing(Job::getDueDate));
    }

    public static void orderJobsByLargestProcessingTime(List<Job> jobs){
        jobs.sort(Comparator.comparing(Job::getProcessingTime));
        Collections.reverse(jobs);
    }

    public static int computeTardiness(List<Job> jobList, int startingTime){
        int tardiness = 0;
        int currTime = startingTime;

        for(Job currJob : jobList){
            currTime+= currJob.getProcessingTime();
            if(currTime > currJob.getDueDate())
                tardiness+=currTime-currJob.getDueDate();
        }

        return tardiness;
    }

    public static int computeNumberOfTardyJobs(List<Job> jobList, int startingTime){
        int tardyJobs = 0;
        int currTime = startingTime;

        for(Job currJob : jobList){
            currTime+= currJob.getProcessingTime();
            if(currTime > currJob.getDueDate())
                tardyJobs++;
        }

        return tardyJobs;
    }

    public static int largestProcessingTimeJobIndex(List<Job> list){
        if(list.size() <= 1) return 0;

        int index = 0;
        for(int i=1; i<list.size(); i++){
            if(list.get(i).getProcessingTime() > list.get(index).getProcessingTime())
                index = i;
        }
        return index;
    }
}
