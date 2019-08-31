package org.deegree.commons.xml.jaxb;

import java.io.IOException;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

class SchemaVersionCheckerXmlReader implements XMLReader {
    private final XMLReader wrapper;

    private SchemaVersionCheckerXmlReader( XMLReader wrapper ) {
        this.wrapper = wrapper;
    }

    public static XMLReader wrap( XMLReader rdr ) {
        if ( rdr instanceof SchemaVersionCheckerXmlReader ) {
            return rdr;
        } else {
            return new SchemaVersionCheckerXmlReader( rdr );
        }
    }

    public void setContentHandler( ContentHandler handler ) {
        if ( handler instanceof SchemaVersionChecker ) {
            wrapper.setContentHandler( handler );
        } else {
            wrapper.setContentHandler( new SchemaVersionChecker( handler ) );
        }
    }

    public boolean getFeature( String name )
                            throws SAXNotRecognizedException,
                            SAXNotSupportedException {
        return wrapper.getFeature( name );
    }

    public void setFeature( String name, boolean value )
                            throws SAXNotRecognizedException,
                            SAXNotSupportedException {
        wrapper.setFeature( name, value );
    }

    public Object getProperty( String name )
                            throws SAXNotRecognizedException,
                            SAXNotSupportedException {
        return wrapper.getProperty( name );
    }

    public void setProperty( String name, Object value )
                            throws SAXNotRecognizedException,
                            SAXNotSupportedException {
        wrapper.setProperty( name, value );
    }

    public void setEntityResolver( EntityResolver resolver ) {
        wrapper.setEntityResolver( resolver );
    }

    public EntityResolver getEntityResolver() {
        return wrapper.getEntityResolver();
    }

    public void setDTDHandler( DTDHandler handler ) {
        wrapper.setDTDHandler( handler );
    }

    public DTDHandler getDTDHandler() {
        return wrapper.getDTDHandler();
    }

    public ContentHandler getContentHandler() {
        return wrapper.getContentHandler();
    }

    public void setErrorHandler( ErrorHandler handler ) {
        wrapper.setErrorHandler( handler );
    }

    public ErrorHandler getErrorHandler() {
        return wrapper.getErrorHandler();
    }

    public void parse( InputSource input )
                            throws IOException,
                            SAXException {
        wrapper.parse( input );
    }

    public void parse( String systemId )
                            throws IOException,
                            SAXException {
        wrapper.parse( systemId );
    }

}
