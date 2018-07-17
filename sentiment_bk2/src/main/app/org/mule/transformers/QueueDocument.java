package org.mule.transformers;
import org.mule.api.MuleEventContext;

import org.mule.api.lifecycle.Callable;

import java.util.*;

import com.semantria.serializer.XmlSerializer;
import com.semantria.interfaces.ISerializer;
import com.semantria.Session;
import com.semantria.CallbackHandler;
import java.util.UUID;
import com.semantria.mapping.Document;



public class QueueDocument implements Callable {
	protected String key = "4893b7bb-e973-4d84-aef6-840a5fcc3348";
	protected String secret = "a8e3ed32-5905-4872-829a-0ffd62022150";
	protected String configID = "";
	protected String docText = "";
	protected String docID = "";
	protected int response = 0;
	
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		
		LinkedHashMap<String,String> payload = (LinkedHashMap<String,String>)eventContext.getMessage().getPayload();
		docID= payload.get("id");
		docText = payload.get("description");
		ISerializer cserializer = new XmlSerializer();
		Session session = Session.createSession(key, secret,cserializer, true);
		session.setCallbackHandler(new CallbackHandler());
			String uid;
			if(docID.length()>0){
				uid=docID;
			}
			else{
				 uid = UUID.randomUUID().toString();
			}
				// Creates a sample document which need to be processed on Semantria
				// Queues document for processing on Semantria service
				response = session.queueDocument( new Document( uid, docText ));
				if( response == 202)
				{
					docID = uid;
					System.out.println("\"" + uid + "\" document queued succsessfully.");
				}
				eventContext.getMessage().setInvocationProperty("responseCode", response);
				eventContext.getMessage().setInvocationProperty("docID", docID);
        return eventContext.getMessage().getPayload();
	
	}

}
