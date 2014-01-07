package mtp

class ScreenMeta {

    String type
    String cell
    String d1name
    String d2name
    
    static constraints = {
    	type(blank:false)
    	cell(blank:false)
    	d1name(blank:false)
    	d2name(blank:false)
    }
    static hasMany = [ sd:ScreenData ]
    
}
