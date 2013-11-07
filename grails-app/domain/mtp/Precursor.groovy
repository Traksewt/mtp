package mtp

class Precursor {

    String mirid
    String acc
    String seq
    
    static constraints = {
    	mirid(blank:false,unique: true)
    	acc(blank:false,unique: true)
    	seq(blank:false)
    }
    static mapping = {
        seq type: "text"
    }
}
