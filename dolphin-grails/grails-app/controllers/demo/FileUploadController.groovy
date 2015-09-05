package demo

class FileUploadController {

    def save = {

        String uploaded = request.inputStream.text
        println uploaded

        render text:uploaded

    }
}
