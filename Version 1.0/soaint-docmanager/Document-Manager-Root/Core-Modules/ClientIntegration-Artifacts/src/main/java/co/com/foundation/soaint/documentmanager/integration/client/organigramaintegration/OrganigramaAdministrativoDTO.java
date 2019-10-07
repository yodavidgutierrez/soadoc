
package co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para organigramaAdministrativoDTO complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="organigramaAdministrativoDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="organigrama" type="{http://co.com.soaint.sgd.correspondencia.service}organigramaItemDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "organigramaAdministrativoDTO", propOrder = {
    "organigrama"
})
public class OrganigramaAdministrativoDTO {

    @XmlElement(nillable = true)
    protected List<OrganigramaItemDTO> organigrama;

    /**
     * Gets the value of the organigrama property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the organigrama property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrganigrama().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OrganigramaItemDTO }
     * 
     * 
     */
    public List<OrganigramaItemDTO> getOrganigrama() {
        if (organigrama == null) {
            organigrama = new ArrayList<OrganigramaItemDTO>();
        }
        return this.organigrama;
    }

}
