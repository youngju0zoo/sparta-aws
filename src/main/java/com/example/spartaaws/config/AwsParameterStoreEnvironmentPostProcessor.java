package com.example.spartaaws.config;

import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.Profiles;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

import java.util.Map;

public class AwsParameterStoreEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (!environment.acceptsProfiles(Profiles.of("prod"))){
            return;
        }
        try (SsmClient ssmClient = SsmClient.builder().region(Region.AP_SOUTHEAST_2).build()) {
            String teamName = getParameter(ssmClient, "/sparta-aws/db/team-name");
            String dbUrl = getParameter(ssmClient, "/sparta-aws/db/url");
            String dbUsername = getParameter(ssmClient, "/sparta-aws/db/username");
            String dbPassword = getParameter(ssmClient, "/sparta-aws/db/password");

            Map<String, Object> properties = Map.of(
                    "team-name", teamName, "DB_URL", dbUrl, "DB_USERNAME",dbUsername, "DB_PASSWORD", dbPassword
            );
            environment.getPropertySources().addFirst(new MapPropertySource("awsParameterStore", properties));
        }
    }

    private String getParameter(SsmClient ssmClient, String parameterName) {
        GetParameterResponse response = ssmClient.getParameter(
                GetParameterRequest.builder().name(parameterName).withDecryption(true).build()
        );
        return response.parameter().value();
    }

    @Override
    public int getOrder() {
        return ConfigDataEnvironmentPostProcessor.ORDER + 1;
    }
}
