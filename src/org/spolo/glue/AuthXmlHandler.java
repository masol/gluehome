package org.spolo.glue;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AuthXmlHandler extends DefaultHandler{
    private String currentQName;
    private HashMap<String, String> map = new HashMap<String, String>();
   @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        currentQName = qName;
    } 
   @Override
    public void characters(char[] ch, int start, int length)
	    throws SAXException {
	String value = new String(ch, start, length);
	if(currentQName!=null){
	    map.put(currentQName, value);
	}
    }

   @Override
    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	super.endElement(uri, localName, qName);
	currentQName = null;
    }

   public HashMap<String, String> getResult(){
       return map;
   }
}
