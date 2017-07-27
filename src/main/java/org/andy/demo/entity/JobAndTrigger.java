package org.andy.demo.entity;

import java.math.BigInteger;

public class JobAndTrigger {
    private String JOB_NAME;
    private String JOB_GROUP;
    private String JOB_CLASS_NAME;
    private String TRIGGER_NAME;
    private String TRIGGER_GROUP;
    private String TRIGGER_TYPE;
    private String TRIGGER_STATE;
    private BigInteger REPEAT_INTERVAL;
    private BigInteger TIMES_TRIGGERED;
    private String CRON_EXPRESSION;
    private String TIME_ZONE_ID;

    public String getJOB_NAME() {
        return JOB_NAME;
    }

    public void setJOB_NAME(String jOB_NAME) {
        this.JOB_NAME = jOB_NAME;
    }

    public String getJOB_GROUP() {
        return JOB_GROUP;
    }

    public void setJOB_GROUP(String jOB_GROUP) {
        this.JOB_GROUP = jOB_GROUP;
    }

    public String getJOB_CLASS_NAME() {
        return JOB_CLASS_NAME;
    }

    public void setJOB_CLASS_NAME(String jOB_CLASS_NAME) {
        this.JOB_CLASS_NAME = jOB_CLASS_NAME;
    }

    public String getTRIGGER_NAME() {
        return TRIGGER_NAME;
    }

    public void setTRIGGER_NAME(String tRIGGER_NAME) {
        this.TRIGGER_NAME = tRIGGER_NAME;
    }

    public String getTRIGGER_GROUP() {
        return TRIGGER_GROUP;
    }

    public void setTRIGGER_GROUP(String tRIGGER_GROUP) {
        this.TRIGGER_GROUP = tRIGGER_GROUP;
    }

    public String getTRIGGER_TYPE() {
        return TRIGGER_TYPE;
    }

    public void setTRIGGER_TYPE(String TRIGGER_TYPE) {
        this.TRIGGER_TYPE = TRIGGER_TYPE;
    }

    public String getTRIGGER_STATE() {
        return TRIGGER_STATE;
    }

    public void setTRIGGER_STATE(String TRIGGER_STATE) {
        this.TRIGGER_STATE = TRIGGER_STATE;
    }

    public BigInteger getREPEAT_INTERVAL() {
        return REPEAT_INTERVAL;
    }

    public void setREPEAT_INTERVAL(BigInteger rEPEAT_INTERVAL) {
        this.REPEAT_INTERVAL = rEPEAT_INTERVAL;
    }

    public BigInteger getTIMES_TRIGGERED() {
        return TIMES_TRIGGERED;
    }

    public void setTIMES_TRIGGERED(BigInteger tIMES_TRIGGERED) {
        this.TIMES_TRIGGERED = tIMES_TRIGGERED;
    }

    public String getCRON_EXPRESSION() {
        return CRON_EXPRESSION;
    }

    public void setCRON_EXPRESSION(String cRON_EXPRESSION) {
        this.CRON_EXPRESSION = cRON_EXPRESSION;
    }

    public String getTIME_ZONE_ID() {
        return TIME_ZONE_ID;
    }

    public void setTIME_ZONE_ID(String tIME_ZONE_ID) {
        this.TIME_ZONE_ID = tIME_ZONE_ID;
    }

}
