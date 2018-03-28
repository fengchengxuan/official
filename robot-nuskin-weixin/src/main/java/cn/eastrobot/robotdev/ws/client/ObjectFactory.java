
package cn.eastrobot.robotdev.ws.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.eastrobot.robotdev.ws.client package. 
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

    private final static QName _Ask_QNAME = new QName("http://www.eastrobot.cn/ws/RobotServiceWx", "ask");
    private final static QName _AskResponse_QNAME = new QName("http://www.eastrobot.cn/ws/RobotServiceWx", "askResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.eastrobot.robotdev.ws.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AskResponse }
     * 
     */
    public AskResponse createAskResponse() {
        return new AskResponse();
    }

    /**
     * Create an instance of {@link Ask }
     * 
     */
    public Ask createAsk() {
        return new Ask();
    }

    /**
     * Create an instance of {@link RobotCommand }
     * 
     */
    public RobotCommand createRobotCommand() {
        return new RobotCommand();
    }

    /**
     * Create an instance of {@link UserAttribute }
     * 
     */
    public UserAttribute createUserAttribute() {
        return new UserAttribute();
    }

    /**
     * Create an instance of {@link RobotRequest }
     * 
     */
    public RobotRequest createRobotRequest() {
        return new RobotRequest();
    }

    /**
     * Create an instance of {@link RobotResponse }
     * 
     */
    public RobotResponse createRobotResponse() {
        return new RobotResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.eastrobot.cn/ws/RobotServiceWx", name = "ask")
    public JAXBElement<Ask> createAsk(Ask value) {
        return new JAXBElement<Ask>(_Ask_QNAME, Ask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.eastrobot.cn/ws/RobotServiceWx", name = "askResponse")
    public JAXBElement<AskResponse> createAskResponse(AskResponse value) {
        return new JAXBElement<AskResponse>(_AskResponse_QNAME, AskResponse.class, null, value);
    }

}
