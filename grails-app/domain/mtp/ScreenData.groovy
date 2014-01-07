package mtp

class ScreenData {

    String library
    Float veh
    Float d1
    Float d2
    
    static constraints = {
    	library(blank:false)
    	veh(blank:false)
    	d1(blank:false)
    	d2(blank:false)
    }

    static belongsTo = [ mature: Mature, sm: ScreenMeta ]
}
