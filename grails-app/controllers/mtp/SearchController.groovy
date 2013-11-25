package mtp

import groovy.sql.Sql
import grails.converters.JSON
import org.xml.sax.InputSource

class SearchController {
	
	javax.sql.DataSource dataSource
	
    def index() { }
    
    def test() { 
    	def sql = new Sql(dataSource)
    	def matcher
    	
    	//get counts of miRs per chrom
    	def freqSql = "select distinct(count(matid)),chr from mature group by chr order by chr;";
    	def freq = sql.rows(freqSql)
    	def freqMap = [:]
    	freq.each{
    		if ((matcher = it.chr =~ /chr(.*)/)){ 
    			freqMap."${matcher[0][1]}"=it.count
    		}
    	}
    	
    	def mirCountSql = "select chr,count(distinct(matid)) from mature where (mature.matid = 'hsa-miR-4314' or mature.matid = 'hsa-miR-1294' or mature.matid = 'hsa-miR-552' or mature.matid = 'hsa-miR-4297' or mature.matid = 'hsa-miR-550a-3p' or mature.matid = 'hsa-miR-432-3p' or mature.matid = 'hsa-miR-193b-3p' or mature.matid = 'hsa-miR-342-5p' or mature.matid = 'hsa-miR-541-3p' or mature.matid = 'hsa-miR-193a-3p' or mature.matid = 'hsa-miR-489' or mature.matid = 'hsa-miR-3192' or mature.matid = 'hsa-miR-892b' or mature.matid = 'hsa-miR-148b-5p' or mature.matid = 'hsa-miR-3140-3p' or mature.matid = 'hsa-miR-654-5p' or mature.matid = 'hsa-miR-876-3p' or mature.matid = 'hsa-miR-3160-3p' or mature.matid = 'hsa-miR-3189-3p' or mature.matid = 'hsa-miR-1289' or mature.matid = 'hsa-miR-19b-1-5p' or mature.matid = 'hsa-miR-1293' or mature.matid = 'hsa-miR-634' or mature.matid = 'hsa-miR-3165' or mature.matid = 'hsa-miR-323a-5p' or mature.matid = 'hsa-miR-1285-3p') group by chr order by count desc;"
    	print mirCountSql
    	def mirCount = sql.rows(mirCountSql)
    	def miRListRel = []
    	def miRList = []
    	def miRMapRel = [:]
    	def miRMap = [:]
    	def chrList = ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","X","Y"]
    	chrList.each{
    		miRMap."${it}" = 0
    		miRMapRel."${it}" = 0
    	}
    	mirCount.each{
    		if ((matcher = it.chr =~ /chr(.*)/)){ 
    			miRMap."${matcher[0][1]}"=it.count
    			miRMapRel."${matcher[0][1]}"=it.count/freqMap."${matcher[0][1]}"*100
    		}
    	}
    	miRMap.each{
    		miRList.add(it.value)
    	}
    	miRMapRel.each{
    		miRListRel.add(it.value)
    	}
    	//print miRList
    	//print miRMap
    	
    	def mirLocSql = "select chr,start from mature where (mature.matid = 'hsa-miR-4314' or mature.matid = 'hsa-miR-1294' or mature.matid = 'hsa-miR-552' or mature.matid = 'hsa-miR-4297' or mature.matid = 'hsa-miR-550a-3p' or mature.matid = 'hsa-miR-432-3p' or mature.matid = 'hsa-miR-193b-3p' or mature.matid = 'hsa-miR-342-5p' or mature.matid = 'hsa-miR-541-3p' or mature.matid = 'hsa-miR-193a-3p' or mature.matid = 'hsa-miR-489' or mature.matid = 'hsa-miR-3192' or mature.matid = 'hsa-miR-892b' or mature.matid = 'hsa-miR-148b-5p' or mature.matid = 'hsa-miR-3140-3p' or mature.matid = 'hsa-miR-654-5p' or mature.matid = 'hsa-miR-876-3p' or mature.matid = 'hsa-miR-3160-3p' or mature.matid = 'hsa-miR-3189-3p' or mature.matid = 'hsa-miR-1289' or mature.matid = 'hsa-miR-19b-1-5p' or mature.matid = 'hsa-miR-1293' or mature.matid = 'hsa-miR-634' or mature.matid = 'hsa-miR-3165' or mature.matid = 'hsa-miR-323a-5p' or mature.matid = 'hsa-miR-1285-3p') group by chr,start;";
    	print mirLocSql
    	def mirLoc = sql.rows(mirLocSql)
    	def mirLocJSON = mirLoc as JSON
        def mirLocDecode = mirLocJSON.decodeURL()
    	//print "mirLoc = "+mirLocDecode
    	
    	def heatsql = "select matid,score,genes.start,name,genes.id from mature,mir2mrna,genes where mature.id = mir2mrna.mature_id and genes.id = mir2mrna.genes_id and (mature.matid = 'hsa-miR-551a' or mature.matid = '' or mature.matid = 'hsa-miR-34a-5p' or mature.matid = '' or mature.matid = 'hsa-miR-200a-5p' or mature.matid = '' or mature.matid = 'hsa-miR-200b-3p' or mature.matid = '' or mature.matid = 'hsa-miR-429' or mature.matid = '' or mature.matid = 'hsa-miR-200a-3p') and source = 'd' order by genes.id;";
    	print heatsql
    	def heat = sql.rows(heatsql)
    	def dMap = [:]
    	def mMap = [:]
    	def mList = []
    	heat.each{
    		mMap."${it.matid}"=0
    	}
    	mMap.each{
    		mList.add(it.key)
    	}
    	def mMapReset = mMap;
    	def old_name = ""
    	heat.each{
    		if (old_name != "" && it.name != old_name){
    			//print mMap
    			dMap."${it.name}"=mMap
    			//reset the map
    			mMap = [:]
    			mMapReset.each{
    				mMap."${it.key}"=0
    			}
    		}
    		mMap."${it.matid}"=it.score
    		old_name = it.id
    	}
		//print dMap
		def s = "gene\t"+mList.join("\t")
		new File("tscan_counts.txt").withWriter { out ->
			out.writeLine("${s}")
			dMap.each{
				//print 
				def scores = it.value
				def scoreList = mList.collect{scores[it]}
				s = it.key+"\t"+scoreList.join("\t")
				out.writeLine("${s}")
			}
		}
		
    	return [miRList:miRList, miRListRel:miRListRel, mirLoc:mirLocDecode]
    }
    
