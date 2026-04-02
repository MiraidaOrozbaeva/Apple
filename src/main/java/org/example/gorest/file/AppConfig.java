package org.example.gorest.file;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:app.properties"})
public interface AppConfig extends Config {

    @Key("gorest.base.url")
    String gorestBaseUrl();

    @Key("bearer.token")
    String bearerToken();

    @Key("server")
    String server();

    @Key("port")
    int port();

    @Key("user")
    String user();


}
