//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-792 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.02.19 at 03:46:10 PM MEZ 
//


package org.deegree.services.wpvs.configuration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TexturesInGPUMem" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="CachedTextureTiles" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="DirectTextureMemory" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="NumberOfDEMFragmentsCached" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="DirectIOMemory" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="NumberOfResultImageBuffers" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="LatitudeOfScene" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="MaxViewWidth" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="MaxViewHeight" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="MaxRequestFarClippingPlane" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="NearClippingPlane" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element ref="{http://www.deegree.org/services/wpvs}Copyright" minOccurs="0"/>
 *         &lt;element ref="{http://www.deegree.org/services/wpvs}SkyImages"/>
 *         &lt;element name="OpenGLInitConfigFile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.deegree.org/services/wpvs}DatasetDefinitions"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "texturesInGPUMem",
    "cachedTextureTiles",
    "directTextureMemory",
    "numberOfDEMFragmentsCached",
    "directIOMemory",
    "numberOfResultImageBuffers",
    "latitudeOfScene",
    "maxViewWidth",
    "maxViewHeight",
    "maxRequestFarClippingPlane",
    "nearClippingPlane",
    "copyright",
    "skyImages",
    "openGLInitConfigFile",
    "datasetDefinitions"
})
@XmlRootElement(name = "ServiceConfiguration")
public class ServiceConfiguration {

    @XmlElement(name = "TexturesInGPUMem", defaultValue = "300")
    protected Integer texturesInGPUMem;
    @XmlElement(name = "CachedTextureTiles", defaultValue = "400")
    protected Integer cachedTextureTiles;
    @XmlElement(name = "DirectTextureMemory", defaultValue = "400")
    protected Integer directTextureMemory;
    @XmlElement(name = "NumberOfDEMFragmentsCached", defaultValue = "1000")
    protected Integer numberOfDEMFragmentsCached;
    @XmlElement(name = "DirectIOMemory", defaultValue = "500")
    protected Integer directIOMemory;
    @XmlElement(name = "NumberOfResultImageBuffers", defaultValue = "25")
    protected Integer numberOfResultImageBuffers;
    @XmlElement(name = "LatitudeOfScene", defaultValue = "51.7")
    protected Double latitudeOfScene;
    @XmlElement(name = "MaxViewWidth", defaultValue = "1200")
    protected Integer maxViewWidth;
    @XmlElement(name = "MaxViewHeight", defaultValue = "1000")
    protected Integer maxViewHeight;
    @XmlElement(name = "MaxRequestFarClippingPlane", defaultValue = "100000")
    protected Double maxRequestFarClippingPlane;
    @XmlElement(name = "NearClippingPlane", defaultValue = "0.1")
    protected Double nearClippingPlane;
    @XmlElement(name = "Copyright")
    protected Copyright copyright;
    @XmlElement(name = "SkyImages", required = true)
    protected SkyImages skyImages;
    @XmlElement(name = "OpenGLInitConfigFile")
    protected String openGLInitConfigFile;
    @XmlElement(name = "DatasetDefinitions", required = true)
    protected DatasetDefinitions datasetDefinitions;

