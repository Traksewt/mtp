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
					"aaSorting": [[ 3, "desc" ]],
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
		</script>
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
	<p>- Each miRNA is a blue circle, and blue lines connect them to target genes (green nodes). 
	<p>- The colour of the blue lines represents the strength of the StarBase prediction (darker line = more computational evidence)
	<p>- Red nodes are TFs and red lines show cases where they target miRNAs.
	<p>- Each node is sticky and can be moved and placed anywhere. To unstick just double click on the node. 
	- 
	<svg id="network_svg"></svg>
	<table id="common">
            <thead>
				<tr><th>Gene symbol</th><th>Name</th><th width="30%">Location</th><th>Count</th></tr>
			</thead>
			<tbody>
				<g:each var="r" in="${session.commonGenes}">
				<tr>
					<td><a href = "http://www.genecards.org/cgi-bin/carddisp.pl?gene=${r.name}" target="_blank">${r.name}</a></td>
					<td><a href="javascript:void(0);" onclick="click()">${r.fullname}</a></td>
					<td><a href="http://asia.ensembl.org/Homo_sapiens/Location/View?db=core;r=${r.location}" target="_blank">${r.location}</a></td>
					<td>${r.count}</td>
				</tr>
				</g:each>
			</tbody>
		</table>
	<script>
		// get the data
		//d3.csv("${resource(dir: 'js', file: 'd3/network.csv')}", function(error, links) { 
// 		
// 		var nodes = {};
// 		// Compute the distinct nodes from the links.
// 		links.forEach(function(link) { 
// 			link.source = nodes[link.source] ||
// 				(nodes[link.source] = {name: link.source});
// 			link.target = nodes[link.target] ||
// 				(nodes[link.target] = {name: link.target});
// 			link.value = +link.value;
// 		});
		
		var data = <%=ndata%>
		
		var width = 1200,
		height = 800;
		
		d3.json(data, function(json) {
				
		var color = d3.scale.category20c();
		
		var svg = d3.select("#network_svg")
			.attr("width", width) 
			.attr("height", height)
			.attr("viewBox", "0 0 " + width + " " + height )
      		.attr("preserveAspectRatio", "xMidYMid meet")
      		.attr("pointer-events", "all")
      		//.append('svg:g')
    		//.call(d3.behavior.zoom().on("zoom", redraw));
		/**
		function redraw() {
  			svg.attr("transform",
      		"translate(" + d3.event.translate + ")"
      		+ " scale(" + d3.event.scale + ")");
		}	
		**/
		
		var force = d3.layout.force() 
			//.nodes(d3.values(nodes)) 
			.nodes(data.nodes)
			//.links(links) 
			.links(data.links)
			.size([width, height]) 
			.linkDistance(60) 
			.charge(-2000) 
			.on("tick", tick) 
			.linkStrength(0.5)
			.friction(0.5)
			.gravity(0.5)
			.start();
			
		var v = d3.scale.linear().range([0, 100]);
		
		v.domain([0, d3.max(data.links, function(d) { return d.value; })]);
		
		data.links.forEach(function(link) {
			if (v(link.value) == 0) {
				link.type = "TFmir";
			//}else if (v(link.value) <= 25) {
			}else if ((link.value) == 1) {
				link.type = "s1";
			//}else if (v(link.value) <= 50 && v(link.value) > 25) {
				}else if ((link.value) == 2) {
				link.type = "s2";
			//}else if (v(link.value) <= 75 && v(link.value) > 50) {
			}else if ((link.value) == 3) {
				link.type = "s3";
			//}else if (v(link.value) <= 100 && v(link.value) > 75) {
			}else if ((link.value) == 4) {
				link.type = "s4";
			}else if ((link.value) == 5) {
				link.type = "s5";
			}
			
			if(link.name == "tf-gene"){
				console.log(link.name)
				link.type = "tf-gene";
			}
		});
		

		// build the arrow.
		
		svg.append("svg:defs").selectAll("marker")
			.data(["end"]) // Different link/path types can be defined here
		  .enter().append("svg:marker")
			.attr("id", String)
			.attr("viewBox", "0 -5 10 10")
			.attr("refX", 15)
			.attr("refY", -1.5)
			.attr("markerWidth", 6)
			.attr("markerHeight", 6)
			.attr("orient", "auto")
		  .append("svg:path")
			.attr("d", "M0,-5L10,0L0,5");
		
		// add the links and the arrows
		// This section adds in the arrows
		var path = svg.append("svg:g").selectAll("path")
			.data(force.links())
  		 .enter().append("svg:path")
    		.attr("class", function(d) { return "link " + d.type; })
    		.attr("marker-end", "url(#end)");
	
		var link = svg.selectAll("line").data(data.links).enter().append("svg:line");
    		
		// define the nodes
		var node = svg.selectAll(".node") 
			.data(force.nodes())
		  .enter().append("g")
			.attr("class", "node")
			//.on("click", click) 
			.on("dblclick", dblclick)
			.call(force.drag)
			//.on("mouseover", fade(.1)).on("mouseout", fade(1));;
			
					
		// add the nodes and change colour and sizes accordingly
		node.append("circle")
			.attr("r", function(d) { 
				if (d.name.match(/^hsa/)){
					return 5; 
				}else{
					return 5
				}
			})
			.style("fill", function(d) { 
				if (d.name.match(/^hsa/)){
					return 'blue'; 
				}else if(d.type.match(/^TF-/)){
					return 'red';
				}else{
					return 'green';
				}
			})
			
		// add the text
		node.append("text")
			.attr("x", 12)
			.attr("dy", ".35em") 
			.text(function(d) { return d.name; });
		
		//make nodes sticky when moved
		node.on("mousedown", function(d) { d.fixed = true; });
		
		function dblclick(d) {
  			d3.select(this).classed("fixed", d.fixed = false);
		}
		
		// add the curvy lines
		function tick() { 
			path.attr("d", function(d) {
				var dx = d.target.x - d.source.x,
				dy = d.target.y - d.source.y,
				dr = Math.sqrt(dx * dx + dy * dy);
			return "M" +
				d.source.x + "," + d.source.y + "A" +
				dr + "," + dr + " 0 0,1 " + d.target.x + "," + d.target.y;
		});
		
		node
			.attr("transform", function(d) {
				return "translate(" + d.x + "," + d.y + ")"; });
		} 
		
		});
		</script>
	</body>
</html>
