package com.company.cktree.member;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface MemberService {
	//ȸ������
	public void insertMember(MemberDTO member,HttpServletRequest request, HttpServletResponse response) throws Exception;
	//�α��� Ȯ��
	public MemberDTO loginCheck(MemberDTO member, HttpSession session);
	// �ڵ��α��� üũ�� ��쿡 ����� ���̺� ���ǰ� ��ȿ�ð��� �����ϱ� ���� �޼���
	public void keepLogin(String m_id,String session_id, Date session_limit); 
	// ������ �α����� ���� �ִ���, �� ��ȿ�ð��� ���� ���� ������ ������ �ִ��� üũ
    public Map<String, Object> checkUserWithSessionKey(String SESSIONKEY);
    //���̵� ã��
	public MemberDTO findId(MemberDTO member);
	//��й�ȣ ã��
    public MemberDTO findPw(MemberDTO member) throws Exception;
    //���̵� Ȯ��
    public int checkId(String id);
    //�̸��� Ȯ��
    public int checkEmail(String email);
    //���� ȸ������ ��ȸ
    public MemberDTO memberInfo(int m_no);
    //ȸ������ ����
    public void memberModify(MemberDTO member,MultipartHttpServletRequest request);
    //ȸ��Ż��
    public void leaveId(int m_no); 
    //��й�ȣ ����
    public int pwChange(int m_no,String org_pw, String m_password, HttpSession session);
    //ȸ������ ��ȸ
    public MemberDTO selectUserInfo(int m_no);
}
