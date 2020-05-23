package ru.zoom4ikdan4ik.transformer;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.launchwrapper.IClassTransformer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Transformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        byte[] bytes = this.getPatch(name + ".class", transformedName.replace(".", "/") + ".class");

        return bytes == null ? basicClass : bytes;
    }

    public final byte[] getPatch(String name, String transformedName) {
        InputStream inputStreamName = this.getPatchedClass(name);
        InputStream inputStreamTransformedName = this.getPatchedClass(transformedName);

        return inputStreamName == null ? this.getBytesPatchedClass(inputStreamTransformedName, transformedName) : this.getBytesPatchedClass(inputStreamName, name);
    }

    public final InputStream getPatchedClass(String name) {
        URLClassLoader urlClassLoader = (URLClassLoader) Transformer.class.getClassLoader();
        URL[] urls = urlClassLoader.getURLs();

        for (URL url : urls)
            try {
                ZipFile zipFile = new ZipFile(new File(url.toURI()));
                ZipEntry zipEntry = zipFile.getEntry(name);

                if (zipEntry != null && zipFile.getEntry(Transformer.class.getName().replace(".", "/") + ".class") != null)
                    return zipFile.getInputStream(zipEntry);
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        return null;
    }

    public final byte[] getBytesPatchedClass(InputStream inputStream, String name) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[64];

        if (inputStream != null) {
            FMLLog.info("Patching %s...", name);

            while (true) {
                try {
                    int bytes = inputStream.read(buffer);

                    if (bytes < 0) {
                        inputStream.close();

                        FMLLog.info("Patched %s %d", name, byteArrayOutputStream.size());

                        return byteArrayOutputStream.toByteArray();
                    } else byteArrayOutputStream.write(buffer, 0, bytes);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        return null;
    }
}
