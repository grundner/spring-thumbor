package biz.grundner.springframework.thumbor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.io.*;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stephan Grundner
 *
 * https://github.com/thumbor/thumbor/wiki/Running
 * https://thumbor.readthedocs.io/en/latest/image_loader.html
 * https://thumbor.readthedocs.io/en/latest/configuration.html
 */
public class ThumborRunner implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ThumborRunner.class);

    private Process process;
    private Integer exitValue;

    private int port = 8889;
    private String loggingLevel = "DEBUG";

    private OutputStream outputStream = System.out;

    private List<String> allowedSources = Collections.singletonList("localhost");
    private String secureKey;

    public Integer getExitValue() {
        return exitValue;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        checkIfRunning();

        this.port = port;
    }

    public String getLoggingLevel() {
        return loggingLevel;
    }

    public void setLoggingLevel(String loggingLevel) {
        checkIfRunning();

        this.loggingLevel = loggingLevel;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream must not be null");
        }

        this.outputStream = outputStream;
    }

    public List<String> getAllowedSources() {
        return allowedSources;
    }

    public void setAllowedSources(List<String> allowedSources) {
        checkIfRunning();

        this.allowedSources = allowedSources;
    }

    public String getSecureKey() {
        return secureKey;
    }

    public void setSecureKey(String secureKey) {
        checkIfRunning();

        this.secureKey = secureKey;
    }

    private void checkIfRunning() {
        if (process != null && process.isAlive()) {
            throw new IllegalStateException("Thumbor already running");
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        checkIfRunning();

        File configFile = File.createTempFile("thumbor", ".conf");
        configFile.deleteOnExit();
        try (FileOutputStream outputStream = new FileOutputStream(configFile);
             PrintStream printer = new PrintStream(outputStream)) {

            printer.println("LOADER = 'thumbor.loaders.http_loader'");

            if (allowedSources != null && !allowedSources.isEmpty()) {
                printer.printf("ALLOWED_SOURCES = [%s]\n", allowedSources.stream()
                        .map(it -> String.format("'%s'", it))
                        .collect(Collectors.joining(",")));
            }

            if (secureKey != null) {
                printer.printf("SECURITY_KEY = '%s'", secureKey);
            }
        }

        ProcessBuilder builder = new ProcessBuilder()
                .command("thumbor",
                "-p", Integer.toString(port),
                "-l", loggingLevel,
                "-c", configFile.toString());

        File directory = Paths.get(".").toRealPath().toFile();
        builder.directory(directory);
        builder.redirectErrorStream(true);

        process = builder.start();

        try (InputStreamReader reader = new InputStreamReader(process.getInputStream());
             BufferedReader buffer = new BufferedReader(reader);
             PrintStream printer = new PrintStream(outputStream)) {

            String line;
            while (process.isAlive() && (line = buffer.readLine()) != null) {
                printer.println(line);
                LOG.debug(line);
            }
        }

        exitValue = process.waitFor();
    }
}
