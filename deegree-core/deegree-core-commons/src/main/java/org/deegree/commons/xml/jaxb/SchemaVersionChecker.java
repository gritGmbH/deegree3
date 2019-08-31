package org.deegree.commons.xml.jaxb;

import static org.deegree.commons.xml.CommonNamespaces.XSINS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.deegree.commons.tom.ows.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

class SchemaVersionChecker implements ContentHandler {

    private static final Logger LOG = LoggerFactory.getLogger( SchemaVersionChecker.class );

    private static final String SCHEMA_LOCATION = "schemaLocation";

    // Regexp to locate version numbers
    private static final String VERSION_PATTERN = "schemas\\.deegree\\.org.*\\/(\\d+(?:\\.\\d+)+).+\\.xsd";

    private Version minVersion = new Version( 3, 4, 0 );

    private final ContentHandler wrapper;

    protected SchemaVersionChecker( ContentHandler wrapper ) {
        this.wrapper = wrapper;
    }

    public void startElement( String uri, String localName, String qName, Attributes atts )
                            throws SAXException {
        wrapper.startElement( uri, localName, qName, atts );
        int schemaLoc = atts.getIndex( XSINS, SCHEMA_LOCATION );
        String schemaUrls = schemaLoc != -1 ? atts.getValue( schemaLoc ) : null;

        if ( schemaUrls != null ) {
            Matcher m = Pattern.compile( VERSION_PATTERN ).matcher( schemaUrls );
            while ( m.find() ) {
                Version fileSchemaVersion = Version.parseVersion( m.group( 1 ) );
                if ( minVersion.compareTo( fileSchemaVersion ) > 0 ) {
                    LOG.warn( "" );
                    LOG.warn( "*** Configuration uses an outdated Version please Upgrade refrence {}", m.group( 0 ) );
                    LOG.warn( "" );
                }
            }
        }
    }

    public void startPrefixMapping( String prefix, String uri )
                            throws SAXException {
        wrapper.startPrefixMapping( prefix, uri );
    }

    public void setDocumentLocator( Locator locator ) {
        wrapper.setDocumentLocator( locator );
    }

    public void startDocument()
                            throws SAXException {
        wrapper.startDocument();
    }

    public void endDocument()
                            throws SAXException {
        wrapper.endDocument();
    }

    public void endPrefixMapping( String prefix )
                            throws SAXException {
        wrapper.endPrefixMapping( prefix );
    }

    public void endElement( String uri, String localName, String qName )
                            throws SAXException {
        wrapper.endElement( uri, localName, qName );
    }

    public void characters( char[] ch, int start, int length )
                            throws SAXException {
        wrapper.characters( ch, start, length );
    }

    public void ignorableWhitespace( char[] ch, int start, int length )
                            throws SAXException {
        wrapper.ignorableWhitespace( ch, start, length );
    }

    public void processingInstruction( String target, String data )
                            throws SAXException {
        wrapper.processingInstruction( target, data );
    }

    public void skippedEntity( String name )
                            throws SAXException {
        wrapper.skippedEntity( name );
    }
}
