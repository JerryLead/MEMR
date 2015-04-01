package html.parser.task;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import profile.mapper.MapperInfo;
import html.util.HtmlFetcher;

public class MapTaskParser {

    public static MapperInfo parseMapTask(String mapTaskDetailsJsp, String serializeDir) {
	
	Document mapDetails = HtmlFetcher.getHtml(mapTaskDetailsJsp);

	Element tr = mapDetails.getElementsByTag("tbody").first().child(1);

	MapperInfo mapper = new MapperInfo();

	String taskId = tr.child(0).text();
	mapper.setTaskId(taskId); // set TaskId

	String machine = tr.child(0).child(0).absUrl("href");
	
	machine = machine.substring(7, machine.indexOf(':', 7));
	mapper.setMachine(machine);

	String counterLink = tr.child(8).child(0).absUrl("href");
	parseMapTaskCounters(counterLink, mapper); // set task counters

	String logLink = tr.child(7).select("a[href]").get(2).absUrl("href");
	parseMapTaskLog(logLink, mapper); // set task log infos
	
	TaskMemoryUsageParser.parseMapperUsageLog(logLink, mapper, serializeDir);
	return mapper;
    }



    private static void parseMapTaskCounters(String counterLink, MapperInfo mapper) {
	Document countersDoc = HtmlFetcher.getHtml(counterLink);
	Elements countersTrs = countersDoc.getElementsByTag("tbody").first()
		.children();
	for (Element elem : countersTrs) {
	    if (elem.getElementsByTag("td").size() == 3) {
		String value = elem.child(2).text();
		mapper.getCounters().setCounter(
			elem.child(1).text(),
			Long.parseLong(value.replaceAll(",", "")));
	    }
	}
    }

    private static void parseMapTaskLog(String logLink, MapperInfo mapper) {
	TaskLogParser.parseMapperLog(logLink, mapper);
    }

  

}
