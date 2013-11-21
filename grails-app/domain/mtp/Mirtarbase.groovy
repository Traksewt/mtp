package mtp

class Mirtarbase {

    String mtid
	String evidence
    int reference
    
    static constraints = {
    	mtid(blank:false)
    	evidence(blank:false)
    	reference(blank:false)
    }
    static belongsTo = [mature: Mature,genes:Genes]
}
