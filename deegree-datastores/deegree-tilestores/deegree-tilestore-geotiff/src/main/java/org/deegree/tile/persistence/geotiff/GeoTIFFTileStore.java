//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2010 by:
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

 Occam Labs UG (haftungsbeschränkt)
 Godesberger Allee 139, 53175 Bonn
 Germany
 http://www.occamlabs.de/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.tile.persistence.geotiff;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.deegree.commons.config.DeegreeWorkspace;
import org.deegree.commons.config.ResourceInitException;
import org.deegree.coverage.raster.geom.RasterGeoReference;
import org.deegree.coverage.raster.io.imageio.geotiff.GeoTiffIIOMetadataAdapter;
import org.deegree.cs.coordinatesystems.ICRS;
import org.deegree.cs.exceptions.UnknownCRSException;
import org.deegree.cs.persistence.CRSManager;
import org.deegree.geometry.Envelope;
import org.deegree.tile.Tile;
import org.deegree.tile.persistence.TileStore;
import org.slf4j.Logger;

/**
 * <code>GeoTIFFTileStore</code>
 * 
 * @author <a href="mailto:schmitz@occamlabs.de">Andreas Schmitz</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 31882 $, $Date: 2011-09-15 02:05:04 +0200 (Thu, 15 Sep 2011) $
 */

public class GeoTIFFTileStore implements TileStore {

    private static final Logger LOG = getLogger( GeoTIFFTileStore.class );

    private final ImageReader reader;

    private final File file;

    private final String crs;

    public GeoTIFFTileStore( ImageReader reader, File file, String crs ) {
        this.reader = reader;
        this.file = file;
        this.crs = crs;
    }

    @Override
    public void init( DeegreeWorkspace workspace )
                            throws ResourceInitException {
        try {
            ICRS crs = null;
            if ( this.crs != null ) {
                crs = CRSManager.getCRSRef( this.crs );
            }

            ImageInputStream iis = ImageIO.createImageInputStream( file );
            reader.setInput( iis );
            int num = reader.getNumImages( true );
            IIOMetadata md = reader.getImageMetadata( 0 );
            Envelope envelope = getCRS( md, reader.getWidth( 0 ), reader.getHeight( 0 ), crs );

            if ( envelope == null ) {
                throw new ResourceInitException( "No envelope information could be read from GeoTIFF, "
                                                 + "and none was configured. Please configure an"
                                                 + " envelope or add one to the GeoTIFF." );
            }

            LOG.debug( "Envelope from GeoTIFF was {}.", envelope );

            for ( int i = 0; i < num; ++i ) {
                int tw = reader.getTileWidth( i );
                int th = reader.getTileHeight( i );
                int width = reader.getWidth( i );
                int height = reader.getHeight( i );
                int numx = (int) Math.ceil( (double) width / (double) tw );
                int numy = (int) Math.ceil( (double) height / (double) th );
                LOG.debug( "Level {} has {}x{} tiles of {}x{} pixels.", new Object[] { i, numx, numy, tw, th } );
            }

            iis.close();

        } catch ( Throwable e ) {
            throw new ResourceInitException( "Unable to create tile store.", e );
        }
    }

    @Override
    public void destroy() {
        // nothing to do
    }

    @Override
    public List<Tile> getTiles( Envelope envelope, double resolution ) {

        return null;
    }

    private static Envelope getCRS( IIOMetadata metaData, int width, int height, ICRS crs )
                            throws ResourceInitException {
        GeoTiffIIOMetadataAdapter geoTIFFMetaData = new GeoTiffIIOMetadataAdapter( metaData );
        try {
            int modelType = Integer.valueOf( geoTIFFMetaData.getGeoKey( GeoTiffIIOMetadataAdapter.GTModelTypeGeoKey ) );
            if ( crs == null ) {
                String epsgCode = null;
                if ( modelType == GeoTiffIIOMetadataAdapter.ModelTypeProjected ) {
                    epsgCode = geoTIFFMetaData.getGeoKey( GeoTiffIIOMetadataAdapter.ProjectedCSTypeGeoKey );
                } else if ( modelType == GeoTiffIIOMetadataAdapter.ModelTypeGeographic ) {
                    epsgCode = geoTIFFMetaData.getGeoKey( GeoTiffIIOMetadataAdapter.GeographicTypeGeoKey );
                }
                if ( epsgCode != null && epsgCode.length() != 0 ) {
                    try {
                        crs = CRSManager.lookup( "EPSG:" + epsgCode );
                    } catch ( UnknownCRSException e ) {
                        LOG.error( "No coordinate system found for EPSG:" + epsgCode );
                    }
                }
            }
            if ( crs == null ) {
                throw new ResourceInitException( "No CRS information could be read from GeoTIFF, "
                                                 + "and none was configured. Please configure a"
                                                 + " CRS or add one to the GeoTIFF." );
            }

            double[] tiePoints = geoTIFFMetaData.getModelTiePoints();
            double[] scale = geoTIFFMetaData.getModelPixelScales();
            if ( tiePoints != null && scale != null ) {

                RasterGeoReference rasterReference;
                if ( Math.abs( scale[0] - 0.5 ) < 0.001 ) { // when first pixel tie point is 0.5 -> center type
                    // rb: this might not always be right, see examples at
                    // http://www.remotesensing.org/geotiff/spec/geotiff3.html#3.2.1.
                    // search for PixelIsArea/PixelIsPoint to determine center/outer
                    rasterReference = new RasterGeoReference( RasterGeoReference.OriginLocation.CENTER, scale[0],
                                                              -scale[1], tiePoints[3], tiePoints[4], crs );
                } else {
                    rasterReference = new RasterGeoReference( RasterGeoReference.OriginLocation.OUTER, scale[0],
                                                              -scale[1], tiePoints[3], tiePoints[4], crs );
                }
                return rasterReference.getEnvelope( width, height, crs );
            }

        } catch ( UnsupportedOperationException ex ) {
            LOG.debug( "couldn't read crs information in GeoTIFF" );
        }
        return null;
    }

}