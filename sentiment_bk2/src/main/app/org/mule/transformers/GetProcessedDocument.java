package org.mule.transformers;
import java.util.*;
import java.io.*;
import com.semantria.Session;
import com.semantria.interfaces.ISerializer;
import com.semantria.serializer.XmlSerializer;
import com.semantria.mapping.Batch;
import com.semantria.mapping.Collection;
import com.semantria.mapping.Document;
import com.semantria.mapping.configuration.stub.*;
import com.semantria.mapping.output.*;
import com.semantria.mapping.output.stub.CollsAnalyticData;
import com.semantria.mapping.output.stub.DocsAnalyticData;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class GetProcessedDocument implements Callable{
	protected String key = "4893b7bb-e973-4d84-aef6-840a5fcc3348";
	protected String secret = "a8e3ed32-5905-4872-829a-0ffd62022150";
	protected String configID = "";
	protected String docID = "";
	protected String response = "";
	
	public String serialize(Object obj)
	{
		String res = null;
		try
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			JAXBContext jc = JAXBContext.newInstance(
					Blacklists.class,
						Categories.class,
						SentimentPhrases.class,
                        			Configurations.class,
						Queries.class,
						UserEntities.class,
						DocAnalyticData.class,
						DocsAnalyticData.class,
						CollAnalyticData.class,
						ServiceStatus.class,
						Subscription.class,
						CollsAnalyticData.class,
						Statistics.class
			);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.marshal(obj, os);
			res = os.toString("UTF-8");

		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return res;
	}

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		LinkedHashMap<String,String> payload = (LinkedHashMap<String,String>)eventContext.getMessage().getPayload();
		docID= payload.get("id");
		ISerializer cserializer = new XmlSerializer();
		Session docSession = Session.createSession(key, secret,cserializer, true);
		DocAnalyticData responseDoc = new DocAnalyticData();
		responseDoc = docSession.getDocument(docID);
		
		response = serialize(responseDoc);
		return response;
	}
	
	
	
}
