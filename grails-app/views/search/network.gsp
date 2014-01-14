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
					"aaSorting": [[ 4, "desc" ]],
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
			opacity: 0.1;
			stroke-width: 1.5px;
		}
		path.link.s2 { 
			opacity: 0.3;
			stroke-width: 2.0px;
		}
		path.link.s3 { 
			opacity: 0.5;
			stroke-width: 2.5px;
		}
		path.link.s4 { 
			opacity: 0.7;
			stroke-width: 3.0px;
		}
		path.link.s5 { 
			opacity: 0.9;
			stroke-width: 3.5px;
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
				<tr><th>Gene symbol</th><th>Name</th><th width="30%">Location</th><th>Count</th><th>Combined score</th></tr>
			</thead>
			<tbody>
				<g:each var="r" in="${session.commonGenes}">
				<tr>
					<td><a href = "http://www.genecards.org/cgi-bin/carddisp.pl?gene=${r.name}" target="_blank">${r.name}</a></td>
					<td>${r.fullname}</td>
					<td><a href="http://asia.ensembl.org/Homo_sapiens/Location/View?db=core;r=${r.location}" target="_blank">${r.location}</a></td>
					<td>${r.count}</td>
					<td>${r.countScore}</td>
				</tr>
				</g:each>
			</tbody>
		</table>
	</body>
</html>
