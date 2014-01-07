package mtp


s1()
s2()

def s1(){
	def metaMap = [:]
	metaMap.type = "Neuroblastoma"
	metaMap.cell = "Shep"
	metaMap.d1 = "Dox"
	metaMap.d2 = "Vin"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("20130930_SHEP_both_drugs_summary_data.csv",smeta)
}

def s2(){
	def metaMap = [:]
	metaMap.type = "Neuroblastoma"
	metaMap.cell = "Kelly"
	metaMap.d1 = "Dox"
	metaMap.d2 = "Vin"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("20131003_Kelly_both_drugs_summary_data.csv",smeta)
}

def addFunData(fileName,smeta){
	def count=0
	print "Adding data for "+fileName
	print "Meta id = "+smeta
	def screen_file = new File("data/"+fileName)
	def metaMap = [:]
	screen_file.eachLine{ line ->
		def l = line.split(",")
		def matacc = l[5].replaceAll(/\"/,"")
		Mature mat = Mature.findByMatacc(matacc)
		metaMap = [:]
		if (mat != null){
			metaMap.library = l[3].replaceAll(/\"/,"")
			metaMap.veh = l[12].toFloat()
			metaMap.d1 = l[16].toFloat()
			metaMap.d2 = l[20].toFloat()
			//print metaMap
			count++
			ScreenData sdata = new ScreenData(metaMap)
			mat.addToSd(sdata)
			smeta.addToSd(sdata)
			if ((count % 100) == 0){
				mat.save(flush:true)
				smeta.save(flush:true)
				print count
			}else{
				mat.save()
				smeta.save()
			}
		}
	}
	print count
}