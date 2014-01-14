Here are some genes...
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