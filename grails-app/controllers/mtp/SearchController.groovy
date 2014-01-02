package mtp

import groovy.sql.Sql
import grails.converters.JSON
import org.xml.sax.InputSource

class SearchController {
	
	javax.sql.DataSource dataSource
	
    def index() { 
    }
    
    def network = {
    	def sql = new Sql(dataSource)
    	print "Creating network..."
    	def mirs = params.mirs
    	mirs = mirs.replaceAll(/[\]\[]/,"");
    	mirs = mirs.replaceAll(/"/,"");
    	print "mirs = "+mirs.getClass()
    	def mirs2 = mirs.split(",")
    	def mirs3 = []
    	
    	def mirString = ""
    	mirs2.each{
    		print "each = "+it
    		mirs3.add(it)
    		mirString <<= " or mature.matid = '"+it+"'"
    	}
    	mirString = mirString[4..-1]
    	print "mirString = "+mirString
    	
    	print "mirs2 = "+mirs2.getClass()
		print "mirs3 = "+mirs3.getClass()
		print "mirs3 = "+mirs3
		
    	//one sql command - too much!
    	//def nsql = "select genes.name,matid,chipbase_gene.tfname as gene_tfname,chipbase_mir.tfname as mir_tfname from mature,mir2mrna,genes,chipbase_gene,chipbase_mir where ("+mirString+") and source = 's' and mir2mrna.genes_id = genes.id and mir2mrna.mature_id = mature.id and chipbase_gene.genes_id = genes.id and chipbase_mir.pre_id = mature.precursor_id order by name;";
    	//print nsql
    	
    	def ndata = []
    	def nmap = [:]
    	mirs2.each{
    		//get the genes and tf-gene data
    		print "Getting network data for "+it
    		//def nsql = "select genes.name,matid,chipbase_gene.tfname as gene_tfname from mature,mir2mrna,genes,chipbase_gene where mature.matid = '"+it+"' and source = 's' and mir2mrna.genes_id = genes.id and mir2mrna.mature_id = mature.id and chipbase_gene.genes_id = genes.id order by name;";
			def nsql = "select count(distinct(genes.name)) as gcount, count(distinct(chipbase_gene.tfname)) as tfg from mature,mir2mrna,genes,chipbase_gene where mature.matid = '"+it+"' and source = 's' and mir2mrna.genes_id = genes.id and mir2mrna.mature_id = mature.id and chipbase_gene.genes_id = genes.id group by matid;";
			print nsql   
			def nres = sql.rows(nsql)
			nmap.mir = it
			nmap.gcount = nres.gcount[0]
			nmap.tfg = nres.tfg[0]
			nsql = "select count(distinct(chipbase_mir.tfname)) as tfm from precursor,mature,chipbase_mir where mature.matid = '"+it+"' and mature.precursor_id = precursor.id and chipbase_mir.pre_id = precursor.id group by matid;";
			nres = sql.rows(nsql)
			nmap.tfm = nres.tfm[0]
			print "nmap = "+nmap
			ndata.add(nmap) 	
			nmap = [:]
    	}
    	print "ndata = "+ndata
    	// def nres = Mature.findAllByMatidInList(mirs3)
//     	nres.each{mir->
//     		print "mir = "+mir.matid			
//  		   	def star = mir.mir2mrna.findAll{
//  		   		mir.mir2mrna.source=="s"
//  		   	}
//  		    //def star = mir.mir2mrna.findAll({mir.mir2mrna.source=="s"}).genes.name.unique()
// 			print "star = "+star.size()
//     	}
//     	//a.mir2mrna.findAllWhere(source:"s" )
    	
    	def com_genes = params.common_genes
    	def com_genes_list = com_genes.replaceAll(/[\]\[]/,"").split(",")
    	def com_genes_list2 = []
    	com_genes_list.each{
    		com_genes_list2.add(it)
    	}
		
		def com_mirs = params.common_mirs
    	def com_mirs_list = com_mirs.replaceAll(/[\]\[]/,"").split(",")
    	def com_mirs_list2 = []
    	com_mirs_list.each{
    		com_mirs_list2.add(it)
    	}
    	
    	return [ndata:ndata, com_genes:com_genes_list2, com_mirs:com_mirs_list2]
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
    
    def test() { 
    	//heatmap data
    	def sql = new Sql(dataSource)
    	
    	def mirList = "mature.matid = 'hsa-miR-380-3p' or mature.matid = 'hsa-miR-3934-5p' or mature.matid = 'hsa-miR-3181' or mature.matid = 'hsa-miR-515-3p' or mature.matid = 'hsa-miR-518c-5p' or mature.matid = 'hsa-miR-3151' or mature.matid = 'hsa-miR-652-3p' or mature.matid = 'hsa-miR-513c-5p' or mature.matid = 'hsa-miR-3162-5p' or mature.matid = 'hsa-miR-125a-3p' or mature.matid = 'hsa-miR-199b-5p' or mature.matid = 'hsa-miR-4264' or mature.matid = 'hsa-miR-411-3p' or mature.matid = 'hsa-miR-299-3p' or mature.matid = 'hsa-miR-3670' or mature.matid = 'hsa-let-7a-3p' or mature.matid = 'hsa-miR-190b' or mature.matid = 'hsa-miR-4302' or mature.matid = 'hsa-miR-4275' or mature.matid = 'hsa-miR-320c' or mature.matid = 'hsa-miR-10a-5p'"
    	
    	def searchSql = "select family.*,precursor.*,mature.* from family,precursor,mature where ("+mirList+") and family.id = precursor.family_id and precursor.id = mature.precursor_id;";
        //println searchSql
        def mirRes = sql.rows(searchSql)
    	
        def heatsql = "select matid,score,genes.start,name,genes.id,famid from family,precursor,mature,mir2mrna,genes where family.id = precursor.family_id and precursor.id = mature.precursor_id and mature.id = mir2mrna.mature_id and genes.id = mir2mrna.genes_id and ("+mirList+") and source = 's' order by genes.id;";
    	def top = 50
    	print heatsql
    	def heat = sql.rows(heatsql)
    	def dMap = []
    	def dList = [:]
    	def mMap = [:]
    	def mList = []
    	def famMap = [:]
		def famList = []
		
		//to get miRs with no targets too use mirRes.each and for just those with targets use heat.each
    	//mirRes.each{
    	heat.each{
    		mMap."${it.matid}"=0
    		famMap."${it.matid}"=it.famid
    	}
    	famMap.each{
    		famList.add(it.value)
    	}
    	famList = famList as JSON
    	famList = famList.decodeURL()
    	print "famList = "+famList

    	mMap.each{
    		mList.add(it.key)
    	}
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
    			//find arrays with most entries or sum the scores
    			def gr = 0
    			data.each{
    				if (it > 0){
    					gr++
    				}
    			}
    			sumData."${old_name}" = gr
    			//sumData."${old_name}" = data.sum()
    			dList."${old_name}" = data
    			//reset the map
    			mMap = [:]
    			mMapReset.each{
    				mMap."${it.key}"=0
    			}
    		}
    		mMap."${it.matid}"=it.score
    		old_name = it.name
    		old_id = it.id
    	}
    	//catch the last ones
    	mMap.each{
    		data.add(it.value)
    	}
    	dList."${old_name}" = data
    	
    	//sort and generate lists
    	def fData = []
    	def gList = []
    	def sortData = sumData.sort{it.value}.drop( sumData.size() - top )
    	print "top = "+sortData
		def count = 0
    	
    	sortData.each{
    		print it.key + " - "+it.value + ": " +dList."${it.key}"
    		gList.add(it.key)
    		fData.add(dList."${it.key}")
		}
		//transorm the matrix into lists by column (miR)
		def frData = []
		def tmp = []
		print "size = "+mList.size()	
		for (int i = 0; i < mList.size(); i++) {
			fData.each{
				tmp.add(it[i])
			}	
			frData.add(tmp)
			tmp = []
		}
		
		print "heatmap data = "+frData
		
    	gList = gList as JSON
    	gList = gList.decodeURL()
    	print "heatmap y = "+gList
    	
    	mList = mList as JSON
    	mList = mList.decodeURL()
    	print "heatmap x = "+mList
    	
    	/*
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
		*/
    	return [famList:famList, fData:frData, mList:mList, gList:gList]
		
    	
    }
    
