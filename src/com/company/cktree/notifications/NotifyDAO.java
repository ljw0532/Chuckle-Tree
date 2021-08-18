package com.company.cktree.notifications;

import java.util.List;
import java.util.Map;

public interface NotifyDAO {
	//�˸� �߻�
	public void insertNotify(Map<String,Object> map);
	//ȸ����ȣ�� �ش��ϴ� �˸� ��ȸ
	public List<NotifyDTO> selectNotify(int m_no, int end);
	//�˸��� ���� �ð�
	public void updateReadDate(int n_no);
	//�˸� ������ȣ�� �ش��ϴ� �˸���ȸ
	public NotifyDTO selectNotifyOne(int n_no);
	//�˸� ����
	public int countNotify(int m_no);
	//�˸� ����
	public void deleteNotify(int n_no);
	
}
