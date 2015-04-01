package html.parser.task;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import profile.mapper.MapperInfo;
import profile.reducer.ReducerInfo;
import html.util.HtmlFetcher;

public class ReduceTaskParser {

    public static ReducerInfo parseReduceTask(String reduceTaskDetailsJsp, String serializeDir) {
	Document reduceDetails = HtmlFetcher.getHtml(reduceTaskDetailsJsp);
	/*
	Element tr = null;
	for (Element elem : reduceDetails.getElementsByTag("tbody").first()
		.children()) {
	    if (elem.child(2).text().equals("SUCCEEDED")) {
		tr = elem;
		break;
	    }
	}
	*/
	if(reduceDetails.getElementsByTag("tbody").isEmpty())
	    return null;
	Element tr = reduceDetails.getElementsByTag("tbody").first().child(1);

	ReducerInfo reducer = new ReducerInfo();
	
	

	String taskId = tr.child(0).text();
	String machine = tr.child(1).text();
	machine = machine.substring(machine.lastIndexOf('/') + 1);

	reducer.setTaskId(taskId); // set task id
	reducer.setMachine(machine); // set machine

	String countersLink = tr.child(10).child(0).absUrl("href");
	parseReduceTaskCounters(countersLink, reducer);

	String logLink = tr.child(9).select("a[href]").get(2).absUrl("href");
	parseReduceTaskLog(logLink, reducer);
	
	TaskMemoryUsageParser.parseReduceUsageLog(logLink, reducer, serializeDir);

	return reducer;
    }
    
    private static void parseReduceTaskCounters(String countersLink,
	    ReducerInfo reducer) {
	Document countersDoc = HtmlFetcher.getHtml(countersLink);
	Elements countersTrs = countersDoc.getElementsByTag("tbody").first()
		.children();
	for (Element elem : countersTrs) {
	    if (elem.getElementsByTag("td").size() == 3) {
		String value = elem.child(2).text();
		reducer.getReducerCounters().setCounter(elem.child(1).text(),
			Long.parseLong(value.replaceAll(",", "")));
	    }
	}
    }

    private static void parseReduceTaskLog(String logLink, ReducerInfo reducer) {
	TaskLogParser.parseReducerLog(logLink, reducer);
    }

}
