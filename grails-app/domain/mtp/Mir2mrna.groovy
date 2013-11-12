package mtp

class Mir2mrna {

    String gene
    String source
    
    static constraints = {
    	gene(blank:false)
    	source(blank:false)
    }
    static belongsTo = [mature: Mature]
}
