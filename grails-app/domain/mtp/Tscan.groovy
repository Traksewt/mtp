package mtp

class Tscan {

    Float tcs
    
    static constraints = {
    	tcs(blank:false)
    }
    static belongsTo = [mature: Mature,genes:Genes]
}
