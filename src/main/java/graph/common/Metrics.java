package graph.common;

public class Metrics {
    private long startTime;
    private long endTime;
    private int dfsVisits;
    private int edgeRelaxations;
    private int queueOperations;

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        endTime = System.nanoTime();
    }

    public long getElapsedTime() {
        return endTime - startTime;
    }

    public void incrementDfsVisits() {
        dfsVisits++;
    }

    public void incrementEdgeRelaxations() {
        edgeRelaxations++;
    }

    public void incrementQueueOperations() {
        queueOperations++;
    }

    public int getDfsVisits() { return dfsVisits; }
    public int getEdgeRelaxations() { return edgeRelaxations; }
    public int getQueueOperations() { return queueOperations; }

    public void reset() {
        dfsVisits = 0;
        edgeRelaxations = 0;
        queueOperations = 0;
    }
}