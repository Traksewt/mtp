<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Home</title>
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
		<style>
		div.bar{ 
			display: inline-block;
			width: 20px;
			height: 75px;
			background-color: blue;
			margin-right: 20px;
		}
		</style>
		

	</head>
	<body>
	<h1>Header</h1>
	<svg class="chart"></svg>
	
		
	<script>
	var chrNum = ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","X","Y"];
	var dataset = [249250621,243199373,198022430,191154276,180915260,171115067,159138663,146364022,141213431,135534747,135006516,133851895,115169878,107349540,102531392,90354753,81195210,78077248,59128983,63025520,48129895,51304566,155270560,59373566]

	//Width and height
	var w = 1000;
	var h = 300;
	var barPadding = 1;
	var moveRight = 50;

	
	//var dataset = [ 5, 10, 13, 19, 21, 25, 22, 18, 15, 13, 11, 12, 15, 20, 18, 17, 16, 18, 23, 25 ];
	// from here http://alignedleft.com/tutorials/d3/making-a-bar-chart and here http://bost.ocks.org/mike/bar/2/
	var xScale = d3.scale.linear()
		.domain([0, d3.max(dataset)])
		.range([0, w]);
	var yScale = d3.scale.linear()
			 .domain([0, d3.max(dataset)])
			 .range([0, h]);
			 
	//Create SVG element
	var svg = d3.select("body")
				.append("svg")
				.attr("width", w)
				.attr("height", h);

	svg.selectAll("rect")
	   .data(dataset)
	   .enter()
	   .append("rect")
	   //sets the distance from the left most x coordinate
	   .attr("x", function(d, i) {
			return i * (w / dataset.length) + moveRight;
	   })
	   //sets the distance from the uppermost height coordinate
	   .attr("y", function(d) {
			return h - (yScale(d) + 10);
	   })
	   //sets the width of the bar
	   .attr("width", w / dataset.length - barPadding)
	   //sets the height of the bar
	   .attr("height", yScale)
	   .attr("fill", function(d,i) {
			return "rgb(0, 0, " + i * 20 + ")";
	   });

	svg.selectAll("text")
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
			return h ;
	   })
	   .attr("font-family", "sans-serif")
	   .attr("font-size", "11px")
	   .attr("fill", "black");


/*			
var data = [4, 8, 15, 16, 23, 42];

 var width = 420,
     barHeight = 20;
 
 var x = d3.scale.linear()
     .domain([0, d3.max(data)])
     .range([0, width]);
 
 //set the size of the canvas
 var chart = d3.select(".chart")
     .attr("width", width)
     .attr("height", barHeight * data.length);
 
 //read the data and set the distance between bars
 var bar = chart.selectAll("g")
     .data(data)
     .enter().append("g")
     .attr("transform", function(d, i) { return "translate(0," + i * barHeight + ")"; });
 
 //set the length and width of the bar
 bar.append("rect")
     .attr("width", x)
     .attr("height", barHeight - 1);
 
 //set the text position
 bar.append("text")
     .attr("x", function(d) { return 30; })
     .attr("y", barHeight / 2)
     .attr("dy", ".35em")
     .text(function(d,i) { return "Chr "+chrNum[i]; });
*/
</script>

	</body>
</html>
