<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Home</title>
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
		<style>

		.bar {
		  fill: steelblue;
		}

		.bar:hover {
		  fill: brown;
		}

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
		

	</head>
	<body>
	<svg id="mir_chart"></svg>
	
		
	<script>
	//data
	var chrNum = ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","X","Y"];
	var dataset = [249250621,243199373,198022430,191154276,180915260,171115067,159138663,146364022,141213431,135534747,135006516,133851895,115169878,107349540,102531392,90354753,81195210,78077248,59128983,63025520,48129895,51304566,155270560,59373566]
	var miRCounts = <%=miRList%>
	//var miRdata = [{chr:"chr1",loc:23370803},{chr:"chr1",loc:35135215},{chr:"chr1",loc:110141562},{chr:"chr1",loc:201777743},{chr:"chr1",loc:220373891},{chr:"chr10",loc:131641613},{chr:"chr11",loc:46473408},{chr:"chr11",loc:65211944},{chr:"chr11",loc:71783318},{chr:"chr12",loc:50627965},{chr:"chr12",loc:54731062},{chr:"chr14",loc:100576052},{chr:"chr14",loc:101350881},{chr:"chr14",loc:101492119},{chr:"chr14",loc:101506606},{chr:"chr14",loc:101530885},{chr:"chr16",loc:14397874},{chr:"chr16",loc:15248806},{chr:"chr17",loc:7991384},{chr:"chr17",loc:11985283},{chr:"chr17",loc:29887069},{chr:"chr17",loc:64783250},{chr:"chr19",loc:10829095},{chr:"chr19",loc:13947261},{chr:"chr19",loc:18497417},{chr:"chr19",loc:46142267},{chr:"chr20",loc:18451268},{chr:"chr20",loc:33054190},{chr:"chr22",loc:41488549},{chr:"chr4",loc:153410488},{chr:"chr5",loc:131701205},{chr:"chr5",loc:132763307},{chr:"chr5",loc:153726713},{chr:"chr7",loc:32772653},{chr:"chr7",loc:63081478},{chr:"chr7",loc:91833341},{chr:"chr7",loc:93113259},{chr:"chr7",loc:151130612},{chr:"chr9",loc:28863634},{chr:"chrX",loc:133303713},{chr:"chrX",loc:145078726}]
	var miRdata = <%=mirLoc%>
		
	
	//Width and height
	var w = $(document).width() * 0.80;
	//alert($(document).width()*0.8)
	var h = 300;
	var barPadding = 1;
	var moveRight = 70;
	var moveUp = 20;
	var testD = [1000000, 110000000, 120000000]
	
	//var dataset = [ 5, 10, 13, 19, 21, 25, 22, 18, 15, 13, 11, 12, 15, 20, 18, 17, 16, 18, 23, 25 ];
	// from here http://alignedleft.com/tutorials/d3/making-a-bar-chart and here http://bost.ocks.org/mike/bar/2/
	var xScale = d3.scale.linear()
		.domain([0, d3.max(dataset)])
		.range([0, w]);
	var yScale = d3.scale.linear()
		.domain([0, d3.max(dataset)])
		.range([0, h]);
	var x2Scale = d3.scale.linear()
		.domain([0, d3.max(miRCounts)])
		.range([0, w]);
	var y2Scale = d3.scale.linear()
		.domain([0, d3.max(miRCounts)])
		.range([0, h]);
	
	//make scales for axis with reversed ranges
	var yAxisScale1 = d3.scale.linear()
		.domain([0, d3.max(dataset)/1000000])
		.range([h+moveUp, moveUp]);		
	var yAxisScale2 = d3.scale.linear()
		.domain([0, d3.max(miRCounts)])
		.range([h+moveUp, moveUp]);
			 
	//Create SVG element
	var chart = d3.select("#mir_chart")
		.attr("width", w+moveRight+50)
		.attr("height", h+50);
			
	//create the bars
	var rects = chart.selectAll("rect")
	   .data(dataset)
	   .enter()
	   .append("rect")
	   //sets the distance from the left most x coordinate
	   .attr("x", function(d, i) {
			return i * (w / dataset.length) + moveRight;
	   })
	   //sets the distance from the uppermost height coordinate
	   .attr("y", function(d) {
			return h - (yScale(d) - moveUp);
	   })
	   //sets the width of the bar
	   .attr("width", w / dataset.length - barPadding)
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
			return i * (w / dataset.length) + (w / dataset.length - barPadding) / 2 + moveRight;
	   })
	   .attr("y", function(d, i) {
			return h + 40;
	   })
	   .attr("font-family", "sans-serif")
	   .attr("font-size", "15px")
	   .attr("fill", "black");

	//add the miR counts text
	/*
	svg.selectAll("text.counts")
	   .data(miRCounts)
	   .enter()
	   .append("text")
	   .text(function(d) {
			return d;
	   })
	   .attr("text-anchor", "middle")
	   .attr("x", function(d, i) {
			return i * (w / dataset.length) + (w / dataset.length - barPadding) / 2 + moveRight;
	   })
	   .attr("y", function(d, i) {
			return 10 ;
	   })
	   .attr("font-family", "sans-serif")
	   .attr("font-size", "15px")
	   .attr("fill", "black");
	*/ 
    	
    chart.append("text")
    .attr("text-anchor", "end")
    .attr("y", 10)
    .attr("x", -moveRight)
    .attr("dy", ".75em")
    .attr("transform", "rotate(-90)")
    .text("Chromosome size (Mb)");  			
	
	chart.append("text")
    .attr("text-anchor", "end")
    .attr("y", w+moveRight+30)
    .attr("x", -moveRight-20)
    .attr("dy", ".75em")
    .attr("transform", "rotate(-90)")
    .text("Number of miRs");
	
	//add the miR lines 
	for (var i=0;i<miRdata.length;i++){
		var m = miRdata[i].chr
		var c = m.match(/chr(.*)/)
		var chr = c[1]
		if (chr.match(/X/)){ chr = "23"}
		if (chr.match(/Y/)){ chr = "24"}
		alert("chromosome: "+chr+ "start: "+miRdata[i].start)
		chart.append("svg:line")
			.attr("x1", (parseInt(chr)-1) * (w / dataset.length) + moveRight)			
    		.attr("y1", h - (yScale(miRdata[i].start) - moveUp))
    		.attr("x2", parseInt(chr) * (w / dataset.length) + moveRight - barPadding)
    		.attr("y2", h - (yScale(miRdata[i].start) - moveUp))
    		.style("stroke", "rgb(6,120,155)");
	} 
	//add line - https://www.dashingd3js.com/svg-paths-and-d3js
	var array = [10,20,30,40,50,60]
	var line = d3.svg.line()
    	.x(function(d,i) { return i * (w / dataset.length) + (w / dataset.length - barPadding) / 2 + moveRight })
    	.y(function(d) { return h - (y2Scale(d) - moveUp)});
    
    var addpath = chart.append('path')
		.attr('d', line(miRCounts))
		.attr("stroke", "red")
        .attr("stroke-width", 2)
    	.attr("fill", "none");
    	
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
