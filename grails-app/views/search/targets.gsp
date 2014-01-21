<meta name="layout" content="main"/>
<title>MTP | Target Results</title>
<head>
	<script>
	//enrichr data
	var eList = <%=gList%>;
	var eList_split = ""
	for (var i=0;i<eList.length;i++){
		eList_split += eList[i]+"\r"
	} 
	
	function enrich(options) {
		var defaultOptions = {
			description: "",
			popup: false
		};
	
		if (typeof options.description == 'undefined')
			options.description = defaultOptions.description;
		if (typeof options.popup == 'undefined')
			options.popup = defaultOptions.popup;
		if (typeof options.list == 'undefined')
			alert('No genes defined.');
	
		var form = document.createElement('form');
		form.setAttribute('method', 'post');
		form.setAttribute('action', 'http://amp.pharm.mssm.edu/Enrichr/enrich');
		if (options.popup)
			form.setAttribute('target', '_blank');
		form.setAttribute('enctype', 'multipart/form-data');
	
		var listField = document.createElement('input');
		listField.setAttribute('type', 'hidden');
		listField.setAttribute('name', 'list');
		listField.setAttribute('value', options.list);
		form.appendChild(listField);
	
		var descField = document.createElement('input');
		descField.setAttribute('type', 'hidden');
		descField.setAttribute('name', 'description');
		descField.setAttribute('value', options.description);
		form.appendChild(descField);
	
		document.body.appendChild(form);
		form.submit();
		document.body.removeChild(form);
	}		
</script>
</head>