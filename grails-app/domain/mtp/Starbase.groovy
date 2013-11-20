package mtp

class Starbase {

    String gene
    int pnum
    
    static constraints = {
    	gene(blank:false)
    	pnum(blank:false)
    }
    static belongsTo = [mature: Mature,genes:Genes]
}
