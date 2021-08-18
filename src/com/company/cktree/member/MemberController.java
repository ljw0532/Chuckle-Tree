package com.company.cktree.member;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import com.company.cktree.authentications.AuthenDAO;
import com.company.cktree.authentications.AuthenDTO;
import com.company.cktree.board.BoardDTO;
import com.company.cktree.comments.CommentsDTO;
import com.company.cktree.exception.AlreadyExistingEmailException;
import com.company.cktree.exception.AlreadyExistingIdException;
import com.company.cktree.util.Email;
import com.company.cktree.util.EmailSender;
import com.company.cktree.util.Pager;
import com.company.cktree.validator.JoinRequestValidator;
import com.company.cktree.validator.MemberModifyValidator;


@Controller
public class MemberController {
	@Inject
	private MemberService memberService;
	@Inject
	private MemberDAO memberDAO;
	@Inject
	private EmailSender emailSender;
	@Inject
	private Email email;
	
	@Resource(name = "profileImagePath")
	String profileImagePath;
	
	@Inject
	private AuthenDAO authenDAO;
	//���� ���丮 ���
	String path="C:\\project\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\Chuckle_Tree\\resources\\images\\";

	
	// �α��������� �̵�
	@RequestMapping("loginForm")
	public String loginForm() {
	
		return "member/loginForm";
	}

	// ȸ������������ �̵�
	@RequestMapping("joinForm/{authen_no}")
	public String joingForm(@PathVariable int authen_no, Model model) {
		Map<String,Object> dto = authenDAO.selectAuthenNo(authen_no);
		
		if(dto == null) { //�������̺� ����� ������ ���� ���(url�� ���� �ּҸ� ġ�� ���� ���) �������� �����̷�Ʈ
			return "redirect:/";
		} else {
		
		model.addAttribute("authen",dto);
		return "join/joinForm";
		}
	}

	// ���̵�ã�������� �̵�
	@RequestMapping("findId")
	public String findIdForm() {
		return "member/findIdForm";
	}

	// ��й�ȣã�������� �̵�
	@RequestMapping("findPw")
	public String findPwForm() {
		return "member/findPwForm";
	}

	// �����ϱ�
	@ResponseBody
	@RequestMapping(value = "joinForm", method=RequestMethod.POST)
	public boolean join(@ModelAttribute("member") MemberDTO member,BindingResult errors, HttpServletRequest request,
			HttpServletResponse response) throws Exception {	
		new JoinRequestValidator().validate(member, errors);
		if (errors.hasErrors()) {
			return false;
		}
		try {
			memberService.insertMember(member, request, response);
			int authen_no = Integer.parseInt(request.getParameter("authen_no").toString());
			authenDAO.deleteEmailName(authen_no);
		} catch (AlreadyExistingEmailException e) {
			errors.rejectValue("m_email", "duplicate", "�̹� ���Ե� �̸����Դϴ�.");
			return false;
		} catch (AlreadyExistingIdException e) {
			errors.rejectValue("m_id", "duplicate", "������� ���̵��Դϴ�.");
			return false;
		}

		return true;
	 
	}
	// �α���
	@RequestMapping(value = "loginForm", method=RequestMethod.POST)
	public String login(MemberDTO member, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		MemberDTO m_dto = memberService.loginCheck(member, session);
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		
		if (m_dto != null) { // �α��� ������	
			if (request.getParameter("autoLogin") != null) { // �ڵ� �α��� üũ��
				Cookie autoLogin = new Cookie("autoLogin", session.getId());
				autoLogin.setPath("/");
				int amount = 60 * 60 * 24 * 7;
				autoLogin.setMaxAge(amount);
				response.addCookie(autoLogin);
				String session_id = session.getId();
				Date session_limit = new Date(System.currentTimeMillis() + (1000 * amount));
				memberService.keepLogin(member.getM_id(), session_id, session_limit);
			}
		
			if(m_dto.getActive().equals("N")) { //��Ȱ��ȸ���̸� ���� ���� �� �����̷�Ʈ
				session.invalidate();
				return "alert/inactiveAlert";
			}
			
			return "redirect:/";		
		} else { // �α��� ���н�
			
			if(memberDAO.loginIdCheck(member.getM_id()) == null) { //��ϵ� ���̵� ���� ���
				writer.write("<script type='text/javascript'> alert('��ϵ� ���̵� �����ϴ�.') </script>");
				writer.flush();
				return "member/loginForm";
			} else { //���̵� ��й�ȣ�� ��ġ���� ���� ���		
				writer.write("<script type='text/javascript'> alert('���̵� �Ǵ� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.') </script>");
				writer.flush();
			return "member/loginForm";
			}
		}
	}

