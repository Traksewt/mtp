package mtp

//family()
mirbase()
//location()

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
		def wget =  "wget ftp://mirbase.org/pub/mirbase/CURRENT/miFam.dat.gz -P data/"
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
	print "Adding mirbase data..."
	print "Downloading miRNA.dat.gz"
	def mir_file = new File("data/miRNA.dat")
	if (mir_file.exists()){
		print "Already done"
	}else{ 
		def wget =  "wget ftp://mirbase.org/pub/mirbase/CURRENT/miRNA.dat.gz -P data/"
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
					//println "\n### "+preid+" ###"
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
					}else{
						fam.save()
					}
					//println preMap

					//get matures
					if (three == true){
						threeMap.matseq = seq[threestart..threestop]
					}
					if (five == true){
						fiveMap.matseq = seq[fivestart..fivestop]
					}
					if (noarm == true){
						noarmMap.matseq = seq[noarmstart..noarmstop]
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
		if ((matcher = line =~ /^AC\s+(.*?);/)){
			preMap.preacc = matcher[0][1]
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
	//println "\n### "+preid+" ###"
	if ((matcher = preid =~ /^hsa-.*/)){
		println preMap

		//get matures
		if (three == true){
			threeMap.seq = seq[threestart..threestop]
		}
		if (five == true){
			fiveMap.seq = seq[fivestart..fivestop]
		}
		if (noarm == true){
			noarmMap.seq = seq[noarmstart..noarmstop]
		}
	}
}


def location(){
	print "Adding location data..."
	def locMap = [:]
	def mirid
	def loc_file = new File("/Users/ben/Work/data/mirbase/hsa.gff3")
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
				print "\n\nprecursor: mirid = "+mirid+"\n"+locMap+"\n"
			}else{
				if ((matcher = s[8] =~ /.*?Name=(.*?);/)){
					mirid = matcher[0][1]
				}
				print "mature: mirid = "+mirid+"\n"+locMap+"\n"
			}
		}
	}
}