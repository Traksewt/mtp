package mtp

class Mature {

    String matid
    String matacc
    String matseq
    Integer arm
    
    static constraints = {
    	matid(blank:false)
    	matacc(blank:false)
    	matseq(blank:false)
    	arm(blank:false)
    }
    static mapping = {
        matseq type: "text"
    }
    static belongsTo = [ precursor: Precursor ]
}
