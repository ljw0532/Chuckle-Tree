package com.company.cktree.authentications;

import com.company.cktree.member.MemberDTO;

public interface AuthenService {
	
	//�����ڵ� DB�� ����
	public void sendSMS(MemberDTO member);
	//�����ڵ� �´��� Ȯ��
	public AuthenDTO checkAuthenCode(String authen_code);
}
