package mtp

class Mature {

    String matid
    String matacc
    String matseq
    String arm
    String chr
    int start
    int stop
    
    static constraints = {
    	matid(blank:false,unique: true)
    	matacc(blank:false,unique: true)
    	matseq(blank:false)
    	chr(blank:false)
    	start(blank:false)
    	stop(blank:false)
    }
    static mapping = {
        matseq type: "text"
    }
    static belongsTo = [ precursor: Precursor ]
    static hasMany = [ starbase : Starbase, mirtarbase : Mirtarbase, mir2mrna: Mir2mrna, tscan: Tscan, diana: Diana ]
}
