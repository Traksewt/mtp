<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Home</title>
		<style type="text/css" media="screen">
			#status {
				background-color: #eee;
				border: .2em solid #fff;
				margin: 2em 2em 1em;
				padding: 1em;
				width: 12em;
				float: left;
				-moz-box-shadow: 0px 0px 1.25em #ccc;
				-webkit-box-shadow: 0px 0px 1.25em #ccc;
				box-shadow: 0px 0px 1.25em #ccc;
				-moz-border-radius: 0.6em;
				-webkit-border-radius: 0.6em;
				border-radius: 0.6em;
			}

			.ie6 #status {
				display: inline; /* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
			}

			#status ul {
				font-size: 0.9em;
				list-style-type: none;
				margin-bottom: 0.6em;
				padding: 0;
			}

			#status li {
				line-height: 1.3;
			}

			#status h1 {
				text-transform: uppercase;
				font-size: 1.1em;
				margin: 0 0 0.3em;
			}

			#page-body {
				margin: 2em 1em 1.25em 18em;
			}

			h2 {
				margin-top: 1em;
				margin-bottom: 0.3em;
				font-size: 1em;
			}

			p {
				line-height: 1.5;
				margin: 0.25em 0;
			}

			#controller-list ul {
				list-style-position: inside;
			}

			#controller-list li {
				line-height: 1.3;
				list-style-position: inside;
				margin: 0.25em 0;
			}

			@media screen and (max-width: 480px) {
				#status {
					display: none;
				}

				#page-body {
					margin: 0 1em 1em;
				}

				#page-body h1 {
					margin-top: 0;
				}
			}
		</style>

		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
		<script>
  			function demoMir(a){
  				if (a == '1'){
  					$("#mirList").val("hsa-miR-4314\t0.02\nhsa-miR-1294\t0.02\nhsa-miR-552\t0.02\nhsa-miR-4297\t0.04\nhsa-miR-550a-3p\t0.04\nhsa-miR-432-3p\t0.04\nhsa-miR-193b-3p\t0.04\nhsa-miR-342-5p\t0.04\nhsa-miR-541-3p\t0.04\nhsa-miR-193a-3p\t0.05\nhsa-miR-489\t0.05\nhsa-miR-3192\t0.05\nhsa-miR-892b\t0.05\nhsa-miR-148b-5p\t0.05\nhsa-miR-3140-3p\t0.05\nhsa-miR-654-5p\t0.06\nhsa-miR-876-3p\t0.06\nhsa-miR-3160-3p\t0.06\nhsa-miR-3189-3p\t0.06\nhsa-miR-1289\t0.06\nhsa-miR-19b-1-5p\t0.06\nhsa-miR-1293\t0.08\nhsa-miR-634\t0.08\nhsa-miR-3165\t0.1\nhsa-miR-323a-5p\t0.11\nhsa-miR-1285-3p\t0.13\nhsa-miR-197-3p\t0.15\nhsa-miR-1231\t0.15\nhsa-miR-3936\t0.16\nhsa-miR-744-5p\t0.17\nhsa-miR-4283\t0.18\nhsa-miR-3115\t0.18\nhsa-miR-644a\t0.19\nhsa-miR-330-5p\t0.2\nhsa-miR-3180\t0.2\nhsa-miR-3907\t0.2\nhsa-miR-638\t0.42\nhsa-miR-664a-3p\t0.44\nhsa-miR-27a-3p\t0.44\nhsa-miR-612\t0.44\nhsa-miR-1281\t0.49");
  				}if (a == '2'){
  					$("#mirList").val("hsa-miR-380-3p\t0.2\nhsa-miR-3934-5p\t0.34\nhsa-miR-3181\t0.34\nhsa-miR-515-3p\t0.38\nhsa-miR-518c-5p\t0.4\nhsa-miR-3151\t0.4\nhsa-miR-652-3p\t0.41\nhsa-miR-513c-5p\t0.42\nhsa-miR-3162-5p\t0.44\nhsa-miR-125a-3p\t0.45\nhsa-miR-199b-5p\t0.48\nhsa-miR-4264\t0.5\nhsa-miR-411-3p\t0.52\nhsa-miR-299-3p\t0.52\nhsa-miR-3670\t0.52\nhsa-let-7a-3p\t0.53\nhsa-miR-190b\t0.53\nhsa-miR-4302\t0.53\nhsa-miR-4275\t0.54\nhsa-miR-320c\t0.54\nhsa-miR-10a-5p\t0.54");
  				}
  			}
  			
  		</script>

  		
	</head>
	<body>
		<a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="status" role="complementary">
			<h1>Application Status</h1>
			<ul>
				<li>App version: <g:meta name="app.version"/></li>
				<li>Grails version: <g:meta name="app.grails.version"/></li>
				<li>Groovy version: ${GroovySystem.getVersion()}</li>
				<li>JVM version: ${System.getProperty('java.version')}</li>
				<li>Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</li>
				<li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
				<li>Domains: ${grailsApplication.domainClasses.size()}</li>
				<li>Services: ${grailsApplication.serviceClasses.size()}</li>
				<li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
			</ul>
			<h1>Installed Plugins</h1>
			<ul>
				<g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
					<li>${plugin.name} - ${plugin.version}</li>
				</g:each>
			</ul>
		</div>
		<div id="page-body" role="main">
			<h1>Search</h1>

				<div class="chart"></div>
			Example lists: <a href = "javascript:void(0)" onclick="demoMir('1')">Top down regulated</a> | <a href = "javascript:void(0)" onclick="demoMir('2')">Top synthetic lethals</a>
			
			<g:uploadForm name="search_mir" action="search_res" method="post">
				<br>Upload a file <input type="file" name="myFile"/>
				<g:textArea name="mirList" rows="5" cols="40" style="width: 60%;"/>
				<br>StarBase evidence: 
				<select name="sEv">
					<option value="0">1 or more</option>
					<option value="1">2 or more</option>
					<option value="2">3 or more</option>
					<option value="3">4 or more</option>
					<option value="4">5</option>
				</select>
				<br>MiRTarBase evidence: 
				<select name="mEv">
					<option value="0">All</option>
					<option value="1">No weak evidence</option>
				</select>
				<br><input class="smallbuttons" type="button" value="Search" id="process" onclick="submit()" >
			</g:uploadForm>
		</div>
	</body>
</html>
