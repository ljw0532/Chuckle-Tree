package com.company.cktree.board;

import java.util.List;
import java.util.Map;

import com.company.cktree.comments.CommentsDTO;

public interface BoardService {
	//fun�Խ��Ǹ���Ʈ
	public List<BoardDTO> funBoardList(String searchOption, String keyword, int start, int end);
	//������ ����Ʈ
	public int pagingList(String searchOption, String keyword);
	//�۾���
	public void boardWrite(BoardDTO board);
	//�Խñ� ��
	public Map<String,Object> funBoardDetail(Map<String,Object> map);
	//��ȸ��
	public void detailCnt(int b_no);
	//���ƿ� insert
	public int insertLikeit(Map<String,Object> map);
	//���ƿ� Count
	public int countLikeit(int b_no);
	//���ƿ�� �Ⱦ�� ���
	public int deleteDislike(Map<String,Object> map);
	//�Ⱦ�� �߰�
	public int insertDislike(Map<String,Object> map);
	//�Ⱦ�� ����
	public int countDislike(int b_no);
	//�Ⱦ��� ���ƿ� ���
	public int deleteLikeit(Map<String,Object> map);
	//��� �ۼ�
	public void writeComments(Map<String,Object> map);
	//��� ����Ʈ
	public Map<String,Object> commentsList(Map<String,Object> map);
	//��� ����
	public String commentsUpdate(Map<String,Object> map);
	//��� ����
	public void commentsDelete(Map<String,Object> map);
	//�Խñ� ����
	public void boardUpdate(BoardDTO board);
	//�Խñ� ����
	public void boardDelete(int b_no);	
	//����Ʈ �Խ��� ����Ʈ
	public List<BoardDTO> bestBoardList(String searchOption, String keyword, int start, int end);
	//����Ʈ �Խ��� ����Ʈ ����
	public int countBestBoardList(String searchOption, String keyword);
	//����Ʈ �Խ��� ����Ʈ
	public List<BoardDTO> worstBoardList(String searchOption, String keyword, int start, int end);
	//����Ʈ �Խ��� ����Ʈ ����
	public int countWorstBoardList(String searchOption, String keyword);
	
}