    /**
     * Gets the value of the texturesInGPUMem property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTexturesInGPUMem() {
        return texturesInGPUMem;
    }

    /**
     * Sets the value of the texturesInGPUMem property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTexturesInGPUMem(Integer value) {
        this.texturesInGPUMem = value;
    }

    /**
     * Gets the value of the cachedTextureTiles property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCachedTextureTiles() {
        return cachedTextureTiles;
    }

    /**
     * Sets the value of the cachedTextureTiles property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCachedTextureTiles(Integer value) {
        this.cachedTextureTiles = value;
    }

    /**
     * Gets the value of the directTextureMemory property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDirectTextureMemory() {
        return directTextureMemory;
    }

    /**
     * Sets the value of the directTextureMemory property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDirectTextureMemory(Integer value) {
        this.directTextureMemory = value;
    }

    /**
     * Gets the value of the numberOfDEMFragmentsCached property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfDEMFragmentsCached() {
        return numberOfDEMFragmentsCached;
    }

    /**
     * Sets the value of the numberOfDEMFragmentsCached property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfDEMFragmentsCached(Integer value) {
        this.numberOfDEMFragmentsCached = value;
    }

    /**
     * Gets the value of the directIOMemory property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDirectIOMemory() {
        return directIOMemory;
    }

    /**
     * Sets the value of the directIOMemory property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDirectIOMemory(Integer value) {
        this.directIOMemory = value;
    }

    /**
     * Gets the value of the numberOfResultImageBuffers property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfResultImageBuffers() {
        return numberOfResultImageBuffers;
    }

    /**
     * Sets the value of the numberOfResultImageBuffers property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfResultImageBuffers(Integer value) {
        this.numberOfResultImageBuffers = value;
    }

    /**
     * Gets the value of the latitudeOfScene property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getLatitudeOfScene() {
        return latitudeOfScene;
    }

    /**
     * Sets the value of the latitudeOfScene property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setLatitudeOfScene(Double value) {
        this.latitudeOfScene = value;
    }

    /**
     * Gets the value of the maxViewWidth property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxViewWidth() {
        return maxViewWidth;
    }

    /**
     * Sets the value of the maxViewWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxViewWidth(Integer value) {
        this.maxViewWidth = value;
    }

    /**
     * Gets the value of the maxViewHeight property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxViewHeight() {
        return maxViewHeight;
    }

    /**
     * Sets the value of the maxViewHeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxViewHeight(Integer value) {
        this.maxViewHeight = value;
    }

    /**
     * Gets the value of the maxRequestFarClippingPlane property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMaxRequestFarClippingPlane() {
        return maxRequestFarClippingPlane;
    }

    /**
     * Sets the value of the maxRequestFarClippingPlane property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMaxRequestFarClippingPlane(Double value) {
        this.maxRequestFarClippingPlane = value;
    }

    /**
     * Gets the value of the nearClippingPlane property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getNearClippingPlane() {
        return nearClippingPlane;
    }

    /**
     * Sets the value of the nearClippingPlane property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setNearClippingPlane(Double value) {
        this.nearClippingPlane = value;
    }

    /**
     * Gets the value of the copyright property.
     * 
     * @return
     *     possible object is
     *     {@link Copyright }
     *     
     */
    public Copyright getCopyright() {
        return copyright;
    }

    /**
     * Sets the value of the copyright property.
     * 
     * @param value
     *     allowed object is
     *     {@link Copyright }
     *     
     */
    public void setCopyright(Copyright value) {
        this.copyright = value;
    }

    /**
     * Gets the value of the skyImages property.
     * 
     * @return
     *     possible object is
     *     {@link SkyImages }
     *     
     */
    public SkyImages getSkyImages() {
        return skyImages;
    }

    /**
     * Sets the value of the skyImages property.
     * 
     * @param value
     *     allowed object is
     *     {@link SkyImages }
     *     
     */
    public void setSkyImages(SkyImages value) {
        this.skyImages = value;
    }

    /**
     * Gets the value of the openGLInitConfigFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpenGLInitConfigFile() {
        return openGLInitConfigFile;
    }

    /**
     * Sets the value of the openGLInitConfigFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpenGLInitConfigFile(String value) {
        this.openGLInitConfigFile = value;
    }

    /**
     * Gets the value of the datasetDefinitions property.
     * 
     * @return
     *     possible object is
     *     {@link DatasetDefinitions }
     *     
     */
    public DatasetDefinitions getDatasetDefinitions() {
        return datasetDefinitions;
    }

    /**
     * Sets the value of the datasetDefinitions property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatasetDefinitions }
     *     
     */
    public void setDatasetDefinitions(DatasetDefinitions value) {
        this.datasetDefinitions = value;
    }

}
