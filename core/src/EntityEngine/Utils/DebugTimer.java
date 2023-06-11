package EntityEngine.Utils;



public class DebugTimer {

    Long startTime;
    Long endTime;
    Long duration = 0L;
    int iteration = 0;
    Long averageDuration = 0L;

    public void startTimer(){

        startTime = java.lang.System.nanoTime();
    }

    public void endTimer(){
        iteration++;
        endTime = java.lang.System.nanoTime();
        duration += (endTime - startTime);
        if (iteration >= 500){
            averageDuration = duration/iteration;
            iteration = 0;
            duration = 0L;
        }

    }

    public Long getAverageDuration() {
        return averageDuration/100;
    }
}
