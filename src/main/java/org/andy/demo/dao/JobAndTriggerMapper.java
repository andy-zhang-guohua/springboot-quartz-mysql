package org.andy.demo.dao;

import org.andy.demo.entity.JobAndTrigger;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface JobAndTriggerMapper {
    @Select("SELECT" +
            " qrtz_job_details.JOB_NAME," +
            " qrtz_job_details.JOB_GROUP," +
            " qrtz_job_details.JOB_CLASS_NAME," +
            " qrtz_triggers.TRIGGER_NAME," +
            " qrtz_triggers.TRIGGER_GROUP," +
            " qrtz_cron_triggers.CRON_EXPRESSION," +
            " qrtz_cron_triggers.TIME_ZONE_ID," +
            " qrtz_triggers.TRIGGER_STATE," +
            " qrtz_triggers.TRIGGER_TYPE" +
            " FROM" +
            " qrtz_job_details" +
            " JOIN qrtz_triggers" +
            " JOIN qrtz_cron_triggers ON qrtz_job_details.SCHED_NAME = qrtz_triggers.SCHED_NAME " +
            " AND qrtz_job_details.JOB_NAME = qrtz_triggers.JOB_NAME " +
            " AND qrtz_job_details.JOB_GROUP = qrtz_triggers.JOB_GROUP " +
            " AND qrtz_triggers.TRIGGER_NAME = qrtz_cron_triggers.TRIGGER_NAME " +
            " AND qrtz_triggers.TRIGGER_GROUP = qrtz_cron_triggers.TRIGGER_GROUP")
    public List<JobAndTrigger> getCronJobAndTriggerDetails();
}
