package mtp

class Genes {
	String name
    String chr
    int start
    int stop
    String strand
    
    static constraints = {
    	name(blank:false)
    	chr(blank:false)
    	start(blank:false)
    	stop(blank:false)
    	strand(blank:false)
    }
}
