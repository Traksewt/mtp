package mtp

pre()

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
					preMap.seq = seq
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
            	preMap.mirid = matcher[0][1]
            	preMap.acc = matcher[0][2]
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