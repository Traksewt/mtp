package mtp

class Diana {

    Float threshold
    
    static constraints = {
    	threshold(blank:false)
    }
    static belongsTo = [mature: Mature,genes:Genes]
}
