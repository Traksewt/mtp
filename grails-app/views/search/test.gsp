<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>MTP | Test</title>
		<script src="${resource(dir: 'js', file: 'DataTables-1.9.4/media/js/jquery.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js', file: 'canvasxpress/build/js/canvasXpress.min.js')}" type="text/javascript"></script>

		<script id='demoScript'>
	 $(document).ready(function() {
	 	console.log(<%=mList%>)
        console.log(<%=gList%>)
        console.log(<%=fData%>)
		var cx2 = new CanvasXpress('canvas1',
          {
            'z': {
            	'Family': <%=famList%>
              //'Description': ['uncharacterized LOC645722', 'DSCAM antisense RNA 1', 'heat shock 70kDa protein 1A', 'plastin 3', 'epithelial membrane protein 1', 'calponin 3, acidic', 'serglycin', 'transforming growth factor, beta receptor II (70/80kDa)', 'anterior gradient 2 homolog (Xenopus laevis)', 'tumor protein D52-like 1', 'collagen, type XIII, alpha 1', 'plasminogen activator, urokinase', 'olfactomedin 1', 'BTG family, member 3', 'tissue factor pathway inhibitor (lipoprotein-associated coagulation inhibitor)', 'v-ets erythroblastosis virus E26 oncogene homolog 1 (avian)', 'branched chain amino-acid transaminase 1, cytosolic', 'v-erb-b2 erythroblastic leukemia viral oncogene homolog 3 (avian)', 'thiosulfate sulfurtransferase (rhodanese)-like domain containing 1']
            },
            'x': {
              //'Type': ['control', 'control', 'control', 'estrogen receptor knockdown', 'estrogen receptor knockdown', 'estrogen receptor knockdown']
            },
            'y': {
              /*
              'vars': ['1555216_a_at', '1562821_a_at', '200799_at', '201215_at', '201324_at', '201445_at', '201859_at', '208944_at', '209173_at', '210372_s_at', '211343_s_at', '211668_s_at', '213131_at', '213134_x_at', '213258_at', '224833_at', '225285_at', '226213_at', '226482_s_at'],
              'smps': ['GSM678802', 'GSM678803', 'GSM678804', 'GSM678805', 'GSM678806', 'GSM678807'],
              'data': [[13.4443, 13.4052, 13.4372, 4.55083, 4.83841, 4.5995],
                       [13.1104, 13.0618, 13.1576, 4.73436, 5.00194, 4.71429],
                       [4.274, 4.24537, 5.36694, 13.2043, 13.1975, 13.1963],
                       [4.34309, 4.57643, 4.97614, 12.4432, 12.4042, 12.4081],
                       [4.69344, 4.56319, 5.27746, 12.1551, 11.7577, 11.9331],
                       [4.29599, 4.39105, 6.18367, 12.8764, 12.953, 12.9437],
                       [4.74912, 4.67893, 5.06339, 11.9744, 11.8, 11.795],
                       [12.5737, 12.5086, 12.5188, 4.67011, 4.98324, 4.72287],
                       [11.6008, 11.7211, 11.648, 4.82716, 4.6292, 4.60583],
                       [4.50014, 4.66061, 5.06175, 11.7515, 11.8144, 11.8301],
                       [4.60574, 4.53201, 4.82074, 12.1995, 12.2452, 12.1187],
                       [11.2839, 11.3428, 11.2473, 4.67248, 4.70424, 4.85937],
                       [4.74606, 4.54343, 4.88302, 11.8511, 11.7496, 11.7863],
                       [4.73078, 4.66955, 4.81833, 11.4661, 11.1592, 11.2315],
                       [4.84254, 4.75658, 5.07002, 11.7535, 11.7758, 11.6974],
                       [4.55831, 4.49289, 4.67046, 12.35, 12.0875, 12.0381],
                       [11.5252, 11.4375, 11.4535, 4.65772, 4.60565, 4.75315],
                       [10.9772, 10.9839, 11.0658, 4.7015, 4.56169, 4.66754],
                       [4.71039, 4.7602, 4.89837, 11.2719, 10.9061, 10.8996]],
              */
              'vars': <%=mList%>,
              'smps': <%=gList%>,
              'data': <%=fData%>,        
              'desc': ['Score']
            },
       
          },
          {'graphType': 'Heatmap',
          'heatmapType':'red',
          'indicatorCenter':'rainbow-green',
          'smpOverlays': ['Type'],
          'title': 'Gene target predictions for each miRNA',
          'varLabelRotate': 45,
          'varOverlays': ['Family']}
        );
        cx2.clusterSamples();
        cx2.clusterVariables();


	});
	
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
	<body>
	<a onclick="enrich({list: eList_split, description: 'sfsfdsf', popup: true})" href="javascript:void(0);">click</a>
	<canvas id='canvas1' width='1000' height='650'></canvas>


	</body>
</html>
