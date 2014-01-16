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
		<style>
			.axis {
			  font: 10px sans-serif;
			}

			.axis path,
			.axis line {
			  fill: none;
			  stroke: #000;
			  shape-rendering: crispEdges;
			}

			.x.axis path {
			  display: none;
			  
			  #myCanvas
			{
    			display: block;
    			margin: 0 auto;
			}
			}
		</style>
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
				
		//console.log(<%=mList%>)
        //console.log(<%=gList%>)
        //console.log(<%=fData%>)
        
        if (<%=mList.size()>2%>){
        
        	//set the width based on document size
			//$("#canvas1").width($( document ).width() * 0.8);
			
			var cx2 = new CanvasXpress('canvas1',
			  {
				'z': {
					'Family': <%=famHeatList%>
				  //'Description': ['uncharacterized LOC645722', 'DSCAM antisense RNA 1', 'heat shock 70kDa protein 1A', 'plastin 3', 'epithelial membrane protein 1', 'calponin 3, acidic', 'serglycin', 'transforming growth factor, beta receptor II (70/80kDa)', 'anterior gradient 2 homolog (Xenopus laevis)', 'tumor protein D52-like 1', 'collagen, type XIII, alpha 1', 'plasminogen activator, urokinase', 'olfactomedin 1', 'BTG family, member 3', 'tissue factor pathway inhibitor (lipoprotein-associated coagulation inhibitor)', 'v-ets erythroblastosis virus E26 oncogene homolog 1 (avian)', 'branched chain amino-acid transaminase 1, cytosolic', 'v-erb-b2 erythroblastic leukemia viral oncogene homolog 3 (avian)', 'thiosulfate sulfurtransferase (rhodanese)-like domain containing 1']
				},
				'x': {
				  //'Type': ['control', 'control', 'control', 'estrogen receptor knockdown', 'estrogen receptor knockdown', 'estrogen receptor knockdown']
				},
				'y': {
				  'vars': <%=mList%>,
				  'smps': <%=gList%>,
				  'data': <%=fData%>,        
				  'desc': ['Score']
				},
	   
			  },
			  {'graphType': 'Heatmap',
			  'heatmapType':'red',
			  'indicatorCenter':'rainbow-green',
			  'smpOverlays': ['Type'],
			  'title': 'StarBase gene target predictions for each miRNA',
			  'varLabelRotate': 45,
			  'varOverlays': ['Family']}
			);
			cx2.clusterSamples();
			cx2.clusterVariables();
		}
			
			});
			
			//enrichr data
			var eList = <%=gList%>;
			var eList_split = ""
			for (var i=0;i<eList.length;i++){
				eList_split += eList[i]+"\r"
			} 

			function enrich(options) {
				var defaultOptions = {
					description: "",
					popup: false
				};

				if (typeof options.description == 'undefined')
					options.description = defaultOptions.description;
				if (typeof options.popup == 'undefined')
					options.popup = defaultOptions.popup;
				if (typeof options.list == 'undefined')
					alert('No genes defined.');

				var form = document.createElement('form');
				form.setAttribute('method', 'post');
				form.setAttribute('action', 'http://amp.pharm.mssm.edu/Enrichr/enrich');
				if (options.popup)
					form.setAttribute('target', '_blank');
				form.setAttribute('enctype', 'multipart/form-data');

				var listField = document.createElement('input');
				listField.setAttribute('type', 'hidden');
				listField.setAttribute('name', 'list');
				listField.setAttribute('value', options.list);
				form.appendChild(listField);

				var descField = document.createElement('input');
				descField.setAttribute('type', 'hidden');
				descField.setAttribute('name', 'description');
				descField.setAttribute('value', options.description);
				form.appendChild(descField);

				document.body.appendChild(form);
				form.submit();
				document.body.removeChild(form);
			}		
  		</script>
	</head>
	<body>
		<h1>Target Search results</h1>

		<h3>Gene counts</h3>
		The table below lists the genes that are most commonly found in the selected target predictions.
		<table id="common">
            <thead>
				<tr><th>Gene symbol</th><th>Ensembl ID</th><th>UniProt ID</th><th>Name</th><th width="30%">Location</th><th>Count</th><th>S</th><th>M</th><th>T</th><th>D</th></tr>
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
						<g:if test="${t.key == 'm'}"><td>${t.value}</td></g:if>
						<g:if test="${t.key == 't'}"><td>${t.value}</td></g:if>
						<g:if test="${t.key == 'd'}"><td>${t.value}</td></g:if>
					</g:each>
				</tr>
				</g:each>
			</tbody>
		</table>
		<br><br>
		
		<g:if test="${mList.size()>2}">
			<h3>Enrichr</h3>
			Click the symbol <a onclick="enrich({list: eList_split, popup: true})" href="javascript:void(0); "><img src="${resource(dir: 'images', file: 'enrichr-icon.png')}"></a> to see Enrichr gene set enrichment analysis for the most common genes. 
			<br><br>
		</g:if>
		
		<g:if test="${mList.size()>2}">
			<h3>Heatmap of gene counts</h3>
			The 50 most common StarBase gene targets are represented below and clustered according to StarBase score. This score represents the number of computational predictions (1-5) that agree with the HITS-CLIP prediction. 
			<div style="position:relative; margin-left:auto; margin-right:auto; width:1000px; height:650px;"=>
				<canvas id='canvas1' width='1000' height='650'></canvas>
			</div>
		</g:if>
	
</body>

</html>