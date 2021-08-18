package com.company.cktree.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.cktree.member.MemberDAO;
import com.company.cktree.member.MemberDTO;
import com.company.cktree.util.Pager;

@Controller
public class ReportController {

	@Inject
	MemberDAO memberDAO;
	@Inject
	ReportDAO reportDAO;
	
	//�Ű��� �̵�
	@RequestMapping("/reportForm/{m_no}")
	public String reportForm(@PathVariable int m_no,Model model) {
		MemberDTO dto = memberDAO.selectUserInfo(m_no);
		model.addAttribute("m_no", dto.getM_no());
		model.addAttribute("m_name", dto.getM_name());
		return "member/report"; 
	}
	
	//�Ű��� DB�� ����
	@ResponseBody
	@RequestMapping("/insertReport")
	public String insertReport(@ModelAttribute("report") ReportDTO report) {
		String result = "success";
		reportDAO.insertReport(report);
		return result;
	}
	
	//������ �������� �Ű��� ����Ʈ �ҷ�����
	@RequestMapping("/admin/selectReportList")
	public String selectReportList(@RequestParam(defaultValue="1")int curPage, Model model,HttpSession session) {
		if(!session.getAttribute("session_m_id").equals("admin")) {
			return "redirect:/";
		} else {
		Map<String,Object> map = new HashMap<>();
		int count = reportDAO.countReportList();
		Pager pager = new Pager(count,curPage);
		int start = pager.getPageBegin();
		int end = pager.getPageEnd();
		map.put("start", start);
		map.put("end", end);
		List<ReportDTO> list = reportDAO.selectReportList(map);
		model.addAttribute("list",list);
		model.addAttribute("pager",pager);
		return "admin/adminPage";
		}	
	}
	
	@RequestMapping(value="/givePenalty", method=RequestMethod.POST)
	public String givePenalty(HttpServletRequest request) {
		int m_no = Integer.parseInt(request.getParameter("m_no").toString());
		int report_no = Integer.parseInt(request.getParameter("report_no").toString());
		reportDAO.givePenalty(m_no); //���Ƽ ���� �ø��� dao
		reportDAO.completePenaltyDate(report_no); //�Ϸ�ð� ������Ʈ�ϴ� dao
		reportDAO.inactiveMember(m_no); //���Ƽ�� 5�̻��� ȸ�� ��Ȱ���ϴ� dao
		return "redirect:/admin/selectReportList";
	}
}
