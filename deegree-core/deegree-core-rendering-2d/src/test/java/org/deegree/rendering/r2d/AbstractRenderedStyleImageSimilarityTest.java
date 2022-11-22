package org.deegree.rendering.r2d;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.System.currentTimeMillis;
import static org.deegree.commons.utils.test.IntegrationTestUtils.isImageSimilar;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.deegree.commons.utils.FileUtils;
import org.deegree.commons.utils.Triple;
import org.deegree.feature.Feature;
import org.deegree.feature.FeatureCollection;
import org.deegree.feature.types.DynamicAppSchema;
import org.deegree.feature.xpath.TypedObjectNodeXPathEvaluator;
import org.deegree.filter.XPathEvaluator;
import org.deegree.geometry.Envelope;
import org.deegree.geometry.Geometry;
import org.deegree.geometry.GeometryFactory;
import org.deegree.geometry.points.Points;
import org.deegree.geometry.standard.points.PackedPoints;
import org.deegree.geometry.utils.GeometryUtils;
import org.deegree.gml.GMLInputFactory;
import org.deegree.gml.GMLStreamReader;
import org.deegree.gml.GMLVersion;
import org.deegree.gml.feature.GMLFeatureReader;
import org.deegree.style.se.parser.SymbologyParser;
import org.deegree.style.se.unevaluated.Style;
import org.deegree.style.styling.Styling;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRenderedStyleImageSimilarityTest extends AbstractSimilarityTest {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRenderedStyleImageSimilarityTest.class);
    @Parameterized.Parameter(0)
    public String testName;

    @Parameterized.Parameter(1)
    public File gmlFile;

    @Parameterized.Parameter(2)
    public File styleFile;

    @Parameterized.Parameter(3)
    public File imageFile;

    public static Collection<Object[]> getFiles( File testDir ) {
        Collection<Object[]> params = new LinkedList<Object[]>();
        String baseName = testDir.getAbsolutePath();

        for ( File fGML : FileUtils.findFilesForExtensions( testDir, true, ".gml" ) ) {
            String base = FileUtils.getBasename( fGML.getAbsoluteFile() );
            String name = base;
            if ( name.startsWith( baseName ) )
                name = name.substring( baseName.length() + 1 );

            File fStyle = new File( base + ".xml" );
            File fImg = new File( base + ".png" );

            if ( fGML.isFile() && fStyle.isFile() && fImg.isFile() ) {
                params.add( new Object[] { name, fGML, fStyle, fImg } );
            } else {
                LOG.warn( "Could not find test data same {}.gml/.xml/.png", name );
            }
        }
        return params;
    }

    @Test
    public void renderAndCompare()
                            throws
                            Exception {

        Style style = readStyle( styleFile );
        FeatureCollection fc = readFeatureCollection( gmlFile );
        BufferedImage expected = ImageIO.read( imageFile );

        long time = currentTimeMillis();
        BufferedImage actual = new BufferedImage( expected.getWidth(), expected.getHeight(), TYPE_INT_RGB );
        Graphics2D graphics = actual.createGraphics();
        graphics.setColor( Color.WHITE );
        graphics.fillRect( 0, 0, actual.getWidth(), actual.getHeight() );

        Envelope fcEnv = (Envelope) fc.getProperties( GMLFeatureReader.BOUNDED_BY_GML32 ).get( 0 ).getValue();
        LOG.trace( "Envelope: {}", fcEnv );
        Java2DRenderer r = new Java2DRenderer( graphics, actual.getWidth(), actual.getHeight(), fcEnv );
        XPathEvaluator<Feature> evaluator = (XPathEvaluator) new TypedObjectNodeXPathEvaluator();

        for ( Feature f : fc ) {
            LOG.trace( "Rendering Feature {}", f.getId() );
            for ( Triple<Styling, LinkedList<Geometry>, String> evaluated : style.evaluate( f, evaluator ) ) {
                for ( Geometry evaluatedGeometry : evaluated.second ) {
                    r.render( evaluated.first, evaluatedGeometry );
                }
            }
        }
        graphics.dispose();
        LOG.debug( "Took {} ms", currentTimeMillis() - time );

        Assert.assertTrue( "Image for " + testName + "are not similar enough",
                           isImageSimilar( expected, actual, 0.01, prefixed( testName ) ) );
    }

    private Style readStyle( File file )
                            throws
                            Exception {
        final XMLInputFactory fac = XMLInputFactory.newInstance();
        XMLStreamReader xmlReader = fac.createXMLStreamReader( file.toURI().toURL().toString(),
                                                               file.toURI().toURL().openStream() );
        xmlReader.next();
        LOG.debug("Reading style document {}", file);
        Style style = SymbologyParser.INSTANCE.parse( xmlReader );
        return style;
    }

    private FeatureCollection readFeatureCollection( File file )
                            throws
                            Exception {
        GMLVersion version = GMLVersion.GML_32;
        URL docURL = file.toURI().toURL();
        GMLStreamReader gmlStream = GMLInputFactory.createGMLStreamReader( version, docURL );
        gmlStream.setApplicationSchema( new DynamicAppSchema() );
        LOG.debug( "Populating feature store with features from file '{}'...", docURL );
        FeatureCollection fc = gmlStream.readFeatureCollection();
        return fc;
    }

    public void generateDemoDataForHatching() {
        int objectId = 0;
        int rotation = 0;

        GeometryFactory fac = new GeometryFactory();
        Points points = new PackedPoints( null, new double[] { 10, 10, 10, 90, 90, 90, 90, 10, 10, 10 }, 2 );
        Geometry poly = fac.createPolygon( "0", null, fac.createLinearRing( null, null, points ), null );
        String tpl = "<gml:featureMember><Object gml:id=\"FEATURE_{0}\"><id>{0}</id><rotation>{1}</rotation>";
        tpl = tpl + "<geom><gml:Polygon gml:id=\"GML_{0}\">";
        tpl = tpl + "<gml:exterior><gml:LinearRing><gml:posList>{2}</gml:posList></gml:LinearRing></gml:exterior>";
        tpl = tpl + "</gml:Polygon></geom></Object></gml:featureMember>";

        for ( int rowOffset = 0; rowOffset < 10; rowOffset += 1 ) {
            for ( int colOffset = 0; colOffset < 10; colOffset += 1 ) {
                objectId++;
                rotation += 1;

                Geometry movedGeometry = GeometryUtils.move( poly, colOffset * 100, rowOffset * 100 );

                String pointsAsText = movedGeometry.toString() //
                                                   .replace( ',', ' ' ) //
                                                   .replaceAll( ".000000", "" );
                pointsAsText = pointsAsText.substring( pointsAsText.lastIndexOf( '(' ) + 1,
                                                       pointsAsText.indexOf( ')' ) );
                System.out.println( MessageFormat.format( tpl, objectId, rotation, pointsAsText ) );
            }
            System.out.println();
        }
    }
}
