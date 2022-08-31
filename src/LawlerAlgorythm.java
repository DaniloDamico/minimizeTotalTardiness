import com.ampl.AMPL;
import entities.Job;
import entities.TardinessSubproblem;

import java.util.*;

/*
A Dynamic Programming algorythm for the solution of minimum total tardiness as presented in
A "pseudo polynomial" algorithm for sequencing jobs to minimize total tardiness, Lawler (1977)
*/

public class LawlerAlgorythm {
    AMPL ampl;
    List<Job> jobs = new ArrayList<>();

    LawlerAlgorythm(AMPL ampl){

        this.ampl = ampl;

        List<Map.Entry<String, Integer>> processingData = DatExtractor.extract(ampl, "processing");
        List<Map.Entry<String, Integer>> dueData = DatExtractor.extract(ampl, "due");

        initializeJobs(paramEnum.PROCESSING, processingData);
        initializeJobs(paramEnum.DUE, dueData);
    }

    private void initializeJobs(paramEnum dataType, List<Map.Entry<String, Integer>> paramList){

        for (Map.Entry<String, Integer> currEntry : paramList) {

            if (jobs.stream().anyMatch(o -> o.getId().equals(currEntry.getKey()))) {

                switch (dataType) {
                    case DUE -> jobs.stream().filter(o -> o.getId().equals(currEntry.getKey())).forEach(
                            o -> o.setDueDate(currEntry.getValue()));
                    case PROCESSING -> jobs.stream().filter(o -> o.getId().equals(currEntry.getKey())).forEach(
                            o -> o.setProcessingTime(currEntry.getValue()));
                }

            } else{
                Job newJob = new Job();
                newJob.setId(currEntry.getKey());

                switch(dataType){
                    case DUE -> newJob.setDueDate(currEntry.getValue());
                    case PROCESSING -> newJob.setProcessingTime(currEntry.getValue());
                }

                jobs.add(newJob);
            }

        }


    }

    public void solve(){
        Utils.orderJobsByDueDate(jobs);
        List<Job> optimalSolution = dynamicProgrammingSolve(jobs, 0);

        //TODO move to main controller class
        System.out.println("Solved!");
        System.out.println("Tardiness: " + Utils.computeTardiness(optimalSolution, 0));
        System.out.println("List: ");
        for(Job j: optimalSolution) System.out.println(j.getId());
    }

    private List<Job> dynamicProgrammingSolve(List<Job> list, int startingTime){

        if(list.isEmpty() || list.size() == 1){
            return list;
        }

        List<Job> shortcutSolution = ShortcutSolution.optimize(list, startingTime);
        if (shortcutSolution != null)
            return shortcutSolution;

        int maxProcessing = Utils.largestProcessingTimeJobIndex(list); // job k
        List<TardinessSubproblem> subproblemsList = new ArrayList<>();

        // ritorna i delta per i quali vale la pena calcolare un Tardiness Subproblem
        List<Integer> deltaList = DeltaOptimization.optimize(list, startingTime);
        //List<Integer> deltaList = new ArrayList<>();

        if(deltaList.isEmpty()){
            for(int delta = 0; delta<= list.size() - maxProcessing; delta++) // delta >=0 && delta <= jobs.size - maxProcessing
                deltaList.add(delta);
        }

        // generate the Tardiness Subproblems
        for(Integer delta : deltaList){

            List<Job> untilMaxPlusDelta = new ArrayList<>();  // job da 1 a k + delta (eccetto k)
            for(int i = 0; i < maxProcessing + delta + 1 && i < list.size(); i++){ // da 1 a k + delta tranne k
                if(i==maxProcessing)
                    continue;
                untilMaxPlusDelta.add(list.get(i));
            }

            List<Job> afterMaxPlusDelta = new ArrayList<>(); //job k+ delta fino a n
            for(int j = maxProcessing + delta + 1; j < list.size(); j++){ // da k + delta + 1 a n
                afterMaxPlusDelta.add(list.get(j));
            }

            TardinessSubproblem currSubproblem = new TardinessSubproblem(untilMaxPlusDelta, list.get(maxProcessing), afterMaxPlusDelta);
            subproblemsList.add(currSubproblem);
        }

        // solve the problems recursively
        List<List<Job>> candidatesList = new ArrayList<>();
        for(TardinessSubproblem subproblem : subproblemsList){

            List<Job> currUntilList = dynamicProgrammingSolve(subproblem.getUntilDelta(), startingTime);
            List<Job> currList = new ArrayList<>(currUntilList);

            currList.add(subproblem.getLargestP());

            int kCompletionTime = startingTime;
            for(Job job : currList)
                kCompletionTime+= job.getProcessingTime();

            List<Job> currAfterList = dynamicProgrammingSolve(subproblem.getAfterDelta(), kCompletionTime);
            currList.addAll(currAfterList);

            candidatesList.add(currList);
        }

        return findLowestTardinessList(candidatesList, startingTime);
    }

    private List<Job> findLowestTardinessList(List<List<Job>> candidatesList, int startingTime){

        List<Job> optimalList = new ArrayList<>();
        int optimalTardiness = -1;
        
        for(List<Job> candidate : candidatesList){

            int currTardiness = Utils.computeTardiness(candidate, startingTime);

            if(optimalTardiness == -1 || currTardiness < optimalTardiness){
                optimalTardiness = currTardiness;
                optimalList = candidate;
            }

        }
        return optimalList;
    }
}
