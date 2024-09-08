package org.jyj.common.configuration;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProfileApplicationRunner implements ApplicationRunner {

    private final EnvironmentComponent environmentComponent;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        environmentComponent.showProject();
    }
}
