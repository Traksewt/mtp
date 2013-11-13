package mtp

class DownloadController {

    def index() { }
    
    def gene_download = {
        def fileOut = new File(params.fileData)
        response.setHeader "Content-disposition", "attachment; filename="+params.fileName
        response.contentType = 'text/csv'
        response.outputStream << fileOut
        response.outputStream.flush()
    }
}
