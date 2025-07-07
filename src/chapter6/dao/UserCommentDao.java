package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.UserComment;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class UserCommentDao {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public UserCommentDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

public List<UserComment> select(Connection connection, Integer id, int num) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ");
			sql.append("    comments.text as text, ");
			sql.append("    comments.message_id as message_id, ");
			sql.append("    comments.created_date as created_date, ");
			sql.append("    users.account as account, ");
			sql.append("    users.name as name ");
			sql.append("FROM comments ");
			sql.append("INNER JOIN users ");
			sql.append("ON comments.user_id = users.id ");
			//取得した結果を降順に表示する-----------リミットが1000件
			sql.append("ORDER BY created_date ASC limit " + num);

			//箱作る
			ps = connection.prepareStatement(sql.toString());

			//実行する
			ResultSet rs = ps.executeQuery();

			//ResultSet型から、List<UserMessage>型に詰替えてるメソッドを呼ぶ(toUserMessages)
			List<UserComment> comments = toUserComments(rs);
			return comments;

		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//ResultSet型から、List<UserMessage>型に詰替える
	private List<UserComment> toUserComments(ResultSet rs) throws SQLException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<UserComment> comments = new ArrayList<UserComment>();
		try {
			while (rs.next()) {
				//beansの型で宣言
				UserComment comment = new UserComment();
				//set…詰めている
				comment.setMessageId(rs.getInt("message_id"));
				comment.setText(rs.getString("text"));
				comment.setAccount(rs.getString("account"));
				comment.setName(rs.getString("name"));
				comment.setCreatedDate(rs.getTimestamp("created_date"));

				//messageに詰め終わったらmessages(list)に入れる
				 comments.add(comment);
			}
			return  comments;
		} finally {
			close(rs);
		}
	}
}