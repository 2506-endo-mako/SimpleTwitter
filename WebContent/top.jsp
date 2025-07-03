<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- htmlが始まる -->
<html>
<!--bodyの前につく -->
<head>
<!-- webページの情報を検索エンジンやブラウザに伝える（meta） -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- タイトル -->
<title>簡易Twitter</title>
<!-- 現在の文書と外部のリソースとの関係を表す -->
<link href="./css/style.css" rel="stylesheet" type="text/css">
</head>
<!-- 表示内容を記述する部分 -->
<body>
	<!-- コンテンツをグループ化するための汎用的なブロックレベルの要素 -->
	<div class="main-contents">
		<!-- ブロック -->
		<div class="header">
			<!-- 条件分岐（｛　｝内がtrueだったら...） -->
			<c:if test="${ empty loginUser }">
				<!--リンク先のurlを指定する(a href) -->
				<!--下記の"login"でServletの"login"に飛ぶ-->
				<a href="login">ログイン</a>
				<a href="signup">登録する</a>
			</c:if>
			<c:if test="${ not empty loginUser }">
				<a href="./">ホーム</a>
				<a href="setting">設定</a>
				<a href="logout">ログアウト</a>
			</c:if>
		</div>
		<!-- ログインしていなかったら表示しない -->
		<c:if test="${ not empty loginUser }">
			<div class="profile">
				<div class="name">
					<!-- 見出しを表す -->
					<h2>
						<c:out value="${loginUser.name}" />
					</h2>
				</div>
				<div class="account">
					<!-- 変数の値を出力する（c:out） -->
					<c:out value="${loginUser.account}" />
				</div>
				<div class="description">
					<!-- 変数の値を出力する（c:out） -->
					<c:out value="${loginUser.description}" />
				</div>
				<c:if test="${ not empty errorMessages }">
					<div class="errorMessages">
						<!-- 順序無しリスト (ul)-->
						<ul>
							<!-- 繰り返し処理する（forEach)-->
							<c:forEach items="${errorMessages}" var="errorMessage">
								<!-- リスト項目を表す（ulまたはol内で使用する）（li） -->
								<li><c:out value="${errorMessage}" />
							</c:forEach>
						</ul>
					</div>
					<!-- jspページで変数の削除をする -->
					<c:remove var="errorMessages" scope="session" />
				</c:if>
			</div>
		</c:if>
		<div class="form-area">
			<c:if test="${ isShowMessageForm }">
				<!-- フォームを作る（form） -->
				<form action="message" method="post">
					いま、どうしてる？<br />
					<!-- □の中に書き込める -->
					<textarea name="text" cols="100" rows="5" class="tweet-box"></textarea>
					<!-- 改行（br） -->
					<!--タグで作成したフォームの中でテキスト入力欄やボタンなどの部品を作成する要素(input)-->
					<br />
					<input type="submit" value="つぶやく">（140文字まで）
				</form>
			</c:if>
		</div>
		<div class="messages">
			<!-- topServletで詰めたmessagesの内容を繰り返し表示(forEach)-->
			<c:forEach items="${messages}" var="message">
				<div class="message">
					<div class="account-name">
						<!--文章中の特定の要素をグループ化する (span)-->
						<span class="account">
						<a href="./?user_id=<c:out value="${message.userId}"/> ">
							<c:out value="${message.account}" /></a></span>
						<span class="name"> <c:out value="${message.name}" /></span>
					</div>
					<div class="text">
						<!-- 整形済みテキストを表示する(空白にも改行にも対応しちゃう！)(pre)-->
						<pre><c:out value="${message.text}" /></pre>
					</div>
					<div class="date">
						<!-- 見た目やレイアウトを調節する -->
						<fmt:formatDate value="${message.createdDate}"
							pattern="yyyy/MM/dd HH:mm:ss" />
					</div>
				</div>

				<!--条件分岐でfalseだったらボタンは非表示にしたい→formの外へ-->
				<c:if test="${message.userId == loginUser.id}">

					<!-- ★つぶやきの削除ボタン -->
					<!-- action…どのServletにいきたいか　method…GetPostを指定 -->
					<form action="deleatMessage" method="post">
						<input name="id" value="${message.id}" id="id" type="hidden" />
						<input type="submit" value="削除" />
					</form>
				</c:if>
				<!-- つぶやいた人と、ログインしている人のidが一緒かどうか -->
				<c:if test="${message.userId == loginUser.id}">

					<!-- ★つぶやきの編集ボタン -->
					<!-- action…どのServletにいきたいか　method…GetPostを指定 -->
					<form action="edit" method="get">
						<!--タグで作成したフォームの中でテキスト入力欄やボタンなどの部品を作成する要素(input)-->
						<input name="id" value="${message.id}" id="id" type="hidden" /> <input
							type="submit" value="編集" />
					</form>
				</c:if>

				<!-- ログインしていなかったら表示しない -->
				<c:if test="${ not empty loginUser }">
					<div class="profile">
						<div class="name"></div>
						<!-- ★返信ボタンとテキストエリア -->
						<form action="comment" method="post">
							<pre><textarea name="text" cols="35" rows="5" id="edit"><c:out value="${comment.text}" /></textarea></pre>
							<input name="message_id" value="${message.id}" id="id" type="hidden" />
							<input type="submit" value="返信" />
						</form>
					</div>
				</c:if>
				<br>
			</c:forEach>
		</div>
	</div>
	<div class="copyright">Copyright(c)Endo_Mako</div>
</body>
</html>