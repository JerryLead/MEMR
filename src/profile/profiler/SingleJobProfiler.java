package profile.profiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import profile.job.JobProfile;
import profile.job.JobTasksParser;
import profile.mapper.MapperCounters;
import profile.mapper.MapperInfo;
import profile.reducer.ReducerCounters;
import profile.reducer.ReducerInfo;
import html.parser.job.JobConfigurationParser;
import html.parser.link.LinksSaver;
import html.parser.task.MapTaskParser;
import html.parser.task.ReduceTaskParser;

public class SingleJobProfiler {
    private JobProfile job;
    private LinksSaver linksSaver;
    private String serializeDir;


    public SingleJobProfiler(String hostname, String jobId, String serializeDir) {
	job = new JobProfile();
	job.setJobId(jobId);
	linksSaver = new LinksSaver(hostname, jobId);
	this.serializeDir = serializeDir + File.separatorChar + jobId;
	
    }


    public JobProfile profile() {


	JobConfigurationParser.parseJobConf(linksSaver, job); // get the
							      // configuration
							      // of this job
	JobTasksParser.parseJobTasks(linksSaver); // initiate map/reduce tasks
						  // link list

	parseMapperTasks();
	parseReducerTasks();

	return job;

    }

    private void parseMapperTasks() {

	for (int i = 0; i < linksSaver.getMap_tasks_list().size(); i++) {
	    MapperInfo newMapper = MapTaskParser.parseMapTask(linksSaver
		    .getMap_tasks_list().get(i), serializeDir);
	    job.addMapperInfo(newMapper);
	}

    }

    private void parseReducerTasks() {

	for (int i = 0; i < linksSaver.getReduce_tasks_list().size(); i++) {
	    ReducerInfo newReducer = ReduceTaskParser
		    .parseReduceTask(linksSaver.getReduce_tasks_list().get(i), serializeDir);
	    job.addReducerInfo(newReducer);
	}
    }

    private static void serialize(String serializeDir, JobProfile job, String jobId) {
	File jobFile = new File(serializeDir + File.separatorChar + job.getJobId() + File.separatorChar, jobId + ".out");
	if (!jobFile.getParentFile().exists())
	    jobFile.getParentFile().mkdirs();

	try {
	    FileOutputStream fos = new FileOutputStream(jobFile);
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(job);
	    oos.flush();
	    oos.close();
	} catch (FileNotFoundException e) {    
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    public static void main(String[] args) {
	String jobId = "job_201503311659_0007";
	
	String hostname = "master";
	boolean toSerialize = true;
	String serializeDir = "/Users/xulijie/Documents/MEMR/InMemWordCount/";

	SingleJobProfiler profiler = new SingleJobProfiler(hostname, jobId, serializeDir);

	JobProfile job = profiler.profile();
	
	if(toSerialize)
	    serialize(serializeDir, job, jobId);
	
	outputDataflowInfo(serializeDir, job);
	
    }

    private static void outputDataflowInfo(String serializeDir, JobProfile job) {
	
	
	File jobFile = new File(serializeDir + File.separatorChar + job.getJobId() + File.separatorChar, "dataflow.txt");
	if (!jobFile.getParentFile().exists())
	    jobFile.getParentFile().mkdirs();

	
	try {
	    PrintWriter writer = new PrintWriter(new FileWriter(jobFile));
	    
	    for(MapperInfo mapperInfo : job.getMapperInfoList()) {
		
		String taskId = mapperInfo.getTaskId();
		int id = Integer.parseInt(taskId.substring(taskId.indexOf("_m_") + 3, taskId.lastIndexOf('_')));
		writer.println("## Mapper " + id);
		
		MapperCounters counter = mapperInfo.getCounters();
		
		writer.println("HDFS_BYTES_READ = " + counter.getHdfs_bytes_read());
		writer.println("Map input records = " + counter.getMap_input_records());
		writer.println("Map output records = " + counter.getMap_output_records());
		
		if(counter.getCombine_input_records() != 0) {
		    writer.println("Combine input records = " + counter.getCombine_input_records());
		    writer.println("Combine output records = " + counter.getCombine_output_records());
		}
		
		writer.println();
	    }
	    
	    writer.println("================================\n");
	    
	    for(ReducerInfo reducerInfo : job.getReducerInfoList()) {
		String taskId = reducerInfo.getTaskId();
		int id = Integer.parseInt(taskId.substring(taskId.indexOf("_r_") + 3, taskId.lastIndexOf('_')));
		writer.println("## Reducer " + id);
		
		ReducerCounters counter = reducerInfo.getReducerCounters();
		
		writer.println("Reduce input groups = " + counter.getReduce_input_groups());
		writer.println("Reduce input records = " + counter.getReduce_input_records());
		writer.println("Reduce output records = " + counter.getReduce_output_records());
		
		if(counter.getCombine_input_records() != 0) {
		    writer.println("Combine input records = " + counter.getCombine_input_records());
		    writer.println("Combine output records = " + counter.getCombine_output_records());
		}
		
		if(counter.getMin() != 0) {
		    writer.println("Min = " + counter.getMin());
		    writer.println("Max = " + counter.getMax());
		    writer.println("Mean = " + counter.getMean());
		    writer.println("Median = " + counter.getMedian());
		    writer.println("Q1 = " + counter.getQ1());
		    writer.println("Q2 = " + counter.getQ2());
		}
		
		writer.println();
		
	    }
	    
	    writer.close();
	    
	} catch (FileNotFoundException e) {    
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
