package com.topsec.bdc.platform.storage;

import org.osgi.framework.BundleContext;

import com.topsec.bdc.platform.core.activator.PlatformActivator;
import com.topsec.bdc.platform.log.PlatformLogger;
import com.topsec.bdc.platform.storage.services.MongoDBStorageService;


public class Activator extends PlatformActivator {

    /**
     * logger.
     */
    final private static PlatformLogger _logger = PlatformLogger.getLogger(Activator.class);

    private static BundleContext CONTEXT = null;

    public static BundleContext getContext() {

        return CONTEXT;
    }

    @Override
    public void start(BundleContext context) throws Exception {

        Activator.CONTEXT = context;

        // register message formatter service
        this.registerService(new MongoDBStorageService());

        //
        _logger.info("Platform storage is started.");
    }

    @Override
    public void stop(BundleContext context) throws Exception {

        this.unregisterAllService();
        //
        _logger.info("Platform storage is stopped.");
    }
}
