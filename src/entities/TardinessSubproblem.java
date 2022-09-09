package entities;

import java.util.List;

public class TardinessSubproblem {
    List<Job> untilSigma = null;
    Job largestP = null;
    List<Job> afterSigma = null;

    public TardinessSubproblem(List<Job> untilSigma, Job largestP, List<Job> afterSigma) {
        setUntilSigma(untilSigma);
        setLargestP(largestP);
        setAfterSigma(afterSigma);
    }

    public List<Job> getUntilDelta() {
        return untilSigma;
    }

    public void setUntilSigma(List<Job> untilSigma) {
        this.untilSigma = untilSigma;
    }

    public Job getLargestP() {
        return largestP;
    }

    public void setLargestP(Job largestP) {
        this.largestP = largestP;
    }

    public List<Job> getAfterDelta() {
        return afterSigma;
    }

    public void setAfterSigma(List<Job> afterSigma) {
        this.afterSigma = afterSigma;
    }
}
