package com.company.cktree.notifications;

import java.util.Map;

public interface NotifyService {
		//�˸� �߻�
		public void insertNotify(Map<String,Object> map);
		//�˸��� ���� �ð�
		public void updateReadDate(int n_no);
		//�˸�������ȣ�� �ش��ϴ� �˸���ȸ
		public NotifyDTO selectNotifyOne(int n_no);
		//�˸� ����
		public void deleteNotify(int n_no);
}
