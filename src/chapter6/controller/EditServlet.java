package chapter6.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet  extends HttpServlet {


    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public EditServlet() {
        InitApplication application = InitApplication.getInstance();
        application.init();

    }

	//★つぶやきの編集画面表示
  	@Override
  	protected void doGet(HttpServletRequest request, HttpServletResponse response)
  			throws ServletException, IOException {

  		log.info(new Object() {
  		}.getClass().getEnclosingClass().getName() +
  				" : " + new Object() {
  				}.getClass().getEnclosingMethod().getName());

  		//jsp(リクエスト)から値を取得している→編集するmessageの情報は一回きりで大丈夫なのでリクエスト
  		//HTMLからくるのはだいたいString型
  		//正規構文で振り分けたい　もしfalseだったら“不正なパラメータが入力されました”と表示する
  		if(request.getParameter("id").matches("^[0-9]+$")) {
			System.out.println("不正なパラメータが入力されました");
		}
  		int id = (Integer.parseInt(request.getParameter("id")));

  		// request→一回だけ保持される　session→一定期間保持される
  		//サービスを呼び出すdeleatメソッドの引数に、
  		//リクエストの情報からmessagesテーブルにあるIDを取ったものを入れる
  		//戻り値があるから変数が必要
  		Message message = new MessageService().select(id);

  		//取得してきたつぶやきの情報をjspで表示できるように、値をset
		//送り返す船に荷物を乗っける　"message"＝edit.jspの${message}の部分と連携している
  		request.setAttribute("message", message);

  		//編集画面にフォワードする(船を送り返す)
  		request.getRequestDispatcher("edit.jsp").forward(request, response);

  	}

  //★編集されたつぶやきを更新する
  	@Override
  	protected void doPost(HttpServletRequest request, HttpServletResponse response)
  			throws ServletException, IOException {

  		log.info(new Object() {
  		}.getClass().getEnclosingClass().getName() +
  				" : " + new Object() {
  				}.getClass().getEnclosingMethod().getName());

  		// request→一回だけ保持される　session→一定期間保持される
  		//jsp(リクエスト)から値を取得している→編集するmessageの情報は一回きりで大丈夫なのでリクエスト
  		//HTMLからくるのはだいたいString型
  		int id = (Integer.parseInt(request.getParameter("id")));

  		//テキストを取得
  		String text = request.getParameter("text");

  		//message 型の変数に、idとtextを入れてあげる
  		Message message = new Message();

  		//messageにidをセット
  		message.setId(id);

  		//messageにtextをセット
  		message.setText(text);

  		//Serviceを呼び出すupdateメソッドの引数に、
  		//リクエストの情報から取ったid,textを入れる
  		new MessageService().update(message);

  		//編集画面にリダイレクトする(仕事を依頼、画面表示はそっちでやってくれる）
  		response.sendRedirect("./");
  	}
}