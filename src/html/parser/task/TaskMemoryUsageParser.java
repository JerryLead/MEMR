package html.parser.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import html.util.HtmlFetcher;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import profile.mapper.MapperInfo;
import profile.reducer.ReducerInfo;

public class TaskMemoryUsageParser {

    public static void parseMapperUsageLog(String logLink, MapperInfo mapper, String outputDir) {
	Document mapLogs = HtmlFetcher.getHtml(logLink);

	Element syserrPre = mapLogs.getElementsByTag("pre").get(1);
	String text = syserrPre.text().trim();
	
	if (!text.isEmpty()) {
	    String syserr[] = text.split("\\n");
	    String taskId = mapper.getTaskId();
	    int id = Integer.parseInt(taskId.substring(taskId.indexOf("_m_") + 3, taskId.lastIndexOf('_')));
	    File jobFile = new File(outputDir, "m" + id + ".txt");
	    if (!jobFile.getParentFile().exists())
		jobFile.getParentFile().mkdirs();

	    try {
		PrintWriter writer = new PrintWriter(new FileWriter(jobFile));
		for (int i = 0; i < syserr.length; i++)
		    writer.println(syserr[i]);

		writer.close();

	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }
    
    public static void parseReduceUsageLog(String logLink, ReducerInfo reducer, String outputDir) {
	Document reduceLogs = HtmlFetcher.getHtml(logLink);
	Element syserrPre = reduceLogs.getElementsByTag("pre").get(1);
	String text = syserrPre.text().trim();
	
	
	PrintWriter writer = null;
	
	if (!text.isEmpty()) {
	    String syserr[] = text.split("\\n");
	    String taskId = reducer.getTaskId();
	    int id = Integer.parseInt(taskId.substring(taskId.indexOf("_r_") + 3, taskId.lastIndexOf('_')));
	    File jobFile = new File(outputDir, "r" + id + ".txt");
	    if (!jobFile.getParentFile().exists())
		jobFile.getParentFile().mkdirs();

	    try {
		writer = new PrintWriter(new FileWriter(jobFile));
		for (int i = 0; i < syserr.length; i++)
		    writer.println(syserr[i]);

		

	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	
	
	Element sysoutPre = reduceLogs.getElementsByTag("pre").get(0);
	String sysout[] = sysoutPre.text().split("\\n");
	
	if(sysout.length > 0) {
	    for(int i = 0; i < sysout.length; i++) {
		if(sysout[i].startsWith("gid"))
		    writer.println(sysout[i]);
		else if(sysout[i].startsWith("Min")) {
		    reducer.getReducerCounters().setMin(Long.parseLong(sysout[i].substring(sysout[i].indexOf('=') + 2)));
		}
		else if(sysout[i].startsWith("Max")) {
		    reducer.getReducerCounters().setMax(Long.parseLong(sysout[i].substring(sysout[i].indexOf('=') + 2)));
		}
		else if(sysout[i].startsWith("Mean")) {
		    reducer.getReducerCounters().setMean(Long.parseLong(sysout[i].substring(sysout[i].indexOf('=') + 2)));
		}
		else if(sysout[i].startsWith("Median")) {
		    reducer.getReducerCounters().setMedian(Long.parseLong(sysout[i].substring(sysout[i].indexOf('=') + 2)));
		}
		else if(sysout[i].startsWith("Q1")) {
		    reducer.getReducerCounters().setQ1(Long.parseLong(sysout[i].substring(sysout[i].indexOf('=') + 2)));
		}
		else if(sysout[i].startsWith("Q2")) {
		    reducer.getReducerCounters().setQ2(Long.parseLong(sysout[i].substring(sysout[i].indexOf('=') + 2)));
		}
	    }
	}
	
	if(writer != null)
	    writer.close();
	
    }
}
