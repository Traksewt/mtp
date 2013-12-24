package mtp

class ChipbaseMir {

    String tfname
        
    static constraints = {
    	tfname(blank:false)
    }
    static belongsTo = [pre:Precursor]
}
