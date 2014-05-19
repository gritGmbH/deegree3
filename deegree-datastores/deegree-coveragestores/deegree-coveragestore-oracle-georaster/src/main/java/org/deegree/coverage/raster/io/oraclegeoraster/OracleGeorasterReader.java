/*----------------------------------------------------------------------------
 This file is part of deegree
 Copyright (C) 2001-2014 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -
 and
 - grit GmbH -
 and others

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

 e-mail: info@deegree.org
 website: http://www.deegree.org/
----------------------------------------------------------------------------*/

package org.deegree.coverage.raster.io.oraclegeoraster;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import oracle.spatial.georaster.JGeoRaster;
import oracle.spatial.georaster.JGeoRasterMeta;
import oracle.spatial.georaster.image.GeoRasterImage;
import oracle.sql.STRUCT;

import org.deegree.commons.config.ResourceInitException;
import org.deegree.commons.utils.JDBCUtils;
import org.deegree.coverage.persistence.oraclegeoraster.jaxb.AbstractOracleGeorasterType;
import org.deegree.coverage.persistence.oraclegeoraster.jaxb.OracleGeorasterConfig;
import org.deegree.coverage.raster.AbstractRaster;
import org.deegree.coverage.raster.data.container.BufferResult;
import org.deegree.coverage.raster.data.info.BandType;
import org.deegree.coverage.raster.data.info.DataType;
import org.deegree.coverage.raster.data.info.InterleaveType;
import org.deegree.coverage.raster.data.info.RasterDataInfo;
import org.deegree.coverage.raster.data.nio.ByteBufferRasterData;
import org.deegree.coverage.raster.geom.RasterGeoReference;
import org.deegree.coverage.raster.geom.RasterGeoReference.OriginLocation;
import org.deegree.coverage.raster.geom.RasterRect;
import org.deegree.coverage.raster.io.RasterIOOptions;
import org.deegree.coverage.raster.io.RasterReader;
import org.deegree.coverage.raster.utils.RasterFactory;
import org.deegree.cs.coordinatesystems.ICRS;
import org.deegree.cs.persistence.CRSManager;
import org.deegree.db.ConnectionProvider;
import org.deegree.db.ConnectionProviderProvider;
import org.deegree.geometry.Envelope;
import org.deegree.geometry.GeometryFactory;
import org.deegree.workspace.Workspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Oracle GeoRaster Reader
 * 
 * @author <a href="mailto:reichhelm@grit.de">Stephan Reichhelm</a>
 * 
 * @since 3.4
 */
public class OracleGeorasterReader implements RasterReader {

    private static Logger LOG = LoggerFactory.getLogger( OracleGeorasterReader.class );

    private static final Color[] DBG_COLORS = new Color[] { Color.RED, Color.YELLOW, Color.BLUE, Color.CYAN,
                                                           Color.ORANGE };

    private static int DBG_COLOR_CNT = -1;

    private Object LOCK = new Object();

    private Rectangle rasterRect;

    private Envelope envelope;

    private RasterGeoReference rasterReference;

    private String dataLocationId;

    private RasterDataInfo rdi;

    private ICRS crs = null;

    private String jdbcConnId;

    private String rasterTable;

    private String rasterRDTTable;

    private String rasterColumn;

    private int rasterId;

    private int bandR = -1;

    private int bandG = -1;

    private int bandB = -1;

    // private byte[] nodata = null;

    private int level;

    private int maxLevel;

    private Workspace workspace;

    private int debug = 0;

