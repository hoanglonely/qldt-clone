package com.mb.lab.banks.utils.event.broadcaster;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Thanh
 */
@ConfigurationProperties("event")
public class EventProperties {
	
	private boolean enabled;
    
    /**
     * List of application which want to subscribe to broadcasted event
     */
    private List<String> applications;
    
    private EventBroadcastMethod method;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

    public List<String> getApplications() {
        return applications;
    }

    public void setApplications(List<String> applications) {
        this.applications = applications;
    }

	public EventBroadcastMethod getMethod() {
		return method;
	}

	public void setMethod(EventBroadcastMethod method) {
		this.method = method;
	}

}
