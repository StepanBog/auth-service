package ru.bogdanov.diplom.mapper.common;

import com.google.protobuf.BoolValue;
import com.google.protobuf.BoolValue.Builder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BoolValueMapperImpl implements BoolValueMapper {

    @Override
    public BoolValue mapToProto(Boolean value) {
        if ( value == null ) {
            return null;
        }

        Builder boolValue = BoolValue.newBuilder();

        boolValue.setValue( value );

        return boolValue.build();
    }
}
