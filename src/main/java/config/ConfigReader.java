package config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:site.properties"})
public interface ConfigReader extends Config {
    @Key("yota.baseUrl")
    String yotaUrl();
}
