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
    	
    	a.each{
    		it = it.replaceAll("\n","")
    		mirList <<= "or mature.matid = '"+it+"' "
    	}
    	mirList = mirList[4..-2]
    	//def searchSql = "select mature.*,mir2mrna.* from mature,mir2mrna where ("+mirList+") and mir2mrna.mature_id = mature.id;;"
        def searchSql = "select family.*,precursor.*,mature.* from family,precursor,mature where ("+mirList+") and family.id = precursor.family_id and precursor.id = mature.precursor_id;";
        println searchSql
        def res = sql.rows(searchSql)
        return [res: res, mirList: a]    
    }
}
