package com.company.cktree.socket;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

public class EchoHandler extends TextWebSocketHandler{
	HttpSessionHandshakeInterceptor i = null;
	List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
	
	//�α������� ��������
	Map<String,WebSocketSession> userSessionsMap = new HashMap<String,WebSocketSession>();
	
	//Ŭ���̾�Ʈ�� ������ �������϶�
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception{
		String key = session.getId();	
		userSessionsMap.put(key,session);
			System.out.println("����"+userSessionsMap.get(key));
	}

	//Ŭ���̾�Ʈ�� Data ���� ��
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
		String msg = message.getPayload();
		if(msg != null) {
			String[] strs = msg.split(",");
			log(strs.toString());
			if(strs != null && strs.length == 5) {
				String b_no = strs[0];
				String b_writer = strs[1];
				String count = strs[2];
				String b_title = strs[3];
				String m_name = strs[4];		
				String comment = m_name+"���� " + b_writer + "���� " + b_title +" �Խù��� ���ƿ並 �������ϴ�.";
				
				System.out.println("b_writer"+userSessionsMap.get(b_writer));
				WebSocketSession receiversession = userSessionsMap.get(session.getId());
				System.out.println("���ù�����"+receiversession);
			
					TextMessage txtmsg = new TextMessage(comment);
					receiversession.sendMessage(txtmsg);//�ۼ��ڿ��� �˷��ݴϴ�
				
			}
		}
	}
	
	//���� ������ ��
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
		String senderId = getMemberId(session);
		if(senderId!=null) {
			log(senderId + "���� �����");
			userSessionsMap.remove(senderId);
			sessions.remove(session);
		}
	}
	
	// ���� �߻���
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log(session.getId() + " �ͼ��� �߻�: " + exception.getMessage());
	}
		
	//�α����
	private void log(String logmsg) {
		System.out.println(new Date() + " : " + logmsg);
	}
	
	//���ǿ��� ȸ�����̵� ��������
	private String getMemberId(WebSocketSession session) {
		Map<String,Object> httpSession = session.getAttributes();
		String m_id = (String) httpSession.get("session_m_id");
		
		
		return m_id == null ? null: m_id;
	}
}
