package chapter6.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.User;
import chapter6.beans.UserComment;
import chapter6.beans.UserMessage;
import chapter6.logging.InitApplication;
import chapter6.service.CommentService;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/index.jsp" })
public class TopServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public TopServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	/*TOP画面表示　つぶやきが見れる*/
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		boolean isShowMessageForm = false;

		User user = (User) request.getSession().getAttribute("loginUser");
		if (user != null) {
			isShowMessageForm = true;
		}

		//※request.getParameterで開始日と終了日を取得
		//67行目でmessageIdとしてMessageServiceに送る
		//
		String startDate = request.getParameter("start_date");
		String endDate = request.getParameter("end_date");

		/*
		 * String型のuser_idの値をrequest.getParameter("user_id")で
		 * JSPから受け取るように設定
		 * MessageServiceのselectに引数としてString型のuser_idを追加
		 */
		String userId = request.getParameter("user_id");
		List<UserMessage> messages = new MessageService().select(userId, startDate, endDate);

		String messageId = request.getParameter("message_id");
		List<UserComment> comments = new CommentService().select(messageId);

		//送り返す船に荷物を乗っける　"messages"＝jspの${messages}の部分と連携している

		request.setAttribute("comments", comments);
		request.setAttribute("messages", messages);
		request.setAttribute("isShowMessageForm", isShowMessageForm);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);

		//船を送り返す
		request.getRequestDispatcher("/top.jsp").forward(request, response);
	}
}