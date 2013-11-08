package mtp

class Family {

    String famid
    String famacc
    
    static constraints = {
    	famid(blank:false,unique: true)
    	famacc(blank:false,unique: true)
    }
    static hasMany = [ precursor: Precursor ]
}
