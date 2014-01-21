<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Home</title>
	
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.dataTables.js')}" type="text/javascript"></script>		
		<script src="${resource(dir: 'js', file: 'jquery-ui-1.10.3/ui/jquery-ui.js')}" type="text/javascript"></script>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_table.css')}" type="text/css"></link>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_page.css')}" type="text/css"></link>
		<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">
		
		<script>
  			function demoMir(a){
  				if (a == '1'){
  					$("#mirList").val("hsa-miR-4314 0.02\nhsa-miR-1294 0.02\nhsa-miR-552 0.02\nhsa-miR-4297 0.04\nhsa-miR-550a-3p 0.04\nhsa-miR-432-3p 0.04\nhsa-miR-193b-3p 0.04\nhsa-miR-342-5p 0.04\nhsa-miR-541-3p 0.04\nhsa-miR-193a-3p 0.05\nhsa-miR-489 0.05\nhsa-miR-3192 0.05\nhsa-miR-892b 0.05\nhsa-miR-148b-5p 0.05\nhsa-miR-3140-3p 0.05\nhsa-miR-654-5p 0.06\nhsa-miR-876-3p 0.06\nhsa-miR-3160-3p 0.06\nhsa-miR-3189-3p 0.06\nhsa-miR-1289 0.06\nhsa-miR-19b-1-5p 0.06\nhsa-miR-1293 0.08\nhsa-miR-634 0.08\nhsa-miR-3165 0.1\nhsa-miR-323a-5p 0.11\nhsa-miR-1285-3p 0.13\nhsa-miR-197-3p 0.15\nhsa-miR-1231 0.15\nhsa-miR-3936 0.16\nhsa-miR-744-5p 0.17\nhsa-miR-4283 0.18\nhsa-miR-3115 0.18\nhsa-miR-644a 0.19\nhsa-miR-330-5p 0.2\nhsa-miR-3180 0.2\nhsa-miR-3907 0.2\nhsa-miR-638 0.42\nhsa-miR-664a-3p 0.44\nhsa-miR-27a-3p 0.44\nhsa-miR-612 0.44\nhsa-miR-1281 0.49");
  				}if (a == '2'){
  					$("#mirList").val("hsa-miR-380-3p 0.2\n hsa-miR-3181 0.34\n hsa-miR-3934-5p 0.34\n hsa-miR-515-3p 0.38\n hsa-miR-3151 0.4\n hsa-miR-518c-5p 0.4\n hsa-miR-652-3p 0.41\n hsa-miR-513c-5p 0.42\n hsa-miR-3162-5p 0.44\n hsa-miR-125a-3p 0.45\n hsa-miR-199b-5p 0.48\n hsa-miR-4264 0.5\n hsa-miR-299-3p 0.52\n hsa-miR-411-3p 0.52\n hsa-let-7a-3p 0.53\n hsa-miR-4302 0.53\n hsa-miR-190b 0.53\n hsa-miR-4275 0.54\n hsa-miR-10a-5p 0.54\n hsa-miR-625-3p 0.55\n");
  				}
  			}
  			
  			function demoScreen(a){
  				if (a == '1'){
  					$("select[name='vSelect']").val("lt");
  					$("select[name='library']").val("inhibitors");
  					$("#vValue").val("0.5");
  					$("#d1Value").val("");
  					$("#d2Value").val("");
  				}if (a == '2'){
  					$("select[name='vSelect']").val("gt");
  					$("select[name='library']").val("mimics");
  					$("#vValue").val("0.8");
  					$("#d2Value").val("0.5");
  					$("#d1Value").val("");
  				}
  			}
  			
  			function switchTab(tabShow,tabHide) {
                $("#tab_"+tabHide).hide();
                $("#tab_"+tabShow).show();
    		}
    		
  		</script>
  		
  		<script>
  		$(document).ready(function() {
  			$('#single_mir').autocomplete({
                 source: '<g:createLink controller='search' action='ajaxMirFinder'/>'
               }); 
  			
			$('#meta').dataTable({
					"bPaginate": false,  
      				"bFilter": false,     
					"aaSorting": [[ 1, "asc" ]],
					"iDisplayLength": 10,
                	"oLanguage": {
                        "sSearch": "Filter records:"
                	},
                	"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
                	"sDom": 'rt<"bottom"flp>'
					
				});
			//stop the page submit if no screen data was selected	
			//$("#tab_2 :button").click(function (e) { 
			//	if(!$('#tab_2 input[type="checkbox"]').is(':checked')){
    		// 		return false;
    		// 	}
 			//});
 			 $('#meta tr')
				.filter(':has(:checkbox:checked)')
				.addClass('row_selected')
				.end()
			  .click(function(event) {
				$(this).toggleClass('row_selected');
				if (event.target.type !== 'checkbox') {
				  $(':checkbox', this).attr('checked', function() {
					return !this.checked;
				  });
				}
			  });
		});
  		</script>

  		
	</head>
	<body>
		<br>
		<div id="tab_1">
            <input type="button" class="tabbuttons" value="MiRNA search" style="color:#BFBFBF"/>
            <input type="button" class="tabbuttons" onclick="switchTab('2','1');" value="Screen Search"/>
            <div style="border:2px solid; border-color:#BFBFBF">

				<br>Either enter a single mature miRBase ID or accession into the small box, a list of IDs or accessions into the large box or upload a file containing a list. Lists should be IDs or accessions on separate lines and can also include a space delimited score/rank.
			
				<g:uploadForm name="search_mir" action="search_res" method="post">
			
				<br><h1>Single mature miRNA</h1>
				Either a miRBase ID (e.g. hsa-miR-302b-5p) or miRBase accession (e.g. MIMAT0000714)
				<div class="demo">
				<div class="ui-widget">
					<input id="single_mir" name="single" >
				</div>
			
				<br><br><h1>List of mature miRNAs</h1>
				<div class="chart"></div>
				Example lists: <a href = "javascript:void(0)" onclick="demoMir('1')">Top essential miRNAs</a> | <a href = "javascript:void(0)" onclick="demoMir('2')">Synthetic lethals (Kelly Vincristine)</a>
			
			
					<br>Upload a file <input type="file" name="myFile"/>
					<br>
					<g:textArea name="mirList" rows="5" cols="40" style="width: 60%;"/>
					<br>StarBase evidence: 
					<select name="sEv">
						<option value="0">1 or more</option>
						<option value="1">2 or more</option>
						<option value="2">3 or more</option>
						<option value="3">4 or more</option>
						<option value="4">5</option>
					</select>
					<!--br>MiRTarBase evidence: 
					<select name="mEv">
						<option value="0">All</option>
						<option value="1">No weak evidence</option>
					</select-->
					<br><br><input class="smallbuttons" type="button" value="Search" id="process" onclick="submit()" >
				</g:uploadForm>
			</div>
		</div>
	</div>
		<div id="tab_2" style="display:none">
        	<input type="button" class="tabbuttons" onclick="switchTab('1','2');" value="MiRNA search" />
         	<input type="button" class="tabbuttons" value="Screen Search" style="color:#BFBFBF"/>
         		<div style="border:2px solid; border-color:#BFBFBF">
         		<h1>Choose screen data</h1>
         		<p>Click anywhere on the two to select the screen data
				<g:form action="screen_res">
				<table id="meta">
				<thead>
				<tr><th width="3%"></th><th>Type</th><th>Cell</th><th>Drug 1</th><th>Drug 2</th><th>Mimics</th><th>Inhibitors</th></tr>
				</thead>
				<tbody>
				<g:each var="r" in="${meta}">
					<tr><td><g:checkBox name="funCheck" value="${r.id}" checked="false"/></td><td>${r.type}</td><td>${r.cell}</td><td>${r.d1name}</td><td>${r.d2name}</td><td>${r.sd.findAll({it.library == 'mimics'}).size()}</td><td>${r.sd.findAll({it.library == 'inhibitors'}).size()}</td></tr>
				</g:each>
				</tbody>
				</table>
				<br>
				<h1>Set paramaters (leave blank to ignore)</h1>
				Example settings: <a href = "javascript:void(0)" onclick="demoScreen('1')">Essential inhibitors</a> | <a href = "javascript:void(0)" onclick="demoScreen('2')">Synthetic lethal mimics</a>

				<table>
					<tr>
						<td>Vehicle</td>
						<td>
							<select name="vSelect">					
							<option value="lt"><</option>
							<option value="gt">></option>
							<option value="eq">=</option>
						</td>	
						<td><g:textField name="vValue" /></td>
					</tr>
					<tr>
						<td>Drug 1</td>
						<td>
							<select name="d1Select">					
							<option value="lt"><</option>
							<option value="gt">></option>
							<option value="eq">=</option>
						</td>	
						<td><g:textField name="d1Value"/></td>
					</tr>
					<tr>
						<td>Drug 2</td>
						<td>
							<select name="d2Select">					
							<option value="lt"><</option>
							<option value="gt">></option>
							<option value="eq">=</option>
						</td>	
						<td><g:textField name="d2Value"/></td>
					</tr>
					<tr><td>
					<select name="library">					
						<option value="mimics">Mimics</option>
						<option value="inhibitors">Inhibitors</option>
						<option value="Either">Either</option>
					</td><td></td>
					<td>
						<input class="smallbuttons" type="button" value="Search" id="process" onclick="submit()" >
					</td></tr>
					</table>
				</g:form>
         	</div>
         </div>
	</body>
</html>
