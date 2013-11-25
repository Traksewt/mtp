package mtp

class DownloadController {

    def index() { }
    
    def gene_download = {
        def fileOut = new File(params.fileData)
        //def fileOut = ['1','2','3']
        print "Creating download file "+params.fileName
        print "Data = "+fileOut
        response.setHeader "Content-disposition", "attachment; filename="+params.fileName
        response.contentType = 'text/csv'
        response.outputStream << fileOut
        response.outputStream.flush()
    }
}
