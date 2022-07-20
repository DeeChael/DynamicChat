package net.deechael.dynamichat.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "dynamichat", name = "DynamicChat", version = "1.00.0", authors = "DeeChael")
public class DyChatVelocity {

    private static DyChatVelocity INSTANCE;

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    @Inject
    public DyChatVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        INSTANCE = this;
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    public static DyChatVelocity getInstance() {
        return INSTANCE;
    }

}
