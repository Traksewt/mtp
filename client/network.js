// get the data
//d3.csv("${resource(dir: 'js', file: 'd3/network.csv')}", function(error, links) { 

//var nodes = {};
//// Compute the distinct nodes from the links.
//links.forEach(function(link) { 
//link.source = nodes[link.source] ||
//(nodes[link.source] = {name: link.source});
//link.target = nodes[link.target] ||
//(nodes[link.target] = {name: link.target});
//link.value = +link.value;
//});


var width = 1200,
height = 800;
var border = 50;
var evidenceWeight = 50;
var foci = {"TF-mir": border, "gene" : height - border, "miRNA" : height / 2 , "s1": evidenceWeight, "s2" : evidenceWeight / 2, "s3" : 0, "s4" : -evidenceWeight/2, "s5" : -evidenceWeight, "TFmir" : 0, "tf-gene" : 0};

d3.json(data, function(json) {

	var color = d3.scale.category20c();

	var svg = d3.select("#network_svg")
	.attr("width", width) 
	.attr("height", height)
	.attr("viewBox", "0 0 " + width + " " + height )
	.attr("preserveAspectRatio", "xMidYMid meet")
	.attr("pointer-events", "all")
	// .append('svg:g')
	// .call(d3.behavior.zoom().on("zoom", redraw));
	/**
	 * function redraw() { svg.attr("transform", "translate(" +
	 * d3.event.translate + ")" + " scale(" + d3.event.scale + ")"); }
	 */


	var v = d3.scale.linear().range([0, 100]);

	v.domain([0, d3.max(data.links, function(d) { return d.value; })]);
	// console.log(d3.max(data.links, function(d) { return d.value; }))

	var miRNAs = {};

	data.links.forEach(function(link) {
		if (v(link.value) == 0) {
			link.type = "TFmir";
			// }else if (v(link.value) <= 25) {
		}else if ((link.value) == 1) {
			link.type = "s1";
			// }else if (v(link.value) <= 50 && v(link.value) > 25) {
		}else if ((link.value) == 2) {
			link.type = "s2";
			// }else if (v(link.value) <= 75 && v(link.value) > 50) {
		}else if ((link.value) == 3) {
			link.type = "s3";
			// }else if (v(link.value) <= 100 && v(link.value) > 75) {
		}else if ((link.value) == 4) {
			link.type = "s4";
		}else if ((link.value) == 5) {
			link.type = "s5";
		}

		if(link.name == "tf-gene"){
			console.log(link.name)
			link.type = "tf-gene";
		}
		var adjustment = foci[link.type];
//		adjustment = 0;
		data.nodes[link.source].y = foci[data.nodes[link.source].type] + adjustment;
		data.nodes[link.target].y = foci[data.nodes[link.target].type] + adjustment;
		if (data.nodes[link.source].type === 'miRNA') {
			miRNAs[data.nodes[link.source].name] = data.nodes[link.source];
		}

	});

	var miRNAList = Object.keys(miRNAs);
	miRNAList.forEach(function (key, i) {
		var o = miRNAs[key];
		o.x = 2 * border + (width - 4 * border) * i / miRNAList.length;
		o.px = o.x;
		o.fixed = true;
	})

	
	var force = d3.layout.force() 
	// .nodes(d3.values(nodes))
	.nodes(data.nodes)
	// .links(links)
	.links(data.links)
	.size([width, height]) 
	.linkDistance(60) 
	.charge(-2000) 
	.on("tick", tick) 
	.linkStrength(1)
	.friction(0.5)
	.gravity(0)
	.start();



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
	// .on("click", click)
	.on("dblclick", dblclick)
	.call(force.drag)
	// .on("mouseover", fade(.1)).on("mouseout", fade(1));;


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

	// make nodes sticky when moved
	node.on("mousedown", function(d) { d.fixed = true; });

	function dblclick(d) {
		d3.select(this).classed("fixed", d.fixed = false);
	}

	// add the curvy lines
	function tick(e) { 
		
		var k = .5 * e.alpha;

		// Push nodes toward their designated focus.
		force.links().forEach(function(o, i) {
			o.source.y += (foci[o.source.type] + foci[o.type] - o.source.y) * k;
			o.target.y += (foci[o.target.type] + foci[o.type] - o.target.y) * k;
//			o.x += (foci[o.id].x - o.x) * k;
		});

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
