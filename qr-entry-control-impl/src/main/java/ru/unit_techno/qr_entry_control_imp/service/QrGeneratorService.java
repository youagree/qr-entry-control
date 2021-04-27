
package ru.unit_techno.qr_entry_control_imp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import ru.unit_techno.qr_entry_control_imp.dto.service.QrObjectTemplateDto;
import ru.unit_techno.qr_entry_control_imp.dto.service.QrPictureObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class QrGeneratorService {
    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public QrPictureObject generateQrPictureObject (QrObjectTemplateDto qrObjectDto) {
        String path = "resources\\temp\\" + qrObjectDto.getName() + qrObjectDto.getSurname() + System.currentTimeMillis() + ".png";
        String jsonObject = mapper.writeValueAsString(qrObjectDto);

        BitMatrix bitMatrix = new MultiFormatWriter().encode(jsonObject, BarcodeFormat.QR_CODE, 144, 144);
        MatrixToImageWriter.writeToPath(bitMatrix, "png", Paths.get(path));

        File qrPictureFile = new File(path);

        // Извлекаем байткод из файла-картинки и кодируем в Base64 и сохраняем в строку
        byte [] fileContent = FileUtils.readFileToByteArray(qrPictureFile);
        String encodedFile = Base64.getEncoder().encodeToString(fileContent);

        QrPictureObject qrPictureObject = new QrPictureObject();
        qrPictureObject.setFilePath(path);
        qrPictureObject.setQrImageInBase64(encodedFile);

        return qrPictureObject;
    }
}