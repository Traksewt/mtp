<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to Grails</title>
	
		
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_table.css')}" type="text/css"></link>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_page.css')}" type="text/css"></link>
		<script>
  			function demoMir(){
  				$("#mirList").val("hsa-miR-125a-3p\nhsa-miR-515-3p\nhsa-miR-652-3p");
  			}
  			$(document).ready(function() {
    			$('#search').dataTable();
			});
  		</script>
	</head>
	<body>
		<h1>Results</h1>
		<table id="search">
            <thead>
				<tr><th>Mature</th><th>Family ID</th><th>Precursor</th><th>Sequence</th><th>Chromosome</th><th>Start</th><th>End</th></tr>
			</thead>
			<tbody>
				<g:each var="r" in="${res}">
					<tr><td>${r.matid}</td><td>${r.famid}</td><td>${r.preid}</td><td>${r.matseq}</td><td>${r.chr}</td><td>${r.start}</td><td>${r.stop}</td></tr>
				</g:each>
			</tbody>
		</table>
		
	</body>
</html>
