package com.zeydie.transformer;

import lombok.extern.java.Log;
import net.minecraft.launchwrapper.IClassTransformer;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Log
public final class Transformer implements IClassTransformer {
    @Override
    public byte[] transform(
            @NotNull String name,
            @NotNull String transformedName,
            byte[] basicClass
    ) {
        final byte[] nameBytes = this.getPatchedClass(name + ".class");

        if (nameBytes != null)
            return nameBytes;

        final byte[] transformedBytes = this.getPatchedClass(transformedName.replace(".", "/") + ".class");

        if (transformedBytes != null)
            return transformedBytes;

        return basicClass;
    }

    private byte[] getPatchedClass(@NotNull final String name) {
        final URLClassLoader urlClassLoader = (URLClassLoader) Transformer.class.getClassLoader();
        final URL[] urls = urlClassLoader.getURLs();

        for (final URL url : urls) {
            try {
                final File file = new File(url.toURI());

                try (final ZipFile zipFile = new ZipFile(file)) {
                    final ZipEntry zipEntry = zipFile.getEntry(name);

                    if (zipEntry != null) {
                        final ZipEntry entryClass = zipFile.getEntry(Transformer.class.getName().replace(".", "/") + ".class");

                        if (entryClass != null)
                            return this.getBytesPatchedClass(zipFile.getInputStream(zipEntry), name);
                    }
                }
            } catch (URISyntaxException | IOException exception) {
                exception.printStackTrace();
            }
        }

        return null;
    }

    private byte[] getBytesPatchedClass(
            @NotNull final InputStream inputStream,
            @NotNull final String name
    ) {
        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            log.info(String.format("Patching %s...", name));

            byte[] buffer = new byte[64];

            while (true) {
                try {
                    final int bytes = inputStream.read(buffer);

                    if (bytes < 0) {
                        log.info(String.format("Patched %s %d", name, byteArrayOutputStream.size()));

                        return byteArrayOutputStream.toByteArray();
                    } else byteArrayOutputStream.write(buffer, 0, bytes);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
