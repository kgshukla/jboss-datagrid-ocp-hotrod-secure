package com.example.hotrod.service;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.infinispan.commons.marshall.jboss.GenericJBossMarshaller;
import org.springframework.stereotype.Component;

@Component
public class HotrodService {
	
	@Value("${DATAGRID_APP_HOTROD_SERVICE_HOST:172.30.154.124}")
	private String hotrodhost;

  @Value("${DATAGRID_APP_HOTROD_SERVICE_PORT:11333}")
	private String hotrodport;
	
	@Value("${USERNAME:user1}")
	private String username;
	
	@Value("${PASSWORD:welcome1}")
	private String password;
	
	private static final String servername = "jdg-server";
	
	private static final String REALM = "ApplicationRealm";
	
	public RemoteCacheManager getCacheManager() {
		
		ConfigurationBuilder config = new ConfigurationBuilder();
        config.addServers(hotrodhost.concat(":").concat(hotrodport))
                .marshaller(new GenericJBossMarshaller())
                .security().authentication()
                .serverName(servername) //define server name, should be specified in XML configuration
                .saslMechanism("DIGEST-MD5") // define SASL mechanism, in this example we use DIGEST with   MD5 hash
                .username(username)
                .password(password);

                //.callbackHandler(new LoginHandler(username, password.toCharArray(), REALM)) // define login handler, implem  entation defined
                //.enable();

        return new RemoteCacheManager(config.build());
	}
}
