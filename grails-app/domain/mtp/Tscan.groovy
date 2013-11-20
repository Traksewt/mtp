package mtp

class Tscan {

    String gene
    Float tcs
    
    static constraints = {
    	gene(blank:false)
    	tcs(blank:false)
    }
    static belongsTo = [mature: Mature,genes:Genes]
}
