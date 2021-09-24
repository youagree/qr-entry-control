package ru.unit_techno.qr_entry_control_impl.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimestampDateSerializer extends StdSerializer<ZonedDateTime> {

    private static final long serialVersionUID = 1L;

    public TimestampDateSerializer(Class<ZonedDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(ZonedDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        ZonedDateTime localDateTime1 = localDateTime.toLocalDateTime().atZone(ZoneId.of("-03:00"));
        Timestamp timestamp = Timestamp.valueOf(localDateTime1.toLocalDateTime());
        jsonGenerator.writeObject(timestamp);

    }
}
