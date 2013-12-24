package mtp

class ChipbaseGene {

    String tfname
        
    static constraints = {
    	tfname(blank:false)
    }
    static belongsTo = [genes:Genes]
}
