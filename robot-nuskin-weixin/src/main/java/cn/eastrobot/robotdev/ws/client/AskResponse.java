
package cn.eastrobot.robotdev.ws.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for askResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="askResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="robotResponse" type="{http://www.eastrobot.cn/ws/RobotServiceWx}robotResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "askResponse", propOrder = {
    "robotResponse"
})
public class AskResponse {

    protected RobotResponse robotResponse;

    /**
     * Gets the value of the robotResponse property.
     * 
     * @return
     *     possible object is
     *     {@link RobotResponse }
     *     
     */
    public RobotResponse getRobotResponse() {
        return robotResponse;
    }

    /**
     * Sets the value of the robotResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link RobotResponse }
     *     
     */
    public void setRobotResponse(RobotResponse value) {
        this.robotResponse = value;
    }

}
