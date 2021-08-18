package com.company.cktree.member;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.company.cktree.board.BoardDTO;
import com.company.cktree.comments.CommentsDTO;

public interface MemberDAO {
	//ȸ������ insert(ȸ������)
	public void insertMember(MemberDTO member);
	//�α��� üũ
	public MemberDTO loginCheck(MemberDTO member);
	//�ڵ� �α���
	public void keepLogin(String m_id,String session_id, Date session_limit);
	//�α��� ����Ű Ȯ��
	public Map<String,Object> checkUserWithSessionKey(String session_id);
	//���̵� ã��
	public MemberDTO findId(MemberDTO member);
	//��й�ȣ ã��
	public MemberDTO findPw(MemberDTO member);
	//ȸ�����Խ� ���̵� �ߺ� üũ
	public int checkId(String id);
	//ȸ�����Խ� �̸��� �ߺ� üũ
	public int checkEmail(String email);
	//���� ȸ������ ��ȸ
	public MemberDTO memberInfo(int m_no);
	//���� ȸ������ ����
	public void memberModify(MemberDTO member);
	//ȸ��Ż��
	public void leaveId(int m_no);
	//�α��ν� ���̵� üũ
	public MemberDTO loginIdCheck(String m_id);
	//���� ���� ������� �̸������� üũ
	public String usingEmail(MemberDTO member);
	//ȸ�����Խ� �ڵ��� �ߺ� üũ
	public String checkPhone(String m_phone);

	//���� �� ��
	public List<BoardDTO> myPosts(int m_no,int end);
	//���� �� ���
	public List<CommentsDTO> myComments(int m_no, int end);
	//���� ���� ���ƿ�
	public List<BoardDTO> myLikeit(int m_no, int end);
	
	//������ �̹��� ��������
	public String selectMyProfileImage(int m_no);
	//��й�ȣ ����
	public void pwChange(int m_no, String m_password);
	//���� ���� ���� �ִ� ��й�ȣ ���� Ȯ��
	public String checkMyPw(int m_no, String m_password);
	//ȸ������ ��ȸ
	public MemberDTO selectUserInfo(int m_no);
	//�ٸ� ȸ���� �ۼ��� ��ȸ�ϱ� ���� id,no
	public MemberDTO selectNoId(int m_no);
	
	//��Ȱ�� ȸ������ üũ�ϴ� ����
	public String inactiveMemberCheck(int m_no);
}