	// �α׾ƿ�
	@RequestMapping("logout")
	public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		Object object = session.getAttribute("session_m_id");
		if (object != null) {
			String member = (String) object;
			session.invalidate();
			Cookie loginCookie = WebUtils.getCookie(request, "autoLogin");
			if (loginCookie != null) {
				loginCookie.setPath("/");
				loginCookie.setMaxAge(0);
				response.addCookie(loginCookie);
				memberService.keepLogin(member, "none", new Date());
			}
		}
		return "redirect:/";
	}

	// ���̵� ã��
	@RequestMapping(value = "findId", method=RequestMethod.POST)
	public String findId(@ModelAttribute MemberDTO member, Model model) throws Exception {
		MemberDTO dto = memberService.findId(member);
		String member_email = member.getM_email();
		String member_name = member.getM_name();
			if (dto != null) {
			String member_id = dto.getM_id();
			email.setSubject("Chuckle Tree! " + member_name + "ȸ������ ��û�Ͻ� �����Դϴ�.");
			email.setReceiver(member_email);
			email.setContent("ȸ������ ���̵�� " + member_id + " �Դϴ�.");
			emailSender.SendEmail(email);
			model.addAttribute("message", "success");
			return "alert/findAlert";
		} else {
			model.addAttribute("message", "id_fail");
			return "alert/findAlert";
		}
	}

	// ��й�ȣ ã��
	@RequestMapping(value = "findPw", method=RequestMethod.POST)
	public String findPw(@ModelAttribute MemberDTO member, Model model) throws Exception {
		MemberDTO dto = memberService.findPw(member);
		if (dto != null) {
			model.addAttribute("message", "success");
			return "alert/findAlert";
		} else {
			model.addAttribute("message", "pw_fail");
			return "alert/findAlert";
		}
	}

	// ���̵� �ߺ� üũ
	@ResponseBody
	@RequestMapping(value = "checkId", method=RequestMethod.POST)
	public int checkId(String m_id) {
		int result = memberService.checkId(m_id);
		return result;
	}

	// �̸��� �ߺ� üũ
	@ResponseBody
	@RequestMapping(value = "checkEmail", method=RequestMethod.POST)
	public int checkEmail(String m_email) {
		int result = memberService.checkEmail(m_email);
		return result;
	}
	
	
	//ȸ������ ���������� �̵�
	@RequestMapping("member/memberModifyForm")
	public String memberModifyForm(HttpSession session,Model model) {	
		int m_no = Integer.parseInt(String.valueOf(session.getAttribute("session_m_no")));
		model.addAttribute("member",memberService.memberInfo(m_no));
		return "profile/memberModifyForm";
	}
	
	//UUID����
 	private String uploadFile(String fileName, byte[] fileData) throws Exception {	
		UUID uid = UUID.randomUUID(); 
		String savedName = uid.toString() + "_" +fileName;	
		File target = new File(profileImagePath, savedName);
		FileCopyUtils.copy(fileData, target);
		return savedName;
	}
	
	//ȸ������ ����
	@ResponseBody
	@RequestMapping("member/memberModify")
	public boolean memberModify(@ModelAttribute("member")MemberDTO member , 
			BindingResult errors, MultipartHttpServletRequest request, HttpSession session) throws Exception {
		MultipartFile file = request.getFile("file");
		new MemberModifyValidator().validate(member,errors);
		int m_no = Integer.parseInt(String.valueOf(session.getAttribute("session_m_no")));
		if(errors.hasErrors()) {
			return false;
		} 
		
		try {
			if(!file.isEmpty()) {
				byte[] fileByte = file.getBytes(); //���� �뷮
				String fileName = file.getOriginalFilename();//���� �̸�
				String uuidFileName = uploadFile(fileName,fileByte);//UUID��ģ �̸�
				member.setM_image(uuidFileName);
				new File(path).mkdir();
				member.getFile().transferTo(new File(path + fileName));
				memberService.memberModify(member,request);		
				session.setAttribute("session_m_image", uuidFileName);
			} else	{
				String profileImage = memberDAO.selectMyProfileImage(m_no); //������ ������ �������� ���� ��� ������ ������ �� 
				member.setM_image(profileImage);
				memberService.memberModify(member,request);					
			}
		} catch (AlreadyExistingEmailException e) {
			errors.rejectValue("m_email", "duplicate", "�̹� ���Ե� �̸����Դϴ�.");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//ȸ��Ż��
	@RequestMapping("member/myProfile/leaveId")
	public String leaveId(HttpSession session) {
		int m_no = Integer.parseInt(String.valueOf(session.getAttribute("session_m_no")));
		memberService.leaveId(m_no);
		session.invalidate();
		return "redirect:/";
	}
	
	//ȸ������ ��ȸ(������)
	@RequestMapping(value="member/myProfile/{m_no}", method=RequestMethod.GET)
	public String myProfile(@PathVariable int m_no,HttpSession session, Model model) {
		int session_m_no;		
		if(session.getAttribute("session_m_no")!= null) { 
		session_m_no = Integer.parseInt(String.valueOf(session.getAttribute("session_m_no"))); 
		} else {
		session_m_no = 0;	
		}
	
		if(session.getAttribute("session_m_no") == null || session_m_no != m_no) {
			return "redirect:/";
		} //���� ���ǰ��� ���ų� �������� ȸ����ȣ�� ���� ������ ȸ����ȣ�� ��ġ���� ������ �������� �����̷�Ʈ 
		
		model.addAttribute("member",memberService.memberInfo(m_no));
		return "profile/myProfile";
	}	
	
	//��й�ȣ ���� �������� �̵�
	@RequestMapping("/member/myProfile/pwChange")
	public String pwChangeForm() {
		return "profile/pwChange";
	}
	
	//���� ���� �ִ� ��й�ȣ���� Ȯ��
	@ResponseBody
	@RequestMapping("/member/myProfile/checkMyPw")
	public boolean checkMyPw(int m_no, String m_password) {
		if(memberDAO.checkMyPw(m_no, m_password)!=null) {
			return true;
		} else {
			return false;
		}
	}
	
	//��й�ȣ ����
	@ResponseBody
	@RequestMapping(value="/member/myProfile/pwChange", method=RequestMethod.POST)
	public Map<String,Object> pwChange(int m_no, String org_pw, String m_password,HttpSession session) {
		Map<String,Object> map = new HashMap<>();
		map.put("result", memberService.pwChange(m_no, org_pw, m_password,session));
		return map;
	}
	
	//myProfile - ���� �� ��
	@ResponseBody
	@RequestMapping("member/myProfile/myPosts")
	public Map<Object,Object> myPosts(int m_no, int end) {		
		 List<BoardDTO> list = memberDAO.myPosts(m_no,end);
		return selectList("myPosts_list",list);
	}
	
	//myProfile - ���� �� ���
	@RequestMapping("member/myProfile/myComments")
	@ResponseBody
	public Map<Object,Object> myComments(int m_no, int end) {
		List<CommentsDTO> list = memberDAO.myComments(m_no,end);		
		return selectList("myComments_list",list);
	}
	
	//myProfile - ���� ���� ���ƿ�
	@RequestMapping("member/myProfile/myLikeit")
	@ResponseBody
	public Map<Object,Object> myLikeit(int m_no, int end) {	
		List<BoardDTO> list = memberDAO.myLikeit(m_no,end);	
		return selectList("myLikeit_list",list);
	}
	
	//Ÿ ȸ�� ������
	@RequestMapping("/user/Profile/{m_no}")
	public String userProfile(@PathVariable int m_no,Model model) {
		model.addAttribute("user", memberService.selectUserInfo(m_no));
		return "/profile/userProfile";
	}
	
	//�ٸ� ȸ�� �ۼ��� ��ȸ
	@ResponseBody
	@RequestMapping("/user/Posts")
	public Map<Object,Object> userPosts(int m_no, int end) {
		List<BoardDTO> list = memberDAO.myPosts(m_no,end);	
		return selectList("userPosts_list",list);
	}
	
	//�ٸ� ȸ�� �ۼ� ��� ��ȸ
	@ResponseBody
	@RequestMapping("/user/Comments")
	public Map<Object,Object> userComments (int m_no, int end) {
		List<CommentsDTO> list = memberDAO.myComments(m_no,end);
		return selectList("userComments_list",list);
	}
		
	//����Ʈ ��ȸ �޼���
	private Map<Object,Object> selectList( String alias, List list){
		Map<Object,Object> result = new HashMap<>();
		result.put(alias,list);
		return result;
	}
}
