package mtp

class Mirtarbase {

    String gene
    String mtid
	String evidence
    int reference
    
    static constraints = {
    	gene(blank:false)
    	mtid(blank:false)
    	evidence(blank:false)
    	reference(blank:false)
    }
    static belongsTo = [mature: Mature]
}