    private OracleGeorasterReader( Workspace workspace, OracleGeorasterReader base, int lvl ) {
        this.workspace = workspace;
        this.crs = base.crs;
        this.jdbcConnId = base.jdbcConnId;
        this.rasterId = base.rasterId;
        this.rasterTable = base.rasterTable;
        this.rasterRDTTable = base.rasterRDTTable;
        this.rasterColumn = base.rasterColumn;

        this.bandR = base.bandR;
        this.bandG = base.bandG;
        this.bandB = base.bandB;
        this.maxLevel = base.maxLevel;
        this.level = lvl;
        this.dataLocationId = base.dataLocationId + "." + lvl;
        this.envelope = base.envelope;
        this.debug = base.debug;

        double sc = Math.pow( 2.0d, lvl );
        int lvlCurWidth = (int) ( ( (double) base.rasterRect.width ) / sc );
        int lvlCurHeight = (int) ( ( (double) base.rasterRect.height ) / sc );

        rasterRect = new Rectangle( 0, 0, lvlCurWidth, lvlCurHeight );
        rasterReference = RasterGeoReference.create( OriginLocation.OUTER, envelope, lvlCurWidth, lvlCurHeight );
    }

    public OracleGeorasterReader( Workspace workspace, OracleGeorasterConfig config ) throws IOException {
        this.workspace = workspace;
        this.jdbcConnId = config.getJDBCConnId();
        AbstractOracleGeorasterType.Raster raster = config.getRaster();
        this.rasterTable = raster.getTable() != null ? raster.getTable() : "DEFAULT_TABLE";
        this.rasterRDTTable = raster.getRDTTable() != null ? raster.getRDTTable() : "DEFAULT_RDT_TABLE";
        this.rasterColumn = raster.getColumn() != null ? raster.getColumn() : "DEFAULT_COLUMN";
        this.rasterId = raster.getId();

        StringBuilder id = new StringBuilder();
        id.append( "http://localhost/oraclegeorasterid/" ).append( this.jdbcConnId );
        id.append( "/" ).append( this.rasterTable ).append( "/" ).append( this.rasterRDTTable );
        id.append( "/" ).append( this.rasterRDTTable ).append( "/" ).append( this.rasterColumn );
        id.append( "/" ).append( this.rasterId ).append( ".dat" );
        this.dataLocationId = id.toString();

        if ( config.getBands() != null ) {
            if ( config.getBands().getSingle() != null ) {
                int val = config.getBands().getSingle().intValue();
                this.bandR = val;
                this.bandG = val;
                this.bandB = val;
            } else if ( config.getBands().getRGB() != null ) {
                this.bandR = config.getBands().getRGB().getRed();
                this.bandG = config.getBands().getRGB().getGreen();
                this.bandB = config.getBands().getRGB().getBlue();
            }

            // if ( config.getBands().getNodata() != null ) {
            // try {
            // ARGB
            // band = new byte[] { (byte) ( i >> 32 & 0xFF ), (byte) ( i >> 16 & 0xFF ), (byte) ( i >> 8 & 0xFF ),
            // (byte) ( i & 0xFF ) };
            // RGB
            // band = new byte[] { (byte) ( i >> 16 & 0xFF ), (byte) ( i >> 8 & 0xFF ), (byte) ( i & 0xFF ) };
            // 255, 0, 0, 0
            // nodata = new byte[] { (byte) ( 255 & 0xFF ), (byte) ( 0 & 0xFF ), (byte) ( 0 & 0xFF ), (byte) ( 0 & 0xFF
            // ) };
            // } catch ( Exception ex ) {
            // throw new IOException( "Failed to parse nodata value", ex );
            // }
            // }
        }

        if ( config.getStorageCRS() != null && config.getStorageCRS().trim().length() > 0 ) {
            this.crs = CRSManager.getCRSRef( config.getStorageCRS() );
        }

        // if ( )

        double[] overrideStorageBBox = null;
        if ( config.getStorageBBox() != null ) {
            List<Double> ll = config.getStorageBBox().getLowerCorner();
            List<Double> ur = config.getStorageBBox().getUpperCorner();
            if ( ll != null && ur != null && ll.size() > 1 && ur.size() > 1 ) {
                try {
                    double[] dbllst = new double[4];
                    dbllst[0] = ll.get( 0 ).doubleValue();
                    dbllst[1] = ll.get( 1 ).doubleValue();
                    dbllst[2] = ur.get( 0 ).doubleValue();
                    dbllst[3] = ur.get( 1 ).doubleValue();
                    overrideStorageBBox = dbllst;
                } catch ( Exception ex ) {
                    LOG.warn( "Failed loading overrideStorageBBox", ex );
                }
            }
        }

        if ( config.getDebug() != null ) {
            this.debug = config.getDebug().intValue();
        }

        /*
         * Load information from database
         */
        double[] mbr = null;
        Connection con = null;
        String bboxSrc = "data";
        ConnectionProvider connProvider;
        try {
            connProvider = workspace.getResource( ConnectionProviderProvider.class, jdbcConnId );

            JGeoRaster jGeoRaster = getGeoraster( connProvider.getConnection() );
            JGeoRasterMeta jGeoRasterMeta = jGeoRaster.getMetadataObject();

            int ultRow = jGeoRasterMeta.getRasterInfo().getULTCoordinate( 1 ).intValue();
            int ultCol = jGeoRasterMeta.getRasterInfo().getULTCoordinate( 0 ).intValue();
            if ( ultRow != 0 || ultCol != 0 ) {
                LOG.info( "Raster {}.{}:{} ULTCoordinate: row {} col {} currently ignored",
                          new Object[] { this.rasterTable, this.rasterColumn, this.rasterId, ultRow, ultCol } );
            }
            
            int lvl0Width = jGeoRasterMeta.getRasterInfo().getDimensionSize( 1 ).intValue();
            int lvl0Height = jGeoRasterMeta.getRasterInfo().getDimensionSize( 0 ).intValue();

            rasterRect = new Rectangle( 0, 0, lvl0Width, lvl0Height );

            if ( overrideStorageBBox != null && overrideStorageBBox.length == 4 ) {
                mbr = overrideStorageBBox;
                calculateEnvelopeAndReference( mbr, lvl0Width, lvl0Height, true );
                bboxSrc = "conf";
            } else {
                try {
                    // TRICKY SpatialReferenceInfo is also missing if georaster object is not spatial referenced
                    //
                    String modellocation = jGeoRasterMeta.getSpatialReferenceInfo().getModelCoordinateLocation();
                    mbr = jGeoRaster.getSpatialExtent().getMBR();

                    calculateEnvelopeAndReference( mbr, lvl0Width, lvl0Height,
                                                   "upperleft".equalsIgnoreCase( modellocation ) );
                } catch ( NullPointerException npe ) {
                    throw new ResourceInitException(
                                                     "Coverage has no Spatial extend, either configure bbox in config or correct spatial extent in database !" );
                }
            }

            level = 0;
            Long objMaxLevel = jGeoRasterMeta.getRasterInfo().getPyramidMaxLevel();
            if ( objMaxLevel != null )
                maxLevel = objMaxLevel.intValue();
            else
                maxLevel = 0;

            if ( mbr != null && mbr.length > 3 ) {
                LOG.info( "Raster {}.{}:{} Size: {}x{} Levels:  0 - {} BBOX: {} {} - {} {} (Source: {})",
                          new Object[] { this.rasterTable, this.rasterColumn, this.rasterId, lvl0Width, lvl0Height,
                                        maxLevel, mbr[0], mbr[1], mbr[2], mbr[3], bboxSrc } );
            } else {
                LOG.info( "Raster {}.{}:{} Size: {}x{} Levels:  0 - {} BBOX: empty (Source: {})",
                          new Object[] { this.rasterTable, this.rasterColumn, this.rasterId, lvl0Width, lvl0Height,
                                        maxLevel, bboxSrc } );
            }

        } catch ( IOException ioe ) {
            LOG.warn( "Raster {}.{}:{} failed (error see next log entry)", new Object[] { this.rasterTable,
                                                                                         this.rasterColumn,
                                                                                         this.rasterId } );
            throw ioe;
        } catch ( Exception othere ) {
            LOG.warn( "Raster {}.{}:{} failed (error see next log entry)", new Object[] { this.rasterTable,
                                                                                         this.rasterColumn,
                                                                                         this.rasterId } );
            throw new IOException( othere );
        } finally {
            JDBCUtils.close( con );
        }
    }

