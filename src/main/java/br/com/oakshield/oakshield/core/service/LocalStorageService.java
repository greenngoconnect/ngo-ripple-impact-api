package br.com.oakshield.oakshield.core.service;

import br.com.oakshield.oakshield.config.LocalStorageProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LocalStorageService {
    private final Path root;

    public LocalStorageService(LocalStorageProperties props) {
        this.root = Path.of(props.getBaseDir()).resolve("policies");
        try { Files.createDirectories(this.root); } catch (IOException e) { throw new IllegalStateException(e); }
    }

    public String savePdf(byte[] pdf) {
        String filename = "policy-" + System.currentTimeMillis() + ".pdf";
        Path file = root.resolve(filename);
        try { Files.write(file, pdf); } catch (IOException e) { throw new IllegalStateException(e); }
        return "/files/policies/" + filename; // URL pública (ver passo 4)
    }
}
