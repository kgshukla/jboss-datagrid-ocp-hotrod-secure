package com.example.hotrod.service;

import javax.net.ssl.SSLContext;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.infinispan.commons.marshall.jboss.GenericJBossMarshaller;
import org.infinispan.commons.util.SslContextFactory;
import org.springframework.stereotype.Component;

@Component
public class HotrodService {
	
	@Value("${DATAGRID_APP_HOTROD_SERVICE_HOST:172.30.154.124}")
	private String hotrodhost;

    @Value("${DATAGRID_APP_HOTROD_SERVICE_PORT:11333}")
	private String hotrodport;
	
	@Value("${cache_username:user1}")
	private String username;
	
	@Value("${cache_passwd:welcome1}")
	private String password;
	
	@Value("${keystore_password:welcome1}")
	private String keystore_password;
	
	@Value("${keystore_alias:selfsigned}")
	private String keystore_alias;
	
	private static final String servername = "jdg-server";
	
	private static final String DEFAULT_SSL_PROTOCOL = "TLSv1.2";
	private static final String DEFAULT_KEYSTORE_TYPE = "JKS";
	
  public RemoteCacheManager getCacheManager() {

    SSLContext sslCtx = SslContextFactory.getContext(
        "/etc/datagrid-secret-volume/jdg-https.jks" ,
        DEFAULT_KEYSTORE_TYPE,
        keystore_password.toCharArray(), 
        null, 
        keystore_alias, 
        "/etc/datagrid-secret-volume//jdg-https.jks",
        DEFAULT_KEYSTORE_TYPE,
        keystore_password.toCharArray(),
        DEFAULT_SSL_PROTOCOL,
        null);	

    ConfigurationBuilder config = new ConfigurationBuilder();
    config.addServers(hotrodhost.concat(":").concat(hotrodport))
      .marshaller(new GenericJBossMarshaller())
      .security()
      .ssl()
      .sslContext(sslCtx)
      .enable()
      .authentication()
      .serverName(servername) //define server name, should be specified in XML configuration
      .saslMechanism("DIGEST-MD5") // define SASL mechanism, in this example we use DIGEST with   MD5 hash
      .username(username)
      .password(password);

    return new RemoteCacheManager(config.build());
  }
}
