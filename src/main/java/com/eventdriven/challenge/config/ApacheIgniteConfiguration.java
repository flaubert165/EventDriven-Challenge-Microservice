package com.eventdriven.challenge.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.concurrent.TimeUnit;

//@Configuration
//@EnableIgniteRepositories(basePackages="com.eventdriven.challenge.repositories")
public class ApacheIgniteConfiguration {
    private static final String INSTANCE_NAME = "challenge-cache";
    private static final int PORT = 47100;

//    @Bean
    public IgniteConfiguration igniteCfg() {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setClientMode(false);
        igniteConfiguration.setMetricsLogFrequency(0);
        igniteConfiguration.setQueryThreadPoolSize(2);
        igniteConfiguration.setDataStreamerThreadPoolSize(1);
        igniteConfiguration.setManagementThreadPoolSize(2);
        igniteConfiguration.setPublicThreadPoolSize(2);
        igniteConfiguration.setSystemThreadPoolSize(2);
        igniteConfiguration.setRebalanceThreadPoolSize(1);
        igniteConfiguration.setAsyncCallbackPoolSize(2);
        igniteConfiguration.setPeerClassLoadingEnabled(false);
        igniteConfiguration.setIgniteInstanceName(INSTANCE_NAME);
        BinaryConfiguration binaryConfiguration = new BinaryConfiguration();
        binaryConfiguration.setCompactFooter(false);
        igniteConfiguration.setBinaryConfiguration(binaryConfiguration);

        configurePersistence(igniteConfiguration);

        /**
         * connection config
         */
        ConnectorConfiguration connectorConfiguration = new ConnectorConfiguration();
        connectorConfiguration.setPort(PORT);

        CacheConfiguration restaurantConnectionHealthConfig = new CacheConfiguration("ifood-restaurant-check-connection-health");
        restaurantConnectionHealthConfig.setCopyOnRead(false);
        restaurantConnectionHealthConfig.setBackups(0);
        restaurantConnectionHealthConfig.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        restaurantConnectionHealthConfig.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.MINUTES, 2)));

        igniteConfiguration.setCacheConfiguration(restaurantConnectionHealthConfig);
        return igniteConfiguration;
    }

    private void configurePersistence(IgniteConfiguration igniteConfiguration) {
        DataStorageConfiguration storageCfg = new DataStorageConfiguration();

        storageCfg.setWalArchivePath("./data/walArchive");
        igniteConfiguration.setDataStorageConfiguration(storageCfg);
    }

//    @Bean(destroyMethod = "close")
    public Ignite igniteInstance(IgniteConfiguration igniteConfiguration) throws IgniteException {
        return Ignition.start("apacheignite-cassandra.xml");
    }
}