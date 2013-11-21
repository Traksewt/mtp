package mtp

class Starbase {

    int pnum
    
    static constraints = {
    	pnum(blank:false)
    }
    static belongsTo = [mature: Mature,genes:Genes]
}
