package mtp

//mirbase()
//starbase_full()
mirtarbase_full()
//tscan_full()


def cleanUpGorm() { 
    def sessionFactory = ctx.getBean("sessionFactory")
    def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
    def session = sessionFactory.currentSession 
    session.flush() 
    session.clear() 
    propertyInstanceMap.get().clear() 
}


def family(){
	def famToMir = [:]
	def count=0;
	print "### Adding family data ###"
	print "Downloading miFam.dat.gz"
	def fam_file = new File("data/miFam.dat")
	if (fam_file.exists()){
		print "Already done"
	}else{ 
		//def wget =  "wget ftp://mirbase.org/pub/mirbase/CURRENT/miFam.dat.gz -P data/"
		def wget = "wget ftp://mirbase.org/pub/mirbase/19/miFam.dat.gz -P data/"
		def proc = wget.execute()
		proc.waitFor()
		print "Unzipping..."
		def gunzip = "gunzip data/miFam.dat.gz"
		proc = gunzip.execute()
		proc.waitFor()
	}
	print "Adding to db..."
	def famMap = [:]
	fam_file.eachLine{ line ->
		if ((matcher = line =~ /^AC\s+(.*)/)){
			famMap.famacc = matcher[0][1]
		}
		if ((matcher = line =~ /^ID\s+(.*)/)){
			famMap.famid = matcher[0][1]
			count++
			Family fam = new Family(famMap)
			if ((count % 100) == 0){
				fam.save(flush:true)
				print count
			}else{
				fam.save()
			}
		}
		if ((matcher = line =~ /^MI\s+(.*?)\s+(.*)/)){
			famToMir."${matcher[0][2]}"=famMap.famid
		//	famMap.preacc = matcher[0][1]
		//	famMap.preid = matcher[0][2] 
		}
		//print ""+famMap+"\n"
	}
	famMap.famacc = "no family"
	famMap.famid = "no family"
	Family fam = new Family(famMap)
	fam.save(flush:true)
	print count 
	//print famToMir
	return famToMir
}

