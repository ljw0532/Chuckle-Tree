package com.company.cktree.authentications;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.cktree.exception.AlreadyExistingPhoneException;
import com.company.cktree.member.MemberDAO;
import com.company.cktree.member.MemberDTO;
import com.company.cktree.validator.AuthenticationValidator;

@Controller
public class AuthenController {
	
	@Inject
	private AuthenService authenService;
	@Inject
	private MemberDAO memberDAO;
	
	@RequestMapping("join")
	public String join() {	
		return "join/joinAuthentication";
	}
	
	//�̸��� �����鼭 DB�� �����ڵ� ����
	@ResponseBody
	@RequestMapping("sendMail")
	public boolean sendEmail(@ModelAttribute("member") MemberDTO member, BindingResult errors) throws Exception {			
		new AuthenticationValidator().validate(member, errors);
		if (errors.hasErrors()) {
			return false;
		}
		
		try {
			authenService.sendSMS(member);
		
		} catch (AlreadyExistingPhoneException e) {
			errors.rejectValue("m_phone", "duplicate", "�̹� ���� ��ȣ�Դϴ�.");
			return false;
		} 
		return true;
	}
	
	//��ϵ� ������ȣ�� �ִ��� ��ȸ
	@ResponseBody
	@RequestMapping("requestAuthentication")
	public Map<String,Object> requestAuthentication(String authen_code){
		Map<String,Object> result = new HashMap<>();
		result.put("authen",authenService.checkAuthenCode(authen_code));
		return result;	
	}
	
	
	//�ڵ�����ȣ �ߺ��˻�
	@ResponseBody
	@RequestMapping("checkPhone")
	public Map<String,Object> checkPhone(String m_phone){
		Map<String,Object> result = new HashMap<>();
		result.put("m_phone", memberDAO.checkPhone(m_phone));
		return result;
	}
}
