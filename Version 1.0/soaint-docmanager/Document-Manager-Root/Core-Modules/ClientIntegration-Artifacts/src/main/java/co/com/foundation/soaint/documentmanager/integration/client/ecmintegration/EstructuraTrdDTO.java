
package co.com.foundation.soaint.documentmanager.integration.client.ecmintegration;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para estructuraTrdDTO complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="estructuraTrdDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contenidoDependenciaList" type="{http://co.com.soaint.ecm.integration.service.ws}contenidoDependenciaTrdDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="organigramaItemList" type="{http://co.com.soaint.ecm.integration.service.ws}organigramaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "estructuraTrdDTO", propOrder = {
    "contenidoDependenciaList",
    "organigramaItemList"
})
public class EstructuraTrdDTO {

    @XmlElement(nillable = true)
    protected List<ContenidoDependenciaTrdDTO> contenidoDependenciaList;
    @XmlElement(nillable = true)
    protected List<OrganigramaDTO> organigramaItemList;

    /**
     * Gets the value of the contenidoDependenciaList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contenidoDependenciaList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContenidoDependenciaList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContenidoDependenciaTrdDTO }
     * 
     * 
     */
    public List<ContenidoDependenciaTrdDTO> getContenidoDependenciaList() {
        if (contenidoDependenciaList == null) {
            contenidoDependenciaList = new ArrayList<ContenidoDependenciaTrdDTO>();
        }
        return this.contenidoDependenciaList;
    }

    /**
     * Gets the value of the organigramaItemList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the organigramaItemList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrganigramaItemList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OrganigramaDTO }
     * 
     * 
     */
    public List<OrganigramaDTO> getOrganigramaItemList() {
        if (organigramaItemList == null) {
            organigramaItemList = new ArrayList<OrganigramaDTO>();
        }
        return this.organigramaItemList;
    }

}