    private void calculateEnvelopeAndReference( double[] mbr, int width, int height, boolean isouter ) {
        MathContext mc = MathContext.DECIMAL128;
        BigDecimal min0 = BigDecimal.valueOf( mbr[0] ).min( BigDecimal.valueOf( mbr[2] ) );
        BigDecimal min1 = BigDecimal.valueOf( mbr[1] ).min( BigDecimal.valueOf( mbr[3] ) );
        BigDecimal max0 = BigDecimal.valueOf( mbr[0] ).max( BigDecimal.valueOf( mbr[2] ) );
        BigDecimal max1 = BigDecimal.valueOf( mbr[1] ).max( BigDecimal.valueOf( mbr[3] ) );

        BigDecimal span0 = max0.subtract( min0, mc );
        BigDecimal span1 = max1.subtract( min1, mc );

        if ( !isouter ) {
            BigDecimal two = new BigDecimal( 2 );
            BigDecimal half0 = span0.divide( new BigDecimal( width ), mc ).divide( two, mc ).abs( mc );
            BigDecimal half1 = span1.divide( new BigDecimal( height ), mc ).divide( two, mc ).abs( mc );

            min0 = min0.add( half0, mc );
            min1 = min1.subtract( half1, mc );
            max0 = max0.add( half0, mc );
            max1 = max1.subtract( half1, mc );
        }

        envelope = ( new GeometryFactory() ).createEnvelope( min0.doubleValue(), min1.doubleValue(),
                                                             max0.doubleValue(), max1.doubleValue(), crs );

        rasterReference = RasterGeoReference.create( OriginLocation.OUTER, envelope, width, height );
    }

