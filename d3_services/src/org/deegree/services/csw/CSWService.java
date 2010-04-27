//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2009 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.services.csw;

import static org.deegree.services.i18n.Messages.get;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.deegree.commons.datasource.configuration.RecordStoreType;
import org.deegree.record.persistence.RecordStore;
import org.deegree.record.persistence.RecordStoreException;
import org.deegree.record.persistence.RecordStoreManager;
import org.deegree.services.csw.configuration.ServiceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specifies the Service-Layer. <br>
 * Initial class to register all configured {@link RecordStore}s that are specified in the Service-Configuration.
 * 
 * 
 * @author <a href="mailto:thomas@deegree.org">Steffen Thomas</a>
 * @author last edited by: $Author: thomas $
 * 
 * @version $Revision: $, $Date: $
 */
public class CSWService {

    private final Set<RecordStore> recordStore = new HashSet<RecordStore>();

    private boolean inspire;

    private boolean fileIdentifierAvailable;

    private static final Logger LOG = LoggerFactory.getLogger( CSWService.class );

    /**
     * Creates a {@link CSWService} instance to get a binding to the configuration.
     * 
     * @param sc
     *            the serviceConfiguration that is specified in the configuration.xml document
     * @throws RecordStoreException
     */
    public CSWService( ServiceConfiguration sc ) throws RecordStoreException {

        LOG.info( "Initializing/looking up configured record stores." );

        for ( JAXBElement<? extends RecordStoreType> obje : sc.getRecordStore() ) {

            RecordStoreType storeType = obje.getValue();

            RecordStore recStore = RecordStoreManager.create( storeType );

            addToStore( recStore );

        }

        /*
         * If the flag Inspire is set up
         */
        if ( sc.isInspire() == null ) {
            sc.setInspire( false );
            this.inspire = sc.isInspire();
        } else {
            this.inspire = sc.isInspire();
        }

        /*
         * If there is no need for a fileIdentifier. ISO defines [0..1]; DC defines [1..*]
         */
        if ( sc.isFileIdentifierAvailable() == null ) {
            sc.setFileIdentifierAvailable( false );
            this.fileIdentifierAvailable = sc.isFileIdentifierAvailable();
        } else {
            this.fileIdentifierAvailable = sc.isFileIdentifierAvailable();
        }
    }

    /**
     * Registers a new {@link RecordStore} to the CSW.
     * 
     * @param rs
     *            store to be registered
     */
    public void addToStore( RecordStore rs ) {
        synchronized ( this ) {
            if ( recordStore.contains( rs ) ) {
                String msg = get( "CSW_RECORDSTORE_ALREADY_REGISTERED", rs );
                LOG.error( msg );
                throw new IllegalArgumentException( msg );
            }

            recordStore.add( rs );
        }
    }

    /**
     * Unregisters the specified {@link RecordStore} from the CSW.
     * 
     * @param rs
     *            store to be registered
     */
    public void removeStore( RecordStore rs ) {
        synchronized ( this ) {
            // TODO
        }
    }

    /**
     * Question after a {@link RecordStore} that is available that maps to the requested typeName. <br>
     * If there is no matching between the parameter typeName and any {@link RecordStore} there is nothing to be
     * returned. Otherwise the requested {@link RecordStore} should return.
     * 
     * <p>
     * TODO's:
     * <p>
     * <b>mappingWithoutTypeName</b> -> if there is a mapping required without a typeName attribute<br>
     * <b>mappingForCommonQueryableProperties</b> -> if there is a mapping required for common queryable properties
     * without a typeName attribute
     * <p>
     * At the moment there is everytime a matching!!!<br>
     * 
     * @param qName
     *            the characteristic typeName for a specific record
     * @return {@link RecordStore}
     */
    public RecordStore getRecordStore( QName qName ) {
        QName mappingWithoutTypeName = new QName( qName.getNamespaceURI(), "", qName.getPrefix() );
        QName mappingForCommonQueryableProperties = new QName( "", "", "" );
        for ( RecordStore rs : recordStore ) {
            for ( QName recordstoreTypeName : rs.getTypeNames().keySet() ) {
                if ( qName.equals( recordstoreTypeName ) || mappingWithoutTypeName.equals( recordstoreTypeName )
                     || mappingForCommonQueryableProperties.equals( recordstoreTypeName ) ) {

                    return rs;
                }
            }
        }

        return null;
    }

    /**
     * 
     * @return Set of type <Code>RecordStore<Code>
     */
    public Set<RecordStore> getRecordStore() {
        return recordStore;
    }

    /**
     * 
     * If the record should correspond to the INSPIRE Technical Guideline (MD_IR_and_ISO_20090218) which is mandatory in
     * Europe, this parameter is set to "true". <br>
     * Otherwise it is set to "false" which should be configured in the configuration-file explicitly.
     * 
     * @return the inspire
     */
    public boolean isInspire() {
        return inspire;
    }

    /**
     * If this is TRUE then a "fileIdentifier" should be set in the catalog record. <br>
     * else the CSW is responsible to generate one.
     * 
     * @return true or false
     */
    public boolean isFileIdentifierAvailable() {
        return fileIdentifierAvailable;
    }

}
