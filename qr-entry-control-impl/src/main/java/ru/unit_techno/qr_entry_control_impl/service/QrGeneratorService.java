
package ru.unit_techno.qr_entry_control_impl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unit_techno.qr_entry_control_impl.dto.service.QrObjectTemplateDto;
import ru.unit_techno.qr_entry_control_impl.dto.service.QrPictureObject;
import ru.unit_techno.qr_entry_control_impl.util.Constant;

import java.io.File;
import java.nio.file.Paths;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class QrGeneratorService {

    private final ObjectMapper mapper;

    @SneakyThrows
    @Transactional
    public QrPictureObject generateQrPictureObject (QrObjectTemplateDto qrObjectDto) {
        createDirectoryIfNotExist();
        String path = Constant.PATH_TO_QRS + qrObjectDto.getFullName().replace(" ", "") + System.currentTimeMillis() + ".png";
        String jsonObject = mapper.writeValueAsString(qrObjectDto);

        BitMatrix bitMatrix = new MultiFormatWriter().encode(jsonObject, BarcodeFormat.QR_CODE, 144, 144);
        MatrixToImageWriter.writeToPath(bitMatrix, "png", Paths.get(path));

        File qrPictureFile = new File(path);

        // Извлекаем байткод из файла-картинки и кодируем в Base64 и сохраняем в строку
        byte[] fileContent = FileUtils.readFileToByteArray(qrPictureFile);
        String encodedFile = Base64.getEncoder().encodeToString(fileContent);

        QrPictureObject qrPictureObject = new QrPictureObject();
        qrPictureObject.setFilePath(Paths.get(path).getFileName().toString());
        qrPictureObject.setQrImageInBase64(encodedFile);

        return qrPictureObject;
    }

    private void createDirectoryIfNotExist() {
        log.debug("start creating directory");
        File directory = new File(Constant.QR_TEMP_DIR);
        if (!directory.exists()) {
            boolean mkdir = directory.mkdir();
            if (!mkdir) {
                throw new RuntimeException();
            }
            log.info("directory temp success creating");
        } else {
            log.debug("directory temp is already exist");
        }
    }
}