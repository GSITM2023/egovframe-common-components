/**
 * @Class Name  : EgovSessionUtil.java
 * @Description : 세션 처리 관련 유틸리티
 * @Modification Information
 *
 *     수정일         수정자                   수정내용
 *     -------          --------        ---------------------------
 *   2009.02.13       이삼섭                  최초 생성
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 02. 13
 * @version 1.0
 * @see
 *
 */

package egovframework.com.utl.cas.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import egovframework.com.cmm.EgovWebUtil;

public class EgovSessionCookieUtil {

	/**
	 * HttpSession에 주어진 키 값으로 세션 정보를 생성하는 기능
	 *
	 * @param request
	 * @param keyStr - 세션 키
	 * @param valStr - 세션 값
	 * @throws Exception
	 */
	public static void setSessionAttribute(HttpServletRequest request, String keyStr, String valStr) throws Exception {

		HttpSession session = request.getSession();
		session.setAttribute(keyStr, valStr);
	}

	/**
	 * HttpSession에 주어진 키 값으로 세션 객체를 생성하는 기능
	 *
	 * @param request - HttpServletRequest 객체
	 * @param keyStr  - 설정할 세션의 키
	 * @param obj     - 설정할 세션의 값(객체)
	 * @throws Exception
	 */
	public static void setSessionAttribute(HttpServletRequest request, String keyStr, Object obj) throws Exception {

		HttpSession session = request.getSession();
		session.setAttribute(keyStr, obj);
	}

	/**
	 * HttpSession에 존재하는 주어진 키 값에 해당하는 세션 값을 얻어오는 기능
	 *
	 * @param request
	 * @param keyStr - 세션 키
	 * @return
	 * @throws Exception
	 */
	public static Object getSessionAttribute(HttpServletRequest request, String keyStr) throws Exception {

		HttpSession session = request.getSession();
		return session.getAttribute(keyStr);
	}

	/**
	 * HttpSession 객체내의 모든 값을 호출하는 기능
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getSessionValuesString(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String returnVal = "";

		Enumeration<?> e = session.getAttributeNames();
		while (e.hasMoreElements()) {
			String sessionKey = (String)e.nextElement();
			returnVal = returnVal + "[" + sessionKey + " : " + session.getAttribute(sessionKey) + "]";
		}

		return returnVal;
	}

	/**
	 * HttpSession에 존재하는 세션을 주어진 키 값으로 삭제하는 기능
	 *
	 * @param request
	 * @param keyStr - 세션 키
	 * @throws Exception
	 */
	public static void removeSessionAttribute(HttpServletRequest request, String keyStr) throws Exception {

		HttpSession session = request.getSession();
		session.removeAttribute(keyStr);
	}

	/**
	 * 쿠키생성 - 입력받은 분만큼 쿠키를 유지되도록 세팅한다.
	 * 쿠키의 유효시간은 minute 파라미터에 따라 설정되며, 최대 24시간으로 제한된다.
	 * 예) minute이 5이면, 쿠키의 유효시간을 5분으로 설정 =>(cookie.setMaxAge(60 * 5))
	 *
	 * @param response - Response
	 * @param cookieNm - 쿠키명
	 * @param cookieVal - 쿠키값
	 * @param minute - 지속시킬 시간(분, 최대 24시간)
	 * @return 없음
	 * @exception UnsupportedEncodingException
	 * @see
	 */
	public static void setCookie(HttpServletResponse response, String cookieNm, String cookieVal, int minute)
		throws UnsupportedEncodingException {

		// 특정의 encode 방식을 사용해 캐릭터 라인을 application/x-www-form-urlencoded 형식으로 변환
		// 일반 문자열을 웹에서 통용되는 'x-www-form-urlencoded' 형식으로 변환하는 역할
		String cookieValue = URLEncoder.encode(cookieVal, "utf-8");

		// 쿠키생성 - 쿠키의 이름, 쿠키의 값
		Cookie cookie = new Cookie(cookieNm, cookieValue);

		cookie.setSecure(true);

		cookie.setHttpOnly(true);//2022.01. Cookie without the HttpOnly flag 처리

		// 쿠키의 유효시간 설정
		int maxAge = 60 * minute;
		// KISA 보안약점 조치 (2018-10-29, 윤창원)
		if (maxAge > 60 * 60 * 24) {
			maxAge = 60 * 60 * 24;
		}
		cookie.setMaxAge(maxAge);

		// response 내장 객체를 이용해 쿠키를 전송
		response.addCookie(cookie);
	}

