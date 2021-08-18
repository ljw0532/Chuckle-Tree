package com.company.cktree.authentications;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.company.cktree.member.MemberDTO;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;	

@Service("AuthenService")
public class AuthenServiceImpl implements AuthenService {

	@Inject
	private AuthenDAO authenDAO;
	
	@Override
	public void sendSMS(MemberDTO member) {
		
		String api_key = "api_key";
		String api_secret = "api_secret";
		
		
		String m_phone = member.getM_phone();
		String rand =  Integer.toString((int)Math.round(Math.random()*1000000));//���� 6�ڸ� ���� ����
		Map<String,Object> map = new HashMap<>(); //DB�� ���������� map
		map.put("authen_code", rand);
		map.put("m_phone", m_phone);
		map.put("m_name", member.getM_name());
		authenDAO.insertAuthenCode(map); //DB�� �����ڵ�,�̸�,����ȣ�� ����
		Message coolsms = new Message(api_key, api_secret);
		
		HashMap<String,String> set = new HashMap<>();
		set.put("to", "01041472676");//���Ź�ȣ
		set.put("from", m_phone);//�߽Ź�ȣ
		set.put("text", "[Chuckle Tree!] ���������� ���� ������ȣ ["+ rand +"]�� �Է��� �ּ���."); //���ڳ���
		set.put("type", "sms");
		
		try {
			JSONObject result = (JSONObject)coolsms.send(set);
			System.out.println(result.toString());
		} catch (CoolsmsException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCode());
			e.printStackTrace();
		}	
	}

	@Override
	public AuthenDTO checkAuthenCode(String authen_code) {
		AuthenDTO dto = authenDAO.selectAuthenCode(authen_code);
		if(dto != null){ //������ �Ǹ�
			authenDAO.updateComDate(dto.getAuthen_no());//�����Ϸ�� �Ϸ�ð� ������Ʈ
			authenDAO.deleteAuthen(); //�Ϸ�ð��� ���� �����͵��� ���� (�� �̸��Ϸ� ������ ������ ��û�� ���)
			return dto;
		} else {
			return dto;
		}
	}

}
