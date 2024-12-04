package service.config;

import lombok.Getter;
import org.aeonbits.owner.ConfigFactory;

public class WebConfigProvider {

    @Getter
    private static final ConfigReader props = ConfigFactory.newInstance().create(ConfigReader.class);
}
