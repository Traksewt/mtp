<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Test</title>
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.dataTables.js')}" type="text/javascript"></script>		
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_table.css')}" type="text/css"></link>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_page.css')}" type="text/css"></link>
		
		<script>
		$(document).ready(function() {
			$('#meta').dataTable({
					"bPaginate": false,  
      				"bFilter": false,     
					"aaSorting": [[ 1, "asc" ]],
					"iDisplayLength": 10,
                	"oLanguage": {
                        "sSearch": "Filter records:"
                	},
                	"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
                	"sDom": 'rt<"bottom"flp>'
					
				});
		});
    	</script>
	</head>
	<body>
	<h1>Choose some data</h1>
	<g:form action="screen_res">
	<table id="meta">
	<thead>
	<tr><th>Select</th><th>Type</th><th>Cell</th><th>Drug 1</th><th>Drug 2</th><th>Mimics</th><th>Inhibitors</th></tr>
	</thead>
	<tbody>
	<g:each var="r" in="${meta}">
		<tr><td><g:checkBox name="funCheck" value="${r.id}" checked="false"/></td><td>${r.type}</td><td>${r.cell}</td><td>${r.d1name}</td><td>${r.d2name}</td><td>${r.sd.findAll({it.library == 'mimics'}).size()}</td><td>${r.sd.findAll({it.library == 'inhibitors'}).size()}</td></tr>
	</g:each>
	</tbody>
	</table>
	<br>
	<h1>Set some paramaters (leave blank to ignore)</h1>
	<table>
		<tr>
			<td>Vehicle</td>
			<td>
				<select name="vSelect">					
				<option value="lt"><</option>
				<option value="gt">></option>
				<option value="eq">=</option>
			</td>	
			<td><g:textField name="vValue" value="0.5" /></td>
		</tr>
		<tr>
			<td>Drug 1</td>
			<td>
				<select name="d1Select">					
				<option value="lt"><</option>
				<option value="gt">></option>
				<option value="eq">=</option>
			</td>	
			<td><g:textField name="d1Value"/></td>
		</tr>
		<tr>
			<td>Drug 2</td>
			<td>
				<select name="d2Select">					
				<option value="lt"><</option>
				<option value="gt">></option>
				<option value="eq">=</option>
			</td>	
			<td><g:textField name="d2Value"/></td>
		</tr>
	</table>
		<select name="library">					
			<option value="mim">Mimics</option>
			<option value="in">Inhibitors</option>
			<br><br>
			<input class="smallbuttons" type="button" value="Search" id="process" onclick="submit()" >
	</g:form>
	</body>
</html>
