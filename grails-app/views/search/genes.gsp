<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Genes</title>
	
		
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'TableTools-2.1.5/media/js/TableTools.js')}" type="text/javascript"></script>
        <script src="${resource(dir: 'js', file: 'TableTools-2.1.5/media/js/ZeroClipboard.js')}" type="text/javascript"></script>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_table.css')}" type="text/css"></link>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_page.css')}" type="text/css"></link>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'TableTools-2.1.5/media/css/TableTools.css')}" type="text/css"></link>
		<script src="${resource(dir: 'js', file: 'd3/d3.v3.min.js')}" type="text/javascript"></script>
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
			}
		</style>
		<script>
  			$(document).ready(function() {
  			
    			$('#search').dataTable({
    				"sPaginationType": "full_numbers",
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
	<h1>Gene target data for ${mirData.matid}</h1>
		<table id="search">
            <thead>
				<tr><th>Gene</th><th>Location</th><th><a href="#" title="StarBase">S</a></th><th><a href="#" title="MiRTarBase">M</a></th><th><a href="#" title="TargetScan">T</a></th><th><a href="#" title="DIANA MicroT-CDS">D</a></th></tr>
			</thead>
			<tbody>
				<g:each var="r" in="${tData}">
				<tr>
					<td><a href = "http://www.genecards.org/cgi-bin/carddisp.pl?gene=${r.name}" target="_blank">${r.name}</a></td>
					<td><a href="http://asia.ensembl.org/Homo_sapiens/Location/View?db=core;r=<%="${r.chr}".replaceAll("chr","")%>:${r.start}-${r.stop}" target="_blank">${r.chr}: ${r.start}-${r.stop}</a></td>
					<td>${r.s}</td>
					<td>${r.m}</td>
					<td>${r.t}</td>
					<td>${r.d}</td>
				</tr>
				</g:each>
			</tbody>
		</table>
		<h2>Gene Targets per chromosome</h2>
		<svg id="gene_chart"></svg>
	
		<h3>Download gene interactions for ${mirData.matid}:</h3><br>
		<table class="compact_left"><tr><td>StarBase</td>
		<td>
		<g:form name="starDownload" url="[controller:'Download', action:'gene_download']" style="display: inline" >
        	<g:hiddenField name="fileData" value="${sGenes}"/>
            <g:hiddenField name="fileName" value="${mirData.matid}.StarBase.txt"/>
            <a href="#" onclick="document.starDownload.submit()">${sCount.size()}</a>
    	</g:form>
    	</td></tr>
    	<tr><td>MiRTarBase</td>
    	<td>
    	<g:form name="mirDownload" url="[controller:'Download', action:'gene_download']" style="display: inline" >
        	<g:hiddenField name="fileData" value="${mGenes}"/>
            <g:hiddenField name="fileName" value="${params.matid}.MirTarBase.txt"/>
            <a href="#" onclick="document.mirDownload.submit()">${mCount.size()}</a>
    	</g:form>
    	</td>
    	<tr><td>TargetScan</td>
    	<td>
    	<g:form name="tsDownload" url="[controller:'Download', action:'gene_download']" style="display: inline" >
        	<g:hiddenField name="fileData" value="${tGenes}"/>
            <g:hiddenField name="fileName" value="${params.matid}.TargetScan.txt"/>
            <a href="#" onclick="document.tsDownload.submit()">${tCount.size()}</a>
    	</g:form>
    	</td></tr>
    	<tr><td>DIANA-microT-CDS</td>
    	<td>
    	<g:form name="diDownload" url="[controller:'Download', action:'gene_download']" style="display: inline" >
        	<g:hiddenField name="fileData" value="${dGenes}"/>
            <g:hiddenField name="fileName" value="${params.matid}.DIANA-microT-CDS.txt"/>
            <a href="#" onclick="document.diDownload.submit()">${dCount.size()}</a>
    	</g:form>
    	</td></tr>
    	<tr><td><b>Union of all</b></td>
    	<td>
    	<g:form name="unionDownload" url="[controller:'Download', action:'gene_download']" style="display: inline" >
        	<g:hiddenField name="fileData" value="${interGenes}"/>
            <g:hiddenField name="fileName" value="${params.matid}.union.txt"/>
            <a href="#" onclick="document.unionDownload.submit()">${unionGeneMap.size()}</a>
    	</g:form>
    	</td></tr>
    	</table>
    	
    	<script>
	//data
	var chrNum = ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","X","Y"];
	var chrSize = [249250621,243199373,198022430,191154276,180915260,171115067,159138663,146364022,141213431,135534747,135006516,133851895,115169878,107349540,102531392,90354753,81195210,78077248,59128983,63025520,48129895,51304566,155270560,59373566];
	var allCounts = <%=allCounts%>;
	var tCounts = <%=tList%>;
	var mCounts = <%=mList%>;
	var dCounts = <%=dList%>;
	var sCounts = <%=sList%>;
	var tdata = <%=tDecode%>;
	var sdata = <%=sDecode%>;
	var mdata = <%=mDecode%>;
	var ddata = <%=dDecode%>;
	
	//Width and height
	var w = $(document).width() * 0.80;
	var h = 300;
	var barPadding = 1;
	var moveRight = 70;
	var moveUp = 20;
	
	// from here http://alignedleft.com/tutorials/d3/making-a-bar-chart and here http://bost.ocks.org/mike/bar/2/
	var xScale = d3.scale.linear()
		.domain([0, d3.max(chrSize)])
		.range([0, w]);
	var yScale = d3.scale.linear()
		.domain([0, d3.max(chrSize)])
		.range([0, h]);
	var x2Scale = d3.scale.linear()
		.domain([0, d3.max(allCounts)])
		.range([0, w]);
	var y2Scale = d3.scale.linear()
		.domain([0, d3.max(allCounts)])
		.range([0, h]);
	
	//make scales for axis with reversed ranges
	var yAxisScale1 = d3.scale.linear()
		.domain([0, d3.max(chrSize)/1000000])
		.range([h+moveUp, moveUp]);		
	var yAxisScale2 = d3.scale.linear()
		.domain([0, d3.max(allCounts)])
		.range([h+moveUp, moveUp]);
			 
	//Create SVG element
	var chart = d3.select("#gene_chart")
		.attr("width", w+moveRight+50)
		.attr("height", h+50);
			
	//create the bars
	var rects = chart.selectAll("rect")
	   .data(chrSize)
	   .enter()
	   .append("rect")
	   //sets the distance from the left most x coordinate
	   .attr("x", function(d, i) {
			return i * (w / chrSize.length) + moveRight;
	   })
	   //sets the distance from the uppermost height coordinate
	   .attr("y", function(d) {
			return h - (yScale(d) - moveUp);
	   })
	   //sets the width of the bar
	   .attr("width", w / chrSize.length - barPadding)
	   //sets the height of the bar
	   .attr("height", yScale)
	   //colours the bars
	   .attr("fill", function(d,i) {
			return "rgb(0, 0, " + i * 20 + ")";
	   });   
    
	//add some text
	chart.selectAll("text.chr")
	   .data(chrNum)
	   .enter()
	   .append("text")
	   .text(function(d) {
			return d;
	   })
	   .attr("text-anchor", "middle")
	   .attr("x", function(d, i) {
			return i * (w / chrSize.length) + (w / chrSize.length - barPadding) / 2 + moveRight;
	   })
	   .attr("y", function(d, i) {
			return h + 40;
	   })
	   .attr("font-family", "sans-serif")
	   .attr("font-size", "15px")
	   .attr("fill", "black");

    chart.append("text")
    .attr("text-anchor", "end")
    .attr("y", 20)
    .attr("x", -moveRight)
    .attr("dy", ".75em")
    .attr("transform", "rotate(-90)")
    .text("Chromosome size (Mb)");  			
	
	chart.append("text")
    .attr("text-anchor", "end")
    .attr("y", w+moveRight+35)
    .attr("x", -moveRight+40)
    .attr("dy", ".75em")
    .attr("transform", "rotate(-90)")
    .text("Gene count / genes per chromosome per Mb");
	
	//add the gene locations lines 	
	var dataLists = [tdata,mdata,sdata,ddata]
	var colours = ['green','grey','orange','purple']
	for (var l=0;l<dataLists.length;l++){
		for (var i=0;i<dataLists[l].length;i++){
			//console.log(dataLists[l][i].chr)
			var m = dataLists[l][i].chr
			var c = m.match(/chr(.*)/)
			var chr = c[1]
			if (chr.match(/X/)){ chr = "23"}
			if (chr.match(/Y/)){ chr = "24"}
			chart.append("svg:line")
				.attr("x1", (parseInt(chr)-1) * (w / chrSize.length) + moveRight)			
				.attr("y1", h - (yScale(dataLists[l][i].start) - moveUp))
				.attr("x2", parseInt(chr) * (w / chrSize.length) + moveRight - barPadding)
				.attr("y2", h - (yScale(dataLists[l][i].start) - moveUp))
				.style("stroke", colours[l]);
		} 
	}
	//add the legend
	var legend = ['TargetScan','MiRTarBase','StarBase','DIANA']
	for (var l=0;l<legend.length;l++){
		chart.append("svg:line")
			.attr("x1", w  - (moveRight +200))
			.attr("y1", h - 250 +(l*25))
			.attr("x2", w - (moveRight + 100))
			.attr("y2", h - 250 +(l*25))
			.style({ 'stroke': colours[l], 'fill': 'none', 'stroke-width': '4px'});
		chart.append("text")
    		.attr("text-anchor", "end")
			.attr("y", h - 258 +(l*25))
			.attr("x", w  - (moveRight +205))
			.attr("dy", ".75em")
			.text(legend[l]); 
	}
	//and the miRNA
	chart.append("svg:line")
			.attr("x1", w  - (moveRight +200))
			.attr("y1", h - 275)
			.attr("x2", w - (moveRight + 100))
			.attr("y2", h - 275)
			.style({ 'stroke': 'red', 'fill': 'none', 'stroke-width': '10px'});
		chart.append("text")
    		.attr("text-anchor", "end")
			.attr("y", h - 280)
			.attr("x", w  - (moveRight +205))
			.attr("dy", ".75em")
			.text("${mirData.matid}"); 
	
	//add lines - https://www.dashingd3js.com/svg-paths-and-d3js
	var line = d3.svg.line()
    	.x(function(d,i) { return i * (w / chrSize.length) + (w / chrSize.length - barPadding) / 2 + moveRight })
    	.y(function(d) { return h - (y2Scale(d) - moveUp)});
    
    var lineLists = [tCounts,mCounts,sCounts,dCounts]
    for (var l=0;l<lineLists.length;l++){
    	var addpath = chart.append('path')
			.attr('d', line(lineLists[l]))
			.attr("stroke", colours[l])
        	.attr("stroke-width", 4)
    		.attr("fill", "none");
    }
    	
	//add the mir
	var miRLoc = <%=miR%>
	var mL = miRLoc[0].chr	
	var cL = mL.match(/chr(.*)/)
	var chrL = cL[1]
	if (chrL.match(/X/)){ chrL = "23"}
	if (chrL.match(/Y/)){ chrL = "24"}	
	chart.append("svg:line")
		.attr("x1", (parseInt(chrL)-1) * (w / chrSize.length) + moveRight)			
    	.attr("y1", h - (yScale(miRLoc[0].start) - moveUp))
    	.attr("x2", parseInt(chrL) * (w / chrSize.length) + moveRight - barPadding)
    	.attr("y2", h - (yScale(miRLoc[0].start) - moveUp))
    	.style({ 'stroke': 'red', 'fill': 'none', 'stroke-width': '10px'});
    
    //add the axes	
    var yAxis1 = d3.svg.axis()
        .scale(yAxisScale1)
        .orient("left");
        
    chart.append("g")
    	.attr("class", "axis")
    	.attr("transform", "translate(" + (moveRight-5) + ",0)")
    	.call(yAxis1);    
    	
    var yAxis2 = d3.svg.axis()
        .scale(yAxisScale2)
        .orient("right");
        
    chart.append("g")
    	.attr("class", "axis")
    	.attr("transform", "translate(" + (w + moveRight) + ",0)")
    	.call(yAxis2); 
		</script>
	</body>
</html>
