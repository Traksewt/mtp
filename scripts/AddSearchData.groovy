package mtp


mirbase()
genes()
cardiac_flag()
starbase_full()
mirtarbase_full()
tscan_full()
diana_full()
chipbase_gene()
chipbase_mir()

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



def starbase_full(){
	def count = 0
	print "Adding starbase data..."
	def starMap = [:]
	def gene
	def star_file = new File("data/starBase_Human_Interactions2013-11-04_05-18.filter.txt")
	//def star_file = new File("data/star_50000.xls")
	star_file.eachLine{ line ->
		def s = line.split("\t")
		//if (s[8].toInteger() > 2){
			gene = s[1]
			starMap.score = s[2]
			starMap.source = 's'
			//print starMap
			//print gene
			count++
			Mature mat = Mature.findByMatid(s[0])
			Genes g = Genes.findByName(gene)
			if (mat != null && g !=null){
				Mir2mrna star = new Mir2mrna(starMap)
				mat.addToMir2mrna(star)
				g.addToMir2mrna(star)
				if ((count % 1000) == 0){
					mat.save(flush:true)
					g.save(flush:true)
					println new Date()
					cleanUpGorm()
					print count
				}else{
					mat.save()
					g.save()
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
	def gene
	def mt_file = new File("data/miRTarBase_hsa_MTI.txt")
	//def mt_file = new File("data/mirtarbase_10000.txt")

	mt_file.eachLine{ line ->
		if ((matcher = line =~ /^MIR.*/)){
			def s = line.split("\t")
			if (s.size() == 9){
				if ((matcher = s[1] =~ /^hsa.*/)){
					gene = s[3]
					mtMap.source = "m"
					mtMap.score = 0
					//print mtMap
					count++
					Mature mat = Mature.findByMatid(s[1])
					Genes g = Genes.findByName(gene)
					if (mat != null && g !=null){
						Mir2mrna mt = new Mir2mrna(mtMap)
						mat.addToMir2mrna(mt)
						g.addToMir2mrna(mt)
						if ((count % 1000) == 0){
							mat.save(flush:true)
							g.save(flush:true)
							println new Date()
							cleanUpGorm()
							print count
						}else{
							mat.save()
							g.save()
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
	def gene
	def ts_file = new File("data/TCS_0.5_hsa.txt")
	//def ts_file = new File("data/tscan_10000.txt")
	ts_file.eachLine{ line ->
		if ((matcher = line =~ /^NM_.*/)){
			def s = line.split("\t")
			if ((matcher = s[12] =~ /^hsa.*/)){
				gene = s[1]
				tsMap.source = "t"
				tsMap.score = s[13]
				//print tsMap
				count++
				Mature mat = Mature.findByMatid(s[12])
				Genes g = Genes.findByName(gene)
				if (mat != null && g !=null){
					Mir2mrna ts = new Mir2mrna(tsMap)
					mat.addToMir2mrna(ts)
					g.addToMir2mrna(ts)
					if ((count % 1000) == 0){
						mat.save(flush:true)
						g.save(flush:true)
						println new Date()
						cleanUpGorm()
						print count
					}else{
						mat.save()
						g.save()
					}
				}
			}
		}
	}
	print count
}

def diana_full(){
	def count = 0
	print "Adding DIANA-microT-CDS data..."
	def diMap = [:]
	def gene
	def di_file = new File("data/microtcds_hsa_0.9_data.csv")
	//def di_file = new File("data/diana_10000.txt")

	di_file.eachLine{ line ->
		if ((matcher = line =~ /^ENST.*/)){
			def s = line.split(",")
			if ((matcher = s[1] =~ /.*?\((.*?)\)/)){
				gene = matcher[0][1]
			}
			if ((matcher = s[2] =~ /(.*?)\(.*/)){
				diMap.mtid = s[2] = matcher[0][1]
			}
			diMap.score = s[3]
			diMap.source = "d"
			//print diMap
			count++
			Mature mat = Mature.findByMatid(s[2])
			Genes g = Genes.findByName(gene)
			if (mat != null && g !=null){
				Mir2mrna di = new Mir2mrna(diMap)
				mat.addToMir2mrna(di)
				g.addToMir2mrna(di)
				if ((count % 1000) == 0){
					mat.save(flush:true)
					g.save(flush:true)
					println new Date()
					cleanUpGorm()
					print count
				}else{
					mat.save()
					g.save()
				}
			}
		}
	}
	print count
}

def cardiac_flag(){
	print "Adding cardiac data..."
	def cMap = [:]
	print "Adding increase data..."
	def cFile = new File("data/twofold_increase_proliferation.tsv")
	cFile.eachLine{ line ->
		def s = line.split("\t")
		cMap.score = s[6]
		cMap.description = "Cardiac increase"
		//print cMap
		Mature mat = Mature.findByMatacc(s[1])
		if (mat != null){
			Flag flag = new Flag(cMap)
			mat.addToFlag(flag)
			mat.save(flush:true)
		}
	}
	print "Adding decrease data..."
	cFile = new File("data/twofold_decrease_proliferation.tsv")
	cFile.eachLine{ line ->
		def s = line.split("\t")
		cMap.score = s[5]
		cMap.description = "Cardiac decrease"
		//print cMap
		Mature mat = Mature.findByMatacc(s[1])
		if (mat != null){
			Flag flag = new Flag(cMap)
			mat.addToFlag(flag)
			mat.save(flush:true)
		}
	}
}
def genes(){
	def count=0
	print "Adding gene data..."
	
	print "Downloading HUGO file"
	def gFile = new File("data/hugo_genes_names.txt")
	if (gFile.exists()){
		print "Already done"
	}else{ 
		//def wget =  "wget ftp://ftp.ebi.ac.uk/pub/databases/genenames/locus_groups/protein-coding_gene.txt.gz -P data/"
		def wget = "wget http://www.genenames.org/cgi-bin/download?col=gd_hgnc_id&col=gd_app_sym&col=gd_app_name&col=gd_status&col=gd_prev_sym&col=gd_aliases&col=gd_pub_chrom_map&col=gd_pub_acc_ids&col=gd_pub_ensembl_id&col=gd_pub_refseq_ids&status=Approved&status=Entry+Withdrawn&status_opt=2&where=&order_by=gd_hgnc_id&format=text&limit=&hgnc_dbtag=on&submit=submit -P data/ -O hugo_gene_names.txt"
		def proc = wget.execute()
		proc.waitFor()
	}
	def nameMap = [:]
	def ensMap = [:]
	gFile.eachLine{ line ->
		def s = line.split("\t")
		nameMap."${s[1]}" = s[2]
		ensMap."${s[1]}" = s[8]
	}
	
	def gMap = [:]
	print "Downloading gencode annotation file"
	gFile = new File("data/gencode.v18.annotation.gtf")
	if (gFile.exists()){
		print "Already done"
	}else{ 
		def wget =  "wget ftp://ftp.sanger.ac.uk/pub/gencode/Gencode_human/release_18/gencode.v18.annotation.gtf.gz -P data/"
		def proc = wget.execute()
		proc.waitFor()
		print "Unzipping..."
		def gunzip = "gunzip data/gencode.v18.annotation.gtf.gz"
		proc = gunzip.execute()
		proc.waitFor()
	}
	
	gFile.eachLine{ line ->
		if ((matcher = line =~ /^chr.*/)){
			def s = line.split("\t")
			if ((matcher = s[2] =~ /gene/)){
				if ((matcher = s[8] =~ /protein_coding/)){
					count++
					gMap.chr = s[0]
					gMap.start = s[3]
					gMap.stop = s[4]
					def name
					if ((matcher = s[8] =~ /.*?transcript_name\s+"(.*?)";.*/)){
						name = matcher[0][1]
						gMap.name = name
						gMap.ensembl = ensMap."${name}"
					}
					gMap.strand = s[6]
					if (nameMap."${name}"){
						gMap.fullname = nameMap."${name}"
					}else{
						gMap.fullname = "n/a"
					}
			
					//print gMap
					Genes g = new Genes(gMap)
					if ((count % 1000) == 0){
						//print gMap
						g.save(flush:true)
						println new Date()
						cleanUpGorm()
						print count
					}else{
						g.save()
					}
				}
			}
		}
	}
	print count
}

def chipbase_gene(){
	def count = 0
	print "Adding chipbase gene data..."
	def cMap = [:]
	def gene
	def chip_file = new File("data/chipBase_Human_proteinTFBSs2013-12-18_13-17.filter.csv")
	chip_file.eachLine{ line ->
		def s = line.split(",")
		gene = s[1]
		cMap.tfname = s[0]
		//print cMap
		//print gene
		count++
		Genes g = Genes.findByName(gene)
		if (g !=null){
			ChipbaseGene chip = new ChipbaseGene (cMap)
			g.addToChipgene(chip)
			if ((count % 1000) == 0){
				g.save(flush:true)
				println new Date()
				cleanUpGorm()
				print count
			}else{
				g.save()
			}
		}
	}
	print count
}

def chipbase_mir(){
	def matcher
	def count = 0
	print "Adding chipbase miRNA data..."
	def cMap = [:]
	def mir
	def chip_file = new File("data/chipBase_Human_MirnaTFBSs2013-12-18_13-36.filter.xls")
	chip_file.eachLine{ line ->
		def s = line.split("\t")
		mir = s[1]
		cMap.tfname = s[0]
		//print cMap
		count++
		def mirSplit 
		if ((matcher = mir =~ /,/)){
			mirSplit = mir.split(",")
			mirSplit.each{
				Precursor p = Precursor.findByPreid(it)
				if (p !=null){
					ChipbaseMir chip = new ChipbaseMir (cMap)
					p.addToChipmir(chip)
					p.save()
				}
			}
		}else{
			Precursor p = Precursor.findByPreid(mir)
			if (p !=null){
				ChipbaseMir chip = new ChipbaseMir (cMap)
				p.addToChipmir(chip)
				if ((count % 1000) == 0){
					p.save(flush:true)
					println new Date()
					cleanUpGorm()
					print count
				}else{
					p.save()
				}
			}
		}
	}
	print count
}