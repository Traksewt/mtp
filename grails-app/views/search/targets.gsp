<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Target Search Results</title>
	
		
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'Highcharts-3.0.7/js/highcharts.js')}" type="text/javascript"></script>
        <script src="${resource(dir: 'js', file: 'Highcharts-3.0.7/js/modules/exporting.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'canvasxpress/build/js/canvasXpress.min.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'TableTools-2.1.5/media/js/TableTools.js')}" type="text/javascript"></script>
        <script src="${resource(dir: 'js', file: 'TableTools-2.1.5/media/js/ZeroClipboard.js')}" type="text/javascript"></script>
        <script src="${resource(dir: 'js', file: 'd3/d3.v3.min.js')}" type="text/javascript"></script>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_table.css')}" type="text/css"></link>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_page.css')}" type="text/css"></link>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'TableTools-2.1.5/media/css/TableTools.css')}" type="text/css"></link>
	
  		<script>
  			$(document).ready(function() {			
				$('#common').dataTable({
					"sPaginationType": "full_numbers",
					"aaSorting": [[ 5, "desc" ]],
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
		<h1>Target Search results</h1>
		<table>
		<tr>
			<td><b>Search results</b></td>
			<td>${sprintf("%,d\n",commonGeneList.size())} gene targets were found in the database in ${duration}.</td>
		</tr>
		<tr>
			<td><b>Network</b></td>
			<td>
			<g:form name="network" action="network">
				Auto <input type="radio" name="networkType" value="a" checked="true"> 
				Top <select name="top">
					<option value=5>5</option>
					<option value=10>10</option>
					<option value=20>20</option>
					<option value=50>50</option>
				</select> genes | 
				Custom <input type="radio" name="networkType" value="c">
				<input type="submit" value="search">
			</g:form>
            </td>	
          </tr>
          <tr>
			<td><b>Heatmap</b></td>
            <td>
			<g:form name="heatmap" action="heatmap"> 
				Top <select name="top">
					<option value=5>5</option>
					<option value=10>10</option>
					<option value=20>20</option>
					<option value=50>50</option>
				</select> genes
				<input type="submit" value="search">
			</g:form>
            </td>	
		</tr>
		<tr>
			<td><b>Enrichr GSEA</b></td>
            <td>
			<g:form name="enrichr" action="enrichr">
				Top <select name="top">
					<option value=5>5</option>
					<option value=10>10</option>
					<option value=20>20</option>
					<option value=50>50</option>
				</select> genes
				<input type="submit" value="search">
			</g:form>
            </td>	
		</tr>
		</table>
		<h3>Gene counts</h3>
		The table below lists the predicted gene targets. Count represents the number of times each gene was predicted to be the target of an independent miRNA.
		<table id="common">
            <thead>
				<tr><th>Gene symbol</th><th>Ensembl ID</th><th>UniProt ID</th><th>Name</th><th width="30%">Location</th><th>Count</th><th>S</th><th>T</th><th>D</th><th>M</th></tr>
			</thead>
			<tbody>
				<g:each var="r" in="${commonGeneList}">
				<tr>
					<td><a href = "http://www.genecards.org/cgi-bin/carddisp.pl?gene=${r.name}" target="_blank">${r.name}</a></td>
					<td><g:if test="${r.ensembl != 'n/a'}"><a href = "http://www.ensembl.org/id/${r.ensembl}">${r.ensembl}</a></g:if><g:else>${r.ensembl}</g:else></td>
					<td><g:if test="${r.uniprot != 'n/a'}"><a href = "http://www.ncbi.nlm.nih.gov/nuccore/${r.uniprot}">${r.uniprot}</a></g:if><g:else>${r.uniprot}</g:else></td>
					<td>${r.fullname}</td>
					<td><a href="http://asia.ensembl.org/Homo_sapiens/Location/View?db=core;r=${r.location}" target="_blank">${r.location}</a></td>
					<td>${r.count.size()}</td>
					<g:each var="t" in="${r.countScore}">
						<g:if test="${t.key == 's'}"><td>${t.value}</td></g:if>
						<g:if test="${t.key == 't'}"><td>${t.value}</td></g:if>
						<g:if test="${t.key == 'd'}"><td>${t.value}</td></g:if>
						<g:if test="${t.key == 'm'}"><td>${t.value}</td></g:if>
					</g:each>
				</tr>
				</g:each>
			</tbody>
		</table>
</body>

</html>