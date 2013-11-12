<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to Grails</title>
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
		<script>
  			function demoMir(a){
  				if (a == '1'){
  					$("#mirList").val("hsa-miR-125a-3p\nhsa-miR-515-3p\nhsa-miR-652-3p");
  				}if (a == '2'){
  					$("#mirList").val("hsa-miR-380-3p\nhsa-miR-3181\nhsa-miR-3934-5p\nhsa-miR-515-3p\nhsa-miR-518c-5p\nhsa-miR-3151\nhsa-miR-652-3p\nhsa-miR-513c-5p\nhsa-miR-3162-5p\nhsa-miR-125a-3p\nhsa-miR-199b-5p\nhsa-miR-4264\nhsa-miR-3670\nhsa-miR-411-3p\nhsa-miR-299-3p\nhsa-miR-4302\nhsa-miR-190b\nhsa-let-7a-3p\nhsa-miR-320c\nhsa-miR-4275\nhsa-miR-10a-5p\nhsa-miR-625-3p\nhsa-miR-639\nhsa-miR-1909-5p\nhsa-miR-3664-5p\nhsa-miR-1252\nhsa-miR-3617-5p\nhsa-miR-941\nhsa-miR-127-3p\nhsa-miR-122-3p\nhsa-miR-4267");
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
			<a href = "javascript:void(0)" onclick="demoMir('1')">Example list 1</a> <a href = "javascript:void(0)" onclick="demoMir('2')">Example list 2</a>
			<g:form name="search_mir" action="search_res">
				<g:textArea name="mirList" rows="5" cols="40" style="width: 60%;"/>
				<br><input class="smallbuttons" type="button" value="Search" id="process" onclick="submit()" >
			</g:form>
		</div>
	</body>
</html>
