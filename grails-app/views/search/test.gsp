<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Test</title>
	</head>
	<body>
	<h1>Choose some data</h1>
	<g:each var="r" in="${meta}">
		<g:checkBox name="myCheckbox"/>${r.type} - ${r.cell}<br>
	</g:each>
	</body>
</html>
