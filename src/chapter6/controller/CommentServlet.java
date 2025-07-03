package chapter6.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chapter6.beans.Comment;
import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.CommentService;

@WebServlet(urlPatterns = { "/comment" })
public class CommentServlet extends HttpServlet {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public CommentServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	//★返信できる入力欄の表示
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		//変数sessionを宣言してる
		HttpSession session = request.getSession();
		//List<String> errorMessages = new ArrayList<String>();
		//jsp(リクエスト)から値を取得している
		String text = request.getParameter("text");
		int messageId = Integer.parseInt(request.getParameter("message_id"));


		//バリデーションチェック　falseの時にエラーメッセージをセッションにセット、リダイレクト
		//if (!isValid(text, errorMessages)) {
		//	session.setAttribute("errorMessages", errorMessages);
		//	response.sendRedirect("./");
		//	return;
		//}

		//登録（insert）するために必要　MessageIdが足りていない
		Comment comment = new Comment();
		comment.setText(text);
		comment.setMessageId(messageId);

		User user = (User) session.getAttribute("loginUser");
		comment.setUserId(user.getId());


		new CommentService().insert(comment);
		response.sendRedirect("./");
	}

}
