//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-792 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.02.19 at 03:45:34 PM MEZ 
//


package org.deegree.services.controller.wms.configuration;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.deegree.services.controller.wms.configuration package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.deegree.services.controller.wms.configuration
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PublishedInformation.SupportedVersions }
     * 
     */
    public PublishedInformation.SupportedVersions createPublishedInformationSupportedVersions() {
        return new PublishedInformation.SupportedVersions();
    }

    /**
     * Create an instance of {@link PublishedInformation.AllowedOperations.GetLegendGraphic }
     * 
     */
    public PublishedInformation.AllowedOperations.GetLegendGraphic createPublishedInformationAllowedOperationsGetLegendGraphic() {
        return new PublishedInformation.AllowedOperations.GetLegendGraphic();
    }

    /**
     * Create an instance of {@link PublishedInformation.ImageFormat }
     * 
     */
    public PublishedInformation.ImageFormat createPublishedInformationImageFormat() {
        return new PublishedInformation.ImageFormat();
    }

    /**
     * Create an instance of {@link PublishedInformation.AllowedOperations.GetFeatureInfo }
     * 
     */
    public PublishedInformation.AllowedOperations.GetFeatureInfo createPublishedInformationAllowedOperationsGetFeatureInfo() {
        return new PublishedInformation.AllowedOperations.GetFeatureInfo();
    }

    /**
     * Create an instance of {@link PublishedInformation.AllowedOperations }
     * 
     */
    public PublishedInformation.AllowedOperations createPublishedInformationAllowedOperations() {
        return new PublishedInformation.AllowedOperations();
    }

    /**
     * Create an instance of {@link PublishedInformation.GetFeatureInfoFormat }
     * 
     */
    public PublishedInformation.GetFeatureInfoFormat createPublishedInformationGetFeatureInfoFormat() {
        return new PublishedInformation.GetFeatureInfoFormat();
    }

    /**
     * Create an instance of {@link PublishedInformation }
     * 
     */
    public PublishedInformation createPublishedInformation() {
        return new PublishedInformation();
    }

}