    public AbstractRaster getRaster() {
        RasterDataInfo rdi = getRasterDataInfo();
        // RasterIOOptions options = new RasterIOOptions();
        // options.setNoData(nodata);
        // return RasterFactory.createEmptyRaster( rdi, envelope, rasterReference, this, false, options );
        return RasterFactory.createEmptyRaster( rdi, envelope, rasterReference, this, false, null );
    }

    private JGeoRaster getGeoraster( Connection con )
                            throws IOException {
        JGeoRaster res = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append( "SELECT " ).append( this.rasterColumn );
            sb.append( " FROM " ).append( this.rasterTable );
            sb.append( " a WHERE a." ).append( this.rasterColumn );
            sb.append( ".rasterid = ? AND a." ).append( this.rasterColumn );
            sb.append( ".rasterdatatable = ?" );
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( 1, this.rasterId );
            ps.setString( 2, this.rasterRDTTable.toUpperCase() );

            rs = ps.executeQuery();

            if ( !rs.next() )
                throw new SQLException( "No GeoRaster object exists at rasterid = " + this.rasterId + ", RDT = "
                                        + this.rasterRDTTable );

            STRUCT struct = (STRUCT) rs.getObject( 1 );
            res = new JGeoRaster( struct );
        } catch ( Exception ex ) {
            throw new IOException( ex.getMessage() );
        } finally {
            JDBCUtils.close( rs );
            JDBCUtils.close( ps );
        }

