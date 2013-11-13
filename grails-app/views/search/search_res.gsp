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
    			$('#search').dataTable({
    				"sPaginationType": "full_numbers",
    			});
			});
  		</script>
	</head>
	<body>
		<h1>Results</h1>
		<table id="search">
            <thead>
				<tr><th>Mature</th><th>Family ID</th><th>Precursor</th><th>Sequence</th><th>Chromosome</th><th>StarBase</th><th>MiRTarBase</th><th>TargetScan</th></tr>
			</thead>
			<tbody>
				<g:each var="r" in="${mirRes}">
				<tr>
					<td><a href="http://mirbase.org/cgi-bin/mirna_entry.pl?acc=${r.matacc}" target="_blank">${r.matid}</a></td>
					<td><a href="http://mirbase.org/cgi-bin/mirna_summary.pl?fam=${r.famacc}" target="_blank">${r.famid}</a></td>
					<td><a href="http://www.mirbase.org/cgi-bin/mirna_entry.pl?acc=${r.preacc}" target="_blank">${r.preid}</a></td>
					<td>${r.matseq}</td><td><a href="http://asia.ensembl.org/Homo_sapiens/Location/View?db=core;r=${r.chr}:${r.start}-${r.stop}" target="_blank">${r.chr}: ${r.start}-${r.stop}</a></td>
					<td><g:link action="genes" params="[matid:r.matid, sEv:sEv, mEv:mEv]"><%=starMap."${r.matid}" %></g:link></td>
					<td><%=mtMap."${r.matid}" %></td>
					<td><%=tsMap."${r.matid}" %></td>
				</tr>
				</g:each>
			</tbody>
		</table>
		
	</body>
</html>
