package ch.rts.demo;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;

@SuppressWarnings("rawtypes")
public class MongoDBInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.4.19"));

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        mongoDBContainer.start();
        context.getEnvironment().getPropertySources().addFirst(new MapPropertySource(
            "testcontainersMongo",
            new HashMap<>() {{
                put("spring.data.mongodb.uri", mongoDBContainer.getConnectionString() + "/demo");
            }}
        ));
    }
}
