//$HeadURL: svn+ssh://developername@svn.wald.intevation.org/deegree/base/trunk/src/org/deegree/datatypes/QualifiedName.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 Contact:

 Andreas Poth
 lat/lon GmbH
 Aennchenstr. 19
 53115 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de

 ---------------------------------------------------------------------------*/
package org.deegree.model.types;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import org.deegree.model.util.StringTools;

/**
 * This class represent a qualified name for something. A name is thought to be built from an
 * optional prefix and/or a local name E.g.: <BR>- deegree - pre:deegree <BR>
 * a name may be located within a namespace assigned to the names prefix (or as default namespace if
 * the name has not prefix).
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 10522 $, $Date: 2008-03-07 12:09:06 +0100 (Fr, 07 Mrz 2008) $
 * @deprecated
 */
public class QualifiedName implements Serializable {

    private static final long serialVersionUID = 5551137348397905772L;

    private String prefix = null;

    private String localName = null;

    private URI namespace = null;

    private String s = null;

    /**
     * @param name
     *            local name/simple (without prefix)
     */
    public QualifiedName( String name ) {
        if ( name.indexOf( '{' ) > -1 ) {
            try {
                namespace = new URI( StringTools.extractStrings( name, "{", "}" )[0] );
            } catch ( URISyntaxException e ) {
            }
            int pos = name.lastIndexOf( ':' );
            localName = name.substring( pos + 1 );
        } else if ( name.indexOf( ':' ) > -1 ) {
            String[] tmp = StringTools.toArray( name, ":", false );
            if ( tmp.length == 2 ) {
                prefix = tmp[0];
                localName = tmp[1];
            } else {
                localName = name;
            }
        } else {
            this.localName = name;
        }
        buildString();
    }

    /**
     * @param name
     *            complete name including a prefix
     * @param namespace
     *            namespace the name is located within
     */
    public QualifiedName( String name, URI namespace ) {
        if ( name.indexOf( ':' ) > -1 ) {
            String[] tmp = StringTools.toArray( name, ":", false );
            prefix = tmp[0];
            this.localName = tmp[1];
        } else {
            this.localName = name;
        }
        this.namespace = namespace;
        buildString();
    }

    /**
     * @param prefix
     * @param localName
     *            local/simple name (e.g. deegree)
     * @param namespace
     *            namespace the name is located within
     */
    public QualifiedName( String prefix, String localName, URI namespace ) {
        this.prefix = prefix;
        this.localName = localName;
        this.namespace = namespace;
        buildString();
    }

    private void buildString() {
        StringBuffer sb = new StringBuffer( 50 );
        if ( prefix != null && prefix.length() != 0 ) {
            sb.append( prefix ).append( ':' );
        }
        sb.append( localName );
        s = sb.toString();
    }
    

    /**
     * returns a string representation of a QualifiedName. prefix and local name are separated by
     * ':'. If the Prefix is null, the sole localname will be returned.
     * 
     * @return string representation of a QualifiedName
     */
    public String getPrefixedName() {
        return s;
    }

    /**
     * @return a QualifiedName as a formatted string. If a QualifiedName has a namespace the
     *         returned format is:<br>
     *         {namespace}:localName. <br>
     *         Otherwise just a String representation of this qualified name will be returned. Which
     *         means, that if the prefix is not null (allthough not bound to namespace) the result
     *         String will be: <br>
     *         PRE_FIX:localName.<br>
     *         If the Prefix is null, the sole localname will be returned.
     */
    public String getFormattedString() {
        if ( namespace != null ) {
            return StringTools.concat( 100, "{", namespace, "}:", localName );
        }
        return s;
    }

    /**
     * returns the names prefix
     * 
     * @return the names prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * returns the local part of the name
     * 
     * @return the local part of the name
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * returns the namespace the name is located within (may be null)
     * 
     * @return the namespace the name is located within (may be null)
     */
    public URI getNamespace() {
        return namespace;
    }

    /**
     * @param ns
     *            the namespace to checkfor
     * @return true if the given namespace equals this qualified name's namespace. If the given ns
     *         is null and the namespace is null, this method will also return true.
     */
    public boolean isInNamespace( URI ns ) {
        if ( ns == null ) {
            if ( this.namespace == null ) {
                return true;
            }
            return false;
        }
        return ns.equals( this.namespace );
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer( 150 );
        result.append( this.s );
        if ( this.prefix != null && this.prefix.length() > 0 ) {
            result.append( " (" );
            result.append( this.prefix );
            result.append( "=" );
            if ( this.namespace == null || this.namespace.toASCIIString().length() == 0 ) {
                result.append( "not bound to a namespace" );
            } else {
                result.append( this.namespace.toASCIIString() );
            }
            result.append( ")" );
        }
        return result.toString();
    }

    @Override
    public int hashCode() {
        return ( this.namespace + this.localName ).hashCode();
    }

    @Override
    public boolean equals( Object o ) {
        // return false in the case that the object is null
        // or isn't an instance of QualifiedName
        if ( o == null || !( o instanceof QualifiedName ) ) {
            return false;
        }

        QualifiedName other = (QualifiedName) o;
        if ( localName.equals( other.getLocalName() ) ) {
            if ( ( namespace != null && namespace.equals( other.getNamespace() ) )
                 || ( namespace == null && other.getNamespace() == null ) ) {
                return true;
            }
        }
        return false;
    }
}