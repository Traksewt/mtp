<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Test</title>
		<script src="${resource(dir: 'js', file: 'd3/d3.v3.min.js')}" type="text/javascript"></script>  		
		
		<style>
		path.link {
			fill: none;
			/**stroke: #666;**/ 
			stroke: #0000ff;
			stroke-width: 1.5px;
		}
		path.link.twofive { 
			opacity: 0.25;
		}
		path.link.fivezero { 
			opacity: 0.50;
		}
		path.link.sevenfive { 
			opacity: 0.75;
		}
		path.link.onezerozero { 
			opacity: 1.0;
		}
		path.link.TFmir{
			stroke: #ff0000; 
			opacity: 0.5;
			stroke-width: 1.0px;
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
		${d}
		<script>
		// get the data
		d3.csv("${resource(dir: 'js', file: 'd3/network.csv')}", function(error, links) { 
		
		var nodes = {};
		// Compute the distinct nodes from the links.
		links.forEach(function(link) { 
			link.source = nodes[link.source] ||
				(nodes[link.source] = {name: link.source});
			link.target = nodes[link.target] ||
				(nodes[link.target] = {name: link.target});
			link.value = +link.value;
		});
		
		var width = 1200,
		height = 1000;
		
		var color = d3.scale.category20c();
		
		var force = d3.layout.force() 
			.nodes(d3.values(nodes)) 
			.links(links) 
			.size([width, height]) 
			.linkDistance(60) 
			.charge(-2000) 
			.on("tick", tick) 
			.linkStrength(0.5)
			.friction(0.5)
			.gravity(0.5)
			.start();
			
		var v = d3.scale.linear().range([0, 100]);
		
		v.domain([0, d3.max(links, function(d) { return d.value; })]);
		
		links.forEach(function(link) {
			if (v(link.value) == 0) {
				link.type = "TFmir";
			}else if (v(link.value) <= 25) {
				link.type = "twofive";
			} else if (v(link.value) <= 50 && v(link.value) > 25) {
				link.type = "fivezero";
			} else if (v(link.value) <= 75 && v(link.value) > 50) {
				link.type = "sevenfive";
			} else if (v(link.value) <= 100 && v(link.value) > 75) {
				link.type = "onezerozero";
		} });
		
		var svg = d3.select("body").append("svg") 
			.attr("width", width) 
			.attr("height", height);
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

    		
		// define the nodes
		var node = svg.selectAll(".node") 
			.data(force.nodes())
		  .enter().append("g")
			.attr("class", "node")
			//.on("click", click) 
			//.on("dblclick", dblclick)
			.call(force.drag);
			
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
				}else if(d.name.match(/^TF-/)){
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
// 		function click() { 
// 		  d3.select(this).select("text").transition()
//         	.duration(750)
//         	.attr("x", 22)
//         	.style("fill", "steelblue")
//         	.style("stroke", "lightsteelblue")
//         	.style("stroke-width", ".5px")
//         	.style("font", "20px sans-serif");
// 		  d3.select(this).select("circle").transition() .duration(750)
//         	.attr("r", 16)
//         	.style("fill", "lightsteelblue");
// 		}
// 		function dblclick() { 
// 		  d3.select(this).select("circle").transition()
// 			.duration(750)
// 			.attr("r", 6)
// 			.style("fill", "#ccc");
// 		  d3.select(this).select("text").transition() .duration(750)
// 			.attr("x", 12)
// 			.style("stroke", "none")
// 			.style("fill", "black")
// 			.style("stroke", "none")
// 			.style("font", "10px sans-serif");
// 		}

		});
		</script>
	</body>
</html>