        return res;
    }

    @Override
    public BufferResult read( RasterRect rect, ByteBuffer result )
                            throws IOException {
        Rectangle intersect = rasterRect.intersection( new Rectangle( rect.x, rect.y, rect.width, rect.height ) );

        if ( LOG.isDebugEnabled() ) {
            LOG.debug( "Reading Rasterdata from {}", this.dataLocationId );
            LOG.debug( " Inters X = {}", intersect.x );
            LOG.debug( " Inters Y = {}", intersect.y );
            LOG.debug( " Inters W = {}", intersect.width );
            LOG.debug( " Inters H = {}", intersect.height );
        }

        if ( intersect.width == 0 || intersect.height == 0 )
            return null;

        if ( LOG.isDebugEnabled() ) {
            LOG.debug( " Raster X = {}", rasterRect.x );
            LOG.debug( " Raster Y = {}", rasterRect.y );
            LOG.debug( " Raster W = {}", rasterRect.width );
            LOG.debug( " Raster H = {}", rasterRect.height );

            LOG.debug( " Rect   X = {}", rect.x );
            LOG.debug( " Rect   Y = {}", rect.y );
            LOG.debug( " Rect   W = {}", rect.width );
            LOG.debug( " Rect   H = {}", rect.height );

            LOG.debug( " Level    = {} ( max = {} )", level, maxLevel );
        }

        Connection con = null;
        BufferResult res = null;
        RenderedImage img = null;

        ConnectionProvider connProvider;
        try {
            connProvider = workspace.getResource( ConnectionProviderProvider.class, jdbcConnId );
            con = connProvider.getConnection();

            JGeoRaster jGeoRaster = getGeoraster( con );
            GeoRasterImage geoRasterImg = jGeoRaster.getGeoRasterImageObject();

            if ( this.bandR > 0 && this.bandG > 0 && this.bandG > 0 ) {
                geoRasterImg.setRed( this.bandR );
                geoRasterImg.setGreen( this.bandG );
                geoRasterImg.setBlue( this.bandB );
            }

            long[] outWindow = new long[4];
            img = geoRasterImg.getRasterImage( level, (long) intersect.y, (long) intersect.x,
                                               (long) ( intersect.y + intersect.height - 1 ),
                                               (long) ( intersect.x + intersect.width - 1 ), outWindow );

            BufferedImage bimg;
            Graphics2D bg = null;

            if ( img == null ) {
                //
                // TRICKY Look into pyramid level below or above if current raster block is corrupted in database
                //
                LOG.warn( "*** Loading Raster from {}:{}/{}.{}={} for level {} failed in the range of {}-{} / {}-{}",
                          new Object[] { this.jdbcConnId, this.rasterTable, this.rasterRDTTable, this.rasterColumn,
                                        this.rasterId, level, intersect.y, intersect.x,
                                        ( intersect.y + intersect.height - 1 ), ( intersect.x + intersect.width - 1 ) } );

                int newlevel;
                long nx, ny, nw, nh;
                if ( level <= 0 ) {
                    newlevel = 1;
                    nx = intersect.x / 2;
                    ny = intersect.y / 2;
                    nw = intersect.width / 2;
                    nh = intersect.height / 2;
                } else {
                    newlevel = level - 1;
                    nx = intersect.x * 2;
                    ny = intersect.y * 2;
                    nw = intersect.width * 2;
                    nh = intersect.height * 2;
                }

                img = geoRasterImg.getRasterImage( newlevel, ny, nx, ny + nh - 1, nx + nw - 1, outWindow );

                bimg = getBufferedImage( intersect.width, intersect.height );
                bg = bimg.createGraphics();

                bg.drawImage( (Image) img, 0, 0, intersect.width, intersect.height, null );
            } else {
                LOG.debug( " Image oW = {}, {}, {}, {}", outWindow );

                bimg = getBufferedImage( img.getWidth(), img.getHeight() );
                bg = bimg.createGraphics();
                bg.drawImage( (Image) img, 0, 0, null );
            }

            if ( bg != null && this.debug > 0 ) {
                LOG.warn( "Rendering additional debug graphics on top of raster data ({})", this.debug );
                if ( this.debug == 1 ) {
                    bg.setColor( DBG_NEXT_COLOR() );
                    // bg.setStroke( new java.awt.BasicStroke( 3 ) );
                    // bg.drawRect( 1, 1, bimg.getWidth() - 3, bimg.getHeight() - 3 );
                    bg.setStroke( new java.awt.BasicStroke( 1 ) );
                    bg.drawRect( 0, 0, bimg.getWidth() - 1, bimg.getHeight() - 1 );
                } else if ( this.debug == 2 ) {
                    bg.setColor( DBG_NEXT_COLOR() );
                    bg.setStroke( new java.awt.BasicStroke( 1 ) );
                    bg.drawRect( 0, 0, 0, 0 );
                    bg.drawRect( bimg.getWidth() - 1, bimg.getHeight() - 1, 0, 0 );
                    bg.drawRect( bimg.getWidth() - 1, 0, 0, 0 );
                    bg.drawRect( 0, bimg.getHeight() - 1, 0, 0 );
                }
            }

            bg.dispose();
            img = bimg;

            if ( LOG.isDebugEnabled() ) {
                LOG.debug( " Image  W = ", img.getWidth() );
                LOG.debug( " Image  H = ", img.getHeight() );
            }

            ByteBufferRasterData rd = RasterFactory.rasterDataFromImage( img, null, result );
            res = new BufferResult( rd.getView(), result );
        } catch ( Exception ex ) {
            // ignore
            LOG.info( "Exception on Loading", ex );
        } finally {
            JDBCUtils.close( con );
        }

        return res;
    }

    private static synchronized Color DBG_NEXT_COLOR() {
        DBG_COLOR_CNT++;
        if ( DBG_COLOR_CNT >= DBG_COLORS.length )
            DBG_COLOR_CNT = 0;

        return DBG_COLORS[DBG_COLOR_CNT];
    }

    @Override
    public AbstractRaster load( File filename, RasterIOOptions options )
                            throws IOException {
        throw new IOException( "Reading GeoRaster from File is not supported" );
    }

    @Override
    public AbstractRaster load( InputStream stream, RasterIOOptions options )
                            throws IOException {
        throw new IOException( "Reading GeoRaster from InputStream is not supported" );
    }

    @Override
    public boolean canLoad( File filename ) {
        return false;
    }

    @Override
    public Set<String> getSupportedFormats() {
        return Collections.emptySet();
    }

    @Override
    public boolean shouldCreateCacheFile() {
        return false;
    }

    @Override
    public File file() {
        // no file
        return null;
    }

    @Override
    public int getHeight() {
        return rasterRect.height;
    }

    @Override
    public int getWidth() {
        return rasterRect.width;
    }

    @Override
    public RasterGeoReference getGeoReference() {
        return rasterReference;
    }

    private BufferedImage getBufferedImage( int w, int h ) {
        return new BufferedImage( w, h, BufferedImage.TYPE_4BYTE_ABGR );
    }

    @Override
    public RasterDataInfo getRasterDataInfo() {

        synchronized ( LOCK ) {
            if ( rdi == null ) {
                //
                // Fixed RDI
                //
                rdi = new RasterDataInfo(
                                          new BandType[] { BandType.ALPHA, BandType.BLUE, BandType.GREEN, BandType.RED },
                                          DataType.BYTE, InterleaveType.PIXEL );
            }
        }

        return rdi;
    }

    @Override
    public boolean canReadTiles() {
        return true;
    }

    @Override
    public String getDataLocationId() {
        return dataLocationId;
    }

    @Override
    public void dispose() {

    }

    public boolean isMultiResulution() {
        return maxLevel > 0;
    }

    /**
     * Return full Pyramid as array of AbstractRaster elements
     * 
     * @return
     */
    public AbstractRaster[] getPyramidRaster() {
        if ( level != 0 )
            return null;

        AbstractRaster[] res = new AbstractRaster[maxLevel];
        for ( int lvl = 1; lvl <= maxLevel; lvl++ ) {
            res[lvl - 1] = new OracleGeorasterReader( workspace, this, lvl ).getRaster();
        }
        return res;
    }
}
