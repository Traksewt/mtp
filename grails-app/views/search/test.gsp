<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Home</title>
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'CanvasXpress/canvasXpress.css')}" type="text/css"></link>
		<script src="${resource(dir: 'js', file: 'CanvasXpress/canvasXpress.min.js')}" type="text/javascript"></script>
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
		
		<script id='demoScript'>

      var showDemo = function () {

        var cx1 = new CanvasXpress('canvas1',
          {
            'z' : {
              'Annt1' : ['Desc:1', 'Desc:2', 'Desc:3', 'Desc:4'],
              'Annt2' : ['Desc:A', 'Desc:B', 'Desc:A', 'Desc:B'],
              'Annt3' : ['Desc:X', 'Desc:X', 'Desc:Y', 'Desc:Y']
            },
            'x' : {
              'Factor1' : ['Lev:1', 'Lev:2', 'Lev:3', 'Lev:1', 'Lev:2', 'Lev:3'],
              'Factor2' : ['Lev:A', 'Lev:B', 'Lev:A', 'Lev:B', 'Lev:A', 'Lev:B'],
              'Factor3' : ['Lev:X', 'Lev:X', 'Lev:Y', 'Lev:Y', 'Lev:Z', 'Lev:Z']
            },
            'y' : {
              'vars' : ['Variable1', 'Variable2', 'Variable3', 'Variable4'],
              'smps' : ['Sample1', 'Sample2', 'Sample3', 'Sample4', 'Sample5', 'Sample6'],
              'data' : [[5, 10, 25, 40, 45, 50], [95, 80, 75, 70, 55, 40], [25, 30, 45, 60, 65, 70], [55, 40, 35, 30, 15, 1]],
              'desc' : ['Magnitude1', 'Magnitude2']
            },
            'a' : {
              'xAxis' : ['Variable1', 'Variable2'],
              'xAxis2' : ['Variable3', 'Variable4']
            },
            't' : {
              'vars' : '(((Variable1,Variable3),Variable4),Variable2)',
              'smps' : '(((((Sample1,Sample2),Sample3),Sample4),Sample5),Sample6)'
            }
          },
          {'graphType': 'Heatmap',
          'highlightSmp': ['Sample1', 'Sample2'],
          'highlightVar': ['Variable2', 'Variable4'],
          'smpOverlays': ['Factor1', 'Factor2', 'Factor3']}
        );
        cx1.clusterSamples();
        cx1.clusterVariables();

        var cx2 = new CanvasXpress('canvas2',
          {
            'z': {
              'Symbol': ['LOC645722', 'DSCAM-AS1', 'HSPA1A', 'PLS3', 'EMP1', 'CNN3', 'SRGN', 'TGFBR2', 'AGR2', 'TPD52L1', 'COL13A1', 'PLAU', 'OLFM1', 'BTG3', 'TFPI', 'ETS1', 'BCAT1', 'RBB3', 'TSTD1'],
              'Description': ['uncharacterized LOC645722', 'DSCAM antisense RNA 1', 'heat shock 70kDa protein 1A', 'plastin 3', 'epithelial membrane protein 1', 'calponin 3, acidic', 'serglycin', 'transforming growth factor, beta receptor II (70/80kDa)', 'anterior gradient 2 homolog (Xenopus laevis)', 'tumor protein D52-like 1', 'collagen, type XIII, alpha 1', 'plasminogen activator, urokinase', 'olfactomedin 1', 'BTG family, member 3', 'tissue factor pathway inhibitor (lipoprotein-associated coagulation inhibitor)', 'v-ets erythroblastosis virus E26 oncogene homolog 1 (avian)', 'branched chain amino-acid transaminase 1, cytosolic', 'v-erb-b2 erythroblastic leukemia viral oncogene homolog 3 (avian)', 'thiosulfate sulfurtransferase (rhodanese)-like domain containing 1']
            },
            'x': {
              'Type': ['control', 'control', 'control', 'estrogen receptor knockdown', 'estrogen receptor knockdown', 'estrogen receptor knockdown']
            },
            'y': {
              'vars': ['1555216_a_at', '1562821_a_at', '200799_at', '201215_at', '201324_at', '201445_at', '201859_at', '208944_at', '209173_at', '210372_s_at', '211343_s_at', '211668_s_at', '213131_at', '213134_x_at', '213258_at', '224833_at', '225285_at', '226213_at', '226482_s_at'],
              'smps': ['GSM678802', 'GSM678803', 'GSM678804', 'GSM678805', 'GSM678806', 'GSM678807'],
              'data': [[13.4443, 13.4052, 13.4372, 4.55083, 4.83841, 4.5995],
                       [13.1104, 13.0618, 13.1576, 4.73436, 5.00194, 4.71429],
                       [4.274, 4.24537, 5.36694, 13.2043, 13.1975, 13.1963],
                       [4.34309, 4.57643, 4.97614, 12.4432, 12.4042, 12.4081],
                       [4.69344, 4.56319, 5.27746, 12.1551, 11.7577, 11.9331],
                       [4.29599, 4.39105, 6.18367, 12.8764, 12.953, 12.9437],
                       [4.74912, 4.67893, 5.06339, 11.9744, 11.8, 11.795],
                       [12.5737, 12.5086, 12.5188, 4.67011, 4.98324, 4.72287],
                       [11.6008, 11.7211, 11.648, 4.82716, 4.6292, 4.60583],
                       [4.50014, 4.66061, 5.06175, 11.7515, 11.8144, 11.8301],
                       [4.60574, 4.53201, 4.82074, 12.1995, 12.2452, 12.1187],
                       [11.2839, 11.3428, 11.2473, 4.67248, 4.70424, 4.85937],
                       [4.74606, 4.54343, 4.88302, 11.8511, 11.7496, 11.7863],
                       [4.73078, 4.66955, 4.81833, 11.4661, 11.1592, 11.2315],
                       [4.84254, 4.75658, 5.07002, 11.7535, 11.7758, 11.6974],
                       [4.55831, 4.49289, 4.67046, 12.35, 12.0875, 12.0381],
                       [11.5252, 11.4375, 11.4535, 4.65772, 4.60565, 4.75315],
                       [10.9772, 10.9839, 11.0658, 4.7015, 4.56169, 4.66754],
                       [4.71039, 4.7602, 4.89837, 11.2719, 10.9061, 10.8996]],
              'desc': ['RMA']
            },
            m: {
              'Description': 'Estrogen receptor silencing induces epithelial to mesenchymal transition in human breast cancer cells.',
              'Reference': 'PLoS One. 2011;6(6):e20610. doi: 10.1371/journal.pone.0020610. Epub 2011 Jun 21'
            }
          },
          {'citation': 'PLoS One. 2011;6(6):e20610. doi: 10.1371/journal.pone.0020610. Epub 2011 Jun 21',
          'citationFontStyle': 'italic',
          'graphType': 'Heatmap',
          'smpOverlays': ['Type'],
          'title': 'Estrogen receptor silencing induces epithelial\nto mesenchymal transition in human breast cancer cells.',
          'varLabelRotate': 45,
          'varOverlays': ['Symbol']}
        );
        cx2.clusterSamples();
        cx2.clusterVariables();

        var cx3 = new CanvasXpress('canvas3',
          {
            'z' : {
              'Annt1' : ['Desc:1', 'Desc:2', 'Desc:3', 'Desc:4'],
              'Annt2' : ['Desc:A', 'Desc:B', 'Desc:A', 'Desc:B'],
              'Annt3' : ['Desc:X', 'Desc:X', 'Desc:Y', 'Desc:Y']
            },
            'x' : {
              'Factor1' : ['Lev:1', 'Lev:2', 'Lev:3', 'Lev:1', 'Lev:2', 'Lev:3'],
              'Factor2' : ['Lev:A', 'Lev:B', 'Lev:A', 'Lev:B', 'Lev:A', 'Lev:B'],
              'Factor3' : ['Lev:X', 'Lev:X', 'Lev:Y', 'Lev:Y', 'Lev:Z', 'Lev:Z']
            },
            'y' : {
              'vars' : ['Variable1', 'Variable2', 'Variable3', 'Variable4'],
              'smps' : ['Sample1', 'Sample2', 'Sample3', 'Sample4', 'Sample5', 'Sample6'],
              'data' : [[5, 10, 25, 40, 45, 50], [95, 80, 75, 70, 55, 40], [25, 30, 45, 60, 65, 70], [55, 40, 35, 30, 15, 1]],
              'desc' : ['Magnitude1', 'Magnitude2']
            },
            'a' : {
              'xAxis' : ['Variable1', 'Variable2'],
              'xAxis2' : ['Variable3', 'Variable4']
            },
            't' : {
              'vars' : '(((Variable1,Variable3),Variable4),Variable2)',
              'smps' : '(((((Sample1,Sample2),Sample3),Sample4),Sample5),Sample6)'
            }
          },
          {'dendrogramHang': true,
          'graphType': 'Heatmap',
          'heatmapType': 'purple-red',
          'indicatorCenter': 'rainbow',
          'showDataValues': true,
          'varDendrogramPosition': 'bottom'}
        );
        cx3.clusterSamples();
        cx3.kmeansSamples();
        cx3.clusterVariables();
        cx3.kmeansVariables();

        var cx4 = new CanvasXpress('canvas4',
          {
            'y' : {
              'vars' : ['Variable1', 'Variable2', 'Variable3', 'Variable4', 'Variable5'],
              'smps' : ['Sample1', 'Sample2', 'Sample3', 'Sample4', 'Sample5', 'Sample6', 'Sample7', 'Sample8', 'Sample9', 'Sample10'],
              'data' : [[1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [10, 9, 8, 7, 6, 5, 4, 3, 2, 1], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [10, 9, 8, 7, 6, 5, 4, 3, 2, 1], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]],
              'data2' : [[1, 1, 2, 2, 3, 3, 4, 4, 5, 5], [6, 6, 7, 7, 8, 8, 9, 9, 10, 10], [10, 10, 9, 9, 8, 8, 7, 7, 6, 6], [5, 5, 4, 4, 3, 3, 2, 2, 1, 1], [3, 3, 4, 4, 5, 5, 6, 6, 7, 7]],
              'data3' : [['A', 'A', 'B', 'B', 'C', 'C', 'D', 'D', 'E', 'E'], ['E', 'E', 'D', 'D', 'C', 'C', 'B', 'B', 'A', 'A'], ['A', 'A', 'B', 'B', 'C', 'C', 'D', 'D', 'E', 'E'], ['E', 'E', 'D', 'D', 'C', 'C', 'B', 'B', 'A', 'A'], ['A', 'A', 'B', 'B', 'C', 'C', 'D', 'D', 'E', 'E']],
              'data4' : [['A', 'A', 'B', 'B', 'A', 'A', 'B', 'B', 'A', 'A'], ['B', 'B', 'A', 'A', 'B', 'B', 'A', 'A', 'B', 'B'], ['A', 'A', 'B', 'B', 'A', 'A', 'B', 'B', 'A', 'A'], ['B', 'B', 'A', 'A', 'B', 'B', 'A', 'A', 'B', 'B'], ['A', 'A', 'B', 'B', 'A', 'A', 'B', 'B', 'A', 'A']]
            },
            'x' : {
              'Factor' : ['Lev:1', 'Lev:2', 'Lev:1', 'Lev:2', 'Lev:1', 'Lev:2', 'Lev:1', 'Lev:2', 'Lev:1', 'Lev:2']
            },
            'z' : {
              'Annt' : ['Desc:1', 'Desc:2', 'Desc:1', 'Desc:2', 'Desc:1', 'Desc:2', 'Desc:1', 'Desc:2', 'Desc:1', 'Desc:2']
            }
          },
          {'graphType': 'Heatmap',
          'outlineBy': 'Outline',
          'outlineByData': 'data2',
          'shapeBy': 'Shape',
          'shapeByData': 'data3',
          'sizeBy': 'Size',
          'sizeByData': 'data4'}
        );
        cx4.clusterSamples();
        cx4.clusterVariables();

      }

      var showCode = function (e, id) {
        var cx = CanvasXpress.getObject(id)
        cx.stopEvent(e);
        cx.cancelEvent(e);
        cx.updateCodeDiv(10000);
        return false;
      }

    </script>
	</head>
	<body> 
	<div id="heatmap"></div>
	<svg id="mir_chart"></svg>
	
		
	<script>
	showDemo()
	//data
	var chrNum = ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","X","Y"];
	var dataset = [249250621,243199373,198022430,191154276,180915260,171115067,159138663,146364022,141213431,135534747,135006516,133851895,115169878,107349540,102531392,90354753,81195210,78077248,59128983,63025520,48129895,51304566,155270560,59373566]
	var miRCountsRel = <%=miRListRel%>
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
    .text("Relative frequency of miRNAs");
	
	//add the miR lines 
	for (var i=0;i<miRdata.length;i++){
		var m = miRdata[i].chr
		var c = m.match(/chr(.*)/)
		var chr = c[1]
		if (chr.match(/X/)){ chr = "23"}
		if (chr.match(/Y/)){ chr = "24"}
		//alert("chromosome: "+chr+ "start: "+miRdata[i].start)
		chart.append("svg:line")
			.attr("x1", (parseInt(chr)-1) * (w / dataset.length) + moveRight)			
    		.attr("y1", h - (yScale(miRdata[i].start) - moveUp))
    		.attr("x2", parseInt(chr) * (w / dataset.length) + moveRight - barPadding)
    		.attr("y2", h - (yScale(miRdata[i].start) - moveUp))
    		.style("stroke", "rgb(6,120,155)");
	} 
	//add line - https://www.dashingd3js.com/svg-paths-and-d3js
	var line = d3.svg.line()
    	.x(function(d,i) { return i * (w / dataset.length) + (w / dataset.length - barPadding) / 2 + moveRight })
    	.y(function(d) { return h - (y2Scale(d) - moveUp)});
    
    var addpath1 = chart.append('path')
		.attr('d', line(miRCounts))
		.attr("stroke", "red")
        .attr("stroke-width", 2)
    	.attr("fill", "none");
    	
    var addpath2 = chart.append('path')
		.attr('d', line(miRCountsRel))
		.attr("stroke", "green")
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
