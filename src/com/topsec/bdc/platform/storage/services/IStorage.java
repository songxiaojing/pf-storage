package com.topsec.bdc.platform.storage.services;

import com.topsec.bdc.platform.core.exception.PlatformException;


/**
 * 
 * Your class summary,end with '.'.
 * 
 * Your class Detail description,end with '.'.
 * 
 * @title IStorage
 * @package com.topsec.bdc.platform.storage.services
 * @author baiyanwei
 * @version
 * @date Jul 23, 2015
 * 
 */
public interface IStorage {

    /**
     * Your Methods description is in here.
     * 
     * @throws PlatformException
     */
    public void initStorage() throws PlatformException;

    /**
     * Your Methods description is in here.
     * 
     * @throws PlatformException
     */
    public void destoryStorage() throws PlatformException;

}
