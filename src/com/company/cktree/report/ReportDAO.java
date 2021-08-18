package com.company.cktree.report;

import java.util.List;
import java.util.Map;

public interface ReportDAO {
	//�Ű��� DB�� ����
	public void insertReport(ReportDTO dto);
	//��ü �Ű��� ��ȸ
	public List<ReportDTO> selectReportList(Map<String,Object> map);
	//���Ƽ �ο�
	public void givePenalty(int m_no);
	//�Ű���Ʈ ����
	public int countReportList();
	//���Ƽ�ο��� �Ϸ�ð� ������Ʈ
	public void completePenaltyDate(int report_no);
	//���Ƽ�� 5�̻��� ȸ�� ��Ȱ��
	public void inactiveMember(int m_no);
	
}
