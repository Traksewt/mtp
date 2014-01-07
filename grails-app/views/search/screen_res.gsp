<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Test</title>
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'TableTools-2.1.5/media/js/TableTools.js')}" type="text/javascript"></script>
        <script src="${resource(dir: 'js', file: 'TableTools-2.1.5/media/js/ZeroClipboard.js')}" type="text/javascript"></script>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_table.css')}" type="text/css"></link>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_page.css')}" type="text/css"></link>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'TableTools-2.1.5/media/css/TableTools.css')}" type="text/css"></link>
		
		<script>
			$(document).ready(function() {
			$('#res').dataTable({
					"sPaginationType": "full_numbers",
					"aaSorting": [[ 3, "asc" ]],
					"iDisplayLength": 10,
                	"oLanguage": {
                        "sSearch": "Filter records:"
                	},
                	"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
                	"sDom": 'T<"clear">lfrtip',
                	"oTableTools": {
                        "sSwfPath": "${resource(dir: 'js', file: 'TableTools-2.1.5/media/swf/copy_csv_xls_pdf.swf')}",
                	}
					
				});
		});
    	</script>
	</head>
	<body>
		<table id="res">
            <thead>
				<tr><th>Source</th><th>MiRBase IDs</th><th>Seed</th><th>Location</th><th>Vehicle</th><th>Drug 1</th><th>Drug 2</th></tr>
			</thead>
			<tbody>
				<g:each var="r" in="${s}">
				<tr>
					<td>${r.type} : ${r.cell}</td>
					<td><a href="http://mirbase.org/cgi-bin/mirna_entry.pl?acc=${r.matacc}" target="_blank">${r.matacc}</a><br>${r.matid}</td>
					<td><font face="courier new">${r.matseq.toUpperCase()[1..6]}</font></td>
					<!--td><font face="courier new">${r.matseq.toUpperCase()[0]}<b>${r.matseq.toUpperCase()[1..6]}</b>${r.matseq.toUpperCase()[6..-1]}</font></td-->
					<td><a href="http://asia.ensembl.org/Homo_sapiens/Location/View?db=core;r=<%="${r.chr}".replaceAll("chr","")%>:${r.start}-${r.stop}" target="_blank">${r.chr}: ${r.start}-${r.stop}</a></td>
					<td>${r.veh}</td><td>${r.d1}</td><td>${r.d2}</td>
				</tr>
				</g:each>
			</tbody>
		</table>

	</body>
</html>
