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
				
				$('#mirTable').dataTable({
					"sPaginationType": "full_numbers",
					"aaSorting": [[ 1, "desc" ]],
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
				
				$('#mirTable tr')
					$('#mirTable tr').click(function(event) {
					if (event.target.type !== 'checkbox') {
					  $(':checkbox', this).trigger('click');
					}
				  });
		
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
				
				 // Select all
				$("A[href='#select_all']").click( function() {
					$("#" + $(this).attr('rel') + " INPUT[type='checkbox']").attr('checked', true).addClass('row_selected');
					return false;
				});
 
				// Select none
				$("A[href='#select_none']").click( function() {
					$("#" + $(this).attr('rel') + " INPUT[type='checkbox']").attr('checked', false);
					return false;
				});
 
				// Invert selection
				$("A[href='#invert_selection']").click( function() {
					$("#" + $(this).attr('rel') + " INPUT[type='checkbox']").each( function() {
						$(this).attr('checked', !$(this).attr('checked'));
					});
					return false;
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
	<p>- Each miRNA is represented by a blue circle, and blue lines connect them to target genes (green circles). 
	<p>- The colour and thickness of the blue lines represents the strength of the StarBase prediction (thicker darker line = more computational evidence)
	<p>- Red circles are transcription factors and red lines show cases where they target miRNAs.
	<p>- Each circle is sticky and can be moved and placed anywhere. To unstick just double click on the node. 
	- 
	<svg id="network_svg"></svg>
	
	<a href="javascript:void(0);" onclick="updateNetwork()"><h1>Update</h1></a>
	<div id="n_1">
		 <input type="button" class="tabbuttons" value="1- MiRNA" style="color:#BFBFBF"/>
         <input type="button" class="tabbuttons" onclick="switchTab('2','1');" value="2 - Gene targets"/>
         <div style="border:2px solid; border-color:#BFBFBF">
			<h1>Step 1 - filter the set of miRNAs</h1>
			<p>Either manually select the miRNAs of interest by clicking on the rows below, or select choose an option below:
			<p>Select <a rel="mirSelect" href="#select_all">All</a> | 
					  <a rel="mirSelect" href="#select_none">None</a> | 
					  <a rel="mirSelect" href="#invert_selection">Invert</a>
			<fieldset id="mirSelect">
				<table id="mirTable">
					<thead>
							<tr><th width="3%"></th><th>MiRNA ID</th><th>MiRNA accession</th><th>Seed</th><th>Location</th><th>Flag</th></tr>
					</thead>
					<tbody>
						<g:each var="r" in="${n1}">
						<tr>
							<td><g:checkBox name="funCheck" value="${r.id}" checked="true"/></td>
							<td>${r.matid}</td>
							<td><a href="http://mirbase.org/cgi-bin/mirna_entry.pl?acc=${r.matacc}" target="_blank">${r.matacc}</a></td>				
							<td><font face="courier new">${r.matseq.toUpperCase()[1..6]}</font></td>
							<!--td><font face="courier new">${r.matseq.toUpperCase()[0]}<b>${r.matseq.toUpperCase()[1..6]}</b>${r.matseq.toUpperCase()[6..-1]}</font></td-->
							<td><a href="http://asia.ensembl.org/Homo_sapiens/Location/View?db=core;r=<%="${r.chr}".replaceAll("chr","")%>:${r.start}-${r.stop}" target="_blank">${r.chr}: ${r.start}-${r.stop}</a></td>
							<td><% if (r.description!=null){print "${r.description}"}else{print "None"} %></td>
						</tr>
						</g:each>
					</tbody>
				</table>
			 </fieldset>
			<g:form name="network" url="[action:'network']">
				<g:hiddenField name="common_genes" value="${gList}"/>
				<g:hiddenField name="common_mirs" value="${mList}"/>
				<a href="javascript:void(0);" onclick="document.network.submit()"><h1>Next Step</h1></a>
			</g:form> 
		</div>
	</div>
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
		
		var ndata = <%=ndata%>
		
		var width = 1200,
		height = 800,
		root;
				
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
		
		var path = svg.append("svg:g").selectAll("path")
		var node = svg.selectAll(".node") 
		var link = svg.selectAll(".link");
		
		var force = d3.layout.force() 
			//.nodes(d3.values(nodes)) 
			.size([width, height]) 
			.linkDistance(60) 
			.charge(-2000) 
			.on("tick", tick) 
			.linkStrength(0.5)
			.friction(0.5)
			.gravity(0.5)
		
		d3.json(ndata, function(json) {
			root = json;
  			update();
		});
		
		function update(){
			console.log("updating")
			var nodes = ndata.nodes;
			var links = ndata.links;
			
			force
				.nodes(nodes)
				.links(links)
				.start();
			
			var v = d3.scale.linear().range([0, 100]);
		
			v.domain([0, d3.max(ndata.links, function(d) { return d.value; })]);
			ndata.links.forEach(function(link) {
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
			path = svg.append("svg:g").selectAll("path")
				.data(force.links())
			 .enter().append("svg:path")
				.attr("class", function(d) { return "link " + d.type; })
				.attr("marker-end", "url(#end)");
	
			 // Update the links…
			//link = link.data(links, function(d) { return d.target.id; });

			// Exit any old links.
			//link.exit().remove();
			
			// Update the nodes…
			//node = node.data(nodes, function(d) { return d.name; }).style("fill", "blue");
			node = node.data(nodes)
			
			// Exit any old nodes.
			node.exit().remove();
			console.log('node = '+node)
				
			// define the nodes
			//node = svg.selectAll(".node") 
			   node.enter().append("g")
 			  	.data(force.nodes())
 				.attr("class", "node")
// 				//.on("click", click) 
 				.on("dblclick", dblclick)
// 				.call(force.drag)
// 				//.on("mouseover", fade(.1)).on("mouseout", fade(1));;
// 			
					
			// add the nodes and change colour and sizes accordingly
			node.append("circle")
			.attr("class", "node")
			.on("dblclick", dblclick)
				.call(force.drag)
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
				.text(function(d) { 
					if (d.flag.match(/[a-b]/)){
						return d.name +" ("+d.flag+")"; 
					}else{
						return d.name
					}
				});
		
			//make nodes sticky when moved
			node.on("mousedown", function(d) { d.fixed = true; });
		}
		
		
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
		var i = 0;
		function updateNetwork(){
			ndata.nodes.splice(1, 1);
			console.log(ndata.nodes + 'i = '+i)
			i++;
			update()
			
		}
		
		</script>
	</body>
</html>
