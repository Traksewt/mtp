<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Download</title>
	
		
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.dataTables.js')}" type="text/javascript"></script>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_table.css')}" type="text/css"></link>
		<link rel="stylesheet" href="${resource(dir: 'js', file: 'DataTables-1.9.4/media/css/demo_page.css')}" type="text/css"></link>

	</head>
	<body>
		<h3>Download gene interactions for ${params.matid}:</h3><br>
		<table class="compact_left"><tr><td>StarBase</td>
		<td>
		<g:form name="starDownload" url="[controller:'Download', action:'gene_download']" style="display: inline" >
        	<g:hiddenField name="fileData" value="${starGenes}"/>
            <g:hiddenField name="fileName" value="${params.matid}.StarBase.txt"/>
            <a href="#" onclick="document.starDownload.submit()">${starAll.size()}</a>
    	</g:form>
    	</td></tr>
    	<tr><td>MiRTarBase</td>
    	<td>
    	<g:form name="mirDownload" url="[controller:'Download', action:'gene_download']" style="display: inline" >
        	<g:hiddenField name="fileData" value="${mtGenes}"/>
            <g:hiddenField name="fileName" value="${params.matid}.MirTarBase.txt"/>
            <a href="#" onclick="document.mirDownload.submit()">${mtAll.size()}</a>
    	</g:form>
    	</td>
    	<tr><td>TargetScan</td>
    	<td>
    	<g:form name="tsDownload" url="[controller:'Download', action:'gene_download']" style="display: inline" >
        	<g:hiddenField name="fileData" value="${tsGenes}"/>
            <g:hiddenField name="fileName" value="${params.matid}.TargetScan.txt"/>
            <a href="#" onclick="document.tsDownload.submit()">${tsAll.size()}</a>
    	</g:form>
    	</td></tr>
    	<tr><td>DIANA-microT-CDS</td>
    	<td>
    	<g:form name="diDownload" url="[controller:'Download', action:'gene_download']" style="display: inline" >
        	<g:hiddenField name="fileData" value="${diGenes}"/>
            <g:hiddenField name="fileName" value="${params.matid}.DIANA-microT-CDS.txt"/>
            <a href="#" onclick="document.diDownload.submit()">${diAll.size()}</a>
    	</g:form>
    	</td></tr>
    	<tr><td><b>Union of all</b></td>
    	<td>
    	<g:form name="unionDownload" url="[controller:'Download', action:'gene_download']" style="display: inline" >
        	<g:hiddenField name="fileData" value="${interGenes}"/>
            <g:hiddenField name="fileName" value="${params.matid}.union.txt"/>
            <a href="#" onclick="document.unionDownload.submit()">${unionGeneMap.size()}</a>
    	</g:form>
    	</td></tr>
    	</table>
	</body>
</html>
