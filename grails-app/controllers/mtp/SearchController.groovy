package mtp

import groovy.sql.Sql
import grails.converters.JSON
import org.xml.sax.InputSource

class SearchController {
	
	javax.sql.DataSource dataSource
	
    def index() { }
    
    def search_res(){
    	def sql = new Sql(dataSource)
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
    	a.each{
    		it = it.replaceAll("\n","")
    		mirList <<= "or mature.matid = '"+it+"' "
    	}
    	mirList = mirList[4..-2]
    	
        def searchSql = "select family.*,precursor.*,mature.* from family,precursor,mature where ("+mirList+") and family.id = precursor.family_id and precursor.id = mature.precursor_id;";
        //println searchSql
        def mirRes = sql.rows(searchSql)
        
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
		
		def possql = "select chr,count(distinct(matid)) from mature where ("+mirList+") group by chr order by count desc;"
        println possql
        def posData = sql.rows(possql)
        def posList = []
        def posCount = []
        posData.each{
        	posList.add(it.chr)
        	posCount.add(it.count)
        }
		def posListJSON = posList as JSON
        def posListDecode = posListJSON.decodeURL()
		
        def starSql = "select mature.matid,count(distinct(gene)) from mature,starbase where ("+mirList+") and starbase.mature_id = mature.id and starbase.pnum > ${starEv} group by mature.matid;"
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
    	def mtSql = "select mature.matid,count(distinct(gene)) from mature,mirtarbase where ("+mirList+") and mirtarbase.mature_id = mature.id ${weak} group by mature.matid;"
        //print mtSql
        def mtRes = sql.rows(mtSql)
        def mtMap = [:]
        mirRes.each{
        	mtMap."${it.matid}" = 0
        }
        mtRes.each{
        	mtMap."${it.matid}" = it.count
        }
        
        def tsSql = "select mature.matid,count(distinct(gene)) from mature,tscan where ("+mirList+") and tscan.mature_id = mature.id group by mature.matid;"
        //print tsSql
        def tsRes = sql.rows(tsSql)
        def tsMap = [:]
        mirRes.each{
        	tsMap."${it.matid}" = 0
        }
        tsRes.each{
        	tsMap."${it.matid}" = it.count
        }
        
        def diSql = "select mature.matid,count(distinct(gene)) from mature,diana where ("+mirList+") and diana.mature_id = mature.id group by mature.matid;"
        //print diSql
        def diRes = sql.rows(diSql)
        def diMap = [:]
        mirRes.each{
        	diMap."${it.matid}" = 0
        }
        diRes.each{
        	diMap."${it.matid}" = it.count
        }
        
        return [posList: posListDecode, posCount: posCount, famList:famListDecode, famCount:famCount, mirList: p, mirRes: mirRes, starMap: starMap, mtMap: mtMap, tsMap:tsMap, diMap: diMap, sEv:params.sEv, mEv: params.mEv]    
    }
    
    def genes(){
	    def sql = new Sql(dataSource)
		def miR = Mature.findMatid(params.matid)
		def starEv = params.sEv
		def starAllSql = "select distinct(gene) from mature,starbase where mature.matid = '"+params.matid+"' and starbase.mature_id = mature.id and starbase.pnum > ${starEv};"
		print starAllSql
		def starAll = sql.rows(starAllSql)
		def unionGeneMap = [:]
		def starGenes = ""
		starAll.each{
			unionGeneMap."${it.gene}"=""
			starGenes <<= it.gene+"\n"
		}
		
		def weak = ""
        if (params.mEv == '1'){
        	weak = "and mirtarbase.evidence !~ '(Weak)'"
        }
		def mtAllSql = "select distinct(gene) from mature,mirtarbase where mature.matid = '"+params.matid+"' and mirtarbase.mature_id = mature.id ${weak};"
		print mtAllSql
		def mtAll = sql.rows(mtAllSql)
		def mtGenes = ""
		mtAll.each{
			unionGeneMap."${it.gene}"=""
			mtGenes <<= it.gene+"\n"
		}

		def tsAllSql = "select distinct(gene) from mature,tscan where mature.matid = '"+params.matid+"' and tscan.mature_id = mature.id;"
		print tsAllSql
		def tsAll = sql.rows(tsAllSql)
		def tsGenes = ""
		tsAll.each{
			unionGeneMap."${it.gene}"=""
			tsGenes <<= it.gene+"\n"
		}
		
		def diAllSql = "select distinct(gene) from mature,diana where mature.matid = '"+params.matid+"' and diana.mature_id = mature.id;"
		print diAllSql
		def diAll = sql.rows(diAllSql)
		def diGenes = ""
		diAll.each{
			unionGeneMap."${it.gene}"=""
			diGenes <<= it.gene+"\n"
		}
		
		
		def union = ""
		unionGeneMap.each{
			union <<= it.key+"\n"
		}
		
		return [miR:miR, starAll:starAll, starGenes:starGenes, mtAll:mtAll, mtGenes:mtGenes, tsAll:tsAll, tsGenes:tsGenes, diAll:diAll, diGenes:diGenes, unionGenes:union, unionGeneMap: unionGeneMap]
	}
}
