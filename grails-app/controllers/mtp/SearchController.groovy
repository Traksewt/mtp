package mtp

import groovy.sql.Sql

class SearchController {
	
	javax.sql.DataSource dataSource
	
    def index() { }
    
    def search_res(){
    	def sql = new Sql(dataSource)
    	def mirList = "("
    	def p = params.mirList.replaceAll("\r\n|\n\r|\n|\r"," ") 
    	def a = p.split(" ")
    	def starEv = params.sEv
    	a.each{
    		it = it.replaceAll("\n","")
    		mirList <<= "or mature.matid = '"+it+"' "
    	}
    	mirList = mirList[4..-2]
    	//def searchSql = "select mature.*,mir2mrna.* from mature,mir2mrna where ("+mirList+") and mir2mrna.mature_id = mature.id;;"
        def searchSql = "select family.*,precursor.*,mature.* from family,precursor,mature where ("+mirList+") and family.id = precursor.family_id and precursor.id = mature.precursor_id;";
        //println searchSql
        def mirRes = sql.rows(searchSql)
        
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
        print mtSql
        def mtRes = sql.rows(mtSql)
        def mtMap = [:]
        mirRes.each{
        	mtMap."${it.matid}" = 0
        }
        mtRes.each{
        	mtMap."${it.matid}" = it.count
        }
        
        def tsSql = "select mature.matid,count(distinct(gene)) from mature,tscan where ("+mirList+") and tscan.mature_id = mature.id group by mature.matid;"
        print tsSql
        def tsRes = sql.rows(tsSql)
        def tsMap = [:]
        mirRes.each{
        	tsMap."${it.matid}" = 0
        }
        tsRes.each{
        	tsMap."${it.matid}" = it.count
        }
        
        return [mirList: p, mirRes: mirRes, starMap: starMap, mtMap: mtMap, tsMap:tsMap, sEv:params.sEv, mEv: params.mEv]    
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
		def union = ""
		unionGeneMap.each{
			union <<= it.key+"\n"
		}
		
		return [miR:miR, starAll:starAll, starGenes:starGenes, mtAll:mtAll, mtGenes:mtGenes, unionGenes:union, unionGeneMap: unionGeneMap]
	}
}
