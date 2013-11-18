package mtp

class Flag {

    Float score
    String description
    
    static constraints = {
    	score(blank:false)
    	description(blank:false)
    }
    static belongsTo = [mature: Mature]
}
