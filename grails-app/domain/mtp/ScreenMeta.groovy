package mtp

class ScreenMeta {

    String type
    String cell
    String d1
    String d2
    
    static constraints = {
    	type(blank:false)
    	cell(blank:false)
    	d1(blank:false)
    	d2(blank:false)
    }
    static hasMany = [ sd:ScreenData ]
    
}