def mirbase(){
	def famToMir = family()
	def locToMir = location()
	print "Adding mirbase data..."
	print "Downloading miRNA.dat.gz"
	def mir_file = new File("data/miRNA.dat")
	if (mir_file.exists()){
		print "Already done"
	}else{ 
		//def wget =  "wget ftp://mirbase.org/pub/mirbase/CURRENT/miRNA.dat.gz -P data/"
		def wget =  "wget ftp://mirbase.org/pub/mirbase/19/miRNA.dat.gz -P data/"
		def proc = wget.execute()
		proc.waitFor()
		print "Unzipping..."
		def gunzip = "gunzip data/miRNA.dat.gz"
		proc = gunzip.execute()
		proc.waitFor()
	}
	print "Adding to db..."
	
	def count = 0;
	def seq = ""
	def preMap = [:]
	def threeMap = [:]
	def fiveMap = [:]
	def noarmMap = [:]
	def (preid,preid_new,matid,matseq,matacc,matstart,matstop,matarm,threestart,threestop,fivestart,fivestop,noarmstartnoarmstop) = []
	def inseq = three = five = noarm = false
	print "Adding precursor data..."
	mir_file.eachLine{ line ->
		if ((matcher = line =~ /^ID\s+(.*?)\s+.*/)){
		preid_new = matcher[0][1]
			if (seq != ""){
				seq = seq.replaceAll("[0-9]", "")
				seq = seq.replaceAll(" ", "")
				seq = seq.replaceAll("/", "")
				preMap.preseq = seq				
				if ((matcher = preid =~ /^hsa-.*/)){
					//println "\n### "+preid+" ###"
					if (locToMir."${preid}"){
						preMap.chr = locToMir."${preid}"[0]
						preMap.start = locToMir."${preid}"[1]
						preMap.stop = locToMir."${preid}"[2]
					}else{
						//print "no match for "+preid
						preMap.chr = "n/a"
						preMap.start = 0
						preMap.stop = 0
					}
					count++
					Precursor pre = new Precursor(preMap)
					Family fam = Family.findByFamid(famToMir."${preid}")
					if (fam != null){
						fam.addToPrecursor(pre)
					}else{
						//print "No family!"
						fam = Family.findByFamid("no family")
						fam.addToPrecursor(pre)
					}
					if ((count % 100) == 0){
						fam.save(flush:true)
						cleanUpGorm()
						print count
						println new Date()
					}else{
						fam.save()
					}
				}
			}
			seq = ""
			inseq = false
			preid = preid_new
			preMap.preid = preid
		}
		if ((matcher = line =~ /^AC\s+(.*?);/)){
			preMap.preacc = matcher[0][1]
		}
		if ((matcher = line =~ /^\s+/)){
			inseq = true
		}
		if (inseq == true){
			seq <<= line
		}
	}
	//catch the last one
	//println "\n### "+preid+" ###"
	if ((matcher = preid =~ /^hsa-.*/)){
		//println preMap
		if (locToMir."${preid}"){
			preMap.chr = locToMir."${preid}"[0]
			preMap.start = locToMir."${preid}"[1]
			preMap.stop = locToMir."${preid}"[2]
		}else{
			preMap.chr = "n/a"
			preMap.start = 0
			preMap.stop = 0
		}
		Precursor pre = new Precursor(preMap)
		Family fam = Family.findByFamid(famToMir."${preid}")
		if (fam != null){
			fam.addToPrecursor(pre)
		}else{
			//print "No family!"
			fam = Family.findByFamid("no family")
			fam.addToPrecursor(pre)
		}
		fam.save()
	}
	print count
	count=0
	
	//re-read to get the mature sequence
	print "Adding mature data..."
	mir_file.eachLine{ line ->
		if ((matcher = line =~ /^ID\s+(.*?)\s+.*/)){
		preid_new = matcher[0][1]
			if (seq != ""){
				seq = seq.replaceAll("[0-9]", "")
				seq = seq.replaceAll(" ", "")
				seq = seq.replaceAll("/", "")
				preMap.preseq = seq
				if ((matcher = preid =~ /^hsa-.*/)){
					count++

					//get matures - for some reason can't use the pre value above so recall it
					Precursor pre_find = Precursor.findByPreid(preid)
					if (three == true){
						threeMap.matseq = seq[threestart..threestop]
						threeMap.arm = "3"
						if (locToMir."${matid}"){
							threeMap.chr = locToMir."${matid}"[0]
							threeMap.start = locToMir."${matid}"[1]
							threeMap.stop = locToMir."${matid}"[2]
						}else{
							threeMap.chr = "n/a"
							threeMap.start = 0
							threeMap.stop = 0
						}
						Mature mat = new Mature(threeMap)
						pre_find.addToMature(mat)
						pre_find.save(flush:true)
					}
					if (five == true){
 						fiveMap.matseq = seq[fivestart..fivestop]
 						fiveMap.arm = "5"
 						if (locToMir."${matid}"){
							fiveMap.chr = locToMir."${matid}"[0]
							fiveMap.start = locToMir."${matid}"[1]
							fiveMap.stop = locToMir."${matid}"[2]
						}else{
							fiveMap.chr = "n/a"
							fiveMap.start = 0
							fiveMap.stop = 0
						}
 						Mature mat = new Mature(fiveMap)
 						pre_find.addToMature(mat)
 						pre_find.save(flush:true)
 					}
 					if (noarm == true){
 						noarmMap.matseq = seq[noarmstart..noarmstop]
 						noarmMap.arm = "0"
 						if (locToMir."${matid}"){
							noarmMap.chr = locToMir."${matid}"[0]
							noarmMap.start = locToMir."${matid}"[1]
							noarmMap.stop = locToMir."${matid}"[2]
						}else{
							noarmMap.chr = "n/a"
							noarmMap.start = 0
							noarmMap.stop = 0
						}
 						Mature mat = new Mature(noarmMap)
 						pre_find.addToMature(mat)
 						pre_find.save(flush:true)
 					}
 					if ((count % 100) == 0){
						cleanUpGorm()
						print count
						println new Date()
					}
				}
				
				//reset the arms flags
				three = false
				five = false
				noarm = false
				
			}
			seq = ""
			inseq = false
			preid = preid_new
			preMap.preid = preid
		}
		if ((matcher = line =~ /^\s+/)){
			inseq = true
		}
		if (inseq == true){
			seq <<= line
		}
		if ((matcher = line =~ /^FT\s+miRNA\s+(.*?)\.\.(.*)/)){
			matstart = matcher[0][1].toInteger()-1
			matstop = matcher[0][2].toInteger()-1
		}
		if ((matcher = line =~ /^FT\s+\/accession="(.*?)"/)){
			matacc = matcher[0][1]
		}
		if ((matcher = line =~ /^FT\s+\/product="(.*?)"/)){
			matid = matcher[0][1]
			//only want human data
			if ((matcher = matid =~ /hsa-.*/)){
			//choose the arm
				if ((matcher = matid =~ /.*?-3p$/)){	
					three = true
					threeMap.matid = matid
					threeMap.matacc = matacc
					threestart = matstart
					threestop = matstop
				}
				else if ((matcher = matid =~ /.*?-5p$/)){
					five = true
					fiveMap.matid = matid
					fiveMap.matacc = matacc
					fivestart = matstart
					fivestop = matstop
				}else{
					noarm = true
					noarmMap.matid = matid
					noarmMap.matacc = matacc
					noarmstart = matstart
					noarmstop = matstop
				}
			}
		}
	}
	//catch the last one
	Precursor pre_find = Precursor.findByPreid(preid)
	if (three == true){
		threeMap.matseq = seq[threestart..threestop]
		threeMap.arm = "3"
		if (locToMir."${matid}"){
			threeMap.chr = locToMir."${matid}"[0]
			threeMap.start = locToMir."${matid}"[1]
			threeMap.stop = locToMir."${matid}"[2]
		}else{
			threeMap.chr = "n/a"
			threeMap.start = 0
			threeMap.stop = 0
		}
		Mature mat = new Mature(threeMap)
		pre_find.addToMature(mat)
		pre_find.save(flush:true)
	}
	if (five == true){
		fiveMap.matseq = seq[fivestart..fivestop]
		fiveMap.arm = "5"
		if (locToMir."${matid}"){
			fiveMap.chr = locToMir."${matid}"[0]
			fiveMap.start = locToMir."${matid}"[1]
			fiveMap.stop = locToMir."${matid}"[2]
		}else{
			fiveMap.chr = "n/a"
			fiveMap.start = 0
			fiveMap.stop = 0
		}
		Mature mat = new Mature(fiveMap)
		pre_find.addToMature(mat)
		pre_find.save(flush:true)
	}
	if (noarm == true){
		noarmMap.matseq = seq[noarmstart..noarmstop]
		noarmMap.arm = "0"
		if (locToMir."${matid}"){
			noarmMap.chr = locToMir."${matid}"[0]
			noarmMap.start = locToMir."${matid}"[1]
			noarmMap.stop = locToMir."${matid}"[2]
		}else{
			noarmMap.chr = "n/a"
			noarmMap.start = 0
			noarmMap.stop = 0
		}
		Mature mat = new Mature(noarmMap)
		pre_find.addToMature(mat)
		pre_find.save(flush:true)
	}
	print count
}


