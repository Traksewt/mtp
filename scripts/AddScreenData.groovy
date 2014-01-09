package mtp

/**
n1()
n2()
p1()
p2()
b1()
b2()
b3()
b4()
b5()
b6()
b7()
b8()
b9()
b10()
b11()
b12()
**/

b4()

def n1(){
	def metaMap = [:]
	metaMap.type = "Neuroblastoma"
	metaMap.cell = "Shep"
	metaMap.d1name = "Doxorubicin"
	metaMap.d2name = "Vincristine"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/20130930_SHEP_both_drugs_summary_data.csv",smeta)
}

def n2(){
	def metaMap = [:]
	metaMap.type = "Neuroblastoma"
	metaMap.cell = "Kelly"
	metaMap.d1name = "Doxorubicin"
	metaMap.d2name = "Vincristine"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/20131003_Kelly_both_drugs_summary_data.csv",smeta)
}

def p1(){
	def metaMap = [:]
	metaMap.type = "Prostate"
	metaMap.cell = "DU145"
	metaMap.d1name = "Docetaxel"
	metaMap.d2name = "Cabazitaxel"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/20131029_DU145_both_drugs_summary_data.csv",smeta)
}

def p2(){
	def metaMap = [:]
	metaMap.type = "Prostate"
	metaMap.cell = "PC3"
	metaMap.d1name = "Docetaxel"
	metaMap.d2name = "Cabazitaxel"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/20131031_PC3_both_drugs_summary_data.csv",smeta)
}

def b1(){
	def metaMap = [:]
	metaMap.type = "Breast_1"
	metaMap.cell = "231"
	metaMap.d1name = "Docetaxel"
	metaMap.d2name = "Epirubicin"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/Breast_Screen_1_231.csv",smeta)
}

def b2(){
	def metaMap = [:]
	metaMap.type = "Breast_1"
	metaMap.cell = "468"
	metaMap.d1name = "Docetaxel"
	metaMap.d2name = "Epirubicin"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/Breast_Screen_1_468.csv",smeta)
}

def b3(){
	def metaMap = [:]
	metaMap.type = "Breast_1"
	metaMap.cell = "BT474"
	metaMap.d1name = "Docetaxel"
	metaMap.d2name = "Epirubicin"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/Breast_Screen_1_BT474.csv",smeta)
}

def b4(){
	def metaMap = [:]
	metaMap.type = "Breast_1"
	metaMap.cell = "SkBr3"
	metaMap.d1name = "Docetaxel"
	metaMap.d2name = "Epirubicin"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/Breast_Screen_1_SkBr3.csv",smeta)
}

def b5(){
	def metaMap = [:]
	metaMap.type = "Breast_2.1"
	metaMap.cell = "SkBr3"
	metaMap.d1name = "Docetaxel"
	metaMap.d2name = "Epirubicin"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/Breast_Screen_2.1_SKBR3.csv",smeta)
}

def b6(){
	def metaMap = [:]
	metaMap.type = "Breast_2.1"
	metaMap.cell = "468"
	metaMap.d1name = "Docetaxel"
	metaMap.d2name = "Epirubicin"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/Breast_Screen_2.1_468.csv",smeta)
}

def b7(){
	def metaMap = [:]
	metaMap.type = "Breast_2.1"
	metaMap.cell = "231"
	metaMap.d1name = "Docetaxel"
	metaMap.d2name = "Epirubicin"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/Breast_Screen_2.1_231.csv",smeta)
}

def b8(){
	def metaMap = [:]
	metaMap.type = "Breast_2.1"
	metaMap.cell = "BT474"
	metaMap.d1name = "Docetaxel"
	metaMap.d2name = "Epirubicin"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/Breast_Screen_2.1_BT474.csv",smeta)
}

def b9(){
	def metaMap = [:]
	metaMap.type = "Breast_2.2"
	metaMap.cell = "SkBr3"
	metaMap.d1name = "Docetaxel"
	metaMap.d2name = "Epirubicin"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/Breast_Screen_2.2_SKBR3.csv",smeta)
}

def b10(){
	def metaMap = [:]
	metaMap.type = "Breast_2.2"
	metaMap.cell = "468"
	metaMap.d1name = "Docetaxel"
	metaMap.d2name = "Epirubicin"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/Breast_Screen_2.2_468.csv",smeta)
}

def b11(){
	def metaMap = [:]
	metaMap.type = "Breast_2.2"
	metaMap.cell = "231"
	metaMap.d1name = "Docetaxel"
	metaMap.d2name = "Epirubicin"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/Breast_Screen_2.2_231.csv",smeta)
}

def b12(){
	def metaMap = [:]
	metaMap.type = "Breast_2.2"
	metaMap.cell = "BT474"
	metaMap.d1name = "Docetaxel"
	metaMap.d2name = "Epirubicin"
	ScreenMeta smeta = new ScreenMeta(metaMap)
	smeta.save(flush:true)
	addFunData("screen/Breast_Screen_2.2_BT474.csv",smeta)
}


//format is comma split library, mature accession, vehicle, drug1, drug2
def addFunData(fileName,smeta){
	def count=0
	print "Adding data for "+fileName
	print "Meta id = "+smeta
	def screen_file = new File("data/"+fileName)
	def metaMap = [:]
	screen_file.eachLine{ line ->
		def l = line.split(",")
		def matacc = l[1].replaceAll(/\"/,"")
		Mature mat = Mature.findByMatacc(matacc)
		metaMap = [:]
		if (mat != null){
			metaMap.library = l[0].replaceAll(/\"/,"")
			metaMap.veh = l[2].toFloat()
			metaMap.d1 = l[3].toFloat()
			metaMap.d2 = l[4].toFloat()
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