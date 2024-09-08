package org.jyj.common.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LogMessage {
    private String threadId;
    private String method;
    private String url;
    private String userAgent;
    private String host;
    private String clientIp;
    private String requestParams;
    private String responseParams;
    private String requestAt;
    private String responseAt;
    private long elapsedTimeInMS;

    public String toLogString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
