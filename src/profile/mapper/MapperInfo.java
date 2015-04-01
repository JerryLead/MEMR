package profile.mapper;

import java.io.Serializable;

public class MapperInfo implements Serializable {

    private static final long serialVersionUID = 1719440286067648190L;
    
    private String taskId;
    private String machine;
    private boolean isMapRunning;
    private String runningPhase;

    // map phase information
    private Input input;

    private MapperBuffer buffer;
    private Spill spill;
    private Merge merge;

    private MapperCounters counters;

    
    public MapperInfo() {
	this.runningPhase = "init"; // map, spill, merge, over
	this.isMapRunning = false; 
	
	this.input = new Input();
	this.buffer = new MapperBuffer();
	this.spill = new Spill();
	this.merge = new Merge();
	this.counters = new MapperCounters();
    }
    
    public String toString() {
	StringBuilder sb = new StringBuilder();

	sb.append("------------ RunningStatus ------------\n");
	sb.append("[taskId] " + machine + "/" + taskId + "\n");
	sb.append("[RunningPhase] " + runningPhase + "\n");
	sb.append("[is map() running] " + isMapRunning + "\n\n");
	
	sb.append("------------ InputSplit ------------\n");
	sb.append(input + "\n");
	
	sb.append("------------ SpillBuffer ------------\n");
	sb.append(buffer + "\n");
	
	if(spill.getHasCombine())
	    sb.append("----- Spills combine -----\n");
	else
	    sb.append("----- Spills without combine --------\n");
	sb.append(spill + "\n");
	
	sb.append("------------ Merge ------------\n");
	sb.append(merge + "\n");
	
	sb.append("------------ Counters ------------\n");
	sb.append(counters + "\n");
	
	return sb.toString();
	
    }
    
    // set task id
    public void setTaskId(String taskId) {
	this.taskId = taskId;
    }

    public void setMachine(String machine) {
	this.machine = machine;
    }
    
    public void setRunningPhase(String runningPhase) {
	this.runningPhase = runningPhase;
    }
    
    public void setIsMapRunning(boolean isMapRunning) {
	this.isMapRunning = isMapRunning;
    }
    
    public Input getInput() {
	return input;
    }

    public void setInput(Input input) {
	this.input = input;
    }

    public MapperBuffer getBuffer() {
	return buffer;
    }

    public void setBuffer(MapperBuffer buffer) {
	this.buffer = buffer;
    }

    public Spill getSpill() {
	return spill;
    }

    public void setSpill(Spill spill) {
	this.spill = spill;
    }

    public Merge getMerge() {
	return merge;
    }

    public void setMerge(Merge merge) {
	this.merge = merge;
    }

    public MapperCounters getCounters() {
	return counters;
    }



    public String getTaskId() {
	return taskId;
    }

    public String getRunningPhase() {
        return runningPhase;
    }

    public boolean isMapRunning() {
        return isMapRunning;
    }

 

}