	/**
	 * 쿠키 생성 및 설정.
	 * 이 메서드를 사용하여 생성된 쿠키는 브라우저 세션 동안만 유지됩니다. (유효시간 설정이 없으므로)
	 *
	 * @param response - 웹 응답 객체
	 * @param cookieNm - 생성할 쿠키의 이름
	 * @param cookieVal - 생성할 쿠키의 값
	 * @throws UnsupportedEncodingException - UTF-8 인코딩을 지원하지 않는 경우 발생
	 */
	public static void setCookie(HttpServletResponse response, String cookieNm, String cookieVal)
			throws UnsupportedEncodingException {

		// 문자열을 'x-www-form-urlencoded' 형식으로 인코딩
		String cookieValue = URLEncoder.encode(cookieVal, "utf-8");

		// 쿠키 생성 (보안 위험을 줄이기 위해 CRLF 문자 제거)
		Cookie cookie = new Cookie(EgovWebUtil.removeCRLF(cookieNm), EgovWebUtil.removeCRLF(cookieValue));

		// 보안 설정
		cookie.setSecure(true);   // HTTPS에서만 쿠키 사용
		cookie.setHttpOnly(true); // 자바스크립트에서 쿠키 접근 방지

		// 응답에 쿠키 추가
		response.addCookie(cookie);
	}


	/**
	 * 쿠키값 사용 - 쿠키값을 읽어들인다.
	 *
	 * @param request - Request
	 * @param name - 쿠키명
	 * @return 쿠키값
	 * @exception
	 * @see
	 */
	public static String getCookie(HttpServletRequest request, String cookieNm) throws Exception {

		// 한 도메인에서 여러 개의 쿠키를 사용할 수 있기 때문에 Cookie[] 배열이 반환
		// Cookie를 읽어서 Cookie 배열로 반환
		Cookie[] cookies = request.getCookies();

		if (cookies == null) {
			return "";
		}

		String cookieValue = null;

		// 입력받은 쿠키명으로 비교해서 쿠키값을 얻어낸다.
		for (int i = 0; i < cookies.length; i++) {

			if (cookieNm.equals(cookies[i].getName())) {

				// 특별한 encode 방식을 사용해 application/x-www-form-urlencoded 캐릭터 라인을 디코드
				// URLEncoder로 인코딩된 결과를 디코딩하는 클래스
				cookieValue = URLDecoder.decode(cookies[i].getValue(), "utf-8");

				break;
			}
		}

		return cookieValue;
	}

	/**
	 * 쿠키값 삭제 - cookie.setMaxAge(0) - 쿠키의 유효시간을 0으로 설정해 줌으로써 쿠키를 삭제하는 것과 동일한 효과
	 *
	 * @param request - Request
	 * @param name - 쿠키명
	 * @return 쿠키값
	 * @exception
	 * @see
	 */
	public static void setCookie(HttpServletResponse response, String cookieNm) throws UnsupportedEncodingException {

		// 쿠키생성 - 쿠키의 이름, 쿠키의 값
		Cookie cookie = new Cookie(EgovWebUtil.removeCRLF(cookieNm), null);

		cookie.setSecure(true);

		cookie.setHttpOnly(true);//2022.01. Cookie without the HttpOnly flag 처리

		// 쿠키를 삭제하는 메소드가 따로 존재하지 않음
		// 쿠키의 유효시간을 0으로 설정해 줌으로써 쿠키를 삭제하는 것과 동일한 효과
		cookie.setMaxAge(0);

		// response 내장 객체를 이용해 쿠키를 전송
		response.addCookie(cookie);
	}
}