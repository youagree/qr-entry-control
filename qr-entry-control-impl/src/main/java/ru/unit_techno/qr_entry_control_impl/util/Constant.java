
package ru.unit_techno.qr_entry_control_impl.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {
    public static final String REGEX = "(^[АВЕКМНОРСТУХ]\\d{3}(?<!000)[АВЕКМНОРСТУХ]{2}\\d{2,3})|(^[АВЕКМНОРСТУХ]{2}\\d{3}(?<!000)\\d{2,3})|(^[АВЕКМНОРСТУХ]{2}\\d{4}(?<!0000)\\d{2,3})|(^\\d{4}(?<!0000)[АВЕКМНОРСТУХ]{2}\\d{2,3})|(^[АВЕКМНОРСТУХ]{2}\\d{3}(?<!000)[АВЕКМНОРСТУХ]\\d{2,3})$";
    public static final String WORKDIR_PATH = System.getProperty("user.dir");
    public static final String QR_TEMP_DIR = WORKDIR_PATH + "/qrs";
    public static final String PATH_TO_QRS = QR_TEMP_DIR + "/";
}