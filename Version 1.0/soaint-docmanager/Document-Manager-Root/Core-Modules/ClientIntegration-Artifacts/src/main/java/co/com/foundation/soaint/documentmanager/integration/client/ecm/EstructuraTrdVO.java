
package co.com.foundation.soaint.documentmanager.integration.client.ecm;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para estructuraTrdVO complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="estructuraTrdVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contenidoDependenciaList" type="{http://co.com.foundation.soaint.documentmanager.ws/services/records}contenidoDependenciaTrdVO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="organigramaItemList" type="{http://co.com.foundation.soaint.documentmanager.ws/services/records}organigramaVO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "estructuraTrdVO", propOrder = {"contenidoDependenciaList", "organigramaItemList"
})
public class EstructuraTrdVO {

    @XmlElement(nillable = true)
    protected List<ContenidoDependenciaTrdVO> contenidoDependenciaList;
    @XmlElement(nillable = true)
    protected List<OrganigramaVO> organigramaItemList;

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
     * {@link ContenidoDependenciaTrdVO }
     * 
     * 
     */
    public List<ContenidoDependenciaTrdVO> getContenidoDependenciaList() {
        if (contenidoDependenciaList == null) {
            contenidoDependenciaList = new ArrayList<ContenidoDependenciaTrdVO>();
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
     * {@link OrganigramaVO }
     * 
     * 
     */
    public List<OrganigramaVO> getOrganigramaItemList() {
        if (organigramaItemList == null) {
            organigramaItemList = new ArrayList<OrganigramaVO>();
        }
        return this.organigramaItemList;
    }

}