def location(){
	def locToMir = [:]
	print "Adding location data..."
	print "Downloading hsa.gff3"
	def loc_file = new File("data/hsa.gff3")
	if (loc_file.exists()){
		print "Already done"
	}else{ 
		//def wget =  "wget ftp://mirbase.org/pub/mirbase/CURRENT/genomes/hsa.gff3 -P data/"
		def wget =  "wget ftp://mirbase.org/pub/mirbase/19/genomes/hsa.gff3 -P data/"
		def proc = wget.execute()
		proc.waitFor()
	}
	def locMap = [:]
	def mirid
	loc_file.eachLine{ line ->
		if ((matcher = line =~ /^chr.*/)){
			//print line
			def s = line.split("\t")
			locMap.chr = s[0]
			locMap.start = s[3]
			locMap.stop = s[4]
			if (s[2] == 'miRNA_primary_transcript'){
				if ((matcher = s[8] =~ /.*?Name=(.*)/)){
					mirid = matcher[0][1]
				}
				//print "\n\nprecursor: mirid = "+mirid+"\n"+locMap+"\n"
				locToMir."${mirid}"=[locMap.chr,locMap.start,locMap.stop]
			}else{
				if ((matcher = s[8] =~ /.*?Name=(.*?);/)){
					mirid = matcher[0][1]
				}
				//print "mature: mirid = "+mirid+"\n"+locMap+"\n"
				locToMir."${mirid}"=[locMap.chr,locMap.start,locMap.stop]
			}
		}
	}
	//print locToMir
	return locToMir
}

