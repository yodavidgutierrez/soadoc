
package co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para crearOrganigrama complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="crearOrganigrama">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="organigramaItem" type="{http://co.com.soaint.sgd.correspondencia.service}organigramaAdministrativoDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "crearOrganigrama", propOrder = {
    "organigramaItem"
})
public class CrearOrganigrama {

    protected OrganigramaAdministrativoDTO organigramaItem;

    /**
     * Obtiene el valor de la propiedad organigramaItem.
     * 
     * @return
     *     possible object is
     *     {@link OrganigramaAdministrativoDTO }
     *     
     */
    public OrganigramaAdministrativoDTO getOrganigramaItem() {
        return organigramaItem;
    }

    /**
     * Define el valor de la propiedad organigramaItem.
     * 
     * @param value
     *     allowed object is
     *     {@link OrganigramaAdministrativoDTO }
     *     
     */
    public void setOrganigramaItem(OrganigramaAdministrativoDTO value) {
        this.organigramaItem = value;
    }

}
