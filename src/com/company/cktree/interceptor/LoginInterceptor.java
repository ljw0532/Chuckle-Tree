package com.company.cktree.interceptor;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.company.cktree.member.MemberService;

public class LoginInterceptor extends HandlerInterceptorAdapter {
	@Inject
	MemberService memberService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();	
		if (session.getAttribute("session_m_id") == null) { //�� �������� �ٽ� ���ö� (������ ������)
			Cookie autoLogin = WebUtils.getCookie(request, "autoLogin");
			if (autoLogin != null) { //������ ���� ���¿��� ��Ű�� autoLogin����� ������
				String sessionkey = autoLogin.getValue();
				Map<String, Object> map = memberService.checkUserWithSessionKey(sessionkey);
				if (map != null) { //�ڵ��α��� �Ⱓ�� ��ȿ�ϴٸ� �ڵ����� session�� ���� ���
					session.setAttribute("session_m_name", map.get("M_NAME"));
					session.setAttribute("session_m_id", map.get("M_ID"));
					session.setAttribute("session_m_no", map.get("M_NO"));
					session.setAttribute("session_m_password", map.get("M_PASSWORD"));
					session.setAttribute("session_m_image", map.get("M_IMAGE"));
				}
			}
		}
		return true;

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}
}
