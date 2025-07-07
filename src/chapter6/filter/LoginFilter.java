package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chapter6.beans.User;

@WebFilter(urlPatterns = { "/setting", "/edit" })
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		//キャスト(型変換のこと)　変換したい型　変数　=　（変換したい型）元々の変数
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		//もしログインユーザーの情報が無い→session領域に情報が無かったらログイン画面に遷移する

		//session領域からログインユーザー情報を持ってきたい

		User user = (User) req.getSession().getAttribute("loginUser");
		if (user == null) {
			List<String> errorMessages = new ArrayList<String>();
			errorMessages.add("ログインしてください");
			//sessionの宣言をしてから、session領域にエラーメッセージを詰めてリダイレクトする
			HttpSession session = req.getSession();
			session.setAttribute("errorMessages", errorMessages);
			//★リダイレクトだけど、top.jspではなくlogin.jspを表示させたい
			//★sendRedirectの宣言をしたい

			//ログイン画面のurlを確認
			res.sendRedirect("./login");
			return;
		}

		chain.doFilter(request, response); // サーブレットを実行

	}

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}

}
