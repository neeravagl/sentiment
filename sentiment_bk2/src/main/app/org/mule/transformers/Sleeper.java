package org.mule.transformers;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class Sleeper implements Callable {

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		System.out.println( "about to sleep @ time" + System.currentTimeMillis());
        Thread.sleep(20000);    
        System.out.println ("done sleeping @ time" + System.currentTimeMillis());
        return eventContext.getMessage().getPayload();
	}

}
