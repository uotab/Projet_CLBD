package com.cpe.projet.springboot.user;



import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;



@Service
public class BusService {
	
	
    @Autowired
    JmsTemplate jmsTemplate;
    
    private final UserService userService;
    
    public BusService(UserService userService) {
    	this.userService = userService;
    }

    public void sendMsg(UserBusMessage msg) {
        System.out.println("[BUSSERVICE] SEND String MSG=["+msg.getFunctionName()+"]");
        jmsTemplate.convertAndSend("RESULT_BUS_MNG",msg);
    }

    public void sendMsg(UserBusMessage msg, String busName) {
        System.out.println("[BUSSERVICE] SEND String MSG=["+msg.getFunctionName()+"] to Bus=["+busName+"]");
        jmsTemplate.convertAndSend(busName,msg);
    }
    
    
	/*
	 * public void addUser(UserDTO user, String busName) { ObjectMapper mapper = new
	 * ObjectMapper(); try {
	 * 
	 * } catch (IOException e) { e.printStackTrace(); }
	 * jmsTemplate.convertAndSend(busName,user); }
	 */
    
    //////////////
    //LISTENER
    @JmsListener(destination = "RESULT_BUS_MNG", containerFactory = "connectionFactory")
    public void receiveMessage(UserBusMessage msg, Message message) {

        System.out.println("[BUSLISTENER] [CHANNEL RESULT_BUS_MNG] RECEIVED String MSG=["+msg.getFunctionName()+"]");
        userService.ReceiveMessageFromBus(msg);
    }

    @JmsListener(destination = "A", containerFactory = "connectionFactory")
    public void receiveMessageA(UserBusMessage msg, Message message) {

        System.out.println("[BUSLISTENER] [CHANNEL A] RECEIVED String MSG=["+msg.getFunctionName()+"]");
        userService.ReceiveMessageFromBus(msg);
    }

    @JmsListener(destination = "B", containerFactory = "connectionFactory")
    public void receiveMessageB(UserBusMessage msg, Message message) {

        System.out.println("[BUSLISTENER] [CHANNEL B] RECEIVED String MSG=["+msg.getFunctionName()+"]");
        userService.ReceiveMessageFromBus(msg);
    }

    
}
