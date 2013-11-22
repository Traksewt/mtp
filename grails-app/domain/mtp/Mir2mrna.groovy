package mtp

class Mir2mrna {

    String source
    Float score
    
    static constraints = {
    	source(blank:false)
    	score(blank:false)
    }
    static belongsTo = [mature: Mature,genes:Genes]
}
