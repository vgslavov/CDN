<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Collaborative Data Network</title>


<script type="text/javascript" src="lib/jsTree/jquery.js"></script>
<script type="text/javascript" src="lib/jsTree/jquery.cookie.js"></script>
<script type="text/javascript" src="lib/jsTree/jquery.hotkeys.js"></script>
<script type="text/javascript" src="lib/jsTree/jquery.jstree.js"></script>

<script type="text/javascript">

$(document).ready(function () {
	$("#demo1").jstree({ 
		"json_data" : {
			"ajax" : {
				"url" : "_json_data.json"
			}
		},
		"themes" : {
			"theme" : "classic",
			"dots" : false,
			"icons" : true
			},
		"plugins" : [ "themes", "json_data", "ui" ]
	})
	.bind("select_node.jstree", function (e, data) {
		
		url = data.rslt.obj.attr("id");
		
		//alert(url);
		
		if(url != ""){
			$.get(url,{},function(results){

				$("#html_page").html(results);
				
			});
		}
		
	});
});

</script>
</head>
<body>

<table width="100%" cellspacing="25">
	<tr>
		<td><img alt="" src="images/pic1.jpg" width="100" height="100"/></td>	
		<td><h2>A Tool for Large scale sharing of HL7 v3 Documents</h2></td>
		<td><img alt="" src="images/pic2.gif"/></td>
	</tr>
	<tr>
		<td colspan="3"><hr/></td>
	</tr>
	<tr>
		<td style="width: 200px" valign="top"><!-- the tree container (notice NOT an UL node) -->
		<div id="demo1" class="demo"></div>
		</td>
		<td valign="top">
		<div id="html_page"></div>
		</td>
		<td></td>
	</tr>

	
</table>




</body>
</html>