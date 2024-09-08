package org.jyj.status.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.jyj.common.configuration.EnvironmentComponent;

import java.time.LocalDateTime;

@Getter
public class HealthResponse {
    private String name;
    private String version;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "systemDefault")
    private LocalDateTime timestamp;

    public HealthResponse() {
        this.name = EnvironmentComponent.PROJECT_NAME;
        this.version = EnvironmentComponent.VERSION;
        this.timestamp = LocalDateTime.now();
    }
}
