<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Network</title>	
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'TableTools-2.1.5/media/js/TableTools.js')}" type="text/javascript"></script>
        <script src="${resource(dir: 'js', file: 'TableTools-2.1.5/media/js/ZeroClipboard.js')}" type="text/javascript"></script>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_table.css')}" type="text/css"></link>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_page.css')}" type="text/css"></link>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'TableTools-2.1.5/media/css/TableTools.css')}" type="text/css"></link>
		<script src="${resource(dir: 'js', file: 'd3/d3.v3.min.js')}" type="text/javascript"></script>  	
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
                        "sTitle": "My title"
                	}
				});
			});	
		window.data = <%=ndata%>
		
		</script>
        <script src="${resource(dir: 'js', file: 'network.js')}" type="text/javascript"></script>
		<style>
		path.link {
			fill: none;
			/**stroke: #666;**/ 
			stroke: #0000ff;
			stroke-width: 1.5px;
		}
		path.link.s1 { 
			opacity: 0.25;
			stroke-width: 1.0px;
		}
		path.link.s2 { 
			opacity: 0.5;
			stroke-width: 2.0px;
		}
		path.link.s3 { 
			opacity: 0.75;
			stroke-width: 3.0px;
		}
		path.link.s4 { 
			opacity: 1.0;
			stroke-width: 4.0px;
		}

		path.link.TFmir{
			stroke: #ff0000; 
			opacity: 0.5;
			stroke-width: 1.0px;
		}
		path.link.tf-gene{
			stroke: #636363;
		}
		circle {
			fill: #ccc;
			stroke: #fff; stroke-width: 1.5px;
		}
		text {
			fill: #000;
			font: 10px sans-serif; pointer-events: none;
		}

		</style>
	</head>
	<body>
	<h1>Network of miRNAs, gene targets and transcription factors</h1>
	<p>- Each miRNA is represented by a blue circle, and blue lines connect them to target genes (green circles). 
	<p>- The colour and thickness of the blue lines represents the strength of the StarBase prediction (thicker darker line = more computational evidence)
	<p>- Red circles are transcription factors and red lines show cases where they target miRNAs.
	<p>- Each circle is sticky and can be moved and placed anywhere. To unstick just double click on the node. 
	- 
	<svg id="network_svg"></svg>
	<table id="common">
            <thead>
					<tr><th>Gene symbol</th><th>Ensembl ID</th><th>UniProt ID</th><th>Name</th><th width="20%">Location</th><th><a href="#" title="(Number of miRNAs with target as prediction / total number of miRNAs in search) * (number of target databases predicting this gene / total number of target databases in search)">Score</th><th># MiRNAs</th><!--th># DBs</th--><th>S</th><th>T</th><th>D</th><th>M</th></tr>
			</thead>
			<tbody>
				<g:each var="r" in="${commonGeneList}">
				<tr>
					<td><a href = "http://www.genecards.org/cgi-bin/carddisp.pl?gene=${r.name}" target="_blank">${r.name}</a></td>
					<td><g:if test="${r.ensembl != 'n/a'}"><a href = "http://www.ensembl.org/id/${r.ensembl}">${r.ensembl}</a></g:if><g:else>${r.ensembl}</g:else></td>
					<td><g:if test="${r.uniprot != 'n/a'}"><a href = "http://www.ncbi.nlm.nih.gov/nuccore/${r.uniprot}">${r.uniprot}</a></g:if><g:else>${r.uniprot}</g:else></td>
					<td>${r.fullname}</td>
					<td><a href="http://asia.ensembl.org/Homo_sapiens/Location/View?db=core;r=${r.location}" target="_blank">${r.location}</a></td>
					<td>${sprintf("%.2f",r.nScore)}</td>
					<td>${r.count.unique().size()}</td>
					<!--td>${r.countScore.collect{it.value}.sum()}</td-->
					<td>${r.countScore.collect{it.value}[0]}</td>
					<td>${r.countScore.collect{it.value}[1]}</td>
					<td>${r.countScore.collect{it.value}[2]}</td>
					<td>${r.countScore.collect{it.value}[3]}</td>
				</tr>
				</g:each>

			</tbody>
		</table>
	</body>
</html>
