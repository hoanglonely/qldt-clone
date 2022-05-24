package com.mb.lab.banks.utils.event;

import java.util.Date;

import com.mb.lab.banks.utils.event.broadcaster.Event;

@Event("BusinessLogEvent")
public class BusinessLogEvent {

    private String logType;
    private String appCode;
    private Date startTime;
    private String username;
    private String ipAddress;
    private String path;
    private String funcName;
    private String funcArgs;
    private String funcClass;
    private Long duration;
    private String description;

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getFuncArgs() {
        return funcArgs;
    }

    public void setFuncArgs(String funcArgs) {
        this.funcArgs = funcArgs;
    }

    public String getFuncClass() {
        return funcClass;
    }

    public void setFuncClass(String funcClass) {
        this.funcClass = funcClass;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
