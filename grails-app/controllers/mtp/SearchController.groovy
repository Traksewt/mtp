package mtp

import groovy.sql.Sql
import grails.converters.JSON
import org.xml.sax.InputSource
import groovy.time.*

class SearchController {
	
	javax.sql.DataSource dataSource
	
    def index() { 	
    	def m = ScreenMeta.findAll()
    	return [meta:m]
    }
    
    def test() {  
    	def sql = new Sql(dataSource)
    	def mirList = "'hsa-miR-518c-5p','hsa-miR-299-3p','hsa-miR-10a-5p','hsa-miR-125a-3p','hsa-miR-190b','hsa-miR-515-3p','hsa-miR-199b-5p','hsa-miR-380-3p'"
    	def ndata = [:]
		def nodeMap = [:]
		def nodeList = []
		def linkMap = [:]
		def linkList = []
		def ndataDecode
		def nsql1 = "select mature.*,description from mature left outer join flag on (mature.id = flag.mature_id) where matid in ("+mirList+");";
		print nsql1
		def n1 = sql.rows(nsql1)
		
		n1.each{
			def source = it.matid
			if (!nodeList.name.contains("${source}")){
				nodeMap.name = "${source}"
				nodeMap.type = "miRNA"
				nodeMap.flag = "${it.description}"
				nodeList.add(nodeMap)
				nodeMap = [:] 
			}
		}
		ndata.nodes = nodeList
    	ndata.links = linkList
    	def ndataJSON = ndata as JSON
        ndataDecode = ndataJSON.decodeURL()
		
		print ndataDecode	
    	return [n1:n1, ndata:ndataDecode]
    }
    
    def network_build = {
    	print "Getting network data..."
		def sql = new Sql(dataSource)
		print params.link
		def ndata = [:]
		render(template:"networkUpdateResponse", model: [ndata:ndata])
    }
    
    
    def screen_res(){
    	def t1 = new Date()
    	def sql = new Sql(dataSource)
		print "Searching screen data... "+new Date()
		def screen = ""
		def mirList = []
		def mirListSearch = ""
		def data = params.funCheck
		print "data = "+data
		
		//set the library search
		def lib = ""
		if (params.library != "Either"){
			lib = "and library = '"+params.library+"'"
		}
		
		def v=""
		def d1=""
		def d2=""
		
		def signMap = [:]
		signMap.eq = "="
		signMap.lt = "<"
		signMap.gt = ">"
		
		def noMatch = false
		
		if (params.vValue){
			v = "and veh"+signMap."${params.vSelect}"+params.vValue+" "
			print "v: "+v
		}
		if (params.d1Value){
			d1 = "and d1"+signMap."${params.d1Select}"+params.d1Value+" "
			print "d1: "+d1
		}
		if (params.d2Value){
			d2 = "and d2"+signMap."${params.d2Select}"+params.d2Value+" "
			print "d2: "+d2
		}
		
		if (data instanceof String){
			screen = "("+data+")"
		}else if (data != null){
			//get the mirs found in all data types
			screen = "("
			data.each{
				screen <<= it+","
			}
			screen = screen[0..-2]
			screen <<= ")"
			print "screen = "+screen
			
			def allCheckSql = "select matacc, count(matacc) from screen_data,screen_meta,mature where screen_meta.id in "+screen+" and screen_data.sm_id = screen_meta.id "+v+d1+d2+" and screen_data.mature_id = mature.id "+lib+" group by matacc;";
			print allCheckSql
			def allCheck = sql.rows(allCheckSql)
			print "all size = "+allCheck.size()
			print "fun size = "+data.size()
			if (allCheck.size()>0){
				allCheck.each{
					if (it.count == data.size()){
						mirList.add(it.matacc)
					}
				}
			}
			print "mirList = "+mirList.size()
			if (mirList.size()>0){
				mirListSearch = mirList.collect { "'" + it+ "'" }.join(',')
				mirListSearch = "matacc in ("+mirListSearch+") and "
			}else{
				noMatch = true
			}
			print "mirListSearch = "+mirListSearch
		}else{
			noMatch = true
		}
		
		def s
		def mList = []
		if (noMatch == false){
			def sSql = "select screen_meta.id as meta_id, cell, type, mature.*, screen_data.* from screen_data,screen_meta,mature where "+mirListSearch+" screen_meta.id in "+screen+" and screen_data.sm_id = screen_meta.id "+v+d1+d2+" and screen_data.mature_id = mature.id "+lib+"";
			print sSql
			s = sql.rows(sSql)
			s.each{
				mList.add("${it.matacc}")
			}
			mList = mList.unique()
			session.screenMirs = mList
			
		}else{
			s = "noMatch"
		}
		def t2 = new Date()
        def TimeDuration duration = TimeCategory.minus(t2, t1)
		return [duration:duration, s:s, mList:mList]
	}
    
