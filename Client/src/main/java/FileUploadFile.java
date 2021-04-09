import java.io.File;
import java.io.Serializable;

public class FileUploadFile implements Serializable {
    private static final long serialVersionUID = 1L;
    private File file;// файл
    private String file_nf;// имя файла
    private int starPos;// начальная позиция
    private byte[] bytes;// байтовый массив файла
    private int endPos;// Конечная позиция

    public int getStarPos() {
        return starPos;
    }

    public void setStarPos(int starPos) {
        this.starPos = starPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFile_nf() {
        return file_nf;
    }

    public void setFile_md5(String file_md5) {
        this.file_nf = file_nf;
    }

}
