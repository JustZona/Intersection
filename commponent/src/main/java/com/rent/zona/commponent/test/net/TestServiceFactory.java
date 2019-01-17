package com.rent.zona.commponent.test.net;

import com.rent.zona.baselib.network.service.ServiceFactory;
import com.rent.zona.baselib.network.service.UserService;

public class TestServiceFactory {
    private static TestServiceFactory sInstance;

    private TestService mTestService;
    private TestServiceFactory() {
    }
    public static TestServiceFactory getInstance() {
        if (sInstance == null) {
            synchronized (TestServiceFactory.class) {
                sInstance = new TestServiceFactory();
            }
        }
        return sInstance;
    }

    public TestService testService() {
        if(mTestService==null){
            mTestService=ServiceFactory.getInstance().forService(TestService.class);
        }
        return mTestService;
    }
}
