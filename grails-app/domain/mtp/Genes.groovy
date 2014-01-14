package mtp

class Genes {
	String name
	String fullname
    String chr
    int start
    int stop
    String strand
    String ensembl
    String uniprot
    
    static constraints = {
    	name(blank:false)
    	fullname(blank:false)
    	chr(blank:false)
    	start(blank:false)
    	stop(blank:false)
    	strand(blank:false)
    	ensembl(blank:false)
    	uniprot(blank:false)
    }
    static hasMany = [ mir2mrna : Mir2mrna, chipgene: ChipbaseGene]
}
