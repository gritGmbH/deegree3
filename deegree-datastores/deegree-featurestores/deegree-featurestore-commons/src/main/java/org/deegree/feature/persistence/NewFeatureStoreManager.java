//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2012 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -
 and
 - Occam Labs UG (haftungsbeschränkt) -

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

 Occam Labs UG (haftungsbeschränkt)
 Godesberger Allee 139, 53175 Bonn
 Germany

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.feature.persistence;

import java.io.File;
import java.io.IOException;

import org.deegree.feature.persistence.cache.BBoxCache;
import org.deegree.feature.persistence.cache.BBoxPropertiesCache;
import org.deegree.workspace.Workspace;
import org.deegree.workspace.standard.DefaultResourceManager;
import org.deegree.workspace.standard.DefaultResourceManagerMetadata;
import org.deegree.workspace.standard.DefaultWorkspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO add class documentation here
 * 
 * @author <a href="mailto:schmitz@occamlabs.de">Andreas Schmitz</a>
 * @author last edited by: $Author: stranger $
 * 
 * @version $Revision: $, $Date: $
 */
public class NewFeatureStoreManager extends DefaultResourceManager<FeatureStore> {

    private static Logger LOG = LoggerFactory.getLogger( NewFeatureStoreManager.class );

    private static final String BBOX_CACHE_FILE = "bbox_cache.properties";

    private BBoxPropertiesCache bboxCache;

    public NewFeatureStoreManager() {
        super( new DefaultResourceManagerMetadata<FeatureStore>( NewFeatureStoreProvider.class, "feature stores",
                                                                 "datasources/feature" ) );
    }

    @Override
    public void find( Workspace workspace ) {
        try {
            if ( workspace instanceof DefaultWorkspace ) {
                File dir = new File( ( (DefaultWorkspace) workspace ).getLocation(), getMetadata().getWorkspacePath() );
                bboxCache = new BBoxPropertiesCache( new File( dir, BBOX_CACHE_FILE ) );
            }
            // else?
        } catch ( IOException e ) {
            LOG.error( "Unable to initialize global envelope cache: " + e.getMessage(), e );
        }

        super.find( workspace );
    }

    public BBoxCache getBBoxCache() {
        return bboxCache;
    }

}
