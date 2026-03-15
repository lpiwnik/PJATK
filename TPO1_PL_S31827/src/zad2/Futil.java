package zad2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;


import static java.nio.file.StandardOpenOption.*;


public class Futil {
    public static void processDir(String dirName, String resultFileName) {

        try (FileChannel channel = FileChannel.open(Paths.get(resultFileName), CREATE, TRUNCATE_EXISTING, WRITE)) {

            Files.walkFileTree(Path.of(dirName), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String content =  Charset.forName("Cp1250")
                            .decode(
                                    ByteBuffer.wrap(Files.readAllBytes(file))
                            ).toString();

                    channel.write(StandardCharsets.UTF_8.encode(content + "\n"));

                    return FileVisitResult.CONTINUE;
                }
            });


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