    def search_res(){
    	def sql = new Sql(dataSource)
    	def matcher
    	//generate the list of miRs to search
    	def mirList
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
    	}else{
			mirList = "("
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
			mirList = mirList[4..-2]
		}
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
		
		//new search
		//select genes.*,score,source from genes,mature,mir2mrna where mature.matid ~ 'hsa' and mature.id = mir2mrna.mature_id and mir2mrna.genes_id = genes.id and ((mir2mrna.source = 's' and score > 2 ) or (mir2mrna.source = 'd' and score > 0.9));
		
		//get params
		def starParam = ""
		if (params.sEv > 0){
			starParam = "and score > "+params.sEv
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
        
        def heatsql = "select distinct on (genes.id,matid,name) matid,score,genes.start,name,fullname,genes.id,famid,genes.chr,genes.start,genes.stop from family,precursor,mature,mir2mrna,genes where family.id = precursor.family_id and precursor.id = mature.precursor_id and mature.id = mir2mrna.mature_id and genes.id = mir2mrna.genes_id and ("+mirList+") and (source = 's' ${starParam}) order by genes.id;";
    	print heatsql
    	def heat = sql.rows(heatsql)
    	
    	def top = 50
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
		def commonGenes = [:]
		def commonGeneList = []
		def geneNames = [:]		
		def geneLoc = [:]
		//to get miRs with no targets too use mirRes.each and for just those with targets use heat.each
    	//mirRes.each{
    
    	heat.each{
    		mMap."${it.matid}"=0
    		famMap."${it.matid}"=it.famid
    		geneNames."${it.name}"=it.fullname
    		geneLoc."${it.name}"=it.chr+":"+it.start+"-"+it.stop
    		if (countGenes."${it.name}"){
    			countGenes."${it.name}" = countGenes."${it.name}" + 1
    		}else{
    			countGenes."${it.name}" = 1
    		}
    	}
    	countGenes.each{
    		commonGenes.name = it.key
    		commonGenes.count = it.value
    		commonGenes.fullname = geneNames."${it.key}"
    		commonGenes.location = geneLoc."${it.key}"
    		//print "commonGenes = "+commonGenes
			commonGeneList.add(commonGenes)
			commonGenes = [:]    		
    	}
    	
    	if (mMap.size()>1){
			famMap.each{
				famHeatList.add(it.value)
			}
			famHeatList = famHeatList as JSON
			famHeatList = famHeatList.decodeURL()
			print "famHeatList = "+famHeatList

			mMap.each{
				mList.add(it.key)
			}
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
					//find arrays with most entries or sum the scores
					def gr = 0
					data.each{
						if (it > 0){
							gr++
						}
					}
					sumData."${old_name}" = gr
					//sumData."${old_name}" = data.sum()
					dList."${old_name}" = data
					//reset the map
					mMap = [:]
					mMapReset.each{
						mMap."${it.key}"=0
					}
				}
				mMap."${it.matid}"=it.score
				old_name = it.name
				old_id = it.id
			}
			//catch the last ones
			mMap.each{
				data.add(it.value)
			}
			dList."${old_name}" = data
		
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
		//print "heatmap data = "+frData
		
