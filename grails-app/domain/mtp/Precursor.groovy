package mtp

class Precursor {

    String preid
    String preacc
    String preseq
    String chr
    int start
    int stop
    
    static constraints = {
    	preid(blank:false,unique: true)
    	preacc(blank:false,unique: true)
    	preseq(blank:false)
    	chr(blank:false)
    	start(blank:false)
    	stop(blank:false)
    }
    static mapping = {
        preseq type: "text"
    }
    static hasMany = [mature:Mature]
    static belongsTo = [family:Family]
}
