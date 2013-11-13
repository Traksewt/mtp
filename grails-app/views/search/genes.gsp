<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to Grails</title>
	
		
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_table.css')}" type="text/css"></link>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_page.css')}" type="text/css"></link>

	</head>
	<body>
		<h1>${params.matid}</h1>
		Downloads:<br>
		<g:form name="starDownload" url="[controller:'Download', action:'gene_download']" style="display: inline" >
        	<g:hiddenField name="fileData" value="${starGenes}"/>
            <g:hiddenField name="fileName" value="${params.matid}.StarBase.txt"/>
            <a href="#" onclick="document.starDownload.submit()">${starAll.size()} StarBase genes</a>
    	</g:form>
    	<br>
    	<g:form name="mirDownload" url="[controller:'Download', action:'gene_download']" style="display: inline" >
        	<g:hiddenField name="fileData" value="${mtGenes}"/>
            <g:hiddenField name="fileName" value="${params.matid}.MirTarBase.txt"/>
            <a href="#" onclick="document.mirDownload.submit()">${mtAll.size()} MirTarBase genes</a>
    	</g:form>
    	<br>
    	<g:form name="interDownload" url="[controller:'Download', action:'gene_download']" style="display: inline" >
        	<g:hiddenField name="fileData" value="${interGenes}"/>
            <g:hiddenField name="fileName" value="${params.matid}.intersection.txt"/>
            <a href="#" onclick="document.interDownload.submit()">${interGeneMap.size()} intersection genes</a>
    	</g:form>
	</body>
</html>
