//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-792 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.02.19 at 03:45:31 PM MEZ 
//


package org.deegree.services.wms.configuration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SupportedFeaturesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SupportedFeaturesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AntiAliasing" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RenderingQuality" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Interpolation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SupportedFeaturesType", propOrder = {
    "antiAliasing",
    "renderingQuality",
    "interpolation"
})
public class SupportedFeaturesType {

    @XmlElement(name = "AntiAliasing")
    protected String antiAliasing;
    @XmlElement(name = "RenderingQuality")
    protected String renderingQuality;
    @XmlElement(name = "Interpolation")
    protected String interpolation;

    /**
     * Gets the value of the antiAliasing property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAntiAliasing() {
        return antiAliasing;
    }

    /**
     * Sets the value of the antiAliasing property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAntiAliasing(String value) {
        this.antiAliasing = value;
    }

    /**
     * Gets the value of the renderingQuality property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRenderingQuality() {
        return renderingQuality;
    }

    /**
     * Sets the value of the renderingQuality property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRenderingQuality(String value) {
        this.renderingQuality = value;
    }

    /**
     * Gets the value of the interpolation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterpolation() {
        return interpolation;
    }

    /**
     * Sets the value of the interpolation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterpolation(String value) {
        this.interpolation = value;
    }

}
