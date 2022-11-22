/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2022 by:
 - grit graphische Informationstechnik Beratungsgesellschaft mbH -

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

 grit graphische Informationstechnik Beratungsgesellschaft mbH
 Landwehrstr. 143, 59368 Werne
 Germany
 http://www.grit.de/

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
package org.deegree.rendering.r2d;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.deegree.commons.utils.TunableParameter;
import org.deegree.filter.function.FunctionManager;
import org.deegree.style.styling.mark.WellKnownNameManager;
import org.deegree.workspace.Destroyable;
import org.deegree.workspace.Workspace;
import org.deegree.workspace.standard.DefaultWorkspace;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Parameterized.class)
public class RenderedStyleImageSimilarityNonSquareTest extends AbstractRenderedStyleImageSimilarityTest {

    private static final File TEST_DIR = new File( "src/test/resources/org/deegree/rendering/r2d/similaritytests_non_square" );

    private static final Logger LOG = LoggerFactory.getLogger( RenderedStyleImageSimilarityNonSquareTest.class );

    private static Workspace ws = new DefaultWorkspace( TEST_DIR );

    private static List<Destroyable> destroyableResources = new LinkedList<>();


    @BeforeClass
    public static void runBefore() {
        System.setProperty( "deegree.rendering.graphics.squared", "false" );
        TunableParameter.resetCache();
        new WellKnownNameManager().init( ws );
        FunctionManager fm = new FunctionManager();
        fm.init( ws );
        destroyableResources.add( fm );
    }

    @AfterClass
    public static void runAfter() {
        destroyableResources.forEach( da -> da.destroy( ws ) );

        System.clearProperty( "deegree.rendering.graphics.squared");
        TunableParameter.resetCache();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> getFiles() {
        return getFiles(TEST_DIR);
    }
}