 	def test2() {  
 		print "test2"
 		print "d = "+session.test  	
    }
    
    def network = {
    	def sql = new Sql(dataSource)
    	print "Creating network... "+new Date()
    	
		def top = params.topNetwork.toInteger()
		
		def commonGeneList = session.commonGeneList.drop( session.commonGeneList.size() - top )
		
		def mList = session.mList.replaceAll(/[\]\[]/,"").replaceAll(/"/,"'")
		def gList = []
		commonGeneList.each{
			gList.add(it.name)
		}
		gList = gList as JSON
		gList = gList.decodeURL()
		print "mList ="+mList
		print "gList ="+gList
		
    	def ndata = [:]
		def nodeMap = [:]
		def nodeList = []
		def linkMap = [:]
		def linkList = []
		def ndataDecode
		//def gList = session.commonGList.drop( session.commonGList.size() - params.top).replaceAll(/[\]\[]/,"").replaceAll(/"/,"'")
		//def mList = session.commonMList.drop( session.commonMList.size() - params.top).replaceAll(/[\]\[]/,"").replaceAll(/"/,"'")
		
		def comsql1 = "select matid,name,count(name) as score from mature,mir2mrna,genes where "+session.targetString+" and matid in ("+mList.replaceAll(/[\]\[]/,"").replaceAll(/"/,"'")+") and name in ("+gList.replaceAll(/[\]\[]/,"").replaceAll(/"/,"'")+") and mature.id = mir2mrna.mature_id and mir2mrna.genes_id = genes.id group by matid,name;"
    	print comsql1
    	def c1 = sql.rows(comsql1)
		c1.each{
			def source = it.matid
			def target = it.name
			def score = it.score
			if (!nodeList.name.contains("${source}")){
				nodeMap.name = "${source}"
				nodeMap.type = "miRNA"
				nodeList.add(nodeMap)
				nodeMap = [:] 
			}
			if (!nodeList.name.contains("${target}")){
				nodeMap.name = "${target}"
				nodeMap.type = "gene"
				nodeList.add(nodeMap)
				nodeMap = [:] 
			}
			//do the links
			def mir_index = nodeList.name.findIndexOf {it == "${source}" }
			def gene_index = nodeList.name.findIndexOf {it == "${target}" }
			linkMap.value = "${score}"
			linkMap.source = mir_index
			linkMap.target = gene_index
			linkMap.name = "mir-gene"
			linkList.add(linkMap)
			linkMap = [:]
		}
		
		def comsql2 = "select matid,tfname from precursor,mature,chipbase_mir where matid in ("+mList.replaceAll(/[\]\[]/,"").replaceAll(/"/,"'")+") and mature.precursor_id = precursor.id and precursor.id = chipbase_mir.pre_id;";
		print comsql2
		def c2 = sql.rows(comsql2)
		c2.each{
			def target = it.matid
			def source = it.tfname
			def score = 0
			if (!nodeList.name.contains("${source}")){
				nodeMap.name = "${source}"
				nodeMap.type = "TF-mir"
				nodeList.add(nodeMap)
				nodeMap = [:] 
			}
			if (!nodeList.name.contains("${target}")){
				nodeMap.name = "${target}"
				nodeMap.type = "miRNA"
				nodeList.add(nodeMap)
				nodeMap = [:] 
			}
			//do the links
			def tf_index = nodeList.name.findIndexOf {it == "${source}" }
			def gene_index = nodeList.name.findIndexOf {it == "${target}" }
			linkMap.value = "${score}"
			linkMap.source = tf_index
			linkMap.target = gene_index
			linkMap.name = "tf-mir"
			linkList.add(linkMap)
			linkMap = [:]
		}
		
		def comsql3 = "select name,tfname from genes,chipbase_gene where name in ("+gList.replaceAll(/[\]\[]/,"").replaceAll(/"/,"'")+") and genes.id = chipbase_gene.genes_id;";
		print comsql3
		def c3 = sql.rows(comsql3)
		c3.each{
			def target = it.name
			def source = it.tfname
			def score = 0
			//only get the tf-gene interactions that are already present in the node list, i.e. TFs that target miRNAs
			if (nodeList.name.contains("${source}")){
				if (nodeList.name.contains("${target}")){
					def tf_index = nodeList.name.findIndexOf {it == "${source}" }
					def gene_index = nodeList.name.findIndexOf {it == "${target}" }
					linkMap.value = 0
					linkMap.source = tf_index
					linkMap.target = gene_index
					linkMap.name = "tf-gene"
					//linkList.add(linkMap)
					linkMap = [:]
				}
			}
		}	
		ndata.nodes = nodeList
		ndata.links = linkList
		def ndataJSON = ndata as JSON
    	ndataDecode = ndataJSON.decodeURL()
		//print ndataDecode
	
    	return [ndata:ndataDecode,commonGeneList:commonGeneList]
    }
    
    def ajaxMirFinder = {
    	def matcher
    	def mirsFound
  	 	if ((matcher = params.term =~ /^M/)){ 
  	 		mirsFound = Mature.withCriteria {
    		ilike 'matacc', params.term + '%'
    		}
 		render (mirsFound?.'matacc' as JSON)
       	}
       	else{ 
  	 		mirsFound = Mature.withCriteria {
    		ilike 'matid', params.term + '%'
    		}
 		render (mirsFound?.'matid' as JSON)
       	}  
	} 
    
    def search_res(){
    	def t1 = new Date()
    	print t1
    	def sql = new Sql(dataSource)
    	def matcher
    	//generate the list of miRs to search
    	def mirList = ""
    	def rank = [:]
    	def p
    	def found = [:]
    	if (params.single){
    		print "single = "+params.single
    		found."${params.single}"=""
    		if ((matcher = params.single =~ /^MIMA/)){ 
    			mirList = "matacc = '"+params.single+"'"
    		}else{
    			mirList = "matid = '"+params.single+"'"
    		}
    	}else if (params.screen == "true"){
    		print "Using list from screen search"
    		//print session.screenMirs
    		session.screenMirs.each{
    			mirList <<= "or mature.matacc = '"+it+"' "
    			found."${it}"=""
    		}
    		mirList = mirList[3..-2]
    		//print "mirList = "+mirList
    		
    	}else{
			def mirFile = params.mirList
			def upload = request.getFile('myFile')
				if (!upload.empty) {
						println "Uploaded file for BLAST"
						println "Class: ${upload.class}"
						println "Name: ${upload.name}"
						println "OriginalFileName: ${upload.originalFilename}"
						println "Size: ${upload.size}"
						println "ContentType: ${upload.contentType}"
						mirFile = upload.inputStream.text
				}
			p = mirFile.replaceAll("\r\n|\n\r|\n|\r",",") 
			def a = p.split(",")
			def starEv = params.sEv
			a.each{
				it = it.trim()
				it = it.replaceAll("\n","")
				def s = it.split(" ")
				found."${s[0]}"=""
				if ((matcher = s[0] =~ /^MIMA/)){
					mirList <<= "or mature.matacc = '"+s[0]+"' "
				}else{
					mirList <<= "or mature.matid = '"+s[0]+"' "
				}
				if (s.size() == 2){
					rank."${s[0]}"=s[1]
				}
			}
			mirList = mirList[3..-2]
		}
		session.mirList = mirList
        def searchSql = "select family.*,precursor.*,mature.* from family,precursor,mature where ("+mirList+") and family.id = precursor.family_id and precursor.id = mature.precursor_id;";
        println searchSql
        def mirRes = sql.rows(searchSql)
        
        def flagsql = "select flag.*,mature.* from flag,mature where ("+mirList+") and flag.mature_id = mature.id;";
        print flagsql
        def flagData = sql.rows(flagsql)
        def flagMap = [:]
        flagData.each{
        	flagMap."${it.matid}"=it.description
        }
        
        
        def famsql = "select famid,count(famid) from mature,precursor,family where ("+mirList+") and mature.precursor_id = precursor.id and precursor.family_id = family.id group by family.famid order by count desc;"
        def famData = sql.rows(famsql)
        def famList = []
        def famCount = []
        famData.each{
        	famList.add(it.famid)
        	famCount.add(it.count)
        }
		def famListJSON = famList as JSON
        def famListDecode = famListJSON.decodeURL()
		
        //get the data for the mir/chromosome plot
        
        //get counts of miRs per chrom
    	def freqSql = "select distinct(count(matid)),chr from mature group by chr order by chr;";
    	def freq = sql.rows(freqSql)
    	def freqMap = [:]
    	freq.each{
    		if ((matcher = it.chr =~ /chr(.*)/)){ 
    			freqMap."${matcher[0][1]}"=it.count
    		}
    	}
        
    	def mirCountSql = "select chr,count(distinct(matid)) from mature where ("+mirList+") group by chr order by count desc;"
    	print mirCountSql
    	def mirCount = sql.rows(mirCountSql)
    	def miRLister = []
    	def miRMap = [:]
    	def chrList = ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","X","Y"]
    	chrList.each{
    		miRMap."${it}" = 0
    	}
    	
    	def chrSize = [249250621,243199373,198022430,191154276,180915260,171115067,159138663,146364022,141213431,135534747,135006516,133851895,115169878,107349540,102531392,90354753,81195210,78077248,59128983,63025520,48129895,51304566,155270560,59373566];
		def chrMap = [:]
		def count=0
		chrList.each{
			chrMap."${it}"=chrSize[count]
			count++
		}
    	
    	mirCount.each{
    		if ((matcher = it.chr =~ /chr(.*)/)){ 
    			//miRMap."${matcher[0][1]}"=it.count/freqMap."${matcher[0][1]}"*100
    			miRMap."${matcher[0][1]}"=it.count / ((freqMap."${matcher[0][1]}" / chrMap."${matcher[0][1]}")*1000000)
    		}
    	}
    	miRMap.each{
    		miRLister.add(it.value)
    	}
    	//print miRList
    	//print miRMap
    	
    	def mirLocSql = "select chr,start from mature where ("+mirList+") order by chr,start;";
    	def mirLoc = sql.rows(mirLocSql)
    	def mirLocJSON = mirLoc as JSON
        def mirLocDecode = mirLocJSON.decodeURL()
    	print "mirLoc = "+mirLocDecode
		
		//get params
		def starParam = ""
		if (params.sEv > 0){
			starParam = "and score > "+params.sEv
			session.starParam = starParam
		}
		/*
		def mirParam = ""
        if (params.mEv == '1'){
        	mirParam = "and mirtarbase.evidence !~ '(Weak)'"
        }
		*/
		def allSql = "select mature.matid,mature.id,source,count(distinct(mir2mrna.genes_id)) from mature,mir2mrna where ("+mirList+") and mir2mrna.mature_id = mature.id and ((mir2mrna.source = 's' ${starParam}) or (mir2mrna.source = 'd') or (mir2mrna.source = 'm') or (mir2mrna.source = 't')) group by matid,source,mature.id;";
		def allRes = sql.rows(allSql) 
		print "allsql = "+allSql
		
        def starMap = [:]
        def mtMap = [:]
        def tsMap = [:]
        def diMap = [:]
        mirRes.each{
        	starMap."${it.matid}" = 0
        	mtMap."${it.matid}" = 0
        	tsMap."${it.matid}" = 0
        	diMap."${it.matid}" = 0
        	if (found."${it.matid}" == ""){
        		found."${it.matid}" = "yes"
        	}
        	if (found."${it.matacc}" == ""){
        		found."${it.matacc}" = "yes"
        	}
        }
        allRes.each{
        	if (it.source == 's'){ starMap."${it.matid}" = it.count}
        	if (it.source == 't'){ tsMap."${it.matid}" = it.count}
        	if (it.source == 'm'){ mtMap."${it.matid}" = it.count}
        	if (it.source == 'd'){ diMap."${it.matid}" = it.count}
        	
        }
        
        def missing = []
        found.each{
        	if (it.value != "yes"){
        		missing.add(it.key)
        	}
        }
        print "found = "+found
        print "missing = "+missing
        
        print miRLister
        print mirLoc
        
        def t2 = new Date()
        def TimeDuration duration = TimeCategory.minus(t2, t1)
        
        return [duration:duration,missing:missing, found:found, miRLister:miRLister, mirLoc:mirLocDecode, flagMap:flagMap, rank:rank, famList:famListDecode, famCount:famCount, mirRes: mirRes, starMap: starMap, mtMap: mtMap, tsMap:tsMap, diMap: diMap, sEv:params.sEv, mEv: params.mEv]    
    }
    
	def targets(){
		def top = 50
		def t1 = new Date()
		def sql = new Sql(dataSource)
		def mirList = session.mirList
		def starParam = session.starParam
		print "starParam = "+starParam
		def count = 0
		def targetList = params.targetDB.toString().replaceAll(/[\]\[]/,"").split(",")
		print "targets = "+targetList
		def targetString = ""
		targetList.each{
			if (it.trim() == 's'){
				targetString <<= " or (source = '"+it.trim()+"' "+starParam+")" 
			}else{
				targetString <<= " or source = '"+it.trim()+"'"
			}
		}
		targetString = "("+targetString[4..-1]+")"
		session.targetString = targetString
		print "targetString = "+targetString
		
		
		
		//target data
		def tsql = "select source,ensembl,uniprot,matid,score,genes.start,name,fullname,genes.id,famid,genes.chr,genes.start,genes.stop from family,precursor,mature,mir2mrna,genes where "+targetString+" and family.id = precursor.family_id and precursor.id = mature.precursor_id and mature.id = mir2mrna.mature_id and genes.id = mir2mrna.genes_id and ("+mirList+") order by genes.id;";
		print tsql
		def tData = sql.rows(tsql)
		
		session.tData = tData
		
		def dMap = []
		def dList = [:]
		def mMap = [:]
		def mList = []
		def famMap = [:]
		def famHeatList = []
		def fData = []
		def gList = []
		def frData = []
		def countGenes = [:]
		def countGeneScore = [:]
		def commonGenes = [:]
		def commonGeneList = []
		def geneNames = [:]
		def geneLoc = [:]
		//to get miRs with no targets too use mirRes.each and for just those with targets use heat.each
		//mirRes.each{
	
		tData.each{
			mMap."${it.matid}"=0
			famMap."${it.matid}"=it.famid
			geneNames."${it.name}"=[it.fullname,it.ensembl,it.uniprot]
			geneLoc."${it.name}"=it.chr+":"+it.start+"-"+it.stop
			
			if (countGenes."${it.name}"){
				//add the mir counts
				def m = countGenes."${it.name}"
				if(!m.contains("${it.matid}")){
					m.add(it.matid)
				}
				countGenes."${it.name}" = m
				
				//add the target counts
				def t = countGeneScore."${it.name}"
				if (t."${it.source}" > 0) {
					t."${it.source}" = t."${it.source}" + 1
				}else{
					t."${it.source}" = 1
				}
				countGeneScore."${it.name}" = t
			}else{
				countGeneScore."${it.name}" = ["s":0,"t":0,"d":0,"m":0]
				countGenes."${it.name}" = [it.matid]
				def t = countGeneScore."${it.name}"
				t."${it.source}" = 1
				countGeneScore."${it.name}" = t
			}
		}
		
		countGenes.each{
			commonGenes.name = it.key
			commonGenes.count = it.value
			commonGenes.fullname = geneNames."${it.key}"[0]
			commonGenes.ensembl = geneNames."${it.key}"[1]
			commonGenes.uniprot = geneNames."${it.key}"[2]
			commonGenes.location = geneLoc."${it.key}"
			commonGenes.countScore = countGeneScore."${it.key}"
			//print "commonGenes = "+commonGenes
			commonGeneList.add(commonGenes)
			commonGenes = [:]
		}
		
		//add list of mirs to session
		mMap.each{
			mList.add(it.key)
		}
		mList = mList as JSON
		session.mList = mList.decodeURL()
		
		session.commonGeneList = commonGeneList.sort{it.count.size()}
		def c = commonGeneList as JSON
		session.commonGeneListJSON = c.decodeURL()
		
		def t2 = new Date()
		def TimeDuration duration = TimeCategory.minus(t2, t1)
		
		return [duration:duration,  commonGeneList:commonGeneList,]
	}
	
	def heatmap(){
		def count
		def dMap = []
		def dList = [:]
		def mMap = [:]
		def mList = []
		def famMap = [:]
		def famHeatList = []
		def fData = []
		def gList = []
		def frData = []
		def countGenes = [:]
		def countGeneScore = [:]
		def commonGenes = [:]
		def commonGeneList = []
		def geneNames = [:]
		def geneLoc = [:]
		//to get miRs with no targets too use mirRes.each and for just those with targets use heat.each
		//mirRes.each{
		
		def top = params.topHeatmap.toInteger()
		def heat = session.tData
		
		heat.each{
			mMap."${it.matid}"=0
			famMap."${it.matid}"=it.famid
		}
		
		if (mMap.size()>1){
			//generate family data
			famMap.each{
				famHeatList.add(it.value)
			}
			famHeatList = famHeatList as JSON
			famHeatList = famHeatList.decodeURL()
			print "famHeatList = "+famHeatList
			
			//generate heatmap numbers
			mMap.each{
				mList.add(it.key)
			}
			//print "mMap = "+mMap
			def mMapReset = mMap;
			def old_id = ""
			def old_name = ""
			def new_id = ""
			def data = []
			def sumData = [:]
			heat.each{
				new_id = it.id
				if (old_id != "" && it.id != old_id){
					//print mMap
					data = []
					mMap.each{
						data.add(it.value)
					}
					sumData."${old_name}" = data.sum()
					dList."${old_name}" = data
					//reset the map
					mMap = [:]
					mMapReset.each{
						mMap."${it.key}"=0
					}
				}
				mMap."${it.matid}"= mMap."${it.matid}" + 1
				old_name = it.name
				old_id = it.id
			}
			//catch the last ones
			mMap.each{
				data.add(it.value)
			}
			dList."${old_name}" = data
			//print "sumData = "+sumData
			//sort and generate lists
			def sortData = sumData.sort{it.value}.drop( sumData.size() - top )
			print "top = "+sortData
			count = 0
		
			sortData.each{
				//print it.key + " - "+it.value + ": " +dList."${it.key}"
				gList.add(it.key)
				fData.add(dList."${it.key}")
			}
			//transorm the matrix into lists by column (miR)
			def tmp = []
			print "size = "+mList.size()
			for (int i = 0; i < mList.size(); i++) {
				fData.each{
					tmp.add(it[i])
				}
				frData.add(tmp)
				tmp = []
			}
		}
		print "heatmap data = "+frData
		gList = gList as JSON
		gList = gList.decodeURL()
		print "heatmap y = "+gList
		
		mList = mList as JSON
		mList = mList.decodeURL()
		print "heatmap x = "+mList

		commonGeneList = session.commonGeneList.drop( session.commonGeneList.size() - top )
		
		//add things to the session
		session.commonMListJSON = mList
		session.commonGListJSON = gList
		
		return [famHeatList:famHeatList, commonGeneList:commonGeneList, fData:frData, mList:mList, gList:gList,]

	}
	
    def genes(){
	    def sql = new Sql(dataSource)
	    def matcher
		def miRsql = "select * from mature where id = "+(params.matid)+";";
		def miR = sql.rows(miRsql)
		def miRJSON = miR as JSON
		def miRDecode = miRJSON.decodeURL()
		
		def mirData = Mature.findById(params.matid)
		
		def unionGeneMap = [:]
		def chrList = ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","X","Y"]
		
		//get params
		def starParam = ""
		print "sEv = "+params.sEv
		if (params.sEv){
			starParam = "and score > "+params.sEv
		}
		/*
		def mirParam = ""
        if (params.mEv == '1'){
        	mirParam = "and mirtarbase.evidence !~ '(Weak)'"
        }
        */
        //get the gene data
        //def allSql = "select mature.matid,source,count(distinct(mir2mrna.genes_id)) from mature,mir2mrna where ("+mirList+") and mir2mrna.mature_id = mature.id and ((mir2mrna.source = 's' ${starParam}) or (mir2mrna.source = 'd') or (mir2mrna.source = 'm' ${mirParam}) or (mir2mrna.source = 't')) group by matid,source;";
		//select genes.*,score,source from genes,mature,mir2mrna where mature.matid ~ 'hsa' and mature.id = mir2mrna.mature_id and mir2mrna.genes_id = genes.id and ((mir2mrna.source = 's' and score > 2 ) or (mir2mrna.source = 'd' and score > 0.9));
		def AllSql = "select genes.*,source,name from mir2mrna,genes where mir2mrna.mature_id = '"+params.matid+"' and mir2mrna.genes_id = genes.id and ((mir2mrna.source = 's' ${starParam}) or (mir2mrna.source = 'd') or (mir2mrna.source = 'm') or (mir2mrna.source = 't')) order by genes.id;"
		print AllSql
		def All = sql.rows(AllSql)
		def sGenes = []
		def mGenes = []
		def tGenes = []
		def dGenes = []
		def sJ = []
		def tJ = []
		def dJ = []
		def mJ = []
		def jMap = [:]
		def tMap = [:]
		def tData = []
		tMap.s = 0
		tMap.m = 0
		tMap.t = 0
		tMap.d = 0
		def old_id = ""
		All.each{
			//print "id = "+it.id
			//print "old_id = "+old_id
			if (it.id != old_id && old_id != ""){
				tData.add(tMap)
				//print "tMap = "+tMap 
				tMap = [:]
				tMap.s = 0
				tMap.m = 0
				tMap.t = 0
				tMap.d = 0
			}
			tMap.chr = it.chr
			tMap.name = it.name
			tMap.start = it.start
			tMap.stop = it.stop
			jMap.chr = it.chr
			jMap.start = it.start
			jMap.source = it.source
			unionGeneMap."${it.name}"=""
			if (it.source == 's'){ 
				tMap.s = 1
				sGenes.add(it.name)
				sJ.add(jMap)
			}
			if (it.source == 'm'){ 
				tMap.m = 1
				mGenes.add(it.name)
				mJ.add(jMap)	
			}
			if (it.source == 't'){ 
				tMap.t = 1
				tGenes.add(it.name)
				tJ.add(jMap)		
			}
			if (it.source == 'd'){ 
				tMap.d = 1
				dGenes.add(it.name)
				dJ.add(jMap)
			}
			//print "tMap2 = "+tMap 
			old_id = it.id
			jMap = [:]
		}
		//catch the last one
		tData.add(tMap)
		
		def sJSON = sJ as JSON
        def sDecode = sJSON.decodeURL()
        def tJSON = tJ as JSON
        def tDecode = tJSON.decodeURL()
        def mJSON = mJ as JSON
        def mDecode = mJSON.decodeURL()
        def dJSON = dJ as JSON
        def dDecode = dJSON.decodeURL()
        
		def sCount = sGenes.unique()
		sGenes = sCount.join(", ")
		def mCount = mGenes.unique()
		mGenes = mCount.join(", ")
		def tCount = tGenes.unique()
		tGenes = tCount.join(", ")
		def dCount = dGenes.unique()
		dGenes = dCount.join(", ")
		
		
		// get the count data
		
		def tM = [:]
		def dM = [:]
		def sM = [:]
		def mM = [:]
		def tList = []
		def dList = []
		def sList = []
		def mList = []
		chrList.each{
    		tM."${it}" = 0
    		dM."${it}" = 0
    		sM."${it}" = 0
    		mM."${it}" = 0
    	}
    	//generate chromosome length map
    	def chrSize = [249250621,243199373,198022430,191154276,180915260,171115067,159138663,146364022,141213431,135534747,135006516,133851895,115169878,107349540,102531392,90354753,81195210,78077248,59128983,63025520,48129895,51304566,155270560,59373566];
		def chrMap = [:]
		def count=0
		chrList.each{
			chrMap."${it}"=chrSize[count]
			count++
		}
		print "chrMap = "+chrMap
		
		//generate genes per chromosome map
		def freqsql = "select count(name),chr from genes group by chr order by chr;";
		def freq = sql.rows(freqsql)
		def freqMap = [:]
    	freq.each{
    		if ((matcher = it.chr =~ /chr(.*)/)){ 
    			freqMap."${matcher[0][1]}"=it.count
    		}
    	}
    	//make map of genes per chromosome per Megabase
    	def gpm = [:]
		freqMap.each{
			if (chrMap."${it.key}" != 'NULL' && it.key != 'M'){
				gpm."${it.key}"= (it.value / chrMap."${it.key}".toInteger()) * 1000000
				//print "chr = "+it.key+" count = "+it.value+" size = "+chrMap."${it.key}"+" gpm = "+(it.value/chrMap."${it.key}".toInteger())*1000000
			}
		}
		def cSql = "select genes.chr,count(name),source from mir2mrna,genes where mir2mrna.mature_id = '"+params.matid+"' and mir2mrna.genes_id = genes.id and ((mir2mrna.source = 's' ${starParam}) or (mir2mrna.source = 'd') or (mir2mrna.source = 'm') or (mir2mrna.source = 't')) group by genes.chr,source order by chr;";
		print cSql
		def c = sql.rows(cSql)
		c.each{
			if ((matcher = it.chr =~ /chr(.*)/)){ 
				/*
				if (it.source == 's'){sM."${matcher[0][1]}"=(it.count/freqMap."${matcher[0][1]}")*100}
				if (it.source == 't'){tM."${matcher[0][1]}"=(it.count/freqMap."${matcher[0][1]}")*100}
				if (it.source == 'd'){dM."${matcher[0][1]}"=(it.count/freqMap."${matcher[0][1]}")*100}
				if (it.source == 'm'){mM."${matcher[0][1]}"=(it.count/freqMap."${matcher[0][1]}")*100}
				*/
				print it.source+": ${matcher[0][1]} - "+ it.count +" - "+gpm."${matcher[0][1]}" + " -> "+ it.count / gpm."${matcher[0][1]}"
				if (it.source == 's'){sM."${matcher[0][1]}"=it.count / gpm."${matcher[0][1]}"}
				if (it.source == 't'){tM."${matcher[0][1]}"=it.count / gpm."${matcher[0][1]}"}
				if (it.source == 'd'){dM."${matcher[0][1]}"=it.count / gpm."${matcher[0][1]}"}
				if (it.source == 'm'){mM."${matcher[0][1]}"=it.count / gpm."${matcher[0][1]}"}
			}
		}
		sM.each{sList.add(it.value)}
		tM.each{tList.add(it.value)}
		print "tM = "+tM
		dM.each{dList.add(it.value)}
		mM.each{mList.add(it.value)}
		
		//generate list of all count data for scaling
		def allCounts = []
		allCounts.add(sList)
		allCounts.add(mList)
		allCounts.add(dList)
		allCounts.add(tList)
		allCounts = allCounts.flatten()
		print "allCounts = "+allCounts
		
		//generate union data
		def union = ""
		unionGeneMap.each{
			union <<= it.key+","
		}
		if (union.size()>0){
			union = union[0..-2]
		}

		//should be four variables for each gene set - Genes, Count, Decode and List
		print "Genes = "+tGenes
		print "Count = "+tCount
		print "Decode = "+tDecode
		print "List = "+tList
		
		return [mirData:mirData, tData:tData, allCounts:allCounts, miR:miRDecode, sCount:sCount, sDecode:sDecode, sGenes:sGenes, sList:sList, mCount:mCount, mDecode:mDecode, mList:mList, mGenes:mGenes, tCount:tCount, tDecode:tDecode, tList:tList, tGenes:tGenes, dCount:dCount, dDecode:dDecode, dList:dList, dGenes:dGenes, unionGenes:union, unionGeneMap: unionGeneMap]
	}
}