    def search_res(){
    	def sql = new Sql(dataSource)
    	def matcher
    	//generate the list of miRs to search
    	def mirList = "("
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
    	def p = mirFile.replaceAll("\r\n|\n\r|\n|\r",",") 
    	def a = p.split(",")
    	def starEv = params.sEv
    	def rank = [:]
    	a.each{
    		it = it.trim()
    		it = it.replaceAll("\n","")
    		def s = it.split(" ")
    		mirList <<= "or mature.matid = '"+s[0]+"' "
    		if (s.size() == 2){
    			rank."${s[0]}"=s[1]
    		}
    	}
    	mirList = mirList[4..-2]
    	    	
        def searchSql = "select family.*,precursor.*,mature.* from family,precursor,mature where ("+mirList+") and family.id = precursor.family_id and precursor.id = mature.precursor_id;";
        //println searchSql
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
    	mirCount.each{
    		if ((matcher = it.chr =~ /chr(.*)/)){ 
    			miRMap."${matcher[0][1]}"=it.count/freqMap."${matcher[0][1]}"*100
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
		
		//new search
		//select genes.*,score,source from genes,mature,mir2mrna where mature.matid ~ 'hsa' and mature.id = mir2mrna.mature_id and mir2mrna.genes_id = genes.id and ((mir2mrna.source = 's' and score > 2 ) or (mir2mrna.source = 'd' and score > 0.9));
		
		//get params
		def starParam = ""
		if (params.sEv > 0){
			starParam = "and score > "+params.sEv
		}
		def mirParam = ""
        if (params.mEv == '1'){
        	mirParam = "and mirtarbase.evidence !~ '(Weak)'"
        }
		
		def allSql = "select mature.matid,mature.id,source,count(distinct(mir2mrna.genes_id)) from mature,mir2mrna where ("+mirList+") and mir2mrna.mature_id = mature.id and ((mir2mrna.source = 's' ${starParam}) or (mir2mrna.source = 'd') or (mir2mrna.source = 'm' ${mirParam}) or (mir2mrna.source = 't')) group by matid,source,mature.id;";
		def allRes = sql.rows(allSql) 
		print allSql
		
        def starMap = [:]
        def mtMap = [:]
        def tsMap = [:]
        def diMap = [:]
        mirRes.each{
        	starMap."${it.matid}" = 0
        	mtMap."${it.matid}" = 0
        	tsMap."${it.matid}" = 0
        	diMap."${it.matid}" = 0
        }
        allRes.each{
        	if (it.source == 's'){ starMap."${it.matid}" = it.count}
        	if (it.source == 't'){ tsMap."${it.matid}" = it.count}
        	if (it.source == 'm'){ mtMap."${it.matid}" = it.count}
        	if (it.source == 'd'){ diMap."${it.matid}" = it.count}
        	
        }
        
        //heatmap data
        def heatsql = "select matid,score,genes.start,name,genes.id from mature,mir2mrna,genes where mature.id = mir2mrna.mature_id and genes.id = mir2mrna.genes_id and ("+mirList+") and mir2mrna.mature_id = mature.id and ((mir2mrna.source = 's' ${starParam}) or (mir2mrna.source = 'd') or (mir2mrna.source = 'm' ${mirParam}) or (mir2mrna.source = 't')) and source = 't' order by genes.id;";
    	print heatsql
    	def heat = sql.rows(heatsql)
    	def dMap = [:]
    	def mMap = [:]
    	def mList = []
    	mirRes.each{
    	//heat.each{
    		mMap."${it.matid}"=0
    	}
    	print "mMap = "+mMap
    	mMap.each{
    		mList.add(it.key)
    	}
    	def mMapReset = mMap;
    	def old_name = ""
    	def new_name = ""
    	heat.each{
    		new_name = it.name
    		if (old_name != "" && it.id != old_name){
    			//print mMap
    			dMap."${new_name}"=mMap
    			//reset the map
    			mMap = [:]
    			mMapReset.each{
    				mMap."${it.key}"=0
    			}
    		}
    		mMap."${it.matid}"=it.score
    		old_name = it.id
    	}
    	//catch the last one
    	dMap."${new_name}"=mMap
    	
		//print dMap
		def s = "gene\t"+mList.join("\t")
		new File("heatmap_counts.txt").withWriter { out ->
			out.writeLine("${s}")
			dMap.each{
				//print 
				def scores = it.value
				def scoreList = mList.collect{scores[it]}
				s = it.key+"\t"+scoreList.join("\t")
				out.writeLine("${s}")
			}
		}
        
        print miRLister
        print mirLoc
        return [miRLister:miRLister, mirLoc:mirLocDecode, flagMap:flagMap, rank:rank, famList:famListDecode, famCount:famCount, mirList: p, mirRes: mirRes, starMap: starMap, mtMap: mtMap, tsMap:tsMap, diMap: diMap, sEv:params.sEv, mEv: params.mEv]    
    }
    
    def genes(){
	    def sql = new Sql(dataSource)
	    def matcher
		def miRsql = "select * from mature where id = "+(params.matid)+";";
		def miR = sql.rows(miRsql)
		def miRJSON = miR as JSON
		def miRDecode = miRJSON.decodeURL()
		def unionGeneMap = [:]
		def chrList = ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","X","Y"]
		
		//get params
		def starParam = ""
		if (params.sEv > 0){
			starParam = "and score > "+params.sEv
		}
		def mirParam = ""
        if (params.mEv == '1'){
        	mirParam = "and mirtarbase.evidence !~ '(Weak)'"
        }
        
        //get the gene data
        //def allSql = "select mature.matid,source,count(distinct(mir2mrna.genes_id)) from mature,mir2mrna where ("+mirList+") and mir2mrna.mature_id = mature.id and ((mir2mrna.source = 's' ${starParam}) or (mir2mrna.source = 'd') or (mir2mrna.source = 'm' ${mirParam}) or (mir2mrna.source = 't')) group by matid,source;";
		//select genes.*,score,source from genes,mature,mir2mrna where mature.matid ~ 'hsa' and mature.id = mir2mrna.mature_id and mir2mrna.genes_id = genes.id and ((mir2mrna.source = 's' and score > 2 ) or (mir2mrna.source = 'd' and score > 0.9));
		def AllSql = "select genes.*,source,name from mir2mrna,genes where mir2mrna.mature_id = '"+params.matid+"' and mir2mrna.genes_id = genes.id and ((mir2mrna.source = 's' ${starParam}) or (mir2mrna.source = 'd') or (mir2mrna.source = 'm' ${mirParam}) or (mir2mrna.source = 't')) order by genes.id;"
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
		def cSql = "select genes.chr,count(name),source from mir2mrna,genes where mir2mrna.mature_id = '"+params.matid+"' and mir2mrna.genes_id = genes.id and ((mir2mrna.source = 's' ${starParam}) or (mir2mrna.source = 'd') or (mir2mrna.source = 'm' ${mirParam}) or (mir2mrna.source = 't')) group by genes.chr,source order by chr;";
		print cSql
		def c = sql.rows(cSql)
		c.each{
			if ((matcher = it.chr =~ /chr(.*)/)){ 
				if (it.source == 's'){sM."${matcher[0][1]}"=it.count}
				if (it.source == 't'){tM."${matcher[0][1]}"=it.count}
				if (it.source == 'd'){dM."${matcher[0][1]}"=it.count}
				if (it.source == 'm'){mM."${matcher[0][1]}"=it.count}
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
		return [tData:tData, allCounts:allCounts, miR:miRDecode, sCount:sCount, sDecode:sDecode, sGenes:sGenes, sList:sList, mCount:mCount, mDecode:mDecode, mList:mList, mGenes:mGenes, tCount:tCount, tDecode:tDecode, tList:tList, tGenes:tGenes, dCount:dCount, dDecode:dDecode, dList:dList, dGenes:dGenes, unionGenes:union, unionGeneMap: unionGeneMap]
	}
}