def starbase_basic(){
	def count = 0
	print "Adding starbase data..."
	def starMap = [:]
	def star_file = new File("data/starBase_Human_Interactions2013-11-04_05-18.xls")
	star_file.eachLine{ line ->
		def s = line.split("\t")
		if (s[8].toInteger() > 2){
			starMap.gene = s[1]
			starMap.source = "s"
			//print starMap
			count++
			Mature mat = Mature.findByMatid(s[0])
			Mir2mrna star = new Mir2mrna(starMap)
			mat.addToMir2mrna(star)
			if ((count % 1000) == 0){
				mat.save(flush:true)
				println new Date()
				cleanUpGorm()
				print count
			}else{
				mat.save()
			}
		}
	}
	print count
}

def mirtarbase_basic(){
	def count = 0
	print "Adding mirtarbase data..."
	def mtMap = [:]

	def mt_file = new File("data/miRTarBase_hsa_MTI.txt")

	mt_file.eachLine{ line ->
		if ((matcher = line =~ /^MIR.*/)){
			def s = line.split("\t")
			if ((matcher = s[7] != /Weak/)){
				if ((matcher = s[1] =~ /^hsa.*/)){
					mtMap.gene = s[3]
					mtMap.source = "m"
					//print mtMap
					count++
					Mature mat = Mature.findByMatid(s[1])
					Mir2mrna mt = new Mir2mrna(mtMap)
					mat.addToMir2mrna(mt)
					if ((count % 1000) == 0){
						mat.save(flush:true)
						println new Date()
						cleanUpGorm()
						print count
					}else{
						mat.save()
					}
				}
			}
		}
	}
	print count
}

def starbase_full(){
	def count = 0
	print "Adding starbase data..."
	def starMap = [:]
	def star_file = new File("data/starBase_Human_Interactions2013-11-04_05-18.xls")
	star_file.eachLine{ line ->
		def s = line.split("\t")
		//if (s[8].toInteger() > 2){
			starMap.gene = s[1]
			starMap.pnum = s[8]
			//print starMap
			count++
			Mature mat = Mature.findByMatid(s[0])
			if (mat != null){
				Starbase star = new Starbase(starMap)
				mat.addToStarbase(star)
				if ((count % 1000) == 0){
					mat.save(flush:true)
					println new Date()
					cleanUpGorm()
					print count
				}else{
					mat.save()
				}
			}
		//}
	}
	print count
}

def mirtarbase_full(){
	def count = 0
	print "Adding mirtarbase data..."
	def mtMap = [:]

	def mt_file = new File("data/miRTarBase_hsa_MTI.txt")

	mt_file.eachLine{ line ->
		if ((matcher = line =~ /^MIR.*/)){
			def s = line.split("\t")
			if (s.size() == 9){
				if ((matcher = s[1] =~ /^hsa.*/)){
					mtMap.gene = s[3]
					mtMap.mtid = s[0]
					mtMap.reference = s[8]
					mtMap.evidence = s[7]
					//print mtMap
					count++
					Mature mat = Mature.findByMatid(s[1])
					if (mat != null){
						Mirtarbase mt = new Mirtarbase(mtMap)
						mat.addToMirtarbase(mt)
						if ((count % 1000) == 0){
							mat.save(flush:true)
							println new Date()
							cleanUpGorm()
							print count
						}else{
							mat.save()
						}
					}
				}
			}else{
				print "Missing data!!! - "+mtMap
			}
		}
	}
	print count
}

def tscan_full(){
	def count = 0
	print "Adding targetscan data..."
	def tsMap = [:]

	def ts_file = new File("data/TCS_0.5_hsa.txt")

	ts_file.eachLine{ line ->
		if ((matcher = line =~ /^NM_.*/)){
			def s = line.split("\t")
			if ((matcher = s[12] =~ /^hsa.*/)){
				tsMap.gene = s[1]
				tsMap.mtid = s[12]
				tsMap.tcs = s[13]
				//print tsMap
				count++
				Mature mat = Mature.findByMatid(s[12])
				if (mat != null){
					Tscan ts = new Tscan(tsMap)
					mat.addToTscan(ts)
					if ((count % 10000) == 0){
						mat.save(flush:true)
						println new Date()
						cleanUpGorm()
						print count
					}else{
						mat.save()
					}
				}
			}
		}
	}
	print count
}