package profile.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import profile.commons.configuration.Configuration;
import profile.mapper.MapperInfo;
import profile.reducer.ReducerInfo;

public class JobProfile implements Serializable {

    private static final long serialVersionUID = 3089567988191585508L;

    // job name
    private String jobName;
    
    private String jobId;

    // mappers and reducers
    private List<MapperInfo> mapperList = new ArrayList<MapperInfo>();
    private List<ReducerInfo> reducerList = new ArrayList<ReducerInfo>();

    // configuration
    private Configuration jobConfiguraiton = new Configuration();

    // set basic information
    public void setJobName(String jobName) {
	this.jobName = jobName;
    }
    
    public void setJobId(String jobId) {
	this.jobId = jobId;
    }

    // add mapper and reducer infos
    public void addMapperInfo(MapperInfo newMapper) {
	mapperList.add(newMapper);
    }

    public void addReducerInfo(ReducerInfo newReducer) {
	reducerList.add(newReducer);
    }

    // add job configuration items
    public void addConfItem(String confName, String value) {
	jobConfiguraiton.set(confName, value);

    }

    public List<MapperInfo> getMapperInfoList() {
	return mapperList;
    }

    public List<ReducerInfo> getReducerInfoList() {
	return reducerList;
    }

    public Configuration getJobConfiguration() {
	return jobConfiguraiton;
    }

    public String getJobName() {
	return jobName;
    }
    
    public String getJobId() {
	return jobId;
    }

}
