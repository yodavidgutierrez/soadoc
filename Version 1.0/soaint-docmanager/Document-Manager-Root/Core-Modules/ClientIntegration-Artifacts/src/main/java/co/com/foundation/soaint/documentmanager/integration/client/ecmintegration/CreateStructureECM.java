
package co.com.foundation.soaint.documentmanager.integration.client.ecmintegration;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para createStructureECM complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="createStructureECM">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="estructura" type="{http://co.com.soaint.ecm.integration.service.ws}estructuraTrdDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createStructureECM", propOrder = {
    "estructura"
})
public class CreateStructureECM {

    protected List<EstructuraTrdDTO> estructura;

    /**
     * Gets the value of the estructura property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the estructura property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEstructura().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EstructuraTrdDTO }
     * 
     * 
     */
    public List<EstructuraTrdDTO> getEstructura() {
        if (estructura == null) {
            estructura = new ArrayList<EstructuraTrdDTO>();
        }
        return this.estructura;
    }

}