    	gList = gList as JSON
    	gList = gList.decodeURL()
    	//print "heatmap y = "+gList
    	
    	mList = mList as JSON
    	mList = mList.decodeURL()
    	//print "heatmap x = "+mList
    	//print "commonGeneList = "+commonGeneList
        
        print miRLister
        print mirLoc
        return [missing:missing, found:found,commonGeneList:commonGeneList, famHeatList:famHeatList, fData:frData, mList:mList, gList:gList, miRLister:miRLister, mirLoc:mirLocDecode, flagMap:flagMap, rank:rank, famList:famListDecode, famCount:famCount, mirRes: mirRes, starMap: starMap, mtMap: mtMap, tsMap:tsMap, diMap: diMap, sEv:params.sEv, mEv: params.mEv]    
    	
    	 /*       
        //heatmap data
        def heatsql = "select matid,score,genes.start,name,genes.id from mature,mir2mrna,genes where mature.id = mir2mrna.mature_id and genes.id = mir2mrna.genes_id and ("+mirList+") and mir2mrna.mature_id = mature.id and ((mir2mrna.source = 's' ${starParam}) or (mir2mrna.source = 'd') or (mir2mrna.source = 'm') or (mir2mrna.source = 't')) and source = 's' order by genes.id;";
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
    	def old_id = ""
    	def old_name = ""
    	def new_id = ""
    	heat.each{
    		new_id = it.id
    		if (old_id != "" && it.id != old_id){
    			//print mMap
    			dMap."${old_name}"=mMap
    			//reset the map
    			mMap = [:]
    			mMapReset.each{
    				mMap."${it.key}"=1
    			}
    		}
    		mMap."${it.matid}"=it.score+1
    		old_name = it.name
    		old_id = it.id
    	}
    	//catch the last one
    	dMap."${old_name}"=mMap
    	
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
        */
    
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
		if (params.sEv > 0){
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
