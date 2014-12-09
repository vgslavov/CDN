<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<table>
	<tr>
		<td>
			Select a category:
		</td>
		<td align="left">
			<select>
				<option value="AL">Allergies</option>
				<option value="DDate">Discharge Date</option>
				<option value="DDiag">Discharge Diagnosis</option>
				<option value="DDisp">Discharge Disposition</option>
				<option value="HPI">History of Present Illness</option>
				<option value="HC">Hospital Course</option>
				<option value="PMH">Past Medical History</option>
				<option value="PSH">Past Surgical History</option>
				<option value="PE">Physical Exam</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>Enter keywords</td>
		<td align="left"><input type="text" id="keywords" value=""/> </td>
	</tr>
	<tr>
		<td></td>
		<td><input type="button" id="search_keywords_button" value="Run"/></td>
	</tr>
	<tr>
		<td colspan="2">
					<textarea rows="20" cols="70" id="output_display"></textarea>
		</td>
	</tr>
</table>
</body>
</html>