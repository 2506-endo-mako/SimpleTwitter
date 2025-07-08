package chapter6.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/deleateMessage" })
public class DeleateMessageServlet extends HttpServlet {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public DeleateMessageServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	//★つぶやきの削除

	@Override
	//DBから削除するだけなので戻り値は無し
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//ログ（記録をとる）
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		//jsp(リクエスト)から値を取得している→削除するmessageの情報は一回きりで大丈夫◎なのでリクエスト
		//HTMLからくるのはだいたいString型
		int id = (Integer.parseInt(request.getParameter("id")));

		// request→一回だけ保持される　session→一定期間保持される
		//サービスを呼び出すdeleateメソッドの引数に、
		//リクエストの情報からmessagesテーブルにあるIDを取ったものを入れる
		//戻り値が無いから変数はいらない
		new MessageService().deleate(id);

		//トップ画面にリダイレクトする　DeleateMessagesServletからtop.jspにいくからリダイレクト
		response.sendRedirect("./");

	}

}
