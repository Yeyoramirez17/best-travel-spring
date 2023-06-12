package com.apptravel.apitravel.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

public class BestTravelUtil {

    private static Random random = new Random();

    public static LocalDateTime getRandomSoon() {
        var randomHours = random.nextInt(5 - 2) + 2;
        var now = LocalDateTime.now();

        return now.plusHours(randomHours);
    }

    public static LocalDateTime getRandomLatter() {
        var randomHours = random.nextInt(12 - 6) + 2;
        var now = LocalDateTime.now();

        return now.plusHours(randomHours);
    }

    public static void writeNotification( String text, String path ) throws IOException {
        var fileWriter = new FileWriter( path, true );
        var bufferWrithe = new BufferedWriter( fileWriter );

        try( fileWriter; bufferWrithe ) {
            bufferWrithe.write( text );
            bufferWrithe.newLine();
        }
    }
}
