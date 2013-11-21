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
    	def mirLoc = sql.rows(mirLocSql)
    	def mirLocJSON = mirLoc as JSON
        def mirLocDecode = mirLocJSON.decodeURL()
    	//print "mirLoc = "+mirLocDecode
    	
    	
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
    	def p = mirFile.replaceAll("\r\n|\n\r|\n|\r"," ") 
    	def a = p.split(" ")
    	def starEv = params.sEv
    	def rank = [:]
    	a.each{
    		it = it.replaceAll("\n","")
    		def s = it.split("\t")
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
		
        def starSql = "select mature.matid,count(distinct(starbase.genes_id)) from mature,starbase where ("+mirList+") and starbase.mature_id = mature.id and starbase.pnum > ${starEv} group by mature.matid;"
        //print starSql
        def starRes = sql.rows(starSql)
        def starMap = [:]
        mirRes.each{
        	starMap."${it.matid}" = 0
        }
        starRes.each{
        	starMap."${it.matid}" = it.count
        }
        
        def weak = ""
        if (params.mEv == '1'){
        	weak = "and mirtarbase.evidence !~ '(Weak)'"
        }
    	def mtSql = "select mature.matid,count(distinct(mirtarbase.genes_id)) from mature,mirtarbase where ("+mirList+") and mirtarbase.mature_id = mature.id ${weak} group by mature.matid;"
        //print mtSql
        def mtRes = sql.rows(mtSql)
        def mtMap = [:]
        mirRes.each{
        	mtMap."${it.matid}" = 0
        }
        mtRes.each{
        	mtMap."${it.matid}" = it.count
        }
        
        def tsSql = "select mature.matid,count(distinct(tscan.genes_id)) from mature,tscan where ("+mirList+") and tscan.mature_id = mature.id group by mature.matid;"
        print tsSql
        def tsRes = sql.rows(tsSql)
        def tsMap = [:]
        mirRes.each{
        	tsMap."${it.matid}" = 0
        }
        tsRes.each{
        	tsMap."${it.matid}" = it.count
        }
        
        def diSql = "select mature.matid,count(distinct(diana.genes_id)) from mature,diana where ("+mirList+") and diana.mature_id = mature.id group by mature.matid;"
        //print diSql
        def diRes = sql.rows(diSql)
        def diMap = [:]
        mirRes.each{
        	diMap."${it.matid}" = 0
        }
        diRes.each{
        	diMap."${it.matid}" = it.count
        }
        print miRLister
        print mirLoc
        return [miRLister:miRLister, mirLoc:mirLocDecode, flagMap:flagMap, rank:rank, famList:famListDecode, famCount:famCount, mirList: p, mirRes: mirRes, starMap: starMap, mtMap: mtMap, tsMap:tsMap, diMap: diMap, sEv:params.sEv, mEv: params.mEv]    
    }
    
    def genes(){
	    def sql = new Sql(dataSource)
	    def matcher
		def miR = Mature.findByMatid(params.matid)
		def unionGeneMap = [:]
		def chrList = ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","X","Y"]
		
		def starEv = params.sEv
		def starAllSql = "select genes.* from mature,starbase,genes where mature.matid = '"+params.matid+"' and starbase.mature_id = mature.id and starbase.pnum > ${starEv} and starbase.genes_id = genes.id;"
		print starAllSql
		def starAll = sql.rows(starAllSql)
		def starGenes = []
		starAll.each{
			unionGeneMap."${it.name}"=""
			starGenes.add(it.name)
		}
		def starCount = starGenes.unique()
		starGenes = starCount.join(", ")
		def cSql = "select genes.chr,count(name) from mature,starbase,genes where mature.matid = '"+params.matid+"' and starbase.mature_id = mature.id and starbase.genes_id = genes.id group by genes.chr order by chr;";
		def c = sql.rows(cSql)
		def cJSON = c as JSON
        def starDecode = cJSON.decodeURL()
    	print "starDecode = "+starDecode
		
		def weak = ""
        if (params.mEv == '1'){
        	weak = "and mirtarbase.evidence !~ '(Weak)'"
        }
		def mtAllSql = "select genes.* from mature,mirtarbase,genes where mature.matid = '"+params.matid+"' and mirtarbase.mature_id = mature.id ${weak} and mirtarbase.genes_id = genes.id;"
		print mtAllSql
		def mtAll = sql.rows(mtAllSql)
		def mtGenes = []
		mtAll.each{
			unionGeneMap."${it.name}"=""
			mtGenes.add(it.name)
		}
		def mtCount = mtGenes.unique()
		mtGenes = mtCount.join(", ")
		cSql = "select genes.chr,count(name) from mature,mirtarbase,genes where mature.matid = '"+params.matid+"' and mirtarbase.mature_id = mature.id and mirtarbase.genes_id = genes.id group by genes.chr order by chr;";
		c = sql.rows(cSql)
		cJSON = c as JSON
        def mtDecode = cJSON.decodeURL()
    	print "mtDecode = "+mtDecode
		
		def tsAllSql = "select genes.* from mature,tscan,genes where mature.matid = '"+params.matid+"' and tscan.mature_id = mature.id and tscan.genes_id = genes.id;"
		print tsAllSql
		def tsAll = sql.rows(tsAllSql)
		def tsGenes = []
		tsAll.each{
			unionGeneMap."${it.name}"=""
			tsGenes.add(it.name)
		}
		def tsCount = tsGenes.unique()
		tsGenes = tsCount.join(", ")
		cSql = "select genes.chr,count(name) from mature,tscan,genes where mature.matid = '"+params.matid+"' and tscan.mature_id = mature.id and tscan.genes_id = genes.id group by genes.chr order by chr;";
		c = sql.rows(cSql)
		cJSON = c as JSON
        def tsDecode = cJSON.decodeURL()
    	print "tsDecode = "+tsDecode
		
		
		cSql = "select genes.chr,genes.start,genes.name from mature,diana,genes where mature.matid = '"+params.matid+"' and diana.mature_id = mature.id and diana.genes_id = genes.id order by chr,start;";
		c = sql.rows(cSql)
		cJSON = c as JSON
        def diDecode = cJSON.decodeURL()
    	print "diDecode = "+diDecode
		
		def diGenes = []
		c.each{
			unionGeneMap."${it.name}"=""
			diGenes.add(it.name)
		}	
		def diCount = diGenes.unique()
		diGenes = diCount.join(", ")
		
		def union = ""
		unionGeneMap.each{
			union <<= it.key+","
		}
		if (union.size()>0){
			union = union[0..-2]
		}
		def diGCountSql = "select genes.chr,count(name) from mature,diana,genes where mature.matid = '"+params.matid+"' and diana.mature_id = mature.id and diana.genes_id = genes.id group by genes.chr;";
    	print diGCountSql
    	def diGCount = sql.rows(diGCountSql)
    	def diLister = []
    	def diMap = [:]
    	chrList.each{
    		diMap."${it}" = 0
    	}
    	diGCount.each{
    		if ((matcher = it.chr =~ /chr(.*)/)){ 
    			diMap."${matcher[0][1]}"=it.count
    		}
    	}
    	diMap.each{
    		diLister.add(it.value)
    	}
		
		print diLister
		print diDecode
		return [miR:miR, starGenes:starGenes, starCount:starCount, starDecode:starDecode, mtGenes:mtGenes, mtCount:mtCount, mtDecode:mtDecode, tsGenes:tsGenes, tsCount:tsCount, tsDecode:tsDecode, diGenes:diGenes, diCount:diCount, diDecode:diDecode, diLister:diLister, unionGenes:union, unionGeneMap: unionGeneMap]
	}
}
