package mtp

class Mature {

    String matid
    String matacc
    String matseq
    String arm
    
    static constraints = {
    	matid(blank:false)
    	matacc(blank:false)
    	matseq(blank:false)
    }
    static mapping = {
        matseq type: "text"
    }
    static belongsTo = [ precursor: Precursor ]
}
