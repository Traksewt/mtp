<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Search Results</title>
	
		
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'Highcharts-3.0.7/js/highcharts.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'Highcharts-3.0.7/js/modules/exporting.js')}" type="text/javascript"></script>
		
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_table.css')}" type="text/css"></link>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_page.css')}" type="text/css"></link>

  		<script>
  			$(document).ready(function() {
  			
    			$('#search').dataTable({
    				"sPaginationType": "full_numbers",
    			});
			
			});
			
		   $(function () { 
				$('#family').highcharts({
					chart: {
						type: 'column'
					},
					title: {
						text: 'Frequency of miRNAs per Family'
					},
					xAxis: {
						categories: <%=famList%>,
						labels: {
               	 			rotation: -90
            			}
					},
					yAxis: {
						title: {
							text: 'miRNA Family'
						},
					},
					series: [{
						name: '',
						data: <%=famCount%>,
						color: '#FF0000'
					}],
					legend: {
            			enabled: false
        			}
				});
				
				$('#position').highcharts({
					chart: {
						type: 'column'
					},
					title: {
						text: 'Frequency of miRNAs per chromosome'
					},
					xAxis: {
						categories: <%=posList%>,
						labels: {
               	 			rotation: -90
            			}
					},
					yAxis: {
						title: {
							text: 'Chromosome'
						}
					},
					series: [{
						name: '',
						data: <%=posCount%>
					}],
					legend: {
            			enabled: false
        			},
				});
			});
  		</script>
	</head>
	<body>
		<h1>Results</h1>
		<table id="search">
            <thead>
				<tr><% if(rank.size()>0){%><th>Score</th><%}%><th>Mature</th><th>Family</th><th>Precursor</th><th>Sequence</th><th>Location</th><th>S</th><th>M</th><th>T</th><th>D</th><th>Flag</th><th>Data</th></tr>
			</thead>
			<tbody>
				<g:each var="r" in="${mirRes}">
				<tr>
					<% if(rank.size()>0){%><td><%=rank."${r.matid}" %></td><%}%>
					<td><a href="http://mirbase.org/cgi-bin/mirna_entry.pl?acc=${r.matacc}" target="_blank">${r.matid}</a></td>
					<td><a href="http://mirbase.org/cgi-bin/mirna_summary.pl?fam=${r.famacc}" target="_blank">${r.famid}</a></td>
					<td><a href="http://www.mirbase.org/cgi-bin/mirna_entry.pl?acc=${r.preacc}" target="_blank">${r.preid}</a></td>
					<td>${r.matseq}</td><td><a href="http://asia.ensembl.org/Homo_sapiens/Location/View?db=core;r=${r.chr}:${r.start}-${r.stop}" target="_blank">${r.chr}: ${r.start}-${r.stop}</a></td>
					<td><a href="http://starbase.sysu.edu.cn/viewMatureMirInfo.php?table=miRNAClipTargets&database=hg19&name=${r.matid}" target="_blank"><%=starMap."${r.matid}" %></a></td>
					<td><a href="http://mirtarbase.mbc.nctu.edu.tw/index.php" target="_blank"><%=mtMap."${r.matid}" %></a></td>
					<td><a href="http://www.targetscan.org/cgi-bin/targetscan/vert_61/targetscan.cgi?species=Human&gid=&mir_sc=&mir_c=&mir_nc=&mirg=${r.matid}" target="_blank"><%=tsMap."${r.matid}" %></a></td>
					<td><a href="http://diana.imis.athena-innovation.gr/DianaTools/index.php?r=microT_CDS/results&keywords=${r.matid}&genes=&mirnas=${r.matid}%20&descr=&threshold=0.7" target="_blank"><%=diMap."${r.matid}" %></a></td>
					<td><% if (flagMap."${r.matid}"){print flagMap."${r.matid}"}else{print "None"} %></td>
					<td><g:link action="genes" params="[matid:r.matid, sEv:sEv, mEv:mEv]">Link</g:link></td>
				</tr>
				</g:each>
			</tbody>
		</table>
		<div id="family" style="width:100%; height:400px;"></div>
		<div id="position" style="width:100%; height:400px;"></div>
	</body>
</html>
