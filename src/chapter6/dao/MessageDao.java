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

import chapter6.beans.Message;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class MessageDao {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public MessageDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	public void insert(Connection connection, Message message) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			//箱を作る
			sql.append("INSERT INTO messages ( ");
			sql.append("    user_id, ");
			sql.append("    text, ");
			sql.append("    created_date, ");
			sql.append("    updated_date ");
			sql.append(") VALUES ( ");
			sql.append("    ?, "); // user_id
			sql.append("    ?, "); // text
			sql.append("    CURRENT_TIMESTAMP, "); // created_date
			sql.append("    CURRENT_TIMESTAMP "); // updated_date
			sql.append(")");
			//接続する
			ps = connection.prepareStatement(sql.toString());
			//値をセットする
			ps.setInt(1, message.getUserId());
			ps.setString(2, message.getText());
			//実行する
			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//★つぶやきの削除
	public void deleate(Connection connection, int id) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			String sql = "DELETE FROM messages WHERE id = ?";
			//箱作る
			ps = connection.prepareStatement(sql);
			//セットする
			ps.setInt(1, id);
			//実行する
			ps.executeUpdate();

		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//★つぶやきの編集画面に、1つのぶやきを表示させるためのsql
	public Message select(Connection connection, int id) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM messages WHERE id = ?";
			//SQL実行用の箱作る
			ps = connection.prepareStatement(sql);
			//セットする
			ps.setInt(1, id);
			//実行する
			ResultSet rs = ps.executeQuery();

			//ResultSet型から、List<Message>型に変換するメソッドを呼ぶ(toMessages)
			List<Message> messages = toMessages(rs);
			//message型で返してあげたい
			if (messages.isEmpty()) {
				return null;
			} else {
				return messages.get(0);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//★編集されたつぶやきを更新するためのsql
	public void update(Connection connection, Message message) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE messages SET ");
			sql.append("    text = ? ,");
			//更新日時も増やす
			sql.append("    updated_date = CURRENT_TIMESTAMP ");
			sql.append("WHERE id = ?");

			//SQL実行用の箱作る
			ps = connection.prepareStatement(sql.toString());

			//セットする
			ps.setString(1, message.getText());
			ps.setInt(2, message.getId());
			//実行する
			ps.executeUpdate();

		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<Message> toMessages(ResultSet rs) throws SQLException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<Message> messages = new ArrayList<Message>();
		try {
			while (rs.next()) {
				//beansの型で宣言
				Message message = new Message();
				//set…詰めている
				message.setId(rs.getInt("id"));
				message.setText(rs.getString("text"));

				//messageに詰め終わったらmessage(list)に入れる
				messages.add(message);
			}
			return messages;
		} finally {
			close(rs);
		}
	}

}