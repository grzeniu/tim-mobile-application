package pl.wat.tim.mobile.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.core.content.FileProvider;
import okhttp3.ResponseBody;
import pl.wat.tim.mobile.BuildConfig;

public class FileUtil {
    public static void openReport(File file, Context context) {
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);

        if (type == null)
            type = "*/*";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);

        intent.setDataAndType(uri, type);

        context.startActivity(intent);
    }

    public static File saveToDisk(ResponseBody body, String filename) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + filename);

        try (InputStream is = body.byteStream(); OutputStream os = new FileOutputStream(file)) {

            byte data[] = new byte[4096];
            int count;
            while ((count = is.read(data)) != -1) {
                os.write(data, 0, count);
            }

            os.flush();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
