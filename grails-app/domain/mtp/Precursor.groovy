package mtp

class Precursor {

    String preid
    String preacc
    String preseq
    
    static constraints = {
    	preid(blank:false,unique: true)
    	preacc(blank:false,unique: true)
    	preseq(blank:false)
    }
    static mapping = {
        preseq type: "text"
    }
    static hasMany = [mature:Mature]
    static belongsTo = [family:Family]
}
