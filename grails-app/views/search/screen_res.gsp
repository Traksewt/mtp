<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Search Results</title>
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
					"aaSorting": [[ 1, "asc" ]],
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
		<h1>Results</h1>
		<g:if test="${s == 'noMatch'}">
			<br><h3>No match was found, please go <a href="previous.html" onClick="history.back();return false;">back</a> and try again</h3>
		</g:if>
		<g:else>
			<table>
				<tr>
					<td><b>Search results</b></td>
					<td>${mList.size()} miRNAs were found in ${duration}.</td>
				</tr>
				<g:if test="${mList.size() > 0}">
					<tr>
						<td><b>Explore the gene target data</b></td>
						<td>
							<g:form name="screen" url="[action:'search_res']">
							<g:hiddenField name="screen" value="true"/>
								Select the level of StarBase evidence  
								<select name="sEv">
									<option value="0">1 or more</option>
									<option value="1">2 or more</option>
									<option value="2">3 or more</option>
									<option value="3">4 or more</option>
									<option value="4">5</option>
								</select>
								and then search:
								<input class="smallbuttons" type="button" value="Search" id="process" onclick="submit()" >
							</g:form> 
						</td>	
					</tr>
				</g:if>
				</table>
			
				<g:if test="${mList.size() > 0}">					
				<table id="res">
				<thead>
					<tr><th>Source</th><th>MiRBase ID</th><th>MiRBase Accession</th><th>Seed</th><th>Location</th><th>Vehicle</th><th>Drug 1</th><th>Drug 2</th></tr>
				</thead>
				<tbody>
					<g:each var="r" in="${s}">
					<tr>
						<td>${r.type} : ${r.cell} : ${r.library}</td>
						<td>${r.matid}</td>
						<td><a href="http://mirbase.org/cgi-bin/mirna_entry.pl?acc=${r.matacc}" target="_blank">${r.matacc}</a></td>
						<td><font face="courier new">${r.matseq.toUpperCase()[1..6]}</font></td>
						<!--td><font face="courier new">${r.matseq.toUpperCase()[0]}<b>${r.matseq.toUpperCase()[1..6]}</b>${r.matseq.toUpperCase()[6..-1]}</font></td-->
						<td><a href="http://asia.ensembl.org/Homo_sapiens/Location/View?db=core;r=<%="${r.chr}".replaceAll("chr","")%>:${r.start}-${r.stop}" target="_blank">${r.chr}: ${r.start}-${r.stop}</a></td>
						<td>${r.veh}</td><td>${r.d1}</td><td>${r.d2}</td>
					</tr>
					</g:each>
				</tbody>
			</table>
				</g:if>
				<g:else>
					Go <a href="previous.html" onClick="history.back();return false;">back</a> and try again
				</g:else>
		</g:else>
		
	</body>
</html>
