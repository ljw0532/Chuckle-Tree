package com.company.cktree.board;

import java.util.List;
import java.util.Map;

import com.company.cktree.comments.CommentsDTO;

public interface BoardDAO {
	//fun�Խ��Ǹ���Ʈ
	public List<BoardDTO> funBoardList(String searchOption, String keyword,int start, int end);
	//����������Ʈ
	public int pagingList(Map<String,Object> map);
	//�Խñ� ����
	public void boardWrite(BoardDTO board);
	//�Խñ� ��
	public Map<String,Object> funBoardDetail(int b_no);
	//�Խñ� ����
	public void boardUpdate(BoardDTO board);
	//�Խñ� ����
	public void boardDelete(int b_no);
	//��ȸ��
	public void detailCnt(int b_no);
	//����Ʈ �Խù� ����Ʈ
	public List<BoardDTO> bestBoardList(String searchOption, String keyword, int start, int end);
	//����Ʈ �Խù� ����Ʈ ����
	public int countBestBoardList(Map<String,Object> map);
	//����Ʈ �Խù� ����Ʈ
	public List<BoardDTO> worstBoardList(String searchOption, String keyword, int start, int end);
	//����Ʈ �Խù� ����Ʈ ����
	public int countWorstBoardList(Map<String,Object> map);
	
	//���ƿ� insert
	public void insertLikeit(Map<String, Object> map);
	//���ƿ� Ȯ��
	public Map<String,Object> selectLikeitExist(Map<String,Object> map);
	//���ƿ� ���
	public void deleteLikeit(Map<String,Object> map);
	//���ƿ� ����	
	public int countLikeit(int b_no);
	
	//�Ⱦ�� insert
	public void insertDislike(Map<String,Object>map);
	//�Ⱦ�� �������� Ȯ��
	public Map<String,Object> selectDislikeExist(Map<String,Object>map);
	//�Ⱦ�� ���
	public void deleteDislike(Map<String,Object> map);
	//�Ⱦ�� ����
	public int countDislike(int b_no);
	
	//��� �ۼ�
	public void writeComments(Map<String,Object> map);
	//��� ����Ʈ
	public List<CommentsDTO> commentsList(Map<String,Object> map);
	//���&��� ����
	public void commentsUpdate(Map<String,Object> map);
	//���&��� ����
	public void commentsDelete(Map<String,Object> map);
	//��� ����
	public int countComments(int b_no);
	
}
