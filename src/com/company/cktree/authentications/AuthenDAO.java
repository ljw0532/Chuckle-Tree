package com.company.cktree.authentications;

import java.util.Map;

public interface AuthenDAO {
	//�����ڵ� ����
	public void insertAuthenCode(Map<String,Object> map);
	// ��ϵ� ���� �ڵ尡 �ִ��� �������� 
	public AuthenDTO selectAuthenCode(String authen_code);
	// ������ �Ϸ�Ǹ� ����� ����������ȣ ��ȸ
	public Map<String,Object> selectAuthenNo(int authen_no);
	// ���� �Ϸ�� �Ϸ�� �ð� ������Ʈ 
	public void updateComDate(int authen_no);
	// ���� �Ϸ�� ������ ������ ����� �̸��� ����  
	public void deleteAuthen();
	// ȸ�����ԿϷ�� �������̺� �̸���,�̸� ��� ����
	public void deleteEmailName(int authen_no);
}
