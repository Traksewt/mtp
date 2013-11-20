package mtp

class Diana {

    String gene
    Float threshold
    
    static constraints = {
    	gene(blank:false)
    	threshold(blank:false)
    }
    static belongsTo = [mature: Mature,genes:Genes]
}
