import entities.Job;

import java.util.ArrayList;
import java.util.List;

public class ShortcutSolution {

    public static List<Job> optimize(List<Job> jobList, int startingTime){

        List<Job> optimalList = applyTheorem5(jobList, startingTime);
        if(optimalList != null)
            return optimalList;

        return applyTheorem4(jobList, startingTime);
    }

    /*Theorem 4
    In a sequence where all jobs are ordered in decreasing p(i) if all jobs are tardy the sequence is optimal
     */
    private static List<Job> applyTheorem4(List<Job> jobList, int startingTime){
        List<Job> workingJobList = induceEarlierDueDates(jobList, startingTime);
        Utils.orderJobsByLargestProcessingTime(workingJobList);

        if(Utils.computeNumberOfTardyJobs(workingJobList, startingTime)==workingJobList.size()){

            //put back the original due date into the optimal list
            for(int i=0; i<workingJobList.size(); i++){
                int finalI = i;
                Job tempJob = jobList.stream().filter(job -> workingJobList.get(finalI).getId().equals(job.getId())).findFirst().orElse(null);
                workingJobList.set(i, tempJob);
            }
            return workingJobList;
        }

        return null;

    }

    private static List<Job> induceEarlierDueDates(List<Job> jobList, int startingTime){
        List<Job> workingJobList = new ArrayList<>(jobList);
        Utils.orderJobsByLargestProcessingTime(workingJobList);

        for(int k=jobList.size()-1; k>=0; k--){
            int newDeadlineK = jobList.get(k).getDueDate();

            int completionK = newDeadlineK; // dummy value for the first loop iteration
            do {
                newDeadlineK = completionK;
                completionK = startingTime;
                for (Job j : jobList) {
                    if (j.getDueDate() >= newDeadlineK && j.getProcessingTime() > jobList.get(k).getProcessingTime())
                        completionK += j.getProcessingTime();
                }
            } while(completionK < newDeadlineK);

        }
        return workingJobList;
    }

    /*Theorem 5
    Jobs by increasing deadline order. If at most one job is tardy, sequence is optimal
    */
    private static List<Job> applyTheorem5(List<Job> jobList, int startingTime){
        List<Job> workingJobList = induceLaterDueDates(jobList, startingTime);
        if(workingJobList == null) return null;
        Utils.orderJobsByDueDate(workingJobList);

        if(Utils.computeNumberOfTardyJobs(workingJobList, startingTime)<=1){

            //put back the original due date into the optimal list
            for(int i=0; i<workingJobList.size(); i++){
                int finalI = i;
                Job tempJob = jobList.stream().filter(job -> workingJobList.get(finalI).getId().equals(job.getId())).findFirst().orElse(null);
                workingJobList.set(i, tempJob);
            }

            int tardiness = Utils.computeTardiness(workingJobList, startingTime);
            return workingJobList;
        }

        return null;
    }

    private static List<Job> induceLaterDueDates(List<Job> jobList, int startingTime){

        if(jobList.size()<1){
            return null;
        }

        Utils.orderJobsByDueDate(jobList);

        List<Job> laterDueDates = new ArrayList<>();
        while(laterDueDates.size() < jobList.size()) laterDueDates.add(null);
        laterDueDates.set(0, jobList.get(0));

        for(int k=0; k < jobList.size(); k++){

            int newDeadlineK = jobList.get(k).getDueDate();

            int completionK = newDeadlineK; // dummy value for the first loop iteration
            do {
                newDeadlineK = completionK;

                completionK = startingTime + jobList.get(k).getProcessingTime();
                for (Job j : jobList) {
                    if (j.getDueDate() <= newDeadlineK && j.getProcessingTime() < jobList.get(k).getProcessingTime())
                        completionK += j.getProcessingTime();
                }

                Job laterJob = new Job();
                laterJob.setId(jobList.get(k).getId());
                laterJob.setProcessingTime(jobList.get(k).getProcessingTime());
                laterJob.setDueDate(newDeadlineK);
                laterDueDates.set(k, laterJob);

            } while(completionK > newDeadlineK);

        }
        return laterDueDates;
    }

}
