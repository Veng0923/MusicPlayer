package Bean;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Music {
    public String getName() {
        return name;
    }

    public String getSinger() {
        return singer;
    }

    public String getAlbum() {
        return album;
    }

    private String name;
    private String singer;
    private String size;
    private String album;

    public String getSize() {
        return size;
    }

    public String getDuration() {
        return duration;
    }

    private String duration;
    private Image image;
    private String path;
    public Image getImage() {
        return image;
    }

    public String getPath() {
        return path;
    }

    public Music(String path) throws IOException, ReadOnlyFileException, TagException, InvalidAudioFrameException, CannotReadException {
        this.path = path;
        MP3File mp3File = new MP3File(path);
        AbstractID3v2Tag id3v2Tag = mp3File.getID3v2Tag();
        name = format(id3v2Tag.frameMap.get("TIT2").toString());
        singer = format(id3v2Tag.frameMap.get("TPE1").toString());
        album = format(id3v2Tag.frameMap.get("TALB").toString()) ;
        initTime(mp3File);
        initSize(id3v2Tag);
        initImage(id3v2Tag,path);
    }

    private void initImage(AbstractID3v2Tag id3v2Tag,String path) throws IOException {
        AbstractID3v2Frame apic = (AbstractID3v2Frame) id3v2Tag.getFrame("APIC");
        FrameBodyAPIC body = (FrameBodyAPIC) apic.getBody();
        byte[] imageData = body.getImageData();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
        image = ImageIO.read(byteArrayInputStream);
        byteArrayInputStream.close();
    }
    private void initSize(AbstractID3v2Tag id3v2Tag) {
        int b = id3v2Tag.getSize();

        int m = b / 1024 / 8;
        int d = b % (1024*8);
        String dd = (d+"").substring(0,2);
        size = m+"."+dd+"M";
    }

    private void initTime(MP3File mp3File) {
        MP3AudioHeader mp3AudioHeader = mp3File.getMP3AudioHeader();
        duration = mp3AudioHeader.getTrackLengthAsString();
    }

    @Override
    public String toString() {
        System.out.println(name);
        System.out.println(singer);
        System.out.println(album);
        System.out.println(duration);
        System.out.println(size);
//        System.out.println(image.toString());
        return super.toString();
    }
    private String format(String string){
        String[] split = string.split("\"");
        string = split[1];
        return string;
    }
}
