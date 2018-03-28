
package cn.eastrobot.robotdev.ws.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ask complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ask">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="robotRequest" type="{http://www.eastrobot.cn/ws/RobotServiceWx}robotRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ask", propOrder = {
    "robotRequest"
})
public class Ask {

    protected RobotRequest robotRequest;

    /**
     * Gets the value of the robotRequest property.
     * 
     * @return
     *     possible object is
     *     {@link RobotRequest }
     *     
     */
    public RobotRequest getRobotRequest() {
        return robotRequest;
    }

    /**
     * Sets the value of the robotRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link RobotRequest }
     *     
     */
    public void setRobotRequest(RobotRequest value) {
        this.robotRequest = value;
    }

}
