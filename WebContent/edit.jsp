<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${loginUser.account}の設定</title>
<link href="css/style.css" rel="stylesheet" type="text/css">
</head>
<!-- body要素始まり -->
<body>
	<!--単独では意味を持たない汎用的なブロックレベル要素をグループ化するためのタグ(div)-->
	<div class="main-contents">

		<!--指定された条件式を真 (true)と評価した場合にのみ、その内容をHTMLに出力する-->
		<c:if test="${ not empty errorMessages }">
			<div class="errorMessages">

				<!--順序がない箇条書きのリストを表示する-->
				<ul>

					<!--コレクションを反復処理する--><!--items属性 = コレクション　var属性 = 格納する変数-->
					<c:forEach items="${errorMessages}" var="errorMessage">

						<!--リストの項目を表す　ulまたはolまたはmenuの下に配置される(li)-->
						<!--変数の値を出力するタグでありJavaプログラム変数の値をHTMLへ出力する
						　value属性には出力する値を指定する-->
						<li><c:out value="${errorMessage}" />
					</c:forEach>
				</ul>
			</div>
		</c:if>

		<!-- action…どのServletにいきたいか　method…GetPostを指定 -->
		<!--formタグの中で不要なところと変更するところあり-->

		<!--編集フォームを作る(form)-->
		<form action="edit" method="post">

			<!--フォームの要素の見出しを表すタグ(label)-->
			<label for="name">つぶやきの編集</label>

			<br /> <input name="id" value="${message.id}" id="id" type="hidden" />

			<!--ユーザーが大量の自由記述テキストを入力できる(textarea)-->
			<!--valueで指定した値を出力する(c:out)-->
			<textarea name="text" cols="35" rows="5" id="edit"><c:out value="${message.text}" /></textarea>

			<!--改行する(br)-->
			<!--タグで作成したフォームの中でテキスト入力欄やボタンなどの部品を作成する要素(input)-->
			<!--リンク先を指定する属性(a href) -->
			<br /> <input type="submit" value="更新" /> <br /> <a href="./">戻る</a>

		</form>





		<!--単独では意味を持たない汎用的なブロックレベル要素をグループ化するためのタグ(div)-->
		<div class="copyright">Copyright(c)Your Name</div>
	</div>
</body>
</html>