//$HeadURL: svn+ssh://lbuesching@svn.wald.intevation.de/deegree/base/trunk/resources/eclipse/files_template.xml $
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

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.client.mdeditor.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.deegree.client.mdeditor.config.ConfigurationException;
import org.deegree.client.mdeditor.config.FormConfigurationFactory;
import org.deegree.client.mdeditor.controller.FormGroupHandler;
import org.deegree.client.mdeditor.model.FormGroupInstance;

/**
 * TODO add class documentation here
 * 
 * @author <a href="mailto:buesching@lat-lon.de">Lyn Buesching</a>
 * @author last edited by: $Author: lyn $
 * 
 * @version $Revision: $, $Date: $
 */
@ManagedBean
@RequestScoped
public class FormGroupInstanceBean {

    private Map<String, List<FormGroupInstance>> formGroupInstances = new HashMap<String, List<FormGroupInstance>>();

    private boolean fgiLoaded = false;

    // TODO
    private void loadFormGropupInstances() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession( false );
        try {
            List<String> formGroupIds = FormConfigurationFactory.getOrCreateFormConfiguration( session.getId() ).getReferencedFormGroupIds();

            for ( String id : formGroupIds ) {
                formGroupInstances.put( id, FormGroupHandler.getFormGroupInstances( id ) );
            }
        } catch ( ConfigurationException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Map<String, List<FormGroupInstance>> getFormGroupInstances() {
        if ( !fgiLoaded ) {
            loadFormGropupInstances();
            fgiLoaded = true;
        }
        return formGroupInstances;
    }
}
