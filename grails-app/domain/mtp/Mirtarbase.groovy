package mtp

class Mirtarbase {

    String gene
    String mtid
    int ref
    
    static constraints = {
    	gene(blank:false)
    	mtid(blank:false)
    }
    static belongsTo = [mature: Mature]
}
