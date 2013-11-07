package mtp

pre()
mat()

def pre(){
	def preMap = [:]
	Precursor pre
	def count = 0;
	def seq = ""
	print "Downloading hairping.fa.gz"
	def h_file = new File("data/hairpin.fa")
	if (h_file.exists()){
		print "Already done"
	}else{ 
		def wget =  "wget ftp://mirbase.org/pub/mirbase/CURRENT/hairpin.fa.gz -P data/"
		def proc = wget.execute()
		proc.waitFor()
		print "Unzipping..."
		def gunzip = "gunzip data/hairpin.fa.gz"
		proc = gunzip.execute()
		proc.waitFor()
	}
	print "Adding to db..."
	def inseq = false
	h_file.eachLine{ line ->
		if ((matcher = line =~ /^>.*/)){
			if ((matcher = line =~ /(hsa-.*?)\s(.*?)\s.*/)){
				inseq = true
				if (seq != ""){
					preMap.preseq = seq
					pre = new Precursor(preMap)
					if ((count % 100) == 0){
						pre.save(flush:true)
						//print count
					}else{
						pre.save()
					}
					seq = ""
					count++
				}
            	preMap.preid = matcher[0][1]
            	preMap.preacc = matcher[0][2]
            }else{
            	inseq = false
            }  
        }else if (inseq == true){
        	seq <<= line
        }      
	}
	pre = new Precursor(preMap)
	pre.save(flush:true)
	print count+" miRNA precursors added"
}

def mat(){
	def matMap = [:]
	Mature mat
	Precursor pre
	def count = 0;
	def seq = ""
	print "Downloading mature.fa.gz"
	def h_file = new File("data/mature.fa")
	if (h_file.exists()){
		print "Already done"
	}else{ 
		def wget =  "wget ftp://mirbase.org/pub/mirbase/CURRENT/mature.fa.gz -P data/"
		def proc = wget.execute()
		proc.waitFor()
		print "Unzipping..."
		def gunzip = "gunzip data/mature.fa.gz"
		proc = gunzip.execute()
		proc.waitFor()
	}
	print "Adding to db..."
	def inseq = false
	h_file.eachLine{ line ->
		if ((matcher = line =~ /^>.*/)){
			if ((matcher = line =~ /(hsa-.*?)\s(.*?)\s.*/)){
				inseq = true

				def matid = matcher[0][1]
				def matacc = matcher[0][2]
				def preid
				if ((matcher = line =~ /(hsa-.*?-.*?)-.*?\s.*/)){
					preid = matcher[0][1]
				}else if ((matcher = line =~ /(hsa-.*?-.*?-.*?)-[3|5]p\s.*/)){
					preid = matcher[0][1]
				}
				print "matid ="+matid+" preid = "+preid
				pre = Precursor.findByPreid(preid)
				print "pre = "+pre
				if (seq != ""){
					matMap.preseq = seq
					mat = new Mature(matMap)
					pre.addToMat(mat)
					if ((count % 100) == 0){
						pre.save(flush:true)
						//print count
					}else{
						pre.save()
					}
					seq = ""
					count++
				}
				
            	matMap.matid = matid
            	matMap.matacc = matacc
            }else{
            	inseq = false
            }  
        }else if (inseq == true){
        	seq <<= line
        }      
	}
	mat = new Mature(matMap)
	pre = Precursor.findByPreid(preid)
	pre.save(flush:true)
	print count+" miRNA mature sequences added"
}